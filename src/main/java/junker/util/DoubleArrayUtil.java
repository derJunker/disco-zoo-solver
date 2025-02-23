package junker.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import junker.board.Coords;

public class DoubleArrayUtil {

    public static <T> Set<String>[][] mapDoubleArrayListToSet(List<T>[][] array,
                                                          Function<List<T>, Set<String>> mapper) {
        Set<String>[][] result =  new Set[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = (Set<String>[]) new Set[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                result[i][j] = mapper.apply(array[i][j]);
            }
        }
        return result;
    }

    public static <T, V> List<V> flatMapDoubleArrayToList(T[][] array, Function<T, V> mapper) {
        List<V> result = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result.add(mapper.apply(array[i][j]));
            }
        }
        return result;
    }

    public static String arrayAsCoordinatesString(double[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                sb.append(String.format("%.2f ", array[x][y]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String arrayAsCoordinatesString(int[][] array) {
        int maxLength = 0;
        for (int[] row : array) {
            for (int value : row) {
                maxLength = Math.max(maxLength, String.valueOf(value).length());
            }
        }
        String format = "%" + maxLength + "d ";
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                sb.append(String.format(format, array[x][y]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static String arrayAsCoordinatesString(Object[][] array) {
        int maxLength = 0;
        for (Object[] row : array) {
            for (Object value : row) {
                maxLength = Math.max(maxLength, value.toString().length());
            }
        }
        String format = "%-" + (maxLength + 1) + "s";
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                String value = array[x][y].toString();
                String gap = x == array[y].length - 1 ? "" : " ";
                sb.append(String.format(format + gap, value));
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
