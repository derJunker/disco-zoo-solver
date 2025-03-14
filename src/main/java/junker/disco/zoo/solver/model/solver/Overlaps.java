package junker.disco.zoo.solver.model.solver;

import java.util.List;
import java.util.Map;
import java.util.Set;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.board.util.DoubleArrayUtil;

public record Overlaps(
        List<AnimalBoardInstance>[][] overallOverlap,
        Map<Animal, Set<AnimalBoardInstance>[][]> uniqueAnimalOverlapMap, Set<Tile[][]> permutations,
        Map<Animal, Integer> animalMaxOverlapCounts
) {
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Overlaps{");
        sb.append("uniqueAnimalOverlapMap=\n");
        for (var entry : uniqueAnimalOverlapMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=\n");
            sb.append(DoubleArrayUtil.arrayAsCoordinatesString(entry.getValue()));
            sb.append("\n");
        }
        sb.append("overallOverlap=\n");
        sb.append(DoubleArrayUtil.arrayAsCoordinatesString(overallOverlap));
        sb.append(",\npermutations=\n");
        for (var perm : permutations) {
            sb.append(DoubleArrayUtil.arrayAsCoordinatesString(perm));
            sb.append("\n");
        }
        sb.append(",\nanimalMaxOverlapCounts=");
        sb.append(animalMaxOverlapCounts);
        sb.append("}");
        return sb.toString();
    }

    public double[][] calculateProbability(Animal animal) {
        var probability = new double[overallOverlap.length][overallOverlap[0].length];
        for (int x = 0; x < overallOverlap.length; x++) {
            for (int y = 0; y < overallOverlap[0].length; y++) {
                var instancesAtTile =
                        (double) overallOverlap[x][y].stream().filter(instance -> instance != null && instance.animal().equals(animal)).count();
                probability[x][y] =
                         instancesAtTile / permutations().size();
            }
        }
        return probability;
    }
}
