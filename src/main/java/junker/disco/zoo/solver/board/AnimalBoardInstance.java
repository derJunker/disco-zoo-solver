package junker.disco.zoo.solver.board;

import junker.disco.zoo.solver.model.animals.Animal;

public record AnimalBoardInstance(Animal animal, String id, Coords origin) {

    @Override
    public String toString() {
        return animal.name().charAt(0) + "@(" + id + ")";
    }

    public static AnimalBoardInstance of(Animal animal, Coords coords) {
        return new AnimalBoardInstance(animal, coords.x() + "|" + coords.y(), coords);
    }
}
