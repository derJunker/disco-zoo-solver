import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {ClickChangeInfo, Game} from "@/types/Game";
import {Coords} from "@/types/Coords";
import {AccuracySingleClickResponse} from "@/types/AccuracySingleClickResponse";

export const useGame = defineStore('game', () => {
    const api = useApi();
    async function startReconstruct(animals: Animal[]): Promise<Game> {
        const resp = await api.postUrl("/reconstruct/start", animals);
        return resp.json();
    }

    async function clickReconstruct(game: Game, animal: Animal | null, coords: Coords): Promise<ClickChangeInfo> {
        const resp = await api.postUrl("/reconstruct/click", {
            "game": game,
            "animal": animal,
            "coords": coords
        });
        return resp.json();
    }

    async function accuracySingleClick(seed: number, region: string, timeless: boolean, gameNumber: number): Promise<AccuracySingleClickResponse> {
        const resp = await api.fetchUrl(`/accuracy/single-click/${seed}`, {
            region: region,
            timeless: timeless,
            gameNumber: gameNumber
        });
        return resp.json();
    }

    return {startReconstruct, clickReconstruct, accuracySingleClick}
})