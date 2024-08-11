import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from '@/router/router.js'
import BlackDashboard from "./plugins/blackDashboard";
import { createPinia } from 'pinia';


createApp(App)
    .use(router)
    .use(BlackDashboard)
    .use(createPinia())
    .mount('#app')
