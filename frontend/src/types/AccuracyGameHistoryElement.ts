import {AccuracySingleClickPerformanceResponse} from "@/types/AccuracySingleClickPerformanceResponse";
import {Game} from "@/types/Game";
import {Animal} from "@/types/Animal";

export interface AccuracyGameHistoryElement {
    performance: AccuracySingleClickPerformanceResponse,
    game: Game,
    region: string,
    animalToFind: Animal
}