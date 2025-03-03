import { defineStore } from 'pinia'

export const useApi = defineStore('api', () => {
    const baseUrl = process.env.VUE_APP_BACKEND_URL;

    async function fetchUrl(url: string): Promise<Response> {
        return  await fetch(`${baseUrl}${url}`);
    }
    return { fetchUrl }
})