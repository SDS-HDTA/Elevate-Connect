import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '@/views/auth/LoginPage.vue';
import RegisterPage from '@/views/auth/RegisterPage.vue';
import ResetPasswordPage from '@/views/auth/ResetPasswordPage.vue';
import MyProjects from '@/views/project/MyProjects.vue';
import LandingPage from '@/views/LandingPage.vue';
import GetMyProjects from '@/views/project/GetMyProjects.vue';
import JoinProject from '@/views/project/JoinProject.vue';
import ProjectDetails from '@/views/project/projectManage/ProjectDetails.vue';
import FolderDetails from '@/views/project/projectManage/FolderDetails.vue';
import MiroBoard from '@/views/project/projectManage/MiroBoard.vue';
import Map from '@/views/project/projectManage/Map.vue';
import Manager from '@/views/manager/Manager.vue';
import NotFoundPage from '@/views/NotFoundPage.vue';
import { useUserStore } from '@/stores/userStore';
import Discover from '@/views/Discover.vue';
import ProjectInfo from '@/views/project/projectManage/ProjectInfo.vue';
import Posts from '@/views/project/projectManage/Posts.vue';
import Resources from '@/views/project/projectManage/Resources.vue';
import Participants from '@/views/project/projectManage/Participants.vue';
import Activities from '@/views/project/projectManage/Activities.vue';
import { usePermissionStore } from '@/stores/permissionStore';
import { permissions } from '@/models/permission';

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
    meta: {
      requiresAuth: true,
      requiresPermission: permissions.AccessDiscover,
    },
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
            path: 'info',
            name: 'info',
            meta: { requiresAuth: true },
            component: ProjectInfo,
          },
          {
            path: 'posts',
            name: 'posts',
            meta: { requiresAuth: true },
            component: Posts,
          },
          {
            path: 'activities',
            name: 'activities',
            meta: { requiresAuth: true },
            component: Activities,
          },
          {
            path: 'resources',
            name: 'resources',
            meta: { requiresAuth: true },
            component: Resources,
          },
          {
            path: 'participants',
            name: 'participants',
            meta: { requiresAuth: true },
            component: Participants,
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
      // TODO: Restore Miro integration
      // {
      //   path: 'board/:boardId',
      //   name: 'miro-board',
      //   meta: { requiresAuth: true },
      //   component: MiroBoard,
      // },
    ],
  },
  {
    path: '/manager',
    name: 'manager',
    component: Manager,
    meta: {
      requiresAuth: true,
      requiresPermission: permissions.AdminAllPermissions,
    },
    children: [
      {
        path: 'users',
        name: 'manager-users',
        meta: {
          requiresAuth: true,
          requiresPermission: permissions.AdminAllPermissions,
        },
        component: () => import('@/views/manager/UserManagement.vue'),
      },
      {
        path: 'projects',
        name: 'manager-projects',
        meta: {
          requiresAuth: true,
          requiresPermission: permissions.AdminAllPermissions,
        },
        component: () => import('@/views/manager/ProjectManagement.vue'),
      },
      {
        path: 'community',
        name: 'manager-community',
        meta: {
          requiresAuth: true,
          requiresPermission: permissions.AdminAllPermissions,
        },
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
  const permissionStore = usePermissionStore();

  if (!userStore.userInfo) {
    await userStore.getUserInfo();
  }

  const isLoggedIn = !!userStore.userInfo;
  const hasRequiredPermission = permissionStore.hasPermission(
    to.meta.requiresPermission
  );

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

  // If route has permissions required defined, check if user has the permission
  // If not, redirect to not-found page
  if (to.meta.requiresPermission && !hasRequiredPermission) {
    return next({ name: 'not-found' });
  }

  next();
});

export default router;
