package junker.disco.zoo.solver.controller;

import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.requests.post_bodies.SolveRequestBody;
import junker.disco.zoo.solver.requests.return_objects.SolveResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/solve")
public class SolveController {


    @PostMapping
    public SolveResult solve(@RequestBody SolveRequestBody body) {
        final var game = body.game().toGame();
        final var animalToSolverFor = body.animalToSolveFor();
        var moveInformation = DiscoZooSolver.getBestMoveInformation(animalToSolverFor, game);
        var bestClicks =
                moveInformation.solutions().stream().map(solution -> solution.clicks().getFirst()).collect(Collectors.toSet());
        return new SolveResult(bestClicks, moveInformation.probabilities());
    }
}
