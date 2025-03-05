import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {Game} from "@/types/Game";
import {Coords} from "@/types/Coords";

export const useGame = defineStore('game', () => {
    const api = useApi();
    async function startReconstruct(animals: Animal[]): Promise<Game> {
        const resp = await api.postUrl("/reconstruct/start", animals);
        return resp.json();
    }

    async function clickReconstruct(game: Game, animal: Animal | null, coords: Coords): Promise<Game> {
        const resp = await api.postUrl("/reconstruct/click", {
            "game": game,
            "animal": animal,
            "coords": coords
        });
        return resp.json();
    }

    return {startReconstruct, clickReconstruct}
})