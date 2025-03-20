import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {ClickChangeInfo, Game} from "@/types/Game";
import {Coords} from "@/types/Coords";
import {AccuracySingleClickGameResponse} from "@/types/AccuracySingleClickGameResponse";
import {AccuracySingleClickPerformanceResponse} from "@/types/AccuracySingleClickPerformanceResponse";

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

    async function accuracySingleClickGetGame(seed: number, region: string, timeless: boolean, gameNumber: number): Promise<AccuracySingleClickGameResponse> {
        const resp = await api.fetchUrl(`/accuracy/single-click/${seed}`, {
            region: region,
            timeless: timeless,
            gameNumber: gameNumber
        });
        return resp.json();
    }

    async function accuracyPerformance(game: Game, animalToFind: Animal, click: Coords): Promise<AccuracySingleClickPerformanceResponse> {
        const resp = await api.postUrl("/accuracy", {
            "game": game,
            "animalToFind": animalToFind,
            "click": click
        })
        return resp.json()
    }

    return {startReconstruct, clickReconstruct, accuracySingleClick: accuracySingleClickGetGame, accuracyPerformance}
})