package junker.disco.zoo.solver.board.solve;

import java.util.Map;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;

public class ExpectedValueCalculator {
    public static void mutateProbabilitiesForAnimal(Animal animalToSearch, Animal animalToPlace, Overlaps overlaps,
                                                    Coords coords,
                                                    Map<Animal, Double> probabilitiesForDifferentAnimals,
                                                    Map<Animal, Double> nextProbabilitiesForDifferentAnimals, Overlaps nextOverlaps) {

        var nextHighestOverlapCoords = OverlapCalulator.findHighestOverlapCoords(nextOverlaps, animalToSearch, true);
        if (nextHighestOverlapCoords.isEmpty())
            return;
        final var probabilityForAnimal =
                overlaps.animalOverlapProbability().get(animalToPlace)[coords.x()][coords.y()];
        probabilitiesForDifferentAnimals.put(animalToPlace, probabilityForAnimal);

        final var nextCoords = nextHighestOverlapCoords.getFirst();
        final var probabilityOfNextHighestOverlap =
                nextOverlaps.animalOverlapProbability().get(animalToSearch)[nextCoords.x()][nextCoords.y()];
        nextProbabilitiesForDifferentAnimals.put(animalToPlace, probabilityOfNextHighestOverlap);
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
