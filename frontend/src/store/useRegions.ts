import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";

export const useRegions = defineStore('regions', () => {
    const api = useApi();

    const hardcodedRegions = ["Farm", "Outback", "Savanna", "Northern", "Polar", "Jungle", "Jurassic", "Ice Age", "City", "Mountain", "Nocturnal", "Moon", "Mars", "Constellation"];

    async function getAllRegions(): Promise<string[]> {
        const resp = await api.fetchUrl("/regions");
        if (!resp.ok) {
            throw new Error(`Error fetching animals: ${resp.statusText}`);
        }

        return await resp.json();
    }

    return {getAllRegions, hardcodedRegions}
})