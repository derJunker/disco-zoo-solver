package junker.disco.zoo.solver.db.repos;


import java.util.List;

import junker.disco.zoo.solver.db.entities.SlowSolutionEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlowSolutionEntryRepository extends JpaRepository<SlowSolutionEntry, String> {
}
