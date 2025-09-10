import { defineStore } from 'pinia';
import { ref } from 'vue';
import request from '@/utils/request';
import { useRouter } from 'vue-router';

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
        return; // Return early if we have cached info
      }

      // TODO: Add token to headers for verification
      // TODO: Handle token expiration
      const res = await request.get(`/user/info?userId=${userId}`);
      if (res.code === 1) {
        userInfo.value = res.data;
        localStorage.setItem('username', res.data.username);
        localStorage.setItem('userEmail', res.data.email);
        localStorage.setItem('type', res.data.type);
      }
      return userInfo.value;
    } catch (error) {
      console.error('Failed to fetch user info:', error);
      userInfo.value = null;

      // Go to login page on error and return to end the function
      router.push('/login');
      return;
    }
  };

  const getUserInfoFromStorage = () => {
    const id = localStorage.getItem('userId');
    if (!id) return null;

    const username = localStorage.getItem('username');
    const email = localStorage.getItem('userEmail');
    const type = localStorage.getItem('type');
    const token = localStorage.getItem('token');

    if (!username || !email || !type || !token) return null;

    userInfo.value = { id, username, email, type, accessToken: token };
    return userInfo.value;
  };

  const setUserInfo = (data) => {
    userInfo.value = data;
    localStorage.setItem('userId', data.id);
    localStorage.setItem('token', data.accessToken);
  };

  const logout = () => {
    localStorage.removeItem('userId');
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('type');
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
