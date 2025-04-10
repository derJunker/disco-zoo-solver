package junker.disco.zoo.solver.board;

public record Coords(
        int x,
        int y
) {

    public static Coords coords(int x, int y) {
        return new Coords(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public Click toClick(double expectedProbability) {
        return new Click(this, expectedProbability);
    }

    public Click toClickWithSize(int size) {
        return new Click(this, (1.0 / size));
    }

    public Click toMaxProbabilityClick() {
        return new Click(this, 1.0);
    }
}
