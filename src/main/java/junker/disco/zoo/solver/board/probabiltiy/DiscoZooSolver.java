package junker.disco.zoo.solver.board.probabiltiy;

import java.util.ArrayList;
import java.util.List;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;

public class DiscoZooSolver {
    public static List<Coords> getBestClicks(Animal animalToSolve, Game game) {
        var clonedGame = new Game(game, true);
        var overlaps = calculateOverlaps(clonedGame);
        var highestOverlapCoords = findHighestOverlapCoords(overlaps, animalToSolve);
        return null;
    }

    public static Overlaps calculateOverlaps(Game game) {
        var board = game.getBoard();
        var containedAnimals = game.getContainedAnimals();
        var permutations = PermutationService.calculateBoardPermutations(board, containedAnimals);
        List<AnimalBoardInstance>[][] overallOverlap = new List[board.length][board[0].length];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                var tileOverlaps = new ArrayList<AnimalBoardInstance>();
                for (var permutation : permutations) {
                    if (permutation[x][y].isOccupied()) {
                        tileOverlaps.add(permutation[x][y].getAnimalBoardInstance());
                    }
                }
                overallOverlap[x][y] = tileOverlaps;
            }
        }
        return new Overlaps(overallOverlap, permutations.size());
    }

    // TODO this can be done inside the calculate Overlaps
    public static List<Coords> findHighestOverlapCoords(Overlaps overlaps, Animal animalToSolve) {
        var overallOverlap = overlaps.overallOverlap();
        var bestCandidates = new ArrayList<Coords>();
        var maxOverlap = 0;
        for (int x = 0; x < overallOverlap.length; x++) {
            for (int y = 0; y < overallOverlap[0].length; y++) {
                var animalTileOverlap =
                        overallOverlap[x][y].stream().filter(animalBoardInstance -> animalBoardInstance.animal().equals(animalToSolve)).count();
                if (animalTileOverlap > maxOverlap) {
                    bestCandidates.clear();
                    bestCandidates.add(new Coords(x, y));
                    maxOverlap = (int) animalTileOverlap;
                } else if (animalTileOverlap == maxOverlap) {
                    bestCandidates.add(new Coords(x, y));
                }
            }
        }
        return bestCandidates;
    }
}
