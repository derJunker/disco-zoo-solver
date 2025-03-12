package junker.disco.zoo.solver.model.solver;

import java.util.List;

import junker.disco.zoo.solver.board.Coords;

public record Solution(
        List<Coords> clicks
) {
}
