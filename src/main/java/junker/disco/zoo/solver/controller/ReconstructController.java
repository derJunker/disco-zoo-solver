package junker.disco.zoo.solver.controller;

import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import junker.animals.Animal;
import junker.board.Coords;
import junker.board.Game;
import junker.disco.zoo.solver.post_bodies.ReconstructClickBody;
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
    public Game click(@RequestBody ReconstructClickBody requestBody) {
        final var game = requestBody.game().toGame();
        var animal = requestBody.animal();
        final var coords = requestBody.coords();
        final var currentlyRevealed = game.getBoard()[coords.x()][coords.y()].isRevealed();
        if (currentlyRevealed)
            animal = null;

        game.setTileIfValid(coords.x(), coords.y(), !currentlyRevealed, animal);
        return game;
    }
}
