package junker.disco.zoo.solver.requests.post_bodies;

import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidAnimal;
import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidCoords;
import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidGameDTO;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Coords;

public record ReconstructClickBody (
    @ValidGameDTO
    GameDTO game,
    @ValidAnimal
    Animal animal,
    @ValidCoords
    Coords coords
) {
}
