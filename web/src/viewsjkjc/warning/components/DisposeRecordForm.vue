<template>
  <div>
    <el-dialog title="处置记录" :visible.sync="dialogVisible" width="75%" :before-close="handleClose">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="设备名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="安装位置">
          <el-input v-model="form.position"></el-input>
        </el-form-item>
        <el-form-item label="告警内容">
          <el-input type="textarea" v-model="form.content"></el-input>
        </el-form-item>
        <el-form-item label="处置措施">
          <el-input type="textarea" v-model="form.dispose_content"></el-input>
        </el-form-item>
        <el-form-item label="处置人员">
          <el-select v-model="form.user" placeholder="请选择处置人员">
            <el-option label="陈飞" value="u001"></el-option>
            <el-option label="陈飞" value="u002"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处置时间">
          <el-col :span="12">
            <el-date-picker type="datetime" placeholder="处置时间" v-model="form.record_datetime" style="width: 100%;"></el-date-picker>
          </el-col>
        </el-form-item>

        <!-- <el-form-item>
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button>取消</el-button>
        </el-form-item> -->
      </el-form>
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
      form: {},
    };
  },
  watch: {
    disposeRecordDialog(newv, oldv) {
      this.dialogVisible = newv;
    }
  },
  methods: {
    handleClose(done) {
      this.$emit("changeDisposeRecordDialog", false);
    },
    getInfo() {
      this.form = {
        name:"设备01",
        position:"马群站076号桥墩",
        content:"设备离线",
        dispose_content:"重新启动，已恢复工作状态",
        user:"u001",
        record_datetime:"2023-08-22 14:38"
      }
    },
  },
  mounted: function () {
    this.getInfo();
  }
};
</script>

<style scoped></style>