import { defineStore } from 'pinia';
import { ref } from 'vue';
import request from '@/utils/request';
import { useRouter } from 'vue-router';
import { jwtDecode } from 'jwt-decode';
import { ElMessage } from 'element-plus';

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null);
  const router = useRouter();

  const getUserInfo = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) return null;

      // If Id exists, check if we already have userInfo cached
      const cachedUserInfo = getUserInfoFromStorage();
      if (cachedUserInfo) {
        const res = await request.get(`/user/role?userId=${cachedUserInfo.id}`); // id will always exist if cachedUserInfo is true
        if (res.code === 1) {
          userInfo.value.role = res.data;
        }

        return; // Return early if we have cached info
      }

      const res = await request.get(`/user/info?userId=${userId}`);
      if (res.code === 1) {
        userInfo.value = res.data;
        localStorage.setItem(
          'fullName',
          `${res.data.firstName} ${res.data.lastName}`
        );
        localStorage.setItem('userEmail', res.data.email);
      }
      return userInfo.value;
    } catch (error) {
      console.error('Failed to fetch user info:', error);

      const token = localStorage.getItem('token');
      if (isTokenExpired(token)) {
        logout();
        ElMessage.error('Session expired. Please log in again.');
        return null;
      }

      userInfo.value = null;
      return null;
    }
  };

  const getUserInfoFromStorage = () => {
    const id = localStorage.getItem('userId');
    if (!id) return null;

    const fullName = localStorage.getItem('fullName');
    const email = localStorage.getItem('userEmail');
    const token = localStorage.getItem('token');

    if (!fullName || !email || !token) return null;

    if (isTokenExpired(token)) {
      logout();
      ElMessage.error('Session expired. Please log in again.');
      return null;
    }

    userInfo.value = { id, fullName, email, accessToken: token };
    return userInfo.value;
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

  const setUserInfo = async (data) => {
    const { userId } = jwtDecode(data);
    const userInfoValue = { userId, token: data };

    userInfo.value = userInfoValue;
    localStorage.setItem('userId', userId);
    localStorage.setItem('token', data);

    await getUserInfo();
  };

  const logout = () => {
    localStorage.removeItem('userId');
    localStorage.removeItem('token');
    localStorage.removeItem('fullName');
    localStorage.removeItem('userEmail');
    userInfo.value = null;

    router.push('/login');
  };

  return {
    userInfo,
    getUserInfo,
    getUserInfoFromStorage,
    logout,
    setUserInfo,
  };
});
