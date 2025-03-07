package junker.disco.zoo.solver.requests.return_objects;

import java.util.Set;

import junker.board.Coords;

public record SolveResult(
        Set<Coords> bestClicks,
        double[][] probabilities
) {

}
