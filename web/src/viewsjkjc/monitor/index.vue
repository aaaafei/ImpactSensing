<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24">
        <!-- <el-button disabled>筛选条件</el-button> -->
        <el-select v-model="selectedSegmentId" placeholder="监测区间段">
          <el-option
            v-for="item in segments"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>

        <el-select v-model="selectedSectionId" placeholder="桩号">
          <el-option
            v-for="item in sections"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>

        <el-select v-model="selectedMonitorItemIds" placeholder="监测项目">
          <el-option
            v-for="item in monitorItems"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-col>

    </el-row>
    <div style="height:10px;"></div>
    <!-- <el-row>
      <el-col :span="20">
        <div>
          <el-button disabled>监测项目</el-button>
          <el-checkbox-group v-model="selectedMonitorItemIds" size="medium">
            <el-checkbox-button v-for="item in monitorItems" :label="item.name" :key="item.id" @change="filterDevices">{{item.name}}</el-checkbox-button>
          </el-checkbox-group>
        </div>
      </el-col>
    </el-row>

    <div style="height:20px;"></div> -->

    <el-row :gutter="20">
      <el-col :span="24">
        <div style="margin-bottom:6px;">
          <span style="margin-right:20px;">设备列表</span>
          <span style="margin-right:60px;">共【30】处传感器</span>
          <span style="margin-right:60px;">【0】故障，设备完好率：100%</span>
          <span style="margin-right:60px;">最新读数时间：2020-07-01 12:00:00</span>
        </div>

        <el-table :data="deviceList" :height="600" border stripe :header-cell-style="{background:'#409eff',color:'#fff'}">
          <el-table-column prop="status" label="运行状态" width='80'>
            <template slot-scope='scope'>
              <el-button type="success" size="mini" v-if="scope.row.status == '1'">正常</el-button>
              <el-button type="warning" size="mini" v-if="scope.row.status == '0'">故障</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="section" label="桩号"></el-table-column>
          <el-table-column prop="code" label="仪器编号" width='120'></el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="type" label="监测项目"></el-table-column>
          <el-table-column prop="type" label="仪器类别"></el-table-column>
          <el-table-column prop="position" label="位置"></el-table-column>
          <el-table-column prop="realtime_data" label="最新读数"></el-table-column>
          <el-table-column prop="warning_data" label="预警值"></el-table-column>
          <el-table-column prop="_oper" label="操作" width="280">
            <template slot-scope='scope'>
              <el-button size="mini" @click="deviceInfoVisible=true;">设备信息</el-button>
              <el-button size="mini" @click="openDeviceStatusHistory(scope.row)">历史读数</el-button>
              <el-button size="mini" @click="deviceDynamicChartVisible=true;">监测图</el-button>

            </template>
          </el-table-column>
        </el-table>
        <el-pagination
      :current-page="page.pageNum"
      :page-sizes="[10, 20, 50]"
      :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total"
    ></el-pagination>

      </el-col>

    </el-row>

    <el-dialog :visible.sync="deviceInfoVisible" width="70%" title="设备信息" append-to-body>
        <deviceInfo :detailForm="deviceInfoForm"></deviceInfo>
    </el-dialog>

    <el-dialog :visible.sync="deviceStatusHistoryVisible" width="50%" :title="historyStatusTitle" append-to-body>
        <deviceStatusHistory :deviceId="targetDeviceId"></deviceStatusHistory>
    </el-dialog>

    <el-dialog :visible.sync="deviceDynamicChartVisible" width="70%" title="监测图" append-to-body>
        <deviceDynamicChart :deviceId="targetDeviceId"></deviceDynamicChart>
    </el-dialog>

  </div>
</template>

<script>
/* eslint-disable */
import LineChart from './components/LineChart';
import deviceInfo from './deviceInfo';
import deviceStatusHistory from './deviceStatusHistory';
import deviceDynamicChart from './deviceDynamicChart';
export default {
  name: 'monitor',
  components: {
    LineChart,
    deviceInfo,
    deviceStatusHistory,
    deviceDynamicChart
  },
  data () {
    return {
      page: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      segments:[],
      selectedSegmentId:null,
      sections:[],
      selectedSectionId:null,
      monitorItems:[],
      selectedMonitorItemIds:[],
      deviceList:[],
      lineChartRefresh: false,
      deviceInfoVisible: false,
      deviceInfoForm: {},
      deviceStatusHistoryVisible: false,
      targetDeviceId: null,
      deviceDynamicChartVisible: false,
      historyStatusTitle: null
    };
  },
  methods: {
    getSegments() {
      this.segments = [
        {name:'浦口大堤',id:'101'},
        {name:'浦口竖井',id:'102'},
        {name:'江中',id:'103'},
        {name:'覆土厚度最小',id:'104'},
        {name:'梅子洲大堤',id:'105'},
        {name:'梅子洲竖井',id:'106'},
      ]
    },
    getSections() {
      this.sections = [
        {id:'101L01',name:'LK3+386'},
        {id:'101L02',name:'LK3+386'},
        {id:'101L03',name:'LK3+386'},
        {id:'101L04',name:'LK3+386'},
      ]
    },
    getMonitorItems() {
      this.monitorItems = [
        {id:'101',name:'管片应力'},
        {id:'102',name:'管片倾斜与偏转'},
        {id:'103',name:'接缝张开度'},
        {id:'104',name:'管片错台'},
        {id:'105',name:'纵向不均匀沉降'},
        {id:'106',name:'地震动'},
      ];
    },
    filterDevices() {
      console.log(this.selectedMonitorItemIds);
    },
    getDeviceList() {


      for(var i=0;i<20;i++){
        this.deviceList.push({uuid:'aa', code:'JLS-AND-001', name:'测缝计', type:'接缝张开度', section:'LK3+386', segment:'浦口大堤', realtime_data:'4.6', warning_data:'5.4', status:'1',position:'右侧拱脚'});
      }

    },
    initChart(){
      this.lineChartRefresh = true;
    },
    openDeviceStatusHistory(row){
      this.deviceStatusHistoryVisible=true;
      this.historyStatusTitle = '历史记录     -   '+row.name+row.code;
    }
  },
  mounted: function() {
    this.getSegments();
    this.getSections();
    this.getMonitorItems();
    this.getDeviceList();
    this.initChart();
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
