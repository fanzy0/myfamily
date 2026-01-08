import request from '@/utils/request'

/**
 * 分页查询商品列表
 */
export function getProductList(params) {
  return request({
    url: '/api/product/page',
    method: 'get',
    params
  })
}

/**
 * 新增商品
 */
export function saveProduct(data, authCode) {
  return request({
    url: '/api/product/save',
    method: 'post',
    data,
    params: { authCode }
  })
}

/**
 * 更新商品
 */
export function updateProduct(data, authCode) {
  return request({
    url: '/api/product/update',
    method: 'post',
    data,
    params: { authCode }
  })
}

/**
 * 冻结商品
 */
export function freezeProduct(productId, authCode) {
  return request({
    url: '/api/product/freeze',
    method: 'post',
    params: { productId, authCode }
  })
}

/**
 * 启用商品
 */
export function enableProduct(productId, authCode) {
  return request({
    url: '/api/product/enable',
    method: 'post',
    params: { productId, authCode }
  })
}

/**
 * 上传图片
 */
export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/api/product/image/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取图片URL
 * 将图片路径转换为可访问的URL
 * 开发环境：需要加上 /shop 前缀以匹配 webpack dev server 代理配置
 * 生产环境：直接使用相对路径，静态文件已部署到 Spring Boot
 */
export function getImageUrl(imagePath) {
  if (!imagePath) {
    return ''
  }
  // 根据环境动态设置前缀
  const prefix = process.env.NODE_ENV === 'development' ? '/shop' : ''
  return `${prefix}/api/product/image/view?path=${encodeURIComponent(imagePath)}`
}

