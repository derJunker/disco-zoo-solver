package junker;

import java.util.HashSet;
import java.util.List;

import junker.animals.AnimalService;
import junker.board.AnimalBoardInstance;
import junker.board.min_cover.BoardCoverCalculator;
import junker.board.Game;
import junker.util.DoubleArrayUtil;

public class Main {
    public static void main(String[] args) {
        var animals = AnimalService.getAnimalsByName( "Koala", "Sasquatch");
        var game = new Game(animals);

        var overlap = BoardCoverCalculator.calculateOverlap(game, animals.get(0));
        var overlapCount = DoubleArrayUtil.arrayAsCoordinatesString(DoubleArrayUtil.mapDoubleArrayListToSet(overlap,
                animalBoardInstances -> new HashSet<>(List.of("" + animalBoardInstances.size()))));
        var overlapSet = DoubleArrayUtil.arrayAsCoordinatesString(DoubleArrayUtil.mapDoubleArrayListToSet(overlap,
                animalBoardInstances -> new HashSet<>(animalBoardInstances.stream().map(AnimalBoardInstance::toString).toList())));
        System.out.println("Overlap count:\n" + overlapCount);
        System.out.println("Overlap set:\n" + overlapSet);
    }
}