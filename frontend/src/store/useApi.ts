import { defineStore } from 'pinia'

export const useApi = defineStore('api', () => {
    let baseUrl = process.env.VUE_APP_BACKEND_URL || '/';

    // Ensure the base URL is absolute
    if (!baseUrl.startsWith('http')) {
        baseUrl = new URL(baseUrl, window.location.origin).toString().replace(/\/$/, '');
    }

    async function fetchUrl(url: string, params: Record<string, any> = {}): Promise<Response> {

        const queryString = new URLSearchParams(params).toString();
        return await fetch(`${baseUrl}${url}${queryString ? `?${queryString}` : ''}`);
    }

    async function postUrl(url: string, data: any): Promise<Response> {
        return await fetch(`${baseUrl}${url}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });
    }
    return { fetchUrl, postUrl }
})