package junker.disco.zoo.solver.requests.post_bodies;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.model.animals.Animal;

public record AccuracyClickBody(
        GameDTO game,
        Animal animalToFind,
        Coords click
) {

}
