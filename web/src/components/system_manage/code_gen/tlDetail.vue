<template>
  <el-form :model="form"  label-width="130px">
    <el-form-item label="名称："  prop="name" >
      <el-input  v-model="form.name"/>
    </el-form-item>
    <el-form-item label="数据库：" prop="db" >
      <el-select v-model="form.db" @change="changeDbValue" placeholder="请选择">
        <el-option
          v-for="item in datasources"
          :key="item.name"
          :label="item.name"
          :value="item.id">
          <span style="float: left">{{ item.name }}</span>
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="项目名称：" prop="projectName">
      <el-input v-model="form.pjName"/>
    </el-form-item>
    <el-form-item label="表前缀：" prop="tablePrefix">
      <el-input v-model="form.tprefix"/>
    </el-form-item>
    <el-form-item label="包路径：" prop="pkg">
      <el-input v-model="form.pkg"/>
    </el-form-item>
    <el-form-item label="版权注释：" prop="copyright">
      <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 8}" v-model="form.copyright"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit" v-if="detailVisible">保存</el-button>
      <el-button @click="close">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import request from '@/assets/utils/request';
  export default {
    name: 'dbDetailBox',
    props: ['detailVisible', 'detailForm', 'operation', 'datasources'],
    data: function () {
      return {
        form: this.detailForm,
        dbName: ''
      };
    },
    methods: {
      changeDbValue (val) {
        let obj = {};
        obj = this.datasources.find((item) => {
          return item.id === val;
        });
        this.dbName = obj.name;
      },
      close () {
        this.$emit('close');
      },
      onSubmit () {
        if (this.operation === 'add') {
          let url = '/code/template/add?';
          url = this.urlAddParam(url);
          request({
            url: url,
            method: 'get',
            param: null
          }).then(res => {
            this.close();
            this.$message({
              message: '添加成功！',
              type: 'success'
            });
            this.$emit('refresh');
          });
        } else {
          let url = '/code/template/update?id=' + this.form.id;
          url = this.urlAddParam(url);
          url += '&valid=true';
          request({
            url: url,
            method: 'get',
            param: null
          }).then(res => {
            this.close();
            this.$message({
              message: '修改成功！',
              type: 'success'
            });
            this.$emit('refresh');
          });
        }
      },
      urlAddParam (url) {
        url += '&name=' + this.form.name;
        url += '&db=' + this.form.db;
        url += '&dbName=' + this.dbName;
        url += '&pkg=' + this.form.pkg;
        url += '&pjName=' + this.form.pjName;
        url += '&tPrefix=' + this.form.tprefix;
        url += '&copyright=' + this.form.copyright;
        return url;
      }
    }
  };
</script>

<style type="stylus">

</style>
