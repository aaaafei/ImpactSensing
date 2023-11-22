<template>
  <div class="app-container">
    <div style="padding-bottom: 10px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>告警中心</el-breadcrumb-item>
        <el-breadcrumb-item>受撞告警</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-row type="flex" align="middle" style="height: 80px;background-color: #CECECE;">
      <el-col :span="17">
        <div style="padding-left: 30px;;">
          <span style="color: #568AF2;">日期</span>
          <el-date-picker v-model="date1" type="date" placeholder="选择日期">
          </el-date-picker>
          <span style="color: #568AF2;">至</span>
          <el-date-picker v-model="date2" type="date" placeholder="选择日期">
          </el-date-picker>
        </div>
      </el-col>
      <el-col :span="5">
        <el-input placeholder="设备编码、设备名称、线路、桩号、桥墩号" prefix-icon="el-icon-search" v-model="search">
        </el-input>
      </el-col>
      <el-col :span="2" style="text-align: center;">
        <el-button type="primary" @click="searchData">查询</el-button>
      </el-col>

    </el-row>
    <div style="height:10px;"></div>

    <el-table :data="deviceList" :height="tableHeight" border stripe 
      :header-cell-style="{ background: '#A1B6D8', color: '#fff' }">
      <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
      <el-table-column prop="deviceCode" label="设备IMEI" align="center"></el-table-column>
      <el-table-column prop="monitorType" label="监测项目" align="center"></el-table-column>
      <el-table-column prop="line" label="线路" align="center"></el-table-column>
      <el-table-column prop="segment" label="区间" align="center"></el-table-column>
      <el-table-column prop="stakeNumber" label="桥墩号" align="center"></el-table-column>
      <el-table-column prop="remark" label="告警内容" width="280" align="center"></el-table-column>
      <el-table-column prop="collectTime" label="时间" align="center"></el-table-column>
      <el-table-column prop="_oper" label="操作" width="280" align="center">
        <template slot-scope='scope'>
          <el-button size="mini" plain type="primary" @click="openDisposeRecordDialog">添加处置</el-button>
          <el-button size="mini" plain type="success" @click="openDisposeRecordDialog">查看处置记录</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="page.pageNum" :page-sizes="[10, 20, 50]" :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="float: left;"></el-pagination>

    <dispose-record-form :disposeRecordDialog = "disposeRecordDialog" @changeDisposeRecordDialog="changeDisposeRecordDialog"></dispose-record-form>

  </div>
</template>

<script>
/* eslint-disable */
import DisposeRecordForm from './components/DisposeRecordForm';
export default {
  name: 'deviceWarning',
  components: {
    DisposeRecordForm,
  },
  data() {
    return {
      tableHeight:document.body.clientHeight-330,
      page: {
        pageNum: 1,
        pageSize: 50,
        total: 15
      },
      deviceList: [],
      date1: null,
      date2: null,
      search: "",
      disposeRecordDialog: false,
    };
  },
  methods: {
    changeDisposeRecordDialog(value) {
      this.disposeRecordDialog = value;
    },
    openDisposeRecordDialog() {
      this.disposeRecordDialog = true;
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.searchData();
    },
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.searchData();
    },
    searchData() {
      let param = {};
      if (this.beginDate != undefined) param.beginDate = this.beginDate;
      if (this.endDate != undefined) param.endDate = this.endDate;
      if (this.search != undefined) param.search = this.search;
      this.$request({
        url: '/tmWarningRecords/getPageList/' + this.page.pageNum + '/' + this.page.pageSize,
        method: 'post',
        data: param
      }).then(res => {
        let data = res.data.result_data;
        this.page.total = data.total;
        this.page.pageNum = data.pageNum;
        this.page.pageSize = data.pageSize;
        this.deviceList = data.list;
      });
    }
  },
  mounted: function () {
    this.searchData();
  }
};
</script>

<style scoped></style>
