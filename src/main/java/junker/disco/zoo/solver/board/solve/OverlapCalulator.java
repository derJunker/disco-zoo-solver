package junker.disco.zoo.solver.board.solve;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.AnimalBoardInstance;
import junker.disco.zoo.solver.board.util.BoardUtil;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.util.PermutationUtil;
import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.solver.Overlaps;

public class OverlapCalulator {
    public static List<Coords> findHighestOverlapCoords(Overlaps overlaps, Animal animalToSolve,
                                                        boolean includeSolved) {
        var overallOverlap = overlaps.overallOverlap();
        var animalMinProbability = overlaps.animalMinProbability().get(animalToSolve);
        if (animalMinProbability == null)
            return List.of();
        boolean isCompletelySolved = animalMinProbability> 0.9999;
        var bestCandidates = new ArrayList<Coords>();
        int permutationSize = overlaps.permutations().size();
        var maxOverlap = 1;
        for (int x = 0; x < overallOverlap.length; x++) {
            for (int y = 0; y < overallOverlap[0].length; y++) {
                var animalTileOverlap = overlaps.uniqueAnimalOverlapMap().get(animalToSolve)[x][y].size();
                if (animalTileOverlap > 0 && isCompletelySolved) {
                    bestCandidates.add(new Coords(x, y));
                    continue;
                }
                if (animalTileOverlap > maxOverlap && (animalTileOverlap < permutationSize || includeSolved)) {
                    bestCandidates.clear();
                    bestCandidates.add(new Coords(x, y));
                    maxOverlap = (int) animalTileOverlap;
                } else if (animalTileOverlap == maxOverlap && (animalTileOverlap < permutationSize || includeSolved)) {
                    bestCandidates.add(new Coords(x, y));
                }
            }
        }
        return bestCandidates;
    }

    public static Overlaps emulateOverlapClick(Overlaps current, Animal animalToPlace,
                                               Set<AnimalBoardInstance> placeableAnimalInstances, Coords coords,
                                               Game game) {
        var placeableAnimalInstancesWithoutCurrent = placeableAnimalInstances.stream()
                .filter(animalBoardInstance -> (animalBoardInstance == null && animalToPlace != null) || (animalBoardInstance != null && !animalBoardInstance.animal().equals(animalToPlace)))
                .collect(Collectors.toSet());

        var newPermutations = getNewPermutations(current, placeableAnimalInstancesWithoutCurrent, coords, animalToPlace);
        return calculateOverlaps(newPermutations, game);
    }

    public static Overlaps calculateOverlaps(Game game) {
        var board = game.getBoard();
        var containedAnimals = game.getContainedAnimals();
        var permutations = PermutationUtil.calculateBoardPermutations(board, containedAnimals);
        return calculateOverlaps(permutations, game);
    }

    private static Overlaps calculateOverlaps(Set<Tile[][]> permutations, Game game) {
        var board = game.getBoard();
        final var boardWidth = board.length;
        final var boardHeight = board[0].length;

        List<AnimalBoardInstance>[][] overallOverlap = new List[boardWidth][boardHeight];
        Map<Animal, Set<AnimalBoardInstance>[][]> animalOverlap = new java.util.HashMap<>();
        Map<Animal, Integer> animalRevealedTileCounts = new java.util.HashMap<>();
        setOverlaps(overallOverlap, animalOverlap, permutations, boardWidth, boardHeight, board,
                animalRevealedTileCounts);

        Map<Animal, Integer> animalMaxOverlaps = new java.util.HashMap<>();
        Map<Animal, Double[][]> animalOverlapProbabilities = new java.util.HashMap<>();
        Map<Animal, Double> animalMinProbabilities = new java.util.HashMap<>();
        setMaxAnimalOverlaps(animalMaxOverlaps, animalOverlapProbabilities, overallOverlap, permutations,
                boardWidth, boardHeight, animalMinProbabilities);

        return new Overlaps(overallOverlap, animalOverlap, animalOverlapProbabilities, permutations,
                animalMaxOverlaps, animalMinProbabilities, animalRevealedTileCounts);
    }

    private static void setOverlaps(List<AnimalBoardInstance>[][] overallOverlap,
                                    Map<Animal, Set<AnimalBoardInstance>[][]> animalOverlap,
                                    Set<Tile[][]> permutations, int boardWidth, int boardHeight,  Tile[][] board,
                                    Map<Animal, Integer> animalRevealedTileCounts) {
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                var tileOverlaps = new ArrayList<AnimalBoardInstance>();
                var tile = board[x][y];
                if (!tile.isRevealed()) {
                    for (var permutation : permutations) {
                        var animalBoardInstance = permutation[x][y].getAnimalBoardInstance();
                        tileOverlaps.add(animalBoardInstance);
                        if (animalBoardInstance != null) {
                            var animal = animalBoardInstance.animal();
                            createNewListArrayIfNotPresent(animal, animalOverlap, boardWidth, boardHeight);
                            var animalOverlapForAnimal = animalOverlap.get(animal);
                            animalOverlapForAnimal[x][y].add(animalBoardInstance);
                        }
                    }
                } else if (tile.isOccupied()) { // if revealed and occupied
                    var animal = tile.getAnimalBoardInstance().animal();
                    animalRevealedTileCounts.put(animal,
                            animalRevealedTileCounts.getOrDefault(animal, 0) + 1);
                }
                overallOverlap[x][y] = tileOverlaps;
            }
        }
    }

    private static void createNewListArrayIfNotPresent(Animal animal,
                                                   Map<Animal, Set<AnimalBoardInstance>[][]> animalOverlap,
                                                       int boardWidth, int boardHeight) {
        if (!animalOverlap.containsKey(animal)) {
            animalOverlap.put(animal, new Set[boardWidth][boardHeight]);
            var animalOverlapForAnimal = animalOverlap.get(animal);
            for (int i = 0; i < boardWidth; i++) {
                for (int j = 0; j < boardHeight; j++) {
                    animalOverlapForAnimal[i][j] = new HashSet<>();
                }
            }
        }
    }

    private static void setMaxAnimalOverlaps(Map<Animal, Integer> animalMaxOverlaps,
                                             Map<Animal, Double[][]> animalOverlapProbabilities,
                                             List<AnimalBoardInstance>[][] overallOverlap, Set<Tile[][]> permutations,
                                             int boardWidth, int boardHeight,
                                             Map<Animal, Double> animalMinProbabilities) {
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                var tileOverlaps = overallOverlap[x][y];
                var nullProbabilities =
                        (double) tileOverlaps.stream().filter(Objects::isNull).count() / permutations.size();
                var distinctAnimals = tileOverlaps.stream()
                        .filter(Objects::nonNull)
                        .map(AnimalBoardInstance::animal)
                        .collect(Collectors.toSet());
                for (var animal : distinctAnimals) {
                    if (!animalOverlapProbabilities.containsKey(animal)) {
                        animalOverlapProbabilities.put(animal, new Double[boardWidth][boardHeight]);
                        for (int i = 0; i < boardWidth; i++) {
                            for (int j = 0; j < boardHeight; j++) {
                                animalOverlapProbabilities.get(animal)[i][j] = 0.0;
                            }
                        }
                    }
                    var instancesAtTile =
                            (double) overallOverlap[x][y].stream().filter(instance -> instance != null && instance.animal().equals(animal)).count();
                    var probabilityAtTile = instancesAtTile / permutations.size();
                    animalOverlapProbabilities.get(animal)[x][y] = probabilityAtTile;

                    var minProbabilityForAnimal = animalMinProbabilities.getOrDefault(animal, 0d);
                    animalMinProbabilities.put(animal, Math.min(minProbabilityForAnimal, probabilityAtTile));

                    if(probabilityAtTile < 1){
                        var animalOverlapCount = (int) tileOverlaps.stream()
                                .filter(animalBoardInstance -> animalBoardInstance != null && animalBoardInstance.animal().equals(animal))
                                .distinct()
                                .count();
                        animalMaxOverlaps.put(animal, Math.max(animalMaxOverlaps.getOrDefault(animal, 1),
                                animalOverlapCount));
                    } else {
                        animalMaxOverlaps.putIfAbsent(animal, 1);
                    }

                }
                if (!animalOverlapProbabilities.containsKey(null)) {
                    animalOverlapProbabilities.put(null, new Double[boardWidth][boardHeight]);
                    for (int i = 0; i < boardWidth; i++) {
                        for (int j = 0; j < boardHeight; j++) {
                            animalOverlapProbabilities.get(null)[i][j] = 0d;
                        }
                    }
                }
                animalOverlapProbabilities.get(null)[x][y] = nullProbabilities;

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
                    var clonedBoard = BoardUtil.cloneBoard(permBoard);
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
