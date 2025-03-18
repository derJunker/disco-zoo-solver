package junker.disco.zoo.solver.controller;

import java.util.Arrays;
import java.util.List;

import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.requests.post_bodies.ReconstructClickBody;
import junker.disco.zoo.solver.requests.return_objects.ClickChangeInfo;
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
    public Game start(@RequestBody Animal[] animals) {
        final var game =  new Game(Arrays.asList(animals));
        return new Game(game, true);
    }

    @PostMapping("/click")
    public ClickChangeInfo click(@RequestBody ReconstructClickBody requestBody) {
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
        return new ClickChangeInfo(
                currentTile,
                isValid,
                completelyRevealedAnimals,
                notCompletelyRevealedAnimalsWithoutBux
        );
    }
}
