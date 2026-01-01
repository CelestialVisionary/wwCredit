import { ref } from 'vue'
import { defineStore } from 'pinia'
import apiClient from '../utils/api'

// User interface
export interface User {
  id: number
  userType: number
  mobile: string
  name: string
  nickName: string
  idCard: string
  email: string
  openid: string
  headImg: string
  bindStatus: number
  borrowAuthStatus: number
  integral: number
  status: number
  createTime: string
  updateTime: string
  deleted: boolean
}

export const useUserStore = defineStore('user', () => {
  // User information
  const userInfo = ref<User | null>(null)
  // Login status
  const isLoggedIn = ref(false)
  // Token
  const token = ref<string | null>(localStorage.getItem('token'))

  // Set token
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // Remove token
  const removeToken = () => {
    token.value = null
    localStorage.removeItem('token')
  }

  // Set user information
  const setUserInfo = (newUserInfo: User) => {
    userInfo.value = newUserInfo
    isLoggedIn.value = true
  }

  // Logout
  const logout = () => {
    removeToken()
    userInfo.value = null
    isLoggedIn.value = false
  }

  // Get user information
  const fetchUserInfo = async () => {
    if (!token.value) return null
    
    try {
      const response = await apiClient.get('/user/userInfo', {
        headers: {
          Authorization: `${token.value}`
        }
      })
      
      console.log('User info response:', response)
      
      if (response.data.code === 200) {
        setUserInfo(response.data.data)
        return response.data.data
      }
    } catch (error) {
      console.error('Failed to get user information:', error)
      // 如果获取用户信息失败，可能是token过期，需要重新登录
      logout()
      return null
    }
    return null
  }

  return {
    userInfo,
    isLoggedIn,
    token,
    setToken,
    removeToken,
    setUserInfo,
    logout,
    fetchUserInfo
  }
})