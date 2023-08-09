<template>
  <div>
    <el-table
      :data="tableData"
      :show-header="true"
      style="width: 100%; height:300px;">
      <el-table-column prop="deviceCode" label="设备编号"  align="center"></el-table-column> 
      <el-table-column prop="monitorType" label="监测项目" width="105" align="center"></el-table-column> 
      <!-- <el-table-column prop="segment" label="监测区间段"  width="90" align="center"></el-table-column>  -->
      <el-table-column prop="stakeNumber" label="桩号"  width="78" align="center"></el-table-column>
      <!-- <el-table-column prop="installPosition" label="安装位置" align="center"></el-table-column> -->
      <!-- <el-table-column prop="val" label="设备读数" align="center"></el-table-column>    -->
      <el-table-column prop="collectTime" label="采集时间" width="140" align="center"></el-table-column>
      <!-- <el-table-column prop="warningValue" label="黄色预警值" align="center"> -->
        <!-- <template slot-scope='scope'> -->
          <!-- {{scope.row.warningValue*0.8 + unitMap[scope.row.monitorType]}} -->
        <!-- </template> -->
      <!-- </el-table-column> -->
      <!-- <el-table-column prop="warningValue" label="红色预警值" align="center"> -->
        <!-- <template slot-scope='scope'> -->
          <!-- {{scope.row.warningValue + unitMap[scope.row.monitorType]}} -->
        <!-- </template> -->
      <!-- </el-table-column> -->
      <el-table-column prop="warningLevel" label="预警等级" width="78" align="center"> 
      </el-table-column> 
    </el-table>

  </div>
</template>

<script>
/* eslint-disable */
export default {
  data() {
    return {
      tableData: []
    };
  },
  methods: {
    getTableData() {
      this.$request({
        url:'tmWarningRecords/getPageList/1/5',
        method:'post',
        data: {}
      }).then(res => {
        let dataList = res.data.result_data.list;
        // this.page.total = res.data.result_data.total;
        this.tableData = dataList;
        // console.log(dataList);
      });
    }
  },
  mounted: function() {
    this.getTableData();
  }
};
</script>

<style scoped>

</style>