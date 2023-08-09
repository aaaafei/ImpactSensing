<template>
  <div id="addEditPost">
    <el-dialog :title="title" :visible="addEditVisible" @close="close('')">
      <el-dialog  width="30%" title="选择组织" :visible.sync="innerVisible" append-to-body >
        <organization   v-on:headCallBack1="callBack1"></organization>
      </el-dialog>
      <el-form ref="addEditPostform" :status-icon="isState" :model="form" :rules="rules" label-width="100px" >
        <el-form-item label="岗位名称：" prop="name" for="postName">
          <el-input id="postName" v-model="form.name" placeholder="请输入岗位名称"/>
        </el-form-item>
        <el-form-item label="岗位编码："  prop="code" for="postCode">
          <el-input id="postCode" :disabled="isDisabled"  v-model="form.code" placeholder="请输入岗位编码"/>
        </el-form-item>
        <el-form-item label="所属角色："  prop="roleId" for="therole">
          <el-select id="therole" v-model="form.roleId" filterable placeholder="请选择角色" :disabled="isDisabled">
            <el-option v-for="item in roleOptions" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所在组织："  prop="orgName" for="theorg">
          <el-input id="theorg" readonly="readonly" v-model="form.orgName" :disabled="isDisabled" placeholder="请选择组织" @focus="innerVisible = isShowOrg"/>
        </el-form-item>
        <el-form-item label="排序：" prop="sortno" for="sortno">
          <el-input id="sortno" v-model="form.sortno" placeholder=""/>
        </el-form-item>
        <el-form-item label="备注：" prop="remark" for="remark">
          <el-input id="remark" v-model="form.remark" placeholder=""/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close('')">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="add('addEditPostform')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';
  import organization from '../organization';
  export default {
    name: 'addEditPostBox',
    props: ['addEditVisible', 'propForm', 'title', 'isDisabled', 'isShowOrg', 'isState'],
    data: function () {
      var checkCode = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入编码'));
        }
        if (this.title === '编辑岗位') {
          this.codeValid = false;
          callback();
        } else {
          setTimeout(() => {
            this.$request({
              url: '/position/check/' + (this.form.code).trim()
            }).then(res => {
              // 响应成功回调
              let success = res.data.data;
              if (success === true) {
                this.codeValid = true;
                callback(new Error('该编码已存在'));
              } else {
                this.codeValid = false;
                callback();
              }
            });
          }, 10);
        }
      };
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
        innerVisible: false,
        theOperated: false,
        finished: false,
        codeValid: false,
        form: this.propForm,
        rules: {
          name: [
            { required: true, message: '请输入岗位名称', trigger: 'blur' }
          ],
          code: [
            // { required: true, message: '请输入角色编码', trigger: 'blur' }
            { required: true, validator: checkCode, trigger: 'blur' }
          ],
          roleId: [
            { required: true, message: '请选择一个角色', trigger: 'change' }
          ],
          orgName: [
            { required: true, message: '请选择一个组织', trigger: 'blur,change' }
          ],
          sortno: [
            { validator: checkSortno, trigger: 'blur' }
          ],
          remark: [{ }]
        },
        roleOptions: [],
        orgData: {
          orgid: '',
          orgName: ''
        }
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      organization: organization // 获取组织的组件
    },
    methods: {
      seachRole () { // 查询角色列表
        this.loading = true;
        this.$request({
          url: '/role/list'
        }).then(res => {
          this.roleOptions = res.data.data;
        });
        this.operated = false; // 每次查询结束后把操作成功项归原
        this.loading = false; // 查询结束后，停止加载
      },
      close (formName) {
        if (formName !== '') {
          this.$refs[formName].resetFields(); // 清空验证信息
          this.$emit('headCallBack', formName);
        } else {
          this.$refs['addEditPostform'].resetFields(); // 清空验证信息
          this.$emit('headCallBack', '');
        }
      },
      close1 () {
        this.innerVisible = false;
      },
      callBack1 (msg) { // 回调函数
        this.orgData.orgid = msg.id;
        this.orgData.orgName = msg.label;
        if (msg.isClose) {
          this.close1();
        }
        this.form.orgName = msg.label;
        this.form.orgId = msg.id;
      },
      validCode () {
        this.$request({
          url: '/position/check/' + (this.form.code).trim()
        }).then(res => {
          // 响应成功回调
          let success = res.data.data;
          if (success === true) {
            this.codeValid = true;
            this.$message.error('岗位编码已存在');
          } else {
            this.codeValid = false;
          }
        });
      },
      add (formName) {
        var Self = this;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            Self.savePost();
          } else {
            return false;
          }
        });
      },
      savePost () {
        if (this.codeValid) {
          return false;
        }
        let options = {
        };
        var Self = this;
        Self.finished = true;
        options.params = Self.form;
        let url = '';
        if (Self.title === '新增岗位') {
          url = '/position/add';
        } else if (Self.title === '编辑岗位') {
          url = '/position/update';
        }
        this.$request({
          url: url,
          params: options.params
        }).then(res => {
          Self.$message({
            type: 'success',
            message: res.data.message
          });
          Self.finished = false;
          Self.theOperated = true;
          Self.codeValid = false;
          Self.close('addEditPostform'); // 操作结束后 关闭窗口
        });
        Self.$refs['addEditPostform'].clearValidate(); // 清空验证信息
      }
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        this.seachRole();
      });
    }
  };
</script>

<style type="stylus">

</style>
