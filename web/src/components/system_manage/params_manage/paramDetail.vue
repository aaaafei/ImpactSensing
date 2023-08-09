<template>
  <div>
    <el-dialog title="参数详情" @close="close('')" :visible="detailVisible">
      <el-form ref="detailParamform" :model="form"  label-width="100px" >
        <el-form-item label="参数名："  prop="paramKey" >
          <el-input  v-model="form.paramKey" readonly />
        </el-form-item>
        <el-form-item label="参数值：" prop="paramValue" >
          <el-input v-model="form.paramValue" readonly />
        </el-form-item>
        <el-form-item label="参数设置类：" prop="clazz">
          <el-input v-model="form.clazz" readonly />
        </el-form-item>
        <el-form-item label="备注：" prop="remark">
          <el-input v-model="form.remark" readonly />
        </el-form-item>
        <!--<el-form-item label="状态：" prop="state">
          <el-input v-model="form.state" readonly />
        </el-form-item>
        <el-form-item label="创建时间：" prop="createTime">
          <el-input v-model="form.createTime" readonly />
        </el-form-item>
        <el-form-item label="创建人：" prop="createUser">
          <el-input v-model="form.createUser" readonly />
        </el-form-item>
        <el-form-item label="更新时间：" prop="updateTime">
          <el-input v-model="form.updateTime" readonly />
        </el-form-item>
        <el-form-item label="更新人：" prop="updateUser">
          <el-input v-model="form.updateUser" readonly />
        </el-form-item>-->
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'paramDetailBox',
    props: ['detailVisible', 'detailForm'],
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
