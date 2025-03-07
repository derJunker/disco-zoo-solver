package junker.disco.zoo.solver.requests.post_bodies;

import junker.animals.Animal;

public record SolveRequestBody(
        GameDTO game,
        Animal animalToSolveFor
) {
}
