package junker.disco.zoo.solver.config;

import junker.disco.zoo.solver.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Bucket4jRateLimitApp implements WebMvcConfigurer {

    private final RateLimitInterceptor interceptor;

    @Autowired
    public Bucket4jRateLimitApp(RateLimitInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**");
    }
}
