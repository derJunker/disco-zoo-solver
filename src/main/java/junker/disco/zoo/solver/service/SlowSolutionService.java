package junker.disco.zoo.solver.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.requests.return_objects.SolveResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SlowSolutionService {

    @Value("${solutions.save.threshold.ms}")
    private int saveThresholdMs;

    private static final Map<String, SolveResult> sharedSlowResultsMap = new ConcurrentHashMap<>();


    public SolveResult addSolutionIfTooSlow(Supplier<SolveResult> supplier, Game game,
                                            Animal animalToSolveFor) {
        var start = System.currentTimeMillis();
        var result = supplier.get();
        var end = System.currentTimeMillis();
        if (end - start > saveThresholdMs) {
            final var hash = hash(game, animalToSolveFor);
            sharedSlowResultsMap.put(hash, result);
            System.out.println("Saved solution, took " + (end - start) + "ms, hash: " + hash);
        }
        return result;
    }

    public SolveResult getIfSaved(Game game, Animal animalToSolveFor) {
        return sharedSlowResultsMap.get(hash(game, animalToSolveFor));
    }


    private static String hash(Game game, Animal animalToSolveFor) {
        final var wipedBoard = game.calcWipedBoard();
        StringBuilder sb = new StringBuilder();
        sb  .append("s=")
            .append(animalToSolveFor.name())
            .append(";");
        sb.append("a=[");
        sb.append(game.getContainedAnimals().stream().map(Animal::name).reduce((a, b) -> a + "," + b).orElse(""));
        sb.append("];");
        sb.append("b=[");
        for (var x = 0; x < wipedBoard.length; x++) {
            for (var y = 0; y < wipedBoard[0].length; y++) {
                var tile = wipedBoard[x][y];
                if (tile.isRevealed() && tile.isOccupied()) {
                    sb.append(x).append(",").append(y).append("->");
                    sb.append(tile.getAnimalBoardInstance().animal().name());
                } else if(tile.isRevealed() && !tile.isOccupied()){
                    sb.append("-");
                } else {
                    sb.append("?");
                }
            }
            sb.append("/");
        }
        sb.append("];");

        return sb.toString();
    }


}
