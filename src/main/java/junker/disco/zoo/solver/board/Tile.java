package junker.disco.zoo.solver.board;

import junker.disco.zoo.solver.model.animals.Animal;

public class Tile {
    private AnimalBoardInstance animalBoardInstance;
    boolean isRevealed;

    public Tile() {
        this.animalBoardInstance = null;
        this.isRevealed = false;
    }

    public Tile(Tile copyThis) {
        this.animalBoardInstance = copyThis.animalBoardInstance;
        this.isRevealed = copyThis.isRevealed;
    }

    public boolean isOccupied() {
        return animalBoardInstance != null;
    }

    public boolean hasAnimalInstanceOfType(Animal animal) {
        if (animalBoardInstance == null) {
            return false;
        }
        return animalBoardInstance.animal().equals(animal);
    }

    public void removeHiddenAnimalData() {
        if (!isRevealed) {
            animalBoardInstance = null;
        }
    }

    @Override
    public String toString() {

        if (isRevealed) {
            if (animalBoardInstance == null) {
                return "0";
            }
            return animalBoardInstance.toString();
        } else {
            if (animalBoardInstance == null) {
                return "X";
            }
            return "(" + animalBoardInstance + ")";
        }
    }

    public AnimalBoardInstance getAnimalBoardInstance() {
        return animalBoardInstance;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public void setAnimalBoardInstance(AnimalBoardInstance instance) {
        this.animalBoardInstance = instance;
    }
}
