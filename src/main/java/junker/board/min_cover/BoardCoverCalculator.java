package junker.board.min_cover;

import java.util.ArrayList;
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
import junker.util.DoubleArrayUtil;

import static junker.board.probabiltiy.PermutationService.getPermutationOfList;
import static junker.util.DoubleArrayUtil.filter;
import static junker.util.DoubleArrayUtil.filterByIndex;
import static junker.util.DoubleArrayUtil.filterListsInDoubleArray;

public class BoardCoverCalculator {

    public static Set<Solution> minCoveringSets(Game game, Animal animalToSearch) {
        var coveringSets = coveringSets(game, animalToSearch);
        var minSizedSets = onlyMinSizedSets(coveringSets);
        return onlyBestSolutions(minSizedSets, animalToSearch);
    }

    private static Set<Solution> onlyBestSolutions(Set<Solution> minSizedSets, Animal animalToSearch) {
        var maxSolvedTileCount = 0;
        var bestSolutions = new HashSet<Solution>();
        for (var solution : minSizedSets) {
            var solvedTileCount = getSolvedTileCount(solution, animalToSearch);
            if (solvedTileCount > maxSolvedTileCount) {
                maxSolvedTileCount = solvedTileCount;
            }
            if (solvedTileCount == maxSolvedTileCount) {
                bestSolutions.add(solution);
            }
        }
        return bestSolutions;
    }

    private static int getSolvedTileCount(Solution solution, Animal animalToSearch) {
        var game = solution.solvedGame();
        var overallOverlap = calculateOverallOverlap(game.getWipedBoard(), game.getContainedAnimals());
        var overlap = getAnimalOverlap(overallOverlap, animalToSearch);
        var maxOverlap = 0;
        for (int x = 0; x < overlap.length; x++) {
            for (int y = 0; y < overlap[0].length; y++) {
                var currentOverlap = new HashSet<>(overlap[x][y]).size();
                if (currentOverlap > maxOverlap) {
                    maxOverlap = currentOverlap;
                }
            }
        }
        if (maxOverlap == 0) {
            throw new IllegalStateException("Solution with no animal found? idk");
        }
        return animalToSearch.pattern().size() - maxOverlap + 1;

    }

    private static Set<Solution> onlyMinSizedSets(Set<Solution> coveringSolutions) {
        var coveringClicks = coveringSolutions.stream().map(Solution::clicks).collect(Collectors.toSet());
        int minSize = coveringClicks.stream().mapToInt(List::size).min().orElse(0);
        return coveringSolutions.stream().filter(sol -> sol.clicks().size() == minSize).collect(Collectors.toSet());
    }

    private static Set<Solution> coveringSets(Game game, Animal animalToSearch) {
        var overallOverlap = calculateOverallOverlap(game.getWipedBoard(), game.getContainedAnimals());
        var overlap = getAnimalOverlap(overallOverlap, animalToSearch);
        var highestOverlapCoords = getHighestOverlapCoords(overlap);


        var remainingUnOverlappedCoords = checkForNoOverlap(overlap);
        if (remainingUnOverlappedCoords != null) {
            return calcNoOverlapSolutions(remainingUnOverlappedCoords, animalToSearch,
                    highestOverlapCoords, game);
        }

        return clickHighestChanceCoordsAndStepInto(highestOverlapCoords, game, animalToSearch, overlap);
    }

    private static Set<Solution> clickHighestChanceCoordsAndStepInto(Set<Coords> highestOverlapCoords, Game game, Animal animalToSearch, List<AnimalBoardInstance>[][] overlap) {

        Map<Coords, List<AnimalBoardInstance>> tilesWithHighestOverlaps = filterByIndex(overlap,
                highestOverlapCoords::contains);
        var multiClickCollection = new ArrayList<>(IndependentSetsCalculator.calculateMaxIndependentSubSets(tilesWithHighestOverlaps,
                AnimalBoardInstance::id));

        var allClonedGames = new ArrayList<List<Game>>();
        Set<Solution> foundSolutions = new HashSet<>();
        for (var multiClicks : multiClickCollection) {
            var clonedGames = new ArrayList<Game>(List.of(new Game(game, true)));
            var multiClickList = new ArrayList<>(multiClicks);
            for (var coords : multiClickList) {
                var newClonedGames = new ArrayList<Game>();
                for (var clonedGame : clonedGames) {
                    var newOverallOverlap = calculateOverallOverlap(clonedGame.getBoard(), clonedGame.getContainedAnimals());
                    var animalsToPlace = getAnimalToPlace(newOverallOverlap, coords, animalToSearch);
                    if (animalsToPlace.size() == 1 && Objects.equals(animalsToPlace.getFirst(), animalToSearch)) {
                        var solutions = new ArrayList<List<Coords>>();
                        var solutionFirstClicks = multiClickList.subList(0, multiClickList.indexOf(coords) + 1);
                        for (var solutionFirstClick : solutionFirstClicks) {
                            var solutionPermutation = new ArrayList<>(List.of(solutionFirstClick));
                            solutionPermutation.addAll(multiClickList.stream().filter(c -> !c.equals(solutionFirstClick)).toList());
                            solutions.add(solutionPermutation);
                        }
                        foundSolutions.addAll(solutions.stream().map(clicks -> new Solution(clicks, clonedGame)).collect(Collectors.toSet()));
                        continue;
                    }
                    ifAnimalToPlaceEmptyThrow(animalsToPlace, game, coords, multiClicks);
                    for (var animal : animalsToPlace) {
                        var newClonedGame = new Game(clonedGame, true);
                        newClonedGame.setTile(coords.x(), coords.y(), true, animal);
                        newClonedGames.add(newClonedGame);
                    }
                }
                clonedGames = newClonedGames;
            }
            allClonedGames.add(clonedGames);
        }

        if (!foundSolutions.isEmpty()) {
            return foundSolutions;
        }

        var overallResults = new HashSet<Solution>();
        for (int i = 0; i < multiClickCollection.size(); i++) {
            var multiClicks = multiClickCollection.get(i);
            var clonedGames = allClonedGames.get(i);
            for (var coords : multiClicks) {
                var fakedClickOrder = new ArrayList<>(List.of(coords));
                fakedClickOrder.addAll(multiClicks.stream().filter(c -> !c.equals(coords)).toList());
                stepIntoNextDepth(clonedGames, fakedClickOrder, overallResults, animalToSearch);
            }
        }
        return overallResults;
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
            var result = coveringSets(clonedGame, animalToSearch);
            result = result.stream()
                    .filter(solution -> solution.clicks().size() < Game.MAX_ATTEMPTS)
                    .map(solution -> {
                        var newList = new ArrayList<>(prevCoords);
                        newList.addAll(solution.clicks());
                        return new Solution(newList, solution.solvedGame());
                    }).collect(Collectors.toSet());
            overallResults.addAll(result);
        }
    }


    private static Set<Solution> calcNoOverlapSolutions(List<Coords> remainingUnOverlappedCoords, Animal animalToSearch,
                                                        Set<Coords> highestOverlapCoords, Game game) {
        var result = new HashSet<Solution>();
        for(var coords : remainingUnOverlappedCoords) {
            var allTilesWithAnimal = animalToSearch.pattern()
                    .stream()
                    .map(coord -> new Coords(coords.x() + coord.x(), coords.y() + coord.y()))
                    .toList();
            for (Coords fullCoordinates : allTilesWithAnimal) {
                if (highestOverlapCoords.contains(fullCoordinates)) {
                    var solution = new ArrayList<>(remainingUnOverlappedCoords);
                    solution.remove(coords);
                    var endingPerms = getPermutationOfList(solution);
                    for (var perm : endingPerms) {
                        var clonedGames = new ArrayList<Game>(List.of(new Game(game, true)));
                        for (var click : perm) {
                            var newClonedGames = new ArrayList<Game>();
                            for (var clonedGame : clonedGames) {
                                var newOverallOverlap = calculateOverallOverlap(clonedGame.getBoard(), clonedGame.getContainedAnimals());
                                var animalsToPlace = getAnimalToPlace(newOverallOverlap, coords, animalToSearch);
                                if (animalsToPlace.size() == 1 && Objects.equals(animalsToPlace.getFirst(), animalToSearch)) {
                                    var solutions = new ArrayList<List<Coords>>();
                                    var solutionFirstClicks = perm.subList(0, perm.indexOf(coords) + 1);
                                    perm.addFirst(fullCoordinates);
                                    for (var solutionFirstClick : solutionFirstClicks) {
                                        var solutionPermutation = new ArrayList<>(List.of(solutionFirstClick));
                                        solutionPermutation.addAll(perm.stream().filter(c -> !c.equals(solutionFirstClick)).toList());
                                        solutions.add(solutionPermutation);
                                    }
                                    result.addAll(solutions.stream().map(clicks -> new Solution(clicks, clonedGame)).collect(Collectors.toSet()));
                                    continue;
                                }
                                for (var animalToPlace : animalsToPlace) {
                                    clonedGame.setTile(click.x(), click.y(), true, animalToPlace);
                                    newClonedGames.add(new Game(clonedGame, true));
                                }
                            }
                            clonedGames = newClonedGames;
                        }
                        result.addAll(clonedGames.stream().map(clonedGame -> new Solution(perm, clonedGame)).collect(Collectors.toSet()));
                    }
                }
            }
        }
        return result;
    }

    private static List<Coords> checkForNoOverlap(List<AnimalBoardInstance>[][] overlap) {
        var uniqueInstances = getUniqueInstances(overlap);
        Map<Coords, Set<AnimalBoardInstance>> uniqueOverlap = filter(uniqueInstances,
                set -> set.size() > 1);
        if (uniqueOverlap.isEmpty()) {
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

    private static List<Coords> getAllNonOverlapping(Set<AnimalBoardInstance>[][] uniqueInstances) {
        return filter(uniqueInstances, set -> set.size() == 1).values()
                .stream()
                .map(set -> set.iterator().next().origin())
                .distinct()
                .toList();
    }

    private static Set<AnimalBoardInstance>[][] getUniqueInstances(List<AnimalBoardInstance>[][] overlap) {
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

    private static List<AnimalBoardInstance>[][] getAnimalOverlap(List<AnimalBoardInstance>[][] overlap,
                                                              Animal animalToSearch) {
        return filterListsInDoubleArray(overlap,
                animalInstance -> animalInstance != null && animalInstance.animal().equals(animalToSearch));
    }

    private static List<AnimalBoardInstance>[][] calculateOverallOverlap(Tile[][] wipedBoard,
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
