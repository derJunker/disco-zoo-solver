package junker.disco.zoo.solver.board.solve.speedup;

import java.util.List;

import junker.disco.zoo.solver.board.Click;
import junker.disco.zoo.solver.model.solver.Solution;

public record PartialGameResult(
        List<Solution> remainingClickSolution
) {

}
