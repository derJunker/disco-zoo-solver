package junker.disco.zoo.solver.board.solve.return_objects;

import java.util.List;

import junker.disco.zoo.solver.model.solver.Solution;

// Made this mutable to improve performance (object can be reused) idk how effective but i like how ugly it is
public class DifferentAnimalPlacementReturnObject {
    public List<Solution> differentAnimalSolutions;
    public double expectedNextProbability;
}
