package junker.board.min_cover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import junker.animals.Animal;
import junker.board.AnimalBoardInstance;
import junker.board.Coords;
import junker.board.Game;
import junker.board.Tile;
import junker.board.probabiltiy.PermutationService;

import static junker.util.DoubleArrayUtil.allNotEmptyListsAreOfEqualLength;
import static junker.util.DoubleArrayUtil.filter;
import static junker.util.DoubleArrayUtil.filterByIndex;
import static junker.util.DoubleArrayUtil.filterListsInDoubleArray;

public class BoardCoverCalculator {

    public static Set<Solution> minCoveringSets(Game game, Animal animalToSearch, boolean forceFullSolution) {
        var overallOverlap = calculateOverallOverlap(game.getWipedBoard(), game.getContainedAnimals());
        var overlap = getAnimalOverlap(overallOverlap, animalToSearch);
        var highestOverlapCoords = getHighestOverlapCoords(overlap);

        if (highestOverlapCoords.size() <= 1 && !forceFullSolution)
            return Set.of(new Solution(new ArrayList<>(highestOverlapCoords), new Game(game, true)));

        var coveringSets = coveringSets(game, animalToSearch, overlap, highestOverlapCoords);
        return onlyMinSizedSets(coveringSets);
    }

    private static Set<Solution> onlyMinSizedSets(Set<Solution> coveringSolutions) {
        var coveringClicks = coveringSolutions.stream().map(Solution::clicks).collect(Collectors.toSet());
        int minSize = coveringClicks.stream().mapToInt(List::size).min().orElse(0);
        return coveringSolutions.stream().filter(sol -> sol.clicks().size() == minSize).collect(Collectors.toSet());
    }

    private static Set<Solution> coveringSets(Game game, Animal animalToSearch, List<Coords> prevCoords) {
        var overallOverlap = calculateOverallOverlap(game.getWipedBoard(), game.getContainedAnimals());
        var overlap = getAnimalOverlap(overallOverlap, animalToSearch);
        var highestOverlapCoords = getHighestOverlapCoords(overlap);


        var noOverlapSolutions = calculateNoOverlapSolutionsIfPresent(overlap, animalToSearch, highestOverlapCoords,
                game, prevCoords);
        if (noOverlapSolutions != null) {
            return noOverlapSolutions;
        }

        return clickHighestChanceCoordsAndStepInto(highestOverlapCoords, game, animalToSearch, overlap, prevCoords);
    }

    /**
     * if overlap etc. is already computed
     */
    private static Set<Solution> coveringSets(Game game, Animal animalToSearch,
                                              List<AnimalBoardInstance>[][] overlap, Set<Coords> highestOverlapCoords) {
        var overlapSolutions = calculateNoOverlapSolutionsIfPresent(overlap, animalToSearch, highestOverlapCoords,
                game, List.of());
        if (overlapSolutions != null) {
            return overlapSolutions;
        }

        return clickHighestChanceCoordsAndStepInto(highestOverlapCoords, game, animalToSearch, overlap, List.of());
    }

    private static Set<Solution> clickHighestChanceCoordsAndStepInto(Set<Coords> highestOverlapCoords, Game game,
                                                                     Animal animalToSearch,
                                                                     List<AnimalBoardInstance>[][] overlap, List<Coords> prevCoords) {

        Map<Coords, List<AnimalBoardInstance>> tilesWithHighestOverlaps = filterByIndex(overlap,
                highestOverlapCoords::contains);
        var multiClickCollection = new ArrayList<>(IndependentSetsCalculator.calculateMaxIndependentSubSets(tilesWithHighestOverlaps,
                AnimalBoardInstance::id));

        var allClonedGames = new ArrayList<List<Game>>();
        Set<Solution> foundSolutions = checkForMultiClickSolutions(multiClickCollection, game, animalToSearch,
                allClonedGames, prevCoords);
        if (!foundSolutions.isEmpty()) {
            return foundSolutions;
        }

        var overallResults = new HashSet<Solution>();
        for (int i = 0; i < multiClickCollection.size(); i++) {
            var multiClicks = multiClickCollection.get(i);
            var clonedGames = allClonedGames.get(i);
            for (var coords : multiClicks) {
                var fakedClickOrder = new ArrayList<>(prevCoords);
                fakedClickOrder.add(coords);
                fakedClickOrder.addAll(multiClicks.stream().filter(c -> !c.equals(coords)).toList());
                stepIntoNextDepth(clonedGames, fakedClickOrder, overallResults, animalToSearch);
            }
        }
        return overallResults;
    }

    private static Set<Solution> checkForMultiClickSolutions(ArrayList<Set<Coords>> multiClickCollection,
                                                             Game game, Animal animalToSearch,
                                                             ArrayList<List<Game>> allClonedGames, List<Coords> prevCoords) {
        var foundSolutions = new HashSet<Solution>();
        for (var multiClicks : multiClickCollection) {
            var clonedGames = new ArrayList<Game>(List.of(new Game(game, true)));
            var multiClickList = new ArrayList<>(multiClicks);
            for (var coords : multiClickList) {
                var newClonedGames = new ArrayList<Game>();
                var gamesWithNoSolution = new ArrayList<Game>();
                for (var clonedGame : clonedGames) {
                    var newOverallOverlap = calculateOverallOverlap(clonedGame.getBoard(), clonedGame.getContainedAnimals());
                    var animalsToPlace = getAnimalToPlace(newOverallOverlap, coords, animalToSearch);
                    ifAnimalToPlaceEmptyThrow(animalsToPlace, game, coords, multiClicks);
                    for (var animal : animalsToPlace) {
                        var newClonedGame = new Game(clonedGame, true);
                        newClonedGame.setTile(coords.x(), coords.y(), true, animal);
                        newClonedGames.add(newClonedGame);
                        if (!newClonedGame.isSolvedFor(animalToSearch)) {
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
                            return new Solution(fullClicks, clonedGame);
                        }).collect(Collectors.toSet()));
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
                                          Animal animalToSearch) {
        for (Game clonedGame : clonedGames) {
            var result = coveringSets(clonedGame, animalToSearch, prevCoords);
            overallResults.addAll(result);
        }
    }

    private static Set<Solution> calculateNoOverlapSolutionsIfPresent(List<AnimalBoardInstance>[][] overlap,
                                                                      Animal animalToSearch,
                                                                      Set<Coords> highestOverlapCoords, Game game, List<Coords> prevCoords) {
        var remainingUnOverlappedStartCoords = checkForNoOverlap(overlap);
        if (remainingUnOverlappedStartCoords != null) {
            return calcNoOverlapSolutions(remainingUnOverlappedStartCoords, animalToSearch,
                    highestOverlapCoords, game, prevCoords);
        }
        return null;
    }


    private static Set<Solution> calcNoOverlapSolutions(Map<Coords, List<Coords>> remainingUnOverlappedStartCoords,
                                                        Animal animalToSearch,
                                                        Set<Coords> highestOverlapCoords, Game game, List<Coords> prevCoords) {
        var remainingUndiscoveredCoords =
                new ArrayList<>(remainingUnOverlappedStartCoords.values().stream().map(List::getFirst).toList());
        var highOverlapClicks = new ArrayList<Coords>();
        var lowerOverlapClicks = new ArrayList<Coords>();
        remainingUndiscoveredCoords.forEach(coords -> {
            if (highestOverlapCoords.contains(coords)) {
                highOverlapClicks.add(coords);
            } else {
                lowerOverlapClicks.add(coords);
            }
        });

        var result = new HashSet<Solution>();
        for (var firstClick : highOverlapClicks) {
            var perm = new ArrayList<>(highOverlapClicks);
            perm.remove(firstClick);
            perm.addFirst(firstClick);
            perm.addAll(lowerOverlapClicks);

            var clonedGames = new ArrayList<Game>(List.of(new Game(game, true)));
            for (var click : perm) {
                var newClonedGames = new ArrayList<Game>();
                for (var clonedGame : clonedGames) {
                    var newOverallOverlap = calculateOverallOverlap(clonedGame.getBoard(), clonedGame.getContainedAnimals());
                    var animalsToPlace = getAnimalToPlace(newOverallOverlap, click, animalToSearch);
                    if (animalsToPlace.size() == 1 && Objects.equals(animalsToPlace.getFirst(), animalToSearch)) {
                        var solutionFirstClicks = perm.subList(0, perm.indexOf(click) + 1);
                        var clicks = new ArrayList<>(prevCoords);
                        clicks.addAll(solutionFirstClicks);
                        result.add(new Solution(clicks, clonedGame));
                        continue;
                    }
                    for (var animalToPlace : animalsToPlace) {
                        clonedGame.setTile(click.x(), click.y(), true, animalToPlace);
                        newClonedGames.add(new Game(clonedGame, true));
                    }
                }
                clonedGames = newClonedGames;
            }
        }
        return result;
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

    public static Set<AnimalBoardInstance> uniqueInstances(List<AnimalBoardInstance>[][] overlap) {
        Set<AnimalBoardInstance> uniqueInstances = new HashSet<>();
        for (int i = 0; i < overlap.length; i++) {
            for (int j = 0; j < overlap[i].length; j++) {
               uniqueInstances.addAll(overlap[i][j]);
            }
        }
        return uniqueInstances;
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

    public static List<AnimalBoardInstance>[][] getAnimalOverlap(List<AnimalBoardInstance>[][] overlap,
                                                                 Animal animalToSearch) {
        return filterListsInDoubleArray(overlap,
                animalInstance -> animalInstance != null && animalInstance.animal().equals(animalToSearch));
    }

    public static List<AnimalBoardInstance>[][] calculateOverallOverlap(Tile[][] wipedBoard,
                                                                 List<Animal> containedAnimals) {
        var boardPermutations = PermutationService.calculateBoardPermutations(wipedBoard, containedAnimals);
        List<AnimalBoardInstance>[][] overlap = new List[wipedBoard.length][wipedBoard[0].length];
        for (var permutation : boardPermutations) {
            for (int x = 0; x < wipedBoard.length; x++) {
                for (int y = 0; y < wipedBoard[0].length; y++) {
                    if (overlap[x][y] == null)
                        overlap[x][y] = new ArrayList<>();
                    if (wipedBoard[x][y].isRevealed())
                        continue;
                    overlap[x][y].add(permutation[x][y].getAnimalBoardInstance());
                }
            }
        }
        return overlap;
    }
}
