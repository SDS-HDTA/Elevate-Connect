import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/views/LoginPage.vue' // 确保views目录存在
import RegisterPage from '@/views/RegisterPage.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage // 导入注册组件
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router