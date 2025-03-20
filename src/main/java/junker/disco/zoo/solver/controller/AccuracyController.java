package junker.disco.zoo.solver.controller;

import junker.disco.zoo.solver.model.animals.Region;
import junker.disco.zoo.solver.requests.post_bodies.AccuracyClickBody;
import junker.disco.zoo.solver.requests.return_objects.AccuracySingleClickGameResponse;
import junker.disco.zoo.solver.requests.return_objects.AccuracySingleClickPerformanceResponse;
import junker.disco.zoo.solver.service.AccuracyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<AccuracySingleClickGameResponse> getGame(@PathVariable Long seed,
                                                                   @RequestParam(required = true, name = "region") String regionStr,
                                                                   @RequestParam(required = true) boolean timeless,
                                                                   @RequestParam(defaultValue = "0", required = false) int gameNumber) {
        var resp = handleInvalidParams(seed, gameNumber, regionStr);
        if (resp != null) return resp;

        var region = Region.byRepr(regionStr);

        var game = accuracyService.getSingleClickGame(seed, gameNumber, region, timeless);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccuracySingleClickPerformanceResponse> clicked(@RequestBody AccuracyClickBody body) {
        var game = body.game().toGame();
        var click = body.click();
        var animalToFind = body.animalToFind();

        var performance = accuracyService.clicked(game, animalToFind, click);
        return new ResponseEntity<>(performance, HttpStatus.OK);
    }

    private ResponseEntity<AccuracySingleClickGameResponse> handleInvalidParams(Long seed, int gameNumber, String regionStr) {
        if (seed == null || gameNumber < 0 || gameNumber > 10 || Region.byRepr(regionStr) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
