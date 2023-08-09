/* eslint-disable */
export default {
  install (Vue) {
    Vue.prototype.$checkButtonPermission = function (operation) {
      if (this.theButtons.indexOf(operation) > -1 || this.theButtons.indexOf('*:*') > -1) {
        return true;
      } else {
        return false;
      }
    };
    Vue.prototype.$checkRoleAdmin = function (){
      let roles = this.$store.getters.getUserInfo.roles;
      if(!roles) return false;
      for (let i=0;i<roles.length;i++) {
        if (roles[i].name == 'ROLE_ADMIN') {
          return true;
        }
      }
      return false;
    };
  }
};
