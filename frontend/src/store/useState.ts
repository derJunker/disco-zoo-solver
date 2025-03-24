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
            seed: -1,
        }
    },
})

export const userSettingsState = defineStore('userSettingsState', {
    state() {
        const storedRegions = localStorage.getItem('allowedTimelessRegions');
        const allowedTimelessRegions = storedRegions ? JSON.parse(storedRegions) : [];

        return {
            get allowedTimelessRegions() {
                return allowedTimelessRegions;
            },
        };
    },
    actions: {
        saveToLocalStorage() {
            localStorage.setItem('allowedTimelessRegions', JSON.stringify(this.allowedTimelessRegions));
        },
        isTimelessRegionAllowed(region: string | null): boolean {
            if (!region) return false;
            return this.allowedTimelessRegions.includes(region.toLowerCase());
        },
        addTimelessRegion(region: string | null) {
            if (region && !this.allowedTimelessRegions.includes(region.toLowerCase())) {
                this.allowedTimelessRegions.push(region.toLowerCase());
                this.saveToLocalStorage();
            }
        },
        removeTimelessRegion(region: string | null) {
            if (region) {
                const index = this.allowedTimelessRegions.indexOf(region.toLowerCase());
                if (index >= 0) {
                    this.allowedTimelessRegions.splice(index, 1);
                    this.saveToLocalStorage();
                }
            }
        },
    }
});
