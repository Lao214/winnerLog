import axios from 'axios'
import { ElMessage } from 'element-plus'

const client = axios.create({
  baseURL: '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 响应拦截器
client.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '系统异常')
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res.data
  },
  error => {
    const errorMsg = error.response?.data?.message || error.message || '网络连接异常'
    ElMessage.error(errorMsg)
    return Promise.reject(error)
  }
)

export default client
