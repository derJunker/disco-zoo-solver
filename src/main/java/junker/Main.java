package junker;

import junker.animals.Animal;
import junker.board.Game;

// TODO Optimizations:
//  - Fix: go until you have a 100% match for all parts of the animal. not just until you find 1. HOWEVER for this you
//    need to remember the starting board. example sasquatch: it would be okay to click the middle ONCE but after that
//    not. When not remembering the board, i press once and then it would also be okay to press the middle again *once*.
//    once you click off one of the solution paths you can reset the remembered board.
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
        var bestClicks = game.getBestSolutions(animals.getFirst());
        System.out.println(bestClicks.size() + " best Solutions:\n" + bestClicks);
    }
}