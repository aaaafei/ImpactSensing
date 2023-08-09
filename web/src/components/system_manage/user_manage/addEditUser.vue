<template>
  <div id="addEditUser">
    <el-dialog :title="title" :visible="addEditVisible" @open="show" @close="close('')">
      
      <el-form ref="addEditUserform" :status-icon="isState" :model="form" :rules="rules" label-width="100px" >
        <el-form-item label="姓名：" prop="truename">
          <el-input v-model="form.truename" placeholder="请输入用户名称"/>
        </el-form-item>
        <el-form-item label="帐号：" prop="username">
          <el-input :disabled="isDisabled" v-model="form.username" placeholder="请输入登录帐号"/>
        </el-form-item>
          <el-form-item label="所在组织：" prop="organname">
          <el-input readonly="readonly" :disabled="isDisabled" v-model="form.organname" placeholder="请选择所在组织" @focus="innerVisible = isShowOrg"/>
        </el-form-item>
        <el-form-item label="角色：" prop="role" for="userWeChat">
          <el-select v-model="roles" multiple placeholder="请选择角色">
            <el-option v-for="item in roleList" :key="item.id" :label="item.des" :value="item.id">
            </el-option>
          </el-select>
            (注：人员角色可设置多个，请第一个添加主角色)
        </el-form-item>
        <!-- <el-form-item label="微信号：" prop="wechatid" for="userWeChat">
          <el-input id="userWeChat" v-model="form.wechatid" placeholder="请输入微信号"/>
        </el-form-item> -->
        <el-form-item label="联系电话：" prop="phone"  ref="userPhone">
          <el-input   v-model="form.phone" placeholder="请输入联系电话"/>
        </el-form-item>
        <!-- <el-form-item prop="email" label="电子邮箱：" ref="userEmail">
          <el-input  v-model="form.email" placeholder="请输入邮箱地址"></el-input>
        </el-form-item> -->
      </el-form>
      <div sl1ot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close('')">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="add('addEditUserform')">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog  width="30%" title="选择组织" :visible.sync="innerVisible" append-to-body>
        <organization v-on:headCallBack1="callBack1"></organization>
      </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  import organ from '../organization';
  import request from '@/assets/utils/request';
  export default {
    name: 'addEditUserbox',
    props: ['addEditVisible', 'propForm', 'roleids', 'title', 'isDisabled', 'isShowOrg', 'isState'],
    data: function () {
      var checkCode = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入用户帐号'));
        }
        if (this.title === '编辑用户') {
          this.codeValid = false;
          callback();
        } else {
            this.codeValid = false;
          callback();
          // setTimeout(() => {
          //   request({
          //     url: '/user/check/' + (this.form.loginName).trim()
          //   }).then(res => {
          //     let success = res.data.data;
          //     if (success === true) {
          //       this.codeValid = true;
          //       callback(new Error('该帐号已存在'));
          //     } else {
          // this.codeValid = false;
          // callback();
          //     }
          //   });
          // }, 10);
        }
      };
      return {
        roleList: [],
        roles: this.roleids, // this.propForm.roleids,
        innerVisible: false,
        theOperated: false,
        finished: false,
        form: this.propForm,
        codeValid: false,
        orgData: { // 接受从组织弹窗选中的组织数据
          organid: '',
          organname: ''
        },
        rules: {
          truename: [
            { required: true, message: '请输入用户名称', trigger: 'blur' }
          ],
          username: [
          // { required: true, message: '请输入用户帐号', trigger: 'blur' }
            { required: true, validator: checkCode, trigger: 'blur' }
          ],
          organname: [
            { required: true, message: '请选择一个组织', trigger: 'blur,change' }
          ], 
          phone: [
            { max: 11, message: '请输入正确的电话号码格式', trigger: 'blur' }
          ]
        }
      };
    },
    watch: {

    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      organization: organ// 获取组织的组件
    },
    methods: {
      show () {
        this.getRoles();
        this.roles = this.roleids;
      },
      getRoles () {
        this.$request({
          url: '/role/get/all'
        }).then(res => {
          // this.loading = false; // 查询结束后，停止加载
          this.roleList = res.data.result_data;
          // this.page.total = res.data.result_data.total;
          // this.tableData3 = res.data.result_data.list;
        });
      },
      close (formName) {
        if (formName !== '') {
          this.$refs[formName].resetFields(); // 清空验证信息
          this.$emit('headCallBack', formName);
        } else {
          this.$refs['addEditUserform'].resetFields(); // 清空验证信息
          this.$emit('headCallBack', '');
        }
      },
      validCode (callback) {
        request({
          url: '/user/check/' + (this.form.loginName).trim()
        }).then(res => {
          let success = res.data.data;
          if (success === true) {
            this.codeValid = true;
            callback(new Error('该帐号已存在'));
          } else {
            this.codeValid = false;
            callback();
          }
        });
        return this.codeValid;
      },
      add (formName) {
        var Self = this;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            Self.saveUser();
          } else {
            return false;
          }
        });
      },
      saveUser () {
        if (this.codeValid) {
          return false;
        }
        this.finished = true;
        let options = {
          params: {}
        };
        options.params = this.form;
        if (this.orgData.organid !== '') {
          options.params.organid = this.orgData.organid;
        }
        options.params.roleids = this.roles;
        options.params.roles = null;
        let url = '';
        if (this.title === '新增用户') {
          url = '/user/addUser';
        } else if (this.title === '编辑用户') {
          url = '/user/updateUser/' + this.form.id;
        }
        request({
          url: url,
          method: 'post',
          data: options.params
        }).then(res => {
          this.finished = false;
          this.theOperated = true;
          this.$message({
            type: 'success',
            message: res.data.result_desc
          });
          this.orgData.organid = '';
          this.orgData.organname = '';
          this.codeValid = false;
          this.close('addEditUserform'); // 操作结束后 关闭窗口
        });
      },
      callBack1 (msg) { // 选选择组织的回调函数
        this.orgData.organid = msg.id;
        this.orgData.organname = msg.label;
        if (msg.isClose) {
          this.close1();
        }
        this.form.organname = msg.label;
        this.form.organid = msg.id;
      },
      close1 () {
        this.innerVisible = false;
      }
    },
    mounted: function () {
      var _this = this;
      this.$nextTick(function () {
        // _this.getRoles();
        // _this.roles = _this.propForm.roleids;
        // console.log(_this.roles);
        // let token = this.$store.getters.getToken;
        // if (token === '') {
        //   this.$store.dispatch('refresh');
        // }
      });
    }
  };
</script>

<style type="stylus">

</style>
