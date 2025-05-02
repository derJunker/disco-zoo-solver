package junker.disco.zoo.zoodle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junker.disco.zoo.solver.board.solve.DiscoZooSolver;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Rarity;
import junker.disco.zoo.solver.model.animals.Region;

public class AnimalFactsLoader {

    private static final String ANIMAL_FACTS_PATH = "data/";
    private static final String ANIMAL_FACTS_FILE = "animal_facts.csv";

    public static List<AnimalFacts> loadAnimalFacts() {
        var dir = new java.io.File(ANIMAL_FACTS_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<AnimalFacts> animalFactsList = new ArrayList<>();
        var file = new java.io.File(dir, ANIMAL_FACTS_FILE);
        if (file.exists()) {
            try (var reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;

                // Skip the header line
                reader.readLine();

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        AnimalFacts af = new AnimalFacts();
                        af.setAnimalName(parts[0]);
                        af.setRegion(Region.valueOf(parts[1]));
                        af.setSolutionLength(Integer.parseInt(parts[2]));
                        af.setRarity(Rarity.valueOf(parts[3]));
                        af.setLegs(Integer.parseInt(parts[4]));
                        af.setHopSize(HopSize.valueOf(parts[5]));
                        af.setCoinsPerMin(Double.parseDouble(parts[6]));

                        animalFactsList.add(af);
                    }
                }
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return animalFactsList;
    }


    private static void saveAnimalFacts(List<AnimalFacts> animalFactsList) {
        var dir = new java.io.File(ANIMAL_FACTS_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        var file = new java.io.File(dir, ANIMAL_FACTS_FILE);

        try (var writer = new java.io.FileWriter(file)) {
            // Write header
            writer.write("AnimalName,Region,SolutionLength,Rarity,Legs,HopSize,CoinsPerMin\n");

            // Write data rows
            for (AnimalFacts af : animalFactsList) {
                writer.write(String.join(",",
                        af.getAnimalName(),
                        af.getRegion().name(),
                        String.valueOf(af.getSolutionLength()),
                        af.getRarity().name(),
                        String.valueOf(af.getLegs()),
                        af.getHopSize().name(),
                        String.valueOf(af.getCoinsPerMin())
                ));
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Animal facts saved to " + file.getAbsolutePath());
    }

    public static void main(String[] args) {
        Animal.initAnimals();
        var animalFactsList = loadAnimalFacts();
        if (animalFactsList.size() < Animal.ALL_ANIMALS.size() - 1) {
            var missingAnimals = Animal.ALL_ANIMALS.stream()
                    .filter(animal -> animalFactsList.stream()
                            .noneMatch(facts -> facts.getAnimalName().equalsIgnoreCase(animal.name())))
                    .filter(animal -> animal.rarity() != Rarity.BUX)
                    .toList();
            Rarity lastRarity = null;
            Region lastRegion = null;
            String lastCoinsPerMin = "2";
            for (var animal : missingAnimals) {
                var animalName = animal.name();
                System.out.println("Missing animal: " + animalName);
                var region = animal.region();
                var solutionLength = DiscoZooSolver.getSolutionSize(animal);
                var rarity = animal.rarity();
                System.out.print("Legs: ");
                var legs = new java.util.Scanner(System.in).nextLine().trim();
                if (legs.isEmpty()) {
                    legs = "4";
                }
                System.out.print("Hop size " + String.join(", ",
                        Arrays.stream(HopSize.values()).map(value -> value.name() + " (" + value.ordinal() + ")").toList()) + " as int: ");
                var hopSize = new java.util.Scanner(System.in).nextLine().trim();
                if(hopSize.isEmpty()) {
                    hopSize = HopSize.SMALL.ordinal() + "";
                }
                var coinsPerMin = lastCoinsPerMin;
                if (!animal.rarity().equals(lastRarity) || !region.equals(lastRegion)) {
                    System.out.print("Coins per min: ");
                    coinsPerMin = new java.util.Scanner(System.in).nextLine().trim();
                } else {
                    System.out.println("Skipped coins per min input");
                }
                animalFactsList.add(new AnimalFacts(animalName, region, solutionLength, rarity,
                        Integer.parseInt(legs), HopSize.values()[Integer.parseInt(hopSize)],
                        Double.parseDouble(coinsPerMin)));
                saveAnimalFacts(animalFactsList);

                lastRarity = rarity;
                lastCoinsPerMin = coinsPerMin;
                lastRegion = region;
            }
            System.out.println("Done!");
        }
    }
}
