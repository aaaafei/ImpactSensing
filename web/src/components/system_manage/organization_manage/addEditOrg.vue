<template>
  <div id="addEditOrg">
    <el-dialog :title="title" :visible="addVisible" @close="close('')">
      <el-form ref="addEditOrgform" :model="form" :rules="rules" label-width="100px" >
        <el-form-item label="上级组织：" prop="pidName">
          <el-input id="pid" v-model="form.pidName" readonly="readonly"  placeholder="无"/>
        </el-form-item>
        <el-form-item label="组织名称：" prop="organname">
          <el-input v-model="form.organname" placeholder="请输入组织名称"/>
        </el-form-item>
        <el-form-item label="组织类型：" prop="deptype"> 
            <el-radio-group v-model="form.deptype">
                <el-radio v-for="item in orgTypeList" :label="item.value" :key="item.value">{{item.label}}</el-radio> 
            </el-radio-group>
        </el-form-item>
        <el-form-item label="备注：" prop="remark">
          <el-input v-model="form.remark"></el-input>
        </el-form-item>
        <el-form-item label="排序：" prop="sortno">
          <el-input v-model="form.sortno" placeholder="数值越大，排序越靠前"></el-input>
        </el-form-item>
      </el-form>
      <div sl1ot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close('')">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="add('addEditOrgform')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  export default {
    name: 'addEditOrgbox',
    props: ['title', 'addVisible', 'propForm', 'orgTypeList'],
    computed: mapGetters({
      token: 'getToken'
    }),
    data: function () { 
        var checkSortno = (rule, value, callback) => {
            if (!value) {
              return callback();
            }
            if(value.length>8){
                callback(new Error('最大为8位数1'));
            }
            else{
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
            theVisible: this.addVisible, // 页面可见性
            innerVisible: false,
            theOperated: false,
            finished: false,
            form: this.propForm,
            codeValid: false,
            rules: { 
                organname: [
                    {required: true, message: '请输入组织名称', trigger: 'blur'}
                ],
                sortno: [
                    { validator: checkSortno, trigger: 'blur' }
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
          this.$refs['addEditOrgform'].resetFields(); // 清空验证信息
          this.$emit('headCallBack', '');
        }
      },
      add (formName) {
        var Self = this;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            Self.saveOrg();
          } else {
            return false;
          }
        });
      },
      saveOrg () {
        if (this.codeValid) {
          return false;
        } 
        var Self = this;
        Self.finished = true; 
        Self.$request({
          url: '/dept/addDept',
          method: 'post',
          data: Self.form
        }).then(res => {
          Self.finished = false;
          Self.theOperated = true;
          Self.codeValid = false;
          Self.$message({
            type: 'success',
            message: res.data.result_desc
          });
          Self.close('addEditOrgform'); // 操作结束后 关闭窗口
        });
      },
      validCode () {
        let options = {
          headers: {
            Authorization: this.$store.getters.getToken
          }
        };
        this.$http.get(this.$api_prefix + '/org/check/' + (this.form.code).trim(), options).then((res) => {
          // 响应成功回调
          let success = res.body.data;
          if (success === true) {
            this.codeValid = true;
            this.$message.error('组织编码已存在');
          } else {
            this.codeValid = false;
          }
        }, (res) => {
          // 响应错误回调
          this.$message.error('服务器请求失败！');
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
  #addEditOrg .el-select{
    width :100%;
  }
</style>
