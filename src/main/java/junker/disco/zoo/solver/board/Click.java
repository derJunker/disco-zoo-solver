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

    @Override
    public String toString() {
        return "Click{" +
                "(" + x() + ", " + y() + ")" +
                "); P=" + expectedProbability +
                '}';
    }
}
