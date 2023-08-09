/* eslint-disable */
import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: '',
    theme: '',
    layout: '',
    userInfo: {},
    menuActive: 'onduty',
    menuActiveMap: {},
    menus: [],
    indexTabs: [{
      title: '排班管理',
      name: 'onduty',
      content: ''
    }],
    indexTabsValue: sessionStorage.indexTabsValue || 'home',
    includePages: [],
    policeInfo: [],
    faultInfo: [],
    authority: []
  },
  getters: {
    getToken: function (state) {
      if (!state.token) {
        if (sessionStorage.token) {
          state.token = sessionStorage.token;
        }
      }
      return state.token;
    },
    getMenuActive: state => state.menuActive,
    getMenuActiveMap: state => state.menuActiveMap,
    getMenus: state => state.menus,
    getIndexTabs: state => state.indexTabs,
    getIndexTabsValue: state => state.indexTabsValue,
    getIncludePages: state => state.includePages,
    getPoliceInfo: state => state.policeInfo,
    getFaultInfo: state => state.faultInfo,
    getTheme: state => state.theme,
    getLayout: state => state.layout,
    getAuthority: state => state.authority,
    getUserInfo: function (state) {
      if (!state.userInfo.username) {
        if (sessionStorage.getItem('userInfo')) {
          state.userInfo = JSON.parse(sessionStorage.getItem('userInfo'));
        }
      }
      return state.userInfo;
    }
  },
  mutations: {
    setTokens (state, token) {
      state.token = token;
      sessionStorage.token = token;
    },
    delTokens (state) {
      state.token = '';
      sessionStorage.removeItem('token');
    },
    refreshToken (state) {
      state.token = sessionStorage.token;
    },
    refreshIndex (state) {
      state.menuActive = sessionStorage.menuActive || 'home';
      if (sessionStorage.indexTabs) {
        if (typeof sessionStorage.indexTabs === 'string') {
          state.indexTabs = JSON.parse(sessionStorage.indexTabs);
        } else {
          state.indexTabs = sessionStorage.indexTabs;
        }
      }
      state.indexTabsValue = state.indexTabsValue || 'home';
      if (sessionStorage.includePages) {
        if (typeof sessionStorage.includePages === 'string') {
          state.includePages = sessionStorage.includePages.split(',');
        } else {
          state.includePages = sessionStorage.includePages;
        }
      }
    },
    setMenuActive (state, menuActive) {
      state.menuActive = menuActive;
      sessionStorage.menuActive = menuActive;
    },
    setMenuActiveMap (state, menuActiveMap) {
      state.menuActiveMap = menuActiveMap;
      sessionStorage.menuActiveMap = menuActiveMap;
    },
    setMenus (state, menus) {
      state.menus = menus;
      sessionStorage.menus = menus;
    },
    setIndexTabs (state, indexTabs) {
      state.indexTabs = indexTabs;
      sessionStorage.indexTabs = JSON.stringify(indexTabs);
    },
    setIndexTabsValue (state, indexTabsValue) {
      state.indexTabsValue = indexTabsValue;
      sessionStorage.indexTabsValue = indexTabsValue;
    },
    setIncludePages (state, includePages) {
      sessionStorage.includePages = includePages;
      if (typeof includePages === 'string') {
        state.includePages = includePages.split(',');
      } else {
        state.includePages = includePages;
      }
    },
    setPoliceInfo (state, policeInfo) {
      state.policeInfo = policeInfo;
    },
    setFaultInfo (state, faultInfo) {
      state.faultInfo = faultInfo;
    },
    setTheme (state, theme) {
      state.theme = theme;
    },
    setLayout (state, layout) {
      state.layout = layout;
    },
    setAuthority (state, authority) {
      state.authority = authority;
    },
    setUserInfo (state, userInfo) {
      state.userInfo = userInfo;
      sessionStorage.setItem('userInfo', JSON.stringify(userInfo));
    }
  },
  actions: {
    setPolice ({commit, state}, policeInfo) {
      commit('setPoliceInfo', policeInfo);
    },
    setFault ({commit, state}, faultInfo) {
      commit('setFaultInfo', faultInfo);
    },
    setToken ({commit, state}, token) {
      commit('setTokens', token);
    },
    delToken ({commit, state}) {
      commit('delTokens');
    },
    refresh ({commit, state}) {
      commit('refreshToken');
    },
    refreshIndexs ({commit, state}) {
      commit('refreshIndex');
    },
    setActive ({commit, state}, mActive) {
      commit('setMenuActive', mActive);
    },
    setTabs ({commit, state}, tabs) {
      commit('setIndexTabs', tabs);
    },
    setTabsValue ({commit, state}, tValue) {
      commit('setIndexTabsValue', tValue);
    },
    setPages ({commit, state}, page) {
      commit('setIncludePages', page);
    },
    setTheme ({commit, state}, theme) {
      commit('setTheme', theme);
    },
    setLayout ({commit, state}, layout) {
      commit('setLayout', layout);
    },
    setAuthority ({commit, state}, authority) {
      commit('setAuthority', authority);
    },
    setUserInfo ({commit, state}, userInfo) {
      commit('setUserInfo', userInfo);
    }
  }
});
