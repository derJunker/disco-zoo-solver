package junker.disco.zoo.solver.requests.post_bodies;

import java.util.List;

import junker.disco.zoo.solver.model.animals.Animal;

public record ReconstructStartBody(
        List<Animal> animals,
        String region,
        String petName
) {
}
