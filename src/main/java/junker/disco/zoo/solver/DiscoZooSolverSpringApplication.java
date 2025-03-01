package junker.disco.zoo.solver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DiscoZooSolverSpringApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DiscoZooSolverSpringApplication.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(DiscoZooSolverSpringApplication.class, args);
    }
}
