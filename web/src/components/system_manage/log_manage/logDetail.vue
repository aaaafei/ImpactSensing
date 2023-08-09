<template>
  <div>
    <el-dialog title="日志详情" @close="close('')" :visible="detailVisible">
      <el-form ref="detailParamform" :model="form"  label-width="100px"  size="small">
        <el-form-item label="请求地址：" prop="url" >
          <el-input v-model="form.url" readonly />
        </el-form-item>
        <el-form-item label="方法名："  prop="method" >
          <el-input  v-model="form.method" readonly />
        </el-form-item>
        <el-form-item label="参数：" prop="param" >
          <el-input v-model="form.param" readonly />
        </el-form-item>
        <el-form-item label="操作结果：" prop="result">
          <el-input v-model="form.result" readonly />
        </el-form-item>
        <el-form-item label="备注：" prop="remark">
          <el-input v-model="form.remark" readonly />
        </el-form-item>
        <el-form-item label="状态：" prop="state">
          <el-input v-model="form.state" readonly />
          <!--<el-select id="state" v-model="form.state" placeholder="请选择状态" disabled>
            <el-option v-for="item in stateList" :key="item.value" :label="item.label" :value="item.value" :disabled="item.disabled">
            </el-option>
          </el-select>-->
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
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'logDetailBox',
    props: ['detailVisible', 'propForm'],
    data: function () {
      return {
        form: this.propForm,
        stateList: [ // 状态
          {
            value: '0',
            label: '无效'
          }, {
            value: '1',
            label: '有效'
          }, {
            value: '2',
            label: '过期'
          }]
      };
    },
    methods: {
      close () {
        this.$emit('headCallBack', '');
      }
    },
    mounted: function () {
    }
  };
</script>

<style type="stylus">

</style>
