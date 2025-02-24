package junker.board.probabiltiy;

import java.util.ArrayList;
import java.util.List;

import junker.animals.Animal;
import junker.board.BoardService;
import junker.board.Coords;
import junker.board.Tile;

public class PermutationService {



    public static Coords getRandomPlacement(Tile[][] board, Animal animalToPlace) {
        List<Coords> possiblePlacements = getPossiblePlacements(board, animalToPlace);
        if (possiblePlacements.isEmpty()) {
            return null;
        }
        return possiblePlacements.get((int) (Math.random() * possiblePlacements.size()));
    }

    public static List<Tile[][]> calculateBoardPermutations(Tile[][] board, List<Animal> animals) {
        var permutations = new ArrayList<Tile[][]>();
        if (animals.isEmpty()) {
            return permutations;
        }
        var placements = getPossiblePlacements(board, animals.getFirst());
        for (var placement : placements) {
            var newBoard = BoardService.cloneBoard(board);
            BoardService.placeAnimal(newBoard, animals.getFirst(), placement);
            var newAnimals = animals.subList(1, animals.size());
            var newPermutations = calculateBoardPermutations(newBoard, newAnimals);
            if (newPermutations.isEmpty()) {
                permutations.add(newBoard);
            } else {
                permutations.addAll(newPermutations);
            }
        }
        return permutations;
    }

    private static List<Coords> getPossiblePlacements(Tile[][] board, Animal animalToPlace) {
        List<Coords> possiblePlacements = new java.util.ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (canPlaceAnimal(board, animalToPlace, i, j)) {
                    possiblePlacements.add(new Coords(i, j));
                }

                if (board[i][j].hasAnimalInstanceOfType(animalToPlace) && board[i][j].isRevealed()) {
                    return getPossiblePlacementsForRevealedAnimal(board, animalToPlace, i, j);
                }
            }
        }
        return possiblePlacements;
    }

    private static List<Coords> getPossiblePlacementsForRevealedAnimal(Tile[][] board, Animal animalToPlace, int x,
                                                                       int y) {
        List<Coords> possiblePlacements = new java.util.ArrayList<>();
        var pattern = animalToPlace.pattern();
        for (var revealedSpot : pattern) {
            boolean centerIsValid = true;
            var center = new Coords(x - revealedSpot.x(), y - revealedSpot.y());
            for (var other : pattern) {
                if (other.equals(revealedSpot)) {
                    continue;
                }
                var otherSpot = new Coords(center.x() + other.x(), center.y() + other.y());
                if (otherSpot.x() >= 0 && otherSpot.x() < board.length && otherSpot.y() >= 0 && otherSpot.y() < board[0].length) {
                    var tile = board[otherSpot.x()][otherSpot.y()];
                    if (tile.isRevealed() && !tile.hasAnimalInstanceOfType(animalToPlace)) {
                        centerIsValid = false;
                        break;
                    }
                } else {
                    centerIsValid = false;
                    break;
                }
            }
            if (centerIsValid) {
                possiblePlacements.add(center);
            }
        }

        var allRevealedCoords = BoardService.getRevealedCoordsForAnimal(board, animalToPlace);
        return possiblePlacements.stream().filter(possibleCoords -> {
            for (var revealedCoord : allRevealedCoords) {
                if (revealedCoord.equals(possibleCoords)) {
                    return false;
                }
            }
            return true;
        }).toList();

    }


    private static boolean canPlaceAnimal(Tile[][] board, Animal animal, int startX, int startY) {
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



}
