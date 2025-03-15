import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";

export const useAnimals = defineStore('animals', () => {
    const api = useApi();
    async function getAnimalsOfRegion(region: string): Promise<Animal[]> {
        const resp =  await api.fetchUrl("/animals?region=" + region);
        if (!resp.ok) {
            throw new Error(`Error fetching animals: ${resp.statusText}`);
        }

        return await resp.json();
    }

    async function getAnimalsByNames(names: string[]): Promise<Animal[]> {
        const resp = await api.fetchUrl("/animals/byName?names=" + names.join(","));
        if (!resp.ok) {
            throw new Error(`Error fetching animals: ${resp.statusText}`);
        }
        return await resp.json();
    }

    function getAnimalPictureUrl(animal: Animal): string {
        return `/animals/${animal.name.toLowerCase()}.png`;
    }
    return {getAnimalsOfRegion, getAnimalPictureUrl, getAnimalsByNames}
})