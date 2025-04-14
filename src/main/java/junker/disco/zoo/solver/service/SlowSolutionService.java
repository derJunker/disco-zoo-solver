package junker.disco.zoo.solver.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.db.entities.CoordsEntity;
import junker.disco.zoo.solver.db.entities.SlowSolutionEntry;
import junker.disco.zoo.solver.db.repos.SlowSolutionEntryRepository;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.requests.return_objects.SolveResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class SlowSolutionService implements ApplicationListener<ApplicationReadyEvent> {


    @Value("${solutions.save.threshold.ms}")
    private int saveThresholdMs;

    @Value("${solutions.save.use.db}")
    private boolean useDb;

    @Value("${solutions.save.use.caching}")
    private boolean useCaching;

    private static final Map<String, SolveResult> sharedSlowResultsMap = new ConcurrentHashMap<>();

    private final SlowSolutionEntryRepository slowSolutionEntryRepository;

    public SlowSolutionService(SlowSolutionEntryRepository slowSolutionEntryRepository) {
        this.slowSolutionEntryRepository = slowSolutionEntryRepository;
    }


    public SolveResult addSolutionIfTooSlow(Supplier<SolveResult> supplier, Game game,
                                            Animal animalToSolveFor) {
        if (!useCaching)
            return supplier.get();
        var start = System.currentTimeMillis();
        var result = supplier.get();
        var end = System.currentTimeMillis();
        if (end - start > saveThresholdMs) {
            final var hash = game.hashString(animalToSolveFor);
            sharedSlowResultsMap.put(hash, result);
            if (useDb) {
                slowSolutionEntryRepository.save(SlowSolutionEntry.builder()
                        .hash(hash)
                        .bestClicks(result.bestClicks().stream().map(CoordsEntity::fromCoords).collect(Collectors.toSet()))
                        .probabilities(result.probabilities())
                        .build());
//                log.info("Saved solution to db, took {}ms, hash: {}", end - start, hash);
            } else {
//                log.info("Saved solution for this uptime, took {}ms, hash: {}", end - start, hash);
            }
        }
        return result;
    }

    public SolveResult getIfSaved(Game game, Animal animalToSolveFor) {
        if (!useCaching)
            return null;
        return sharedSlowResultsMap.get(game.hashString(animalToSolveFor));
    }

    public void loadSlowResultsIntoMemory() {
        final var entries = slowSolutionEntryRepository.findAll();
        entries.forEach(entry -> {
            final var hash = entry.getHash();
            final var bestClicks = entry.getBestClicks();
            final var probabilities = entry.getProbabilities();
            sharedSlowResultsMap.put(hash, new SolveResult(bestClicks.stream()
                    .map(CoordsEntity::toCoords).collect(Collectors.toSet()),
                    probabilities));
        });
//        log.info("Loaded {} slow solutions into memory", entries.size());
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(useDb && useCaching)
            loadSlowResultsIntoMemory();
    }



}
