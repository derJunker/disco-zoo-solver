package junker.disco.zoo.solver.db.entities;

import jakarta.persistence.Embeddable;
import junker.disco.zoo.solver.board.Coords;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoordsEntity {
    private int x;
    private int y;

    public static CoordsEntity fromCoords(Coords coords) {
        return new  CoordsEntity(coords.x(), coords.y());
    }

    public Coords toCoords() {
        return new Coords(x, y);
    }
}
