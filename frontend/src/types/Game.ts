import {Animal} from "@/types/Animal";

export interface Game {
    board: Tile[][],
    containedAnimals: Animal[],
}

interface Tile {
    animalBoardInstance: {
        animal: Animal,
    },
    revealed: boolean,
    occupied: boolean
}