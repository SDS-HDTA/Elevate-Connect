import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/views/HomePage.vue'
import LoginPage from '@/views/auth/LoginPage.vue'
import RegisterPage from '@/views/auth/RegisterPage.vue'
import ResetPasswordPage from '@/views/auth/ResetPasswordPage.vue'
import MyProjects from '@/views/project/MyProjects.vue'
import GetMyProjects from '@/views/project/GetMyProjects.vue'
import CreateProject from '@/views/project/CreateProject.vue'
import JoinProject from '@/views/project/JoinProject.vue'
import ProjectDetails from '@/views/project/projectManage/ProjectDetails.vue'
import Channel from '@/views/project/projectManage/Channel.vue'
import Backlog from '@/views/project/projectManage/Backlog.vue'
import WorkPiece from '@/views/project/projectManage/WorkPiece.vue'
import Member from '@/views/project/projectManage/Member.vue'
import Tasks from '@/views/Tasks.vue'
import FolderDetails from '@/views/project/projectManage/FolderDetails.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomePage,
  },
  {
    path: '/my-projects',
    name: 'my-projects',
    component: MyProjects,
    children: [
      {
        path: '',
        name: 'get-my-projects',
        component: GetMyProjects
      },
      {
        path: 'create',
        name: 'create-project',
        component: CreateProject
      },
      {
        path: 'join',
        name: 'join-project',
        component: JoinProject
      },
      {
        path: ':id',
        name: 'project-details',
        component: ProjectDetails,
        children: [
          {
            path: 'channel',
            name: 'channel',
            component: Channel
          },
          {
            path: 'backlog',
            name: 'backlog',
            component: Backlog
          },
          {
            path: 'workpiece',
            name: 'workpiece',
            component: WorkPiece
          },
          {
            path: 'member',
            name: 'member',
            component: Member
          }
        ]
      },
      {
        path: 'workpiece/:id',
        name: 'workpiece-iteration',
        component: FolderDetails
      }
    ]
  },
  {
    path: '/tasks',
    name: 'tasks',
    component: Tasks
  },
  {
    path: '/login',
    name: 'login',
    component: LoginPage
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage
  },
  {
    path: '/reset-password',
    name: 'ResetPassword',
    component: ResetPasswordPage
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})


// 路由守卫
router.beforeEach((to, from, next) => {
  // TODO: 实现实际的用户认证检查
  next()
})

export default router