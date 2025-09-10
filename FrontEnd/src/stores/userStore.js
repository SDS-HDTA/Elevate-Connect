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
        // TODO: make an endpoint to return the role
        const res = await request.get(`/user/info?userId=${cachedUserInfo.id}`); // id will always exist if cachedUserInfo is true
        if (res.code === 1) {
          userInfo.value.role = res.data.role;
        }

        return; // Return early if we have cached info
      }

      // TODO: Handle token expiration
      const res = await request.get(`/user/info?userId=${userId}`);
      if (res.code === 1) {
        userInfo.value = res.data;
        localStorage.setItem('fullName', res.data.fullName);
        localStorage.setItem('userEmail', res.data.email);
      }
      return userInfo.value;
    } catch (error) {
      console.error('Failed to fetch user info:', error);
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

    userInfo.value = { id, fullName, email, accessToken: token };
    return userInfo.value;
  };

  const setUserInfo = async (data) => {
    userInfo.value = data;
    localStorage.setItem('userId', data.id);
    localStorage.setItem('token', data.accessToken);

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
