import axios from 'axios';

const API_URL = import.meta.env.PROD
  ? 'https://elevate-connect-api-sydney-978545577075.australia-southeast1.run.app'
  : 'http://localhost:8080';

const request = axios.create({
  baseURL: API_URL,
  timeout: 600000,
});

// Request interceptor
request.interceptors.request.use(
  (config) => {
    // Get token from localStorage
    const token = localStorage.getItem('token');

    // Add token to request headers if it exists
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
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
