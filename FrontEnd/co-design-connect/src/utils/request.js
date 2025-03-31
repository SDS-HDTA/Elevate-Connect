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
      config.headers['Authorization'] = `${token}`
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

    // Return the response data directly
    return res

  },
  (error) => {
    console.error('Response error:', error)
    return Promise.reject(error)
  }
)

export default request
