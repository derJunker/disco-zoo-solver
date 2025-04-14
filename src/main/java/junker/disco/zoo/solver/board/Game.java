package junker.disco.zoo.solver.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junker.disco.zoo.solver.board.util.PermutationUtil;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Region;
import lombok.Getter;

import static junker.disco.zoo.solver.board.util.BoardUtil.cloneBoard;
import static junker.disco.zoo.solver.board.util.DoubleArrayUtil.arrayAsCoordinatesString;
import static junker.disco.zoo.solver.board.util.PermutationUtil.canClickAndPlace;

@Getter
public class Game {
    public static final int BOARD_SIZE = 5;

    private Tile[][] board;
    private final List<Animal> containedAnimals;
    private final Region region;

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

    public Game(List<Animal> animalsToPlace, Region region) {
        board = new Tile[BOARD_SIZE][BOARD_SIZE]; // Assuming a 10x10 board for simplicity
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                board[x][y] = new Tile();
            }
        }
        var permutations = new ArrayList<>(PermutationUtil.calculateBoardPermutations(board, animalsToPlace));
        if (permutations.isEmpty()) {
            throw new IllegalArgumentException("Cannot place animals: " + animalsToPlace);
        }
        board = permutations.get((int) (Math.random() * permutations.size()));
        containedAnimals = new ArrayList<>(animalsToPlace);
        this.region = region;
    }

    public Game(Game gameToClone, boolean wipe) {
        var boardToClone = gameToClone.getBoard();
        this.board = wipe? getWipedBoard(boardToClone) : cloneBoard(boardToClone);
        this.containedAnimals = new ArrayList<>(gameToClone.containedAnimals);
        this.region = gameToClone.region;
    }

    public Game(Tile[][] board, List<Animal> containedAnimals, Region region) {
        this.board = board;
        this.containedAnimals = containedAnimals;
        this.region = region;
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
            tile.setAnimalBoardInstance(AnimalBoardInstance.of(animal, new Coords(-1, -1)));
    }

    public boolean setTileIfValid(int x, int y, boolean shouldReveal, Animal animal) {
        if(shouldReveal) {
            boolean isValid = canClickAndPlace(this, x, y, animal);
            if (!isValid) {
                return false;
            }
        }
        setTile(x, y, shouldReveal, animal);
        return true;
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

    public Tile[][] calcWipedBoard() {
        return Game.getWipedBoard(board);
    }

    public Tile getTile(Coords coords) {
        return board[coords.x()][coords.y()];
    }

    public String hashString(Animal animalToSolve) {
        final var wipedBoard = this.calcWipedBoard();
        StringBuilder sb = new StringBuilder();
        sb  .append("s=")
                .append(animalToSolve.name())
                .append(";");
        sb.append("a=[");
        sb.append(this.getContainedAnimals().stream().map(Animal::name).reduce((a, b) -> a + "," + b).orElse(""));
        sb.append("];");
        sb.append("b=[");
        for (var x = 0; x < wipedBoard.length; x++) {
            for (var y = 0; y < wipedBoard[0].length; y++) {
                var tile = wipedBoard[x][y];
                if (tile.isRevealed() && tile.isOccupied()) {
                    sb.append(x).append(",").append(y).append("->");
                    sb.append(tile.getAnimalBoardInstance().animal().name());
                } else if(tile.isRevealed() && !tile.isOccupied()){
                    sb.append("-");
                } else {
                    sb.append("?");
                }
            }
            sb.append("/");
        }
        sb.append("];");

        return sb.toString();
    }
}
