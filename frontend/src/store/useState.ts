import {defineStore} from 'pinia'
import {AccuracyGameHistoryElement} from "@/types/accuracy/AccuracyGameHistoryElement";
import {AccuracyDifficulty} from "@/types/accuracy/AccuracyDifficulty";

export const useAccuracyState = defineStore( 'accuracyState', {
    state: () => {
        return {
            singleClickHistory: [] as AccuracyGameHistoryElement[],
            withTimeless: false,
            region: null as string | null,
            difficulty: null as AccuracyDifficulty | null,
        }
    },
})