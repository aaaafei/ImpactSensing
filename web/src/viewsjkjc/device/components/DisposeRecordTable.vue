<template>
  <div>
    <el-dialog title="处置记录" :visible.sync="dialogVisible" width="75%" :before-close="handleClose">
      <el-table :data="tableData" :height="550" border stripe 
        :header-cell-style="{ background: '#A1B6D8', color: '#fff' }">
        <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
        <el-table-column prop="content" label="处置内容" align="center"></el-table-column>
        <el-table-column prop="username" label="处置人" width="280" align="center"></el-table-column>
        <el-table-column prop="record_datetime" label="处置时间" align="center"></el-table-column>
      </el-table>
      <el-pagination :current-page="page.pageNum" :page-sizes="[10, 20, 50]" :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="float: left;"></el-pagination>

    </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */
export default {
  props: ["disposeRecordDialog"],
  data() {
    return {
      dialogVisible: this.disposeRecordDialog,
      page: {
        pageNum: 1,
        pageSize: 50,
        total: 15
      },
      tableData: [],
    };
  },
  watch:{
    disposeRecordDialog(newv, oldv) {
      this.dialogVisible = newv;
    }
  },
  methods: {
    handleClose(done) {
      this.$emit("changeDisposeRecordDialog", false);
    },
    getInfo() {
      this.tableData = [
        
      ]
    },
  },
  mounted: function () {
    this.getInfo();
  }
};
</script>

<style scoped></style>