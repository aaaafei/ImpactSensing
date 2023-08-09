<template>
  <el-form :model="form"  label-width="130px">
    <el-form-item label="名称："  prop="name" >
      <el-input  v-model="form.name"/>
    </el-form-item>
    <el-form-item label="URL：" prop="url" >
      <el-input v-model="form.url" />
    </el-form-item>
    <el-form-item label="驱动类：" prop="driverClass">
      <el-input v-model="form.driverClass"/>
    </el-form-item>
    <el-form-item label="账号：" prop="username">
      <el-input v-model="form.username"/>
    </el-form-item>
    <el-form-item label="密码：" prop="password">
      <el-input v-model="form.password"/>
    </el-form-item>
    <!-- <el-form-item label="是否有效：" prop="valid">
      <el-switch v-model="form.valid"/>
    </el-form-item> -->
    <el-form-item>
      <el-button type="success" @click="testConn" v-if="saveBtnVisible">测试连接</el-button>
      <el-button type="primary" @click="onSubmit" v-if="saveBtnVisible">保存</el-button>
      <el-button type="text" @click="close">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import request from '@/assets/utils/request';
  export default {
    name: 'dbDetailBox',
    props: ['saveBtnVisible', 'detailForm', 'operation'],
    data: function () {
      return {
        form: this.detailForm
        // operation: this.operation
      };
    },
    activated: function () {
      this.form = this.detailForm;
    },
    methods: {
      close () {
        this.$emit('close');
      },
      onSubmit () {
        if (this.operation === 'add') {
          let url = '/code/db/add?';
          let params = this.urlAddParam();
          url = this.urlAddParam(url);
          // url += '&url=' + this.form.url;
          request({
            url: url,
            params: params
          }).then(res => {
            this.close();
            this.$emit('refreshDb');
          });
        } else {
          let url = '/code/db/update?id=' + this.form.id;
          let params = this.urlAddParam();
          // url += '&valid=true';
          // url += '&url=' + this.form.url;
          request({
            url: url,
            params: params
          }).then(res => {
            this.close();
            this.$message({
              message: '修改成功！',
              type: 'success'
            });
            this.$emit('refreshDb');
          });
        }
      },
      testConn () {
        let url = '/code/db/test/' + this.form.id;
        request({
          url: url
        }).then(res => {
          this.$message({
            message: '连接成功！',
            type: 'success'
          });
        });
      },
      urlAddParam () {
        // url += '&name=' + this.form.name;
        // url += '&url=' + encodeURI(this.form.url);
        // url += '&driverClass=' + this.form.driverClass;
        // url += '&username=' + this.form.username;
        // url += '&password=' + this.form.password;
        let params = {
          name: this.form.name,
          url: this.form.url,
          driverClass: this.form.driverClass,
          username: this.form.username,
          password: this.form.password,
          valid: true
        };
        return params;
      }
    },
    mounted: function () {
    }
  };
</script>

<style type="stylus">

</style>
