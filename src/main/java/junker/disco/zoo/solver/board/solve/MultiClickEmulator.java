package junker.disco.zoo.solver.board.solve;

import java.util.List;
import java.util.Set;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;

import static junker.disco.zoo.solver.board.util.DoubleArrayUtil.filterByIndex;

public class MultiClickEmulator {
    public static Set<Set<Coords>> calculateMultiClickSets(Overlaps overlaps,
                                                           List<Coords> highestOverlapCoords, Animal animalToSolve) {
        var tilesWithHighestOverlapsToAnimalInstances = filterByIndex(overlaps.uniqueAnimalOverlapMap().get(animalToSolve),
                highestOverlapCoords::contains);
        return IndependentSetsCalculator.calculateMaxIndependentSubSets(tilesWithHighestOverlapsToAnimalInstances,
                AnimalBoardInstance::id);
    }
}
