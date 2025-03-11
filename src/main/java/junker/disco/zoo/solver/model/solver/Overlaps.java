package junker.disco.zoo.solver.model.solver;

import java.util.List;

import junker.disco.zoo.solver.board.AnimalBoardInstance;

public record Overlaps(
        List<AnimalBoardInstance>[][] overallOverlap,
        int permutationSize
) {
}
