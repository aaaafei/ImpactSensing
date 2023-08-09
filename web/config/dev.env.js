'use strict'
const merge = require('webpack-merge')
// const prodEnv = require('./prod.env')

module.exports = merge({
  NODE_ENV: '"development"',
  API_HOST:"/api/"
})
