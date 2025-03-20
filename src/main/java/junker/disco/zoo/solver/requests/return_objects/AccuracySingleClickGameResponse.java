package junker.disco.zoo.solver.requests.return_objects;

import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Animal;

public record AccuracySingleClickGameResponse(
        Game game,
        Animal animalToFind
) {
}
