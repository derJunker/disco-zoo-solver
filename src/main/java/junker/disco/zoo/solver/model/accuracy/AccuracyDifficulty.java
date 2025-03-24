package junker.disco.zoo.solver.model.accuracy;

public enum AccuracyDifficulty {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    private final String repr;

    AccuracyDifficulty(String repr) {
        this.repr = repr;
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
}
