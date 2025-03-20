package junker.disco.zoo.solver.controller;

import junker.disco.zoo.solver.model.animals.Region;
import junker.disco.zoo.solver.requests.return_objects.AccuracySingleClickResponse;
import junker.disco.zoo.solver.service.AccuracyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/accuracy")
public class AccuracyController {

    private final AccuracyService accuracyService;

    public AccuracyController(AccuracyService accuracyService) {
        this.accuracyService = accuracyService;
    }


    @GetMapping("/single-click/{seed}")
    public ResponseEntity<AccuracySingleClickResponse> getGame(@PathVariable Long seed,
                                                               @RequestParam(required = true) Region region,
                                                               @RequestParam(required = true) boolean timeless,
                                                               @RequestParam(defaultValue = "0", required = false) int gameNumber) {
        var resp = handleInvalidParams(seed, gameNumber);
        if (resp != null) return resp;


        var game = accuracyService.getSingleClickGame(seed, gameNumber, region, timeless);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    private ResponseEntity<AccuracySingleClickResponse> handleInvalidParams(Long seed, int gameNumber) {
        if (seed == null || gameNumber < 0 || gameNumber > 10) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
