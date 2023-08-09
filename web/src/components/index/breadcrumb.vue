<template>
  <div>
      <el-breadcrumb>
        <el-breadcrumb-item :to="{ path: 'home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{storedMenu.menuname}}</el-breadcrumb-item>
      </el-breadcrumb>
  </div>
</template>

<script>
  /* eslint-disable */
  export default {
    data () {
      return {
        
      };
    },
    computed: {
      storedMenu() {
        return this.$store.getters.getMenuActiveMap;
      }
    },
    watch: {
      storedMenu: function(newval, oldval) {
        // this.menu = newval;
      }
    },
    methods: {

    },
    mounted: function() {
      
    }
    
  };
</script>

<style scoped>

</style>