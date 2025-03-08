package junker.disco.zoo.solver.board.min_cover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.board.probabiltiy.PermutationService;
import junker.disco.zoo.solver.model.calculations.AnimalOverlapAndProbabilities;
import junker.disco.zoo.solver.model.calculations.OverlapsAndProbabilities;

import static junker.disco.zoo.solver.util.DoubleArrayUtil.allNotEmptyListsAreOfEqualLength;
import static junker.disco.zoo.solver.util.DoubleArrayUtil.filter;
import static junker.disco.zoo.solver.util.DoubleArrayUtil.filterByIndex;

public class BoardCoverCalculator {

    public static Set<Solution> minCoveringSets(Game game, Animal animalToSearch, boolean forceFullSolution) {
        var overlapsAndProbabilities = calculateOverlapsAndProbabilities(game);
        var overlapAndProbabilities = overlapsAndProbabilities.animalOverlapAndProbabilities().get(animalToSearch);
        var overlap = overlapAndProbabilities.overlap();
        var highestOverlapCoords = getHighestOverlapCoords(overlap);

        if (highestOverlapCoords.size() <= 1 && !forceFullSolution)
            return Set.of(new Solution(new ArrayList<>(highestOverlapCoords), new Game(game, true)));

        var tracker = new MinSolutionTracker();
        var coveringSets = coveringSets(game, animalToSearch, overlap, highestOverlapCoords, tracker);
        System.out.printf("stopped calculating early for %.2f%% rejection rate%n", ((double) tracker.rejectionCounter / tracker.totalCounter) * 100);
        return onlyMinSizedSets(coveringSets);
    }

    public static AnimalOverlapAndProbabilities calculateOverlapAndProbabilities(Game game, Animal animalToSearch) {
        return calculateOverlapsAndProbabilities(game).animalOverlapAndProbabilities().get(animalToSearch);
    }

    public static OverlapsAndProbabilities calculateOverlapsAndProbabilities(Game game) {
        final var boardWidth = game.getBoard().length;
        final var boardHeight = game.getBoard()[0].length;

        var wipedBoard = game.calcWipedBoard();
        var containedAnimals = game.getContainedAnimals();
        var boardPermutations = PermutationService.calculateBoardPermutations(wipedBoard, containedAnimals);
        List<AnimalBoardInstance>[][] overallOverlap = new List[boardWidth][boardHeight];
        Map<Animal, List<AnimalBoardInstance>[][]> animalOverlaps = new HashMap<>();
        Map<Animal, int[][]> animalOverlapCounts = new HashMap<>();
        Map<Animal, Set<AnimalBoardInstance>> animalDistinctInstancesMap = new HashMap<>();
        for (var permutation : boardPermutations) {
            for (int x = 0; x < boardWidth; x++) {
                for (int y = 0; y < boardHeight; y++) {
                    if (overallOverlap[x][y] == null)
                        overallOverlap[x][y] = new ArrayList<>();
                    if (wipedBoard[x][y].isRevealed())
                        continue;
                    var animalBoardInstance = permutation[x][y].getAnimalBoardInstance();
                    overallOverlap[x][y].add(animalBoardInstance);
                    if (animalBoardInstance != null) {
                        var animal = animalBoardInstance.animal();

                        animalOverlaps.computeIfAbsent(animal, _ -> {
                            var animList = new List[boardWidth][boardHeight];
                            for (int i = 0; i < boardWidth; i++) {
                                for (int j = 0; j < boardHeight; j++) {
                                    animList[i][j] = new ArrayList<>();
                                }
                            }
                            return animList;
                        });
                        animalOverlapCounts.putIfAbsent(animal, new int[boardWidth][boardHeight]);

                        var animalOverlap = animalOverlaps.get(animal);
                        if (animalOverlap[x][y] == null)
                            animalOverlap[x][y] = new ArrayList<>();
                        animalOverlap[x][y].add(animalBoardInstance);

                        var animalOverlapCount = animalOverlapCounts.get(animal);
                        animalOverlapCount[x][y] += 1;

                        animalDistinctInstancesMap.putIfAbsent(animal, new HashSet<>());
                        animalDistinctInstancesMap.get(animal).add(animalBoardInstance);
                    }
                }
            }
        }
        Map<Animal, Double[][]> animalProbabilities = new HashMap<>();
        animalOverlaps.keySet().forEach(animal -> {
            var overlapCount = animalOverlapCounts.get(animal);
            var allDistinctAnimals = animalDistinctInstancesMap.get(animal);
            final var probabilities = new Double[boardWidth][boardHeight];
            for (int y = 0; y < boardWidth; y++) {
                for (int x = 0; x < boardHeight; x++) {
                    probabilities[x][y] = ((double) overlapCount[x][y]) / allDistinctAnimals.size();
                }
            }
            animalProbabilities.put(animal, probabilities);
        });
        var animalOverlapAndProbabilities = new HashMap<Animal, AnimalOverlapAndProbabilities>();

        animalOverlaps.keySet().forEach(animal -> animalOverlapAndProbabilities.put(animal, new AnimalOverlapAndProbabilities(animalOverlaps.get(animal),
                animalProbabilities.get(animal))));
        return new OverlapsAndProbabilities(overallOverlap, animalOverlapAndProbabilities);
    }

    private static Set<Solution> onlyMinSizedSets(Set<Solution> coveringSolutions) {
        var coveringClicks = coveringSolutions.stream().map(Solution::clicks).collect(Collectors.toSet());
        int minSize = coveringClicks.stream().mapToInt(List::size).min().orElse(0);
        return coveringSolutions.stream().filter(sol -> sol.clicks().size() == minSize).collect(Collectors.toSet());
    }

    private static Set<Solution> coveringSets(Game game, Animal animalToSearch, List<Coords> prevCoords, MinSolutionTracker tracker) {
        var overlapsAndProbabilities = calculateOverlapsAndProbabilities(game);
        var overlapAndProbabilities = overlapsAndProbabilities.animalOverlapAndProbabilities().get(animalToSearch);
        var overlap = overlapAndProbabilities.overlap();
        var highestOverlapCoords = getHighestOverlapCoords(overlap);


        var noOverlapSolutions = calculateNoOverlapSolutionsIfPresent(overlap, animalToSearch, highestOverlapCoords,
                game, prevCoords, tracker);
        if (noOverlapSolutions != null) {
            return noOverlapSolutions;
        }

        return clickHighestChanceCoordsAndStepInto(highestOverlapCoords, game, animalToSearch, overlap, prevCoords, tracker);
    }

    /**
     * if overlap etc. is already computed
     */
    private static Set<Solution> coveringSets(Game game, Animal animalToSearch,
                                              List<AnimalBoardInstance>[][] overlap, Set<Coords> highestOverlapCoords
            , MinSolutionTracker tracker) {
        var overlapSolutions = calculateNoOverlapSolutionsIfPresent(overlap, animalToSearch, highestOverlapCoords,
                game, List.of(), tracker);
        if (overlapSolutions != null) {
            return overlapSolutions;
        }

        return clickHighestChanceCoordsAndStepInto(highestOverlapCoords, game, animalToSearch, overlap, List.of(), tracker);
    }

    private static Set<Solution> clickHighestChanceCoordsAndStepInto(Set<Coords> highestOverlapCoords, Game game,
                                                                     Animal animalToSearch,
                                                                     List<AnimalBoardInstance>[][] overlap,
                                                                     List<Coords> prevCoords, MinSolutionTracker tracker) {

        Map<Coords, List<AnimalBoardInstance>> tilesWithHighestOverlaps = filterByIndex(overlap,
                highestOverlapCoords::contains);
        var multiClickCollection = new ArrayList<>(IndependentSetsCalculator.calculateMaxIndependentSubSets(tilesWithHighestOverlaps,
                AnimalBoardInstance::id));

        var allClonedGames = new ArrayList<List<Game>>();
        Set<Solution> foundSolutions = checkForMultiClickSolutions(multiClickCollection, game, animalToSearch,
                allClonedGames, prevCoords, tracker);
        if (!foundSolutions.isEmpty()) {
            return foundSolutions;
        }

        var overallResults = new HashSet<Solution>();
        for (int i = 0; i < multiClickCollection.size(); i++) {
            var multiClicks = multiClickCollection.get(i);
            var clonedGames = allClonedGames.get(i);
            if (clonedGames == null)
                continue;
            for (var coords : multiClicks) {
                var fakedClickOrder = new ArrayList<>(prevCoords);
                fakedClickOrder.add(coords);
                fakedClickOrder.addAll(multiClicks.stream().filter(c -> !c.equals(coords)).toList());
                stepIntoNextDepth(clonedGames, fakedClickOrder, overallResults, animalToSearch, tracker);
            }
        }
        return overallResults;
    }

    private static Set<Solution> checkForMultiClickSolutions(ArrayList<Set<Coords>> multiClickCollection,
                                                             Game game, Animal animalToSearch,
                                                             ArrayList<List<Game>> allClonedGames,
                                                             List<Coords> prevCoords, MinSolutionTracker tracker) {
        var foundSolutions = new HashSet<Solution>();
        for (var multiClicks : multiClickCollection) {
            tracker.totalCounter++;
            if (multiClicks.size() + prevCoords.size() > tracker.minSolutionSize) {
                tracker.rejectionCounter++;
                allClonedGames.add(null);
                continue;
            }
            var clonedGames = new ArrayList<Game>(List.of(new Game(game, true)));
            var multiClickList = new ArrayList<>(multiClicks);
            for (var coords : multiClickList) {
                var newClonedGames = new ArrayList<Game>();
                var gamesWithNoSolution = new ArrayList<Game>();
                for (var clonedGame : clonedGames) {
                    var overlapsAndProbabilities = calculateOverlapsAndProbabilities(clonedGame);
                    var newOverallOverlap = overlapsAndProbabilities.overallOverlap();
                    var animalsToPlace = getAnimalToPlace(newOverallOverlap, coords, animalToSearch);
                    ifAnimalToPlaceEmptyThrow(animalsToPlace, game, coords, multiClicks);
                    for (var animal : animalsToPlace) {
                        var newClonedGame = new Game(clonedGame, true);
                        newClonedGame.setTile(coords.x(), coords.y(), true, animal);
                        newClonedGames.add(newClonedGame);
                        if (!newClonedGame.animalIsCompletelyRevealed(animalToSearch)) {
                            gamesWithNoSolution.add(newClonedGame);
                        }
                    }
                }
                if (gamesWithNoSolution.isEmpty()) {
                    if (newClonedGames.isEmpty()) {
                        throw new IllegalStateException("No cloned games created in coverCalculation");
                    }
                    var solutions = new ArrayList<List<Coords>>();
                    var solutionFirstClicks = multiClickList.subList(0, multiClickList.indexOf(coords) + 1);
                    for (var solutionFirstClick : solutionFirstClicks) {
                        var solutionPermutation = new ArrayList<>(List.of(solutionFirstClick));
                        solutionPermutation.addAll(multiClickList.stream().filter(c -> !c.equals(solutionFirstClick)).toList());
                        solutions.add(solutionPermutation);
                    }
                    for (var clonedGame : newClonedGames)
                        foundSolutions.addAll(solutions.stream().map(clicks -> {
                            var fullClicks = new ArrayList<>(prevCoords);
                            fullClicks.addAll(clicks);
                            tracker.update(fullClicks.size());
                            return new Solution(fullClicks, clonedGame);
                        })
                        .collect(Collectors.toSet()));
                    continue;
                }
                clonedGames = newClonedGames;
            }
            allClonedGames.add(clonedGames);
        }
        return foundSolutions;
    }

    private static void ifAnimalToPlaceEmptyThrow(List<Animal> animalToPlace, Game game, Coords coords, Set<Coords> multiClicks) {
        if (animalToPlace.isEmpty()) {
            System.out.println("current multiclick: " + multiClicks);
            game.printGame();
            throw new IllegalStateException("No animal to place at " + coords + ". In coverCalculation");
        }
    }


    // TODO maybe make the list created in here return instead of modifying the overallResults
    private static void stepIntoNextDepth(List<Game> clonedGames, List<Coords> prevCoords,
                                          Set<Solution> overallResults,
                                          Animal animalToSearch, MinSolutionTracker tracker) {
        for (Game clonedGame : clonedGames) {
            var result = coveringSets(clonedGame, animalToSearch, prevCoords, tracker);
            overallResults.addAll(result);
        }
    }

    private static Set<Solution> calculateNoOverlapSolutionsIfPresent(List<AnimalBoardInstance>[][] overlap,
                                                                      Animal animalToSearch,
                                                                      Set<Coords> highestOverlapCoords, Game game,
                                                                      List<Coords> prevCoords, MinSolutionTracker tracker) {
        var remainingUnOverlappedStartCoords = checkForNoOverlap(overlap);
        if (remainingUnOverlappedStartCoords != null) {
            return calcNoOverlapSolutions(remainingUnOverlappedStartCoords, animalToSearch,
                    highestOverlapCoords, game, prevCoords, tracker);
        }
        return null;
    }


    private static Set<Solution> calcNoOverlapSolutions(Map<Coords, List<Coords>> remainingUnOverlappedStartCoords,
                                                        Animal animalToSearch,
                                                        Set<Coords> highestOverlapCoords, Game game, List<Coords> prevCoords
    , MinSolutionTracker tracker) {
        // all the lists in the remainingUnOverlappedStartCoords are of equal length
        // therefore we know we have to click at least all of them for one solution, then additionally we need to
        // click one tile of all the other unOverlappedCords to make sure it is not there
        final var clicksToSolution =
                new ArrayList<>(remainingUnOverlappedStartCoords.values()).getFirst().size() + remainingUnOverlappedStartCoords.keySet().size()-1;
        tracker.totalCounter++;
        if (prevCoords.size() + clicksToSolution > tracker.minSolutionSize) {
            tracker.rejectionCounter++;
            return Set.of();
        }
        final var allSolutions = new HashSet<Solution>();
        for (var finalClickCoordsEntry : remainingUnOverlappedStartCoords.entrySet()) {
            var finalClickCoords = finalClickCoordsEntry.getValue();
            var positionsThatWillBeClickedBefore = new ArrayList<>(remainingUnOverlappedStartCoords.keySet());
            positionsThatWillBeClickedBefore.remove(finalClickCoordsEntry.getKey());
            if (positionsThatWillBeClickedBefore.isEmpty()) {
                for (var firstFinalClick : finalClickCoords) {
                    var remainingFinalClicks = new ArrayList<>(finalClickCoords);
                    remainingFinalClicks.remove(firstFinalClick);

                    var solutionClicks = new ArrayList<Coords>();
                    solutionClicks.add(firstFinalClick);
                    solutionClicks.addAll(remainingFinalClicks);
                    allSolutions.add(clickThroughSolution(game, solutionClicks, animalToSearch, prevCoords));
                }
            }
            for (var positionThatWillBeClickedBefore : positionsThatWillBeClickedBefore) {
                var possibleFirstClicksOfFirstPosition = remainingUnOverlappedStartCoords.get(positionThatWillBeClickedBefore);
                for (var asFirstOfThePositionThatWillBeClickedFirst : possibleFirstClicksOfFirstPosition) {
                    var clicksBeforeTheLastClickButAfterTheFirst = new ArrayList<>(positionsThatWillBeClickedBefore);
                    clicksBeforeTheLastClickButAfterTheFirst.remove(positionThatWillBeClickedBefore);

                    var solutionClicks = new ArrayList<Coords>();
                    solutionClicks.add(asFirstOfThePositionThatWillBeClickedFirst);
                    solutionClicks.addAll(clicksBeforeTheLastClickButAfterTheFirst);
                    solutionClicks.addAll(finalClickCoords);
                    allSolutions.add(clickThroughSolution(game, solutionClicks, animalToSearch, prevCoords));
                }
            }
        }
        return allSolutions;
    }

    private static Solution clickThroughSolution(Game intialGame, List<Coords> clicks, Animal animalToSearch, List<Coords> prevCoords) {
        var solutionGame = new Game(intialGame, true);
        for (var click : clicks) {
            var overallOverlap = calculateOverlapsAndProbabilities(solutionGame).overallOverlap();
            var animalToPlace = getAnimalToPlace(overallOverlap, click, animalToSearch);
            solutionGame.setTile(click.x(), click.y(), true, animalToPlace.getFirst());
        }
        var solutionClicks = new ArrayList<>(prevCoords);
        solutionClicks.addAll(clicks);
        return new Solution(solutionClicks, solutionGame);
    }

    private static Map<Coords, List<Coords>> checkForNoOverlap(List<AnimalBoardInstance>[][] overlap) {
        var uniqueInstances = reduceToUniqueInstances(overlap);
        Map<Coords, Set<AnimalBoardInstance>> uniqueOverlap = filter(uniqueInstances,
                set -> set.size() > 1);
        // Check if all the overlaps have equal value
        if (uniqueOverlap.isEmpty() && allNotEmptyListsAreOfEqualLength(overlap)) {
            return getAllNonOverlapping(uniqueInstances);
        } else {
            return null;
        }
    }

    private static List<Animal> getAnimalToPlace(List<AnimalBoardInstance>[][] overallOverlap,
                                             Coords coords, Animal animalToSearch) {
        var possibleTileAnimalsAndNull =
                overallOverlap[coords.x()][coords.y()].stream().map(instance -> instance == null ? null :
                        instance.animal()).collect(Collectors.toSet());
        return getNecessaryAnimalToPlace(possibleTileAnimalsAndNull, animalToSearch);
    }

    private static List<Animal> getNecessaryAnimalToPlace(Set<Animal> possibleTileAnimalsAndNull,
                                                         Animal animalToSearch) {
        var list = new ArrayList<Animal>();
        if (possibleTileAnimalsAndNull.contains(null)) {
            list.add(null);
        } else {
            var nonQueriedAnimals = possibleTileAnimalsAndNull.stream().filter(animal -> !animal.equals(animalToSearch)).toList();
            if (nonQueriedAnimals.isEmpty()) {
                list.add(animalToSearch);
            }
            else {
                list.addAll(nonQueriedAnimals);
            }
        }
        return list;
    }

    private static Map<Coords, List<Coords>> getAllNonOverlapping(Set<AnimalBoardInstance>[][] uniqueInstances) {
        final Map<Coords, List<Coords>> result = new HashMap<>();
        filter(uniqueInstances, set -> set.size() == 1).entrySet()
                .stream()
                .forEach( entrySet ->{
                    var boardInstance = entrySet.getValue().iterator().next();
                    var coords = entrySet.getKey();

                    var origin = boardInstance.origin();
                    result.putIfAbsent(origin, new ArrayList<>());
                    result.get(origin).add(coords);
                });
        return result;
    }

    private static Set<AnimalBoardInstance>[][] reduceToUniqueInstances(List<AnimalBoardInstance>[][] overlap) {
        Set<AnimalBoardInstance>[][] result =  new Set[overlap.length][];
        for (int i = 0; i < overlap.length; i++) {
            result[i] = (Set<AnimalBoardInstance>[]) new Set[overlap[i].length];
            for (int j = 0; j < overlap[i].length; j++) {
                result[i][j] = new HashSet<>(overlap[i][j]);
            }
        }
        return result;
    }


    private static Set<Coords> getHighestOverlapCoords(List<AnimalBoardInstance>[][] overlap) {
        int highestOverlap = 0;
        List<Coords> highestOverlapCoords = new ArrayList<>();
        for (int x = 0; x < overlap.length; x++) {
            for (int y = 0; y < overlap[0].length; y++) {
                if (overlap[x][y].size() > highestOverlap) {
                    highestOverlap = overlap[x][y].size();
                    highestOverlapCoords.clear();
                    highestOverlapCoords.add(new Coords(x, y));
                } else if (overlap[x][y].size() == highestOverlap) {
                    highestOverlapCoords.add(new Coords(x, y));
                }
            }
        }
        return new HashSet<>(highestOverlapCoords);
    }


    private static class MinSolutionTracker {
        int minSolutionSize = Integer.MAX_VALUE;
        int totalCounter = 0;
        int rejectionCounter = 0;

        void update (int size) {
            if (size < minSolutionSize) {
                minSolutionSize = size;
            }
        }
    }
}
