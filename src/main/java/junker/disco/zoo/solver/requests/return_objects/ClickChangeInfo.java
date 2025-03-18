package junker.disco.zoo.solver.requests.return_objects;

import java.util.List;

import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.model.animals.Animal;

public record ClickChangeInfo(
        Tile updatedTile,
        boolean wasValidClick,
        List<Animal> completelyRevealedAnimals,
        List<Animal> notCompletelyRevealedAnimalsWithoutBux

) {

}
