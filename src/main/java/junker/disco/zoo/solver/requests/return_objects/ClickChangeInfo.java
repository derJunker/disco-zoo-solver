package junker.disco.zoo.solver.requests.return_objects;

import java.util.List;
import java.util.Set;

import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.model.animals.Animal;

public record ClickChangeInfo(
        Tile updatedTile,
        boolean wasValidClick,
        Set<Animal> placeableAnimals,
        List<Animal> completelyRevealedAnimals,
        List<Animal> notCompletelyRevealedAnimalsWithoutBux

) {

}
