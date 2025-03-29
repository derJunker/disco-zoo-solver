import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {Game} from "@/types/Game";
import {Coords} from "@/types/Coords";
import {useErrors} from "@/store/useErrors";

export const useSolver = defineStore('solver', () => {
    const api = useApi();
    const errorStore = useErrors();

    async function solve(game: Game, animalToSolveFor: Animal): Promise<{bestClicks: Coords[], probabilities: number[][]}> {
        const resp = await api.postUrl('/solve', {game, animalToSolveFor});
        if(!resp || !resp.ok) {
            errorStore.addError("Error solving game: " + resp?.status);
            return {bestClicks: [], probabilities: []};
        }
        return resp.json();
    }

    return {solve}
})