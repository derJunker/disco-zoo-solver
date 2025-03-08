package junker.disco.zoo.solver.board;

import junker.disco.zoo.solver.model.animals.Animal;

public record AnimalBoardInstance(Animal animal, String id, Coords origin) {

    @Override
    public String toString() {
        return animal.name().substring(0,1) + "@" + id;
    }
}
