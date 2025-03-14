package junker.disco.zoo.solver.board.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static <T> Set<List<T>> putFirstAndPermuteRest(List<T> list, T element) {
        var remainingList = new ArrayList<T>(list);
        remainingList.remove(element);
        var remainingListPerms = PermutationUtil.getPermutationOfCollection(remainingList);
        remainingListPerms.forEach(perm -> perm.addFirst(element));
        return remainingListPerms;
    }

    public static <T> Set<List<T>> permuteFirst(Collection<T> collection) {
        var list = new ArrayList<>(collection);
        var permutedFirstLists = new HashSet<List<T>>();
        for (var elToPutFirst : list) {
            permutedFirstLists.add(putFirst(list, elToPutFirst));
        }
        return permutedFirstLists;
    }

    public static <T> List<T> cloneThenAddAll(List<T> previousClicks, Collection<T> collectionToAdd) {
        var newClicks = new ArrayList<>(previousClicks);
        newClicks.addAll(collectionToAdd);
        return newClicks;
    }
}
