import {Animal} from "@/types/Animal";

export interface Game {
    board: Tile[][],
    containedAnimals: Animal[],
    completelyRevealedAnimals: Animal[],
    notCompletelyRevealedAnimalsWithoutBux: Animal[],
    region: string
}

export interface ClickChangeInfo {
    updatedTile: Tile;
    wasValidClick: boolean;
    completelyRevealedAnimals: Animal[];
    notCompletelyRevealedAnimalsWithoutBux: Animal[];
}

export interface Tile {
    animalBoardInstance: {
        animal: Animal,
    },
    revealed: boolean,
    occupied: boolean
}