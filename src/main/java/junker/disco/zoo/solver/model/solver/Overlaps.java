package junker.disco.zoo.solver.model.solver;

import java.util.List;
import java.util.Map;
import java.util.Set;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.board.util.DoubleArrayUtil;
import junker.disco.zoo.solver.model.animals.Animal;

public record Overlaps(
        List<AnimalBoardInstance>[][] overallOverlap,
        Map<Animal, Set<AnimalBoardInstance>[][]> uniqueAnimalOverlapMap,
        Map<Animal, Double[][]> animalOverlapProbability,
        Set<Tile[][]> permutations,
        Map<Animal, Integer> animalMaxOverlapCounts,
        Map<Animal, Double> animalMinProbability,
        Map<Animal, Integer> animalRevealedTileCounts,
        boolean verticalSymmetry,
        boolean horizontalSymmetry
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
        sb.append(",\nanimalOverlapProbability=\n");
        for (var entry : animalOverlapProbability.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=\n");
            sb.append(DoubleArrayUtil.arrayAsCoordinatesString(entry.getValue()));
            sb.append("\n");
        }
        sb.append("overallOverlap=\n");
        sb.append(DoubleArrayUtil.aggregatedListArrayAsCoordinatesString(overallOverlap));
        sb.append(",\nanimalMaxOverlapCounts=");
        sb.append(animalMaxOverlapCounts);
//        sb.append(",\npermutations=\n");
//        for (var perm : permutations) {
//            sb.append(DoubleArrayUtil.arrayAsCoordinatesString(perm));
//            sb.append("\n");
//        }
        sb.append("}");
        return sb.toString();
    }
}
