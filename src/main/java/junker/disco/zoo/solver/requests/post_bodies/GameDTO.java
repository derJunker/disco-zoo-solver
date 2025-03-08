package junker.disco.zoo.solver.requests.post_bodies;

import java.util.List;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.Tile;

public record GameDTO(
        Tile[][] board,
        List<Animal> containedAnimals
) {

    public Game toGame() {
        return new Game(board, containedAnimals);
    }
}
