package junker.board;

import java.util.List;

import junker.animals.Animal;

public class BoardService {
    public static void placeAnimal(Tile[][] board, Animal animal, Coords coords, short id) {
        var instance = new AnimalBoardInstance(animal, id);
        for (Coords coord : animal.pattern()) {
            int x = coords.x() + coord.x();
            int y = coords.y() + coord.y();
            board[x][y].setAnimalBoardInstance(instance);
        }
    }

    public static List<Animal> getContainedAnimals(Tile[][] board) {
        var animals = new java.util.ArrayList<Animal>();
        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile.isOccupied()) {
                    animals.add(tile.getAnimalBoardInstance().animal());
                }
            }
        }
        return animals;
    }

    public static List<Coords> getRevealedCoordsForAnimal(Tile[][] board, Animal animal) {
        var revealedCoords = new java.util.ArrayList<Coords>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].animalInstanceOfType(animal) && board[i][j].isRevealed()) {
                    revealedCoords.add(new Coords(i, j));
                }
            }
        }
        return revealedCoords;
    }

    public static Tile[][] cloneBoard(Tile[][] board) {
        Tile[][] clone = new Tile[board.length][board[0].length];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                clone[x][y] = new Tile(board[x][y]);
            }
        }
        return clone;
    }
}
