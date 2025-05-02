package junker.disco.zoo.solver.controller.validation.attribute_validators;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Rarity;

public class AnimalValidator implements ConstraintValidator<ValidAnimal, Animal> {

    @Override
    public boolean isValid(Animal animal, ConstraintValidatorContext context) {
        if (animal == null) {
            return true; // Consider null as valid, handle null checks elsewhere if needed
        }

        var foundAnimalList = Animal.findAnimalsByName(animal.name());
        if (foundAnimalList.isEmpty())
            foundAnimalList = Animal.findPetByName(animal.name()).map(List::of).orElse(List.of());
        if (foundAnimalList.size() != 1) {
            context.buildConstraintViolationWithTemplate("Animal not found")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            return false;
        }

        var foundAnimal = foundAnimalList.getFirst();
        if (foundAnimal == null || (!foundAnimal.equals(animal) && animal.rarity() != Rarity.BUX)) {
            context.buildConstraintViolationWithTemplate("Animal mismatch")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
