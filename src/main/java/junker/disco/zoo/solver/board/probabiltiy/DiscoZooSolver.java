package junker.disco.zoo.solver.board.probabiltiy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;
import junker.disco.zoo.solver.model.solver.Solution;
import junker.disco.zoo.solver.util.ListUtil;

import static junker.disco.zoo.solver.board.probabiltiy.NoOverlapSolutionFinder.solutionsForNoOverlap;
import static junker.disco.zoo.solver.board.probabiltiy.OverlapCalulator.calculateOverlaps;
import static junker.disco.zoo.solver.board.probabiltiy.OverlapCalulator.emulateOverlapClick;
import static junker.disco.zoo.solver.board.probabiltiy.OverlapCalulator.findHighestOverlapCoords;
import static junker.disco.zoo.solver.board.probabiltiy.OverlapCalulator.getBestReducingRemainingAnimalOverlapCoords;

public class DiscoZooSolver {
    public static List<Solution> getBestSolutions(Animal animalToSolve, Game game) {
        var clonedGame = new Game(game, true);
        var overlaps = calculateOverlaps(clonedGame);
        return emulateClicks(overlaps, animalToSolve, clonedGame, List.of(), Integer.MAX_VALUE);
    }

    private static List<Solution> emulateClicks(Overlaps overlaps, Animal animalToSolve, Game game,
                                                List<Coords> previousClicks, int smallestSolutionLength) {
        var maxOverlapCount = overlaps.animalOverlapCounts().get(animalToSolve);
        if (maxOverlapCount == null || maxOverlapCount == 0) {
            return List.of(new Solution(previousClicks));
        } else if (maxOverlapCount == 1) {
            return solutionsForNoOverlap(overlaps, animalToSolve, game, previousClicks, smallestSolutionLength);
        }
        var highestOverlapCoords = findHighestOverlapCoords(overlaps, animalToSolve);
        if (highestOverlapCoords.isEmpty()) {
            return List.of(new Solution(previousClicks));
        }
        if (game.getContainedAnimals().size() >= 2) // TODO AND is not a discobux
            highestOverlapCoords = getBestReducingRemainingAnimalOverlapCoords(highestOverlapCoords, overlaps, animalToSolve);
        var overallOverlap = overlaps.overallOverlap();
        var allSolutions = new ArrayList<Solution>();
        for (var coords : highestOverlapCoords) {
            Set<AnimalBoardInstance> animalInstances =
                    new HashSet<>(overallOverlap[coords.x()][coords.y()]);
            var placeableAnimals = animalInstances.stream()
                    .map(animalBoardInstance -> animalBoardInstance != null? animalBoardInstance.animal() : null)
                    .collect(Collectors.toSet());
            var differentAnimalSolutions = new ArrayList<Solution>();
            var worstSolutionLengthForDifferentAnimals = 0;
            for (var animalToPlace : placeableAnimals) {
                var nextOverlaps = emulateOverlapClick(overlaps, animalToPlace, animalInstances, coords);
                var nextGame = new Game(game, true);
                nextGame.setTile(coords.x(), coords.y(), true, animalToPlace);

                var nextPreviousClicks = ListUtil.putLast(previousClicks, coords);
                if (nextPreviousClicks.size() > smallestSolutionLength) {
                    continue;
                }
                var solutions = emulateClicks(nextOverlaps, animalToSolve, nextGame, nextPreviousClicks, smallestSolutionLength);
                // it is the worst solution compared to the other placeable animals.
                if (solutions.isEmpty())
                    continue;

                solutions = onlyMinSolutions(solutions); // Get the best solutions of that animal click and check if
                worstSolutionLengthForDifferentAnimals = ListUtil.resetAddIfAboveLimit(differentAnimalSolutions,
                        solutions,
                        solutions.getFirst().clicks().size(), worstSolutionLengthForDifferentAnimals);
            }
            if (differentAnimalSolutions.isEmpty())
                continue;
            smallestSolutionLength = ListUtil.resetAddIfBelowLimit(allSolutions, differentAnimalSolutions,
                    differentAnimalSolutions.getFirst().clicks().size(),
                    smallestSolutionLength);
        }
        return allSolutions;
    }

    private static List<Solution> onlyMinSolutions(List<Solution> solutions) {
        var minOptSolutions = solutions.stream().mapToInt(solution -> solution.clicks().size()).min();
        if (minOptSolutions.isEmpty())
            return solutions;
        var minSolutionLength = minOptSolutions.getAsInt();
        var minSolution = new ArrayList<Solution>();
        for (var solution : solutions) {
            if (solution.clicks().size() == minSolutionLength)
                minSolution.add(solution);
        }
        return minSolution;
    }

    // TODO this can be done inside the calculate Overlaps

}
