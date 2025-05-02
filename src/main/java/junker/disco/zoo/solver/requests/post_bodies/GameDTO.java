package junker.disco.zoo.solver.requests.post_bodies;

import java.util.List;

import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidContainedAnimals;
import junker.disco.zoo.solver.controller.validation.attribute_validators.ValidRegionName;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.model.animals.Region;

public record GameDTO(
        Tile[][] board,

        @ValidContainedAnimals
        List<Animal> containedAnimals,
        @ValidRegionName
        String region
) {

    public Game toGame() {
        var optRegion = Region.byRepr(region).or(() -> java.util.Optional.of(Region.valueOf(region)));
        return new Game(board, containedAnimals, optRegion.orElseThrow());
    }
}
