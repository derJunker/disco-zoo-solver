package junker.disco.zoo.solver.board;

import java.util.List;

import junker.disco.zoo.solver.model.animals.Animal;

public class BoardService {
    public static void placeAnimal(Tile[][] board, Animal animal, Coords coords) {
        var instance = new AnimalBoardInstance(animal, coords.x() + " " + coords.y(), coords);
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

    public static Tile[][] cloneBoard(Tile[][] board) {
        Tile[][] clone = new Tile[board.length][board[0].length];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                clone[x][y] = new Tile(board[x][y]);
            }
        }
        return clone;
    }
}
