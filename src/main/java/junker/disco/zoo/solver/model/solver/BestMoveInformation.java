package junker.disco.zoo.solver.model.solver;

import java.util.List;

public record BestMoveInformation(
        Double[][] probabilities,
        List<Solution> solutions
) {
}
