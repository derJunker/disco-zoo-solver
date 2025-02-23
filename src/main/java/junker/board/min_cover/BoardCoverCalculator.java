package junker.board.min_cover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junker.animals.Animal;
import junker.board.AnimalBoardInstance;
import junker.board.Coords;
import junker.board.Game;
import junker.board.Tile;
import junker.board.probabiltiy.PermutationService;

public class BoardCoverCalculator {

    public static Set<Set<Coords>> minimumSetCover(Game game, Animal animalToSearch) {
        var overlap = calculateOverlap(game, animalToSearch);
        return minimumSetCover(game.getBoard(), animalToSearch, overlap);
    }

    private static Set<Set<Coords>> minimumSetCover(Tile[][] board, Animal animalToSearch,
                                                    List<AnimalBoardInstance>[][] overlap) {
        var highestOverlapCoords = getHighestOverlapCoords(overlap);
        for (var coords : highestOverlapCoords) {

        }
        return null;
    }


    private static Set<Coords> getHighestOverlapCoords(List<AnimalBoardInstance>[][] overlap) {
        int highestOverlap = 0;
        List<Coords> highestOverlapCoords = new ArrayList<>();
        for (int y = 0; y < overlap.length; y++) {
            for (int x = 0; x < overlap[0].length; x++) {
                if (overlap[x][y].size() > highestOverlap) {
                    highestOverlap = overlap[x][y].size();
                    highestOverlapCoords.clear();
                    highestOverlapCoords.add(new Coords(x, y));
                } else if (overlap[x][y].size() == highestOverlap) {
                    highestOverlapCoords.add(new Coords(x, y));
                }
            }
        }
        return new HashSet<>(highestOverlapCoords);
    }

    public static List<AnimalBoardInstance>[][] calculateOverlap(Game game, Animal animalToSearch) {
        var boardPermutations = PermutationService.calculateBoardPermutations(game.getWipedBoard(), game.getContainedAnimals());
        var board = game.getBoard();
        List<AnimalBoardInstance>[][] overlap = new List[board.length][board[0].length];
        for (var permutation : boardPermutations) {
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board[0].length; x++) {
                    if (overlap[x][y] == null)
                        overlap[x][y] = new ArrayList<>();
                    if (permutation[x][y].hasAnimalInstanceOfType(animalToSearch)) {
                        overlap[x][y].add(permutation[x][y].getAnimalBoardInstance());
                    }
                }
            }
        }
        return overlap;
    }
}
