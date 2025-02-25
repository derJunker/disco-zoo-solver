package junker.board.min_cover;

import java.util.*;

import junker.board.Coords;

public class IndependentSetsCalculator {

    /**
     * Returns all maximal independent subsets of coordinates from the given 2D array.
     * Two cells (with their associated sets) are independent if their sets share no common element.
     * Each maximal independent subset is represented as a set of Coords.
     *
     * @param ids a 2D array of sets of strings
     * @return a set of maximal independent subsets, each represented as a set of Coords
     */
    public static Set<Set<Coords>> calculateMaxIndependentSubSets(Set<String>[][] ids) {
        int rows = ids.length;
        if (rows == 0) {
            return Collections.emptySet();
        }
        int cols = ids[0].length;
        int total = rows * cols;

        // Flatten the 2D array into a list of Coords and a corresponding list of sets.
        List<Coords> coordsList = new ArrayList<>(total);
        List<Set<String>> setList = new ArrayList<>(total);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                coordsList.add(new Coords(i, j));
                setList.add(ids[i][j]);
            }
        }

        // Build a conflict matrix: conflict[i][j] is true if setList.get(i) and setList.get(j) share any element.
        boolean[][] conflict = new boolean[total][total];
        for (int i = 0; i < total; i++) {
            for (int j = i + 1; j < total; j++) {
                if (!Collections.disjoint(setList.get(i), setList.get(j))) {
                    conflict[i][j] = true;
                    conflict[j][i] = true;
                }
            }
        }

        // Build the complement graph:
        // Two vertices (cells) are connected in the complement if their sets are disjoint.
        boolean[][] complement = new boolean[total][total];
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                if (i != j) {
                    complement[i][j] = !conflict[i][j];
                }
            }
        }

        // Use Bron–Kerbosch to find all maximal cliques in the complement graph,
        // which correspond to maximal independent sets of cells.
        Set<Set<Integer>> maximalCliquesIndices = new HashSet<>();
        Set<Integer> R = new HashSet<>();
        Set<Integer> P = new HashSet<>();
        Set<Integer> X = new HashSet<>();
        for (int i = 0; i < total; i++) {
            P.add(i);
        }
        bronKerbosch(R, P, X, complement, maximalCliquesIndices);

        // Convert the cliques (indices) back to sets of Coords.
        Set<Set<Coords>> maximalCliquesCoords = new HashSet<>();
        for (Set<Integer> clique : maximalCliquesIndices) {
            Set<Coords> coordsClique = new HashSet<>();
            for (Integer idx : clique) {
                coordsClique.add(coordsList.get(idx));
            }
            maximalCliquesCoords.add(coordsClique);
        }
        return maximalCliquesCoords;
    }

    /**
     * Recursive Bron–Kerbosch algorithm with pivoting.
     *
     * @param R current clique
     * @param P prospective vertices that can be added to R
     * @param X vertices already processed (should be excluded)
     * @param graph the complement graph represented as an adjacency matrix
     * @param cliques set to collect all maximal cliques found
     */
    private static void bronKerbosch(Set<Integer> R, Set<Integer> P, Set<Integer> X,
                                     boolean[][] graph, Set<Set<Integer>> cliques) {
        if (P.isEmpty() && X.isEmpty()) {
            // Found a maximal clique.
            cliques.add(new HashSet<>(R));
            return;
        }

        // Choose a pivot from P ∪ X.
        Set<Integer> unionPX = new HashSet<>(P);
        unionPX.addAll(X);
        Integer pivot = unionPX.iterator().next();

        // Only consider vertices in P that are not neighbors of the pivot.
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
}
