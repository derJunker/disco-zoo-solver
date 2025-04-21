package junker.disco.zoo.solver.controller.validation.body_validators;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Rarity;
import junker.disco.zoo.solver.requests.post_bodies.ReconstructClickBody;
import junker.disco.zoo.solver.requests.post_bodies.ReconstructStartBody;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ReconstructClickBodyValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ReconstructClickBody.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var reconstructClickBody = (ReconstructClickBody) target;
        var animalToClick = reconstructClickBody.animal();
        var containedAnimals = reconstructClickBody.game().containedAnimals();
        if (animalToClick != null &&
                animalToClick.rarity() != Rarity.BUX && animalToClick.rarity() != Rarity.PET) {

            if (!containedAnimals.contains(animalToClick))
                errors.rejectValue("animalToClick", "animal.not.contained", "Animal to find must be contained in the game");
        }

    }
}
