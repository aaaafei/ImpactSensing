<template>
  <div id="queryOverhaul1">
    <el-dialog title="查看工单" :visible="queryVisible" :before-close="handleClose" id="queryOverhaulDailogId1">
      <el-form  ref="form" :model="form" label-width="100px" >
        <el-form-item label="工单编号：" for="code">
        <el-input
          id="code"
          v-model="form.code"
          readonly
          placeholder="无工单编号"/>
      </el-form-item>
        <el-form-item label="工单名称：" for="orderName">
          <el-input
            id="orderName"
            readonly
            v-model="form.name"
            placeholder="无工单名称"/>
        </el-form-item>
        <el-form-item label="工单来源：" for="source">
          <el-input
            id="orderName"
            readonly
            v-model="form.workFromName"
            placeholder="无工单来源"/>
        </el-form-item>
        <el-form-item label="检修部门：" for="depart">
          <el-input
            id="depart"
            readonly
            v-model="form.orgName"
            placeholder="无检修部门"/>
        </el-form-item>
        <el-form-item label="开始时间：" for="time">
          <el-date-picker
            id="time"
            type="datetime"
            v-model="form.startTime"
            readonly
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="无开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="完成期限：" for="time">
          <el-date-picker
            id="time"
            type="datetime"
            v-model="form.endTime"
            format="yyyy-MM-dd HH:mm:ss"
            readonly
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="无完成期限">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="检修设备：" for="equips">
          <el-input
            id="equips"
            readonly
            v-model="form.deviceName"
            placeholder="无检修设备"/>
        </el-form-item>
        <el-form-item label="巡检分区：" for="place">
          <el-input
            id="place"
            readonly
            v-model="form.comptName"
            placeholder="无巡检分区"/>
        </el-form-item>
        <el-form-item label="备注：" for="comments">
          <el-input
            id="comments"
            readonly
            type="textarea"
            :rows="3"
            v-model="form.remark"
            placeholder="无备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose('form')">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import $ from 'jquery';
export default {
  name: 'queryOverhaul1',
  props: ['queryVisible', 'propForm', 'title'],
  data: function () {
    return {
      queryVisible2: this.queryVisible, // 页面可见性
      form: this.propForm
    };
  },
  methods: {
    handleClose (done) {
      this.auditVisible2 = false;
      this.$emit('headCallBack', this.auditVisible2); // 给父组件传值，隐藏弹出框
    },
    close (formName, flag) {
      this.$refs[formName].resetFields(); // 清空验证信息
      this.queryVisible2 = false;
      this.$emit('headCallBack', this.queryVisible2); // 给父组件传值，隐藏弹出框
    }
  },
  mounted: function () {
    this.$nextTick(function () {
    });
    $('.v-modal').css('z-index', '0 !important');
  }
};
</script>

<style>
#queryOverhaulDailogId1  .el-dialog__body{
  height: 300px;
  overflow: auto;
}
#queryOverhaul1 .el-dialog {
  width: 50%;
}
#queryOverhaul1 .el-dialog__wrapper{
  z-index: 99999;
  background-color:rgba(0, 0, 0, 0.5);
}
#queryOverhaul1 .el-dialog .el-form {
  width: 100%;
  margin: 0px auto;
}
</style>
