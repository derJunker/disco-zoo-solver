package junker.board;

import junker.animals.Animal;

public record AnimalBoardInstance(Animal animal, String id, Coords origin) {

    @Override
    public String toString() {
        return id;
    }
}
