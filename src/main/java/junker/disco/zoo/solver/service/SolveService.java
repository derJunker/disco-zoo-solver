package junker.disco.zoo.solver.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Rarity;
import junker.disco.zoo.solver.requests.return_objects.SolveResult;
import org.springframework.stereotype.Service;

@Service
public class SolveService {

    private final SlowSolutionService slowSolutionService;

    public SolveService(SlowSolutionService slowSolutionService) {
        this.slowSolutionService = slowSolutionService;
    }

    public SolveResult solve(Game game, Animal animalToSolverFor) {
        if (animalToSolverFor.rarity() == Rarity.BUX) {
            var probs = DiscoZooSolver.getInvertedProbabilities(game);
            return new SolveResult(Set.of(), probs);
        }
        final var potentialResult = slowSolutionService.getIfSaved(game, animalToSolverFor);
        if (potentialResult != null) {
            System.out.println("Returning saved solution");
            return potentialResult;
        }

        return slowSolutionService.addSolutionIfTooSlow(() -> {
            final var moveInformation = DiscoZooSolver.getBestMoveInformation(animalToSolverFor, game);
            var bestClicks =
                    moveInformation.solutions().stream().map(solution -> solution.clicks().getFirst()).collect(Collectors.toSet());
            return new SolveResult(bestClicks, moveInformation.probabilities());
        }, game, animalToSolverFor);
    }
}
