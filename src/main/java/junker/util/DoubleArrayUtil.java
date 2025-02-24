package junker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static <T> Map<Coords, T> filter(T[][] array, Predicate<T> predicate) {
        Map<Coords, T> result = new HashMap<>();
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                if (predicate.test(array[x][y])) {
                    result.put(new Coords(x, y), array[x][y]);
                }
            }
        }
        return result;
    }

    public static <T> List<T>[][] filterListsInDoubleArray(List<T>[][] array, Predicate<T> predicate) {
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                array[x][y]  = array[x][y].stream().filter(predicate).toList();
            }
        }
        return array;
    }

    public static <T> List<T>[][] cloneDoubleListArray(List<T>[][] doubleListArray) {
        List<T>[][] clone = new List[doubleListArray.length][];
        for (int i = 0; i < doubleListArray.length; i++) {
            clone[i] = new List[doubleListArray[i].length];
            for (int j = 0; j < doubleListArray[i].length; j++) {
                clone[i][j] = new ArrayList<>(doubleListArray[i][j]);
            }
        }
        return clone;
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
