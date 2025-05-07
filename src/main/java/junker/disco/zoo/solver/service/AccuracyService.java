package junker.disco.zoo.solver.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.model.accuracy.AccuracyDifficulty;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Region;
import junker.disco.zoo.solver.requests.return_objects.AccuracySingleClickGameResponse;
import junker.disco.zoo.solver.requests.return_objects.AccuracySingleClickPerformanceResponse;
import org.springframework.stereotype.Service;

@Service
public class AccuracyService {

    private final SolveService solveService;

    public AccuracyService(SolveService solveService) {
        this.solveService = solveService;
    }

    public AccuracySingleClickGameResponse getSingleClickGame(Long seed,
                                                               int gameNumber, Region region, boolean timeless,
                                                               AccuracyDifficulty difficulty) {
        return getSingleClickGame(seed, gameNumber, region, timeless, difficulty, null);
    }

    public AccuracySingleClickGameResponse precomputeGameResults(Long seed, int gameNumber,
                                                                  int gamePreComputeAmnt, int offset, Region region,
                                                                  boolean timeless, AccuracyDifficulty difficulty) {
        var firstGameResp = getSingleClickGame(seed, gameNumber, region, timeless, difficulty);

        new Thread(() -> {
            IntStream.range(offset, offset+gamePreComputeAmnt).parallel().forEach( i -> {
                var gameResp = getSingleClickGame(seed, gameNumber+i, region, timeless, difficulty);
                solveService.solve(gameResp.game(), gameResp.animalToFind());
                System.out.println("Precomputed: " + (i+gameNumber));
            });
        }).start();
        return firstGameResp;
    }

    private AccuracySingleClickGameResponse getSingleClickGame(Long seed,
                                                              int gameNumber, Region region, boolean timeless,
                                                              AccuracyDifficulty difficulty, Random random) {
        if (random == null) {
            random = new Random(seed);
        }
        List<Animal> previousAnimals = new ArrayList<>();
        if (gameNumber > 0) {
            // Recursively get the previous game to get the animals that were placed
            var previousResponse = getSingleClickGame(seed, gameNumber - 1, region, timeless, difficulty, random);
            previousAnimals = previousResponse.game().getContainedAnimals();
        }

        var animalAmount = animalAmount(difficulty, random);

        if (region.equals(Region.ANY)) {
            var regionsWithoutAny = Region.exclusiveValues();
            region = regionsWithoutAny.get(random.nextInt(regionsWithoutAny.size()));
        }

        var animalsOfRegion = Animal.getAnimalListByRegion(region, timeless);
        List<Animal> remainingAnimals;
        List<Animal> animalsToPlace;
        do {
            remainingAnimals = new ArrayList<>(animalsOfRegion);
            animalsToPlace = new ArrayList<>();
            for (var i = 0; i < animalAmount; i++) {
                var chosenAnimalIndex = random.nextInt(remainingAnimals.size());
                var animal = remainingAnimals.remove(chosenAnimalIndex);
                animalsToPlace.add(animal);
            }
        }
        while (animalsToPlace.size() == previousAnimals.size() &&
                new HashSet<>(animalsToPlace).containsAll(previousAnimals) && new HashSet<>(previousAnimals).containsAll(animalsToPlace));
        var animalToSearch = animalsToPlace.get(random.nextInt(animalsToPlace.size()));
        var game = new Game(animalsToPlace, region);
        return new AccuracySingleClickGameResponse(game, animalToSearch);
    }

    private int animalAmount(AccuracyDifficulty difficulty, Random random) {
        int roll = random.nextInt(100);

        int chance1, chance2, chance3;

        switch (difficulty) {
            case EASY:
                chance1 = 70;
                chance2 = 30;
                chance3 = 0;
                break;
            case MEDIUM:
                chance1 = 20;
                chance2 = 60;
                chance3 = 20;
                break;
            case HARD:
                chance1 = 0;
                chance2 = 25;
                chance3 = 75;
                break;
            default:
                return 1;
        }

        if (roll < chance1) return 1;
        if (roll < chance1 + chance2) return 2;
        return 3;
    }

    public AccuracySingleClickPerformanceResponse clicked(Game game, Animal animalToFind, Coords click) {
        var solveResult = solveService.solve(game, animalToFind);
        var probabilities = solveResult.probabilities();
        var clickedProbability = probabilities[click.x()][click.y()];
        var bestClicks = solveResult.bestClicks();
        var bestClickProbability = bestClicks.stream().findAny()
                        .map(coords -> probabilities[coords.x()][coords.y()])
                        .orElse(0d);
        var accuracy = clickedProbability / bestClickProbability;
        return new AccuracySingleClickPerformanceResponse(accuracy, probabilities, bestClicks);
    }
}
