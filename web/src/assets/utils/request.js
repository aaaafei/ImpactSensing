import Vue from 'vue';
import axios from 'axios';
import { Message } from 'element-ui';
import store from '@/store';
/* eslint-disable */
// Vue.use(VueRouter);
Vue.prototype.$api_prefix = '/api/';
Vue.prototype.$message = Message;
// create an axios instance
const service = axios.create({
  baseURL: Vue.prototype.$api_prefix, // api的base_url
  method: 'get',
  params: {},
  timeout: 50000 // request timeout
});

// request interceptor
// 暂不拦截
service.interceptors.request.use(config => {
    // Do something before request is sent
  if (store.getters.getToken !== '') {
        // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
    config.headers['token'] = store.getters.getToken; // getToken();
  } else {
    window.location = 'login';
  }
  return config;
}, error => {
    // Do something with request err
  window.console.log(error); // for debug
  Promise.reject(error);
});

// respone interceptor
service.interceptors.response.use(
    response => { 
      if (response.data.status === 403) {
        // Vue.prototype.$router.push('/login');
        Vue.prototype.$message({
          type: 'error',
          message: '认证失效，请重新登录!',
          onClose: function () {
            window.location = 'login';
          }
        });
        // router.push('/login');
        return false;
      }
      // let success = response.data.success;
      // if (success === true) {
      let success = response.data.result_code;
      if(success!=undefined){
        if (success === 0) {
          return response;
        } else {
          // Vue.prototype.$message({
          //   type: 'error',
          //   message: '请求失败! 提示信息：' + response.data.message
          // });
          // window.location.href = '/#/login';
          // window.location = 'login';
          // router.push('/login');
          return response;
        }
      }
      return response;
    },
    error => {
      window.console.log('err:' + error); // for debug
    //   this.$Message.error(error.message); 
      if (error.response != undefined && error.response.data != undefined && error.response.data.status === 403) {
        // Vue.prototype.$router.push('/login');
        Vue.prototype.$message({
          type: 'error',
          message: '认证失效，请重新登录!',
          onClose: function () {
            window.location = 'login';
          }
        });
        // router.push('/login');
        return false;
      } else {
        Vue.prototype.$message({
          type: 'error',
          message: '服务器暂时无法连接，请稍后再试!'
        });
        // window.location = 'login';
        return Promise.reject(error);
      }
    });

export default service;
