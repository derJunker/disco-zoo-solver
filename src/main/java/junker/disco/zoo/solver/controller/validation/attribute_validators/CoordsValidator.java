package junker.disco.zoo.solver.controller.validation.attribute_validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import junker.disco.zoo.solver.board.Coords;

public class CoordsValidator implements ConstraintValidator<ValidCoords, Coords> {

    @Override
    public boolean isValid(Coords coords, ConstraintValidatorContext context) {
        if (coords == null) return true; // let @NotNull handle that
        return coords.x() >= 0 && coords.x() <= 4 && coords.y() >= 0 && coords.y() <= 4;
    }
}
