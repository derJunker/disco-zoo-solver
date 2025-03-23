import {Coords} from "@/types/Coords";

export interface AccuracySingleClickPerformanceResponse {
    accuracy: number,
    probabilities: number[][],
    bestClicks: Coords[],
}