package junker.disco.zoo.solver.service;

import java.util.HashSet;

import junker.animals.Animal;
import junker.board.AnimalBoardInstance;
import junker.board.Game;
import junker.board.min_cover.BoardCoverCalculator;
import junker.util.DoubleArrayUtil;
import org.springframework.stereotype.Service;

@Service
public class ProbabilityService {
    public double[][] getProbabilities(Game game, Animal animalToGetProbabilitiesFor) {
        final var overlap = BoardCoverCalculator.calculateAnimalOverlap(game, animalToGetProbabilitiesFor);
        final var overlapCount = DoubleArrayUtil.countListDoubleArray(overlap);
        final var allAnimals = new HashSet<AnimalBoardInstance>();
        final var probabilities = new double[game.getBoard().length][game.getBoard()[0].length];
        for (int y = 0; y < game.getBoard().length; y++) {
            for (int x = 0; x < game.getBoard()[0].length; x++) {
                allAnimals.addAll(overlap[x][y]);
            }
        }
        for (int y = 0; y < game.getBoard().length; y++) {
            for (int x = 0; x < game.getBoard()[0].length; x++) {
                probabilities[x][y] = ((double)overlapCount[x][y]) / allAnimals.size();
            }
        }
        return probabilities;
    }
}
