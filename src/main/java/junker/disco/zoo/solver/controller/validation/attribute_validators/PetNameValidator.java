package junker.disco.zoo.solver.controller.validation.attribute_validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import junker.disco.zoo.solver.model.animals.Animal;

public class PetNameValidator implements ConstraintValidator<ValidPetName, String> {
    @Override
    public boolean isValid(String petName, ConstraintValidatorContext constraintValidatorContext) {
        var optPet = Animal.findPetByName(petName);
        return optPet.isPresent() || petName == null;
    }
}
