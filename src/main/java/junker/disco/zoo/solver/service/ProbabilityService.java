package junker.disco.zoo.solver.service;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.calculations.AnimalOverlapAndProbabilities;
import org.springframework.stereotype.Service;

@Service
public class ProbabilityService {
    public AnimalOverlapAndProbabilities getProbabilitiesAndOverlap(Game game, Animal animalToGetProbabilitiesFor) {
        return BoardCoverCalculator.calculateOverlapAndProbabilities(game,
                animalToGetProbabilitiesFor);
    }
}
