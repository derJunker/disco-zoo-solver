import { createApp } from 'vue'
import { createPinia } from 'pinia'


import App from './App.vue'
import router from './router'
import './assets/css/transitions.css'
import './assets/css/menus.css'
import './assets/css/buttons.css'
import './assets/css/global.css'

const pinia = createPinia()
const app = createApp(App);

app.use(pinia);
app.use(router);
app.mount('#app');
