package junker.disco.zoo.solver.util;

import java.util.ArrayList;
import java.util.List;

import junker.disco.zoo.solver.board.probabiltiy.PermutationService;

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

    public static <T> int resetAddIfBelowLimit(List<T> listToModify, List<T> elements, int value, int limit) {
        return addAndClearIfThresholdTrue(listToModify, elements, value, limit, value < limit);
    }

    public static <T> int resetAddIfAboveLimit(List<T> listToModify, List<T> elements, int value,
                                                          int limit) {
        return addAndClearIfThresholdTrue(listToModify, elements, value, limit, value > limit);
    }

    private static <T> int addAndClearIfThresholdTrue(List<T> listToModify, List<T> elements, int value, int limit,
                                            boolean threshold) {
        if (threshold) {
            listToModify.clear();
            listToModify.addAll(elements);
            return value;
        } else if(value == limit) {
            listToModify.addAll(elements);
            return value;
        } else {
            return limit;
        }
    }

    public static <T>  List<List<T>> putFirstAndPermuteRest(List<T> list, T element) {
        var remainingList = new ArrayList<T>(list);
        remainingList.remove(element);
        var remainingListPerms = PermutationService.getPermutationOfList(remainingList);
        remainingListPerms.forEach(perm -> perm.addFirst(element));
        return remainingListPerms;
    }
}
