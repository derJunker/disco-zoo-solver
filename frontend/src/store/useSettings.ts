import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {Animal} from "@/types/Animal";
import {useErrors} from "@/store/useErrors";
import {ref} from "vue";

export const useSettings = defineStore('settings', () => {
    const showPercentages = ref(getSettingFromLocalStorage('showPercentages', false));

    function getSettingFromLocalStorage(settingName: string, defaultValue: any) {
        const storedValue = localStorage.getItem(`disco_settings_${settingName}`);
        return storedValue !== null ? JSON.parse(storedValue) : defaultValue;
    }

    function setSettingToLocalStorage(settingName: string, value: any) {
        localStorage.setItem(`disco_settings_${settingName}`, JSON.stringify(value));
    }

    function setShowPercentages(value: boolean) {
        showPercentages.value = value;
        setSettingToLocalStorage('showPercentages', value);
    }

    return {
        showPercentages,
        setShowPercentages,
    };
});