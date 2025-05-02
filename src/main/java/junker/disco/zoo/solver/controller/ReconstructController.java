package junker.disco.zoo.solver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.validation.Valid;
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
    public ResponseEntity<Object> start(@RequestBody @Valid ReconstructStartBody body) {
        var validateReconstructStartBody = validateReconstructStartBody(body);
        if (validateReconstructStartBody != null) {
            return validateReconstructStartBody;
        }
        var region =
                Region.byRepr(body.region()).or(() -> Optional.of(Region.valueOf(body.region().toUpperCase()))).get();
        final var petOpt = Animal.findPetByName(body.petName());
        var fullAnimals = new ArrayList<>(body.animals());
        petOpt.ifPresent(fullAnimals::add);

        final var game =  new Game(fullAnimals, region);
        return new ResponseEntity<>(new Game(game, true), HttpStatus.OK);
    }

    private static ResponseEntity<Object> validateReconstructStartBody(ReconstructStartBody body) {
        Region region;
        try {
            region =
                    Region.byRepr(body.region()).or(() -> Optional.of(Region.valueOf(body.region().toUpperCase()))).orElseThrow();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        if (body.petName() != null && Animal.findPetByName(body.petName()).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if ((body.animals() == null || body.animals().isEmpty()) && (body.petName() == null)) {
            return ResponseEntity.badRequest().build();
        }
        if (body.animals() != null) {
            if (body.animals().stream().anyMatch(animal -> !animal.region().equals(region) && animal.region() != Region.ANY)) {
                return ResponseEntity.badRequest().build();
            }
        }
        return null;
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
