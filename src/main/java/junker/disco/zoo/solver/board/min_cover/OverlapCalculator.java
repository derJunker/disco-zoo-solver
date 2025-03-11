package junker.disco.zoo.solver.board.min_cover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.probabiltiy.PermutationService;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.calculations.AnimalOverlapAndProbabilities;
import junker.disco.zoo.solver.model.calculations.OverlapsAndProbabilities;

public class OverlapCalculator {
    public static OverlapsAndProbabilities calculateOverlapsAndProbabilities(Game game) {
        final var boardWidth = game.getBoard().length;
        final var boardHeight = game.getBoard()[0].length;

        var wipedBoard = game.calcWipedBoard();
        var containedAnimals = game.getContainedAnimals();
        var boardPermutations = PermutationService.calculateBoardPermutations(wipedBoard, containedAnimals);
        List<AnimalBoardInstance>[][] overallOverlap = new List[boardWidth][boardHeight];
        Map<Animal, List<AnimalBoardInstance>[][]> animalOverlaps = new HashMap<>();
        Map<Animal, int[][]> animalOverlapCounts = new HashMap<>();
        Map<Animal, Set<AnimalBoardInstance>> animalDistinctInstancesMap = new HashMap<>();
        for (var permutation : boardPermutations) {
            for (int x = 0; x < boardWidth; x++) {
                for (int y = 0; y < boardHeight; y++) {
                    if (overallOverlap[x][y] == null)
                        overallOverlap[x][y] = new ArrayList<>();
                    if (wipedBoard[x][y].isRevealed())
                        continue;
                    var animalBoardInstance = permutation[x][y].getAnimalBoardInstance();
                    overallOverlap[x][y].add(animalBoardInstance);
                    if (animalBoardInstance != null) {
                        var animal = animalBoardInstance.animal();

                        animalOverlaps.computeIfAbsent(animal, _ -> {
                            var animList = new List[boardWidth][boardHeight];
                            for (int i = 0; i < boardWidth; i++) {
                                for (int j = 0; j < boardHeight; j++) {
                                    animList[i][j] = new ArrayList<>();
                                }
                            }
                            return animList;
                        });
                        animalOverlapCounts.putIfAbsent(animal, new int[boardWidth][boardHeight]);

                        var animalOverlap = animalOverlaps.get(animal);
                        if (animalOverlap[x][y] == null)
                            animalOverlap[x][y] = new ArrayList<>();
                        animalOverlap[x][y].add(animalBoardInstance);

                        var animalOverlapCount = animalOverlapCounts.get(animal);
                        animalOverlapCount[x][y] += 1;

                        animalDistinctInstancesMap.putIfAbsent(animal, new HashSet<>());
                        animalDistinctInstancesMap.get(animal).add(animalBoardInstance);
                    }
                }
            }
        }
        Map<Animal, Double[][]> animalProbabilities = new HashMap<>();
        animalOverlaps.keySet().forEach(animal -> {
            var overlapCount = animalOverlapCounts.get(animal);
            var allDistinctAnimals = animalDistinctInstancesMap.get(animal);
            final var probabilities = new Double[boardWidth][boardHeight];
            for (int y = 0; y < boardWidth; y++) {
                for (int x = 0; x < boardHeight; x++) {
                    probabilities[x][y] = ((double) overlapCount[x][y]) / allDistinctAnimals.size();
                }
            }
            animalProbabilities.put(animal, probabilities);
        });
        var animalOverlapAndProbabilities = new HashMap<Animal, AnimalOverlapAndProbabilities>();

        animalOverlaps.keySet().forEach(animal -> animalOverlapAndProbabilities.put(animal, new AnimalOverlapAndProbabilities(animalOverlaps.get(animal),
                animalProbabilities.get(animal))));
        return new OverlapsAndProbabilities(overallOverlap, animalOverlapAndProbabilities);
    }
}
