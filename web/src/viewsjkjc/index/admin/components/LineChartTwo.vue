<template>
  <div
    :class="className"
    :style="{height:height,width:width}"
  />
</template>

<script>
/* eslint-disable */
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "chart"
    },
    width: {
      type: String,
      default: "100%"
    },
    height: {
      type: String,
      default: "500px"
    },
    autoResize: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
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
        x_axis: ["3:15:00","3:15:10","3:15:20","3:15:30","3:15:40","3:15:50","3:16:00"],
        segment_1: [110, 92, 71, 154, 162, 140, 143],
        segment_2: [110, 72, 91, 114, 152, 130, 125],
        segment_3: [120, 82, 81, 124, 142, 120, 165],
        segment_4: [120, 72, 91, 134, 152, 146, 145],
        segment_5: [130, 92, 81, 154, 122, 135, 165],
        segment_6: [130, 82, 71, 164, 132, 142, 105]
      }
    };
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val);
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      let _this = this;
      this.initChart();
      setTimeout(this.mock(), 1000*10);
      

    });
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    randomData() {
      let now = new Date();
      let mtime = [now.getHours(), now.getMinutes(), now.getSeconds()].join(':');
      this.chartRandomData.x_axis.push(mtime);
      this.chartRandomData.segment_1.push(parseInt(Math.random()*(180-50+1)+50,10));
      this.chartRandomData.segment_2.push(parseInt(Math.random()*(180-50+1)+50,10));
      this.chartRandomData.segment_3.push(parseInt(Math.random()*(180-50+1)+50,10));
      this.chartRandomData.segment_4.push(parseInt(Math.random()*(180-50+1)+50,10));
      this.chartRandomData.segment_5.push(parseInt(Math.random()*(180-50+1)+50,10));
      this.chartRandomData.segment_6.push(parseInt(Math.random()*(180-50+1)+50,10));
    },
    mock() {
      let _this = this;
      setInterval(function () {
        _this.randomData();
        _this.setOptions(_this.chartRandomData);
      }, 1000*10);
    },
    initChart() {
      this.chart = echarts.init(this.$el, "macarons");
      // this.setOptions(this.chartData);
      this.setOptions(this.chartRandomData);
    },
    setOptions({
      x_axis,
      segment_1,
      segment_2,
      segment_3,
      segment_4,
      segment_5,
      segment_6
    } = {}) {
      this.chart.setOption({
        xAxis: {
          data: x_axis,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
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
          axisTick: {
            show: false
          }
        },
        legend: {
          data: [
            "管片应力",
            "管片倾斜与偏转",
            "接缝张开度",
            "管片错台",
            "纵向不均匀沉降",
            "震动相应"
          ]
        },
        visualMap: {
            top: 10,
            right: 10,
            pieces: [{
                gt: 0,
                lte: 50,
                color: '#096'
            }, {
                gt: 50,
                lte: 100,
                color: '#ffde33'
            }, {
                gt: 100,
                lte: 150,
                color: '#ff9933'
            }, {
                gt: 150,
                lte: 200,
                color: '#cc0033'
            }],
            outOfRange: {
                color: '#999'
            }
        },
        // dataZoom: [{
        //     type: 'inside',
        //     start: 0,
        //     end: 10
        // },{
        //     start: 0,
        //     end: 10,
        //     handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
        //     handleSize: '80%',
        //     handleStyle: {
        //         color: '#fff',
        //         shadowBlur: 3,
        //         shadowColor: 'rgba(0, 0, 0, 0.6)',
        //         shadowOffsetX: 2,
        //         shadowOffsetY: 2
        //     }
        // }],
        series: [
          {
            name: "管片应力",
            itemStyle: {
              normal: {
                // color: "#FF005A",
                lineStyle: {
                  // color: "#FF005A",
                  width: 2
                }
              }
            },
            smooth: true,
            type: "line",
            data: segment_1,
            animationDuration: 2800,
            animationEasing: "cubicInOut",
            markLine: {
              silent: true,
              data: [
                {
                  yAxis: 50
                },
                {
                  yAxis: 100
                },
                {
                  yAxis: 150
                }
              ]
            }
          },
          {
            name: "管片倾斜与偏转",
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
            data: segment_2,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          },
          {
            name: "接缝张开度",
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
            data: segment_3,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          },
          {
            name: "管片错台",
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
            data: segment_4,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          },
          {
            name: "纵向不均匀沉降",
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
            data: segment_5,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          },
          {
            name: "地震动",
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
            data: segment_6,
            animationDuration: 2800,
            animationEasing: "quadraticOut"
          }
        ]
      });
    }
  }
};
</script>
