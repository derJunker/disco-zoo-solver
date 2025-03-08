package junker.disco.zoo.solver.requests.post_bodies;

import junker.disco.zoo.solver.model.animals.Animal;

public record SolveRequestBody(
        GameDTO game,
        Animal animalToSolveFor
) {
}
