package junker;

import java.util.List;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.board.util.PermutationUtil;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.model.animals.Region;

// TODO Optimizations:
//  - ignore discobux
//  - Maybe limit the multiclick only if there are more than X highest spots
//  - Multiple threads
//  - reducing duplicate paths (either manually or with a certain algorithm to choose what to click? idk

public class Main {
    public static void main(String[] args) {
//        var animals = Animal.findAnimalsByName("Sheep", "Rabbit");
//        var game = new Game(animals, Region.ANY);
//        var bestSolutions = DiscoZooSolver.getBestSolutions(animals.getFirst(), game);
//        var bestClicks = bestSolutions.stream().map(solution -> solution.clicks().getFirst()).collect(Collectors.toSet());
//        System.out.println(bestClicks.size() + " best Solutions:\n" + bestClicks);
        var solutionCounter = 0;
        var failCounter = 0;
        var assortments = 0;
        var maxAssortments = 0;
        List<Animal> maxAssortmentAnimals = List.of();
        for (var region : Region.exclusiveValues()) {
            var animals = Animal.getAnimalListByRegion(region, true);
            for (var animal1 : animals) {
                for (var animal2 : animals) {
                    for (var animal3 : animals) {
                        if (animal1 == animal2 || animal1 == animal3 || animal2 == animal3)
                            continue;
                        var gameAnimals = List.of(animal3, animal1, animal2);
                        try {
                            var game = new Game(gameAnimals, region);
                            var permutations = PermutationUtil.calculateBoardPermutations(game.calcWipedBoard(), gameAnimals);
                            assortments += permutations.size();
                            if (permutations.size() > maxAssortments) {
                                maxAssortments = permutations.size();
                                maxAssortmentAnimals = gameAnimals;
                            }
                        } catch (IllegalArgumentException e) {
                            failCounter++;
                            System.out.println("Failed to create game with animals: " + gameAnimals.stream().map(Animal::name).collect(Collectors.joining(", ")));
                        }
                        solutionCounter++;
                    }

                }
            }
        }
        System.out.println("Max Assortments: " + maxAssortments + " with animals: " + maxAssortmentAnimals.stream().map(Animal::name).collect(Collectors.joining(", ")));
        System.out.println("Average Assortments: " + assortments / solutionCounter);
        System.out.println("Solutions: " + solutionCounter + ", Fails: " + failCounter);
    }
}