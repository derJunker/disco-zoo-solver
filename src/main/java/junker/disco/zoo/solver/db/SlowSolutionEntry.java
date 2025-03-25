package junker.disco.zoo.solver.db;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import junker.disco.zoo.solver.board.Coords;
import junker.disco.zoo.solver.board.util.DoubleArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="slow_solution_entry")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlowSolutionEntry {

    @Id
    private String hash;

    @ElementCollection
    private Set<Coords> bestClicks;

    @Lob  // Store as large object (useful for big matrices)
    @Column(columnDefinition = "TEXT") // Ensures storage as text
    @Convert(converter = DoubleArrayUtil.DoubleArrayConverter.class) // Custom converter
    Double[][] probabilities;
}
