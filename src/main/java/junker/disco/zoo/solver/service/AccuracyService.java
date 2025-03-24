package junker.disco.zoo.solver.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.model.accuracy.AccuracyDifficulty;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Region;
import junker.disco.zoo.solver.requests.return_objects.AccuracySingleClickGameResponse;
import junker.disco.zoo.solver.requests.return_objects.AccuracySingleClickPerformanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccuracyService {

    // How many random calls to skip ahead for each round. This should be the maximum amount of random.next...()
    // calls for each "getRandomGame()
    private static final int RANDOM_CALLS_UPPER_BOUND = 20;

    public AccuracySingleClickGameResponse getSingleClickGame(Long seed,
                                                              int gameNumber, Region region, boolean timeless,
                                                              AccuracyDifficulty difficulty) {
        var random = new java.util.Random(seed);

        skipRandomAhead(gameNumber, random);

        var animalAmount = animalAmount(difficulty, random);

        if (region.equals(Region.ANY)) {
            var regionsWithoutAny = Region.exclusiveValues();
            region = regionsWithoutAny.get(random.nextInt(regionsWithoutAny.size()));
        }

        var animalsOfRegion = Animal.getAnimalListByRegion(region, timeless);
        var remainingAnimals = new ArrayList<>(animalsOfRegion);
        var animalsToPlace = new ArrayList<Animal>();
        for (var i = 0; i < animalAmount; i++) {
            var chosenAnimalIndex = random.nextInt(remainingAnimals.size());
            var animal = remainingAnimals.remove(chosenAnimalIndex);
            animalsToPlace.add(animal);
        }
        var animalToSearch = animalsToPlace.get(random.nextInt(animalsToPlace.size()));
        var game = new Game(animalsToPlace, region);
        return new AccuracySingleClickGameResponse(game, animalToSearch);
    }

    private void skipRandomAhead(int gameNumber, Random random) {
        for (int i = 0; i < gameNumber; i++) {
            for (int j = 0; j < RANDOM_CALLS_UPPER_BOUND; j++) {
                random.nextInt();
            }
        }
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
        var bestMoveInfo = DiscoZooSolver.getBestMoveInformation(animalToFind, game);
        var probabilities = bestMoveInfo.probabilities();
        var clickedProbability = probabilities[click.x()][click.y()];
        var bestClicks = bestMoveInfo.solutions().stream()
                .map(solution -> solution.clicks().getFirst()).collect(Collectors.toSet());
        var bestClickProbability = bestClicks.stream().findAny()
                        .map(coords -> probabilities[coords.x()][coords.y()])
                        .orElse(0d);
        var accuracy = clickedProbability / bestClickProbability;
        return new AccuracySingleClickPerformanceResponse(accuracy, probabilities, bestClicks);
    }
}
