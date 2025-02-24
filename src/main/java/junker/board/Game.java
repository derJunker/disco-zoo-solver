package junker.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import junker.animals.Animal;
import junker.board.min_cover.BoardCoverCalculator;
import junker.board.probabiltiy.PermutationService;
import junker.util.DoubleArrayUtil;

import static junker.board.BoardService.cloneBoard;

public class Game {
    public static final int BOARD_SIZE = 5;
    public static final int MAX_ATTEMPTS = 10;

    private Tile[][] board;
    private final List<Animal> containedAnimals;

    private static Tile[][] getWipedBoard(Tile[][] board) {
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

    public Game(Game gameToClone, boolean wipe) {
        var boardToClone = gameToClone.getBoard();
        this.board = wipe? getWipedBoard(boardToClone) : cloneBoard(boardToClone);
        this.containedAnimals = new ArrayList<>(gameToClone.containedAnimals);
    }

    public static Game of(Tile[][] board) {
        Game game = new Game(new ArrayList<>());
        game.board = board;
        game.containedAnimals.addAll(BoardService.getContainedAnimals(board));
        return game;
    }


    public void revealTile(int x, int y) {
        var tile = board[x][y];
        tile.setRevealed(true);
    }

    public void setTile(int x, int y, boolean revealed, Animal animal) {
        var tile = board[x][y];
        tile.setRevealed(revealed);
        tile.setAnimalBoardInstance(null);
        if (animal != null)
            tile.setAnimalBoardInstance(new AnimalBoardInstance(animal, "", new Coords(-1, -1)));
    }

    public void printGame() {
        System.out.println(this);
    }

    public Set<Coords> getBestClicks(Animal animalToSearch) {
        var minCover = BoardCoverCalculator.minCoveringSets(this, animalToSearch);
        return minCover.stream().map(List::getFirst).collect(Collectors.toSet());
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
        return Game.getWipedBoard(board);
    }
}
