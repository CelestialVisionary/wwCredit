import axios from 'axios'

// 设置所有请求的baseURL
axios.defaults.baseURL = 'http://localhost:8110'

// 请求拦截器
axios.interceptors.request.use(
  (config) => {
    // 可以在这里添加token等请求头
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
axios.interceptors.response.use(
  (response) => {
    // 可以在这里统一处理响应数据格式
    return response
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default axios