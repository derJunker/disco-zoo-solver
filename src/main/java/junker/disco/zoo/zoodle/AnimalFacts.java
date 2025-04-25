package junker.disco.zoo.zoodle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Rarity;
import junker.disco.zoo.solver.model.animals.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalFacts implements Serializable {

    private String animalName;
    private Region region;
    private int solutionLength;
    private Rarity rarity;
    private int legs;
    private HopSize hopSize;
    private double coinsPerMin;

    public final static List<AnimalFacts> ALL_ANIMAL_FACTS = new ArrayList<>();
    public static AnimalFacts of(Animal animal) {
        return ALL_ANIMAL_FACTS.parallelStream()
                .filter(facts -> facts.animalName.equalsIgnoreCase(animal.name()))
                .findFirst().orElseThrow();
    }

    public String compareTo(AnimalFacts other) {
        if (this == other) {
            return "Same animal";
        }
        StringBuilder sb = new StringBuilder();

//        val regionList = Arrays.stream(Region.values()).toList();
//        var thisRegionIndex = regionList.indexOf(this.region);
//        var otherRegionIndex = regionList.indexOf(other.region);
//        sb.append("Region: ")
//                .append(getCompareString(thisRegionIndex, otherRegionIndex));

        sb.append("clicksToSolve: ");
        sb.append(getCompareString(this.solutionLength, other.solutionLength));

        sb.append(", rarity: ");
        sb.append(getCompareString(this.rarity.getValue(), other.rarity.getValue()));

        sb.append(", legs: ");
        sb.append(getCompareString(this.legs, other.legs));

        sb.append(", hopSize: ");
        sb.append(getCompareString(this.hopSize.ordinal(), other.hopSize.ordinal()));

        sb.append(", coinsPerMin: ");
        sb.append(getCompareString((int) this.coinsPerMin, (int) other.coinsPerMin));

        return sb.toString();
    }

    public static void initAnimalFacts() {
        ALL_ANIMAL_FACTS.addAll(AnimalFactsLoader.loadAnimalFacts());
    }

    private String getCompareString(int thisValue, int otherValue) {
        if (thisValue < otherValue) {
            return "v";
        } else if (thisValue > otherValue) {
            return "^";
        } else {
            return "=";
        }
    }

    @Override
    public String toString() {
        return "AnimalFacts: " +
                "region=" + region +
                ", solutionLength=" + solutionLength +
                ", rarity=" + rarity
                + ", legs=" + legs +
                ", hopSize=" + hopSize +
                ", coinsPerMin=" + coinsPerMin;
    }
}
