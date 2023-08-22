<template>
  <div class="dashboard-editor-container">
    <el-row :gutter="20">
      <el-col :span="18" :offset="0">
        <tunnel-map></tunnel-map>
      </el-col>
      <el-col :span="6" :offset="0">
        <bar-chart></bar-chart>
        <hr>
        <report-table></report-table>
        <div></div>
      </el-col>
    </el-row>
    


  </div>
</template>

<script>
/* eslint-disable */
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import LineChartTwo from './components/LineChartTwo'
import BarChart from './components/BarChart'
import TunnelMap from './components/TunnelMap'
import ThreeDimensionMap from './components/ThreeDimensionMap'
import ReportTable from './components/ReportTable'
import PanelNav from './components/PanelNav'


export default {
  name: 'DashboardAdmin',
  components: {
    PanelGroup,
    LineChart,
    LineChartTwo,
    BarChart,
    TunnelMap,
    ThreeDimensionMap,
    ReportTable,
    PanelNav
  },
  data() {
    return {
      activeMapName: 'first',
      activeChartName: 'first',
      activeInfo1Name: 'first',
      activeInfo2Name: 'first',
      isCompentNeed: false,
      lineChartRefresh: false,
      lineChart2Refresh: false,
      barChartRefresh: false,
      reportData:[],
      currentBglx:'1',
    }
  },
  methods: {
    initChart(){
      if(this.activeChartName == 'first') {
        this.lineChartRefresh = true;
      }else if (this.activeChartName == 'second') {
        this.lineChart2Refresh = true;
      }else if (this.activeChartName == 'third') {
        this.barChartRefresh = true;
      }
    },
    handleChartTab(tab,event) {
      if (tab.name == 'first') {
        this.lineChartRefresh = true;
      }else if (this.activeChartName == 'second') {
        this.lineChart2Refresh = true;
      }else if (this.activeChartName == 'third') {
        this.barChartRefresh = true;
      }
    },
    pageTo(path, query) {
      this.$router.push({ path: path, query: query });
    },
  },
  mounted: function() {

  }
}
</script>

<style  scoped>
.dashboard-editor-container {
  padding: 5px 10px;
  background-color: rgb(240, 242, 245);
  position: relative;
  height: calc(100vh - 200px);
}


</style>
