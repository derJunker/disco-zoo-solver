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
        const resp = await api.postUrl('/solve', {game, animalToSolveFor}).catch(
            (e) => errorStore.addError("Error solving game: " + e)
        )
        if(!resp || !resp.ok) {
            if(resp)
                errorStore.addError("Error solving game: " + resp?.status);
            return {bestClicks: [], probabilities: []};
        }
        return resp.json();
    }

    return {solve}
})