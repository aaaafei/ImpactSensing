<template>
  <el-table
    :data="tableData"
    :show-header="false"
    style="width: 100%; height:300px;"
  >
    <el-table-column
      prop="report_date"
      label="日期"
      header-align="center"
      align="center"
      width="160"
    >
    </el-table-column>

    <el-table-column
      prop="name"
      header-align="center"
      align="center"
      label="标题"
    >
      <template
        slot-scope="scope"
        v-if="scope.row.name"
      >
        <el-button
          type="text"
          size="small"
          title="点击查看"
          @click="previewFile(scope.row.full_path)"
        >
          {{scope.row.name}}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
/* eslint-disable */
export default {
  props:['tableData'],
  data() {
    return {
      
    };
  },
  methods: {
    previewFile(filePath){//页面查看附件内容
      window.open('./preview?filepath='+filePath);
    },
  },
  mounted: function() {

  }
};
</script>

<style scoped>
</style>