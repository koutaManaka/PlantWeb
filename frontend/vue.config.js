const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    host:'127.0.0.1',
    port: 80,
    // open: true,
    // overlay: {
    //   warnings: false,
    //   errors: true
    // },
    proxy: {
      '/api': {
        target: 'http://localhost:9090',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      },
      '/onenet': {
        target: 'http://api.heclouds.com',
        changeOrigin: true,
        pathRewrite: {
          '^/onenet': ''
        }
      }
    },
    client: {
      webSocketURL: 'ws://0.0.0.0:80/ws',
    },
    headers: {
      'Access-Control-Allow-Origin': '*',
    }
  },
})
