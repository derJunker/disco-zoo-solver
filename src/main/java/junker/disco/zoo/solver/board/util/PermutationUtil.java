package junker.disco.zoo.solver.board.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.Game;
import junker.disco.zoo.solver.board.Tile;
import junker.disco.zoo.solver.board.solve.OverlapCalulator;
import junker.disco.zoo.solver.model.animals.Animal;

public class PermutationUtil {

    public static Set<Animal> getPlaceableAnimals(Game game, int x, int y) {
        var potentialAnimals = new ArrayList<>(game.getContainedAnimals());
        potentialAnimals.add(null);
        return potentialAnimals.stream()
                .filter(anim -> canClickAndPlace(game, x, y, anim))
                .collect(Collectors.toSet());
    }

    public static Set<Tile[][]> calculateBoardPermutations(Tile[][] board, List<Animal> animals) {
        var permutations = new HashSet<Tile[][]>();
        if (animals.isEmpty()) {
            return permutations;
        }
        var placements = getPossiblePlacements(board, animals.getFirst());
        for (var placement : placements) {
            var newBoard = BoardUtil.cloneBoard(board);
            BoardUtil.placeAnimal(newBoard, animals.getFirst(), placement);
            var newAnimals = animals.subList(1, animals.size());
            if (newAnimals.isEmpty()) {
                permutations.add(newBoard);
                continue;
            }
            var newPermutations = calculateBoardPermutations(newBoard, newAnimals);
            if (!newPermutations.isEmpty()) {
                permutations.addAll(newPermutations);
            }
        }
        return permutations;
    }

    private static boolean canClickAndPlace(Game game, int x, int y, Animal animal) {
        var wipedGame = new Game(game, true);
        wipedGame.setTile(x, y, true, animal);
        var permutations = calculateBoardPermutations(wipedGame.getBoard(), game.getContainedAnimals());
        return !permutations.isEmpty();
    }

    private static List<Coords> getPossiblePlacements(Tile[][] board, Animal animalToPlace) {
        List<Coords> possiblePlacements = new java.util.ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (canPlaceAnimalOfOrigin(board, animalToPlace, i, j)) {
                    possiblePlacements.add(new Coords(i, j));
                }

                if (board[i][j].hasAnimalInstanceOfType(animalToPlace) && board[i][j].isRevealed()) {
                    return getPossiblePlacementsForRevealedAnimal(animalToPlace, board);
                }
            }
        }
        return possiblePlacements;
    }

    private static List<Coords> getPossiblePlacementsForRevealedAnimal(Animal animalToPlace, Tile[][] board) {
        var pattern = animalToPlace.pattern();
        var possiblePlacements = new ArrayList<Coords>();
        var revealedSpots = getAllRevealedSpotOfAnimal(board, animalToPlace);
        if (revealedSpots.isEmpty()) {
            throw new IllegalStateException("No revealed spots found for animal " + animalToPlace.name());
        }
        final var initialRevealedSpot = revealedSpots.getFirst();
        for (var patternAsRevealed : pattern) {
            var potentialOriginX = initialRevealedSpot.x() - patternAsRevealed.x();
            var potentialOriginY = initialRevealedSpot.y() - patternAsRevealed.y();
            var revealedSpotsToCover = new ArrayList<>(revealedSpots);
            var canPlace = true;
            for (var patternSpot : pattern) {
                var x = potentialOriginX + patternSpot.x();
                var y = potentialOriginY + patternSpot.y();
                revealedSpotsToCover.remove(new Coords(x, y));
                if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) {
                    canPlace = false;
                    break;
                } else {
                    var tile = board[x][y];
                    if (tile.isRevealed() && tile.isOccupied() && !tile.hasAnimalInstanceOfType(animalToPlace)) {
                        canPlace = false;
                        break;
                    } else if(tile.isRevealed() && !tile.isOccupied()) {
                        canPlace = false;
                        break;
                    } else if (!tile.isRevealed() && tile.isOccupied()) {
                        canPlace = false;
                        break;
                    }
                }
            }
            if (canPlace && revealedSpotsToCover.isEmpty())
                possiblePlacements.add(new Coords(potentialOriginX, potentialOriginY));
        }



        return possiblePlacements;
    }

    private static List<Coords> getAllRevealedSpotOfAnimal(Tile[][] board, Animal animal) {
        List<Coords> revealedSpots = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].hasAnimalInstanceOfType(animal) && board[i][j].isRevealed()) {
                    revealedSpots.add(new Coords(i, j));
                }
            }
        }
        return revealedSpots;
    }


    private static boolean canPlaceAnimalOfOrigin(Tile[][] board, Animal animal, int startX, int startY) {
        for (Coords coord : animal.pattern()) {
            int x = startX + coord.x();
            int y = startY + coord.y();
            if (x < 0 || x >= board.length || y < 0 || y >= board[0].length ||
                    (!board[x][y].hasAnimalInstanceOfType(animal) && (board[x][y].isOccupied() || board[x][y].isRevealed()))) {
                return false;
            }
        }
        return true;
    }

    public static <T> Set<List<T>> getPermutationOfCollection(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        Set<List<T>> result = new HashSet<>();
        if (list.isEmpty()) {
            result.add(new ArrayList<>());
            return result;
        }
        T firstElement = list.getFirst();
        List<T> remainingList = list.subList(1, list.size());
        Set<List<T>> permutations = getPermutationOfCollection(remainingList);
        for (List<T> permutation : permutations) {
            for (int i = 0; i <= permutation.size(); i++) {
                List<T> newPermutation = new ArrayList<>(permutation);
                newPermutation.add(i, firstElement);
                result.add(newPermutation);
            }
        }
        return result;
    }



}
