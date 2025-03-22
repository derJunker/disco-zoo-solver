package junker.disco.zoo.solver.requests.return_objects;

import java.util.Collection;

import junker.disco.zoo.solver.board.Coords;

public record AccuracySingleClickPerformanceResponse(
        double accuracy,
        Double[][] probabilities,
        Collection<Coords> bestClicks
) {
}
