package junker.disco.zoo.solver.config;

import junker.disco.zoo.solver.controller.validation.body_validators.AccuracyClickBodyValidator;
import junker.disco.zoo.solver.controller.validation.body_validators.ReconstructClickBodyValidator;
import junker.disco.zoo.solver.controller.validation.body_validators.ReconstructStartBodyValidator;
import junker.disco.zoo.solver.controller.validation.body_validators.SolveRequestBodyValidator;
import junker.disco.zoo.solver.requests.post_bodies.AccuracyClickBody;
import junker.disco.zoo.solver.requests.post_bodies.ReconstructClickBody;
import junker.disco.zoo.solver.requests.post_bodies.ReconstructStartBody;
import junker.disco.zoo.solver.requests.post_bodies.SolveRequestBody;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class ValidatorConfig {

    private final AccuracyClickBodyValidator accuracyClickBodyValidator;
    private final ReconstructStartBodyValidator reconstructStartBodyValidator;
    private final ReconstructClickBodyValidator reconstructClickBodyValidator;
    private final SolveRequestBodyValidator solveRequestBodyValidator;

    public ValidatorConfig(AccuracyClickBodyValidator accuracyClickBodyValidator,
                           ReconstructStartBodyValidator reconstructStartBodyValidator,
                           ReconstructClickBodyValidator reconstructClickBodyValidator,
                           SolveRequestBodyValidator solveRequestBodyValidator) {
        this.accuracyClickBodyValidator = accuracyClickBodyValidator;
        this.reconstructStartBodyValidator = reconstructStartBodyValidator;
        this.reconstructClickBodyValidator = reconstructClickBodyValidator;
        this.solveRequestBodyValidator = solveRequestBodyValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof AccuracyClickBody) {
            binder.addValidators(accuracyClickBodyValidator);
        }

        if (binder.getTarget() instanceof ReconstructStartBody) {
            binder.addValidators(reconstructStartBodyValidator);
        }

        if (binder.getTarget() instanceof ReconstructClickBody) {
            binder.addValidators(reconstructClickBodyValidator);
        }

        if (binder.getTarget() instanceof SolveRequestBody) {
            binder.addValidators(solveRequestBodyValidator);
        }
    }
}
