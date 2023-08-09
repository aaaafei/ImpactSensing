<template>
  <div class="app-container">
   <el-row>
      <el-col>
        <el-date-picker
          v-model="value2"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions">
        </el-date-picker>
        <el-button :loading="downloadLoading" class="filter-item" type="default" icon="el-icon-search" @click="handleDownload">
          查询
        </el-button>
         <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
          导出
        </el-button>
      </el-col>
    </el-row>
  
    <div style="height:20px;"></div>
        <el-table :data="deviceList" :height="500" border stripe>
          <el-table-column prop="detect_time" label="监测时间"></el-table-column> 
          <el-table-column prop="realtime_data" label="读取值"></el-table-column>  
          <el-table-column prop="realtime_data" label="预警值"></el-table-column>  
          <el-table-column prop="status" label="运行状态">
            <template slot-scope='scope'>
              <el-button type="success" size="mini" v-if="scope.row.status == '1'">正常</el-button>
              <el-button type="warning" size="mini" v-if="scope.row.status == '0'">故障</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
      :current-page="page.pageNum"
      :page-sizes="[50, 100, 150, 200]"
      :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total"
    ></el-pagination>
 
  </div>
</template>

<script>
/* eslint-disable */  
export default { 
  data () {
    return {
      page: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      }, 
      deviceList:[],
      // pickerOptions: {
      //   shortcuts: [{
      //     text: '最近一周',
      //     onClick(picker) {
      //       const end = new Date();
      //       const start = new Date();
      //       start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      //       picker.$emit('pick', [start, end]);
      //     }
      //   }, {
      //     text: '最近一个月',
      //     onClick(picker) {
      //       const end = new Date();
      //       const start = new Date();
      //       start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      //       picker.$emit('pick', [start, end]);
      //     }
      //   }, {
      //     text: '最近三个月',
      //     onClick(picker) {
      //       const end = new Date();
      //       const start = new Date();
      //       start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
      //       picker.$emit('pick', [start, end]);
      //     }
      //   }]
      // },
      value1: '',
      value2: ''
    };
  },
  methods: { 
    getDeviceList() {
      this.deviceList = [
        {uuid:'aa', detect_time:'2020-06-30 12:00', realtime_data:'4.6',  status:'1'},
        {uuid:'aa', detect_time:'2020-06-29 12:00', realtime_data:'4.6',  status:'0'},
        {uuid:'aa', detect_time:'2020-06-28 12:00', realtime_data:'4.6',  status:'1'},
        {uuid:'aa', detect_time:'2020-06-27 12:00', realtime_data:'4.6',  status:'1'},
        {uuid:'aa', detect_time:'2020-06-26 12:00', realtime_data:'4.6',  status:'0'},
        {uuid:'aa', detect_time:'2020-06-25 12:00', realtime_data:'4.6',  status:'1'},
        {uuid:'aa', detect_time:'2020-06-24 12:00', realtime_data:'4.6',  status:'1'},
        {uuid:'aa', detect_time:'2020-06-23 12:00', realtime_data:'4.6',  status:'0'},
        {uuid:'aa', detect_time:'2020-06-22 12:00', realtime_data:'4.6',  status:'1'},
        {uuid:'aa', detect_time:'2020-06-21 12:00', realtime_data:'4.6',  status:'1'},
        {uuid:'aa', detect_time:'2020-06-20 12:00', realtime_data:'4.6',  status:'1'},
      ];
    },
      handleDownload() { 
       
    }
  },
  mounted: function() { 
    this.getDeviceList(); 
  }
};
</script>

<style scoped>
  .myrow {
    height:60px;
    margin-bottom:1px;
    font-size: 18px;
  }
</style>