import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {ClickChangeInfo, Game} from "@/types/Game";
import {Coords} from "@/types/Coords";
import {AccuracySingleClickGameResponse} from "@/types/accuracy/AccuracySingleClickGameResponse";
import {AccuracySingleClickPerformanceResponse} from "@/types/accuracy/AccuracySingleClickPerformanceResponse";
import {useErrors} from "@/store/useErrors";

export const useGame = defineStore('game', () => {
    const api = useApi();
    const errorStore = useErrors();

    async function startReconstruct(animals: Animal[], region: string): Promise<Game | null> {
        const resp = await api.postUrl("/reconstruct/start", {
            animals: animals,
            region: region
        }).catch(reason => errorStore.addError("Error starting game: " + reason));
        if(!resp) {
            return null
        }
        return resp.json();
    }

    async function clickReconstruct(game: Game, animal: Animal | null, coords: Coords): Promise<ClickChangeInfo | null> {
        const resp = await api.postUrl("/reconstruct/click", {
            "game": game,
            "animal": animal,
            "coords": coords
        }).catch(reason => errorStore.addError("Error clicking game: " + reason));
        if(!resp) {
            return null
        }
        return resp.json();
    }

    async function accuracySingleClickGetGame(seed: number, region: string, timeless: boolean, gameNumber: number, difficulty: string): Promise<AccuracySingleClickGameResponse | null> {
        const resp = await api.fetchUrl(`/accuracy/single-click/${seed}`, {
            region: region,
            difficulty: difficulty,
            timeless: timeless,
            gameNumber: gameNumber
        }).catch(reason => errorStore.addError("Error fetching game: " + reason));
        if(!resp) {
            return null
        }
        return resp.json();
    }

    async function accuracyPerformance(game: Game, animalToFind: Animal, click: Coords): Promise<AccuracySingleClickPerformanceResponse | null> {
        const resp = await api.postUrl("/accuracy", {
            "game": game,
            "animalToFind": animalToFind,
            "click": click
        }).catch(reason => errorStore.addError("Error fetching performance: " + reason));
        if(!resp) {
            return null;
        }
        return resp.json()
    }

    return {startReconstruct, clickReconstruct, accuracySingleClick: accuracySingleClickGetGame, accuracyPerformance}
})