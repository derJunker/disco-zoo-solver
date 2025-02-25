package junker.board.min_cover;

import java.util.List;

import junker.board.Coords;
import junker.board.Game;

public record Solution(List<Coords> clicks, Game solvedGame) {
    @Override
    public int hashCode() {
        return clicks.stream().map(Coords::hashCode).reduce(0, Integer::sum);
    }
}
