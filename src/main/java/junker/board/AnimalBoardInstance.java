package junker.board;

import junker.animals.Animal;

public record AnimalBoardInstance(Animal animal, String id, Coords origin) {

    @Override
    public String toString() {
        return animal.name().substring(0,1) + "@" + id;
    }
}
