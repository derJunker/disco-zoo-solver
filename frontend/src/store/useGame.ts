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

    async function startReconstruct(animals: Animal[], region: string, petName: string | null): Promise<Game | null> {
        const resp = await api.postUrl("/reconstruct/start", {
            animals: animals,
            petName: petName,
            region: region
        }).catch(
            (e) => {
                errorStore.addError("Error starting game: " + e);
                return null;
            }
        )
        if(!resp || !resp.ok) {
            errorStore.addError("Error starting game: " + resp?.status);
            return null
        }
        return resp.json();
    }

    async function clickReconstruct(game: Game, animal: Animal | null, coords: Coords): Promise<ClickChangeInfo | null> {
        const resp = await api.postUrl("/reconstruct/click", {
            "game": game,
            "animal": animal,
            "coords": coords
        }).catch(
            (e) => errorStore.addError("Error clicking game: " + e)
        )
        if(!resp || !resp.ok) {
            if(resp)
                errorStore.addError("Error clicking game: " + resp?.status);
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
        }).catch(
            (e) => errorStore.addError("Error fetching game: " + e)
        )
        if(!resp || !resp.ok) {
            if(resp)
                errorStore.addError("Error fetching game: " + resp?.status);
            return null
        }
        return resp.json();
    }

    async function accuracyPerformance(game: Game, animalToFind: Animal, click: Coords): Promise<AccuracySingleClickPerformanceResponse | null> {
        const resp = await api.postUrl("/accuracy", {
            "game": game,
            "animalToFind": animalToFind,
            "click": click
        }).catch(
            (e) => errorStore.addError("Error fetching performance: " + e)
        )
        if(!resp || !resp.ok) {
            if(resp)
                errorStore.addError("Error fetching performance: " + resp?.status);
            return null;
        }
        return resp.json()
    }

    return {startReconstruct, clickReconstruct, accuracySingleClick: accuracySingleClickGetGame, accuracyPerformance}
})