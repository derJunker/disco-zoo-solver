package junker.disco.zoo.solver.controller.validation.body_validators;

import junker.disco.zoo.solver.model.animals.Rarity;
import junker.disco.zoo.solver.requests.post_bodies.AccuracyClickBody;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AccuracyClickBodyValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AccuracyClickBody.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var accuracyClickBody = (AccuracyClickBody) target;
        var animalToFind = accuracyClickBody.animalToFind();
        var containedAnimals = accuracyClickBody.game().containedAnimals();
        if (animalToFind.rarity() != Rarity.BUX && !containedAnimals.contains(animalToFind)) {
            errors.rejectValue("animalToFind", "animal.not.contained", "Animal to find must be contained in the game");
        }
    }
}
