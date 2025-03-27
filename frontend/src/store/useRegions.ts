import {defineStore} from 'pinia'
import {useApi} from "@/store/useApi";
import {useErrors} from "@/store/useErrors";

export const useRegions = defineStore('regions', () => {
    const api = useApi();
    const errorStore = useErrors();

    const hardcodedRegions = ["Farm", "Outback", "Savanna", "Northern", "Polar", "Jungle", "Jurassic", "Ice Age", "City", "Mountain", "Nocturnal", "Moon", "Mars", "Constellation"];

    async function getAllRegions(): Promise<string[]> {
        const resp = await api.fetchUrl("/regions").catch(
            reason => errorStore.addError("Error fetching regions: " + reason
        ));
        if (!resp) {
            return []
        }

        return await resp.json();
    }

    return {getAllRegions, hardcodedRegions}
})