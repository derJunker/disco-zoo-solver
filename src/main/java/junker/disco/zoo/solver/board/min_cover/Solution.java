package junker.disco.zoo.solver.board.min_cover;

import java.util.List;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;

public record Solution(List<Coords> clicks, Game solvedGame) {
    @Override
    public int hashCode() {
        return clicks.stream().map(Coords::hashCode).reduce(0, Integer::sum);
    }

    @Override
    public String toString() {
        return "Solution{" +
                "clicks=" + clicks.stream().map(c -> "(" + c.x() + ", " + c.y() + ")").toList() +
                ", solvedGame=\n" + solvedGame +
                '}';
    }
}
