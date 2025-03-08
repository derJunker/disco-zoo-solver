package junker.disco.zoo.solver.model.calculations;

import java.util.List;
import java.util.Map;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.model.animals.Animal;

public record OverlapsAndProbabilities(
        List<AnimalBoardInstance>[][] overallOverlap,
        Map<Animal, AnimalOverlapAndProbabilities> animalOverlapAndProbabilities
) {
}
