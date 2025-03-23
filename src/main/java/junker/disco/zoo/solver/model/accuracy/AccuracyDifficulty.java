package junker.disco.zoo.solver.model.accuracy;

public enum AccuracyDifficulty {
    EASY("easy", 1),
    MEDIUM("medium", 2),
    HARD("hard", 3);

    private final String repr;
    private final int animalAmount;

    AccuracyDifficulty(String repr, int animalAmount) {
        this.repr = repr;
        this.animalAmount = animalAmount;
    }

    public static AccuracyDifficulty byRepr(String repr) {
        for (var difficulty : values()) {
            if (difficulty.repr.equals(repr)) {
                return difficulty;
            }
        }
        throw new IllegalArgumentException("Invalid difficulty: " + repr);
    }

    public String repr() {
        return repr;
    }

    public int animalAmount() {
        return animalAmount;
    }
}
