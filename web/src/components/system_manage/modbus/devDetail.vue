<template>
  <el-form :model="form"  label-width="130px">
    <el-form-item label="设备id："  prop="slaveId" >
      <el-input  v-model="form.slaveId"/>
    </el-form-item>
    <el-form-item label="设备编码：" prop="deviceCode" >
      <el-input v-model="form.deviceCode" />
    </el-form-item>
    <el-form-item label="连接：" prop="connectId">
      <!-- <el-input v-model="form.connectId"/> -->
      <el-select v-model="form.connectName" @change="changeConnValue" placeholder="请选择">
        <el-option
          v-for="item in connections"
          :key="item.id"
          :label="item.ip"
          :value="item.id">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="读功能码：" prop="functionRead">
      <el-input v-model="form.functionRead"/>
    </el-form-item>
    <el-form-item label="写功能码：" prop="functionWrite">
      <el-input v-model="form.functionWrite"/>
    </el-form-item>
    <el-form-item label="起始地址：" prop="address">
      <el-input v-model="form.address"/>
    </el-form-item>
    <el-form-item label="读取位置：" prop="offset">
      <el-input v-model="form.offset"/>
    </el-form-item>
    <el-form-item label="数值格式：" prop="dataType">
      <el-input v-model="form.dataType"/>
    </el-form-item>
    <el-form-item label="数据类型：" prop="deviceDataType">
      <el-select v-model="form.deviceDataTypeName" @change="changeDatatypeValue" placeholder="请选择">
        <el-option
          v-for="item in dataTypes"
          :key="item.code"
          :label="item.text"
          :value="item.code">
        </el-option>
      </el-select>
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
        connections: null,
        dataTypes: null,
        api: {
          URL_add: '/modbus/modbusDefinition/add',
          URL_update: '/modbus/modbusDefinition/update',
          URL_connections: '/modbus/modbusConnect/list',
          URL_dict: '/dict/subCodes/'
        },
        formProperties: [
          'connectId', 'slaveId', 'functionRead', 'functionWrite', 'address', 'offset', 'dataType', 'deviceCode', 'deviceDataType'
        ]
        // operation: this.operation
      };
    },
    watch: {
      detailForm: {
        handler (newValue, oldValue) {
          console.log(newValue);
          // this.form = newValue;
          this.getInitData();
        },
        deep: true
      }
    },
    activated: function () {
      this.form = this.detailForm;
      console.log(this.form);
    },
    methods: {
      changeConnValue (val) {
        // let obj = {};
        // obj = this.connections.find((item) => {
        //   return item.id === val;
        // });
        this.form.connectId = val;
      },
      changeDatatypeValue (val) {
        this.form.dataType = val;
      },
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
      getInitData () {
        this.getConns();
        this.getDataType();
      },
      getDataType () {
        let url = this.api.URL_dict + 'xy_dev_data_type';
        this.form.connectName = '';
        const _this = this;
        request({
          url: url
        }).then(res => {
          // let connections = res.data.data;
          _this.dataTypes = res.data.data;
        });
      },
      getConns () {
        let url = this.api.URL_connections;
        this.form.connectName = '';
        const _this = this;
        request({
          url: url
        }).then(res => {
          let connections = res.data.data;
          for (let i in connections) {
            if (connections[i].id === _this.form.connectId) {
              _this.form.connectName = connections[i].ip;
            }
          };
          _this.connections = connections;
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
      this.getInitData();
    }
  };
</script>

<style type="stylus">

</style>
