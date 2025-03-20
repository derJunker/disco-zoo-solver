import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {AccuracyGameHistoryElement} from "@/types/AccuracyGameHistoryElement";

export const useState = defineStore('state',  {
    state: () => {
        return {
            selectedAnimals: [] as Animal[],
            selectedRegion: null as string | null,
        }
    },
})

export const useAccuracyState = defineStore( 'accuracyState', {
    state: () => {
        return {
            singleClickHistory: [] as AccuracyGameHistoryElement[]
        }
    },
})