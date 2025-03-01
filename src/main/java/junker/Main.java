package junker;

import junker.animals.Animal;
import junker.board.Game;

// TODO Optimizations:
//  - if a full solution is found propagate it, so you can cancel every solution longer than that
//  - ignore discobux
//  - Maybe limit the multiclick only if there are more than X highest spots
//  - Multiple threads
//  - reducing duplicate paths (either manually or with a certain algorithm to choose what to click? idk

public class Main {
    public static void main(String[] args) {
        Animal.initAnimals();
        var animals = Animal.findAnimalsByName("Sasquatch");
        var game = new Game(animals);
//        game.setTile(3, 1, true, animals.getFirst());
//        game.setTile(4, 0, true, null);
//        game.setTile(4, 1, true, null);
//        game.setTile(4, 2, true, null);
//        game.setTile(4, 3, true, null);
//        game.setTile(4, 4, true, null);
//        game.setTile(1, 0, true, null);
        var bestClicks = game.getBestClicks(animals.getFirst());
        System.out.println(bestClicks.size() + " best Solutions:\n" + bestClicks);
    }
}