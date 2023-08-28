<template>
  <div class="app-container">
    <div style="padding-bottom: 10px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>告警中心</el-breadcrumb-item>
        <el-breadcrumb-item>设备告警</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-row type="flex" align="middle" style="height: 80px;background-color: #CECECE;">
      <el-col :span="17">
        <div style="padding-left: 30px;;">
          <span style="color: #568AF2;">日期</span>
          <el-date-picker v-model="value1" type="date" placeholder="选择日期">
          </el-date-picker>
          <span style="color: #568AF2;">至</span>
          <el-date-picker v-model="value1" type="date" placeholder="选择日期">
          </el-date-picker>
        </div>
      </el-col>
      <el-col :span="5">
        <el-input placeholder="设备编码、设备名称、线路、桩号、桥墩号" prefix-icon="el-icon-search" v-model="input2">
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
      <el-table-column prop="content" label="告警内容" width="280" align="center"></el-table-column>
      <el-table-column prop="record_datetime" label="时间" align="center"></el-table-column>
      <el-table-column prop="_oper" label="操作" width="280" align="center">
        <template slot-scope='scope'>
          <el-button size="mini" plain type="primary">添加处置记录</el-button>
          <el-button size="mini" plain type="success">查看处置记录</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="page.pageNum" :page-sizes="[10, 20, 50]" :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="float: left;"></el-pagination>

  </div>
</template>

<script>
/* eslint-disable */
export default {
  name: 'deviceWarning',
  components: {
  },
  data() {
    return {
      page: {
        pageNum: 1,
        pageSize: 50,
        total: 15
      },
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
    };
  },
  methods: {

  },
  mounted: function () {

  }
};
</script>

<style scoped></style>
