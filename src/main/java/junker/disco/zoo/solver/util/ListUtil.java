package junker.disco.zoo.solver.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    public static <T> List<T> putFirst(List<T> list, T element) {
        var newList = new ArrayList<T>(list);
        newList.remove(element);
        newList.addFirst(element);
        return newList;
    }

    public static <T> List<T> putLast(List<T> list, T element) {
        var newList = new ArrayList<T>(list);
        newList.remove(element);
        newList.addLast(element);
        return newList;
    }
}
