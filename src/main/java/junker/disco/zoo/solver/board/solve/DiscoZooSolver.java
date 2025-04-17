package junker.disco.zoo.solver.board.solve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sun.jdi.ClassObjectReference;
import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Click;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.solve.return_objects.DifferentAnimalPlacementReturnObject;
import junker.disco.zoo.solver.board.solve.speedup.SingularBoardCalcTracker;
import junker.disco.zoo.solver.board.util.ListUtil;
import junker.disco.zoo.solver.board.util.StatTracker;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.BestMoveInformation;
import junker.disco.zoo.solver.model.solver.Overlaps;
import junker.disco.zoo.solver.model.solver.Solution;

import static junker.disco.zoo.solver.board.solve.NoOverlapSolutionFinder.solutionsForNoOverlap;
import static junker.disco.zoo.solver.board.solve.OverlapCalulator.calculateOverlaps;
import static junker.disco.zoo.solver.board.solve.OverlapCalulator.emulateOverlapClick;
import static junker.disco.zoo.solver.board.solve.OverlapCalulator.findHighestOverlapCoords;

public class DiscoZooSolver {

    public static BestMoveInformation getBestMoveInformation(Animal animalToSolve, Game game) {
        var wipedGame = new Game(game, true);
        var overlaps = calculateOverlaps(wipedGame);
        var tracker = StatTracker.ofGame(game, animalToSolve);
        tracker.initialPermutationSize = overlaps.permutations().size();
        tracker.startTimer();
        var solutions = getBestSolutions(animalToSolve, wipedGame, overlaps, tracker);
        tracker.endTimer();
        tracker.printStats();
        return new BestMoveInformation(overlaps.animalOverlapProbability().get(animalToSolve), solutions);
    }

    public static BestMoveInformation getBuxProbability(Game game) {
        var wipedGame = new Game(game, true);
        var overlaps = calculateOverlaps(wipedGame);
        var nullProbs = overlaps.animalOverlapProbability().get(null);
        var pNullSum = 0.0d;
        for (int x = 0; x < game.getBoard().length; x++) {
            for (int y = 0; y < game.getBoard()[0].length; y++) {
                var pNull = nullProbs[x][y];
                pNullSum += pNull;
            }
        }
        var coordsWithHighestProb = new ArrayList<Coords>();
        var highestProb = Double.NEGATIVE_INFINITY;
        var buxProb = new Double[game.getBoard().length][game.getBoard()[0].length];
        for (int x = 0; x < game.getBoard().length; x++) {
            for (int y = 0; y < game.getBoard()[0].length; y++) {
                var pNull = nullProbs[x][y];
                var buxProbValue = pNull / pNullSum;
                buxProb[x][y] = buxProbValue;
                highestProb = ListUtil.resetAddIfAboveLimit(coordsWithHighestProb, List.of(new Coords(x, y)),
                        buxProbValue,
                        highestProb);
            }
        }
        final var highestProbValue = highestProb;
        return new BestMoveInformation(buxProb,
                coordsWithHighestProb.stream().map(coords -> new Solution(List.of(coords.toClick(highestProbValue)))).toList());
    }

    private static List<Solution> getBestSolutions(Animal animalToSolve, Game game, Overlaps overlaps, StatTracker tracker) {
        var clonedGame = new Game(game, true);
        var highestOverlapCoords = findHighestOverlapCoords(overlaps, animalToSolve, true);
        if (highestOverlapCoords.isEmpty())
            return List.of();
        else if (highestOverlapCoords.size() == 1) {
            return List.of(new Solution(List.of(highestOverlapCoords.getFirst().toMaxProbabilityClick())));
        }
        return emulateClicks(overlaps, animalToSolve, clonedGame, List.of(), highestOverlapCoords, Integer.MAX_VALUE,
                tracker, new SingularBoardCalcTracker());
    }

    private static List<Solution> emulateClicks(Overlaps overlaps, Animal animalToSolve, Game game,
                                                List<Click> previousClicks,
                                                List<Coords> highestOverlapCoords, int smallestSolutionLength,
                                                StatTracker tracker, SingularBoardCalcTracker singularBoardCalcTracker) {
        tracker.totalEmulateCalls++;
        var maxOverlapCount = overlaps.animalMaxOverlapCounts().get(animalToSolve);
        if (maxOverlapCount == null || maxOverlapCount == 0) {
            return List.of(new Solution(previousClicks));
        } else if (maxOverlapCount == 1) {
            var noOtherAnimalOverlaps = Arrays.stream(overlaps.overallOverlap())
                    .flatMap(Arrays::stream)
                    .filter(Objects::nonNull)
                    .filter(list -> list.parallelStream().filter(Objects::nonNull).anyMatch(instance -> Objects.equals(instance.animal(), animalToSolve)))
                    .allMatch(list ->
                            list.stream()
                                    .filter(Objects::nonNull)
                                    .map(instance -> instance.animal().name())
                                    .distinct().count() == 1);
            if (noOtherAnimalOverlaps)
                return solutionsForNoOverlap(overlaps, animalToSolve, game, previousClicks, smallestSolutionLength,
                        highestOverlapCoords);
        }

        var minClicksNeeded =
                (animalToSolve.pattern().size() - overlaps.animalRevealedTileCounts().getOrDefault(animalToSolve
                , 0) - 1) + (1-(int)Math.floor(overlaps.animalMinProbability().get(animalToSolve)));

        if (minClicksNeeded + previousClicks.size() > smallestSolutionLength) {
            return List.of(new Solution(IntStream.range(0, minClicksNeeded + previousClicks.size()).mapToObj(unused -> new Coords(-1, -1).toClick(-1)).toList()));
        }

        if (overlaps.animalMaxOverlapCounts().size() == 1) {
            var multipleClickSets = MultiClickEmulator.calculateMultiClickSets(overlaps,
                    highestOverlapCoords, animalToSolve);
            return emulateClicksForSingleAnimal(multipleClickSets, overlaps, game, previousClicks, animalToSolve,
                    smallestSolutionLength, tracker); // This is recursive back to this main function
        } else {
            return emulateClicksForMultipleAnimals(highestOverlapCoords, overlaps, game, previousClicks, animalToSolve,
                    smallestSolutionLength, tracker, singularBoardCalcTracker); // This is recursive back to this main function
        }
    }

    private static List<Solution> emulateClicksForMultipleAnimals(List<Coords> highestOverlapCoords, Overlaps overlaps,
                                                                  Game game, List<Click> previousClicks,
                                                                  Animal animalToSolve, int smallestSolutionLength,
                                                                  StatTracker tracker, SingularBoardCalcTracker singularBoardCalcTracker) {
        List<Solution> allSolutions = new ArrayList<>();
        Map<Coords, Double> expectedNextProbabilities = new HashMap<>();
        var results = new DifferentAnimalPlacementReturnObject();
        for (var coords : highestOverlapCoords) {
            Set<AnimalBoardInstance> animalInstances =
                    new HashSet<>(overlaps.overallOverlap()[coords.x()][coords.y()]);
            var placeableAnimals = animalInstances.stream()
                    .map(animalBoardInstance -> animalBoardInstance != null? animalBoardInstance.animal() : null)
                    .collect(Collectors.toSet());
            placeAllPossibleAnimalsAtCoordsAndAddSolutionsToLists(placeableAnimals, animalToSolve,
                    overlaps, animalInstances, coords, game, previousClicks, smallestSolutionLength, tracker, results
                    , singularBoardCalcTracker);

            expectedNextProbabilities.put(coords, results.expectedNextProbability);

            if (results.differentAnimalSolutions.isEmpty())
                continue;

            smallestSolutionLength = ListUtil.resetAddIfBelowLimit(allSolutions, results.differentAnimalSolutions,
                    results.differentAnimalSolutions.getFirst().clicks().size(),
                    smallestSolutionLength);
        }

        return filterSolutionsByOnlyBestExpectedProbabilities(allSolutions, expectedNextProbabilities,
                previousClicks.size());
    }

    private static void placeAllPossibleAnimalsAtCoordsAndAddSolutionsToLists
            (Set<Animal> placeableAnimals, Animal animalToSolve, Overlaps overlaps, Set<AnimalBoardInstance> animalInstances,
             Coords coords, Game game, List<Click> previousClicks, int smallestSolutionLength, StatTracker tracker,
             DifferentAnimalPlacementReturnObject results, SingularBoardCalcTracker singularBoardCalcTracker) {
        List<Solution> differentAnimalSolutions = new ArrayList<>();
        var worstSolutionLengthForDifferentAnimals = 0;
        var probabilitiesForDifferentAnimals = new HashMap<Animal, Double>();
        var nextProbabilitiesForDifferentAnimals = new HashMap<Animal, Double>();
        for (var animalToPlace : placeableAnimals) {
            var solutions = placeAnimalAndEmulateFurtherClicks(animalToPlace, animalToSolve, overlaps,
                    animalInstances, coords, game, previousClicks, smallestSolutionLength, tracker,
                    probabilitiesForDifferentAnimals, nextProbabilitiesForDifferentAnimals, singularBoardCalcTracker);
            if (solutions != null) {
                worstSolutionLengthForDifferentAnimals = ListUtil.resetAddIfAboveLimit(differentAnimalSolutions,
                        solutions,
                        solutions.getFirst().clicks().size(), worstSolutionLengthForDifferentAnimals);
            }
        }

        var expectedNextProbability =
                ExpectedValueCalculator.expectedNextProbability(probabilitiesForDifferentAnimals,
                        nextProbabilitiesForDifferentAnimals);

        if (previousClicks.isEmpty())
            expectedNextProbability *= probabilitiesForDifferentAnimals.get(animalToSolve);

        differentAnimalSolutions = modifyExpectedProbabilityOfSolutionsAtIndex(differentAnimalSolutions,
                previousClicks.size(), expectedNextProbability);

        results.differentAnimalSolutions = differentAnimalSolutions;
        results.expectedNextProbability = expectedNextProbability;
    }

    private static List<Solution> placeAnimalAndEmulateFurtherClicks(Animal animalToPlace,
                                                          Animal animalToSolve, Overlaps overlaps, Set<AnimalBoardInstance> animalInstances,
                                                          Coords coords, Game game, List<Click> previousClicks,
                                                          int smallestSolutionLength, StatTracker tracker,
                                                          Map<Animal, Double> probabilitiesForDifferentAnimals,
                                                          Map<Animal, Double> nextProbabilitiesForDifferentAnimals,
                                                         SingularBoardCalcTracker singularBoardCalcTracker) {
        var nextOverlaps = emulateOverlapClick(overlaps, animalToPlace, animalInstances, coords, game);
        var nextGame = new Game(game, true);
        nextGame.setTile(coords.x(), coords.y(), true, animalToPlace);

        var nextPreviousClicks = ListUtil.putLast(previousClicks, coords.toClick(-1));
        if (nextPreviousClicks.size() > smallestSolutionLength) {
            return null;
        }

        var potentialSolutions = singularBoardCalcTracker.getIfPresent(nextGame, animalToSolve, nextPreviousClicks, tracker);
        if (potentialSolutions != null) {
            ExpectedValueCalculator.mutateProbabilitiesForAnimal(animalToSolve, animalToPlace, overlaps, coords,
                    probabilitiesForDifferentAnimals, nextProbabilitiesForDifferentAnimals, nextOverlaps,
                    potentialSolutions, nextPreviousClicks.size());
            return potentialSolutions;
        }

        var nextHighestOverlapCoords = findHighestOverlapCoords(nextOverlaps, animalToSolve, false);

        var solutions = emulateClicks(nextOverlaps, animalToSolve, nextGame, nextPreviousClicks,
                nextHighestOverlapCoords, smallestSolutionLength, tracker, singularBoardCalcTracker);

        ExpectedValueCalculator.mutateProbabilitiesForAnimal(animalToSolve, animalToPlace, overlaps, coords,
                probabilitiesForDifferentAnimals, nextProbabilitiesForDifferentAnimals, nextOverlaps,
                solutions, nextPreviousClicks.size());

        // it is the worst solution compared to the other placeable animals.
        if (solutions.isEmpty()) {
            solutions = List.of(new Solution(IntStream.range(0,
                    game.getBoard().length * game.getBoard()[0].length).mapToObj(i -> new Coords(-1, -1).toClick(-1)).toList()));
        }

        singularBoardCalcTracker.add(nextGame, solutions, animalToSolve, nextPreviousClicks);

        solutions = onlyMinSolutions(solutions);
        return solutions;
    }

    private static List<Solution> modifyExpectedProbabilityOfSolutionsAtIndex(List<Solution> differentAnimalSolutions,
                                                                              int index,
                                                                              double expectedNextProbability) {
        var newDifferentAnimalSolutions = new ArrayList<Solution>();
        for (var solutionForAnimal : differentAnimalSolutions) {
            if (index < 0 || index >= solutionForAnimal.clicks().size())
                continue;
            var clickToChange = solutionForAnimal.clicks().get(index);
            var newClick = clickToChange.coords().toClick(expectedNextProbability);
            var newSolutionClicks = new ArrayList<Click>();
            for (int i = 0; i < solutionForAnimal.clicks().size(); i++) {
                if (i == index) {
                    newSolutionClicks.add(newClick);
                } else {
                    newSolutionClicks.add(solutionForAnimal.clicks().get(i));
                }
            }
            newDifferentAnimalSolutions.add(new Solution(newSolutionClicks));
        }
        return newDifferentAnimalSolutions;
    }

    private static List<Solution> filterSolutionsByOnlyBestExpectedProbabilities(List<Solution> solutions,
                                                                   Map<Coords, Double> expectedNextProbabilities,
                                                                                 int clickIndex) {
        var maxProbability = Double.NEGATIVE_INFINITY;
        var bestCoords = new ArrayList<Coords>();
        var occurringProbabilities =
                expectedNextProbabilities.entrySet().stream()
                        .filter(entry ->
                                solutions.stream()
                                        .anyMatch(solution -> solution.clicks().get(clickIndex).coords().equals(entry.getKey())))
                        .toList();
        for (var entry : occurringProbabilities) {
            maxProbability = ListUtil.resetAddIfAboveLimit(bestCoords, List.of(entry.getKey()), entry.getValue(), maxProbability);
        }
        return solutions.stream()
                        .filter(solution -> bestCoords.contains(solution.clicks().get(clickIndex).coords()))
                        .toList();

    }

    private static List<Solution> emulateClicksForSingleAnimal(Set<Set<Coords>> multipleClickSets, Overlaps overlaps,
                                                               Game game, List<Click> previousClicks,
                                                               Animal animalToSolve, int smallestSolutionLength,
                                                               StatTracker tracker) {
        var allSolutions = new ArrayList<Solution>();
        for (var multiClickSet : multipleClickSets) {
            var nextOverlaps = overlaps;
            var nextGame = game;
            List<Double> probabilitiesForMultiClickSet = new ArrayList<>();
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
                probabilitiesForMultiClickSet.add(nextOverlaps.animalOverlapProbability().get(animalToSolve)[click.x()][click.y()]);
                nextOverlaps = emulateOverlapClick(nextOverlaps, animalToPlace, animalInstances, click, nextGame);
                nextGame = new Game(nextGame, true);
                nextGame.setTile(click.x(), click.y(), true, animalToPlace);
            }
            var differentFirstSolutions = new ArrayList<Solution>();
            var worstSolutionLengthForDifferentAnimals = 0;

            for (var firstClickPermutation : ListUtil.permuteFirst(multiClickSet)) {
                var firstClickPermutationWithProbabilities = new ArrayList<Click>();
                for (int i = 0; i < firstClickPermutation.size(); i++) {
                    var click = firstClickPermutation.get(i);
                    var probability = probabilitiesForMultiClickSet.get(i);
                    firstClickPermutationWithProbabilities.add(click.toClick(probability));
                }
                var nextPreviousClicks = ListUtil.cloneThenAddAll(previousClicks, firstClickPermutationWithProbabilities);
                var highestOverlapCoords = findHighestOverlapCoords(nextOverlaps, animalToSolve, false);
                var multiClickSolutions =  emulateClicks(nextOverlaps, animalToSolve, nextGame, nextPreviousClicks,
                        highestOverlapCoords,
                        smallestSolutionLength, tracker, new SingularBoardCalcTracker());

                // it is the worst solution compared to the other placeable animals.
                if (multiClickSolutions.isEmpty())
                    multiClickSolutions = List.of(new Solution(IntStream.range(0,
                            game.getBoard().length * game.getBoard()[0].length + 1).mapToObj(i -> new Coords(-1, -1).toClick(-1)).toList()));

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
