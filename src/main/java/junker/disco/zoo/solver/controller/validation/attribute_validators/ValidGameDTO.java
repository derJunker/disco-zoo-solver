package junker.disco.zoo.solver.controller.validation.attribute_validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GameDTOValidator.class)
public @interface ValidGameDTO {
    String message() default "GameDTO is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
