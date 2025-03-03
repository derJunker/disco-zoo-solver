import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";

export const useAnimals = defineStore('animals', () => {
    const api = useApi();
    async function getAnimalsOfRegion(region: string): Promise<Animal[]> {
        const resp =  await api.fetchUrl("/animals?region=" + region);
        return await resp.json();
    }
    return {getAnimalsOfRegion}
})