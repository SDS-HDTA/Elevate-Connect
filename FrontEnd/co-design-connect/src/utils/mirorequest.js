import axios from 'axios'
import { MIRO_CONFIG } from '@/config/miro'
import request from '@/utils/request'

const miroRequest = axios.create({
  baseURL: 'https://api.miro.com/v2',
  timeout: 600000
})

// 从后端获取 token
export const getMiroTokens = async () => {
  try {
    const res = await request.get('/miro/tokens')
    if (res.code === 1) {
      const { accessToken, refreshToken } = res.data
      localStorage.setItem('miro_access_token', accessToken)
      localStorage.setItem('miro_refresh_token', refreshToken)
      return true
    }
    return false
  } catch (error) {
    console.error('获取 Miro tokens 失败:', error)
    return false
  }
}

// 更新 token 到后端
export const updateMiroTokens = async (accessToken, refreshToken) => {
  try {
    const res = await request.post('/miro/tokens', {
      accessToken,
      refreshToken
    })
    return res.code === 1
  } catch (error) {
    console.error('更新 Miro tokens 失败:', error)
    return false
  }
}

// 清除本地 token
export const clearMiroTokens = () => {
  localStorage.removeItem('miro_access_token')
  localStorage.removeItem('miro_refresh_token')
}

// 检查是否已初始化 token
export const hasMiroTokens = () => {
  return !!(localStorage.getItem('miro_access_token') && localStorage.getItem('miro_refresh_token'))
}

// 获取初始 access token
const getInitialAccessToken = async (code) => {
  try {
    const response = await axios.post('https://api.miro.com/v1/oauth/token', {
      grant_type: 'authorization_code',
      client_id: MIRO_CONFIG.clientId,
      client_secret: MIRO_CONFIG.clientSecret,
      code: code,
      redirect_uri: MIRO_CONFIG.redirectUri
    })

    const { access_token, refresh_token, expires_in } = response.data
    
    // 保存 tokens
    localStorage.setItem('miro_access_token', access_token)
    localStorage.setItem('miro_refresh_token', refresh_token)
    
    // 设置 token 过期时间
    const expiresAt = Date.now() + (expires_in * 1000)
    localStorage.setItem('miro_token_expires_at', expiresAt)

    return access_token
  } catch (error) {
    console.error('获取初始 token 失败:', error)
    throw error
  }
}

// 获取授权 URL
const getAuthUrl = () => {
  const scopes = MIRO_CONFIG.scopes.join(' ')
  return `https://miro.com/oauth/authorize?response_type=code&client_id=${MIRO_CONFIG.clientId}&redirect_uri=${encodeURIComponent(MIRO_CONFIG.redirectUri)}&scope=${encodeURIComponent(scopes)}`
}

// 刷新 token 的方法
const refreshAccessToken = async () => {
  try {
    const refreshToken = localStorage.getItem('miro_refresh_token')
    if (!refreshToken) {
      throw new Error('No refresh token available')
    }

    const response = await axios.post('https://api.miro.com/v1/oauth/token', {
      grant_type: 'refresh_token',
      client_id: MIRO_CONFIG.clientId,
      client_secret: MIRO_CONFIG.clientSecret,
      refresh_token: refreshToken
    })

    const { access_token, refresh_token } = response.data
    
    // 更新本地 tokens
    localStorage.setItem('miro_access_token', access_token)
    localStorage.setItem('miro_refresh_token', refresh_token)

    // 更新后端 tokens
    await updateMiroTokens(access_token, refresh_token)

    return access_token
  } catch (error) {
    console.error('刷新 token 失败:', error)
    clearMiroTokens()
    throw error
  }
}


// 请求拦截器
miroRequest.interceptors.request.use(
  async (config) => {
    const token = localStorage.getItem('miro_access_token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    config.headers['Content-Type'] = 'application/json'
    return config
  },
  (error) => {
    console.error('Miro request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
miroRequest.interceptors.response.use(
  (response) => {
    const res = response.data
    return res
  },
  async (error) => {
    // 如果是 401 错误，尝试刷新 token
    if (error.response && error.response.status === 401) {
      try {
        const newToken = await refreshAccessToken()
        // 重试原始请求
        error.config.headers['Authorization'] = `Bearer ${newToken}`
        return miroRequest(error.config)
      } catch (refreshError) {
        console.error('Token refresh failed:', refreshError)
        clearMiroTokens()
        return Promise.reject(refreshError)
      }
    }
    return Promise.reject(error)
  }
)

// 封装常用的 Miro API 方法
export const miroApi = {
  // 获取 token
  getMiroTokens,
  
  // 更新 token
  updateMiroTokens,
  
  // 清除 token
  clearMiroTokens,
  
  // 检查是否已初始化 token
  hasMiroTokens,
  
  // 获取授权 URL
  getAuthUrl,
  
  // 获取初始 access token
  getInitialAccessToken,
  
  // 获取所有 boards
  getBoards: () => miroRequest.get('/boards'),
  
  // 获取特定 board
  getBoard: (boardId) => miroRequest.get(`/boards/${boardId}`),
  
  // 创建新 board
  createBoard: (data) => miroRequest.post('/boards', data),
  
  // 更新 board
  updateBoard: (boardId, data) => miroRequest.patch(`/boards/${boardId}`, data),
  
  // 删除 board
  deleteBoard: (boardId) => miroRequest.delete(`/boards/${boardId}`)
}

export default miroRequest
