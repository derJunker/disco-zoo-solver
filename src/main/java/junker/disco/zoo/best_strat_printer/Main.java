package junker.disco.zoo.best_strat_printer;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import junker.disco.zoo.solver.board.Click;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.board.util.StatTracker;
import junker.disco.zoo.solver.model.animals.Animal;

import static junker.disco.zoo.solver.board.solve.OverlapCalulator.calculateOverlaps;

public class Main {
    public static void main(String[] args) throws IOException {
        Animal.initAnimals();
        Animal.initPets();

//        Animal.ALL_ANIMALS.forEach();
        GridVisualizer.drawGridWithCoords(List.of(new Coords(2, 0), new Coords(2, 1), new Coords(2, 2), new Coords(2, 3), new Coords(2, 4)), "grid.png");
    }

//    private static String getBestAnimalSolution(Animal animal) {
//        var game = new Game(new Game(List.of(animal), animal.region()), true);
//        var moveInfo = DiscoZooSolver.getBestMoveInformation(animal, game);
//        var clicks = moveInfo.solutions().getFirst().clicks();
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int x = 0; x < game.getBoard().length; x++) {
//            for (int y = 0; y < game.getBoard()[x].length; y++) {
//                var finalX = x;
//                var finalY = y;
//                var matchingIndex = IntStream.range(0, clicks.size())
//                        .filter(i -> {
//                            var coords = clicks.get(i).coords();
//                            return coords.x() == finalX && coords.y() == finalY;
//                        })
//                        .findFirst()
//                        .orElse(-1);
//
//                if (matchingIndex != -1)
//                {
//                    stringBuilder.append(matchingIndex+1);
//                } else {
//                    stringBuilder.append("O");
//                }
//            }
//            System.out.println();
//        }
//    }
}
