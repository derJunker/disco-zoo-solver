package junker.disco.zoo.solver.config;

import junker.disco.zoo.solver.controller.validation.body_validators.AccuracyClickBodyValidator;
import junker.disco.zoo.solver.controller.validation.body_validators.ReconstructClickBodyValidator;
import junker.disco.zoo.solver.controller.validation.body_validators.ReconstructStartBodyValidator;
import junker.disco.zoo.solver.controller.validation.body_validators.SolveRequestBodyValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorBeanConfig {
    @Bean
    public AccuracyClickBodyValidator accuracyClickBodyValidator() {return new AccuracyClickBodyValidator();}

    @Bean
    public ReconstructStartBodyValidator reconstructStartBodyValidator() {return new ReconstructStartBodyValidator();}

    @Bean
    public ReconstructClickBodyValidator reconstructClickBodyValidator() {return new ReconstructClickBodyValidator(); }

    @Bean
    public SolveRequestBodyValidator solveRequestBodyValidator() {return new SolveRequestBodyValidator();}
}
