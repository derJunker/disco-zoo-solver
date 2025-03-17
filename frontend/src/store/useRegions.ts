import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";

export const useRegions = defineStore('regions', () => {
    const api = useApi();
    let cachedRegions: string[] | null = null;

    const hardcodedRegions = ["Farm", "Outback", "Savanna", "Northern", "Polar", "Jungle", "Jurassic", "Ice Age", "City", "Mountain", "Nocturnal", "Moon", "Mars", "Constellation"];

    async function getAllRegions(allowAny = true): Promise<string[]> {
        let allRegions: string[] = []
        if (cachedRegions) {
            allRegions = cachedRegions;
        }

        const resp = await api.fetchUrl("/regions");
        if (!resp.ok) {
            throw new Error(`Error fetching animals: ${resp.statusText}`);
        }

        cachedRegions = await resp.json();
        allRegions = cachedRegions!;
        if (!allowAny) {
            allRegions = allRegions.filter((region) => region.toLowerCase() !== "any");
        }
        return allRegions;
    }

    return {getAllRegions, hardcodedRegions}
})