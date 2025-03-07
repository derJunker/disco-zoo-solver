package junker.disco.zoo.solver.requests.post_bodies;

import java.util.List;

import junker.animals.Animal;
import junker.board.Game;
import junker.board.Tile;

public record GameDTO(
        Tile[][] board,
        List<Animal> containedAnimals
) {

    public Game toGame() {
        return new Game(board, containedAnimals);
    }
}
