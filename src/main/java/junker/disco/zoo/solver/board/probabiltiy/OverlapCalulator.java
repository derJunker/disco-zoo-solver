package junker.disco.zoo.solver.board.probabiltiy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;

public class OverlapCalulator {

    public static List<Coords> getBestReducingRemainingAnimalOverlapCoords(List<Coords> highestOverlapCoords,
                                                                            Overlaps overlaps, Animal animalToSolve) {
        // TODO
        System.out.println("TODO: Implement getBestReducingClicks");
        return highestOverlapCoords;
    }
    public static List<Coords> findHighestOverlapCoords(Overlaps overlaps, Animal animalToSolve) {
        var overallOverlap = overlaps.overallOverlap();
        var bestCandidates = new ArrayList<Coords>();
        var maxOverlap = 0;
        for (int x = 0; x < overallOverlap.length; x++) {
            for (int y = 0; y < overallOverlap[0].length; y++) {
                var animalTileOverlap =
                        overallOverlap[x][y].stream()
                                .filter(Objects::nonNull)
                                .filter(animalBoardInstance -> animalBoardInstance.animal().equals(animalToSolve)).count();
                if (animalTileOverlap > maxOverlap) {
                    bestCandidates.clear();
                    bestCandidates.add(new Coords(x, y));
                    maxOverlap = (int) animalTileOverlap;
                } else if (animalTileOverlap == maxOverlap) {
                    bestCandidates.add(new Coords(x, y));
                }
            }
        }
        return bestCandidates;
    }
}
