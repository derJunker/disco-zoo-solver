package junker.disco.zoo.solver.controller.validation.body_validators;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Rarity;
import junker.disco.zoo.solver.requests.post_bodies.AccuracyClickBody;
import junker.disco.zoo.solver.requests.post_bodies.ReconstructStartBody;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ReconstructStartBodyValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ReconstructStartBody.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var reconstructStartBody = (ReconstructStartBody) target;
        var regionsMatchGivenRegion =
                reconstructStartBody.animals().stream()
                        .map(Animal::region)
                        .allMatch(region -> region.toString().equalsIgnoreCase(reconstructStartBody.region()));
        if (!regionsMatchGivenRegion) {
            errors.rejectValue("region", "region.not.contained", "Region must be contained in the animals");
        }

        var animalCount = (reconstructStartBody.petName() != null ? 1 : 0) + reconstructStartBody.animals().size();
        if (animalCount > 3) {
            errors.rejectValue("animals", "animal.count.too.high", "Animal + Pet count must be less than or equal to " +
                    "3");
        }
    }
}
