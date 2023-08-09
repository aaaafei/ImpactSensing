<template>
<div style="border-bottom: 2px solid #dcdfe6;">

  <el-row style="height:80px; background:url('../../../../static/images/h_head.png') center no-repeat;background-color:#f0f6fd;">
    <el-col :span="4">&nbsp;</el-col>
    <el-col :span="13">
      <img src="../../../../static/images/project/logo_banner.png" style="vertical-align: middle;height:80px;"/>
    </el-col>
    <el-col :span="3" style="padding-top:25px;text-align:right;">
      <span style="color:#757575;font-size:16px;">欢迎您：{{user.truename}}</span>
      <img src="../../../../static/images/login.png" title="修改密码" @click="showPassForm"  style="vertical-align: top;height:20px;cursor:pointer;">  
      <img src="../../../../static/images/logout.png" title="退出登录" @click="loginOut"  style="vertical-align: top;height:20px;cursor:pointer;"> 
    </el-col>
    <el-col :span="4">&nbsp;</el-col>
  </el-row>
  
  <el-row style="background-color: #fff;">
    <el-col :span="4" style="height:60px;line-height:40px;background-color:#005BB4;">
    </el-col>
    <el-col :span="20">
      <el-menu
        :default-active="this.$route.path"
        router
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#005BB4"
        text-color="#fff"
        active-text-color="#ffd04b">
        <el-menu-item index="/home" route="home">首页</el-menu-item>
        <el-menu-item index="2">监测数据</el-menu-item>
        <el-submenu index="3">
          <template slot="title">告警中心</template>
          <el-menu-item index="3-1">设备告警</el-menu-item>
          <el-menu-item index="3-2">已发送通知</el-menu-item>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">设备信息</template>
          <el-menu-item index="4-1">终端装置</el-menu-item>
          <el-menu-item index="4-2">检查及更换记录</el-menu-item>
        </el-submenu>
        <el-menu-item index="5">统计分析</el-menu-item>
        <el-menu-item index="6">智慧工务</el-menu-item>
        <el-menu-item index="/sysmanage" route="sysmanage">系统设置</el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
 
  <el-dialog title="修改密码" :visible.sync="passFormVisible" @close="closePassDialog">
		<el-form :model="passForm" status-icon :rules="passRules" ref="passForm">
			<el-form-item label="原密码" :label-width="passWidth" prop="oldPass">
			<el-input type="password" v-model="passForm.oldPass" placeholder="请输入原密码"></el-input>
			</el-form-item>
			<el-form-item label="新密码" :label-width="passWidth" prop="newPass">
			<el-input type="password" v-model="passForm.newPass" placeholder="请输入新密码"></el-input>
			</el-form-item>
			<el-form-item label="确认密码" :label-width="passWidth" prop="cofPass">
			<el-input type="password" v-model="cofPass" placeholder="请输入新密码"></el-input>
			</el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button @click="closePassDialog">取 消</el-button>
			<el-button type="primary" @click="changePass">确 定</el-button>
		</div>
  </el-dialog>

</div>
</template>

<script>
  /* eslint-disable */
  import request from '@/assets/utils/request'; 
  import NavMenu from "@/components/index/header/NavMenu.vue";

  export default {
    name: 'indexHeader',
    data: function () {
      let pass = (rule, value, callback) => {
        if (this.passForm.newPass === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.passForm.cofPass !== '') {
            this.$refs.passForm.validateField('cofPass');
          }
          callback();
        }
      };
      let validatePass = (rule, value, callback) => {
        if (this.cofPass === '') {
          callback(new Error('请再次输入密码'));
        } else if (this.cofPass !== this.passForm.newPass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        activeIndex: this.$route.path.substring(1),
        hasSide: false,
        userMenus: [],
        activeName: 'polling-result',
        passWidth: '120px',
        agencyFlag: false,
        passFormVisible: false,
        persionVisible: false,
        agencyVisible: 'agencyhide',
        passForm: {
          oldPass: '',
          newPass: ''
        },
        cofPass: '',
        user: {},
        workNum: 0,
        setting: {
          theme: 'blue-theme',
          layout: 'side'
        },
        slide: true,
        passRules: {
          oldPass: [
            { required: true, message: '请输入原密码', trigger: 'blur' }
          ],
          newPass: [
            {validator: pass, trigger: 'blur'}
          ],
          cofPass: [
            {validator: validatePass, trigger: 'blur'}
          ]
        },
        menuWidth: 0, 
        oneClickScrollLength: 200
      };
    },
    methods: {
      getUser: function () {
        this.user = this.$store.getters.getUserInfo;
      },
      loginOut () {
        this.$confirm('确认退出系统吗？','提示',{
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            this.$store.dispatch('delToken');
            this.$router.push('/login'); 
        }).catch(()=>{
            this.$message({type: 'info',message: '已取消退出'});
        }); 
      },
      showPassForm () {
        this.passFormVisible = true;
      },
      getUserInfo () {
        this.persionVisible = true;
      },
      changePass () {
        this.$refs['passForm'].validate((valid) => {
          if (valid) {
            let options = {
              // headers: {
              //   Authorization: this.$store.getters.getToken
              // },
              params: {
                oldpwd: this.passForm.oldPass,
                newpwd: this.passForm.newPass
              }
            };

            if(this.passForm.oldPass == this.passForm.newPass){
                this.$message({
                    message: "新旧密码相同，请重设新密码！",
                    type: 'warning'
                });
                return;
            }

            request({
              url: '/user/password',
              params: options.params
            }).then(res => {
                if(res.data.result_code == "0"){
                    this.$message({
                        message: "密码修改成功，请重新登录！",
                        type: 'success'
                    });
                    // this.passFormVisible = false;
                    // this.$refs['passForm'].resetFields();
                    // this.loginOut();
                    this.$store.dispatch('delToken');
                    this.$router.push('/login'); 
                }
                else{
                    this.$message({
                        message: res.data.result_desc,
                        type: 'error'
                    });
                } 
            });
          }
        });
      },
      closePassDialog () {
        this.passFormVisible = false;
        this.cofPass = '';
        this.$refs['passForm'].resetFields();
      },
      // 面包屑切换
      handleClick (tab, event) {
        // $('div#mainCrumb').children(':last').children('span').text(tab.label);
      },
      handleSelect(key, keyPath) {
        console.log(key,keyPath);
        this.activeIndex = key;
      },
      getMenus: function () {
        let self = this;
        this.$request({
          url: '/menu/getMenuByUser'
        }).then(res => {
          var userMenus = res.data.result_data; 
          self.userMenus = userMenus;
          self.$store.commit('setMenus',self.userMenus);
          self.$emit('setHeadHeight', userMenus.length);
 
        });
       
      },
    },
    mounted: function () {
      var self = this;
        this.$nextTick(function () {
            self.getUser();
            // self.getMenus();
        });  
       
    },
    components: {
      NavMenu: NavMenu
    },
    watch: {
      $route:function (to, from) { 
        this.activeIndex = to.path.substring(1); // 点击回退时让菜单选项随之改变 
      }
    }
  };
</script>

<style scoped>
.el-menu--horizontal>.el-menu-item.is-active {
  font-size: 20px;
}
.el-menu--horizontal>.el-menu-item {
  font-size: 20px;
}
.el-submenu /deep/ .el-submenu__title{
  font-size: 20px;
}
.el-menu--horizontal .el-menu .el-menu-item {
  font-size: 16px;
}
a {
  text-decoration: none
}
</style>
