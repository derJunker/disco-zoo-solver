package junker.animals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    MOON("Moon", 10),

    MARS("Mars", 11),

    ANY("Any", 100);

    private final String repr;
    private final int order;

    Region(String repr, int order) {
        this.repr = repr;
        this.order = order;
    }

    public static Region byRepr(String repr) {
        return Arrays.stream(Region.values()).filter(region -> region.repr.equalsIgnoreCase(repr)).findFirst().orElse(null);
    }

    public static List<Region> exclusiveValues() {
        var list = new ArrayList<>(Arrays.asList(values()));
        list.remove(ANY);
        return list;
    }
}
