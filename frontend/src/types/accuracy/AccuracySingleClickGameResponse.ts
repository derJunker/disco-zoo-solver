import {Animal} from "@/types/Animal";
import {Game} from "@/types/Game";

export interface AccuracySingleClickGameResponse {
    game: Game,
    animalToFind: Animal,
}