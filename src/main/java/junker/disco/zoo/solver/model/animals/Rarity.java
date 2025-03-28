package junker.disco.zoo.solver.model.animals;

import lombok.Getter;

@Getter
public enum Rarity {

    COMMON(1),
    RARE(2),
    TIMELESS(3),
    EPIC(4),
    PET(5),
    BUX(0);

    private final int value;
    Rarity(int value) {
        this.value =  value;
    }

}
