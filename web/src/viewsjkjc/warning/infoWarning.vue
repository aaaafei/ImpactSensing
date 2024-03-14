<template>
  <div class="app-container">
    <div style="padding-bottom: 10px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>告警中心</el-breadcrumb-item>
        <el-breadcrumb-item>已发送通知</el-breadcrumb-item>
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
        <el-input placeholder="消息类型、消息内容、接收人" prefix-icon="el-icon-search" v-model="input2">
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
      <el-table-column prop="info_type" label="消息类型" align="center"></el-table-column>
      <el-table-column prop="info_content" label="消息内容" align="center" width="500"></el-table-column>
      <el-table-column prop="info_reciever" label="接收人" align="center"></el-table-column>
      <el-table-column prop="record_datetime" label="发送时间" align="center"></el-table-column>
      <!-- <el-table-column prop="_oper" label="操作" width="280" align="center">
        <template slot-scope='scope'>
          <el-button size="mini" type="primary">添加处置记录</el-button>
          <el-button size="mini" type="success">查看处置记录</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    <el-pagination :current-page="page.pageNum" :page-sizes="[10, 20, 50]" :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="float: left;"></el-pagination>

  </div>
</template>

<script>
/* eslint-disable */
export default {
  name: 'infoWarning',
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
