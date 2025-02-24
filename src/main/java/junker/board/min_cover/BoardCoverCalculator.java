package junker.board.min_cover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import junker.animals.Animal;
import junker.board.AnimalBoardInstance;
import junker.board.Coords;
import junker.board.Game;
import junker.board.Tile;
import junker.board.probabiltiy.PermutationService;

import static junker.util.DoubleArrayUtil.arrayAsCoordinatesString;
import static junker.util.DoubleArrayUtil.cloneDoubleListArray;
import static junker.util.DoubleArrayUtil.filter;

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
        var overlap = calculateOverlap(game.getWipedBoard(), game.getContainedAnimals(), animalToSearch);
        var uniqueInstances = getUniqueInstances(overlap);
        Map<Coords, Set<AnimalBoardInstance>> uniqueOverlap = filter(uniqueInstances,
                set -> set.size() > 1);
        if (uniqueOverlap.isEmpty()) {
            var nonOverlappings = getAllNonOverlapping(uniqueInstances);
            return new HashSet<>(Set.of(nonOverlappings));
        }


        var highestOverlapCoords = getHighestOverlapCoords(overlap);
        var overallResults = new HashSet<List<Coords>>();
        for (var coords : highestOverlapCoords) {
            var clonedGame = new Game(game, true);
            clonedGame.setTile(coords.x(), coords.y(), true, null); // TODO all possible animals and null

            var result = coveringSets(clonedGame, animalToSearch);
            result = result.stream()
                    .filter(list -> list.size() < Game.MAX_ATTEMPTS -1)
                    .map(list -> {
                var newList = new ArrayList<>(List.of(coords));
                newList.addAll(list);
                return newList;
            }).collect(Collectors.toSet());
            overallResults.addAll(result);
        }
        return overallResults;
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

    private static void removeOverlapAt(Coords coords, List<AnimalBoardInstance>[][] overlap) {
        var instances = overlap[coords.x()][coords.y()];
        overlap[coords.x()][coords.y()] = new ArrayList<>();
        for (var instance : instances) {
            for (int y = 0; y < overlap.length; y++) {
                for (int x = 0; x < overlap[0].length; x++) {
                    overlap[x][y].remove(instance);
                }
            }
        }
    }


    private static Set<Coords> getHighestOverlapCoords(List<AnimalBoardInstance>[][] overlap) {
        int highestOverlap = 0;
        List<Coords> highestOverlapCoords = new ArrayList<>();
        for (int y = 0; y < overlap.length; y++) {
            for (int x = 0; x < overlap[0].length; x++) {
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

    public static List<AnimalBoardInstance>[][] calculateOverlap(Tile[][] wipedBoard,
                                                                 List<Animal> containedAnimals, Animal animalToSearch) {
        var boardPermutations = PermutationService.calculateBoardPermutations(wipedBoard, containedAnimals);
        List<AnimalBoardInstance>[][] overlap = new List[wipedBoard.length][wipedBoard[0].length];
        System.out.println(boardPermutations.size());
        for (var permutation : boardPermutations) {
            for (int y = 0; y < wipedBoard.length; y++) {
                for (int x = 0; x < wipedBoard[0].length; x++) {
                    if (overlap[x][y] == null)
                        overlap[x][y] = new ArrayList<>();
                    if (permutation[x][y].hasAnimalInstanceOfType(animalToSearch)) {
                        overlap[x][y].add(permutation[x][y].getAnimalBoardInstance());
                    }
                }
            }
        }
        return overlap;
    }
}
