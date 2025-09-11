import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 600000,
});

// Request interceptor
request.interceptors.request.use(
  (config) => {
    // If the configuration specifies that no token is needed, skip adding the token
    // Note: at the moment this is only the case for the home page. This should be removed later
    if (config.noToken) {
      return config;
    }

    // Get token from localStorage
    const token = localStorage.getItem('token');
    const fullName = localStorage.getItem('fullName');

    // Add token to request headers if it exists
    if (token) {
      config.headers['Authorization'] = `${token}`;
      config.headers['username'] = `${fullName}`;
    }

    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
request.interceptors.response.use(
  (response) => {
    const res = response.data;

    // Update localStorage if response contains new token
    if (response.headers['new-token']) {
      localStorage.setItem('token', response.headers['new-token']);
    }

    // Return the response data directly
    return res;
  },
  (error) => {
    console.error('Response error:', error);
    return Promise.reject(error);
  }
);

export default request;
