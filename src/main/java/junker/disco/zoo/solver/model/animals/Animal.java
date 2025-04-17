package junker.disco.zoo.solver.model.animals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.util.AnimalSymmetryFinder;

import static junker.disco.zoo.solver.board.Coords.coords;

public record Animal(
        String name,
        Rarity rarity,
        List<Coords> pattern,
        Region region,
        boolean horizontalSymmetry,
        boolean verticalSymmetry
) {
    public static List<Animal> ALL_ANIMALS = new ArrayList<>();
    public static List<Animal> ALL_PETS = new ArrayList<>();

    static {
        initAnimals();
        initPets();
    }


    @Override
    public String toString() {
        return name.substring(0, 1);
    }

    public static Animal of(String name, Rarity rarity, List<Coords> pattern, Region region) {
        var symmetries = AnimalSymmetryFinder.isSymmetric(pattern);
        return new Animal(name, rarity, pattern, region, symmetries.get("vertical"), symmetries.get("horizontal"));
    }

    public static List<Animal> findAnimalsByName(String... names) {
        var animals = Arrays.stream(names)
                .map(name -> ALL_ANIMALS.stream()
                        .filter(animal -> animal.name().equalsIgnoreCase(name))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
        return animals;
    }

    public static Optional<Animal> findPetByName(String name) {
        return ALL_PETS.stream()
                .filter(animal -> animal.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public static List<Animal> getAnimalListByRegion(Region region, boolean timeless) {
        return ALL_ANIMALS.stream()
                .filter(spec -> spec.region.equals(region) || region == Region.ANY)
                .filter(spec -> timeless || spec.rarity != Rarity.TIMELESS)
                .toList();
    }

    public static void initAnimals() {
        ALL_ANIMALS.addAll(List.of(
            Animal.of( "Discobux", Rarity.BUX, List.of(coords(0, 0)), Region.ANY),

            Animal.of("Sheep", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(2, 0), coords(3, 0)),
                    Region.FARM),
            Animal.of("Rabbit", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(0, 2), coords(0, 3)),
                    Region.FARM),
            Animal.of("Pig", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 1), coords(1, 0)),
            Region.FARM),
            Animal.of("Horse", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(0, 2)), Region.FARM),
            Animal.of("Cow", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 0)), Region.FARM),
            Animal.of("Unicorn", Rarity.EPIC, List.of(coords(0, 0), coords(1, 1), coords(2, 1)), Region.FARM),
            Animal.of("Chicken", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, -1), coords(2, -2)), Region.FARM),

            Animal.of("Kangaroo", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 2), coords(3, 3)),
                    Region.OUTBACK),
            Animal.of("Crocodile", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(2, 0), coords(3, 0)),
                    Region.OUTBACK),
            Animal.of("Platypus", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(2, 1)),
                    Region.OUTBACK),
            Animal.of("Koala", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(1, 0)), Region.OUTBACK),
            Animal.of("Cockatoo", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(1, 2)), Region.OUTBACK),
            Animal.of("Tiddalik", Rarity.EPIC, List.of(coords(0, 0), coords(1, -1), coords(2, 0)), Region.OUTBACK),
            Animal.of("Echidna", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, 0), coords(2, -1)), Region.OUTBACK),

            Animal.of("Zebra", Rarity.COMMON, List.of(coords(0, 0), coords(2, 0), coords(1, -1), coords(1, 1)),
                    Region.SAVANNA),
            Animal.of("Hippo", Rarity.COMMON, List.of(coords(0, 0), coords(0, 2), coords(2, 0), coords(2, 2)),
                    Region.SAVANNA),
            Animal.of("Giraffe", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(0, 2), coords(0, 3)),
                    Region.SAVANNA),
            Animal.of("Lion", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 0)), Region.SAVANNA),
            Animal.of("Elephant", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(1, 0)), Region.SAVANNA),
            Animal.of("Gryphon", Rarity.EPIC, List.of(coords(0, 0), coords(1, 1), coords(2, 0)), Region.SAVANNA),
            Animal.of("Rhino", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, -1), coords(1, 1)), Region.SAVANNA),

            Animal.of("Skunk", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, -1), coords(2, -1)),
                    Region.NORTHERN),
            Animal.of("Bear", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(1, 2)),
                    Region.NORTHERN),
            Animal.of("Beaver", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(2, -1), coords(2, 1)),
                    Region.NORTHERN),
            Animal.of("Fox", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 1)), Region.NORTHERN),
            Animal.of("Moose", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, 0)), Region.NORTHERN),
            Animal.of("Sasquatch", Rarity.EPIC, List.of(coords(0, 0), coords(0, 1)), Region.NORTHERN),
            Animal.of("Otter", Rarity.TIMELESS, List.of(coords(0, 0), coords(0, 1), coords(1, 1)), Region.NORTHERN),

            Animal.of("Penguin", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(-1, 2), coords(1, 2)),
                    Region.POLAR),
            Animal.of("Seal", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 2), coords(3, 1)),
                    Region.POLAR),
            Animal.of("Muskox", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 0), coords(2, 1)),
                    Region.POLAR),
            Animal.of("Polar Bear", Rarity.RARE, List.of(coords(0, 0), coords(2, 0), coords(2, 1)), Region.POLAR),
            Animal.of("Walrus", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, 1)), Region.POLAR),
            Animal.of("Yeti", Rarity.EPIC, List.of(coords(0, 0), coords(0, 2)), Region.POLAR),
            Animal.of("Snowy Owl", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, 0), coords(1, 1)), Region.POLAR),

            Animal.of("Monkey", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 0), coords(3, 1)),
                    Region.JUNGLE),
            Animal.of("Toucan", Rarity.COMMON, List.of(coords(0, 0), coords(-1, 1), coords(0, 2), coords(0, 3)),
                    Region.JUNGLE),
            Animal.of("Gorilla", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(2, 0), coords(2, 1)),
                    Region.JUNGLE),
            Animal.of("Panda", Rarity.RARE, List.of(coords(0, 0), coords(2, -1), coords(2, 1)), Region.JUNGLE),
            Animal.of("Tiger", Rarity.RARE, List.of(coords(0, 0), coords(2, 0), coords(3, 0)), Region.JUNGLE),
            Animal.of("Phoenix", Rarity.EPIC, List.of(coords(0, 0), coords(2, 2)), Region.JUNGLE),
            Animal.of("Lemur", Rarity.TIMELESS, List.of(coords(0, 0), coords(-1, 1), coords(-1, -1)), Region.JUNGLE),

            Animal.of("Diplodocus", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(1, 2), coords(2, 1)),
                    Region.JURASSIC),
            Animal.of("Raptor", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(2, 2)),
                    Region.JURASSIC),
            Animal.of("Stegosaurus", Rarity.COMMON, List.of(coords(0, 0), coords(1, -1), coords(2, -1), coords(3, 0))
            , Region.JURASSIC),
            Animal.of("Triceratops", Rarity.RARE, List.of(coords(0, 0), coords(2, 1), coords(0, 2)), Region.JURASSIC),
            Animal.of("T-Rex", Rarity.RARE, List.of(coords(0, 0), coords(0, 2), coords(1, 2)), Region.JURASSIC),
            Animal.of("Dragon", Rarity.EPIC, List.of(coords(0, 0), coords(2, 1)), Region.JURASSIC),
            Animal.of("Ankylo", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, -1), coords(2, 0)), Region.JURASSIC),

            Animal.of("Wooly Rhino", Rarity.COMMON, List.of(coords(0, 0), coords(2, -1), coords(3, 0), coords(1, 1))
            , Region.ICE_AGE),
            Animal.of("Giant Sloth", Rarity.COMMON, List.of(coords(0, 0), coords(0, 2), coords(2, 1), coords(2, 2))
                    , Region.ICE_AGE),
            Animal.of("Dire Wolf", Rarity.COMMON, List.of(coords(0, 0), coords(1, -1), coords(1, 1), coords(3, 0)),
                    Region.ICE_AGE),
            Animal.of("Saber Tooth", Rarity.RARE, List.of(coords(0, 0), coords(1, 2), coords(2, 1)), Region.ICE_AGE),
            Animal.of("Mammoth", Rarity.RARE, List.of(coords(0, 0), coords(1, -1), coords(2, 1)), Region.ICE_AGE),
            Animal.of("Akhlut", Rarity.EPIC, List.of(coords(0, 0), coords(2, -1), coords(2, 1)), Region.ICE_AGE),
            Animal.of("Yukon Camel", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, -1), coords(3, 1)),
            Region.ICE_AGE),

            Animal.of("Raccoon", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(2, 0), coords(3, 1)),
                    Region.CITY),
            Animal.of("Pigeon", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1),  coords(1, 2), coords(2, 2)),
                    Region.CITY),
            Animal.of("Rat", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(3, 1)),
            Region.CITY),
            Animal.of("Squirrel", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, -1)), Region.CITY),
            Animal.of("Opossum", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(2, 1)), Region.CITY),
            Animal.of("Sewer Turtle", Rarity.EPIC, List.of(coords(0, 0), coords(1, 0)), Region.CITY),
            Animal.of("Chipmunk", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, -1), coords(3, 0)), Region.CITY),


            // Mountain
            Animal.of("Goat", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 1), coords(2, 1)),
                    Region.MOUNTAIN),
            Animal.of("Cougar", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1),  coords(0, 2), coords(2, 2)),
                    Region.MOUNTAIN),
            Animal.of("Elk", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 1), coords(2, 0)),
                    Region.MOUNTAIN),
            Animal.of("Eagle", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(1, 2)), Region.MOUNTAIN),
            Animal.of("Coyote", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 1)), Region.MOUNTAIN),
            Animal.of("Aatxe", Rarity.EPIC, List.of(coords(0, 0), coords(2, -1)), Region.MOUNTAIN),
            Animal.of("Pika", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, 0), coords(2, 1)), Region.MOUNTAIN),

            // Nocturnal
            Animal.of("Badger", Rarity.COMMON, List.of(coords(0, 0), coords(0, -1), coords(2, -1), coords(2, -2)),
                    Region.NOCTURNAL),
            Animal.of("Bat", Rarity.COMMON, List.of(coords(0, 0), coords(2, 0), coords(1, 1), coords(1, 2)),
                    Region.NOCTURNAL),
            Animal.of("Kiwi", Rarity.COMMON, List.of(coords(0, 0), coords(1,-1), coords(2, 0), coords(2, 1)),
                    Region.NOCTURNAL),
            Animal.of("Flying Squirrel", Rarity.RARE, List.of(coords(0, 0), coords(2, -1), coords(3, 0)),
                    Region.NOCTURNAL),
            Animal.of("Kakapo", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(1, 2)), Region.NOCTURNAL),
            Animal.of("Ghost", Rarity.EPIC, List.of(coords(0, 0), coords(1,1)), Region.NOCTURNAL),
            Animal.of("Firefly", Rarity.TIMELESS, List.of(coords(0,0), coords(0, 1), coords(2, 2)), Region.NOCTURNAL),


            // Moon
            Animal.of("Tribble", Rarity.COMMON, List.of(coords(0, 0), coords(-1, 1), coords(0, 1), coords(1, 1)),
                    Region.MOON),
            Animal.of("Lunar Tick", Rarity.COMMON, List.of(coords(0, 0), coords(-1, 1),  coords(1, 1), coords(0, -2))
            , Region.MOON),
            Animal.of("Moonkey", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(2, 1), coords(2, 2)),
                    Region.MOON),
            Animal.of("Luna Moth", Rarity.RARE, List.of(coords(0, 0), coords(1, 2), coords(2, 0)), Region.MOON),
            Animal.of("Moonicorn", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(1, 1)), Region.MOON),
            Animal.of("Jade Rabbit", Rarity.EPIC, List.of(coords(0, 0), coords(1, 2)), Region.MOON),
            Animal.of("Babmoon", Rarity.TIMELESS, List.of(coords(0, 0), coords(-1, 2), coords(1, 1)), Region.MOON),

            // Mars
            Animal.of("Marsmot", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, -1), coords(1, -2)),
                    Region.MARS),
            Animal.of("Marsmoset", Rarity.COMMON, List.of(coords(0, 0), coords(1, 2),  coords(2, 0), coords(2, 1)),
                    Region.MARS),
            Animal.of("Rock", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 1), coords(1, 0)),
            Region.MARS),
            Animal.of("Rover", Rarity.RARE, List.of(coords(0, 0), coords(1, -1), coords(2, 0)), Region.MARS),
            Animal.of("Martian", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, 0)), Region.MARS),
            Animal.of("Marsmallow", Rarity.EPIC, List.of(coords(0, 0), coords(0, 2)), Region.MARS),
            Animal.of("Marsten", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, 0), coords(3, 1)), Region.MARS),
            // Constellation

            Animal.of("Chamaeleon", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, -1), coords(3, -1)),
                    Region.CONSTELLATION),
            Animal.of("Corvus", Rarity.COMMON, List.of(coords(0, 0), coords(2, 0), coords(1, -2), coords(2, -2)),
                    Region.CONSTELLATION),
            Animal.of("Lynx", Rarity.COMMON, List.of(coords(0, 0), coords(0, -1), coords(1, -1), coords(1, -2)),
                    Region.CONSTELLATION),
            Animal.of("Pisces", Rarity.RARE, List.of(coords(0, 0), coords(1, -2), coords(2, -1)),
                    Region.CONSTELLATION),
            Animal.of("Capricornus", Rarity.RARE,  List.of(coords(0, 0), coords(2, 1), coords(3, 0)),
                    Region.CONSTELLATION),
            Animal.of("Pegasus", Rarity.EPIC,  List.of(coords(0,0), coords(2, -2)), Region.CONSTELLATION),
            Animal.of("Horologium", Rarity.TIMELESS,  List.of(coords(0, 0), coords(2, 1), coords(2, 2)),
                    Region.CONSTELLATION)
        ));
    }

    public static void initPets() {
        ALL_PETS.addAll(List.of(
                Animal.of("Rabbit", Rarity.PET, List.of(coords(0,0), coords(0, 1), coords(1, 2), coords(2, 1),
                        coords(2, 0)), Region.ANY),
                Animal.of("Bird", Rarity.PET, List.of(coords(0,0), coords(1, 1), coords(2, 0), coords(3,1), coords(4,
                                0)),
                Region.ANY),
                Animal.of("Dog", Rarity.PET, List.of(coords(0,0), coords(0, 1), coords(1, 1), coords(1, 0), coords(1
                        ,-1)),
                        Region.ANY),
                Animal.of("Cat", Rarity.PET, List.of(coords(0,0), coords(1, 0), coords(0,1), coords(0,2), coords(1,2)),
                        Region.ANY),
                Animal.of("Fish", Rarity.PET, List.of(coords(0,0), coords(1,0), coords(2,0), coords(3,1), coords(3,
                        -1)),
                        Region.ANY),
                Animal.of("Turtle", Rarity.PET, List.of(coords(0,0), coords(1,0), coords(1,1), coords(1,2), coords(2
                        ,0)),
                        Region.ANY),
                Animal.of("Lizard", Rarity.PET, List.of(coords(0,0), coords(0, 1), coords(0,2), coords(1,2),
                        coords(2,2)),
                        Region.ANY),
                Animal.of("Hamster", Rarity.PET, List.of(coords(0,0), coords(1,1), coords(0,2), coords(2,0),
                        coords(2,2)),
                        Region.ANY)

        ));
    }
}
