<template>
  <div id="addEditRole">
    <el-dialog :title="title" :visible="addEditVisible" @close="close('')">
      <el-form ref="addEditRoleform" :status-icon="isState" :model="form" :rules="rules"  label-width="100px" >
        <el-form-item label="角色名称：" prop="des" for="roleName">
          <el-input
            id="roleName"
            v-model="form.des"
            placeholder="请输入角色名称"/>
        </el-form-item>
        <el-form-item label="角色编码："  prop="name" for="roleCode">
          <el-input
            id="roleCode"
            :disabled="isDisabled"
            v-model="form.name"
            placeholder="请输入角色编码"/>
        </el-form-item>
        <el-form-item label="排序：" prop="sortno" for="sortno" ref="sortNo">
          <el-input
            id="sortno"
            v-model="form.sortno"
            placeholder=""/>
        </el-form-item>
        <el-form-item label="备注：" prop="remark" for="roleDes">
          <el-input
            id="roleDes"
            v-model="form.remark"
            placeholder=""/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close('')">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="add('addEditRoleform')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';
  export default {
    name: 'addEditRoleBox',
    props: ['addEditVisible', 'propForm', 'title', 'isDisabled', 'isState'],
    data: function () {
      var checkCode = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入编码'));
        }
        if (this.title === '编辑角色') {
          this.codeValid = false;
          callback();
        } else {
          // setTimeout(() => {
          //   this.$request({
          //     url: '/role/check/' + (this.form.code).trim()
          //   }).then(res => {
          //     // 响应成功回调
          //     let success = res.data.data;
          //     if (success === true) {
          //       this.codeValid = true;
          //       callback(new Error('角色编码已存在'));
          //     } else {
          //       this.codeValid = false;
          //       callback();
          //     }
          //   });
          // }, 10);
          this.codeValid = false;
          callback();
        }
      };
      var checkSortno = (rule, value, callback) => {
        if (!value) {
          return callback();
        }
        if (value.length > 8) {
          callback(new Error('最大为8位数'));
        } else {
          let value1 = '' + parseInt(value);
          value = '' + value;
          // console.log('你好');
          // console.log(value1.length);
          // console.log(value.length);
          // console.log(value1 === value);
          if (value1 === value) {
            callback();
          } else {
            callback(new Error('请输入数字值'));
          }
        }
        // if(value.matches('/^[d]{0,101}$/')&&s1.length()<=10){
        //   System.out.println("Yes");
        // }
        // var r = /^\+?[1-9][0-9]*$/;
        // var r = /^\d{11,}$/;
        // console.log('你好');
        // console.log(r.test(value));
        // if (!Number.isInteger(value)) {
        //   callback(new Error('请输入数字值'));
        // } else {
        // }
      };
      return {
        theVisible: this.addEditVisible, // 页面可见性
        form: this.propForm,
        theOperated: false,
        finished: false,
        codeValid: false,
        rules: {
          name: [
            { required: true, message: '请输入角色名称', trigger: 'blur' }
          ],
          code: [
            { required: true, validator: checkCode, trigger: 'blur' }
          ],
          sortno: [
            { validator: checkSortno, trigger: 'blur' }
          ],
          remark: [{max: 100, message: '最多可以输入100个字！', trigger: 'blur'}]
        }
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    methods: {
      close (formName) {
        if (formName !== '') {
          this.$refs[formName].resetFields(); // 清空验证信息
          this.$emit('headCallBack', formName);
        } else {
          this.$refs['addEditRoleform'].resetFields(); // 清空验证信息
          this.$emit('headCallBack', '');
        }
      },
      validCode () {
        this.$request({
          url: '/role/check/' + (this.form.code).trim()
        }).then(res => {
          // 响应成功回调
          let success = res.data.data;
          if (success === true) {
            this.codeValid = true;
            this.$message(new Error('角色编码已存在'));
          } else {
            this.codeValid = false;
          }
        });
      },
      add (formName) {
        // setTimeout(() => {
        //   this.saveRole(formName);
        // }, 1000);
        var Self = this;
        this.$refs[formName].validate(function (valid) {
          // if(Self.form.sortno === '') {
          //   Self.$refs['sortNo'].clearValidate() // 清除排序号的验证
          // }
          if (valid) {
            // console.log('验证通过')
            Self.saveRole();
            // setTimeout(() => {
            //   this.saveRole();
            // }, 1000);
          } else {
            return false;
          }
        });
      },
      saveRole () {
        if (this.codeValid) {
          return false;
        }
        this.finished = true; // 按钮加载状态开始
        let options = {
          headers: {
            Authorization: this.$store.getters.getToken
          }
        };
        // var Self = this;
        options.params = this.form;
        let url = '';
        if (this.title === '新增角色') {
          url = '/role/addRole';
        } else if (this.title === '编辑角色') {
          url = '/role/updateRole/' + this.form.id;
        }
        this.$request({
          url: url,
          method: 'post',
          data: options.params
        }).then(res => {
          this.finished = false; // 按钮加载状态结束
          this.$message({
            type: 'success',
            message: res.data.result_desc
          });
          this.theOperated = true;
          this.codeValid = false;
          this.close('addEditRoleform'); // 操作结束后 关闭窗口
        });
      }
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
      });
    }
  };
</script>

<style type="stylus">

</style>
