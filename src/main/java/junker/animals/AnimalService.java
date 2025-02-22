package junker.animals;

import java.util.List;

import junker.board.Coords;

public class AnimalService {
    public static List<Animal> animals = List.of(
            new Animal("Sasquatch", List.of(
                    new Coords(0, 0),
                    new Coords(0, 1)
                )
            ),
            new Animal("Koala", List.of(
                    new Coords(0, 0),
                    new Coords(1, 0),
                    new Coords(1, 1)
                )
            )
    );

    public static Animal getAnimalByName(String name) {
        for (Animal animal : animals) {
            if (animal.name().equals(name)) {
                return animal;
            }
        }
        return null; // or throw an exception if preferred
    }
}
