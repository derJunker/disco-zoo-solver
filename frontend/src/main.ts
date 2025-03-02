import { createApp } from 'vue'
import { createPinia } from 'pinia'


import App from './App.vue'
import router from './router'

const app = createApp(App);
const pinia = createPinia()

app.config.globalProperties.$baseUrl = process.env.VUE_APP_BACKEND_URL;

app.use(pinia);
app.use(router);
app.mount('#app');
