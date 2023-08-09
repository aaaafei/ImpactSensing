<template>
  <div id="addEditTask">
    <el-dialog :title="title" :visible="addEditVisible" @close="close('')">
      <!--<el-dialog  width="30%" title="选择组织" :visible.sync="innerVisible" append-to-body>
        <organization   v-on:headCallBack1="callBack1"></organization>
      </el-dialog>-->
      <el-form ref="addEditTaskform" :status-icon="isState" :model="form" :rules="rules" label-width="100px" >
        <el-form-item label="执行地址：" prop="address" >
          <el-input  v-model="form.address" placeholder="请输入执行地址"/>
        </el-form-item>
        <el-form-item label="参数："   prop="params">
          <el-input  :disabled="isDisabled" v-model="form.params" placeholder="仅支持单个字符串参数"/>
        </el-form-item>
        <el-form-item label="cron表达式：" prop="cron" >
        <el-input :disabled="isDisabled" v-model="form.cron" placeholder="请输入cron表达式" />
        <el-button type="primary" icon="el-icon-edit" @click="cronVisible = !cronVisible"></el-button>
      </el-form-item>
        <el-form-item label="任务类型：" prop="type">
          <el-select v-model="form.type" filterable placeholder="请选择任务类型">
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注：" prop="remark" >
          <el-input  v-model="form.remark" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <div sl1ot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close('')">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="add('addEditTaskform')">确 定</el-button>
      </div>
      <el-dialog  width="60%" title="cron编辑器" :visible="cronVisible" @close="closeCron">
        <cron v-model="form.cron" @closeCron="closeCron"></cron>
      </el-dialog>
    </el-dialog>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';
  import organization from '../organization';
  import cron from './cron';
  export default {
    name: 'addEditTaskbox',
    props: ['addEditVisible', 'propForm', 'title', 'isDisabled', 'typeOptions', 'isState'],
    data: function () {
      return {
        // innerVisible: false,
        // theOperated: false,
        finished: false,
        form: this.propForm,
        codeValid: false,
        cronVisible: false,
        rules: {
          address: [
            { required: true, message: '请输入执行地址', trigger: 'blur' }
          ],
          cron: [
          { required: true, message: '请输入cron表达式', trigger: 'blur' }
          //   { required: true, validator: checkCode, trigger: 'blur' }
          ],
          type: [
            { required: true, message: '请选择一个类型', trigger: 'change' }
          ],
          remark: [{}],
          params: [{}]
        }
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      organization: organization, // 获取组织的组件
      cron: cron
    },
    methods: {
      close (formName) {
        if (formName !== '') {
          this.$refs[formName].resetFields(); // 清空验证信息
          this.$emit('headCallBack', formName);
        } else {
          this.$refs['addEditTaskform'].resetFields(); // 清空验证信息
          this.$emit('headCallBack', '');
        }
      },
      add (formName) {
        var Self = this;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            Self.saveTask();
          } else {
            return false;
          }
        });
      },
      saveTask () {
        // if (this.codeValid) {
        //   return false;
        // }
        this.finished = true;
        let options = {
          params: {}
        };
        options.params = this.form;
        let url = '';
        if (this.title === '新增任务') {
          url = '/job/add';
        }
        if (this.title === '编辑任务') {
          url = '/job/update';
        }
        this.$request({
          url: url,
          params: options.params
        }).then(res => {
          this.$message({
            type: 'success',
            message: res.data.message
          });
          this.finished = false;
          // this.codeValid = false;
          this.close('addEditTaskform'); // 操作结束后 关闭窗口
        });
      },
      closeCron () {
        this.cronVisible = false;
        console.log(this.cronVisible);
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
