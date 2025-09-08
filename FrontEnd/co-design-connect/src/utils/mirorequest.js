import axios from 'axios';
import request from '@/utils/request';

const miroRequest = axios.create({
  baseURL: 'https://api.miro.com/v2',
  timeout: 600000,
});

// Get token from backend
export const getMiroTokens = async () => {
  try {
    const res = await request.get('/miro/tokens');
    if (res.code === 1) {
      const accessToken = res.data.accessToken;
      if (accessToken) {
        localStorage.setItem('miro_access_token', accessToken);
        return true;
      }
    }
    return false;
  } catch (error) {
    console.error('Failed to get Miro tokens:', error);
    return false;
  }
};

// Clear local token
export const clearMiroTokens = () => {
  localStorage.removeItem('miro_access_token');
};

// Method to refresh token
const refreshAccessToken = async () => {
  try {
    const refreshToken = localStorage.getItem('miro_refresh_token');
    if (!refreshToken) {
      throw new Error('No refresh token available');
    }

    const response = await request.get('/miro/tokens');

    if (response.code === 1) {
      const accessToken = response.data.access_token;

      // Update local tokens
      localStorage.setItem('miro_access_token', accessToken);

      return accessToken;
    } else {
      throw new Error('Token refresh failed');
    }
  } catch (error) {
    console.error('Token refresh failed:', error);
    clearMiroTokens();
    throw error;
  }
};

// Request interceptor
miroRequest.interceptors.request.use(
  async (config) => {
    const token = localStorage.getItem('miro_access_token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    config.headers['Content-Type'] = 'application/json';
    return config;
  },
  (error) => {
    console.error('Miro request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
miroRequest.interceptors.response.use(
  (response) => {
    const res = response.data;
    return res;
  },
  async (error) => {
    // If it's a 401 error, try to refresh token
    if (error.response && error.response.status === 401) {
      try {
        const newToken = await refreshAccessToken();
        // Retry original request
        error.config.headers['Authorization'] = `Bearer ${newToken}`;
        return miroRequest(error.config);
      } catch (refreshError) {
        console.error('Token refresh failed:', refreshError);
        clearMiroTokens();
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

// Wrapper for common Miro API methods
export const miroApi = {
  // Get token
  getMiroTokens,

  // Clear token
  clearMiroTokens,

  // Get all boards
  getBoards: () => miroRequest.get('/boards'),

  // Get specific board
  getBoard: (boardId) => miroRequest.get(`/boards/${boardId}`),

  // Create new board
  createBoard: (data) => miroRequest.post('/boards', data),

  // Update board
  updateBoard: (boardId, data) => miroRequest.patch(`/boards/${boardId}`, data),

  // Delete board
  deleteBoard: (boardId) => miroRequest.delete(`/boards/${boardId}`),
};

export default miroRequest;
