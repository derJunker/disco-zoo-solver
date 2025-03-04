package junker.disco.zoo.solver.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junker.animals.Animal;
import junker.board.Game;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reconstruct")
public class ReconstructController {
    @PostMapping("/start")
    public Game start(@RequestBody Animal[] animals) {
        return new Game(Arrays.asList(animals));
    }
}
