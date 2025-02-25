package junker;

import junker.animals.AnimalService;
import junker.board.Game;

// TODO Optimizations:
//  Maybe limit the multiclick only if there are more than X highest spots
//  Multiple threads
//  reducing duplicate paths (either manually or with a certain algorithm to choose what to click? idk
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
        var bestClicks = game.getBestClicks(animals.get(0));
        System.out.println(bestClicks.size() + " best Solutions:\n" + bestClicks);
    }
}