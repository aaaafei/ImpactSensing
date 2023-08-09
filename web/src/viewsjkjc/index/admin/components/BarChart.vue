<template>
  <div>

    <el-row>
      <el-col :span="4">
    <div
      id="chart1"
      :class="className"
      :style="{height:height,width:width}"
    />
  </el-col>

      <el-col :span="4">
        <div
      id="chart2"
      :class="className"
      :style="{height:height,width:width}"
    />
  </el-col>
  </el-col>

      <el-col :span="4">
        <div
      id="chart3"
      :class="className"
      :style="{height:height,width:width}"
    />
  </el-col>
  </el-col>

      <el-col :span="4">
        <div
      id="chart4"
      :class="className"
      :style="{height:height,width:width}"
    />
  </el-col>
  </el-col>

      <el-col :span="4">
        <div
      id="chart5"
      :class="className"
      :style="{height:height,width:width}"
    />
  </el-col>
  </el-col>

      <el-col :span="4">
        <div
      id="chart6"
      :class="className"
      :style="{height:height,width:width}"
    />
  </el-col>
   </el-row>

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
import echarts from 'echarts';
require('echarts/theme/macarons'); // echarts theme

const animationDuration = 2000;

export default {
  data() {
    return {
      className:  "chart" ,
      width:   "100%" ,
      height:  "500px" ,
      switchValue:true,
      chart: {},
      segment: [
        "浦口大堤",
        "浦口竖井",
        "江中",
        "覆土厚度最小",
        "梅子洲大堤",
        "梅子洲竖井"
      ]
    }
  },
  watch: { 
    switchValue: { 
      handler(val) { 
        this.requestLoadData();
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
        this.requestLoadData();
    });
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }  

    for(var key in this.chart){  
　　　　this.chart[key].dispose();
　　}
    this.chart = null
  },
  methods: {
    requestLoadData(){
      let url = '/tmDevice/getDeviceStatus/';
      let param = {};
      if(this.switchValue){
        param["line"] = "右线";
      }
      else{
        param["line"] = "左线";
      }
          this.$request({
            url:url,
            method:'post', 
            data: param
          }).then(res => {   
            let list = res.data.result_data; 
            let status = {};
            for(let i=0;i<list.length;i++){
              for(let j=0;j<this.segment.length;j++){
                if(list[i].segment==this.segment[j]){
                    let t = {type: list[i].type,num: list[i].normalRate}; 
                    if(status[this.segment[j]]==undefined || status[this.segment[j]]==null) status[this.segment[j]]= new Array();
                    status[this.segment[j]].push(t);
                } 
              } 
            }
            
            for(let j=0;j<this.segment.length;j++){
              var jsontext=JSON.stringify(status[this.segment[j]]);
              this.initChart('chart'+(j+1),this.segment[j],jsontext);
            }

          }); 
    },
    initChart(id,segment,jsontext) {
      if(this.chart[id]==null)  this.chart[id] = echarts.init(document.getElementById(id), "macarons"); 
      let json=JSON.parse(jsontext);  
      let xAxisArr = [segment];
      let seriesArr = [];
      for(let i = 0;i<json.length;i++){
        let tmp = {};
        tmp.name = json[i].type;
        tmp.type="bar";
        tmp.stack = "vistors";
        tmp.barWidth="60%";
        tmp.data=[json[i].num];
        tmp.itemStyle = {
          normal: {
              label: {
                show: true,
                position: 'inside',
                formatter: '{a}\n{c}%'
              }
            }
          },animationDuration;
          seriesArr.push(tmp);
      }
      


      this.chart[id].setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          left: 10,
          right: 0,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: xAxisArr,
          axisTick: {
            alignWithLabel: true
          }
        }],
        yAxis: [{
          type: 'value',
          max: function(value){
            return value.max;
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            show: true,
            interval: 'auto', 
            formatter: function (value, index) { 
              return [(value/seriesArr.length).toFixed(1), '%'].join('');
            }
          }
        }],
        series:seriesArr 
      },true);
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
