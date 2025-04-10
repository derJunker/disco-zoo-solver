package junker.disco.zoo.solver.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Rarity;
import junker.disco.zoo.solver.model.solver.Solution;
import junker.disco.zoo.solver.requests.return_objects.SolveResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public class SolveService {

    private final SlowSolutionService slowSolutionService;

    public SolveService(SlowSolutionService slowSolutionService) {
        this.slowSolutionService = slowSolutionService;
    }

    public SolveResult solve(Game game, Animal animalToSolverFor) {
        if(animalToSolverFor.rarity() == Rarity.BUX){
            var bestMoveInformation = DiscoZooSolver.getBuxProbability(game);
            return new SolveResult(solutionsToFirstClicks(bestMoveInformation.solutions()),
                    bestMoveInformation.probabilities());
        }
        final var potentialResult = slowSolutionService.getIfSaved(game, animalToSolverFor);
        if (potentialResult != null) {
//            log.info("Found saved result with animals: {} and heatmap: {}",
//                    game.getContainedAnimals().stream().map(Animal::name).collect(Collectors.toList()),
//                    animalToSolverFor.name());
            return potentialResult;
        }

        return slowSolutionService.addSolutionIfTooSlow(() -> {
            final var moveInformation = DiscoZooSolver.getBestMoveInformation(animalToSolverFor, game);
            var bestClicks = solutionsToFirstClicks(moveInformation.solutions());
            return new SolveResult(bestClicks, moveInformation.probabilities());
        }, game, animalToSolverFor);
    }

    private static Set<Coords> solutionsToFirstClicks(List<Solution> solutions) {
        return solutions.stream().map(solution -> solution.clicks().getFirst().coords()).collect(Collectors.toSet());
    }
}
