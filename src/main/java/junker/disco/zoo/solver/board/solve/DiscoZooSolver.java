package junker.disco.zoo.solver.board.solve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.BestMoveInformation;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;
import junker.disco.zoo.solver.model.solver.Solution;
import junker.disco.zoo.solver.board.util.ListUtil;

import static junker.disco.zoo.solver.board.solve.NoOverlapSolutionFinder.solutionsForNoOverlap;
import static junker.disco.zoo.solver.board.solve.OverlapCalulator.calculateOverlaps;
import static junker.disco.zoo.solver.board.solve.OverlapCalulator.emulateOverlapClick;
import static junker.disco.zoo.solver.board.solve.OverlapCalulator.findHighestOverlapCoords;

public class DiscoZooSolver {

    public static BestMoveInformation getBestMoveInformation(Animal animalToSolve, Game game) {
        var clonedGame = new Game(game, true);
        var overlaps = calculateOverlaps(clonedGame);
        var highestOverlapCoords = findHighestOverlapCoords(overlaps, animalToSolve);
        var solutions = emulateClicks(overlaps, animalToSolve, clonedGame, List.of(), highestOverlapCoords,
                Integer.MAX_VALUE);
        return new BestMoveInformation(overlaps.animalOverlapProbability().get(animalToSolve), solutions);
    }

    public static List<Solution> getBestSolutions(Animal animalToSolve, Game game) {
        var clonedGame = new Game(game, true);
        var overlaps = calculateOverlaps(clonedGame);
        var highestOverlapCoords = findHighestOverlapCoords(overlaps, animalToSolve);
        return emulateClicks(overlaps, animalToSolve, clonedGame, List.of(), highestOverlapCoords, Integer.MAX_VALUE);
    }

    private static List<Solution> emulateClicks(Overlaps overlaps, Animal animalToSolve, Game game,
                                                List<Coords> previousClicks,
                                                List<Coords> highestOverlapCoords, int smallestSolutionLength) {
        var maxOverlapCount = overlaps.animalMaxOverlapCounts().get(animalToSolve);
        if (maxOverlapCount == null || maxOverlapCount == 0) {
            return List.of(new Solution(previousClicks, Optional.of(new Game(game, true))));
        } else if (maxOverlapCount == 1) {
            return solutionsForNoOverlap(overlaps, animalToSolve, game, previousClicks, smallestSolutionLength, highestOverlapCoords);
        }
        if (highestOverlapCoords.isEmpty()) {
            return List.of(new Solution(previousClicks, Optional.of(new Game(game, true)))); // TODO not sure if this
            // is needed tbh, the first case
            // should
            // cover this imo
        }

        if (game.getNotCompletelyRevealedAnimals().size() == 1) { // TODO remove discobux from this logic
            var multipleClickSets = MultiClickEmulator.calculateMultiClickSets(overlaps,
                    highestOverlapCoords, animalToSolve);
            return emulateClicksForSingleAnimal(multipleClickSets, overlaps, game, previousClicks, animalToSolve,
                    smallestSolutionLength); // This is recursive back to this main function
        } else {
            return emulateClicksForMultipleAnimals(highestOverlapCoords, overlaps, game, previousClicks, animalToSolve,
                    smallestSolutionLength);
        }
    }

    private static List<Solution> emulateClicksForMultipleAnimals(List<Coords> highestOverlapCoords, Overlaps overlaps,
                                                                  Game game, List<Coords> previousClicks, Animal animalToSolve, int smallestSolutionLength) {
        var overallOverlap = overlaps.overallOverlap();
        List<Solution> allSolutions = new ArrayList<>();
        Map<Solution, Double> solutionProbabilities = new HashMap<>();
        for (var coords : highestOverlapCoords) {
            Set<AnimalBoardInstance> animalInstances =
                    new HashSet<>(overallOverlap[coords.x()][coords.y()]);
            var placeableAnimals = animalInstances.stream()
                    .map(animalBoardInstance -> animalBoardInstance != null? animalBoardInstance.animal() : null)
                    .collect(Collectors.toSet());
            var differentAnimalSolutions = new ArrayList<Solution>();
            Map<Solution, Double> differentAnimalSolutionProbabilities = new HashMap<>();
            var worstSolutionLengthForDifferentAnimals = 0;
            var worstProbabilityForDifferentAnimals = 1.0d;
            for (var animalToPlace : placeableAnimals) {
                var nextOverlaps = emulateOverlapClick(overlaps, animalToPlace, animalInstances, coords);
                var nextGame = new Game(game, true);
                nextGame.setTile(coords.x(), coords.y(), true, animalToPlace);

                var nextPreviousClicks = ListUtil.putLast(previousClicks, coords);
                if (nextPreviousClicks.size() > smallestSolutionLength) {
                    continue;
                }
                var nextHighestOverlapCoords = findHighestOverlapCoords(nextOverlaps, animalToSolve);
                var solutions = emulateClicks(nextOverlaps, animalToSolve, nextGame, nextPreviousClicks,
                        nextHighestOverlapCoords, smallestSolutionLength);
                // it is the worst solution compared to the other placeable animals.
                if (solutions.isEmpty()) {
                    solutions = List.of(new Solution(IntStream.range(0,
                            game.getBoard().length * game.getBoard()[0].length).mapToObj(i -> new Coords(-1, -1)).toList(), Optional.empty()));
                }

                solutions = onlyMinSolutions(solutions);

                var prevWorstSolutionLength = worstSolutionLengthForDifferentAnimals;
                worstSolutionLengthForDifferentAnimals = ListUtil.resetAddIfAboveLimit(differentAnimalSolutions,
                        solutions,
                        solutions.getFirst().clicks().size(), worstSolutionLengthForDifferentAnimals);
                // Get the best solutions of that animal click and check if
                for (Solution solution : solutions) {
                    solutionProbabilities.putIfAbsent(solution, 0.0);
                    if (!nextHighestOverlapCoords.isEmpty()) {
                        var anyHighestOverlap = nextHighestOverlapCoords.getFirst();
                        var probability =
                                nextOverlaps.animalOverlapProbability().get(animalToSolve)[anyHighestOverlap.x()][anyHighestOverlap.y()];
                        if (probability < worstProbabilityForDifferentAnimals - 0.0001 && prevWorstSolutionLength != worstSolutionLengthForDifferentAnimals) {
                            worstProbabilityForDifferentAnimals = probability;
                            differentAnimalSolutionProbabilities.clear();
                            differentAnimalSolutionProbabilities.put(solution, probability);
                        } else if (Math.abs(probability - worstProbabilityForDifferentAnimals) < 0.0001) {
                            differentAnimalSolutionProbabilities.put(solution, probability);
                        }
                    }
                }
                if (prevWorstSolutionLength != worstSolutionLengthForDifferentAnimals) {
                    var anyHighestOverlap = nextHighestOverlapCoords.getFirst();
                    final var probability = nextOverlaps.animalOverlapProbability().get(animalToSolve)[anyHighestOverlap.x()][anyHighestOverlap.y()];
                    worstProbabilityForDifferentAnimals = probability;
                    differentAnimalSolutionProbabilities.clear();
                    differentAnimalSolutions.forEach(solution -> differentAnimalSolutionProbabilities.put(solution, probability));
                }
                differentAnimalSolutions.removeIf(Predicate.not(differentAnimalSolutionProbabilities::containsKey));
            }
            if (differentAnimalSolutions.isEmpty())
                continue;
            solutionProbabilities.putAll(differentAnimalSolutionProbabilities);
            smallestSolutionLength = ListUtil.resetAddIfBelowLimit(allSolutions, differentAnimalSolutions,
                    differentAnimalSolutions.getFirst().clicks().size(),
                    smallestSolutionLength);
            var max = allSolutions.stream().map(solutionProbabilities::get).mapToDouble(dob -> dob).max().orElse(0);
            var newSolutionProbabilities = new HashMap<Solution, Double>();
            if (previousClicks.isEmpty()) {
                allSolutions =
                        new ArrayList<>(allSolutions.stream()
                                .filter(solution -> solutionProbabilities.get(solution) >= max - 0.0001)
                                .peek(solution -> newSolutionProbabilities.put(solution, solutionProbabilities.get(solution)))
                                .toList());
                solutionProbabilities.clear();
                solutionProbabilities.putAll(newSolutionProbabilities);
            }
        }
        return allSolutions;
    }

    private static List<Solution> emulateClicksForSingleAnimal(Set<Set<Coords>> multipleClickSets, Overlaps overlaps, Game game, List<Coords> previousClicks, Animal animalToSolve, int smallestSolutionLength) {
        var allSolutions = new ArrayList<Solution>();
        for (var multiClickSet : multipleClickSets) {
            var nextOverlaps = overlaps;
            var nextGame = game;
            for (var click : multiClickSet) {
                Set<AnimalBoardInstance> animalInstances =
                        new HashSet<>(nextOverlaps.overallOverlap()[click.x()][click.y()]);
                var placeableAnimals = animalInstances.stream()
                        .map(animalBoardInstance -> animalBoardInstance != null? animalBoardInstance.animal() : null)
                        .collect(Collectors.toSet());
                // These placeable Animals can only be null and the animal to solve
                // if there is only one animal to solve, we can just click on it
                Animal animalToPlace = null;
                if (placeableAnimals.size() == 1)
                    animalToPlace = placeableAnimals.iterator().next();
                nextOverlaps = emulateOverlapClick(nextOverlaps, animalToPlace, animalInstances, click);
                nextGame = new Game(nextGame, true);
                nextGame.setTile(click.x(), click.y(), true, animalToPlace);
            }
            var differentFirstSolutions = new ArrayList<Solution>();
            var worstSolutionLengthForDifferentAnimals = 0;
            for (var firstClickPermutation : ListUtil.permuteFirst(multiClickSet)) {
                var nextPreviousClicks = ListUtil.cloneThenAddAll(previousClicks, firstClickPermutation);
                var highestOverlapCoords = findHighestOverlapCoords(nextOverlaps, animalToSolve);
                var multiClickSolutions =  emulateClicks(nextOverlaps, animalToSolve, nextGame, nextPreviousClicks,
                        highestOverlapCoords,
                        smallestSolutionLength);

                // it is the worst solution compared to the other placeable animals.
                if (multiClickSolutions.isEmpty())
                    multiClickSolutions = List.of(new Solution(IntStream.range(0,
                            game.getBoard().length * game.getBoard()[0].length + 1).mapToObj(i -> new Coords(-1, -1)).toList(), Optional.empty()));

                multiClickSolutions = onlyMinSolutions(multiClickSolutions); // Get the best solutions of that animal click and check if

                worstSolutionLengthForDifferentAnimals = ListUtil.resetAddIfAboveLimit(differentFirstSolutions,
                        multiClickSolutions,
                        multiClickSolutions.getFirst().clicks().size(), worstSolutionLengthForDifferentAnimals);
            }

            smallestSolutionLength = ListUtil.resetAddIfBelowLimit(allSolutions, differentFirstSolutions,
                    differentFirstSolutions.getFirst().clicks().size(),
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
}
