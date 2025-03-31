import './assets/main.css'

// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 新增路由导入

const app = createApp(App)
app.use(router) // 注册路由
app.mount('#app')
