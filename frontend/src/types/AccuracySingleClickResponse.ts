import {Animal} from "@/types/Animal";
import {Game} from "@/types/Game";

export interface AccuracySingleClickResponse {
    game: Game,
    animalToFind: Animal,
}