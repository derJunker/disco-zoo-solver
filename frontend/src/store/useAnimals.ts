import { defineStore } from 'pinia'
import {useApi} from "@/store/useApi";

export const useAnimals = defineStore('animals', () => {
    const api = useApi();
    async function getAnimals() {
        return  await api.fetchUrl("/test");
    }
    return { getAnimals}
})