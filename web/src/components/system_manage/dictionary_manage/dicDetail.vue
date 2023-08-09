<template>
  <div>
    <el-dialog title="字典详情" @close="close('')" :visible="detailVisible">
      <el-form  :model="form"  label-width="130px">
        <el-form-item label="名称："  prop="text" >
          <el-input  v-model="form.text" readonly />
        </el-form-item>
        <el-form-item label="编码：" prop="code" >
          <el-input v-model="form.code" readonly />
        </el-form-item>
        <el-form-item label="上级：" prop="pidName">
          <el-input v-model="form.pidName" readonly />
        </el-form-item>
        <el-form-item label="上级编码：" prop="pcode">
          <el-input v-model="form.pcode" readonly />
        </el-form-item>
        <el-form-item label="是否子节点：" prop="leaf">
          <el-input v-model="form.leaf" readonly />
        </el-form-item>
        <el-form-item label="备注：" prop="remark">
          <el-input v-model="form.remark" readonly />
        </el-form-item>
        <el-form-item label="排序号：" prop="sortno">
          <el-input v-model="form.sortno" readonly />
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
    name: 'dicDetailBox',
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
