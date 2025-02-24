package junker;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import junker.animals.AnimalService;
import junker.board.AnimalBoardInstance;
import junker.board.min_cover.BoardCoverCalculator;
import junker.board.Game;
import junker.util.DoubleArrayUtil;

// TODO Optimizations:
//  Independent Sets for highest overlaps and then immediately clicking all of them
//  Multiple threads
//  reducing duplicate paths (either manually or with a certain algorithm to choose what to click? idk
//  make getWhipedBoard only calculate it once per game instance
//  if still slow add db

public class Main {
    public static void main(String[] args) {
        var animals = AnimalService.getAnimalsByName(  "Koala");
        var game = new Game(animals);
//        game.setTile(2, 1, true, null);
//        game.setTile(2, 3, true, null);
//        game.setTile(3, 0, true, null);
//        game.setTile(3, 1, true, null);
//        game.setTile(3, 0, true, null);
//        game.setTile(1, 0, true, null);
        var minCover = BoardCoverCalculator.minCoveringSets(game, animals.get(0));
        var bestClicks = minCover.stream().map(cover -> cover.getFirst()).collect(Collectors.toSet());
        System.out.println("best clicks:\n" + bestClicks);

//        var overlap = BoardCoverCalculator.calculateOverlap(game, animals.get(0));
//        var overlapCount = DoubleArrayUtil.arrayAsCoordinatesString(DoubleArrayUtil.mapDoubleArrayListToSet(overlap,
//                animalBoardInstances -> new HashSet<>(List.of("" + animalBoardInstances.size()))));
//        var overlapSet = DoubleArrayUtil.arrayAsCoordinatesString(DoubleArrayUtil.mapDoubleArrayListToSet(overlap,
//                animalBoardInstances -> new HashSet<>(animalBoardInstances.stream().map(AnimalBoardInstance::toString).toList())));
//        System.out.println("Overlap count:\n" + overlapCount);
//        System.out.println("Overlap set:\n" + overlapSet);
    }
}