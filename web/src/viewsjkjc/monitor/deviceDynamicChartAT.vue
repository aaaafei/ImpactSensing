<template>

  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="今天" name="oneDay"></el-tab-pane>
      <el-tab-pane label="查询" name="export">
        <template slot-scope='scope'>
          <el-date-picker
            v-model="value2"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
          <el-button type="primary" icon="el-icon-refresh" @click="dataRefresh">刷新</el-button>
          <span>**数据量较大，建议查询的日期范围不超过3天</span>
          <!-- <el-button type="primary" icon="el-icon-download">导出</el-button> -->
        </template>
      </el-tab-pane>
    </el-tabs>

    <!-- <el-checkbox v-model="filterDataSwitcher" :indeterminate="false" @change="dataRefresh" style="margin-left:45px;">滤波后测值</el-checkbox> -->
    <div id="deviceDataLineChartX" style="height:300px;width:100%;"></div>
    <br>
    <div id="deviceDataLineChartY" style="height:300px;width:100%;"></div>
    <br>
    <div id="deviceDataLineChartZ" style="height:300px;width:100%;"></div>
    <br>


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
  props:["deviceCode","unit","showtype"],
  data() {
    return {
      activeName: 'oneDay',
      loadLineChoose:1,
      chart: null,
      chartData: {
        actualData: [],
        dateRange: []
      },
      chart2: null,
      chartData2: {
        actualData: [],
        dateRange: []
      },
      chart3: null,
      chartData3: {
        actualData: [],
        dateRange: []
      },
      value1: '',
      value2: [],
      deviceLegends: [],
      filterDataSwitcher:true
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
    chartData3: {
      deep: true,
      handler(val) { 
        this.setOptions3(val);
      }
    },
    deviceCode: {
      handler(val) {
        let tab = {name:this.activeName}
        this.handleClick(tab);
        this.chartData= { actualData: [],  dateRange: []  }; 
        this.chartData2={  actualData: [],  dateRange: []  };
        this.chartData3={  actualData: [],  dateRange: []  };
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.chartData= {actualData:[],dateRange:[]}; 
      this.chartData2= {actualData:[],dateRange:[]}; 
      this.chartData3= {actualData:[],dateRange:[]}; 
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
      this.getOriginData(deviceCode,begin,end);
    },
    initChart() {
      this.chart = echarts.init(document.getElementById('deviceDataLineChartX'), 'macarons');
      this.setOptions(this.chartData);
      this.chart2 = echarts.init(document.getElementById('deviceDataLineChartY'), 'macarons');
      this.setOptions2(this.chartData2);
      this.chart3 = echarts.init(document.getElementById('deviceDataLineChartZ'), 'macarons');
      this.setOptions3(this.chartData3);

    }, 
    setOptions({actualData,dateRange} = {}) {
      this.chart.setOption({
        title: {
          show: true, 
          extStyle: {
            color: "grey",
            fontSize: 14
          },
          text: "X向加速度",
          left: "center",
          top: "top"
        }, 
        color:['#00a8e1','#99cc00','#fcd300','#800080','#00994e','#ff6600','#808000','#db00c2','#008080','#0000ff','#c8cc00',
          '#3682be','#45a776','#f05326','#eed777','#334f65','#b3974e','#38cb7d','#ddae33','#844bb3','#93c555','#5f6694','#df3881'],
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
            }
          },
          name:'单位('+this.unit+')',
          type: 'value',
          splitLine: {
            show: false
          },
          scale:true
        },
        legend: { 
          data: this.deviceLegends
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
        series: {
          data:actualData,
          type:'line',
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }
      },true);
    },
    setOptions2({actualData,dateRange} = {}) {
      this.chart2.setOption({
        title: {
          show: true, 
          extStyle: {
            color: "grey",
            fontSize: 14
          },
          text: "Y向加速度",
          left: "center",
          top: "top"
        }, 
        color:['#00a8e1','#99cc00','#fcd300','#800080','#00994e','#ff6600','#808000','#db00c2','#008080','#0000ff','#c8cc00',
          '#3682be','#45a776','#f05326','#eed777','#334f65','#b3974e','#38cb7d','#ddae33','#844bb3','#93c555','#5f6694','#df3881'],
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
            }
          },
          name:'单位('+this.unit+')',
          type: 'value',
          splitLine: {
            show: false
          },
          scale:true
        },
        legend: { 
          data: this.deviceLegends
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
        series: {
          data:actualData,
          type:'line',
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }
      },true);
    },
    setOptions3({actualData,dateRange} = {}) {
      this.chart3.setOption({
        title: {
          show: true, 
          extStyle: {
            color: "grey",
            fontSize: 14
          },
          text: "Z向加速度",
          left: "center",
          top: "top"
        },  
        color:['#00a8e1','#99cc00','#fcd300','#800080','#00994e','#ff6600','#808000','#db00c2','#008080','#0000ff','#c8cc00',
          '#3682be','#45a776','#f05326','#eed777','#334f65','#b3974e','#38cb7d','#ddae33','#844bb3','#93c555','#5f6694','#df3881'],
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
            }
          },
          name:'单位('+this.unit+')',
          type: 'value',
          splitLine: {
            show: false
          },
          scale:true
        },
        legend: { 
          data: this.deviceLegends
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
        series: {
          data:actualData,
          type:'line',
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }
      },true);
    },
    getOriginData(deviceCode,begin,end){  
      if(deviceCode!=''){
        this.chart.showLoading({
          text: '数据正在加载...',
          textStyle: { fontSize : 30 , color: '#444' },
          effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
        });
        this.chart2.showLoading({
          text: '数据正在加载...',
          textStyle: { fontSize : 30 , color: '#444' },
          effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
        });
        this.chart3.showLoading({
          text: '数据正在加载...',
          textStyle: { fontSize : 30 , color: '#444' },
          effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
        });

       this.$request({
          url: '/tmOriginData/getEarthquakeData',
          method: 'post',
          data:{
            "device_code": deviceCode,
            "beginDate": begin,
            "endDate": end,
            "needDataFilter": this.filterDataSwitcher
          }
       }).then(res => {
          let codeArr = deviceCode.split('、'); 
          let data = res.data.result_data;
          
          this.deviceLegends = codeArr;
          if (data.length<1) return;
          let line =  {
              name: codeArr[0],
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
          
          let seriesArrX = data[codeArr[0]].map(function(item) {
            return item.x_value;
          });
          let seriesArrY = data[codeArr[0]].map(function(item) {return item.y_value});
          let seriesArrZ = data[codeArr[0]].map(function(item) {return item.z_value});
          let seriesArrDatetime = data[codeArr[0]].map(function(item) {return item.collect_time});
          
          this.chartData.actualData = seriesArrX; 
          this.chartData2.actualData = seriesArrY; 
          this.chartData3.actualData = seriesArrZ; 
          this.chartData.dateRange = seriesArrDatetime;
          this.chartData2.dateRange = seriesArrDatetime;
          this.chartData3.dateRange = seriesArrDatetime;
          // console.log(seriesArrX);
          this.chart.hideLoading();
          this.chart2.hideLoading();
          this.chart3.hideLoading();
       });
      }
    },
    

  }
}
</script>
