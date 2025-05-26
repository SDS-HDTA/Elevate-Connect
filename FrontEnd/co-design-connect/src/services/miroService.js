import { Miro } from '@mirohq/miro-api'
import { MIRO_CONFIG } from '../config/miro'

class MiroService {
  constructor() {
    this.miro = new Miro({
      clientId: MIRO_CONFIG.clientId,
      clientSecret: MIRO_CONFIG.clientSecret,
      redirectUri: MIRO_CONFIG.redirectUri
    })
  }

  // 获取授权URL
  getAuthUrl() {
    return this.miro.getAuthUrl({
      scopes: MIRO_CONFIG.scopes
    })
  }

  // 获取访问令牌
  async getAccessToken(code) {
    try {
      const response = await this.miro.exchangeCodeForAccessToken(code)
      return response
    } catch (error) {
      console.error('获取访问令牌失败:', error)
      throw error
    }
  }

  // 获取用户信息
  async getUserInfo(accessToken) {
    try {
      const user = await this.miro.getUser(accessToken)
      return user
    } catch (error) {
      console.error('获取用户信息失败:', error)
      throw error
    }
  }

  // 获取画板列表
  async getBoards(accessToken) {
    try {
      const boards = await this.miro.getBoards(accessToken)
      return boards
    } catch (error) {
      console.error('获取画板列表失败:', error)
      throw error
    }
  }
}

export default new MiroService() 