package junker.disco.zoo.solver.requests.post_bodies;

import java.util.List;

import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidContainedAnimals;
import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidPetName;
import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidRegionName;
import junker.disco.zoo.solver.model.animals.Animal;

public record  ReconstructStartBody(
        @ValidContainedAnimals List<Animal> animals,

        @ValidRegionName String region,
        @ValidPetName String petName
) {
}
