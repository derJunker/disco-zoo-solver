package junker.disco.zoo.solver.requests.post_bodies;

import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidAnimal;
import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidGameDTO;
import junker.disco.zoo.solver.model.animals.Animal;

public record SolveRequestBody(
        @ValidGameDTO
        GameDTO game,
        @ValidAnimal
        Animal animalToSolveFor
) {
}
