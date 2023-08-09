<template>

  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="今天" name="oneDay"></el-tab-pane>
      <el-tab-pane label="最近一周" name="oneWeek"></el-tab-pane>
      <el-tab-pane label="最近一月" name="oneMonth"></el-tab-pane>
      <el-tab-pane label="查询" name="export">
        <template slot-scope='scope'>
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
          <el-button type="primary" icon="el-icon-refresh" @click="dataRefresh">刷新</el-button>
          <!-- <el-button type="primary" icon="el-icon-download">导出</el-button> -->
        </template>
      </el-tab-pane>
    </el-tabs>

 <el-radio-group v-model="loadLineChoose">
    <el-radio :label="0">都加载</el-radio>
    <el-radio :label="1">监测值</el-radio>
    <el-radio :label="2">健康度</el-radio>
  </el-radio-group>

    <div id="deviceDataLineChart" style="height:450px;width:100%;"></div>

    <div id="healthyDataLineChart" style="height:450px;width:100%;"></div>

  </div>
</template>

<script>
/* eslint-disable */
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './components/mixins/resize'
import { dateUtil } from '@/assets/utils/dateUtil';

export default {
  mixins: [resize],
  props:["deviceCode","unit","subsideRange"],
  data() {
    return {
      activeName: 'oneDay',
      loadLineChoose:1,
      chart: null,
      chartData: {
        actualData: [],
        dateRange: [],
      },
      lengend1Map:{}, 
      chart2: null,
      chartData2: {
        actualData: [],
        dateRange: []
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      value1: '',
      value2: []
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val);
      }
    },
    chartData2: {
      deep: true,
      handler(val) { 
        this.setOptions2(val);
      }
    },
    deviceCode: {
      handler(val) {
        let tab = {name:this.activeName}
        this.handleClick(tab);
 
        this.chartData= { actualData: [],  dateRange: []  }; 
        this.chartData2={  actualData: [],  dateRange: []  };
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.chartData= {actualData:[],dateRange:[]}; 
      this.chartData2= {actualData:[],dateRange:[]}; 
      this.initChart();
      this.handleClick(this.activeName);
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose();
    this.chart = null;

    if (!this.chart2) {
      return
    }
    this.chart2.dispose();
    this.chart2 = null;
  },
  methods: {
    dataRefresh(){
      //console.log(this.value2);
      if(this.value2!=undefined && this.value2!= null && this.value2.length>0){
        let begin = new dateUtil(this.value2[0]).getYearMonthDay();
        let end =  new dateUtil(new dateUtil(this.value2[1]).getTime() + 1 * new dateUtil().getOneDayMillis()).getYearMonthDay();
        this.getData(this.deviceCode,begin,end);
      }
    },
    handleClick(tab, event) {
      //tab切换肯定要切换上次加载的曲线
      this.chartData= {actualData:[],dateRange:[]}; //清空x轴 
      this.chartData2= {actualData:[],dateRange:[]}; //清空x轴

      const today = new dateUtil();
      let begin;
      let end;
      switch(tab.name){
        case 'oneDay':
          /* 今天：获取当天从0点开始的所有数据 */
          begin = today.getYearMonthDay();
          let nextDay = new dateUtil(today.getTime() + 1 * today.getOneDayMillis());
          end = nextDay.getYearMonthDay();
          this.getData(this.deviceCode,begin,end);
          break;
        case 'oneWeek':
          /* 最近一周：从当天算起，往前推6天 */
          end = new dateUtil(today.getTime() + 1 * today.getOneDayMillis()).getYearMonthDay();
          begin = new dateUtil(today.getTime() - 6 * today.getOneDayMillis()).getYearMonthDay();
          this.getData(this.deviceCode,begin,end);
          break;
        case 'oneMonth':
          /* 最近一月：从当天算起，往前推29天 */
          end = new dateUtil(today.getTime() + 1 * today.getOneDayMillis()).getYearMonthDay();
          begin = new dateUtil(today.getTime() - 29 * today.getOneDayMillis()).getYearMonthDay();
          this.getData(this.deviceCode,begin,end);
          break;
      }
    },
    getData(deviceCode,begin,end){
      if(this.loadLineChoose==0){
        this.getOriginData(deviceCode,begin,end);
        this.getHealthData(deviceCode,begin,end);
      }
      else if(this.loadLineChoose==1){ 
        this.getOriginData(deviceCode,begin,end);
      }
      else if(this.loadLineChoose==2){ 
        this.getHealthData(deviceCode,begin,end);
      }
    },
    initChart() {
      this.chart = echarts.init(document.getElementById('deviceDataLineChart'), 'macarons');
      this.setOptions(this.chartData);

      this.chart2 = echarts.init(document.getElementById('healthyDataLineChart'), 'macarons');
      this.setOptions2(this.chartData2);
    }, 
    setOptions({actualData,dateRange} = {}) {
      let that = this;
      this.chart.setOption({
      title: {
        show: this.deviceCode=='', 
        extStyle: {
          color: "grey",
          fontSize: 20
        },
        text: "请先选择需要展示变化曲线的设备",
        left: "center",
        top: "center"
      }, 
        xAxis: {
          name:'时间',
          data: dateRange,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 35,
          right: 55,
          bottom: 40,
          top: 80,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        }, 
        yAxis: { 
           axisLine:{  
                 lineStyle:{  
                     color:'#4A5675',  
                     // width:2  
                 }  
           },
           name:'单位',
           type: 'value',
           splitLine: {
               show: false
           },
           scale:true
        },  
        legend: {
          // width:'90%',
          // left:'5%',
          data: this.deviceCode.split('、'),
          formatter: function (name) { 
              return that.lengend1Map[name];
          }
        },
        toolbox: {
          show: this.$checkRoleAdmin(), 
          bottom: 0,
          feature: {
            dataZoom: {
                yAxisIndex: 'none'
            },
            dataView: {readOnly: false},
            restore: {},
            saveAsImage: {}
          }
        },
        series: actualData
      },true);
    },
    setOptions2({actualData,dateRange} = {}) {
      let that = this;  
      this.chart2.setOption({
      title: {
        show: this.deviceCode=='', 
        extStyle: {
          color: "grey",
          fontSize: 20
        },
        text: "请先选择需要展示变化曲线的设备",
        left: "center",
        top: "center"
      }, 
        xAxis: {
          name:'时间',
          data: dateRange,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 35,
          right: 55,
          bottom: 40,
          top: 80,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {   
					 max:5,
           axisLine:{  
                 lineStyle:{  
                     color:'#4A5675',  
                     // width:2  
                 }  
           },
           name: '安全度值',
           type: 'value',
           splitLine: {
               show: false
           },
           scale:true,
        },  
        legend: {
          data: this.deviceCode.split('、'),
          formatter: function (name) {   
            let codes = that.deviceCode.split('、');
            let idx = 0;
            codes.forEach( (item,index) => {
              if(name==item){ 
                idx = index; 
              } 
            });
            let range = that.subsideRange.split('、');
            return range[idx];
          }
        },
        toolbox: {
          show: this.$checkRoleAdmin(), 
          bottom: 0,
          feature: {
            dataZoom: {
                yAxisIndex: 'none'
            },
            dataView: {readOnly: false},
            restore: {},
            saveAsImage: {}
          }
        },
        series: actualData
      },true);
    },
    getOriginData(deviceCode,begin,end){  
      if(deviceCode!=''){


        this.chartData= {actualData:[],dateRange:[]}; //清空x轴
        this.chart.showLoading({
          text: '数据正在加载...',
          textStyle: { fontSize : 30 , color: '#444' },
          effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
        });

       this.$request({
        url: '/tmOriginData/getSubsideDataMulti',
        method: 'post',
        data:{
          "device_code": deviceCode,
          "beginDate": begin,
          "endDate": end
        }
       }).then(res => {
          // let codeArr = deviceCode.split('、'); 
          let data = res.data.result_data;
          let codeArr = []; 
          for(let key in data){ 
            codeArr.push(key);
            //console.log(key,data[key][0]['subside_range']);
            this.lengend1Map[key]= data[key][0]['subside_range'];
          }
          //console.log(this.lengend1Map);
          let seriesArr = new Array();
          for(let x=0;x<codeArr.length;x++){
                    let line =  {
                        name: codeArr[x],
                        smooth: true,
                        type: 'line',
                        itemStyle: {
                          normal: { 
                            lineStyle: { 
                              width: 2
                            }
                          }
                      }, 
                      animationDuration: 2800,
                      animationEasing: 'quadraticOut'
                    };

                  //y轴 value
                  let actualData = []; 
                  for(let i=0; i < data[codeArr[x]].length ; i++){
                    actualData.push(data[codeArr[x]][i].subside_value); 
                  }
                  line['data'] = actualData; 
                  seriesArr.push(line);

                  //x轴 data range
                  if(x==0){ 
                    this.chartData.dateRange = [];
                    for(let i=0; i < data[codeArr[x]].length ; i++){ 
                          this.chartData.dateRange.push(data[codeArr[x]][i].collect_time);
                    }
                  }
          

          }
          this.chartData.actualData = seriesArr; 
          this.chart.hideLoading();
       });

    


      }

    },
    getHealthData(deviceCode,begin,end){  
      if(deviceCode!=''){
 
      //设备健康度
      this.chartData2= {actualData:[],dateRange:[]}; //清空x轴
      this.chart2.showLoading({
        text: '数据正在加载...',
        textStyle: { fontSize : 30 , color: '#444' },
        effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
      });
      //SL-HM/PD/L/002@SL-HM/PD/L/001、SL-HM/PD/L/003@SL-HM/PD/L/002、SL-HM/PD/L/004@SL-HM/PD/L/003
      //指传前一个静力水准仪编号就可以了
      var codes = deviceCode.split("、"); 
      var newCodes = new Array();//device1的device_code数组
      var device1CodeMapTwain = {};//键值对：device1_code:device1_code@device2_code
      codes.forEach(
        element =>{
          var temp = element.split('@');
          if(temp!=null && temp.length>0){
            newCodes.push(temp[0]);
            device1CodeMapTwain[temp[0]]=element;
          }
        }
      ); 

       this.$request({
        url: '/calculateModel/getDevicesHealthy',
        method: 'post',
        data:{
          "deviceCodes": newCodes.join("、"),
          "beginDate": begin,
          "endDate": end
        }
       }).then(res => {
 
          let codeArr = []; 
          let data = res.data.result_data;

          for(let key in data){ 
            if(data[key].length>0)
              codeArr.push(key); 
          }
          //健康度的值是从后台根据device1的id作为检索条件获取的，静力水准仪也是如此，
          //但前台展示时还应显示成对的
      

          let seriesArr = new Array();
          for(let x=0;x<codeArr.length;x++){ 
                    let line =  {
                        name: device1CodeMapTwain[codeArr[x]],
                        smooth: true,
                        type: 'line',
                        itemStyle: {
                          normal: { 
                            lineStyle: { 
                              width: 2
                            }
                          }
                      }, 
                      animationDuration: 2800,
                      animationEasing: 'quadraticOut'
                    };

                  //y轴 value
                  let actualData = []; 
                  for(let i=0; i < data[codeArr[x]].length ; i++){
                    actualData.push(data[codeArr[x]][i].health_value); 
                  }
                  line['data'] = actualData; 
                  seriesArr.push(line);

                  //x轴 data range
                  if(x==0){ 
                    this.chartData2.dateRange = [];
                    for(let i=0; i < data[codeArr[x]].length ; i++){ 
                          this.chartData2.dateRange.push(data[codeArr[x]][i].calculate_time);
                    }
                  }
          

          }
          this.chartData2.actualData = seriesArr; 
          this.chart2.hideLoading();
       });


      }


    }





  }
}
</script>
