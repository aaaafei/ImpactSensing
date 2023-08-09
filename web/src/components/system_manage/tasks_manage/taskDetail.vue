<template>
  <div>
      <el-form  :model="form"  label-width="120px" >
        <el-form-item label="执行地址："  prop="address" >
          <el-input  v-model="form.address" readonly />
        </el-form-item>
        <el-form-item label="参数：" prop="params" >
          <el-input v-model="form.params" readonly />
        </el-form-item>
        <el-form-item label="cron表达式：" prop="cron">
          <el-input v-model="form.cron" readonly />
        </el-form-item>
        <el-form-item label="信息：" prop="msg">
          <el-input type="textarea" v-model="form.msg" readonly />
        </el-form-item>
        <el-form-item label="结果：" prop="resultName">
          <el-input v-model="form.resultName" readonly />
        </el-form-item>
        <el-form-item label="任务类型：" prop="typeName">
          <el-input v-model="form.typeName" readonly />
        </el-form-item>
        <el-form-item label="开始时间：" prop="startTime">
          <el-input v-model="form.startTime" readonly />
        </el-form-item>
        <el-form-item label="结束时间：" prop="endTime">
          <el-input v-model="form.endTime" readonly />
        </el-form-item>
        <el-form-item label="备注：" prop="remark">
          <el-input v-model="form.remark" readonly />
        </el-form-item>
      </el-form>
  </div>
</template>

<script>
  export default {
    name: 'taskDetailBox',
    props: ['detailForm'],
    data: function () {
      return {
        form: this.detailForm
      };
    },
    methods: {
      close () {
        this.$emit('headCallBackDetail', '');
      }
    },
    mounted: function () {
    }
  };
</script>

<style type="stylus">

</style>
