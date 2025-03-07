package junker.board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import junker.animals.Animal;
import junker.board.min_cover.BoardCoverCalculator;
import junker.board.min_cover.Solution;
import junker.board.probabiltiy.PermutationService;

import static junker.board.BoardService.cloneBoard;
import static junker.board.min_cover.BoardCoverCalculator.calculateOverallOverlap;
import static junker.board.min_cover.BoardCoverCalculator.getAnimalOverlap;
import static junker.board.min_cover.BoardCoverCalculator.uniqueInstances;
import static junker.board.probabiltiy.PermutationService.canClickAndPlace;
import static junker.util.DoubleArrayUtil.arrayAsCoordinatesString;

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

    public Game(Tile[][] board, List<Animal> containedAnimals) {
        this.board = board;
        this.containedAnimals = containedAnimals;
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

    public Set<Coords> getBestClicks(Animal animalToSearch) {
        var minCover = BoardCoverCalculator.minCoveringSets(this, animalToSearch, false);
        return minCover.stream().map(sol -> sol.clicks().getFirst()).collect(Collectors.toSet());
    }

    public Set<Solution> getBestSolutions(Animal animalToSearch) {
        return BoardCoverCalculator.minCoveringSets(this, animalToSearch, true);
    }

    @Override
    public String toString() {
        return arrayAsCoordinatesString(board);
    }

    public boolean isSolvedFor(Animal animalToBeSolved) {
        return getSolvedTileCount(animalToBeSolved) == animalToBeSolved.pattern().size();
    }

    public int getSolvedTileCount(Animal animalToSearch) {
        var overallOverlap = calculateOverallOverlap(calcWipedBoard(), containedAnimals);
        var overlap = getAnimalOverlap(overallOverlap, animalToSearch);
        var occuringInstances = uniqueInstances(overlap);
        if (occuringInstances.isEmpty()) {
            System.out.println(arrayAsCoordinatesString(overallOverlap));;
            throw new IllegalStateException("Animal not found on board: " + animalToSearch.name());
        }
        var solvedTileCount = 0;
        for (int x = 0; x < overlap.length; x++) {
            for (int y = 0; y < overlap[0].length; y++) {
                var tileOverlap = overlap[x][y];
                if (new HashSet<>(tileOverlap).containsAll(occuringInstances) && tileOverlap.size() == occuringInstances.size()) {
                    solvedTileCount++;
                }
            }
        }
        return solvedTileCount;
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

    public Tile[][] calcWipedBoard() {
        return Game.getWipedBoard(board);
    }
}
