package junker.disco.zoo.solver.post_bodies;

import junker.animals.Animal;
import junker.board.Coords;

public record ReconstructClickBody (
    GameDTO game,
    Animal animal,
    Coords coords
) {
}
