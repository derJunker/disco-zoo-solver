package junker.animals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junker.board.Coords;

import static junker.board.Coords.coords;

public record Animal(
        String name,
        Rarity rarity,
        List<Coords> pattern,
        Region region
) {
    public static List<Animal> ALL_ANIMALS = new ArrayList<>();

    public Animal {
        ALL_ANIMALS.add(this);
    }

    @Override
    public String toString() {
        return name.substring(0, 1);
    }

    public static List<Animal> findAnimalsByName(String... names) {
        var animals = new ArrayList<Animal>();
        for (Animal animal : ALL_ANIMALS) {
            if (Arrays.stream(names).anyMatch(animal.name::equalsIgnoreCase)) {
                animals.add(animal);
            }
        }
        return animals;
    }

    public static List<Animal> getAnimalListByRegion(Region region, boolean timeless) {
        return ALL_ANIMALS.stream()
                .filter(spec -> spec.region.equals(region) || spec.region == Region.ANY || region == Region.ANY)
                .filter(spec -> timeless || spec.rarity != Rarity.TIMELESS)
                .toList();
    }

    public static void initAnimals() {
        Region region = Region.ANY;
        new Animal( "Discobux", Rarity.BUX, List.of(coords(0, 0)), region);
        new Animal("Discobux 2", Rarity.BUX, List.of(coords(0, 0)), region);

        region = Region.FARM;
        new Animal("Sheep", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(2, 0), coords(3, 0)),
                region);
        new Animal("Rabbit", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(0, 2), coords(0, 3)),
                region);
        new Animal("Pig", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 1), coords(1, 0)), region);
        new Animal("Horse", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(0, 2)), region);
        new Animal("Cow", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 0)), region);
        new Animal("Unicorn", Rarity.EPIC, List.of(coords(0, 0), coords(1, 1), coords(2, 1)), region);
        new Animal("Chicken", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, -1), coords(2, -2)), region);

        region = Region.OUTBACK;
        new Animal("Kangaroo", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 2), coords(3, 3)),
                region);
        new Animal("Crocodile", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(2, 0), coords(3, 0)),
                region);
        new Animal("Platypus", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(2, 1)),
                region);
        new Animal("Koala", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(1, 0)), region);
        new Animal("Cockatoo", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(1, 2)), region);
        new Animal("Tiddalik", Rarity.EPIC, List.of(coords(0, 0), coords(1, -1), coords(2, 0)), region);
        new Animal("Echidna", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, 0), coords(2, -1)), region);

        region = Region.SAVANNA;
        new Animal("Zebra", Rarity.COMMON, List.of(coords(-1, 0), coords(1, 0), coords(0, -1), coords(0, 1)),
                region);
        new Animal("Hippo", Rarity.COMMON, List.of(coords(-1, -1), coords(-1, 1), coords(1, -1), coords(1, 1)),
                region);
        new Animal("Giraffe", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(0, 2), coords(0, 3)),
                region);
        new Animal("Lion", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 0)), region);
        new Animal("Elephant", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(1, 0)), region);
        new Animal("Gryphon", Rarity.EPIC, List.of(coords(0, 0), coords(1, 1), coords(2, 0)), region);
        new Animal("Rhino", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, -1), coords(1, 1)), region);

        region = Region.NORTHERN;
        new Animal("Skunk", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, -1), coords(2, -1)),
                region);
        new Animal("Bear", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(1, 2)), region);
        new Animal("Beaver", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(2, -1), coords(2, 1)),
                region);
        new Animal("Fox", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 1)), region);
        new Animal("Moose", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, 0)), region);
        new Animal("Sasquatch", Rarity.EPIC, List.of(coords(0, 0), coords(0, 1)), region);
        new Animal("Otter", Rarity.TIMELESS, List.of(coords(0, 0), coords(0, 1), coords(1, 1)), region);

        region = Region.POLAR;
        new Animal("Penguin", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(-1, 2), coords(1, 2)), region);
        new Animal("Seal", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 2), coords(3, 1)), region);
        new Animal("Muskox", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 0), coords(2, 1)), region);
        new Animal("Polar Bear", Rarity.RARE, List.of(coords(0, 0), coords(2, 0), coords(2, 1)), region);
        new Animal("Walrus", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, 1)), region);
        new Animal("Yeti", Rarity.EPIC, List.of(coords(0, 0), coords(0, 2)), region);
        new Animal("Snowy Owl", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, 0), coords(1, 1)), region);

        region = Region.JUNGLE;
        new Animal("Monkey", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 0), coords(3, 1)), region);
        new Animal("Toucan", Rarity.COMMON, List.of(coords(0, 0), coords(-1, 1), coords(0, 2), coords(0, 3)), region);
        new Animal("Gorilla", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(2, 0), coords(2, 1)), region);
        new Animal("Panda", Rarity.RARE, List.of(coords(0, 0), coords(2, -1), coords(2, 1)), region);
        new Animal("Tiger", Rarity.RARE, List.of(coords(0, 0), coords(2, 0), coords(3, 0)), region);
        new Animal("Phoenix", Rarity.EPIC, List.of(coords(0, 0), coords(2, 2)), region);
        new Animal("Lemur", Rarity.TIMELESS, List.of(coords(0, 0), coords(-1, 1), coords(-1, -1)), region);

        region = Region.JURASSIC;
        new Animal("Diplodocus", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(1, 2), coords(2, 1)),
                region);
        new Animal("Raptor", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(2, 2)),
                region);
        new Animal("Stegosaurus", Rarity.COMMON, List.of(coords(0, 0), coords(1, -1), coords(2, -1), coords(3, 0))
        , region);
        new Animal("Triceratops", Rarity.RARE, List.of(coords(0, 0), coords(2, 1), coords(0, 2)), region);
        new Animal("T-Rex", Rarity.RARE, List.of(coords(0, 0), coords(0, 2), coords(1, 2)), region);
        new Animal("Dragon", Rarity.EPIC, List.of(coords(0, 0), coords(2, 1)), region);
        new Animal("Ankylo", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, -1), coords(2, 0)), region);

        region = Region.ICE_AGE;
        new Animal("Wooly Rhino", Rarity.COMMON, List.of(coords(0, 0), coords(2, -1), coords(3, 0), coords(1, 1))
        , region);
        new Animal("Giant Sloth", Rarity.COMMON, List.of(coords(0, 0), coords(0, 2), coords(2, 1), coords(2, 2))
                , region);
        new Animal("Dire Wolf", Rarity.COMMON, List.of(coords(0, 0), coords(1, -1), coords(1, 1), coords(3, 0)),
                region);
        new Animal("Saber Tooth", Rarity.RARE, List.of(coords(0, 0), coords(1, 2), coords(2, 1)), region);
        new Animal("Mammoth", Rarity.RARE, List.of(coords(0, 0), coords(1, -1), coords(2, 1)), region);
        new Animal("Akhlut", Rarity.EPIC, List.of(coords(0, 0), coords(2, -1), coords(2, 1)), region);
        new Animal("Yukon Camel", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, -1), coords(3, 1)), region);

        region = Region.CITY;
        new Animal("Raccoon", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(2, 0), coords(3, 1)),
                region);
        new Animal("Pigeon", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1),  coords(1, 2), coords(2, 2)),
                region);
        new Animal("Rat", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, 1), coords(3, 1)), region);
        new Animal("Squirrel", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, -1)), region);
        new Animal("Opossum", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(2, 1)), region);
        new Animal("Sewer Turtle", Rarity.EPIC, List.of(coords(0, 0), coords(1, 0)), region);
        new Animal("Chipmunk", Rarity.TIMELESS, List.of(coords(0, 0), coords(1, -1), coords(3, 0)), region);

        region = Region.MOUNTAIN;
        new Animal("Goat", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 1), coords(2, 1)), region);
        new Animal("Cougar", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1),  coords(0, 2), coords(2, 2)),
                region);
        new Animal("Elk", Rarity.COMMON, List.of(coords(0, 0), coords(1, 1), coords(2, 1), coords(2, 0)), region);
        new Animal("Eagle", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(1, 2)), region);
        new Animal("Coyote", Rarity.RARE, List.of(coords(0, 0), coords(1, 0), coords(2, 1)), region);
        new Animal("Aatxe", Rarity.EPIC, List.of(coords(0, 0), coords(2, -1)), region);
        new Animal("Pika", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, 0), coords(2, 1)), region);

        region = Region.MOON;
        new Animal("Tribble", Rarity.COMMON, List.of(coords(0, 0), coords(-1, 1), coords(0, 1), coords(1, 1)),
                region);
        new Animal("Lunar Tick", Rarity.COMMON, List.of(coords(0, 0), coords(-1, 1),  coords(1, 1), coords(0, -2))
        , region);
        new Animal("Moonkey", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(2, 1), coords(2, 2)),
                region);
        new Animal("Luna Moth", Rarity.RARE, List.of(coords(0, 0), coords(1, 2), coords(2, 0)), region);
        new Animal("Moonicorn", Rarity.RARE, List.of(coords(0, 0), coords(0, 1), coords(1, 1)), region);
        new Animal("Jade Rabbit", Rarity.EPIC, List.of(coords(0, 0), coords(1, 2)), region);
        new Animal("Babmoon", Rarity.TIMELESS, List.of(coords(0, 0), coords(-1, 2), coords(1, 1)), region);

        region = Region.MARS;
        new Animal("Marsmot", Rarity.COMMON, List.of(coords(0, 0), coords(1, 0), coords(1, -1), coords(1, -2)),
                region);
        new Animal("Marsmoset", Rarity.COMMON, List.of(coords(0, 0), coords(1, 2),  coords(2, 0), coords(2, 1)),
                region);
        new Animal("Rock", Rarity.COMMON, List.of(coords(0, 0), coords(0, 1), coords(1, 1), coords(1, 0)), region);
        new Animal("Rover", Rarity.RARE, List.of(coords(0, 0), coords(1, -1), coords(2, 0)), region);
        new Animal("Martian", Rarity.RARE, List.of(coords(0, 0), coords(1, 1), coords(2, 0)), region);
        new Animal("Marsmallow", Rarity.EPIC, List.of(coords(0, 0), coords(0, 2)), region);
        new Animal("Marsten", Rarity.TIMELESS, List.of(coords(0, 0), coords(2, 0), coords(3, 1)), region);
    }
}
