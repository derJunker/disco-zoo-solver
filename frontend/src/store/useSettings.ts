import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {useErrors} from "@/store/useErrors";
import {ref} from "vue";

export const useSettings = defineStore('settings', () => {
    const showPercentages = ref(getSettingFromLocalStorage('showPercentages', false));
    const highlightSolved = ref(getSettingFromLocalStorage('highlightSolved', true));

    function getSettingFromLocalStorage(settingName: string, defaultValue: any) {
        const storedValue = localStorage.getItem(`disco-zoo_settings_${settingName}`);
        console.log(`disco_settings_${settingName}`, storedValue);
        return storedValue !== null ? JSON.parse(storedValue) : defaultValue;
    }

    function setSettingToLocalStorage(settingName: string, value: any) {
        localStorage.setItem(`disco-zoo_settings_${settingName}`, JSON.stringify(value));
    }

    function setShowPercentages(value: boolean) {
        showPercentages.value = value;
        setSettingToLocalStorage('showPercentages', value);
    }

    function setHighlightSolved(value: boolean) {
        highlightSolved.value = value;
        setSettingToLocalStorage('highlightSolved', value);
    }

    return {
        showPercentages,
        setShowPercentages,
        highlightSolved,
        setHighlightSolved
    };
});