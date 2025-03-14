package junker.disco.zoo.solver.board.probabiltiy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;
import junker.disco.zoo.solver.model.solver.Solution;

public class NoOverlapSolutionFinder {
    /**
     * This function assumes there is at most 1 animalBoardInstance per tile for the animalToSolve.
     * @param overlaps
     * @param animalToSolve
     * @param game
     * @param previousClicks
     * @return
     */
    public static List<Solution> solutionsForNoOverlap(Overlaps overlaps, Animal animalToSolve, Game game,
                                                  List<Coords> previousClicks, int smallestSolutionLength) {
        int boardWidth = game.getBoard().length;
        int boardHeight = game.getBoard()[0].length;
        var overallOverlap = overlaps.overallOverlap();

        // For each animalBoardInstance all the clickable Coords
        var animalBoardInstancesClickableCoordsMap = getClickableCoordsForAnimalBoardInstance(overallOverlap,
                animalToSolve, boardWidth, boardHeight);
        var minSolutionLength = minSolutionLength(animalBoardInstancesClickableCoordsMap, previousClicks);
        if (minSolutionLength > smallestSolutionLength)
            return List.of(new Solution(IntStream.range(0, minSolutionLength).mapToObj(i -> new Coords(-1, -1)).toList()));
        return allSolutionsForDifferentClickPermutations(animalBoardInstancesClickableCoordsMap, previousClicks);
    }

    private static Map<AnimalBoardInstance, Set<Coords>> getClickableCoordsForAnimalBoardInstance(List<AnimalBoardInstance>[][] overallOverlap, Animal animalToSolve, int boardWidth, int boardHeight) {
        // For each animalBoardInstance all the clickable Coords
        var animalBoardInstancesClickableCoordsMap = new HashMap<AnimalBoardInstance, Set<Coords>>();
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                var overallTileOverlap = overallOverlap[x][y]; // TODO add a overlaps map for each animal to have its
                // own overlap. So its precomputed

                // As this function assumes an overlap of 1 there only should be 1 animalBoardInstance. So its okay
                // to say findAny
                Optional<AnimalBoardInstance> optAnimalInstance = overallTileOverlap.stream()
                        .filter(Objects::nonNull)
                        .filter(instance -> instance.animal().equals(animalToSolve))
                        .findAny();
                if (optAnimalInstance.isPresent()) {
                    var animalInstance = optAnimalInstance.get();
                    animalBoardInstancesClickableCoordsMap.putIfAbsent(animalInstance, new HashSet<Coords>());
                    animalBoardInstancesClickableCoordsMap.get(animalInstance).add(new Coords(x, y));
                }
            }
        }
        return animalBoardInstancesClickableCoordsMap;
    }

    private static List<Solution> allSolutionsForDifferentClickPermutations(Map<AnimalBoardInstance, Set<Coords>> animalBoardInstancesClickableCoordsMap, List<Coords> previousClicks) {
        List<Solution> solutions = new ArrayList<>();
        for (var lastClickedInstanceToClickableCoords : animalBoardInstancesClickableCoordsMap.entrySet()) {
            var lastClickedInstance = lastClickedInstanceToClickableCoords.getKey();
            var lastClickableCoords = lastClickedInstanceToClickableCoords.getValue();

            var instancesToEliminateFirst = new HashMap<>(animalBoardInstancesClickableCoordsMap);
            instancesToEliminateFirst.remove(lastClickedInstance);
            var solutionClicks = new ArrayList<>(previousClicks);
            for (var clickableCoords : instancesToEliminateFirst.values()) {
                var anyClickableCoords = clickableCoords.iterator().next();
                solutionClicks.add(anyClickableCoords);
            }
            solutionClicks.addAll(lastClickableCoords);
            solutions.add(new Solution(solutionClicks));
        }
        return solutions;
    }

    private static int minSolutionLength(Map<AnimalBoardInstance, Set<Coords>> animalBoardInstancesClickableCoordsMap,
                                                   List<Coords> previousClicks) {
        var clicksToSolveSingleNoOverlap =
                animalBoardInstancesClickableCoordsMap.entrySet().iterator().next().getValue().size();
        var clicksToSolveRemainingNoOverlaps = animalBoardInstancesClickableCoordsMap.size() - 1;
        return previousClicks.size() + clicksToSolveSingleNoOverlap + clicksToSolveRemainingNoOverlaps;
    }
}
