import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";

export const useRegions = defineStore('regions', () => {
    const api = useApi();
    let cachedRegions: string[] | null = null;

    async function getAllRegions(): Promise<string[]> {
        if (cachedRegions) {
            return cachedRegions;
        }

        const resp = await api.fetchUrl("/regions");
        if (!resp.ok) {
            throw new Error(`Error fetching animals: ${resp.statusText}`);
        }

        cachedRegions = await resp.json();
        return cachedRegions!;
    }

    return {getAllRegions}
})