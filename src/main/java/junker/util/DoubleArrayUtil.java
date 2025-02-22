package junker.util;

import java.util.Arrays;
import java.util.function.Function;

public class DoubleArrayUtil {

    public static <T, V> V[][] mapDoubleArray(T[][] array, Function<T, V> mapper) {
        V[][] result = (V[][]) new Object[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = (V[]) new Object[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                result[i][j] = mapper.apply(array[i][j]);
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
