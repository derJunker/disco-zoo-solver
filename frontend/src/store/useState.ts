import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";

export const useState = defineStore('state',  {
    state: () => {
        return {
            selectedAnimals: [] as Animal[],
            selectedRegion: null as string | null,
        }
    },
})