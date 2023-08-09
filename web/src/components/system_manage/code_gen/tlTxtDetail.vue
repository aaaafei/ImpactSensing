<template>
  <el-form :model="form"  label-width="130px">
    <el-form-item label="名称："  prop="name" >
      <el-input  v-model="form.name"/>
    </el-form-item>
    <!-- <el-form-item label="数据库：" prop="db" >
      <el-select v-model="form.db" @change="changeDbValue" placeholder="请选择">
        <el-option
          v-for="item in datasources"
          :key="item.name"
          :label="item.name"
          :value="item.id">
          <span style="float: left">{{ item.name }}</span>
        </el-option>
      </el-select>
    </el-form-item> -->
    <el-form-item label="包路径：" prop="pkg">
      <el-input v-model="form.pkg"/>
    </el-form-item>
    <el-form-item label="内容：" prop="content">
      <el-input type="textarea" :autosize="{ minRows: 8, maxRows: 12}" v-model="form.content"/>
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
          let url = '/code/templateContent/add';
          // url = this.urlAddParam(url);
          let param = this.urlAddParam(url);
          request({
            url: url,
            method: 'get',
            params: param
          }).then(res => {
            this.close();
            this.$emit('refresh');
          });
        } else {
          let url = '/code/templateContent/update?id=' + this.form.id;
          let param = this.urlAddParam(url);
          param.valid = true;
          // url += '&valid=true';
          request({
            url: url,
            method: 'get',
            params: param
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
        let param = {};
        param.name = this.form.name;
        param.templateId = this.form.templateId;
        param.pkg = this.form.pkg;
        param.content = this.form.content;
        // url += '&name=' + this.form.name;
        // url += '&templateId=' + this.form.templateId;
        // url += '&pkg=' + this.form.pkg;
        // url += '&content=' + this.form.content;
        return param;
      }
    }
  };
</script>

<style type="stylus">

</style>
