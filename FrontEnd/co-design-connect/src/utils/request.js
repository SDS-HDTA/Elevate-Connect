import axios from 'axios'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

// Request interceptor
request.interceptors.request.use(
  (config) => {
    // Get token from localStorage
    const token = localStorage.getItem('token')

    // Add token to request headers if it exists
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }

    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor
request.interceptors.response.use(
  (response) => {
    const res = response.data

    // Update localStorage if response contains new token
    const newToken = response.headers['new-token'] || (res.data && res.data.accessToken)
    if (newToken) {
      localStorage.setItem('token', newToken)
    }

    // Can be adjusted based on backend data structure
    if (res.code === 1) {
      return res
    }

    // Handle other status codes
    if (res.code === 0) {
      // Token expired or invalid
      localStorage.removeItem('token')
      localStorage.removeItem('id')  // Also remove user ID
      router.push('/login')
      return Promise.reject(new Error(res.message || 'Login expired, please login again'))
    }

  //   // Other error cases
  //   return Promise.reject(new Error(res.message || 'Unknown error'))
  // },
  // (error) => {
  //   console.error('Response error:', error)

  //   // Handle specific error status codes
  //   if (error.response) {
  //     switch (error.response.status) {
  //       case 401:
  //         // Unauthorized, clear token and redirect to login
  //         localStorage.removeItem('token')
  //         localStorage.removeItem('id')  // Also remove user ID
  //         router.push('/login')
  //         return Promise.reject(new Error('Please login first'))

  //       case 403:
  //         return Promise.reject(new Error('Access denied'))

  //       case 404:
  //         return Promise.reject(new Error('Requested resource not found'))

  //       case 500:
  //         return Promise.reject(new Error('Server error, please try again later'))

  //       default:
  //         return Promise.reject(new Error('Network error, please try again later'))
  //     }
  //   }

  //   return Promise.reject(error)
  }
)

export default request
