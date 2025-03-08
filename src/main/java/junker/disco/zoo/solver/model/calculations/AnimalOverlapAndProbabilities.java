package junker.disco.zoo.solver.model.calculations;

import java.util.List;

import junker.disco.zoo.solver.board.AnimalBoardInstance;

public record AnimalOverlapAndProbabilities(
        List<AnimalBoardInstance>[][] overlap,
        Double[][] probabilities
) {
}
