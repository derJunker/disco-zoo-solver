package junker.disco.zoo.solver.controller;

import jakarta.validation.Valid;
import junker.disco.zoo.solver.requests.post_bodies.SolveRequestBody;
import junker.disco.zoo.solver.requests.return_objects.SolveResult;
import junker.disco.zoo.solver.service.SolveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/solve")
public class SolveController {

    private final SolveService solveService;

    public SolveController(SolveService solveService) {
        this.solveService = solveService;
    }


    @PostMapping
    public ResponseEntity<SolveResult> solve(@RequestBody @Valid SolveRequestBody body) {
        final var game = body.game().toGame();
        final var animalToSolverFor = body.animalToSolveFor();
        if (animalToSolverFor == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(solveService.solve(game, animalToSolverFor), HttpStatus.OK);
    }
}
