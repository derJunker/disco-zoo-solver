package junker.disco.zoo.solver.board.probabiltiy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;

import static junker.disco.zoo.solver.board.probabiltiy.OverlapCalulator.findHighestOverlapCoords;
import static junker.disco.zoo.solver.board.probabiltiy.OverlapCalulator.getBestReducingRemainingAnimalOverlapCoords;

public class DiscoZooSolver {
    public static List<Coords> getBestClicks(Animal animalToSolve, Game game) {
        var clonedGame = new Game(game, true);
        var overlaps = calculateOverlaps(clonedGame);
        var highestOverlapCoords = findHighestOverlapCoords(overlaps, animalToSolve);
        if (game.getContainedAnimals().size() >= 2) // TODO AND is not a discobux
            highestOverlapCoords = getBestReducingRemainingAnimalOverlapCoords(highestOverlapCoords, overlaps, animalToSolve);

        return null;
    }



    private static Overlaps calculateOverlaps(Game game) {
        var board = game.getBoard();
        var containedAnimals = game.getContainedAnimals();
        var permutations = PermutationService.calculateBoardPermutations(board, containedAnimals);
        List<AnimalBoardInstance>[][] overallOverlap = new List[board.length][board[0].length];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                var tileOverlaps = new ArrayList<AnimalBoardInstance>();
                for (var permutation : permutations) {
                    tileOverlaps.add(permutation[x][y].getAnimalBoardInstance());
                }
                overallOverlap[x][y] = tileOverlaps;
            }
        }
        return new Overlaps(overallOverlap, permutations.size());
    }

    // TODO this can be done inside the calculate Overlaps

}
