package junker.board;

import java.util.ArrayList;
import java.util.List;

import junker.animals.Animal;
import junker.board.probabiltiy.PermutationService;
import junker.util.DoubleArrayUtil;

public class Game {
    public static final int BOARD_SIZE = 5;

    private Tile[][] board;
    private final List<Animal> containedAnimals;

    public Game(List<Animal> animalsToPlace) {
        board = new Tile[BOARD_SIZE][BOARD_SIZE]; // Assuming a 10x10 board for simplicity
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                board[x][y] = new Tile();
            }
        }
        placeAnimalsRandomly(animalsToPlace);
        containedAnimals = new ArrayList<>(animalsToPlace);
    }

    public static Game of(Tile[][] board) {
        Game game = new Game(new ArrayList<>());
        game.board = board;
        game.containedAnimals.addAll(BoardService.getContainedAnimals(board));
        return game;
    }

    public Float[][] calculateProbabilities() {
        return null; // TODO: Implement this method
    }

    public void printGame() {
        System.out.println(DoubleArrayUtil.arrayAsCoordinatesString(board));
    }

    @Override
    public String toString() {
        return DoubleArrayUtil.arrayAsCoordinatesString(board);
    }

    private void placeAnimalsRandomly(List<Animal> animalsToPlace) {
        for (Animal animal : animalsToPlace) {
            Coords coords = PermutationService.getRandomPlacement(board, animal);
            if (coords != null) {
                BoardService.placeAnimal(board, animal, coords);
            } else {
                throw new IllegalArgumentException("Cannot place animal: " + animal.name());
            }
        }
    }

    public List<Animal> getContainedAnimals() {
        return containedAnimals;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public Tile[][] getWipedBoard() {
        Tile[][] wipedBoard = new Tile[board.length][board[0].length];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                Tile newTile = new Tile(board[x][y]);
                newTile.removeHiddenAnimalData();
                wipedBoard[x][y] = newTile;
            }
        }
        return wipedBoard;
    }
}
