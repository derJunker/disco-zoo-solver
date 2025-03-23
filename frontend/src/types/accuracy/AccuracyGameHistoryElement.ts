import {AccuracySingleClickPerformanceResponse} from "@/types/accuracy/AccuracySingleClickPerformanceResponse";
import {Game} from "@/types/Game";
import {Animal} from "@/types/Animal";
import {Coords} from "@/types/Coords";

export interface AccuracyGameHistoryElement {
    click: Coords
    accuracy: number,
    animalToFind: Animal,
    game: Game,
    region: string,
    probabilities: number[][],
    minProb: number,
    maxProb: number,
    bestClicks: Coords[],
    wasBestClick: boolean,
}