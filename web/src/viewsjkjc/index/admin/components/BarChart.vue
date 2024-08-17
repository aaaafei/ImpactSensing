<template>
  <div>
    <el-row :gutter="20" type="flex" align="top" style="height:140px;" >
      <el-col :span="12" :offset="0">
        <div style="height:140px;" ref="pieChart"></div>
      </el-col>
      <el-col :span="12" :offset="0">
        <div class="title">运行状态概览</div>
        <hr>
        <div class="container">
          <img src="../../../../../static/images/project/home-device.png" alt="" class="centered-image" style="height: 15px;">
          <span class="centered-text">设备总数：2</span>
        </div>
        <div class="container">
          <img src="../../../../../static/images/project/home-connected.png" alt="" class="centered-image" style="height: 15px;">
          <span class="centered-text">已连接数：2</span>
        </div>
      </el-col>
    </el-row>
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
      
    }
  },
  watch: {

  },
  mounted() {
    this.$nextTick(() => {
      this.requestLoadData();
    });
  },
  beforeDestroy() {
    
  },
  methods: {
    requestLoadData() {
      this.initChart();
    },
    initChart() {
      let dChart = echarts.init(this.$refs.pieChart);
      let pass_rate = 100;
      let total = 2;
      dChart.setOption({
        grid: {
          top:0,
          bottom:0,
          height: '100%'
        },
        tooltip: {
          show: false,
          trigger: 'item',
          formatter: "{a} : {c} ({d}%)"
        },
        color: ['#1684FC'],
        series: [
          {
            name: '设备正常工作率',
            type: 'pie',
            radius: ['50%', '60%'],
            center: ['50%', '30%'],
            avoidLabelOverlap: false,
            //环的位置
            label: {
              show: true,
              position: 'center',
              color:'#4c4a4a',
              formatter: '{pass_rate|' + pass_rate +'%}'+ '\n\r' + '{desc|设备正常\n工作率}',
              rich: {
                pass_rate:{
                  fontSize: 20,
                  fontFamily : "微软雅黑",
                  color:'#1684FC'
                },
                desc: {
                  fontFamily : "微软雅黑",
                  fontSize: 12,
                  color:'#6c7a89',
                  lineHeight:14,
                },
              }
            },
            emphasis: {//中间文字显示
              show: true,
            },
            data: [
              {
                value: pass_rate, //需要显示的数据
                name: '设备正常工作率' + pass_rate + '%'
              },
              {
                value: total - pass_rate,
                //不需要显示的数据，颜色设置成和背景一样
                itemStyle: {
                  normal: {
                    color: '#eff2f4'//'transparent'
                  }
                }
              }
            ]
          },
        ]
      });
    }
  }
};

</script>
<style scoped>
#up-div {
  width: 150px;
  height: 10px;
  top: -2px;
  left: 20px;
  position: absolute;
  z-index: 999;
  padding: 0px 15px;
  border: 1px solid white;
  background-color: white;
}
.title {
  font-size: 16;
  font-weight: bold;
  font-family: "微软雅黑";
  color:'#1684FC';
}

</style>
