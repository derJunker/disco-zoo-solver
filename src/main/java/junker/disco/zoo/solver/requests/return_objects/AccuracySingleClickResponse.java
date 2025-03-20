package junker.disco.zoo.solver.requests.return_objects;

import java.util.Map;

import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Animal;

public record AccuracySingleClickResponse(
        Game game,
        Animal animalToFind
) {
}
