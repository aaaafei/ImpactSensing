<template>
  <div > 
    <div id="chart1" :class="className" :style="{height:height,width:width}" /> 
  </div>
</template>

<script>
/* eslint-disable */
import echarts from 'echarts';
require('echarts/theme/macarons'); // echarts theme

const animationDuration = 2000;

export default { 
  props:['treeData'],
  data() {
    return {
      className:  "chart" ,
      width:   "100%" ,
      height:  "650px",
      data: this.treeData
    }
  }, 
  mounted() {
    this.$nextTick(() => {
        this.requestLoadData();
    });
  }, 
  methods: {
    requestLoadData(){ 
        var myChart = echarts.init(document.getElementById('chart1'), "macarons");
        var data = this.data;  
        var option = {
            tooltip: {
              trigger: 'item',
              triggerOn: 'mousemove'
            }, 
            series:[
                {
                    type: 'tree', 
                    data: [data], 
                    left: '1%',
                    right: '1%',
                    top: '18%',
                    bottom: '40%', 
                    symbol: 'emptyCircle', 
                    orient: 'vertical', 
                    expandAndCollapse: true, 
                    label: {
                        position: 'top',
                        rotate: -90,
                        verticalAlign: 'middle',
                        align: 'right',
                        fontSize: 20
                    },

                    leaves: {
                        label: {
                            position: 'bottom',
                            rotate: -90,
                            verticalAlign: 'middle',
                            align: 'left'
                        }
                    }, 
                    animationDurationUpdate: 750
                }
            ]
        };
        myChart.setOption(option);
    } 
  },
  watch:{ 
      treeData(value,oldvalue){
          this.data = value;
           this.requestLoadData();
      }
  }
};

</script>

