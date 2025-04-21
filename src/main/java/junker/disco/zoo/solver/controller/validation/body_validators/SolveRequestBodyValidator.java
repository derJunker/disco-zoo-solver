package junker.disco.zoo.solver.controller.validation.body_validators;


import junker.disco.zoo.solver.model.animals.Rarity;
import junker.disco.zoo.solver.requests.post_bodies.SolveRequestBody;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SolveRequestBodyValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return SolveRequestBody.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var solveRequestBody = (SolveRequestBody) target;
        var animalToFind = solveRequestBody.animalToSolveFor();
        var containedAnimals = solveRequestBody.game().containedAnimals();
        if (animalToFind.rarity() != Rarity.BUX && animalToFind.rarity() != Rarity.PET  && !containedAnimals.contains(animalToFind)) {
            errors.rejectValue("animalToSolveFor", "animal.not.contained", "Animal to find must be contained in the game");
        }
    }
}
