package junker.disco.zoo.solver.requests.return_objects;

import java.util.Set;

import junker.disco.zoo.solver.board.Coords;

public record SolveResult(
        Set<Coords> bestClicks,
        Double[][] probabilities
) {

}
