package junker.disco.zoo.solver.board.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;

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

    public static Map<String, Boolean> isSymmetric(Game game) {
        var arr = game.getBoard();
        int width = arr.length;     if (width == 0) return Map.of("vertical", true, "horizontal", true);
        int height = arr[0].length; if (height == 0) return Map.of("vertical", true, "horizontal", true);

        double centerX = (width-1) / 2.0;
        double centerY = (height-1) / 2.0;

        boolean verticalSymmetry = true;
        boolean horizontalSymmetry = true;

        // Vertical symmetry: mirror around vertical center line (x = centerX)
        outerV:
        for (int x = 0; x < width; x++) {
            int mirrorX = (int) (2 * centerX - x);
            for (int y = 0; y < height; y++) {
                var tile = arr[x][y];
                if (tile.isRevealed() && tile.isOccupied())
                    return Map.of("vertical", false, "horizontal", false);
                if (tile.isRevealed() != arr[mirrorX][y].isRevealed()) {
                    verticalSymmetry = false;
                    break outerV;
                }
            }
        }

        // Horizontal symmetry: mirror around horizontal center line (y = centerY)
        outerH:
        for (int y = 0; y < height; y++) {
            int mirrorY = (int) (2 * centerY - y);
            if (mirrorY < 0 || mirrorY >= height) continue;
            for (int x = 0; x < width; x++) {
                var tile = arr[x][y];
                if (tile.isRevealed() && tile.isOccupied())
                    return Map.of("vertical", false, "horizontal", false);
                if (tile.isRevealed() != arr[x][mirrorY].isRevealed()) {
                    horizontalSymmetry = false;
                    break outerH;
                }
            }
        }

        Map<String, Boolean> result = new HashMap<>();
        result.put("vertical", verticalSymmetry);
        result.put("horizontal", horizontalSymmetry);
        return result;
    }

    public static Coords getSymmetryCoords(Coords coords, int gameWidth, int gameHeight, boolean verticalSymmetry,
                                           boolean horizontalSymmetry) {
        double centerX = (gameWidth - 1) / 2.0;
        double centerY = (gameHeight - 1) / 2.0;

        int x = coords.x();
        int y = coords.y();

        if (verticalSymmetry && x > centerX) {
            x = (int) (2 * centerX - x);
        }

        if (horizontalSymmetry && y > centerY) {
            y = (int) (2 * centerY - y);
        }

        return new Coords(x, y);
    }
}
