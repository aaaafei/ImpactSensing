'use strict'
// Template version: 1.2.7
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path')

module.exports = {
  dev: {
    // Paths
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    proxyTable: {
      // '/api/frame/code': {
      //   // target: 'http://localhost:8880/',//'http://gitlab.yuanxd.com:9110/',
      //   target: 'http://xt.yuanxd.com:9120/',
      //   changeOrigin: true
      //   // pathRewrite: {
      //   //   '^/api/frame/code': '/api/frame/code'
      //   // }
      // },
      // '/api/frame/modbus': {
      //   target: 'http://localhost:8088/',
      //   // target: 'http://xt.yuanxd.com:9120/',
      //   changeOrigin: true,
      //   pathRewrite: {
      //     '^/api/frame/modbus': '/'
      //   }
      // },
      '/api/': {
        target: 'http://localhost:3000/',
        // target: 'http://xt.yuanxd.com:9120/',
        changeOrigin: true,
        pathRewrite: {
          '^/api/': '/'
        }
      }
    },

    // Various Dev Server settings
    host: '0.0.0.0', // can be overwritten by process.env.HOST
    port: 8081, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

    // Use Eslint Loader?
    // If true, your code will be linted during bundling and
    // linting errors and warnings will be shown in the console.
    useEslint: true,
    // If true, eslint errors and warnings will also be shown in the error overlay
    // in the browser.
    showEslintErrorsInOverlay: false,

    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'eval-source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    // CSS Sourcemaps off by default because relative paths are "buggy"
    // with this option, according to the CSS-Loader README
    // (https://github.com/webpack/css-loader#sourcemaps)
    // In our experience, they generally work as expected,
    // just be aware of this issue when enabling this option.
    cssSourceMap: false,
  },

  build: {
    // Template for index.html
    index: path.resolve(__dirname, '../dist/frame/index.html'),

    // Paths
    /**
     * 在build下的assetsPublicPath默认情况下是'/'，
     * 此时打包的index.html文件中的资源文件(js、css、img)默认情况都是以/开头的绝对路径，
     * 指向http服务器的根路径
     * 如果想修改为相对路径则需要将assetsPublicPath的值修改为'./'，
     * 这样就是指向index.html的相对路径了
     */
    assetsRoot: path.resolve(__dirname, '../dist/frame'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/frame/',

    /**
     * Source Maps
     */

    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
