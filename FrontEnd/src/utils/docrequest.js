import axios from 'axios';
import request from '@/utils/request';

const docRequest = axios.create({
  baseURL: 'https://docs.googleapis.com/v1',
  timeout: 600000,
});

// Get token from backend
export const getDocTokens = async () => {
  try {
    const res = await request.get('/google/tokens');
    if (res.code === 1) {
      const accessToken = res.data.access_token;
      localStorage.setItem('google_access_token', accessToken);

      return true;
    }
    return false;
  } catch (error) {
    console.error('Failed to get Google tokens:', error);
    return false;
  }
};

// Clear local token
export const clearDocTokens = () => {
  localStorage.removeItem('google_access_token');
  localStorage.removeItem('google_refresh_token');
};

// Method to refresh token
const refreshAccessToken = async () => {
  try {
    const refreshToken = localStorage.getItem('google_refresh_token');
    if (!refreshToken) {
      throw new Error('No refresh token available');
    }

    const response = await request.get('/google/tokens');

    if (response.code === 1) {
      const accessToken = response.data.accessToken;
      // Update local tokens
      localStorage.setItem('google_access_token', accessToken);
      return accessToken;
    } else {
      throw new Error('Token refresh failed');
    }
  } catch (error) {
    console.error('Token refresh failed:', error);
    clearDocTokens();
    throw error;
  }
};

// Request interceptor
docRequest.interceptors.request.use(
  async (config) => {
    const token = localStorage.getItem('google_access_token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    config.headers['Content-Type'] = 'application/json';
    return config;
  },
  (error) => {
    console.error('Google Docs request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
docRequest.interceptors.response.use(
  (response) => {
    const res = response.data;
    return res;
  },
  async (error) => {
    // If 401 error, try to refresh token
    if (error.response && error.response.status === 401) {
      try {
        const newToken = await refreshAccessToken();
        // Retry original request
        error.config.headers['Authorization'] = `Bearer ${newToken}`;
        return docRequest(error.config);
      } catch (refreshError) {
        console.error('Token refresh failed:', refreshError);
        clearDocTokens();
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

// Wrapper for common Google Docs API methods
export const docApi = {
  // Get token
  getDocTokens,

  // Clear token
  clearDocTokens,

  // Create new document
  createDoc: (data) => docRequest.post('/documents', data),

  // Get document
  getDoc: (docId) => docRequest.get(`/documents/${docId}`),

  // Update document
  updateDoc: (docId, data) => docRequest.patch(`/documents/${docId}`, data),

  // Delete document
  deleteDoc: (docId) => docRequest.delete(`/documents/${docId}`),

  // Get document list
  getDocs: () => docRequest.get('/documents'),
};

export default docRequest;
