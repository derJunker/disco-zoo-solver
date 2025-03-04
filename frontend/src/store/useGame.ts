import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {Game} from "@/types/Game";

export const useGame = defineStore('game', () => {
    const api = useApi();
    async function startReconstruct(animals: Animal[]): Promise<Game> {
        const resp = await api.postUrl("/reconstruct/start", animals);
        return resp.json();
    }
    return {startReconstruct}
})