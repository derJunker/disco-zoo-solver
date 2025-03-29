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
            final var hash = hash(game, animalToSolveFor);
            sharedSlowResultsMap.put(hash, result);
            if (useDb) {
                slowSolutionEntryRepository.save(SlowSolutionEntry.builder()
                        .hash(hash)
                        .bestClicks(result.bestClicks().stream().map(CoordsEntity::fromCoords).collect(Collectors.toSet()))
                        .probabilities(result.probabilities())
                        .build());
                System.out.println("Saved solution to db, took " + (end - start) + "ms, hash: " + hash);
            } else {
                System.out.println("Saved solution for this uptime, took " + (end - start) + "ms, hash: " + hash);
            }
        }
        return result;
    }

    public SolveResult getIfSaved(Game game, Animal animalToSolveFor) {
        if (!useCaching)
            return null;
        return sharedSlowResultsMap.get(hash(game, animalToSolveFor));
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
        System.out.println("Loaded " + entries.size() + " slow solutions into memory");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(useDb && useCaching)
            loadSlowResultsIntoMemory();
    }


    private static String hash(Game game, Animal animalToSolveFor) {
        final var wipedBoard = game.calcWipedBoard();
        StringBuilder sb = new StringBuilder();
        sb  .append("s=")
            .append(animalToSolveFor.name())
            .append(";");
        sb.append("a=[");
        sb.append(game.getContainedAnimals().stream().map(Animal::name).reduce((a, b) -> a + "," + b).orElse(""));
        sb.append("];");
        sb.append("b=[");
        for (var x = 0; x < wipedBoard.length; x++) {
            for (var y = 0; y < wipedBoard[0].length; y++) {
                var tile = wipedBoard[x][y];
                if (tile.isRevealed() && tile.isOccupied()) {
                    sb.append(x).append(",").append(y).append("->");
                    sb.append(tile.getAnimalBoardInstance().animal().name());
                } else if(tile.isRevealed() && !tile.isOccupied()){
                    sb.append("-");
                } else {
                    sb.append("?");
                }
            }
            sb.append("/");
        }
        sb.append("];");

        return sb.toString();
    }


}
