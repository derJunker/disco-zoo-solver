package junker.disco.zoo.solver.model.animals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import junker.disco.zoo.solver.board.Coords;

import static junker.disco.zoo.solver.board.Coords.coords;

public record Animal(
        String name,
        Rarity rarity,
        List<Coords> pattern,
        Region region
) {
    public static List<Animal> ALL_ANIMALS = new ArrayList<>();

    static {
        initAnimals();
    }

    @Override
    public String toString() {
        return name.substring(0, 1);
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

    public static List<Animal> getAnimalListByRegion(Region region, boolean timeless) {
        return ALL_ANIMALS.stream()
                .filter(spec -> spec.region.equals(region) || spec.region == Region.ANY || region == Region.ANY)
                .filter(spec -> timeless || spec.rarity != Rarity.TIMELESS)
                .toList();
    }

    public static void initAnimals() {
        ALL_ANIMALS.addAll(List.of(
//            new Animal( "Discobux", Rarity.BUX, List.of(coords(0, 0)), Region.ANY),
//            new Animal("Discobux 2", Rarity.BUX, List.of(coords(0, 0)), Region.ANY),

            new Animal("Sheep", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(2, 0), coords(3, 0)),
                    Region.FARM),
            new Animal("Rabbit", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(0, 2), coords(0, 3)),
                    Region.FARM),
            new Animal("Pig", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 1), coords(1, 0)), Region.FARM),
            new Animal("Horse", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(0, 2)), Region.FARM),
            new Animal("Cow", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 0)), Region.FARM),
            new Animal("Unicorn", Rarity.EPIC, List.of(coords(0, 0), coords(1, 1), coords(2, 1)), Region.FARM),
            new Animal("Chicken", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, -1), coords(2, -2)), Region.FARM),

            new Animal("Kangaroo", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 2), coords(3, 3)),
                    Region.OUTBACK),
            new Animal("Crocodile", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(2, 0), coords(3, 0)),
                    Region.OUTBACK),
            new Animal("Platypus", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(2, 1)),
                    Region.OUTBACK),
            new Animal("Koala", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(1, 0)), Region.OUTBACK),
            new Animal("Cockatoo", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(1, 2)), Region.OUTBACK),
            new Animal("Tiddalik", Rarity.EPIC, List.of(coords(0, 0), coords(1, -1), coords(2, 0)), Region.OUTBACK),
            new Animal("Echidna", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, 0), coords(2, -1)), Region.OUTBACK),

            new Animal("Zebra", Rarity.COMMON, List.of(coords(0, 0), coords(2, 0), coords(1, -1), coords(1, 1)),
                    Region.SAVANNA),
            new Animal("Hippo", Rarity.COMMON, List.of(coords(0, 0), coords(0, 2), coords(2, 0), coords(2, 2)),
                    Region.SAVANNA),
            new Animal("Giraffe", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(0, 2), coords(0, 3)),
                    Region.SAVANNA),
            new Animal("Lion", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 0)), Region.SAVANNA),
            new Animal("Elephant", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(1, 0)), Region.SAVANNA),
            new Animal("Gryphon", Rarity.EPIC, List.of(coords(0, 0), coords(1, 1), coords(2, 0)), Region.SAVANNA),
            new Animal("Rhino", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, -1), coords(1, 1)), Region.SAVANNA),

            new Animal("Skunk", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, -1), coords(2, -1)),
                    Region.NORTHERN),
            new Animal("Bear", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(1, 2)),
                    Region.NORTHERN),
            new Animal("Beaver", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(2, -1), coords(2, 1)),
                    Region.NORTHERN),
            new Animal("Fox", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 1)), Region.NORTHERN),
            new Animal("Moose", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, 0)), Region.NORTHERN),
            new Animal("Sasquatch", Rarity.EPIC, List.of(coords(0, 0), coords(0, 1)), Region.NORTHERN),
            new Animal("Otter", Rarity.TIMELESS, List.of(coords(0, 0), coords(0, 1), coords(1, 1)), Region.NORTHERN),

            new Animal("Penguin", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(-1, 2), coords(1, 2)),
                    Region.POLAR),
            new Animal("Seal", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 2), coords(3, 1)),
                    Region.POLAR),
            new Animal("Muskox", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 0), coords(2, 1)),
                    Region.POLAR),
            new Animal("Polar Bear", Rarity.RARE, List.of(coords(0, 0), coords(2, 0), coords(2, 1)), Region.POLAR),
            new Animal("Walrus", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, 1)), Region.POLAR),
            new Animal("Yeti", Rarity.EPIC, List.of(coords(0, 0), coords(0, 2)), Region.POLAR),
            new Animal("Snowy Owl", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, 0), coords(1, 1)), Region.POLAR),

            new Animal("Monkey", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 0), coords(3, 1)),
                    Region.JUNGLE),
            new Animal("Toucan", Rarity.COMMON, List.of(coords(0, 0), coords(-1, 1), coords(0, 2), coords(0, 3)),
                    Region.JUNGLE),
            new Animal("Gorilla", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(2, 0), coords(2, 1)),
                    Region.JUNGLE),
            new Animal("Panda", Rarity.RARE, List.of(coords(0, 0), coords(2, -1), coords(2, 1)), Region.JUNGLE),
            new Animal("Tiger", Rarity.RARE, List.of(coords(0, 0), coords(2, 0), coords(3, 0)), Region.JUNGLE),
            new Animal("Phoenix", Rarity.EPIC, List.of(coords(0, 0), coords(2, 2)), Region.JUNGLE),
            new Animal("Lemur", Rarity.TIMELESS, List.of(coords(0, 0), coords(-1, 1), coords(-1, -1)), Region.JUNGLE),

            new Animal("Diplodocus", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(1, 2), coords(2, 1)),
                    Region.JURASSIC),
            new Animal("Raptor", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(2, 2)),
                    Region.JURASSIC),
            new Animal("Stegosaurus", Rarity.COMMON, List.of(coords(0, 0), coords(1, -1), coords(2, -1), coords(3, 0))
            , Region.JURASSIC),
            new Animal("Triceratops", Rarity.RARE, List.of(coords(0, 0), coords(2, 1), coords(0, 2)), Region.JURASSIC),
            new Animal("T-Rex", Rarity.RARE, List.of(coords(0, 0), coords(0, 2), coords(1, 2)), Region.JURASSIC),
            new Animal("Dragon", Rarity.EPIC, List.of(coords(0, 0), coords(2, 1)), Region.JURASSIC),
            new Animal("Ankylo", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, -1), coords(2, 0)), Region.JURASSIC),

            new Animal("Wooly Rhino", Rarity.COMMON, List.of(coords(0, 0), coords(2, -1), coords(3, 0), coords(1, 1))
            , Region.ICE_AGE),
            new Animal("Giant Sloth", Rarity.COMMON, List.of(coords(0, 0), coords(0, 2), coords(2, 1), coords(2, 2))
                    , Region.ICE_AGE),
            new Animal("Dire Wolf", Rarity.COMMON, List.of(coords(0, 0), coords(1, -1), coords(1, 1), coords(3, 0)),
                    Region.ICE_AGE),
            new Animal("Saber Tooth", Rarity.RARE, List.of(coords(0, 0), coords(1, 2), coords(2, 1)), Region.ICE_AGE),
            new Animal("Mammoth", Rarity.RARE, List.of(coords(0, 0), coords(1, -1), coords(2, 1)), Region.ICE_AGE),
            new Animal("Akhlut", Rarity.EPIC, List.of(coords(0, 0), coords(2, -1), coords(2, 1)), Region.ICE_AGE),
            new Animal("Yukon Camel", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, -1), coords(3, 1)), Region.ICE_AGE),

            new Animal("Raccoon", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(2, 0), coords(3, 1)),
                    Region.CITY),
            new Animal("Pigeon", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1),  coords(1, 2), coords(2, 2)),
                    Region.CITY),
            new Animal("Rat", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(3, 1)), Region.CITY),
            new Animal("Squirrel", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, -1)), Region.CITY),
            new Animal("Opossum", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(2, 1)), Region.CITY),
            new Animal("Sewer Turtle", Rarity.EPIC, List.of(coords(0, 0), coords(1, 0)), Region.CITY),
            new Animal("Chipmunk", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, -1), coords(3, 0)), Region.CITY),


            // Mountain
            new Animal("Goat", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 1), coords(2, 1)),
                    Region.MOUNTAIN),
            new Animal("Cougar", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1),  coords(0, 2), coords(2, 2)),
                    Region.MOUNTAIN),
            new Animal("Elk", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 1), coords(2, 0)),
                    Region.MOUNTAIN),
            new Animal("Eagle", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(1, 2)), Region.MOUNTAIN),
            new Animal("Coyote", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 1)), Region.MOUNTAIN),
            new Animal("Aatxe", Rarity.EPIC, List.of(coords(0, 0), coords(2, -1)), Region.MOUNTAIN),
            new Animal("Pika", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, 0), coords(2, 1)), Region.MOUNTAIN),

            // Nocturnal
            new Animal("Badger", Rarity.COMMON, List.of(coords(0, 0), coords(0, -1), coords(2, -1), coords(2, -2)),
                    Region.NOCTURNAL),
            new Animal("Bat", Rarity.COMMON, List.of(coords(0, 0), coords(2, 0), coords(1, 1), coords(1, 2)),
                    Region.NOCTURNAL),
            new Animal("Kiwi", Rarity.COMMON, List.of(coords(0, 0), coords(1,-1), coords(2, 0), coords(2, 1)),
                    Region.NOCTURNAL),
            new Animal("Flying Squirrel", Rarity.RARE, List.of(coords(0, 0), coords(2, -1), coords(3, 0)),
                    Region.NOCTURNAL),
            new Animal("Kakapo", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(1, 2)), Region.NOCTURNAL),
            new Animal("Ghost", Rarity.EPIC, List.of(coords(0, 0), coords(1,1)), Region.NOCTURNAL),
            new Animal("Firefly", Rarity.TIMELESS, List.of(coords(0,0), coords(0, 1), coords(2, 2)), Region.NOCTURNAL),


            // Moon
            new Animal("Tribble", Rarity.COMMON, List.of(coords(0, 0), coords(-1, 1), coords(0, 1), coords(1, 1)),
                    Region.MOON),
            new Animal("Lunar Tick", Rarity.COMMON, List.of(coords(0, 0), coords(-1, 1),  coords(1, 1), coords(0, -2))
            , Region.MOON),
            new Animal("Moonkey", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(2, 1), coords(2, 2)),
                    Region.MOON),
            new Animal("Luna Moth", Rarity.RARE, List.of(coords(0, 0), coords(1, 2), coords(2, 0)), Region.MOON),
            new Animal("Moonicorn", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(1, 1)), Region.MOON),
            new Animal("Jade Rabbit", Rarity.EPIC, List.of(coords(0, 0), coords(1, 2)), Region.MOON),
            new Animal("Babmoon", Rarity.TIMELESS, List.of(coords(0, 0), coords(-1, 2), coords(1, 1)), Region.MOON),

            // Mars
            new Animal("Marsmot", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, -1), coords(1, -2)),
                    Region.MARS),
            new Animal("Marsmoset", Rarity.COMMON, List.of(coords(0, 0), coords(1, 2),  coords(2, 0), coords(2, 1)),
                    Region.MARS),
            new Animal("Rock", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 1), coords(1, 0)), Region.MARS),
            new Animal("Rover", Rarity.RARE, List.of(coords(0, 0), coords(1, -1), coords(2, 0)), Region.MARS),
            new Animal("Martian", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, 0)), Region.MARS),
            new Animal("Marsmallow", Rarity.EPIC, List.of(coords(0, 0), coords(0, 2)), Region.MARS),
            new Animal("Marsten", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, 0), coords(3, 1)), Region.MARS),
            // Constellation

            new Animal("Chamaeleon", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, -1), coords(3, -1)),
                    Region.CONSTELLATION),
            new Animal("Corvus", Rarity.COMMON, List.of(coords(0, 0), coords(2, 0), coords(1, -2), coords(2, -2)),
                    Region.CONSTELLATION),
            new Animal("Lynx", Rarity.COMMON, List.of(coords(0, 0), coords(0, -1), coords(1, -1), coords(1, -2)),
                    Region.CONSTELLATION),
            new Animal("Pisces", Rarity.RARE, List.of(coords(0, 0), coords(1, -2), coords(2, -1)),
                    Region.CONSTELLATION),
            new Animal("Capricornus", Rarity.RARE,  List.of(coords(0, 0), coords(2, 1), coords(3, 0)),
                    Region.CONSTELLATION),
            new Animal("Pegasus", Rarity.EPIC,  List.of(coords(0,0), coords(2, -2)), Region.CONSTELLATION),
            new Animal("Horologium", Rarity.TIMELESS,  List.of(coords(0, 0), coords(2, 1), coords(2, 2)),
                    Region.CONSTELLATION)
        ));
    }
}
