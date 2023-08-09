
<template>
  <div id="addEditDict">
    <el-dialog :title="title" :visible="addEditVisible" @close="close('')">
      <el-form ref="addEditDictform" :status-icon="isState" :model="form" :rules="rules" label-width="100px" >
        <el-form-item label="上级名称：" for="label">
          <el-input id="label" v-model="preLabel"/>
        </el-form-item>
        <el-form-item label="字典名称：" prop="text" for="name">
          <el-input id="name" v-model="form.text" placeholder="请输入字典名称"/>
        </el-form-item>
        <el-form-item label="字典编码："  prop="code" for="code">
          <el-input id="code" v-model="form.code" placeholder="请输入字典编码" :disabled="codeEdit"/>
        </el-form-item>
        <el-form-item label="备注：" prop="remark" for="remark">
          <el-input id="remark" v-model="form.remark" placeholder=""/>
        </el-form-item>
        <el-form-item label="是子节点：" prop="leaf" for="leaf">
          <el-select v-model="form.leaf" placeholder="请选择">
            <el-option v-for="(item,index) in leafs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序：" prop="sortno" for="sortno">
          <el-input id="sortno" v-model="form.sortno" placeholder=""/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close('')">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="add('addEditDictform')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import request from '@/assets/utils/request';
  export default {
    name: 'addEditDictBox',
    props: ['addEditVisible', 'propForm', 'title', 'preLabel', 'codeEdit', 'isState'],
    data: function () {
      var checkSortno = (rule, value, callback) => {
        if (!value) {
          return callback();
        }
        if (value.length > 11) {
          callback(new Error('最大为11位数'));
        } else {
          let value1 = '' + parseInt(value);
          value = '' + value;
          if (value1 === value) {
            callback();
          } else {
            callback(new Error('请输入数字值'));
          }
        }
      };
      return {
        form: this.propForm,
        finished: false,
        rules: {
          text: [
            { required: true, message: '请输入字典名称', trigger: 'blur' }
          ],
          code: [
            { required: true, message: '请输入字典编码', trigger: 'blur' }
          ],
          leaf: [
            { required: true, message: '请选择是否为节点', trigger: 'blur' }
          ],
          sortno: [
            { validator: checkSortno, trigger: 'blur' }
          ],
          remark: [{ }]
        },
        leafs: [
          {
            label: '是',
            value: '1'
          },
          {
            label: '否',
            value: '0'
          }
        ]
      };
    },
    methods: {
      close (formName) {
        if (formName !== '') {
          this.$refs[formName].resetFields(); // 清空验证信息
          this.$emit('headCallBack', formName);
        } else {
          this.$refs['addEditDictform'].resetFields(); // 清空验证信息
          this.$emit('headCallBack', '');
        }
      },
      add (formName) {
        var Self = this;
        let options = {
          params: {}
        };
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            Self.finished = true;
            options.params = Self.form;
            let url = '';
            if (Self.title === '新增字典') {
              url = '/dict/add';
            } else if (Self.title === '编辑字典') {
              url = '/dict/update';
            }
            request({
              url: url,
              params: options.params
            }).then(res => {
              Self.$message({
                type: 'success',
                message: '操作成功'
              });
              Self.finished = false;
              Self.close('addEditDictform'); // 操作结束后 关闭窗口
            });
          }
        });
      }
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
      });
    }
  };
</script>

<style type="stylus">

</style>
