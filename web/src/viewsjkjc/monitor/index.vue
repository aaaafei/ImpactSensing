<template>
  <div class="app-container">
    <div style="padding-bottom: 10px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>监测数据</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-row type="flex" align="middle" style="height: 80px;background-color: #CECECE;">
      <el-col :span="17">
        <div style="padding-left: 30px;;">
          <span style="color: #568AF2;">日期</span>
          <el-date-picker v-model="beginDate" type="date" placeholder="选择日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
          <span style="color: #568AF2;">至</span>
          <el-date-picker v-model="endDate" type="date" placeholder="选择日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss">
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
      <!-- <el-table-column prop="c" label="序号" width="50" align="center"></el-table-column> -->
      <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
      <el-table-column prop="clientimei" label="设备imei" align="center"></el-table-column>
      <el-table-column prop="mtype" label="监测项目" align="center"></el-table-column>
      <el-table-column prop="line" label="线路" align="center"></el-table-column>
      <el-table-column prop="segment" label="区间" align="center"></el-table-column>
      <el-table-column prop="stakeNumber" label="桩号/桥墩号" align="center"></el-table-column>
      <el-table-column prop="content" label="内容" width="280" align="center">
        <template slot-scope="scope">
          <span> {{ "状态：正常，剩余电量：" + scope.row.voltage }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="timstamp" label="时间" align="center"></el-table-column>
      <!-- <el-table-column prop="_oper" label="操作" width="280" align="center">
        <template slot-scope='scope'>
          <el-button size="mini">详细信息</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.pageNum"
      :page-sizes="[20, 50, 100]" :page-size="page.pageSize" layout="total, sizes, prev, pager, next, jumper"
      :total="page.total"></el-pagination>

  </div>
</template>

<script>
/* eslint-disable */
export default {
  name: 'monitor',
  components: {
  },
  data() {
    return {
      tableHeight: document.body.clientHeight - 330,
      page: {
        pageNum: 1,
        pageSize: 50,
        total: 1
      },
      deviceList: [],
      beginDate: null,
      endDate: null,
      search: "",
    };
  },
  methods: {
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
        url: '/tmOriginData/getPageListCatalogH0/' + this.page.pageNum + '/' + this.page.pageSize,
        method: 'post',
        data: param
      }).then(res => {
        let data = res.data.result_data;
        this.page.total = data.total;
        this.page.pageNum = data.pageNum;
        this.page.pageSize = data.pageSize;
        this.deviceList = data.list;
      });

    },
  },
  mounted: function () {
    this.searchData();
  }
};
</script>

<style scoped></style>
