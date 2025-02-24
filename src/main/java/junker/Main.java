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
//  ignore discobux

public class Main {
    public static void main(String[] args) {
        var animals = AnimalService.getAnimalsByName(  "Sasquatch", "Koala");
        var game = new Game(animals);
//        game.setTile(1, 1, true, null);
//        game.setTile(1, 3, true, null);
//        game.setTile(3, 1, true, null);
//        game.setTile(3, 3, true, null);
//        game.setTile(3, 0, true, null);
//        game.setTile(1, 0, true, null);
        var bestClicks = game.getBestClicks(animals.get(1));
        System.out.println(bestClicks.size() + " best clicks:\n" + bestClicks);
    }
}