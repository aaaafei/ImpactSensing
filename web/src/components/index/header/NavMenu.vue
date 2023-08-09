<template>
  <div class="navMenu">

    <template v-for="navMenu in navMenus">
        <!-- 最后一级菜单 -->
      <el-menu-item v-if="!navMenu.children&&navMenu":key="navMenu.id" :data="navMenu" :index="navMenu.url" >
         <img :src="$STATIC_PREFIX+'images/menu/'+navMenu.iconClass"/>
        <span slot="title">{{navMenu.menuname}}</span>
      </el-menu-item>

      <!-- 此菜单下还有子菜单 -->
      <el-submenu v-if="navMenu.children&&navMenu"
                  :key="navMenu.id" :data="navMenu" :index="''+navMenu.id">
        <template slot="title">
          <img :src="$STATIC_PREFIX+'images/menu/'+navMenu.iconClass"/>
          <span> {{navMenu.menuname}}</span>
        </template>
        <!-- 递归 -->
        <NavMenu :navMenus="navMenu.children"></NavMenu>
      </el-submenu>
    </template>

  </div>
</template>

<script>
/* eslint-disable */
  export default {
    name: 'NavMenu',
    props: ['navMenus'],
    data() {
      return {}
    },
    methods: {},
    mounted: function () {
       //console.log(this.navMenus);
    }
  };
</script>

<style scoped>
  .navMenu {
    width:1000px;
    background-color: aqua;
  }
/* 水平样式 */
 .el-menu--horizontal>div>.el-submenu {
    float: left;
}
/* 一级菜单的样式 */
.el-menu--horizontal>div>.el-menu-item {
    float: left;
    height: 40px;
    line-height: 40px;
    margin: 0;
    border-bottom: 2px solid transparent;
    color: #909399;
}
/* 解决下图1 下拉三角图标 */
.el-menu--horizontal>div>.el-submenu .el-submenu__icon-arrow {
    position: static;
    vertical-align: middle;
    margin-left: 8px;
    margin-top: -3px;
}
/* 解决下图2 无下拉菜单时 不对齐问题 */
.el-menu--horizontal>div>.el-submenu .el-submenu__title {
    height: 40px;
    line-height: 40px;
    border-bottom: 2px solid transparent;
    color: #909399;
}
</style>
