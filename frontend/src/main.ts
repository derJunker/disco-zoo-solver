import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App);
app.config.globalProperties.$baseUrl = process.env.VUE_APP_BACKEND_URL;
app.use(router).mount('#app');
