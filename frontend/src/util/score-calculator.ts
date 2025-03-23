import {AccuracyGameHistoryElement} from "@/types/AccuracyGameHistoryElement";

const scoreWeights = {
    accuracy: (val: number) => val*0.95,
    percentageBestClicks: (val: number) => val*0.5,
    amountOfAnimals:(val: number) => Math.sqrt(val)*1.5,
}

export function calculateAccuracy(accuracy: number, percentageBestClicks: number): number {
    const accuracyWeight = 0.95;
    return (accuracy * accuracyWeight) + (percentageBestClicks * (1-accuracyWeight));
}

export function calculateScore(clickHistory: AccuracyGameHistoryElement[]): number {
    let score = 0;
    clickHistory.forEach(accuracyClick => {
        score += calculateSingleGameScore(accuracyClick);
    });
    return Math.round(score * 100);
}

function calculateSingleGameScore(accuracyClick: AccuracyGameHistoryElement): number {
    const game = accuracyClick.game
    const amountOfAnimals = game.containedAnimals.length
    const accuracy = accuracyClick.accuracy
    const percentageBestClicks = accuracyClick.bestClicks.length / (game.board.length * game.board[0].length)
    return scoreWeights.amountOfAnimals(amountOfAnimals) + scoreWeights.accuracy(accuracy) + scoreWeights.percentageBestClicks(percentageBestClicks);
}