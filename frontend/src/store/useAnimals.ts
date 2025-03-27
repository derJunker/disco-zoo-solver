import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {useErrors} from "@/store/useErrors";

export const useAnimals = defineStore('animals', () => {
    const api = useApi();
    const errorStore = useErrors();


    async function getAnimalsOfRegion(region: string): Promise<Animal[]> {
        const resp =  await api.fetchUrl("/animals?region=" + region).catch(reason =>
            errorStore.addError("Error fetching animals: " + reason)
        );
        if (!resp) {
            return [] as Animal[]
        }

        return await resp.json();
    }

    async function getAnimalsByNames(names: string[]): Promise<Animal[]> {
        const resp = await api.fetchUrl("/animals/byName?names=" + names.join(",")).catch(
            reason => errorStore.addError("Error fetching animals by names: " + reason)
        );
        if (!resp) {
            return []
        }
        return await resp.json();
    }

    function getAnimalPictureUrl(animal: Animal): string {
        return `/animals/${animal.name.toLowerCase()}.png`;
    }
    return {getAnimalsOfRegion, getAnimalPictureUrl, getAnimalsByNames}
})