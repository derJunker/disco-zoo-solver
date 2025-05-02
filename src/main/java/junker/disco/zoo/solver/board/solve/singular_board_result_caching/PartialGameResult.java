package junker.disco.zoo.solver.board.solve.singular_board_result_caching;

import java.util.List;

import junker.disco.zoo.solver.model.solver.Solution;

public record PartialGameResult(
        List<Solution> remainingClickSolution
) {

}
