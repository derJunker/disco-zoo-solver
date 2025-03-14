package junker.disco.zoo.solver.board.probabiltiy;

import java.util.*;
import java.util.function.Function;

import junker.disco.zoo.solver.board.Coords;

public class IndependentSetsCalculator {

    /**
     * Returns all maximal independent subsets of Coords from the given map.
     * Each coordinate is associated with a list of items of type T. For each coordinate,
     * we compute the set of string ids (via idMapper) from its items. Two coordinates conflict
     * if their id sets share at least one common id. A maximal independent subset is a set of
     * coordinates that are pairwise non-conflicting (i.e. their id sets are pairwise disjoint)
     * and maximal with respect to this property.
     *
     * @param <T>      the type of items stored at each coordinate
     * @param items    a map from Coords to a List of T
     * @param idMapper a function that maps an item T to its string id
     * @return a set of maximal independent subsets, each represented as a set of Coords
     */
    public static <T, V extends Collection<T>> Set<Set<Coords>> calculateMaxIndependentSubSets(
            Map<Coords, V> items,
            Function<T, String> idMapper) {

        // Extract the list of coordinates.
        List<Coords> coordsList = new ArrayList<>(items.keySet());
        int total = coordsList.size();

        // For each coordinate, compute the set of ids present in its associated list.
        List<Set<String>> idSets = new ArrayList<>(total);
        for (Coords coord : coordsList) {
            List<T> itemList = new ArrayList<>(items.get(coord));
            Set<String> ids = new HashSet<>();
            for (T item : itemList) {
                ids.add(idMapper.apply(item));
            }
            idSets.add(ids);
        }

        // Build a conflict matrix:
        // conflict[i][j] is true if the id set at coordsList[i] and coordsList[j] share any common id.
        boolean[][] conflict = new boolean[total][total];
        for (int i = 0; i < total; i++) {
            for (int j = i + 1; j < total; j++) {
                if (!Collections.disjoint(idSets.get(i), idSets.get(j))) {
                    conflict[i][j] = true;
                    conflict[j][i] = true;
                }
            }
        }

        // Build the complement graph:
        // In the complement graph, two coordinates are adjacent if their id sets are disjoint.
        boolean[][] complement = new boolean[total][total];
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                if (i != j) {
                    complement[i][j] = !conflict[i][j];
                }
            }
        }

        // Use Bron–Kerbosch algorithm with pivoting to find all maximal cliques in the complement graph.
        // Each maximal clique corresponds to a maximal independent subset of coordinates.
        Set<Set<Integer>> maximalCliquesIndices = new HashSet<>();
        Set<Integer> R = new HashSet<>();
        Set<Integer> P = new HashSet<>();
        Set<Integer> X = new HashSet<>();
        for (int i = 0; i < total; i++) {
            P.add(i);
        }
        bronKerbosch(R, P, X, complement, maximalCliquesIndices);

        // Map cliques (indices) back to coordinates.
        Set<Set<Coords>> result = new HashSet<>();
        for (Set<Integer> cliqueIndices : maximalCliquesIndices) {
            Set<Coords> clique = new HashSet<>();
            for (Integer idx : cliqueIndices) {
                clique.add(coordsList.get(idx));
            }
            result.add(clique);
        }

        return result;
    }

    /**
     * Recursive Bron–Kerbosch algorithm with pivoting to enumerate all maximal cliques.
     *
     * @param R       current clique (set of vertex indices)
     * @param P       prospective vertices (indices) that can be added to R
     * @param X       vertices already processed (indices) that should be excluded
     * @param graph   the complement graph represented as an adjacency matrix
     * @param cliques set to accumulate all maximal cliques (each clique is a set of indices)
     */
    private static void bronKerbosch(Set<Integer> R, Set<Integer> P, Set<Integer> X,
                                     boolean[][] graph, Set<Set<Integer>> cliques) {
        if (P.isEmpty() && X.isEmpty()) {
            // R is a maximal clique.
            cliques.add(new HashSet<>(R));
            return;
        }

        // Choose a pivot from P ∪ X.
        Set<Integer> unionPX = new HashSet<>(P);
        unionPX.addAll(X);
        Integer pivot = unionPX.iterator().next();

        // Consider only vertices in P that are not neighbors of the pivot.
        Set<Integer> candidates = new HashSet<>(P);
        for (int v = 0; v < graph.length; v++) {
            if (graph[pivot][v]) {
                candidates.remove(v);
            }
        }

        for (Integer v : new HashSet<>(candidates)) {
            Set<Integer> newR = new HashSet<>(R);
            newR.add(v);

            // newP = P ∩ N(v)
            Set<Integer> newP = new HashSet<>();
            for (Integer w : P) {
                if (graph[v][w]) {
                    newP.add(w);
                }
            }

            // newX = X ∩ N(v)
            Set<Integer> newX = new HashSet<>();
            for (Integer w : X) {
                if (graph[v][w]) {
                    newX.add(w);
                }
            }

            bronKerbosch(newR, newP, newX, graph, cliques);
            P.remove(v);
            X.add(v);
        }
    }

    // Example usage.
    public static void main(String[] args) {
        // For demonstration, suppose we have a simple class:
        class Item {
            private final String id;
            private final String description;

            public Item(String id, String description) {
                this.id = id;
                this.description = description;
            }

            public String getId() {
                return id;
            }

            @Override
            public String toString() {
                return "Item(" + id + ", " + description + ")";
            }
        }

        // Construct a Map<Coords, List<Item>>.
        // For example, at coordinate (0,0) we have two items,
        // at (0,1) one item, at (1,0) two items, and at (1,1) one item.
        Map<Coords, List<Item>> items = new HashMap<>();
        items.put(new Coords(0, 0), Arrays.asList(new Item("a", "A1"), new Item("b", "B1")));
        items.put(new Coords(0, 1), Arrays.asList(new Item("b", "B2")));
        items.put(new Coords(1, 0), Arrays.asList(new Item("c", "C1"), new Item("d", "D1")));
        items.put(new Coords(1, 1), Arrays.asList(new Item("a", "A2")));

        // In this example:
        // - (0,0) has ids {"a", "b"}
        // - (0,1) has id {"b"}
        // - (1,0) has ids {"c", "d"}
        // - (1,1) has id {"a"}
        // Two coordinates conflict if their id sets overlap.
        // So (0,0) conflicts with (0,1) (they share "b") and with (1,1) (they share "a").
        // The Bron–Kerbosch algorithm will find all maximal sets of coordinates that are pairwise disjoint by id.

        Set<Set<Coords>> result = calculateMaxIndependentSubSets(items, Item::getId);
        System.out.println("Maximal independent subsets (by Coords):");
        for (Set<Coords> subset : result) {
            System.out.println(subset);
        }
    }
}
