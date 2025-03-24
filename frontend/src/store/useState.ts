import {defineStore} from 'pinia'
import {AccuracyGameHistoryElement} from "@/types/accuracy/AccuracyGameHistoryElement";
import {AccuracyDifficulty} from "@/types/accuracy/AccuracyDifficulty";
import {
    getDiscoZooLocalStorageItem,
    removeDiscoZooLocalStorageItem,
    setDiscoZooLocalStorageItem
} from "@/util/disco-local-storage";

export const useAccuracyState = defineStore( 'accuracyState', {
    state: () => {
        return {
            singleClickHistory: [] as AccuracyGameHistoryElement[],
            withTimeless: false,
            region: null as string | null,
            difficulty: null as AccuracyDifficulty | null,
            seed: -1,
            _lastSelectedRegion: getDiscoZooLocalStorageItem('acc_last-selected-region') as string | null,
            _lastSelectedDifficulty: getDiscoZooLocalStorageItem('acc_last-selected-difficulty') as AccuracyDifficulty | null,
            _lastSelectedGameType: getDiscoZooLocalStorageItem('acc_last-selected-game-type') as string | null,
        }
    },
    getters: {
        getLastSelectedRegion: (state) => state._lastSelectedRegion,
        getLastSelectedDifficulty: (state) => state._lastSelectedDifficulty,
        getLastSelectedGameType: (state) => state._lastSelectedGameType,
    },
    actions: {
        setLastSelectedRegion(region: string | null) {
            this._lastSelectedRegion = region;
            if (!region)
                removeDiscoZooLocalStorageItem('acc_last-selected-region');
            else
                setDiscoZooLocalStorageItem('acc_last-selected-region', region);
        },
        setLastSelectedDifficulty(difficulty: AccuracyDifficulty | null) {
            this._lastSelectedDifficulty = difficulty;
            if (!difficulty)
                removeDiscoZooLocalStorageItem('acc_last-selected-difficulty');
            else
                setDiscoZooLocalStorageItem('acc_last-selected-difficulty', difficulty.toString());
        },
        setLastSelectedGameType(gameType: string | null) {
            this._lastSelectedGameType = gameType;
            if (!gameType)
                removeDiscoZooLocalStorageItem('acc_last-selected-game-type');
            else
                setDiscoZooLocalStorageItem('acc_last-selected-game-type', gameType);
        },
    }
})

export const useReconstructState = defineStore('reconstructState', {
    state: () => {
        return {
            _lastSelectedRegion: getDiscoZooLocalStorageItem('rec_last-selected-region') as string | null,
        }
    },
    getters: {
        getLastSelectedRegion: (state) => state._lastSelectedRegion,
    },
    actions: {
        setLastSelectedRegion(region: string | null) {
            this._lastSelectedRegion = region;
            if (!region)
                removeDiscoZooLocalStorageItem('rec_last-selected-region');
            else
                setDiscoZooLocalStorageItem('rec_last-selected-region', region);
        }
    }
})

export const userSettingsState = defineStore('userSettingsState', {
    state() {
        const storedRegions = getDiscoZooLocalStorageItem('allowed-timeless-regions');
        const allowedTimelessRegions = storedRegions ? JSON.parse(storedRegions) : [];

        return {
            get allowedTimelessRegions() {
                return allowedTimelessRegions;
            },
        };
    },
    actions: {
        saveToLocalStorage() {
            setDiscoZooLocalStorageItem('allowed-timeless-regions', JSON.stringify(this.allowedTimelessRegions));
        },
        isTimelessRegionAllowed(region: string | null): boolean {
            if (!region) return false;
            return this.allowedTimelessRegions.includes(region.toLowerCase());
        },
        addTimelessRegion(region: string | null) {
            console.log("addTimelessRegion", region);
            console.log("allowedTimelessRegions", this.allowedTimelessRegions);
            if (region && !this.allowedTimelessRegions.includes(region.toLowerCase())) {
                this.allowedTimelessRegions.push(region.toLowerCase());
                this.saveToLocalStorage();
            }
        },
        removeTimelessRegion(region: string | null) {
            console.log("removeTimelessRegion", region);
            console.log("allowedTimelessRegions", this.allowedTimelessRegions);
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
