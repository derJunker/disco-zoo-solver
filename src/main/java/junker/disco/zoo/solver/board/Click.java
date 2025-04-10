package junker.disco.zoo.solver.board;

public record Click(
        Coords coords,
        double expectedProbability
) {
    public int x() {
        return coords.x();
    }

    public int y() {
        return coords.y();
    }
}
