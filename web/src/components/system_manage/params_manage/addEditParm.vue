<template>
  <div id="addEditParam">
    <el-dialog :title="title" @close="close('')" :visible="addEditVisible">
      <el-form ref="addEditParamform" :status-icon="isState" :model="form" :rules="rules" label-width="100px" >
        <el-form-item label="参数名称：" prop="paramKey" for="name">
          <el-input id="name" v-model="form.paramKey" :disabled="isDisabled" placeholder="请输入参数名称"/>
        </el-form-item>
        <el-form-item label="参数值："  prop="paramValue" for="value">
          <el-input id="value" v-model="form.paramValue" placeholder="请输入参数值"/>
        </el-form-item>
        <el-form-item label="参数设置类："  prop="clazz" for="clazz">
          <el-input id="clazz" v-model="form.clazz" placeholder="请输入参数设置类"/>
        </el-form-item>
        <el-form-item label="备注：" prop="remark" for="remark">
          <el-input
            id="remark"
            v-model="form.remark"
            placeholder=""/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close('')">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="add('addEditParamform')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'addEditParamBox',
    props: ['addEditVisible', 'propForm', 'title', 'isDisabled', 'isState'],
    data: function () {
      var checkCode = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入名称'));
        }
        if (this.title === '编辑参数') {
          this.codeValid = false;
          callback();
        } else {
          setTimeout(() => {
            this.$request({
              url: '/param/check/' + (this.form.paramKey).trim()
            }).then(res => {
              let success = res.data.data;
              if (success === true) {
                this.codeValid = true;
                callback(new Error('参数名已存在'));
              } else {
                this.codeValid = false;
                callback();
              }
            });
          }, 10);
        }
      };
      return {
        form: this.propForm,
        theOperated: false,
        finished: false,
        codeValid: false,
        rules: {
          paramKey: [
            // { required: true, message: '请输入参数名称' }
            { required: true, validator: checkCode, trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      close (formName) {
        if (formName !== '') {
          this.$refs[formName].resetFields(); // 清空验证信息
          this.$emit('headCallBack', formName);
        } else {
          this.$refs['addEditParamform'].resetFields(); // 清空验证信息
          this.$emit('headCallBack', '');
        }
      },
      validCode () {
        this.$request({
          url: '/param/check/' + (this.form.paramKey).trim()
        }).then(res => {
          let success = res.data.data;
          if (success === true) {
            this.codeValid = true;
            this.$message.error('参数名已存在');
          } else {
            this.codeValid = false;
          }
        });
      },
      add (formName) {
        var Self = this;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            Self.saveParam();
          } else {
            return false;
          }
        });
      },
      saveParam () {
        if (this.codeValid) {
          return false;
        }
        this.finished = true;
        let options = {
          params: {}
        };
        var Self = this;
        options.params = Self.form;
        let url = '';
        if (Self.title === '新增参数') {
          url = '/param/add';
        } else if (Self.title === '编辑参数') {
          url = '/param/update';
        }
        this.$request({
          url: url,
          params: options.params
        }).then(res => {
          Self.finished = false;
          Self.codeValid = false;
          Self.$message({
            type: 'success',
            message: res.data.message
          });
          Self.close('addEditParamform'); // 操作结束后 关闭窗口
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
