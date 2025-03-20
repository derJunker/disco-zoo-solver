package junker.disco.zoo.solver.requests.return_objects;

public record AccuracySingleClickPerformanceResponse(
        double accuracy,
        boolean wasBestClick
) {
}
