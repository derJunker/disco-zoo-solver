package junker.disco.zoo.solver.board.util;


import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Animal;

public class StatTracker {
    public int totalEmulateCalls = 0;
    public int initialPermutationSize = 0;

    public int successfulCachedEmulationCalls = 0;
    public int totalCachedEmulationCalls = 0;

    public long symmetricPermutations = 0;
    public long asymmetricPermutations = 0;

    public long earlyInterrupt = 0;
    public long recursiveCalls = 0;

    private long startTime = 0;
    private long endTime = 0;


    private Game game;
    private Animal animalToSearch;

    private StatTracker() {}

    public static StatTracker ofGame(Game game, Animal animalToSearch) {
        var tracker = new StatTracker();
        tracker.game = game;
        tracker.animalToSearch = animalToSearch;
        return tracker;
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public void endTimer() {
        endTime = System.currentTimeMillis();
    }

    public void printStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("Animal to search: ").append(animalToSearch.name()).append("\n");
        stats.append("Animals in game: ").append(game.getContainedAnimals().stream().map(Animal::name).toList()).append("\n");
        stats.append("Game:\n").append(game).append("\n");
        stats.append("-------------------------\n");
        stats.append("Initial permutation size: ").append(initialPermutationSize).append("\n");
        stats.append("Total emulate calls: ").append(totalEmulateCalls).append("\n");
        if (endTime > startTime) {
            stats.append("Time taken: ").append(endTime - startTime).append(" ms\n");
        }
        stats.append("Successful cached emulation calls: ").append(successfulCachedEmulationCalls).append(" of ")
                .append(totalCachedEmulationCalls).append("(").append("%.4f".formatted((double) successfulCachedEmulationCalls / totalCachedEmulationCalls * 100)).append("%)\n");
        stats.append("Symmetric permutations: ").append(symmetricPermutations).append(" of ").append((symmetricPermutations + asymmetricPermutations)).append(" (")
                .append("%.4f".formatted((double) symmetricPermutations / (symmetricPermutations + asymmetricPermutations) * 100)).append("%)\n");
        stats.append("Early interrupts: ").append(earlyInterrupt).append(" of ").append(totalEmulateCalls).append(" (")
                .append("%.4f".formatted((double) earlyInterrupt / recursiveCalls * 100)).append("%)\n");
        System.out.println(stats);
    }
}
