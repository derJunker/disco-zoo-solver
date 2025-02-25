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
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[0].length; y++) {
                if (predicate.test(array[x][y])) {
                    result.put(new Coords(x, y), array[x][y]);
                }
            }
        }
        return result;
    }

    public static <T> Map<Coords, T> filterByIndex(T[][] array, Predicate<Coords> predicate) {
        Map<Coords, T> result = new HashMap<>();
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[0].length; y++) {
                var coords = new Coords(x, y);
                if (predicate.test(coords)) {
                    result.put(coords, array[x][y]);
                }
            }
        }
        return result;
    }

    public static <T> List<T>[][] filterListsInDoubleArray(List<T>[][] array, Predicate<T> predicate) {
        List<T>[][] result = new List[array.length][];
        for (int x = 0; x < array.length; x++) {
            result[x] = new List[array[x].length];
            for (int y = 0; y < array[x].length; y++) {
                if (array[x][y] == null) {
                    result[x][y] = null;
                    continue;
                }
                result[x][y] = array[x][y].stream().filter(predicate).toList();
            }
        }
        return result;
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

    public static String arrayAsCoordinatesString(Object[][] array) {
        int maxLength = 0;
        for (Object[] row : array) {
            for (Object value : row) {
                maxLength = Math.max(maxLength, value.toString().length());
            }
        }
        String format = "%-" + (maxLength + 1) + "s";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array[0].length; i++) {
            for (int j = 0; j < array.length; j++) {
                String value = array[j][i].toString();
                String gap = j == array[i].length - 1 ? "" : " ";
                sb.append(String.format(format + gap, value));
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
