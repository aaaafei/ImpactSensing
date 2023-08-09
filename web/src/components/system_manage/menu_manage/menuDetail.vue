<template>
  <div>
    <el-dialog title="菜单详情" @close="close('')" :visible="detailVisible">
      <el-form  :model="form"  label-width="130px">
        <el-form-item label="名称："  prop="name" >
          <el-input  v-model="form.name" readonly />
        </el-form-item>
        <el-form-item label="编码：" prop="code" >
          <el-input v-model="form.code" readonly />
        </el-form-item>
        <el-form-item label="图标：" prop="icon">
          <el-input v-model="form.icon" readonly />
        </el-form-item>
        <el-form-item label="所属菜单：" prop="pname">
          <el-input v-model="form.pname" readonly />
        </el-form-item>
        <el-form-item label="菜单类型：" prop="typeName">
          <el-input v-model="form.typeName" readonly />
        </el-form-item>
        <el-form-item label="菜单链接地址：" prop="url">
          <el-input v-model="form.url" readonly />
        </el-form-item>
        <el-form-item label="是否末级菜单：" prop="leaf">
          <el-input v-model="form.leaf" readonly />
        </el-form-item>
        <el-form-item label="是否外部链接：" prop="outLink">
          <el-input v-model="form.outLink" readonly />
        </el-form-item>
        <el-form-item label="排序号：" prop="sortno">
          <el-input v-model="form.sortno" readonly />
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
    name: 'menuDetailBox',
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
