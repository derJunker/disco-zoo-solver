package junker.disco.zoo.solver.model.solver;

import java.util.List;
import java.util.Map;
import java.util.Set;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.util.DoubleArrayUtil;

public record Overlaps(
        List<AnimalBoardInstance>[][] overallOverlap,
        Set<Tile[][]> permutations,
        Map<Animal, Integer> animalOverlapCounts
) {
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Overlaps{");
        sb.append("overallOverlap=\n");
        sb.append(DoubleArrayUtil.arrayAsCoordinatesString(overallOverlap));
        sb.append(",\npermutations=\n");
        for (var perm : permutations) {
            sb.append(DoubleArrayUtil.arrayAsCoordinatesString(perm));
            sb.append("\n");
        }
        sb.append(",\nanimalOverlapCounts=");
        sb.append(animalOverlapCounts);
        sb.append("}");
        return sb.toString();
    }
}
