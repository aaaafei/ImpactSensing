// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
  /* eslint-disable */
import Vue from 'vue'; // 引入vue
/**
 * 自适应布局，首先安装lib-flexible在项目目录运行命令：npm i lib-flexible --save
 * 再安装px2rem-loade 运行命令：npm install px2rem-loader, 在build下utils.js中配置px2rem-loade
 */
// import 'lib-flexible';
import ElementUI from 'element-ui';
import '@/assets/theme/theme-chalk/index.css';
import '@/assets/theme/theme-chalk/layout.css';
import '@/assets/icon/font_third/iconfont.css';
import App from './App'; // 引入src目录下的App.vue
import routers from './router';
import VueRouter from 'vue-router'; // 引入vue-router
import permUtil from './assets/utils/permUtil.js';

import '../node_modules/video.js/dist/video-js.css';
import store from './store';
import request from '@/assets/utils/request';
import '@/assets/theme/theme_orange/index.css';
import '@/assets/theme/theme_orange/layout.css';
import '@/assets/theme/theme_blue/index.css';
import '@/assets/theme/theme_blue/layout.css';

/**
 * 先运行npm install vue-resource --save
 */
import VueResource from 'vue-resource';
import VideoPlayer from 'vue-video-player'; // 引入vue-video-player视频
import EasyScroll from 'easyscroll';

import echarts from 'echarts';

import BaiduMap from 'vue-baidu-map';

Vue.prototype.$echarts = echarts;

Vue.use(BaiduMap, {
  ak: '8c0Ob2kfbEdDRG5OUwTh96nl'
});

Vue.use(permUtil);
Vue.use(EasyScroll);
Vue.config.productionTip = false; // 来关闭生产模式下给出的提示

Vue.use(VideoPlayer);
Vue.use(VueRouter); // 使用vue-router
Vue.use(VueResource);
Vue.use(ElementUI);
// Vue.prototype.permUtil = permUtil;
Vue.prototype.$api_prefix = '/api/';
// Vue.prototype.$api_prefix_code = '/api/water/frame/code/';

const router = new VueRouter({
  base: '/frame/',
  mode: 'history',
  routes: routers
});
Vue.prototype.$request = request;

// 配置图片路径前缀
if (process.env.NODE_ENV === 'development') {
  Vue.prototype.$STATIC_PREFIX = '../../../static/';
} else {
  Vue.prototype.$STATIC_PREFIX = '../../../frame/static/';
}
// window.jQuery = jQuery;
// 实例配置在上面，路由拦截在下面
router.beforeEach((to, from, next) => {
  if (to.meta.noRequireAuth) {  // 判断该路由是否需要登录权限
    next();
  } else {
    if (store.getters.getToken) {  // 通过vuex state获取当前的token是否存在
      next();
    } else {
      Vue.prototype.$message({
        type: 'error',
        message: '登录过期，请重新登录!',
        onClose: function () {
          next('/login');
        }
      });
    }
  }
});
router.afterEach((to, from) => {
  let usermenus = store.getters.getMenus;
  let menuActiveMap = {
    menuname: '',
    path: ''
  }
  for(let i=0;i<usermenus.length;i++) {
    let item = usermenus[i];
    if(('/'+item.url) == to.path) {
      menuActiveMap.menuname = item.menuname;
      menuActiveMap.path = item.url;
      store.commit('setMenuActiveMap',menuActiveMap);
    }
  }
});
/* eslint-disable no-new */
// Date.prototype.Format = function (fmt) {
//   var o = {
//     'M+': this.getMonth() + 1,
//     'd+': this.getDate(),
//     'H+': this.getHours(),
//     'm+': this.getMinutes(),
//     's+': this.getSeconds(),
//     'q+': Math.floor((this.getMonth() + 3) / 3),
//     'S': this.getMilliseconds()
//   };
//   if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
//   for (var k in o)
//       if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
//   return fmt;
// };
/* eslint-disable no-new */
new Vue({
  el: '#app', // App.vue中的id=app的div
  router,
  store,
  permUtil,
  template: '<App/>', // 这里是把'./App'的内容加到这个模板中
  components: { App } // 这里的App等于App: App ，名称要和模板名称一样，上面的template中写的App,这里也要是App
});
