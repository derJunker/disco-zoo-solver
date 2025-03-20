package junker.disco.zoo.solver.service;

import java.util.ArrayList;
import java.util.Random;

import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.accuracy.AccuracyGameType;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Region;
import org.springframework.stereotype.Service;

@Service
public class AccuracyService {

    // How many random calls to skip ahead for each round. This should be the maximum amount of random.next...()
    // calls for each "getRandomGame()
    private static final int RANDOM_CALLS_UPPER_BOUND = 20;

    public Game getRandomGame(Long seed,
                              int gameNumber, Region region, boolean timeless) {
        var random = new java.util.Random(seed);

        skipRandomAhead(gameNumber, random);

        var animalAmount = random.nextInt(3) + 1;

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
        return new Game(animalsToPlace);
    }

    private void skipRandomAhead(int gameNumber, Random random) {
        for (int i = 0; i < gameNumber; i++) {
            for (int j = 0; j < RANDOM_CALLS_UPPER_BOUND; j++) {
                random.nextInt();
            }
        }
    }
}
