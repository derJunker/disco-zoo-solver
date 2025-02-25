package junker.animals;

public enum Rarity {

    COMMON(1),
    RARE(2),
    TIMELESS(3),
    EPIC(4),
    BUX(0);

    private final int value;
    Rarity(int value) {
        this.value =  value;
    }

    public int getValue() {
        return value;
    }
}
