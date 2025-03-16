package junker.disco.zoo.solver.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junker.disco.zoo.solver.board.util.BoardUtil;
import junker.disco.zoo.solver.board.util.PermutationUtil;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Rarity;

import static junker.disco.zoo.solver.board.util.BoardUtil.cloneBoard;
import static junker.disco.zoo.solver.board.util.PermutationUtil.canClickAndPlace;
import static junker.disco.zoo.solver.board.util.DoubleArrayUtil.arrayAsCoordinatesString;

public class Game {
    public static final int BOARD_SIZE = 5;

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

    public Game(Tile[][] board, List<Animal> containedAnimals) {
        this.board = board;
        this.containedAnimals = containedAnimals;
    }

    public static Game of(Tile[][] board) {
        Game game = new Game(new ArrayList<>());
        game.board = board;
        game.containedAnimals.addAll(BoardUtil.getContainedAnimals(board));
        return game;
    }


    public void revealTile(int x, int y) {
        var tile = board[x][y];
        tile.setRevealed(true);
    }

    public void setTile(Coords coords, boolean revealed, Animal animal) {
        setTile(coords.x(), coords.y(), revealed, animal);
    }

    public void setTile(int x, int y, boolean revealed, Animal animal) {
        var tile = board[x][y];
        tile.setRevealed(revealed);
        tile.setAnimalBoardInstance(null);
        if (animal != null)
            tile.setAnimalBoardInstance(new AnimalBoardInstance(animal, "", new Coords(-1, -1)));
    }

    public void setTileIfValid(int x, int y, boolean shouldReveal, Animal animal) {
        if(shouldReveal) {
            boolean isValid = canClickAndPlace(this, x, y, animal);
            if (!isValid) {
                return;
            }
        }
        setTile(x, y, shouldReveal, animal);
    }

    public void printGame() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return arrayAsCoordinatesString(board);
    }

    public boolean animalIsCompletelyRevealed(Animal animal) {
        return getCompletelyRevealedAnimals().contains(animal);
    }

    public List<Animal> getCompletelyRevealedAnimals() {
        var animalRevealedCountMap = new HashMap<Animal, Integer>();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                var tile = board[x][y];
                if (tile.isRevealed() && tile.getAnimalBoardInstance() != null) {
                    var animal = tile.getAnimalBoardInstance().animal();
                    animalRevealedCountMap.put(animal, animalRevealedCountMap.getOrDefault(animal, 0) + 1);
                }
            }
        }
        return animalRevealedCountMap.entrySet().stream()
                .filter(e -> e.getValue() == e.getKey().pattern().size())
                .map(Map.Entry::getKey).toList();
    }

    public List<Animal> getNotCompletelyRevealedAnimalsWithoutBux() {
        var allAnimals = new ArrayList<>(containedAnimals);
        allAnimals.removeAll(getCompletelyRevealedAnimals());
        return allAnimals;
    }


    private void placeAnimalsRandomly(List<Animal> animalsToPlace) {
        for (Animal animal : animalsToPlace) {
            Coords coords = PermutationUtil.getRandomPlacement(board, animal);
            if (coords != null) {
                BoardUtil.placeAnimal(board, animal, coords);
            } else {
                throw new IllegalArgumentException("Cannot place animal: " + animal.name() + " all: " + animalsToPlace);
            }
        }
    }

    public List<Animal> getContainedAnimals() {
        return containedAnimals;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public Tile[][] calcWipedBoard() {
        return Game.getWipedBoard(board);
    }
}
