package junker.disco.zoo.solver.requests.post_bodies;

import jakarta.validation.Valid;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidAnimal;
import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidCoords;
import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidGameDTO;
import junker.disco.zoo.solver.model.animals.Animal;

public record AccuracyClickBody(
        @Valid @ValidGameDTO
        GameDTO game,
        @ValidAnimal
        Animal animalToFind,
        @ValidCoords
        Coords click
) {

}
