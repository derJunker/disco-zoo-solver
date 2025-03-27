import {defineStore} from "pinia";

export const useErrors = defineStore( 'errors', {
    state: () => ({
        errors: [] as string[]
    }),
    actions: {
        addError(error: string) {
            if(this.errors.length > 5)
                this.errors.shift();
            this.errors.push(error);
            setTimeout(() => {
                const index = this.errors.indexOf(error);
                if (index !== -1) {
                    this.errors.splice(index, 1);
                }
            }, 5000);
        },

        removeError(error: string) {
            const index = this.errors.indexOf(error);
            if (index !== -1) {
                this.errors.splice(index, 1);
            }
        }
    }
})