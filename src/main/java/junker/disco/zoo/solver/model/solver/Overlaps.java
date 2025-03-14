package junker.disco.zoo.solver.model.solver;

import java.util.List;
import java.util.Set;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Tile;

public record Overlaps(
        List<AnimalBoardInstance>[][] overallOverlap,
        Set<Tile[][]> permutations
) {
}
