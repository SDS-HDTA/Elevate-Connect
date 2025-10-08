import './assets/main.css';
import permissionDirectives from '@/directives/permissions';

// src/main.js
import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // Added router import
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import { createPinia } from 'pinia';

const app = createApp(App);

app.use(createPinia()); // For state management and stores
app.use(router); // Register the router
app.use(permissionDirectives);
app.use(ElementPlus);
app.mount('#app');
