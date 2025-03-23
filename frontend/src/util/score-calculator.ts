import {AccuracyGameHistoryElement} from "@/types/accuracy/AccuracyGameHistoryElement";

const scoreWeights = {
    amountOfAnimals:(val: number) => Math.sqrt(val),
    accuracy: (val: number) => (Math.exp((val - 0.7) * 5)) / (Math.exp((1 - 0.7) * 5))+1,
    percentageBestClicks: (val: number) => Math.pow(1-val, 5)+1,
    withTimeless: () => 1.2,
}

export function calculateAccuracy(accuracy: number, percentageBestClicks: number): number {
    const accuracyWeight = 0.95;
    return (accuracy * accuracyWeight) + (percentageBestClicks * (1-accuracyWeight));
}

export function calculateScore(clickHistory: AccuracyGameHistoryElement[], withTimeless: boolean): number {
    let score = 0;
    clickHistory.forEach(accuracyClick => {
        score += calculateSingleGameScore(accuracyClick, withTimeless);
    });
    return Math.round(score * 100);
}

function calculateSingleGameScore(accuracyClick: AccuracyGameHistoryElement, withTimeless: boolean): number {

    const game = accuracyClick.game
    const amountOfAnimals = game.containedAnimals.length
    const accuracy = accuracyClick.accuracy
    const percentageBestClicks = accuracyClick.bestClicks.length / (game.board.length * game.board[0].length)
    const wasBestClick = accuracyClick.wasBestClick
    let timelessMultiplier = 1;
    if (withTimeless.toString() === "true") { // For some reason i cannot just use if(withTimeless)... it always returns true
        timelessMultiplier = 1.2;
    }
    return scoreWeights.amountOfAnimals(amountOfAnimals)* scoreWeights.accuracy(accuracy)
        * (wasBestClick ? scoreWeights.percentageBestClicks(percentageBestClicks) : 1) * timelessMultiplier;
}