package junker.disco.zoo.solver.board.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junker.disco.zoo.solver.board.Coords;

public class AnimalSymmetryFinder {
    public static Map<String, Boolean> isSymmetric(List<Coords> pattern) {
        Set<Coords> points = new HashSet<>(pattern);

        int minX = points.stream().mapToInt(Coords::x).min().orElse(0);
        int maxX = points.stream().mapToInt(Coords::x).max().orElse(0);
        int minY = points.stream().mapToInt(Coords::y).min().orElse(0);
        int maxY = points.stream().mapToInt(Coords::y).max().orElse(0);

        boolean verticalSymmetry = false;
        boolean horizontalSymmetry = false;

        // Check vertical symmetry across all possible axes (including half-integers)
        for (int axisRaw = 2 * minX; axisRaw <= 2 * maxX; axisRaw++) {
            double axis = axisRaw / 2.0;
            boolean allMatch = true;

            for (Coords p : points) {
                double mirroredX = 2 * axis - p.x();
                int mirroredXi = (int) mirroredX;

                // Must be an integer point to exist in pattern
                if (mirroredX != mirroredXi || !points.contains(new Coords(mirroredXi, p.y()))) {
                    allMatch = false;
                    break;
                }
            }

            if (allMatch) {
                verticalSymmetry = true;
                break;
            }
        }

        // Check horizontal symmetry
        for (int axisRaw = 2 * minY; axisRaw <= 2 * maxY; axisRaw++) {
            double axis = axisRaw / 2.0;
            boolean allMatch = true;

            for (Coords p : points) {
                double mirroredY = 2 * axis - p.y();
                int mirroredYi = (int) mirroredY;

                if (mirroredY != mirroredYi || !points.contains(new Coords(p.x(), mirroredYi))) {
                    allMatch = false;
                    break;
                }
            }

            if (allMatch) {
                horizontalSymmetry = true;
                break;
            }
        }

        Map<String, Boolean> result = new HashMap<>();
        result.put("vertical", verticalSymmetry);
        result.put("horizontal", horizontalSymmetry);
        return result;
    }
}
