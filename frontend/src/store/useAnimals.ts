import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {useErrors} from "@/store/useErrors";

export const useAnimals = defineStore('animals', () => {
    const api = useApi();
    const errorStore = useErrors();


    async function getAnimalsOfRegion(region: string): Promise<Animal[]> {
        const resp =  await api.fetchUrl("/animals?region=" + region).catch(
            (e) => errorStore.addError("Error fetching animals: " + e)
        )
        if (!resp || !resp.ok) {
            if(resp)
                errorStore.addError("Error fetching animals: " + resp?.status);
            return [] as Animal[]
        }

        return await resp.json();
    }

    async function getAnimalsByNames(names: string[]): Promise<Animal[]> {
        const resp = await api.fetchUrl("/animals/byName?names=" + names.join(",")).catch(
            (e) => errorStore.addError("Error fetching animals by names " + e)
        )
        if (!resp || !resp.ok) {
            if(resp)
                errorStore.addError("Error fetching animals by names " + resp?.status);
            return []
        }
        return await resp.json();
    }

    async function getAllPets(): Promise<Animal[]> {
        const resp = await api.fetchUrl("/animals/pets").catch(
            (e) => errorStore.addError("Error fetching pets: " + e)
        )
        if (!resp || !resp.ok) {
            if(resp)
                errorStore.addError("Error fetching pets: " + resp?.status);
            return []
        }
        return await resp.json();
    }

    function getAnimalPictureUrl(animal: Animal): string {
        return `/animals/${animal.name.toLowerCase()}.png`;
    }

    function getPetPictureUrl(animal: Animal): string {
        return `/pets/${animal.name.toLowerCase()}.png`;
    }

    return {getAnimalsOfRegion, getAnimalPictureUrl, getAnimalsByNames, getAllPets, getPetPictureUrl}
})