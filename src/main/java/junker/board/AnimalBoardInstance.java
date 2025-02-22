package junker.board;

import junker.animals.Animal;

public record AnimalBoardInstance(Animal animal, short id) {

    @Override
    public String toString() {
        return animal.name().charAt(0) + "@" + id;
    }
}
