package junker.animals;

import java.util.List;

import junker.board.Coords;

public record Animal(
        String name,
        List<Coords> pattern
) {
    @Override
    public String toString() {
        return name.substring(0, 1);
    }
}
