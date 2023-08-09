<template>
  <div>
    <div
      id="chart"
      :class="className"
      :style="{height:height,width:width}"
    />
    <div id="up-div">
      <el-switch
        v-model="switchValue" 
        active-text="右线"
        inactive-text="左线">
      </el-switch>
    </div>
  </div>
</template>

<script>
/* eslint-disable */
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme 
import { dateUtil } from '@/assets/utils/dateUtil';

export default { 
  data() {
    return {
      className:  "chart" ,
      width:   "100%" ,
      height:  "500px" , 
      switchValue:false,
      chart: null,
      chartData: {
        x_axis: ["3:15:00","3:15:10","3:15:20","3:15:30","3:15:40","3:15:50","3:16:00"],
        segment_1: [110, 92, 71, 154, 162, 140, 145],
        segment_2: [110, 72, 91, 114, 152, 140, 145],
        segment_3: [120, 82, 81, 124, 142, 140, 145],
        segment_4: [120, 72, 91, 134, 152, 140, 145],
        segment_5: [130, 92, 81, 154, 122, 140, 145],
        segment_6: [130, 82, 71, 164, 132, 140, 145]
      },
      chartRandomData: {
        x_axis: [],
        segment_1: [],
        segment_2: [],
        segment_3: [],
        segment_4: [],
        segment_5: [],
        segment_6: []
      },
      itvl: null,
      colorStatus: {
            A: "#008000",
            B: "#3888fa",
            C: "#FFA500",
            D: '#FF0000',
            E: '#800000'
          }
    };
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val);
      }
    }, 
    switchValue: { 
      handler(val) {
        this.randomData();
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      let _this = this;
      this.randomData();
      setTimeout(this.mock(), 1000*10); 
    });

  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
    window.clearInterval(this.itvl);
  },
  methods: { 
    randomData() {
    
      let segment =[
        "浦口大堤",
        "浦口竖井",
        "江中",
        "覆土厚度最小",
        "梅子洲大堤",
        "梅子洲竖井"
      ]; 
      const today = new dateUtil();
      let begin;
      let end; 
      begin = today.getYearMonthDay();
      let nextDay = new dateUtil(today.getTime() + 1 * today.getOneDayMillis());
      end = nextDay.getYearMonthDay();
      let line=null;
      if(this.switchValue){
        line = "R";
      }
      else{
        line = "L";
      }
      this.$request({
        url:'safety/getHistoryData',
        method:'post',
        params: {'begin_time': begin, 'end_time': end,'line': line}
      }).then(res => {  
        let x_axis= [];
        let segment_1= [];
        let segment_2= [];
        let segment_3= [];
        let segment_4= [];
        let segment_5= [];
        let segment_6= [];
        let list = res.data.result_data;
        for(var i=0;i<list.length;i++){ 
          if(list[i].segment_name==segment[0]) { 
            segment_1.push(list[i].safety_value);
          }
          if(list[i].segment_name==segment[1]) {
            segment_2.push(list[i].safety_value); 
            x_axis.push(list[i].collect_time);
          }
          if(list[i].segment_name==segment[2]) segment_3.push(list[i].safety_value);
          if(list[i].segment_name==segment[3]) segment_4.push(list[i].safety_value);
          if(list[i].segment_name==segment[4]) segment_5.push(list[i].safety_value);
          if(list[i].segment_name==segment[5]) segment_6.push(list[i].safety_value);
        } 
        this.chartRandomData.x_axis= x_axis; 
        this.chartRandomData.segment_1= segment_1;
        this.chartRandomData.segment_2= segment_2;
        this.chartRandomData.segment_3= segment_3;
        this.chartRandomData.segment_4= segment_4;
        this.chartRandomData.segment_5= segment_5;
        this.chartRandomData.segment_6= segment_6;  
        this.initChart();
      });


    },
    mock() {
      let _this = this;
      this.itvl =setInterval(function () {
        _this.randomData(); 
      }, 1000*10);
    },
    initChart() { 
      if(this.chart==null)  this.chart = echarts.init(document.getElementById('chart'), "macarons");
      // this.setOptions(this.chartData);
      this.setOptions(this.chartRandomData);
    },
    setOptions() {
      if(this.chart==null)  this.chart = echarts.init(document.getElementById('chart'), "macarons");
      this.chart.setOption({
        xAxis: {
          data: this.chartRandomData.x_axis,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 40,
          right: 100,
          bottom: 20,
          top: 60,
          containLabel: true
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross"
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
           name: '安全度值',
           type: 'value',
           splitLine: {
               show: false
           },
           scale:false,
        },  
        legend: {
          data: [
            "浦口大堤",
            "浦口竖井",
            "江中",
            "覆土厚度最小",
            "梅子洲大堤",
            "梅子洲竖井"
          ]
        },
        visualMap: {
            top: 0,
            right: 1,
            precision:1,
            pieces: [{
                gt: 4.2,
                lte: 5.0,
                color: this.colorStatus['A']
            }, {
                gt: 3.4,
                lte: 4.2,
                color: this.colorStatus['B']
            }, {
                gt: 2.6,
                lte: 3.4,
                color: this.colorStatus['C']
            }, {
                gt: 1.8,
                lte: 2.6,
                color: this.colorStatus['D']
            }, {
                gt: 1.0,
                lte: 1.8,
                color: this.colorStatus['E']
            }],
            outOfRange: {
                color: '#999'
            }
        },
        dataZoom: [{
            type: 'inside',
            start: 0,
            end: 100
        },{
            start: 0,
            end: 10,
            handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
            handleSize: '80%',
            handleStyle: {
                color: '#fff',
                shadowBlur: 3,
                shadowColor: 'rgba(0, 0, 0, 0.6)',
                shadowOffsetX: 2,
                shadowOffsetY: 2
            }
        }],
        series: [
          {
            name: "浦口大堤",
            itemStyle: {
              normal: {
                color: "#FF005A",
                lineStyle: {
                color: "#FF005A",
                  width: 2
                }
              }
            },
            smooth: true,
            type: "line",
            data: this.chartRandomData.segment_1,
            animationDuration: 2800,
            animationEasing: "cubicInOut"
          },
          {
            name: "浦口竖井",
            smooth: true,
            type: "line",
            itemStyle: {
              normal: {
                color: "#ff9900",
                lineStyle: {
                  color: "#ff9900",
                  width: 2
                },
                areaStyle: {
                  color: "#f3f8ff"
                }
              }
            },
            data: this.chartRandomData.segment_2,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          },
          {
            name: "江中",
            smooth: true,
            type: "line",
            itemStyle: {
              normal: {
                color: "#ffff00",
                lineStyle: {
                  color: "#ffff00",
                  width: 2
                },
                areaStyle: {
                  color: "#f3f8ff"
                }
              }
            },
            data: this.chartRandomData.segment_3,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          },
          {
            name: "覆土厚度最小",
            smooth: true,
            type: "line",
            itemStyle: {
              normal: {
                color: "#006633",
                lineStyle: {
                  color: "#006633",
                  width: 2
                },
                areaStyle: {
                  color: "#f3f8ff"
                }
              }
            },
            data: this.chartRandomData.segment_4,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          },
          {
            name: "梅子洲大堤",
            smooth: true,
            type: "line",
            itemStyle: {
              normal: {
                color: "#3888fa",
                lineStyle: {
                  color: "#3888fa",
                  width: 2
                },
                areaStyle: {
                  color: "#f3f8ff"
                }
              }
            },
            data: this.chartRandomData.segment_5,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          },
          {
            name: "梅子洲竖井",
            smooth: true,
            type: "line",
            itemStyle: {
              normal: {
                color: "#6633ff",
                lineStyle: {
                  color: "#6633ff",
                  width: 2
                },
                areaStyle: {
                  color: "#f3f8ff"
                }
              }
            },
            data: this.chartRandomData.segment_6,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          }
        ]
      });
    }
  }
};
</script>
<style scoped>
#up-div{
  width:150px;
  height:10px;
  top:-2px;
  left:20px;
  position:absolute;
  z-index:999;
  padding: 0px 15px;
  border:1px solid white;
  background-color:white;
}
</style>
