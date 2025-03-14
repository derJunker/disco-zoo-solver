package junker.disco.zoo.solver.model.solver;

import java.util.List;
import java.util.Optional;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;

public record Solution(
        List<Coords> clicks,
        Optional<Game> optResultingGame
) {
}
