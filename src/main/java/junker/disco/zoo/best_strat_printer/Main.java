package junker.disco.zoo.best_strat_printer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import junker.disco.zoo.solver.board.Click;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.board.util.StatTracker;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Rarity;

import static junker.disco.zoo.solver.board.solve.OverlapCalulator.calculateOverlaps;

public class Main {
    public static void main(String[] args) throws IOException {
        Animal.initAnimals();
        Animal.initPets();

        Animal.ALL_ANIMALS.parallelStream().forEach(animal -> {
            if (animal.rarity().equals(Rarity.BUX))
                return;
            var sols = DiscoZooSolver.getBestSolutionsForSingleAnimal(animal);
            var clicks = sols.getFirst().clicks();
            var coords = new ArrayList<Coords>();
            for (var click : clicks) {
                coords.add(click.coords());
                if (click.expectedProbability() >= 1)
                    break;
            }
            try {
                GridVisualizer.drawGridWithCoords(coords, animal.region().ordinal() + "_" + animal.region() + "/" + animal.name() + "Strategy");
            } catch (IOException e) {
                System.out.printf("Animal: %s\n", animal.name());
                throw new RuntimeException(e);
            }
        });
    }
}
