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
import junker.board.probabiltiy.PermutationService;

import static junker.util.DoubleArrayUtil.arrayAsCoordinatesString;
import static junker.util.DoubleArrayUtil.cloneDoubleListArray;
import static junker.util.DoubleArrayUtil.filter;

public class BoardCoverCalculator {

    public static Set<Set<Coords>> coveringSets(Game game, Animal animalToSearch) {
        var overlap = calculateOverlap(game, animalToSearch);
        var coveringSets = coveringSets(overlap);
        return onlyMinSizedSets(coveringSets);
    }

    private static Set<Set<Coords>> onlyMinSizedSets(Set<Set<Coords>> coveringSets) {
        int minSize = coveringSets.stream().mapToInt(Set::size).min().orElse(0);
        return coveringSets.stream().filter(set -> set.size() == minSize).collect(Collectors.toSet());
    }

    private static Set<Set<Coords>> coveringSets(List<AnimalBoardInstance>[][] overlap) {

        var uniqueInstances = getUniqueInstances(overlap);
        Map<Coords, Set<AnimalBoardInstance>> uniqueOverlap = filter(uniqueInstances,
                set -> set.size() > 1);
        if (uniqueOverlap.isEmpty()) {
            var nonOverlappings = getAllNonOverlapping(uniqueInstances);
            return new HashSet<>(Set.of(nonOverlappings));
        }


        var highestOverlapCoords = getHighestOverlapCoords(overlap);
        var overallResults = new HashSet<Set<Coords>>();
        for (var coords : highestOverlapCoords) {
            var clonedOverlap = cloneDoubleListArray(overlap);
            removeOverlapAt(coords, clonedOverlap);
            var result = coveringSets(clonedOverlap);
            result.forEach(set -> set.add(coords));
            overallResults.addAll(result);
        }
        return overallResults;
    }

    private static Set<Coords> getAllNonOverlapping(Set<AnimalBoardInstance>[][] uniqueInstances) {
        return filter(uniqueInstances, set -> set.size() == 1).values()
                .stream()
                .map(set -> set.iterator().next().origin())
                .collect(Collectors.toSet());
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

    public static List<AnimalBoardInstance>[][] calculateOverlap(Game game, Animal animalToSearch) {
        var boardPermutations = PermutationService.calculateBoardPermutations(game.getWipedBoard(), game.getContainedAnimals());
        var board = game.getBoard();
        List<AnimalBoardInstance>[][] overlap = new List[board.length][board[0].length];
        for (var permutation : boardPermutations) {
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board[0].length; x++) {
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
