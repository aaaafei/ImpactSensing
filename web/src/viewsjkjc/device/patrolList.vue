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
        <el-button type="primary" @click="searchData">查询</el-button>
      </el-col>

    </el-row>
    <div style="height:10px;"></div>

    <!-- <el-table :data="deviceList" :height="600" border stripe 
      :header-cell-style="{ background: '#A1B6D8', color: '#fff' }">
      <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
      <el-table-column prop="code" label="设备IMEI" align="center"></el-table-column>
      <el-table-column prop="type" label="监测项" align="center"></el-table-column>
      <el-table-column prop="line" label="线路" align="center"></el-table-column>
      <el-table-column prop="segment" label="区间" align="center"></el-table-column>
      <el-table-column prop="stakeNumber" label="桩号、桥墩号" align="center"></el-table-column>
      <el-table-column prop="_tx" label="检修记录" width="120" align="center">
        <template slot-scope='scope'>
          <span style="color:#1684FC;cursor: pointer;" @click="openDisposeRecordDialog">0次</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.pageNum" :page-sizes="[10, 20, 50]" :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="float: left;"></el-pagination>
    <dispose-record-table :disposeRecordDialog = "disposeRecordDialog" @changeDisposeRecordDialog="changeDisposeRecordDialog"></dispose-record-table>

    <el-dialog title="" :visible.sync="imgDialogVisible" width="50%">
      <div style="height:600px;text-align: center;">
        <img :src="previewImg" alt="" style="height: 100%;">
      </div>
    </el-dialog> -->

    <el-table :data="patrolList" :height="600" border stripe 
      :header-cell-style="{ background: '#A1B6D8', color: '#fff' }">
      <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
      <el-table-column prop="position" label="地点" align="center"></el-table-column>
      <el-table-column prop="content" label="工作内容" align="center"></el-table-column>
      <el-table-column prop="date" label="时间" align="center"></el-table-column>
    </el-table>

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
        total: 1
      },
      search:"",
      deviceList: [],
      disposeRecordDialog: false,
      imgDialogVisible: false,
      images: [this.$STATIC_PREFIX+'images/demo/xc001.png',this.$STATIC_PREFIX+'images/demo/xc002.png',this.$STATIC_PREFIX+'images/demo/xc003.png'],
      previewImg: '',
      patrolList: []
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
      if (this.search != undefined) param.search = this.search;
      this.$request({
        url: '/tmDevice/getDevicePageData/' + this.page.pageNum + '/' + this.page.pageSize,
        method: 'post',
        data: param
      }).then(res => {
        let data = res.data.result_data;
        this.page.total = data.total;
        this.page.pageNum = data.pageNum;
        this.page.pageSize = data.pageSize;
        this.deviceList = data.list;

        this.patrolList = [
          {position:'刘村-马骡圩区间14号桥墩', content:'现场设备检查，通讯模块调试', date:'2024-03-23'},
          {position:'迈皋桥-晓庄区间13号桥墩附近', content:'记录现场设备损坏情况', date:'2024-03-24'},
          {position:'刘村-马骡圩区间14号桥墩', content:'现场设备检查，微损修复', date:'2024-07-11'},
          {position:'迈皋桥-晓庄区间13号桥墩附近', content:'现场设备检查', date:'2024-07-16'},
          {position:'刘村-马骡圩区间14号桥墩', content:'现场设备检查', date:'2024-09-29'},
          {position:'迈皋桥-晓庄区间13号桥墩附近', content:'现场设备检查', date:'2024-09-29'},
        ]
      });
    },
  },
  mounted: function () {
    this.searchData();
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
