import {defineStore} from 'pinia'
import {AccuracyGameHistoryElement} from "@/types/AccuracyGameHistoryElement";
import {Animal} from "@/types/Animal";

export const useAccuracyState = defineStore( 'accuracyState', {
    state: () => {
        return {
            singleClickHistory: [] as AccuracyGameHistoryElement[]
        }
    },
})

export const useReconstructState = defineStore('reconstructState', {
    state: () => {
        return {
            region: null as string | null,
            animals: [] as Animal[]
        }
    }
})