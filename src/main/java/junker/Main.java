package junker;

import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Game;

// TODO Optimizations:
//  - ignore discobux
//  - Maybe limit the multiclick only if there are more than X highest spots
//  - Multiple threads
//  - reducing duplicate paths (either manually or with a certain algorithm to choose what to click? idk

public class Main {
    public static void main(String[] args) {
        Animal.initAnimals();
        var animals = Animal.findAnimalsByName("Sheep", "Rabbit");
        var game = new Game(animals);
        var bestSolutions = DiscoZooSolver.getBestSolutions(animals.getFirst(), game);
        var bestClicks = bestSolutions.stream().map(solution -> solution.clicks().getFirst()).collect(Collectors.toSet());
        System.out.println(bestClicks.size() + " best Solutions:\n" + bestClicks);
    }
}