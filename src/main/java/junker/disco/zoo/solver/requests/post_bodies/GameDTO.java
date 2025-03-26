package junker.disco.zoo.solver.requests.post_bodies;

import java.util.List;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.model.animals.Region;

public record GameDTO(
        Tile[][] board,
        List<Animal> containedAnimals,
        String region
) {

    public Game toGame() {
        var optRegion = Region.byRepr(region).or(() -> java.util.Optional.of(Region.valueOf(region)));
        return new Game(board, containedAnimals, optRegion.orElseThrow());
    }
}
