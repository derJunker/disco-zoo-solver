export function calculateScore(accuracy: number, percentageBestClicks: number): number {
    // 89% of the score is based on overallAccuracy, 11% from percentageBestClicks
    return (accuracy * 0.89) + (percentageBestClicks * 0.11);
}