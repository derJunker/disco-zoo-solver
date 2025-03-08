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
}
