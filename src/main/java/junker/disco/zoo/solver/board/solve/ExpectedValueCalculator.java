package junker.disco.zoo.solver.board.solve;

import java.util.List;
import java.util.Map;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;
import junker.disco.zoo.solver.model.solver.Solution;

public class ExpectedValueCalculator {
    public static void mutateProbabilitiesForAnimal(Animal animalToSearch, Animal animalToPlace, Overlaps overlaps,
                                                    Coords coords,
                                                    Map<Animal, Double> probabilitiesForDifferentAnimals,
                                                    Map<Animal, Double> nextProbabilitiesForDifferentAnimals,
                                                    Overlaps nextOverlaps, List<Solution> solutions, int moveIndex) {

        var nextHighestOverlapCoords = OverlapCalulator.findHighestOverlapCoords(nextOverlaps, animalToSearch, true);
        if (nextHighestOverlapCoords.isEmpty())
            return;
        final var probabilityForAnimal =
                overlaps.animalOverlapProbability().get(animalToPlace)[coords.x()][coords.y()];
        probabilitiesForDifferentAnimals.put(animalToPlace, probabilityForAnimal);

        final var probabilityOfNextHighestOverlap = nextProbabilityOfBestSolutions(solutions, nextOverlaps,
                moveIndex, animalToSearch);
        nextProbabilitiesForDifferentAnimals.put(animalToPlace, probabilityOfNextHighestOverlap);
    }

    private static double nextProbabilityOfBestSolutions(List<Solution> solutions, Overlaps nextOverlaps,
                                                         int moveIndex, Animal animalToSearch) {
        return solutions.parallelStream()
                .map(solution -> solution.clicks().get(moveIndex))
                .filter(click -> click.x() >= 0 && click.y() >= 0)
                .map(click -> nextOverlaps.animalOverlapProbability().get(animalToSearch)[click.x()][click.y()])
                .max(Double::compareTo)
                .orElse(0.0);
    }

    public static double expectedNextProbability(Map<Animal, Double> probabilitiesForDifferentAnimals,
                                                 Map<Animal, Double> nextProbabilitiesForDifferentAnimals) {
        var expectedValue = 0d;
        for (var animal : probabilitiesForDifferentAnimals.keySet()) {
            var probability = probabilitiesForDifferentAnimals.get(animal);
            expectedValue += probability * nextProbabilitiesForDifferentAnimals.get(animal);
        }
        return expectedValue;
    }
}
