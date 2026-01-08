const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

const name = '自制shop' // 页面标题

// 如果你的端口设置为80，使用管理员权限执行命令行
const port = process.env.port || process.env.npm_config_port || 9527 // 开发端口

module.exports = {
  /**
   * 如果你打算在子路径下部署你的站点，你将需要设置publicPath，
   * 例如GitHub Pages。如果你打算将你的站点部署到https://foo.github.io/bar/，
   * 那么publicPath应该被设置为"/bar/"。
   * 在大多数情况下请使用'/'！！！
   * 详情: https://cli.vuejs.org/config/#publicpath
   */
  publicPath: '/',
  outputDir: '../resources/static',
  assetsDir: 'static',
  lintOnSave: false, // 开发阶段禁用ESLint检查
  productionSourceMap: false,
  devServer: {
    port: port,
    open: true,
    client: {
      overlay: {
        warnings: false,
        errors: true
      }
    },
    historyApiFallback: true,
    proxy: {
      // 代理所有 /api 的请求到后端
      '/shop': {
        target: 'http://localhost:18002',
        changeOrigin: true,
        pathRewrite: {
          '^/shop': ''
        }
      }
    }
  },
  configureWebpack: {
    // 在webpack的name字段中提供应用程序的标题，这样
    // 它可以在index.html中访问它来注入正确的标题。
    name: name,
    resolve: {
      alias: {
        '@': resolve('src')
      }
    }
  },
  chainWebpack(config) {
    // 删除预加载和预获取插件，避免配置冲突
    config.plugins.delete('preload')
    config.plugins.delete('prefetch')

    // 生产环境优化
    if (process.env.NODE_ENV === 'production') {
      // 代码分割
      config.optimization.splitChunks({
        chunks: 'all',
        cacheGroups: {
          libs: {
            name: 'chunk-libs',
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: 'initial'
          },
          elementUI: {
            name: 'chunk-elementUI',
            priority: 20,
            test: /[\\/]node_modules[\\/]_?element-ui(.*)/
          },
          commons: {
            name: 'chunk-commons',
            test: resolve('src/components'),
            minChunks: 3,
            priority: 5,
            reuseExistingChunk: true
          }
        }
      })
    }
  }
}
