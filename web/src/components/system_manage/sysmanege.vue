<template>
  <!--<div>-->
  <!--<div>
    系统管理
  </div>-->

 <!-- <div class="layui-row" id="content">
    <div class="nav-ul second-menu">
      <ul id="sysManagemenu" class="second-nav" :class="[slide ? 'menu-show' : 'menu-hide']">
        <li class="no-selected" theindex="0" @click="change(0,'组织管理')">
          <router-link to="/user" >
            <div>组织管理</div>
          </router-link>
        </li>
        <li class="no-selected" theindex="1" @click="change(50,'用户管理')">
          <router-link to="/user">
            <div>用户管理</div>
          </router-link>
        </li>
        <li class="no-selected" theindex="2" @click="change(100,'角色管理')">
          <router-link to="/user">
            <div>角色管理</div>
          </router-link>
        </li>
        <li class="no-selected" theindex="3" @click="change(150,'岗位管理')">
          <router-link to="/user">
            <div>岗位管理</div>
          </router-link>
        </li>
      </ul>
     <span class="selected-tip" id="cdc" :style="styleObject">
           <span>{{tipText}}</span>
      </span>
    </div>
    <div class="nav-content" >
      <router-view></router-view>
    </div>
  </div>-->
  <div style="height: 100%">
    <el-row class="tac" style="height: 100%">
      <el-col :span="2" style="background: #CFE9FF;height: 100%">
        <el-menu class="el-menu-vertical-demo">
          <router-link to="/orga">
            <el-menu-item index="1" style="text-align: center" @click="change(5,'组织管理')">
             <!-- <i class="el-icon-menu"></i>-->
              <span slot="title">组织管理</span>
            </el-menu-item>
          </router-link>
          <router-link to="/user">
            <el-menu-item index="2" style="text-align: center" @click="change(63,'用户管理')">
              <!-- <i class="el-icon-menu"></i>-->
              <span slot="title">用户管理</span>
            </el-menu-item>
          </router-link>
          <router-link to="/role">
            <el-menu-item index="3" style="text-align: center" @click="change(120,'角色管理')">
              <!-- <i class="el-icon-menu"></i>-->
              <span slot="title">角色管理</span>
            </el-menu-item>
          </router-link>
          <!-- <router-link to="/post">
            <el-menu-item index="4" style="text-align: center" @click="change(176,'岗位管理')">
              <span slot="title">岗位管理</span>
            </el-menu-item>
          </router-link> -->
        </el-menu>
        <span class="selected-tip" id="cdc" :style="styleObject">
           <span>{{tipText}}</span>
      </span>
      </el-col>
      <el-col :span="22" style="padding-left: 3.5%;padding-top: 10px">
        <router-view></router-view>
      </el-col>
    </el-row>
  </div>

</template>
<script>
  import SystemRouter from 'vue-router';
  const orga = () => import('./organization_manage/orgbox');
  const user = () => import('./user_manage/userbox');
  const role = () => import('./role_manage/rolebox');
  const post = () => import('./post_manage/postbox');
  let routes = [
    {
      path: '/',
      redirect: '/orga' // 指定默认页面
    },
    {
      path: '',
      redirect: '/orga' // 指定默认页面
    },
    {
      path: '/orga', component: orga
    },
    {
      path: '/user', component: user
    },
    {
      path: '/role', component: role
    },
    {
      path: '/post', component: post
    }
  ];
  let router = new SystemRouter({
    routes
  });
  export default {
    name: 'sysmanage',
    router,
    data: function () {
      return {
        slide: true,
        styleObject: {
          top: '0px'
        },
        tipText: ''
      };
    },
    components: {
    },
    methods: {
      menuSlide: function () {
        this.slide = !this.slide;
      },
      change: function (index, text) {
        this.styleObject.top = index + 'px';
        this.tipText = text;
      }
    },
    mounted: function () {
      this.$nextTick(function () {
        this.change(5, '组织管理');
      });
    }
  };
</script>


<style scoped>
  .second-menu{
    background-color: #CFE9FF;
  }
  .second-nav{
    width: 120px;
  }
  .second-nav li{

    width: 120px;
    height: 50px;
    padding: 0px;
    text-align: center;
    line-height: 50px;
  }
  .selected-tip{
    display: inline-block;
    width: 12.5%;
    height: 56px;
    background: url(../../../static/images/second_select.png) no-repeat;
    background-size: 100% 100%;
    position: absolute;
    left: -2%;
    text-align: center;
    top:5px;
    line-height: 50px;
    font-size: 17px;
    color:#d3e6fe;
  }

  .el-menu{
    background-color: #CFE9FF;
  }

  /*.router-link-active{
     background: url("../../../static/images/second_select.png") no-repeat;
     color: #FFFFFF;
   }*/
</style>
