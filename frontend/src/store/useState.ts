import {defineStore} from 'pinia'
import {AccuracyGameHistoryElement} from "@/types/AccuracyGameHistoryElement";
import {Animal} from "@/types/Animal";

export const useAccuracyState = defineStore( 'accuracyState', {
    state: () => {
        return {
            singleClickHistory: [] as AccuracyGameHistoryElement[],
            withTimeless: false,
            region: null as string | null,
        }
    },
})