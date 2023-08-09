<template>
  <div class="app-container">
    <el-row :gutter="12">
      <!-- 左边 -->
      <el-col :span="4">
        <div class="item-title">
          <el-switch
            v-model="switchValue"
            @change="changeData"
            active-text="按监测项目查看"
            inactive-text="按监测区段查看">
          </el-switch>
        </div>
        <div style="height: 80vh;overflow: auto;position: relative;">
          <el-tree :data="data" :props="defaultProps" default-expand-all @node-click="handleNodeClick" :highlight-current="true"></el-tree>
        </div>
      </el-col>

      <!-- 右边 -->
      <el-col :span="20">
        <!-- title -->
        <div class="item-title">
          <span style="font-weight:bold;">监测数据: {{currentMonitorItem}}_{{currentSegment}}_{{currentLine}}_共{{deviceList.length}}支传感器</span>
          <span style="text-decoration:underline;padding-left:50px;">采集时间：{{collectTime}}</span>
          <el-switch
            class="switch2"
            v-model="switchValue2"
            @change="changeLine"
            active-text="右线"
            inactive-text="左线">
          </el-switch>
        </div>

        <!-- device table -->
        <div>
          <el-table :data="subsideDeviceList" :height="500" border stripe :default-expand-all="true" 
          :header-cell-style="{background:'#409eff',color:'#fff'}" 
          @selection-change="handleSelectionChange2" v-if="currentMonitorItem=='纵向不均匀沉降' || currentMonitorItem=='沉降曲线'">

            <el-table-column type="selection"  width="55"> </el-table-column>
            <el-table-column prop="subside_range" label="沉降区间" align="center">
              <template slot-scope='scope'>
                {{scope.row.subside_range}}
              </template>
            </el-table-column>
            <el-table-column prop="device1_code" label="起点" width='150' align="center">
              <template slot-scope='scope'>
                <el-button size="mini" @click="showDeviceInfo(scope.row.device1_code)">{{scope.row.device1_code}}</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="device1_result_value" label="起点结果值(mm)" width='100' align="center">
              <template slot-scope='scope'>
                {{scope.row.device1_result_value}}
              </template>
            </el-table-column>
            <el-table-column prop="device2_code" label="终点" width='150' align="center">
              <template slot-scope='scope'>
                <el-button size="mini" @click="showDeviceInfo(scope.row.device2_code)">{{scope.row.device2_code}}</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="device2_result_value" label="终点结果值(mm)" width='100' align="center">
              <template slot-scope='scope'>
                {{scope.row.device2_result_value}}
              </template>
            </el-table-column>
            <el-table-column prop="range_distance" label="区间范围（m）" width='100' align="center">
              <template slot-scope='scope'>
                {{scope.row.range_distance}}
              </template>
            </el-table-column>
            <el-table-column prop="subside_value" label="差异沉降" width='100' align="center">
              <template slot-scope='scope'>
                {{scope.row.subside_value}}
              </template>
            </el-table-column>
            <el-table-column prop="_oper" label="操作" width='100' align="center">
              <template slot-scope='scope'>
                <el-tooltip content="显示不均匀沉降" placement="top">
                  <span style="cursor:pointer;" @click="showSubside(scope.row)"><img :src="$STATIC_PREFIX+'images/cj.png'" style="width:18px;"/></span>
                </el-tooltip>
                &nbsp;&nbsp;
                <el-tooltip content="显示沉降数值" placement="top">
                  <span style="cursor:pointer;" @click="showSubsideChart(scope.row)"><img :src="$STATIC_PREFIX+'images/cj_value.png'" style="width:18px;"/></span>  
                </el-tooltip>
                
              </template>
            </el-table-column>

          </el-table>

          <el-table :data="deviceList" :height="500" border stripe :default-expand-all="true" 
          :header-cell-style="{background:'#409eff',color:'#fff'}" 
          @selection-change="handleSelectionChange" @row-click="viewDevice" v-if="currentMonitorItem!='纵向不均匀沉降' && currentMonitorItem!='沉降曲线'">

            <el-table-column type="selection"  width="55"> </el-table-column>
            <el-table-column prop="status" label="运行状态" width='100'  align="center">
              <template slot-scope='scope'>
                <el-button type="success" size="mini" v-if="scope.row.status == '1'">正常</el-button>
                <el-button type="success" size="mini" v-if="scope.row.status == '3'">正常</el-button>
                <el-button type="warning" size="mini" v-if="scope.row.status == '0'">故障</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="stakeNumber" label="桩号" width='100'  align="center">
              <template slot-scope='scope'>
                {{scope.row.stakeNumber}}
              </template>
            </el-table-column>
            <el-table-column prop="code" label="仪器编号" align="center">
              <template slot-scope='scope'>
                <el-button size="mini" @click="showDeviceInfo(scope.row.code)">{{scope.row.code}}</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="installPosition" label="位置" width='100'  align="center">
              <template slot-scope='scope' style="padding: 1px 1px;">
                <el-button size="mini" @click="devicePositionVisible=true;" style="padding: 1px 1px;">{{scope.row.installPosition}}</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="监测项目" width='140' align="center">
              <template slot-scope='scope'>
                {{scope.row.type}}
              </template>
            </el-table-column>
            <el-table-column prop="deviceType" label="仪器类别" width='140'  align="center">
              <template slot-scope='scope'>
                {{scope.row.deviceType}}
              </template>
            </el-table-column>
            <el-table-column prop="" label="结果值" width='150' align="center" v-if="currentMonitorItem!='地震动响应'">
              <template slot-scope='scope'>
                {{(scope.row.realtimePhysicalData - scope.row.initialValue).toFixed(5)}}&nbsp;&nbsp;{{getUnit(scope.row.type)}}
              </template>
            </el-table-column>

          </el-table>
        </div>


        <div style="margin-top:10px;background-color:#ffffff;vertical-align:middle;padding:10px 10px 0 10px;"> 
          <span style="font-weight:bold;" >设备监测值和健康度变化曲线: </span>
          <span>{{selectDevicesStr==''?'未选择设备':selectDevicesStr}}</span>
        </div>

        <!-- dynamic chart -->
        <div style="background-color:#fff;" v-if="currentMonitorItem!='纵向不均匀沉降' && currentMonitorItem!='地震动响应'">
          <device-dynamic-chart :deviceCode="selectDevicesStr" :unit="getUnit(currentMonitorItem)" :showtype="'all'"></device-dynamic-chart>
        </div>

        <div style="background-color:#fff;" v-if="currentMonitorItem=='纵向不均匀沉降'">
          <device-dynamic-chart-subside :deviceCode="selectDevicesStr2" :unit="getUnit(currentMonitorItem)" :subsideRange="subsideRange"></device-dynamic-chart-subside>
        </div>

        <div style="background-color:#fff;" v-if="currentMonitorItem=='地震动响应'">
          <device-dynamic-chart-earthquake :deviceCode="selectDevicesStr" :unit="getUnit(currentMonitorItem)"></device-dynamic-chart-earthquake>
        </div>

      </el-col>
    </el-row>

    <el-dialog :visible.sync="deviceInfoVisible" width="70%" title="设备信息" append-to-body>
        <deviceInfo :deviceInfoForm="deviceInfoForm"></deviceInfo>
    </el-dialog>

    <el-dialog :visible.sync="devicePositionVisible" width="40%" height="60%" title="安装位置-剖面图" append-to-body>
        <devicePositionInfo :deviceInfoForm="deviceInfoForm"></devicePositionInfo>
    </el-dialog>

  </div>
</template>

<script>
/* eslint-disable */
// import LineChart from './components/LineChart';
import deviceInfo from './deviceInfo';
import deviceStatusHistory from './deviceStatusHistory';
import deviceDynamicChart from './deviceDynamicChart';
// import deviceList from './components/deviceList';
import devicePositionInfo from './devicePositionInfo';
import DeviceDynamicChartSubside from './deviceDynamicChartSubside.vue';
import deviceDynamicChartEarthquake from './deviceDynamicChartAT.vue';
export default {
  name: 'monitor',
  components: {
    // LineChart,
    deviceInfo,
    deviceStatusHistory,
    deviceDynamicChart,
    // deviceList,
    devicePositionInfo,
    DeviceDynamicChartSubside,
    deviceDynamicChartEarthquake,
  },
  data () {
    return {
      switchValue: true,
      switchValue2: false,
      deviceList:[],
      deviceInfoVisible: false,
      devicePositionVisible: false,
      deviceInfoForm: {},
      searchText:'',
      data: [],
      defaultProps: [{
        children: 'children',
        label: 'label'
      }],
      statusList: [
        {label:'正常',value:'1'},
        {label:'异常',value:'0'},
      ],
      currentMonitorItem:'断面环缝伸缩量',
      currentSegment:'浦口竖井',
      currentLine: this.switchValue2?'右线':'左线',
      collectTime: '',
      multipleSelection: [],
      selectDevicesStr: '',
      selectDevicesStr2: '',
      subsideDeviceList:[],
      subsideRange:''
    };
  },
  computed: {
    
  },
  methods: {
    viewDataWithDecimal(val){
      let v = str(val).split(".");
      if (v.length<2) {
        return v[0];
      }else{
        return v[0] + '.' + v[1].substr(0,2);
      }
    },
    extractAdjoiningNumber(adjoiningNumber){
      return adjoiningNumber.split('-')[adjoiningNumber.split('-').length-1];
    },
    getTreeData() {
      this.$request({
        url:'monitorItem/getTreeDataByMonitorItem',
        method:'post',
        params: {}
      }).then(res => {
        let data = res.data.result_data;
        this.data = [data];
      });

    },
    getTreeData2() {
      this.$request({
        url:'monitorItem/getTreeDataBySegment',
        method:'post',
        params: {}
      }).then(res => {
        let data = res.data.result_data;
        this.data = [data];
      });

    },
    getDeviceList() {
      let param = {
        segment:this.currentSegment,
        type:this.currentMonitorItem,
        line:this.currentLine
      };
      this.$request({
        url:'tmDevice/getDeviceData/',
        method:'post',
        data: param
      }).then(res => {
        let data = res.data.result_data;
        //console.log(data);
        this.deviceList = data;
        if(data.length>0){
          this.deviceInfoForm = data[0];
          this.collectTime = data[0].collectTime;
        }
      });
    },
    getSubsideDeviceList() {
      let that = this;
      let param = {
        segment:this.currentSegment,
        type:this.currentMonitorItem,
        line:this.currentLine
      };
      this.$request({
        url:'tmDevice/getSubsideData/',
        method:'post',
        data: param
      }).then(res => {
        let data = res.data.result_data;
        this.subsideDeviceList = data;
      });
    },
    handleNodeClick(obj,obj2,obj3) {
      let code = obj.code;
      let parent_code = obj2.parent.data.code;
      if(parent_code=='root') {
        //console.log(code);
        return;
      }
      let param = {};
      if(this.switchValue) {
        this.currentMonitorItem = parent_code;
        this.currentSegment = code;
      }else {
        this.currentMonitorItem = code;
        this.currentSegment = parent_code;
      }
      if(this.currentMonitorItem == '纵向不均匀沉降') {
        this.getSubsideDeviceList();
      }else {
        this.getDeviceList();
      }
      
    },
    changeData(obj) {
      if(obj) {
        this.getTreeData();
      }else {
        this.getTreeData2();
      }
    },
    changeLine() {
      if(this.switchValue2) {
        this.currentLine = '右线';
      }else {
        this.currentLine = '左线';
      }
      if(this.currentMonitorItem == '纵向不均匀沉降') {
        this.getSubsideDeviceList();
      }else {
        this.getDeviceList();
      }
    },
    viewDevice(row, column, event) {
      this.deviceInfoForm = row;
    },
    getUnit(text){
      let unit = '';
      if (text=='断面环缝伸缩量') unit = 'mm';
      if (text=='断面间环缝伸缩量') unit = 'mm';
      if (text=='断面纵缝伸缩量') unit = 'mm';
      if (text=='管片错台') unit = 'mm';
      if (text=='管片纵缝错台') unit = 'mm';
      if (text=='管片环缝错台') unit = 'mm';
      if (text=='纵向不均匀沉降') unit = 'mm';
      if (text=='沉降曲线') unit = 'mm';
      if (text=='断面结构应力') unit = 'με';
      if (text=='管片倾斜与偏转') unit = 'º';
      if (text=='地震动响应') unit = 'm/s²';
      this.unit = unit;
      return unit
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      var arr=new Array();
      for(var i=0;i<this.multipleSelection.length;i++){
        arr.push(this.multipleSelection[i].code);
      }
      this.selectDevicesStr = arr.join("、");
    },
    handleSelectionChange2(val) {
      this.multipleSelection = val;
      var arr=new Array();
      let arr2 =  new Array();
      for(var i=0;i<this.multipleSelection.length;i++){
        arr.push(this.multipleSelection[i].device1_code+ "@"+ this.multipleSelection[i].device2_code); 
        arr2.push(this.multipleSelection[i].subside_range);
      }
      this.selectDevicesStr2 = arr.join("、");
      this.subsideRange = arr2.join("、"); 
      this.selectDevicesStr = this.selectDevicesStr2;
    },
    showDeviceInfo(device_code){
      this.deviceInfoForm = null;
      let that = this;
      let param = {
        code:device_code,
      };
      this.$request({
        url:'tmDevice/getDeviceData/',
        method:'post',
        data: param
      }).then(res => {
        let data = res.data.result_data[0];
        this.deviceInfoForm = data;
        this.deviceInfoVisible = true;
      });
    },
    showSubsideChart(row){
      var arr=[row.device1_code, row.device2_code];
      this.selectDevicesStr = arr.join("、");
      this.currentMonitorItem = '沉降曲线';
    },
    showSubside(row){
      console.log(row);
      var arr=[row.device1_code, row.device2_code];
      this.selectDevicesStr = arr.join("@");
      this.currentMonitorItem = '纵向不均匀沉降';
    }
  },
  mounted: function() { 
    this.getTreeData();
    this.getDeviceList();
    
  }
};
</script>

<style scoped>
  .item-title {
    height:35px;
    background-color: #ffffff;
    padding: 10px 10px 0 10px ;
    margin-bottom: 10px;
    vertical-align: middle;
  }
  .switch2 {
    float: right;
  }
  .el-table >>> .el-table\_\_expanded-cell{
    padding: 0px 0px;
  }
  .el-table >>> .el-form-item{
    margin-bottom: 0px;
  }
  .row-expanded {
    padding: 5px 0px;
  }

  /**滚动条的宽度*/
  ::-webkit-scrollbar {
      width: 2px;/*竖向*/        
      height: 5px;/*横向*/
  }

  /*滚动条的滑块*/
  ::-webkit-scrollbar-thumb {
      background-color: #409EFF;
      border-radius: 3px;
  }

  /deep/ .el-table__body-wrapper::-webkit-scrollbar {
      width: 3px;/*竖向*/        
      height: 5px;/*横向*/
  }

  /deep/ .el-table__body-wrapper::-webkit-scrollbar-thumb{
      background-color: #409EFF;
      border-radius: 3px;
  }

</style>
