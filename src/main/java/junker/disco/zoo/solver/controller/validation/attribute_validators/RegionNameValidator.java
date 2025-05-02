package junker.disco.zoo.solver.controller.validation.attribute_validators;

import java.util.Optional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import junker.disco.zoo.solver.model.animals.Region;

public class RegionNameValidator implements ConstraintValidator<ValidRegionName, String> {

    @Override
    public boolean isValid(String region, ConstraintValidatorContext context) {
        var optErrorMessage = validateGetErrorMsg(region);
        if (optErrorMessage.isPresent()) {
            context.buildConstraintViolationWithTemplate(optErrorMessage.get())
                    .addPropertyNode("region")
                    .addConstraintViolation();
            return false;
        } else {
            return true;
        }
    }

    private Optional<String> validateGetErrorMsg(String region) {
        if (region == null || region.isBlank()) {
            return Optional.of("Region cannot be empty");
        }
        var optRegion = Region.byRepr(region);
        if (optRegion.isEmpty()) {
            return Optional.of("Region not found");
        } else if (optRegion.get() == Region.ANY) {
            return Optional.of("Region cannot be ANY");
        }
        return Optional.empty();
    }
}
