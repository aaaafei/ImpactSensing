<template>
  <div>
    <el-dialog title="告警处置记录" :visible.sync="dialogVisible" width="50%" :before-close="handleClose" align="center">
      <div class="dialog-main">
        <el-form ref="form" :model="form" label-width="150px">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>告警内容</span>
            </div>
            <el-form-item label="设备名称">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="线路">
              <el-input v-model="form.position"></el-input>
            </el-form-item>
            <el-form-item label="区间">
              <el-input v-model="form.position"></el-input>
            </el-form-item>
            <el-form-item label="桥墩号">
              <el-input v-model="form.position"></el-input>
            </el-form-item>
            <el-form-item label="监测项目">
              <el-input v-model="form.position"></el-input>
            </el-form-item>
            <el-form-item label="告警内容">
              <el-input type="textarea" v-model="form.content"></el-input>
            </el-form-item>
            <el-form-item label="告警时间">
              <el-col :span="12">
                <el-date-picker type="datetime" placeholder="告警时间" v-model="form.record_datetime" style="width: 100%;"></el-date-picker>
              </el-col>
            </el-form-item>
          </el-card>

          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>指派处理</span>
            </div>
            <el-form-item label="责任班组">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="班组长">
              <el-input v-model="form.position"></el-input>
            </el-form-item>
            <el-form-item label="处理人">
              <el-input v-model="form.position"></el-input>
            </el-form-item>
            <el-form-item label="指派时间">
              <el-col :span="12">
                <el-date-picker type="datetime" placeholder="时间" v-model="form.record_datetime" style="width: 100%;"></el-date-picker>
              </el-col>
            </el-form-item>
          </el-card>

          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>现场确认及申请消警</span>
            </div>
            <el-form-item label="现场图片">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="处置结果">
              <el-input v-model="form.position"></el-input>
            </el-form-item>
            <el-form-item label="消警申请人">
              <el-input v-model="form.position"></el-input>
            </el-form-item>
            <el-form-item label="申请时间">
              <el-col :span="12">
                <el-date-picker type="datetime" placeholder="时间" v-model="form.record_datetime" style="width: 100%;"></el-date-picker>
              </el-col>
            </el-form-item>
          </el-card>

          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>消警审核</span>
            </div>
            <el-table :data="deviceList" :height="200" border stripe >
              <el-table-column fixed type="index" label="序号" width="50" align="center"></el-table-column>
              <el-table-column fixed prop="lineName" label="审核人" align="center"></el-table-column>
              <el-table-column fixed prop="lineName" label="审核结果" align="center"></el-table-column>
              <el-table-column fixed prop="lineName" label="审核意见" align="center"></el-table-column>
              <el-table-column fixed prop="lineName" label="审核时间" align="center"></el-table-column>
            </el-table>
          </el-card>

        </el-form>
      </div>
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

<style scoped>
  .dialog-main {
    height: 60vh;
    overflow:auto;
  }
  .box-card {
    margin-bottom: 10px;
  }
  
  /deep/ .el-card__header {
    padding: 5px 20px;
  }
  /deep/ .el-card__body {
    padding: 5px 20px;
  }
  /deep/ .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>