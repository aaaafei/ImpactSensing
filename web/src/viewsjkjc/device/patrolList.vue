<template>
  <div class="app-container">
    <div style="padding-bottom: 10px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>设备信息</el-breadcrumb-item>
        <el-breadcrumb-item>检修记录</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-row type="flex" align="middle" style="height: 80px;background-color: #CECECE;">
      <el-col :span="17">
        <div style="padding-left: 30px;;">
          
        </div>
      </el-col>
      <el-col :span="5">
        <el-input placeholder="设备编码、设备名称、线路、桩号、桥墩号" prefix-icon="el-icon-search" v-model="search">
        </el-input>
      </el-col>
      <el-col :span="2" style="text-align: center;">
        <el-button type="primary">查询</el-button>
      </el-col>

    </el-row>
    <div style="height:10px;"></div>

    <el-table :data="deviceList" :height="600" border stripe 
      :header-cell-style="{ background: '#A1B6D8', color: '#fff' }">
      <el-table-column prop="c" label="序号" width="50" align="center"></el-table-column>
      <el-table-column prop="device_code" label="设备编码" align="center"></el-table-column>
      <el-table-column prop="device_name" label="设备名称" align="center"></el-table-column>
      <el-table-column prop="line" label="线路" align="center"></el-table-column>
      <el-table-column prop="direction" label="上下行" align="center"></el-table-column>
      <el-table-column prop="bridge_serial_no" label="桩号、桥墩号" align="center"></el-table-column>
      <el-table-column prop="content" label="现场安装图片" width="280" align="center">
        <template slot-scope='scope'>
          <img :src="item" alt="" v-for="(item,index) in images" :key="index" style="height: 40px;margin-left: 2px;" @click="previewImage(item)">
        </template>
      </el-table-column>
      <el-table-column prop="_tx" label="检修记录" width="120" align="center">
        <template slot-scope='scope'>
          <span style="color:#1684FC;cursor: pointer;" @click="openDisposeRecordDialog">10次</span>
        </template>
      </el-table-column>
      <!-- <el-table-column prop="_sz" label="更换记录" width="120" align="center">
        <template slot-scope='scope'>
          <span style="color:#1684FC">无</span>
        </template>
      </el-table-column> -->
      <!-- <el-table-column prop="_oper" label="操作" width="120" align="center">
        <template slot-scope='scope'>
          <el-button size="mini" plain type="primary">设备详情</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    <el-pagination :current-page="page.pageNum" :page-sizes="[10, 20, 50]" :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="float: left;"></el-pagination>

    <dispose-record-table :disposeRecordDialog = "disposeRecordDialog" @changeDisposeRecordDialog="changeDisposeRecordDialog"></dispose-record-table>

    <el-dialog title="" :visible.sync="imgDialogVisible" width="50%">
      <div style="height:600px;text-align: center;">
        <img :src="previewImg" alt="" style="height: 100%;">
      </div>
    </el-dialog>

  </div>
</template>

<script>
/* eslint-disable */
import DisposeRecordTable from './components/DisposeRecordTable';
export default {
  name: 'deviceWarning',
  components: {
    DisposeRecordTable
  },
  data() {
    return {
      page: {
        pageNum: 1,
        pageSize: 50,
        total: 15
      },
      search:"",
      deviceList: [
        {"c":"1","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"2","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"3","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"4","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"5","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"设备离线","record_datetime":"2023-02-23 12:23:00"},
        {"c":"6","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"设备离线","record_datetime":"2023-02-23 12:23:00"},
        {"c":"7","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"设备离线","record_datetime":"2023-02-23 12:23:00"},
        {"c":"8","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"设备离线","record_datetime":"2023-02-23 12:23:00"},
        {"c":"9","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"10","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"11","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"12","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"13","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"14","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
        {"c":"15","device_code":"SBBM001001","device_name":"设备01","line":"2号线","direction":"上行","bridge_serial_no":"076","content":"状态：正常，剩余电量：80%","record_datetime":"2023-02-23 12:23:00"},
      ],
      disposeRecordDialog: false,
      imgDialogVisible: false,
      images: ['../../../static/images/demo/xc001.png','../../../static/images/demo/xc002.png','../../../static/images/demo/xc003.png'],
      previewImg: '',
    };
  },
  methods: {
    changeDisposeRecordDialog(value) {
      this.disposeRecordDialog = value;
    },
    openDisposeRecordDialog() {
      this.disposeRecordDialog = true;
    },
    previewImage(value) {
      this.previewImg = value;
      this.imgDialogVisible = true;
    },
  },
  mounted: function () {

  }
};
</script>

<style scoped>
.qr-code-icon-default {
  /deep/ .el-icon-my-crash {
    background: url('../../../static/images/project/crash.png') no-repeat;
  }
}
.qr-code-icon-disabled {
  /deep/ .el-icon-my-crash {
    background: url('../../../static/images/project/crash.png') no-repeat;
  }
}
/deep/.el-icon-my-crash {
  font-size: 12px;
  background-size: cover;
}
/deep/ .el-icon-my-crash:before {
  content: '替';
  font-size: 12px;
  visibility: hidden;
}
</style>
