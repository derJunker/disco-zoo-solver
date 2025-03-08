package junker.disco.zoo.solver.service;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.min_cover.BoardCoverCalculator;
import junker.disco.zoo.solver.model.calculations.OverlapAndProbabilities;
import org.springframework.stereotype.Service;

@Service
public class ProbabilityService {
    public OverlapAndProbabilities getProbabilitiesAndOverlap(Game game, Animal animalToGetProbabilitiesFor) {
        return BoardCoverCalculator.calculateOverlapAndProbabilities(game,
                animalToGetProbabilitiesFor);
    }
}
