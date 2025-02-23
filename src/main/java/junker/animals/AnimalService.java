package junker.animals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public static Optional<Animal> getAnimalByName(String name) {
        for (Animal animal : animals) {
            if (animal.name().equals(name)) {
                return Optional.of(animal);
            }
        }
        return Optional.empty(); // or throw an exception if preferred
    }

    public static List<Animal> getAnimalsByName(String... names) {
        List<Animal> result = new ArrayList<>();
        for (String name : names) {
            getAnimalByName(name).ifPresent(result::add);
        }
        return result;
    }
}
