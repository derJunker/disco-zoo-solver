package junker.disco.zoo.solver.controller;

import java.util.List;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Region;
import junker.disco.zoo.solver.requests.post_bodies.ReconstructClickBody;
import junker.disco.zoo.solver.requests.post_bodies.ReconstructStartBody;
import junker.disco.zoo.solver.requests.return_objects.ClickChangeInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reconstruct")
@CrossOrigin
public class ReconstructController {
    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody ReconstructStartBody body) {
        var regionOpt = Region.byRepr(body.region());
        if (regionOpt.isEmpty())
            return ResponseEntity.notFound().build();
        final var game =  new Game(body.animals(), regionOpt.get());
        return new ResponseEntity<>(new Game(game, true), HttpStatus.OK);
    }

    @PostMapping("/click")
    public ResponseEntity<ClickChangeInfo> click(@RequestBody ReconstructClickBody requestBody) {
        final var game = requestBody.game().toGame();
        var animal = requestBody.animal();
        final var coords = requestBody.coords();
        final var currentTile = game.getTile(coords);
        final var clickWithSameAnimal = (currentTile.getAnimalBoardInstance() == null && animal == null) ||
                (currentTile.getAnimalBoardInstance() != null && currentTile.getAnimalBoardInstance().animal().equals(animal));
        final var shouldReveal =
                !currentTile.isRevealed() || !clickWithSameAnimal;
        if (clickWithSameAnimal && !shouldReveal) {
            animal = null;
        }
        var isValid = game.setTileIfValid(coords.x(), coords.y(), shouldReveal, animal);
        List<Animal> completelyRevealedAnimals = null;
        List<Animal> notCompletelyRevealedAnimalsWithoutBux = null;
        if (isValid) {
            completelyRevealedAnimals = game.getCompletelyRevealedAnimals();
            notCompletelyRevealedAnimalsWithoutBux = game.getNotCompletelyRevealedAnimalsWithoutBux();
        }
        return new ResponseEntity<>(new ClickChangeInfo(
                currentTile,
                isValid,
                completelyRevealedAnimals,
                notCompletelyRevealedAnimalsWithoutBux
        ), HttpStatus.OK);
    }
}
