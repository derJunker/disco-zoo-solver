package junker.disco.zoo.zoodle;

import java.util.List;

import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.model.animals.Animal;

// I have a web version in a different project for this. but this remains as an artifact here :D why not.
public class Zoodle {
    public static void main(String[] args) {
        AnimalFacts.initAnimalFacts();
        var random = new java.util.Random();
        var randomAnimal = Animal.ALL_ANIMALS.get(random.nextInt(Animal.ALL_ANIMALS.size()));
        var randomAnimalFacts = AnimalFacts.of(randomAnimal);

//        System.out.printf("Animal: '%s' clicksToSolve: %d region: %s%n",
//                randomAnimal.name(), numberOfClicksUntilSolve, region);
        var guessNr = 0;
        var maxGuesses = 5;

        while (guessNr < maxGuesses) {
            System.out.printf("Guess %d: ", guessNr);
            var guess = new java.util.Scanner(System.in).nextLine();
            var optGuessedAnim = Animal.findAnimalsByName(guess);
            if (optGuessedAnim.isEmpty()) {
                System.out.printf("'%s' is not a valid animal name. Try again!%n", guess);
                continue;
            }
            var guessedAnim = optGuessedAnim.getFirst();
            if (guessedAnim.name().equals(randomAnimal.name())) {
                System.out.println("You guessed it!");
                break;
            }
            var guessedAnimalFacts = AnimalFacts.of(guessedAnim);
            var comparison = randomAnimalFacts.compareTo(guessedAnimalFacts);
            System.out.println(guessedAnimalFacts);
            System.out.printf("Your guess: %s%n", comparison);
            System.out.println("------------------------");

            guessNr++;
        }
        System.out.printf("The animal was: %s%n", randomAnimal.name());
        System.out.printf("It took you %d guesses.%n", guessNr);
        System.out.println(randomAnimalFacts);
        System.out.println("Thanks for playing!");
    }
}
