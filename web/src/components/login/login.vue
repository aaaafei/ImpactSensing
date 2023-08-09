<template>
  <div>
    <div class="login">
      <div class="login_box">
        <div style="margin-bottom:50px;margin-top:100px;display:none;">
            <img src="../../../static/images/gljtlogo.png" width="90%" height="105px">
          </div>
        <div style="height:200px;"> </div>
        <div class="login_form_box">
          <div style="flex: 1">
            <!-- <h1 style="color:#f2f2f2;width:100%;height:70px;font-size:40px;"><i class="el-icon-setting"></i>后台管理系统</h1> -->
          </div>
          <div class="login-form" style="flex: 1.5">
            <div class="header-div"  style="background:#1f1c4e;width:100%;height:80px;">
              <div style="width:100%;height:80px;padding:20px 0 0 20px">
                <!-- <img src="../../../static/images/login.png" width="40px" height="40px" style="float:left;"> -->
                <h1 style="color:#f2f2f2;width:100%;height:100px;font-size:18px;margin:1px 0 0 1px;">南京地铁桥梁防撞感知系统</h1>
              </div>
            </div>
            <div class="form-div">
              <el-form status-icon :model="loginForm" ref="loginForm"  :rules="rules">
                <div class="login_form_ele">
                  <el-form-item prop="userName">
                    <el-input type="text" prefix-icon="el-icon-name" v-model="loginForm.userName" id="userName"></el-input>
                  </el-form-item>
                </div>
                <div  class="login_form_ele">
                  <el-form-item prop="password">
                    <el-input type="password" prefix-icon="el-icon-password" v-model="loginForm.password" id="password" @keyup.enter.native="login"></el-input>
                  </el-form-item>
                </div>
              </el-form>
              <div style=" position: relative;padding: 0 10px" @click="login">
                <el-button type="primary" :loading="logining">登&nbsp;&nbsp;&nbsp;&nbsp;录</el-button>
                <div  class="login_button_adorn"> </div>
              </div>
            </div>
          </div>
           <div style="flex: 1"> 
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapGetters, mapActions } from 'vuex';
  export default {
    computed: mapGetters({
      token: 'getToken'
    }),
    name: 'login',
    data: function () {
      return {
        logining: false,
        loginForm: {
          userName: '',
          password: ''
        },
        rules: {
          userName: [
            {required: true, message: '请输入用户名', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'}
          ]
        }
      };
    },
    methods: {
      ...mapActions({
        setToken: 'setToken'
      }),
      login () {
        let self = this;
        this.$refs['loginForm'].validate((valid) => {
          if (valid) {
            this.logining = true;
            let url = this.$api_prefix + '/user/auth/login';
            let params = {
              'username': this.loginForm.userName,
              'password': this.loginForm.password
            };
            this.$http.post(url, params).then((res) => {
              this.logining = false;
              let success = res.body.result_code;
              if (success === 0) {
                this.$store.dispatch('setToken', 'JSTI ' + res.body.result_data.token);
                this.$store.dispatch('setUserInfo', res.body.result_data);
                self.$router.push('/index');
              } else {
                this.$message.error('用户名或密码错误！');
              }
            }, (res) => {
              this.$message.error('登录失败，请重试！');
            });
            // self.$router.push('/index');
          }
        });
      }
    },
    mounted: function () {
      this.$nextTick(function () {
        sessionStorage.clear();
        document.getElementById('userName').focus();
      });
    }
  };
</script>

<style type="stylus">
  .login{
    width:100vw;
    height: 100vh;
    margin: 0;
    padding: 0;
    background: url("../../../static/images/login_back.jpg") no-repeat;
    background-size: 100% 100%;
  }
  .header-div{
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
  }
  .login-form{
    /* background: url("/static/images/dlk.png") no-repeat; */
    background-color:#f2f2f2;
    background-size: 100% 100%;
    border-radius: 8px;
  }
  .form-div{
    padding: 50px 30px;
  }
  .login_box{
    width:50%;
    margin:0 auto;
    padding-top: 2%;
  }
  .login_form_box{
    display: flex;
    align-items: center;
  }
  .login_form_box form{
    color: #fff;
  }
  .login_form_ele{
    width: 100%;
    margin-bottom: 20px;
    padding: 0 10px;
    box-sizing:border-box;
  }
  .login_form_ele input{
    display: inline-block;
    width: 100%;
    border: 1px #7491d3 solid;
    outline: none;
    line-height: 40px;
    height: 40px;
    margin-bottom: 1px;
    border-radius: 4px;
    /* background: #263c76; */
    /* color: #ffffff; */
  }
  .login .el-input__inner{
    padding-left: 40px !important;
  }
  .login_form_ele .el-input__prefix .el-input__icon{
    width: 28px;
    height: 50%;
    margin-top: 10px;
    margin-left: 5px;
    border-right: 1px #7491d3 solid;
  }
  .el-icon-name{
    background: url('../../../static/images/username.png') no-repeat;
  }
  .el-icon-password{
    background: url('../../../static/images/password.png') no-repeat;
  }
  .form-div .el-button--primary{
    width:100%;
    height: 40px;
    border: none;
    background: #3aaffe;
    color: #ffffff;
    border-radius: 5px;
    font-size: 18px;
  }
</style>
