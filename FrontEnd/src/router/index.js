import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '@/views/auth/LoginPage.vue';
import RegisterPage from '@/views/auth/RegisterPage.vue';
import ResetPasswordPage from '@/views/auth/ResetPasswordPage.vue';
import MyProjects from '@/views/project/MyProjects.vue';
import LandingPage from '@/views/LandingPage.vue';
import GetMyProjects from '@/views/project/GetMyProjects.vue';
import JoinProject from '@/views/project/JoinProject.vue';
import ProjectDetails from '@/views/project/projectManage/ProjectDetails.vue';
import Channel from '@/views/project/projectManage/Channel.vue';
import Backlog from '@/views/project/projectManage/Backlog.vue';
import WorkPiece from '@/views/project/projectManage/WorkPiece.vue';
import Member from '@/views/project/projectManage/Member.vue';
import FolderDetails from '@/views/project/projectManage/FolderDetails.vue';
import MiroBoard from '@/views/project/projectManage/MiroBoard.vue';
import Map from '@/views/project/projectManage/Map.vue';
import Manager from '@/views/manager/Manager.vue';
import NotFoundPage from '@/views/NotFoundPage.vue';
import { useUserStore } from '@/stores/userStore';
import Discover from '@/views/Discover.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    meta: { requiresAuth: false },
    component: LandingPage,
  },
  {
    path: '/discover',
    name: 'discover',
    meta: { requiresAuth: true, roles: [2, 3] },
    component: Discover,
  },
  {
    path: '/my-projects',
    name: 'my-projects',
    meta: { requiresAuth: true },
    component: MyProjects,
    children: [
      {
        path: '',
        name: 'get-my-projects',
        meta: { requiresAuth: true },
        component: GetMyProjects,
      },
      {
        path: 'join',
        name: 'join-project',
        meta: { requiresAuth: true, roles: [3] }, // TODO: remove this page, managers will add users to projects likely in a modal
        component: JoinProject,
      },
      {
        path: ':id',
        name: 'project-details',
        meta: { requiresAuth: true },
        component: ProjectDetails,
        children: [
          {
            path: 'channel',
            name: 'channel',
            meta: { requiresAuth: true },
            component: Channel,
          },
          {
            path: 'backlog',
            name: 'backlog',
            meta: { requiresAuth: true },
            component: Backlog,
          },
          {
            path: 'workpiece',
            name: 'workpiece',
            meta: { requiresAuth: true },
            component: WorkPiece,
          },
          {
            path: 'member',
            name: 'member',
            meta: { requiresAuth: true },
            component: Member,
          },
          {
            path: 'map',
            name: 'map',
            meta: { requiresAuth: true },
            component: Map,
          },
        ],
      },
      {
        path: 'workpiece/:projectId/:statusId/:iterationId',
        name: 'workpiece-iteration',
        meta: { requiresAuth: true },
        component: FolderDetails,
      },
      {
        path: 'board/:boardId',
        name: 'miro-board',
        meta: { requiresAuth: true },
        component: MiroBoard,
      },
    ],
  },
  {
    path: '/manager',
    name: 'manager',
    component: Manager,
    meta: { requiresAuth: true, roles: [3] },
    children: [
      {
        path: 'users',
        name: 'manager-users',
        meta: { requiresAuth: true, roles: [3] },
        component: () => import('@/views/manager/UserManagement.vue'),
      },
      {
        path: 'projects',
        name: 'manager-projects',
        meta: { requiresAuth: true, roles: [3] },
        component: () => import('@/views/manager/ProjectManagement.vue'),
      },
      {
        path: 'community',
        name: 'manager-community',
        meta: { requiresAuth: true, roles: [3] },
        component: () => import('@/views/manager/CommunityManagement.vue'),
      },
    ],
  },
  {
    path: '/login',
    name: 'login',
    meta: { requiresAuth: false },
    component: LoginPage,
  },
  {
    path: '/register',
    name: 'register',
    meta: { requiresAuth: false },
    component: RegisterPage,
  },
  {
    path: '/reset-password',
    name: 'reset-password',
    component: ResetPasswordPage,
  },
  {
    path: '/not-found',
    name: 'not-found',
    component: NotFoundPage,
  },
  // Catch-all route for undefined paths
  {
    path: '/:pathMatch(.*)*',
    redirect: '/not-found',
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// Router guard
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore();

  if (!userStore.userInfo) {
    await userStore.getUserInfo();
  }

  const isLoggedIn = !!userStore.userInfo;

  const userType = userStore.userInfo?.role; // role is not stored in local storage, for security

  // If requiresAuth is true (protected page) and user is not logged in, redirect to login
  if (to.meta.requiresAuth && !isLoggedIn) {
    return next({ name: 'login' });
  }

  // If requiresAuth is false (public page) and user is logged in, redirect to home
  // Except for reset-password page
  if (
    to.meta.requiresAuth === false &&
    isLoggedIn &&
    to.name !== 'reset-password'
  ) {
    return next({ name: 'get-my-projects' });
  }

  // If route has roles defined, check if user has one of the roles
  // If not, redirect to not-found page
  if (to.meta.roles && !to.meta.roles.includes(userType)) {
    return next({ name: 'not-found' });
  }

  next();
});

export default router;
