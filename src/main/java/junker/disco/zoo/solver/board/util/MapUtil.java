package junker.disco.zoo.solver.board.util;

import java.util.Map;

public class MapUtil {
    public static String convertWithIteration(Map<?, ?> map) {
        StringBuilder mapAsString = new StringBuilder("{");
        for (var key : map.keySet()) {
            mapAsString.append(key).append("=").append(map.get(key)).append(", ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }
}
