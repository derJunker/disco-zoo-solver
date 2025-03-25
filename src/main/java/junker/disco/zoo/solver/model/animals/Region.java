package junker.disco.zoo.solver.model.animals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public enum Region {
    FARM("Farm", 0),
    OUTBACK("Outback", 1),
    SAVANNA("Savanna", 2),
    NORTHERN("Northern", 3),
    POLAR("Polar", 4),
    JUNGLE("Jungle", 5),
    JURASSIC("Jurassic", 6),
    ICE_AGE("Ice Age", 7),
    CITY("City", 8),
    MOUNTAIN("Mountain", 9),
    NOCTURNAL("Nocturnal", 10),

    MOON("Moon", 15),
    MARS("Mars", 16),
    CONSTELLATION("Constellation", 17),

    ANY("Any", 100);

    private final String repr;
    private final int order;

    Region(String repr, int order) {
        this.repr = repr;
        this.order = order;
    }

    public static Optional<Region> byRepr(String repr) {
        return Arrays.stream(Region.values()).filter(region -> region.repr.equalsIgnoreCase(repr)).findFirst();
    }

    public static List<String> getRegionReprs() {
        return Region.exclusiveValues().stream().map(region -> region.repr).toList();
    }

    public static List<Region> exclusiveValues() {
        var list = new ArrayList<>(Arrays.asList(values()));
        list.remove(ANY);
        return list;
    }
}
