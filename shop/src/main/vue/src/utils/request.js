import axios from 'axios'
import { Message } from 'element-ui'

// 根据环境动态设置 baseURL
// 开发环境：使用 /shop 前缀，通过 webpack dev server 代理转发
// 生产环境：使用空字符串，直接访问同域后端（静态文件已部署到 Spring Boot）
const baseURL = process.env.NODE_ENV === 'development' ? '/shop' : ''

// 创建 axios 实例
const service = axios.create({
  baseURL: baseURL,
  timeout: 30000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    // 如果是文件流（图片），直接返回
    if (response.config.responseType === 'blob') {
      return response
    }
    return res
  },
  error => {
    console.error('响应错误:', error)
    Message({
      message: error.message || '请求失败',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service

