package junker.disco.zoo.solver.board.probabiltiy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.BoardService;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;

public class OverlapCalulator {

    public static List<Coords> getBestReducingRemainingAnimalOverlapCoords(List<Coords> highestOverlapCoords,
                                                                            Overlaps overlaps, Animal animalToSolve) {
        // TODO
        System.out.println("TODO: Implement getBestReducingClicks");
        return highestOverlapCoords;
    }

    // TODO precomputable in Overlaps class
    public static List<Coords> findHighestOverlapCoords(Overlaps overlaps, Animal animalToSolve) {
        var overallOverlap = overlaps.overallOverlap();
        var bestCandidates = new ArrayList<Coords>();
        var maxOverlap = 1;
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

    public static Overlaps emulateOverlapClick(Overlaps current, Animal animalToPlace,
                                               Set<AnimalBoardInstance> placeableAnimalInstances, Coords coords) {
        var placeableAnimalInstancesWithoutCurrent = placeableAnimalInstances.stream()
                .filter(animalBoardInstance -> (animalBoardInstance == null && animalToPlace != null) || (animalBoardInstance != null && !animalBoardInstance.animal().equals(animalToPlace)))
                .collect(Collectors.toSet());

        var newPermutations = getNewPermutations(current, placeableAnimalInstancesWithoutCurrent, coords, animalToPlace);
        return calculateOverlaps(newPermutations, current.overallOverlap().length, current.overallOverlap()[0].length);
    }

    public static Overlaps calculateOverlaps(Game game) {
        var board = game.getBoard();
        var containedAnimals = game.getContainedAnimals();
        var permutations = PermutationService.calculateBoardPermutations(board, containedAnimals);
        return calculateOverlaps(permutations, board.length, board[0].length);
    }

    private static Overlaps calculateOverlaps(Set<Tile[][]> permutations, int boardWidth, int boardHeight) {
        List<AnimalBoardInstance>[][] overallOverlap = new List[boardWidth][boardHeight];
        setOverallOverlap(overallOverlap, permutations, boardWidth, boardHeight);

        Map<Animal, Integer> animalMaxOverlaps = new java.util.HashMap<>();
        setMaxAnimalOverlaps(animalMaxOverlaps, overallOverlap, boardWidth, boardHeight);

        return new Overlaps(overallOverlap, permutations, animalMaxOverlaps);
    }

    private static void setOverallOverlap(List<AnimalBoardInstance>[][] overallOverlap,
                                          Set<Tile[][]> permutations, int boardWidth,int boardHeight) {
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                var tileOverlaps = new ArrayList<AnimalBoardInstance>();
                for (var permutation : permutations) {
                    var animalBoardInstance = permutation[x][y].getAnimalBoardInstance();
                    tileOverlaps.add(animalBoardInstance);
                }
                overallOverlap[x][y] = tileOverlaps;
            }
        }
    }

    private static void setMaxAnimalOverlaps(Map<Animal, Integer> animalMaxOverlaps,
                                             List<AnimalBoardInstance>[][] overallOverlap, int boardWidth, int boardHeight) {
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                var tileOverlaps = overallOverlap[x][y];
                var distinctAnimals = tileOverlaps.stream()
                        .filter(Objects::nonNull)
                        .map(AnimalBoardInstance::animal)
                        .collect(Collectors.toSet());
                for (var animal : distinctAnimals) {
                    var animalOverlapCount = (int) tileOverlaps.stream()
                            .filter(animalBoardInstance -> animalBoardInstance != null && animalBoardInstance.animal().equals(animal))
                            .distinct()
                            .count();
                    animalMaxOverlaps.put(animal, Math.max(animalMaxOverlaps.getOrDefault(animal, 0), animalOverlapCount));
                }
            }
        }
    }


    private static Set<Tile[][]> getNewPermutations(Overlaps current,
                                                        Set<AnimalBoardInstance> placeableAnimalsWithoutCurrent,
                                                        Coords coords, Animal animalToPlace) {
        var newPermutations = new HashSet<>(current.permutations());
        var potentialAnimalInstances =
                current.overallOverlap()[coords.x()][coords.y()]
                        .stream()
                        .filter(instance -> instance != null && instance.animal().equals(animalToPlace))
                        .collect(Collectors.toSet());
        newPermutations.removeIf(board -> tileHasOtherAnimalThanToPlace(board, placeableAnimalsWithoutCurrent) ||
                boardHasSameAnimalOfDifferentOrigin(board, animalToPlace, potentialAnimalInstances));
        return newPermutations.stream().map(
                permBoard -> {
                    var clonedBoard = BoardService.cloneBoard(permBoard);
                    var clickedTile = clonedBoard[coords.x()][coords.y()];
                    clickedTile.setRevealed(true);
                    clickedTile.setAnimalBoardInstance(null);
                    return clonedBoard;
                }).collect(Collectors.toSet());
    }

    private static boolean tileHasOtherAnimalThanToPlace(Tile[][] board,
                                                         Set<AnimalBoardInstance> placeableAnimalsWithoutCurrent) {
        for (var placeableAnimal : placeableAnimalsWithoutCurrent) {
            if (placeableAnimal == null)
                continue;
            final var origin = placeableAnimal.origin();
            var anyTileContainsImpossibleOriginInstance = placeableAnimal.animal().pattern().stream().anyMatch( patternCoord -> {
                var tileCoord = new Coords(origin.x() + patternCoord.x(), origin.y() + patternCoord.y());
                var tile = board[tileCoord.x()][tileCoord.y()];
                return tile.hasAnimalInstanceOfType(placeableAnimal.animal())
                        && tile.getAnimalBoardInstance().origin().equals(placeableAnimal.origin());
            });
            if (anyTileContainsImpossibleOriginInstance) {
                return true;
            }
        }
        return false;
    }

    private static boolean boardHasSameAnimalOfDifferentOrigin(Tile[][] board, Animal animalToPlace,
                                                               Set<AnimalBoardInstance> potentialAnimalInstances) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                var tile = board[x][y];
                if (tile.hasAnimalInstanceOfType(animalToPlace)
                        && potentialAnimalInstances.stream()
                        .noneMatch(instance -> tile.getAnimalBoardInstance().origin().equals(instance.origin()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
