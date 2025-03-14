package junker.disco.zoo.solver.model;

import java.util.List;

import junker.disco.zoo.solver.model.solver.Solution;

public record BestMoveInformation(
        double[][] probabilities,
        List<Solution> solutions
) {
}
