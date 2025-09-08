import './assets/main.css';

// src/main.js
import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // Added router import
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

const app = createApp(App);
app.use(router); // Register the router
app.use(ElementPlus);
app.mount('#app');
