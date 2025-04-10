package junker.disco.zoo.solver.board.solve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Click;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.util.ListUtil;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;
import junker.disco.zoo.solver.model.solver.Solution;

public class NoOverlapSolutionFinder {
    /**
     * This function assumes there is at most 1 animalBoardInstance per tile for the animalToSolve.
     */
    public static List<Solution> solutionsForNoOverlap(Overlaps overlaps, Animal animalToSolve, Game game,
                                                       List<Click> previousClicks, int smallestSolutionLength,
                                                       List<Coords> highestOverlapCoords) {
        int boardWidth = game.getBoard().length;
        int boardHeight = game.getBoard()[0].length;

        // For each animalBoardInstance all the clickable Coords
        var animalBoardInstancesClickableCoordsMap = getClickableCoordsForAnimalBoardInstance(overlaps,
                animalToSolve, boardWidth, boardHeight);
        var minSolutionLength = minSolutionLength(animalBoardInstancesClickableCoordsMap, previousClicks);
        if (minSolutionLength > smallestSolutionLength)
            return List.of(new Solution(IntStream.range(0, minSolutionLength).mapToObj(unused -> new Coords(-1, -1).toClick(-1)).toList()));
        return allSolutionsForDifferentClickPermutations(animalBoardInstancesClickableCoordsMap, previousClicks,
                highestOverlapCoords, overlaps.animalOverlapProbability().get(animalToSolve));
    }

    private static Map<AnimalBoardInstance, Set<Coords>> getClickableCoordsForAnimalBoardInstance(Overlaps overlaps,
                                                                                                  Animal animalToSolve,
                                                                                                  int boardWidth, int boardHeight) {
        // For each animalBoardInstance all the clickable Coords
        var animalBoardInstancesClickableCoordsMap = new HashMap<AnimalBoardInstance, Set<Coords>>();
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                var animalTileOverlap = overlaps.uniqueAnimalOverlapMap().get(animalToSolve)[x][y];
                if (!animalTileOverlap.isEmpty()) {
                    for (var animalInstance : animalTileOverlap) {
                        animalBoardInstancesClickableCoordsMap.putIfAbsent(animalInstance, new HashSet<>());
                        animalBoardInstancesClickableCoordsMap.get(animalInstance).add(new Coords(x, y));
                    }
                }
            }
        }
        return animalBoardInstancesClickableCoordsMap;
    }

    private static List<Solution> allSolutionsForDifferentClickPermutations(Map<AnimalBoardInstance, Set<Coords>> animalBoardInstancesClickableCoordsMap,
                                                                            List<Click> previousClicks,
                                                                            List<Coords> highestOverlapCoords,
                                                                            Double[][] animalProbabilities) {
        List<Solution> solutions = new ArrayList<>();
        for (var lastClickedInstanceToClickableCoords : animalBoardInstancesClickableCoordsMap.entrySet()) {
            var lastClickedInstance = lastClickedInstanceToClickableCoords.getKey();
            var lastClickableCoords = lastClickedInstanceToClickableCoords.getValue();

            var instancesToEliminateBefore = new HashMap<>(animalBoardInstancesClickableCoordsMap);
            instancesToEliminateBefore.remove(lastClickedInstance);

            if (instancesToEliminateBefore.isEmpty()) {
                var permutedSolutionClicks =
                        ListUtil.permuteFirst(lastClickableCoords).stream().map(coords -> {
                            var appendedPrevClicks = new ArrayList<>(previousClicks);
                            appendedPrevClicks.addAll(coords.stream().map(Coords::toMaxProbabilityClick).toList());
                            return new Solution(appendedPrevClicks);
                        }).toList();
                solutions.addAll(permutedSolutionClicks);
                return solutions;
            }

            for (int i = 0; i < instancesToEliminateBefore.size(); i++) {
                var firstClickableCoordAnimalInstance = (AnimalBoardInstance) instancesToEliminateBefore.keySet().toArray()[i];
                if (instancesToEliminateBefore.get(firstClickableCoordAnimalInstance).stream().noneMatch(highestOverlapCoords::contains)) {
                    continue;
                }
                var instancesToEliminateInBetween = new HashMap<>(instancesToEliminateBefore);
                instancesToEliminateInBetween.remove(firstClickableCoordAnimalInstance);
                var clickableCoordPermutations =
                        ListUtil.permuteFirst(instancesToEliminateBefore.get(firstClickableCoordAnimalInstance));
                for (var clickableCoordPermutation : clickableCoordPermutations) {
                    var solutionClicks = new ArrayList<>(previousClicks);
                    solutionClicks.add(clickableCoordPermutation.getFirst().toClickWithSize(animalBoardInstancesClickableCoordsMap.size()));
                    var anyClickableCoordsInBetween =
                            instancesToEliminateInBetween.values().stream().map(instances -> instances.iterator().next()).toList();
                    for (int j = 0; j < anyClickableCoordsInBetween.size(); j++) {
                        var size = animalBoardInstancesClickableCoordsMap.size()-1-j;
                        solutionClicks.add(anyClickableCoordsInBetween.get(j).toClickWithSize(size));
                    }
                    solutionClicks.addAll(lastClickableCoords.stream().map(Coords::toMaxProbabilityClick).toList());
                    solutions.add(new Solution(solutionClicks));
                }
            }
        }
        return solutions;
    }

    private static int minSolutionLength(Map<AnimalBoardInstance, Set<Coords>> animalBoardInstancesClickableCoordsMap,
                                                   List<Click> previousClicks) {
        var clicksToSolveSingleNoOverlap =
                animalBoardInstancesClickableCoordsMap.entrySet().iterator().next().getValue().size();
        var clicksToSolveRemainingNoOverlaps = animalBoardInstancesClickableCoordsMap.size() - 1;
        return previousClicks.size() + clicksToSolveSingleNoOverlap + clicksToSolveRemainingNoOverlaps;
    }
}
