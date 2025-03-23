import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {ClickChangeInfo, Game} from "@/types/Game";
import {Coords} from "@/types/Coords";
import {AccuracySingleClickGameResponse} from "@/types/accuracy/AccuracySingleClickGameResponse";
import {AccuracySingleClickPerformanceResponse} from "@/types/accuracy/AccuracySingleClickPerformanceResponse";
import {AccuracyDifficulty} from "@/types/accuracy/AccuracyDifficulty";

export const useGame = defineStore('game', () => {
    const api = useApi();
    async function startReconstruct(animals: Animal[], region: string): Promise<Game> {
        const resp = await api.postUrl("/reconstruct/start", {
            animals: animals,
            region: region
        });
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

    async function accuracySingleClickGetGame(seed: number, region: string, timeless: boolean, gameNumber: number, difficulty: string): Promise<AccuracySingleClickGameResponse> {
        const resp = await api.fetchUrl(`/accuracy/single-click/${seed}`, {
            region: region,
            difficulty: difficulty,
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