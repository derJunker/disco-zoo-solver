package junker.disco.zoo.solver.controller.validation.attribute_validators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import junker.disco.zoo.solver.model.animals.Animal;

public class ListAnimalValidator implements ConstraintValidator<ValidContainedAnimals, List<Animal>> {
    @Override
    public boolean isValid(List<Animal> containedAnimals, ConstraintValidatorContext context) {
        if (containedAnimals.isEmpty() || containedAnimals.size() > 3) {
            context.buildConstraintViolationWithTemplate("Animal count must be between 1 and 3")
                    .addPropertyNode("containedAnimals")
                    .addConstraintViolation();
            return false;
        }
        var animalNames = containedAnimals.stream().map(Animal::name).toArray(String[]::new);
        var foundAnimals = Animal.findAnimalsByName(animalNames);
        if (containedAnimals.size() != foundAnimals.size()) {
            context.buildConstraintViolationWithTemplate("Some animals are not found")
                    .addPropertyNode("containedAnimals")
                    .addConstraintViolation();
            return false;
        }
        var uniqueAnimalNames = new HashSet<>(Arrays.asList(animalNames));
        if (uniqueAnimalNames.size() != animalNames.length) {
            context.buildConstraintViolationWithTemplate("Some animals are duplicates")
                    .addPropertyNode("containedAnimals")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
