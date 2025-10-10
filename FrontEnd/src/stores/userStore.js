import { defineStore } from 'pinia';
import { ref } from 'vue';
import request from '@/utils/request';
import { useRouter } from 'vue-router';
import { jwtDecode } from 'jwt-decode';
import { ElMessage } from 'element-plus';
import { usePermissionStore } from './permissionStore';

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null);
  const router = useRouter();
  const permissionsStore = usePermissionStore();

  const getUserInfo = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) return null;
      if (isTokenExpired(token)) {
        logout();
        ElMessage.error('Session expired. Please log in again.');
        return null;
      }

      const res = await request.get(`/user/info`);
      if (res.code === 1) {
        userInfo.value = res.data;
        userInfo.value.fullName = res.data.firstName + ' ' + res.data.lastName;
      }

      await permissionsStore.getPermissions();

      return userInfo.value;
    } catch (error) {
      const token = localStorage.getItem('token');
      if (token) {
        logout();
      }

      userInfo.value = null;
      return null;
    }
  };

  const isTokenExpired = (token) => {
    try {
      const { exp } = jwtDecode(token);
      if (!exp) return true;
      return Date.now() >= exp * 1000;
    } catch (e) {
      return true;
    }
  };

  const setToken = async (token) => {
    localStorage.setItem('token', token);

    await getUserInfo();
  };

  const logout = () => {
    localStorage.removeItem('token');
    userInfo.value = null;
    permissionsStore.clearPermissions();

    router.push('/login');
  };

  return {
    userInfo,
    getUserInfo,
    logout,
    setToken,
  };
});
