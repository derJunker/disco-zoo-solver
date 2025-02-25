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

import static junker.util.DoubleArrayUtil.filter;
import static junker.util.DoubleArrayUtil.filterByIndex;
import static junker.util.DoubleArrayUtil.filterListsInDoubleArray;

public class BoardCoverCalculator {

    public static Set<List<Coords>> minCoveringSets(Game game, Animal animalToSearch) {
        var coveringSets = coveringSets(game, animalToSearch);
        return onlyMinSizedSets(coveringSets);
    }

    private static Set<List<Coords>> onlyMinSizedSets(Set<List<Coords>> coveringSets) {
        int minSize = coveringSets.stream().mapToInt(List::size).min().orElse(0);
        return coveringSets.stream().filter(list -> list.size() == minSize).collect(Collectors.toSet());
    }

    private static Set<List<Coords>> coveringSets(Game game, Animal animalToSearch) {
        var overallOverlap = calculateOverallOverlap(game.getWipedBoard(), game.getContainedAnimals());
        var overlap = getAnimalOverlap(overallOverlap, animalToSearch);
        var highestOverlapCoords = getHighestOverlapCoords(overlap);

        var remainingUnOverlappedCoords = checkForNoOverlap(overlap);
        if (remainingUnOverlappedCoords != null) {
            return calcNoOverlapSolutions(remainingUnOverlappedCoords, animalToSearch,
                    highestOverlapCoords);
        }

        return clickHighestChanceCoordsAndStepInto(highestOverlapCoords, game, overallOverlap, animalToSearch, overlap);
    }

    private static Set<List<Coords>> clickHighestChanceCoordsAndStepInto(Set<Coords> highestOverlapCoords, Game game,
                                                                         List<AnimalBoardInstance>[][] overallOverlap
            , Animal animalToSearch, List<AnimalBoardInstance>[][] overlap) {

        Map<Coords, List<AnimalBoardInstance>> tilesWithHighestOverlaps = filterByIndex(overlap,
                highestOverlapCoords::contains);
        var multiClickCollection = new ArrayList<>(IndependentSetsCalculator.calculateMaxIndependentSubSets(tilesWithHighestOverlaps,
                AnimalBoardInstance::id));

        var allClonedGames = new ArrayList<List<Game>>();
        Set<List<Coords>> foundSolutions = new HashSet<>();
        for (var multiClicks : multiClickCollection) {
            var clonedGames = new ArrayList<Game>(List.of(new Game(game, true)));
            var multiClickList = new ArrayList<>(multiClicks);
            for (var coords : multiClickList) {
                var newClonedGames = new ArrayList<Game>();
                for (var clonedGame : clonedGames) {
                    var newOverallOverlap = calculateOverallOverlap(clonedGame.getBoard(), clonedGame.getContainedAnimals());
                    var animalToPlace = getAnimalToPlace(newOverallOverlap, coords, animalToSearch);
                    if (animalToPlace.size() == 1 && Objects.equals(animalToPlace.getFirst(), animalToSearch)) {
                        var solutions = new ArrayList<List<Coords>>();
                        var solutionFirstClicks = multiClickList.subList(0, multiClickList.indexOf(coords) + 1);
                        for (var solutionFirstClick : solutionFirstClicks) {
                            var solutionPermutation = new ArrayList<>(List.of(solutionFirstClick));
                            solutionPermutation.addAll(multiClickList.stream().filter(c -> !c.equals(solutionFirstClick)).toList());
                            solutions.add(solutionPermutation);
                        }
                        foundSolutions.addAll(solutions);
                        continue;
                    }
                    ifAnimalToPlaceEmptyThrow(animalToPlace, game, coords, multiClicks);
                    for (var animal : animalToPlace) {
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

        var overallResults = new HashSet<List<Coords>>();
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
                                          HashSet<List<Coords>> overallResults,
                                          Animal animalToSearch) {
        for (Game clonedGame : clonedGames) {
//            clonedGame.printGame();
            var result = coveringSets(clonedGame, animalToSearch);
            result = result.stream()
                    .filter(list -> list.size() < Game.MAX_ATTEMPTS)
                    .map(list -> {
                        var newList = new ArrayList<>(prevCoords);
                        newList.addAll(list);
                        return newList;
                    }).collect(Collectors.toSet());
            overallResults.addAll(result);
        }
    }


    private static Set<List<Coords>> calcNoOverlapSolutions(List<Coords> remainingUnOverlappedCoords, Animal animalToSearch, Set<Coords> highestOverlapCoords) {
        var result = new HashSet<List<Coords>>();
        for(var coords : remainingUnOverlappedCoords) {
            var allTilesWithAnimal = animalToSearch.pattern()
                    .stream()
                    .map(coord -> new Coords(coords.x() + coord.x(), coords.y() + coord.y()))
                    .toList();
            for (Coords fullCoordinates : allTilesWithAnimal) {
                if (highestOverlapCoords.contains(fullCoordinates)) {
                    var solution = new ArrayList<>(remainingUnOverlappedCoords);
                    solution.remove(coords);
                    solution.addFirst(fullCoordinates);
                    result.add(solution);
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
