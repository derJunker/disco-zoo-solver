package junker.disco.zoo.solver.requests.post_bodies;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Coords;

public record ReconstructClickBody (
    GameDTO game,
    Animal animal,
    Coords coords
) {
}
