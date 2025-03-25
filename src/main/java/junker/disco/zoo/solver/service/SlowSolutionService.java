package junker.disco.zoo.solver.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junker.disco.zoo.solver.model.solver.Solution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SlowSolutionService {

    @Value("${solutions.save.threshold.ms}")
    private int saveThresholdMs;

    private static final Map<String, Solution> sharedSlowSolutionsMap = Collections.synchronizedMap(new HashMap<>());


    public void addSolutionIfTooSlow(int duration, Solution solution, String hash) {
        if (duration > saveThresholdMs) {
            sharedSlowSolutionsMap.put(hash, solution);
        }
    }


}
