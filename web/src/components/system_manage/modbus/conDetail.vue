<template>
  <el-form :model="form"  label-width="130px">
    <el-form-item label="IP："  prop="ip" >
      <el-input  v-model="form.ip"/>
    </el-form-item>
    <el-form-item label="PORT：" prop="port" >
      <el-input v-model="form.port" />
    </el-form-item>
    <el-form-item label="间隔：" prop="interval">
      <el-input v-model="form.interval"/>
    </el-form-item>
    <el-form-item label="备注：" prop="remark">
      <el-input v-model="form.remark"/>
    </el-form-item>
    <!-- <el-form-item label="是否有效：" prop="valid">
      <el-switch v-model="form.valid"/>
    </el-form-item> -->
    <el-form-item>
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
    // {
    //   saveBtnVisible: Boolean,
    //   detailForm: {
    //     type: Object,
    //     default: function () {
    //       return {};
    //     }
    //   },
    //   operation: String
    // },
    data: function () {
      return {
        form: this.detailForm,
        api: {
          URL_add: '/modbus/modbusConnect/add',
          URL_update: '/modbus/modbusConnect/update',
          URL_test: '/modbus/code/db/test/'
        },
        formProperties: [
          'ip', 'port', 'interval', 'remark'
        ]
        // operation: this.operation
      };
    },
    watch: {
      form: {
        handler (newValue, oldValue) {
          console.log(newValue);
          console.log(oldValue);
          // this.form = newValue;
        },
        deep: true
      }
    },
    activated: function () {
      this.form = this.detailForm;
      console.log(this.form);
    },
    methods: {
      close () {
        this.$emit('close');
      },
      onSubmit () {
        if (this.operation === 'add') {
          let url = this.api.URL_add;
          let params = this.urlAddParam();
          // url = this.urlAddParam(url);
          // url += '&url=' + this.form.url;
          request({
            url: url,
            params: params
          }).then(res => {
            this.close();
            this.$emit('refreshDb');
          });
        } else {
          let url = this.api.URL_update + '?id=' + this.form.id;
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
        let url = this.api.URL_test + this.form.id;
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
        let params = {};
        for (var i = 0; i < this.formProperties.length; i++) {
          var prop = this.formProperties[i];
          params[prop] = this.form[prop];
        }
        params.valid = true;
        // let params = {
        //   ip: this.form.ip,
        //   port: this.form.port,
        //   interval: this.form.interval,
        //   remark: this.form.remark,
        //   valid: true
        // };
        return params;
      }
    },
    mounted: function () {
    }
  };
</script>

<style type="stylus">

</style>
