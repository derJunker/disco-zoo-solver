package junker;

import java.util.List;

import junker.animals.AnimalService;
import junker.board.Game;
import junker.board.probabiltiy.BoardProbabilityCalculator;
import junker.board.probabiltiy.PermutationService;

import static junker.util.DoubleArrayUtil.arrayAsCoordinatesString;
import static junker.util.DoubleArrayUtil.mapDoubleArray;


public class Main {
    public static void main(String[] args) {
        var animal = AnimalService.getAnimalByName("Koala");
        var game = new Game(List.of(animal));
        var overlap = PermutationService.calculateOverlap(game);
        System.out.println(arrayAsCoordinatesString(mapDoubleArray(overlap, set -> set.size())));
    }
}