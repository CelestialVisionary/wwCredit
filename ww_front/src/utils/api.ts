import axios from 'axios'

// 创建Axios实例
const apiClient = axios.create({
  baseURL: '/api/core', // 根据实际后端API地址配置
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
apiClient.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = token
    }
    return config
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  (response) => {
    // 对响应数据做些什么
    return response.data
  },
  (error) => {
    // 对响应错误做些什么
    return Promise.reject(error)
  }
)

export default apiClient