package junker.disco.zoo.solver.board.solve.speedup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junker.disco.zoo.solver.board.Click;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.util.StatTracker;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Solution;


public class SingularBoardCalcTracker {
    private final Map<String, PartialGameResult> partialGameResults = new HashMap<>();

    public List<Solution> getIfPresent(Game game, Animal animalToSolve, List<Click> previousClicks, StatTracker tracker) {
        var gameHash = game.hashString(animalToSolve);
        tracker.totalCachedEmulationCalls++;
        if (partialGameResults.containsKey(gameHash)) {
            tracker.successfulCachedEmulationCalls++;
            var partialGameResult = partialGameResults.get(gameHash);
            var partialSolutions = partialGameResult.remainingClickSolution();
            return partialSolutions.stream().map(partial -> {
                var solutionClicks = new ArrayList<>(previousClicks);
                solutionClicks.addAll(partial.clicks());
                return new Solution(solutionClicks);
            }).toList();
        }
        return null;
    }


    public void add(Game game, List<Solution> fullSolutions, Animal animalToSolve, List<Click> previousClicks) {
        var gameHash = game.hashString(animalToSolve);
        var partialSolutions =
                fullSolutions.parallelStream().map(fullSolution -> new Solution(fullSolution.clicks().subList(previousClicks.size(),
                        fullSolution.clicks().size()))).distinct().toList();
        var partialGameResult = new PartialGameResult(partialSolutions);
        partialGameResults.put(gameHash, partialGameResult);
    }
}

