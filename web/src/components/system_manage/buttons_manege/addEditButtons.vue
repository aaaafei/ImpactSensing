<template>
  <div id="addEditButton">
    <el-dialog :title="title" :visible="addEditVisible" @close="close('')">
      <el-dialog  width="30%" title="选择所属菜单" :visible.sync="innerVisible" append-to-body >
        <div id="menuTree1" class="grid-content bg-purple" style="text-align: center;padding-top: 6px">
          <el-tree
            :data="treeData1"
            default-expand-all
            :expand-on-click-node="false"
            ref="tree"
            @node-click="chooseMenu">
          </el-tree>
        </div>
      </el-dialog>
      <el-form ref="addEditButtonform" :status-icon="isState" :model="form" :rules="rules" label-width="100px" >
        <el-form-item label="名称：" prop="name" for="name">
          <el-input id="name" v-model="form.name" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item label="编码："  prop="code" for="code">
          <el-input id="code" v-model="form.code" :disabled="isDisabled" placeholder="请输入编码"/>
        </el-form-item>
        <el-form-item label="所属菜单："  prop="menuName" for="menuName">
          <el-input id="menuName" readonly="readonly" :disabled="isDisabled"  v-model="form.menuName" placeholder="请选择所属菜单" @focus="innerVisible = isShowOrg"/>
        </el-form-item>
        <el-form-item label="图标："  prop="icon" for="theorg">
          <el-input id="icon"  v-model="form.icon" placeholder="请输入图标" />
        </el-form-item>
        <el-form-item label="排序：" prop="sortno" for="sortno">
          <el-input id="sortno" v-model="form.sortno" placeholder="请输入排序号"/>
        </el-form-item>
        <el-form-item label="备注：" prop="remark" for="remark">
          <el-input id="remark" v-model="form.remark" placeholder=""/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close('')">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="add('addEditButtonform')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';
  import organization from '../organization';
  import request from '@/assets/utils/request';
  export default {
    name: 'addEditButtonBox',
    props: ['addEditVisible', 'propForm', 'title', 'isDisabled', 'isState', 'isShowOrg'],
    data: function () {
      /* var validCode1 = (rule, value, callback) => {
        if (this.form.code !== '') {
          if (value === '') {
            callback(new Error('请选择所属菜单'));
          } else {
            if (this.form.menuId !== '') {
              let options = {
                headers: {
                  Authorization: this.$store.getters.getToken
                },
                params: {menuId: this.form.menuId}
              };
              this.$http.get(this.$api_prefix + '/button/check/' + this.form.code, options).then((res) => {
                // 响应成功回调
                let success = res.body.data;
                if (success === true) {
                  this.codeValid = true;
                  this.$message.error('该菜单下此按钮编码已存在');
                } else {
                  this.codeValid = false;
                }
              }, (res) => {
              });
            }
            callback();
          }
        } else {
          callback();
        }
      }; */
      var checkCode = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入编码'));
        }
        if (this.form.menuId === '') {
          return callback();
        }
        if (this.title === '编辑按钮') {
          this.codeValid = false;
          callback();
        } else {
          setTimeout(() => {
            let options = {
              // headers: {
              //   Authorization: this.$store.getters.getToken
              // },
              params: {}
            };
            if (this.form.menuId !== '') {
              options.params.menuId = this.form.menuId;
            } else {
              return false;
            }
            request({
              url: '/button/check/' + this.form.menuId + '/' + (this.form.code).trim(),
              params: options.params
            }).then(res => {
              let success = res.data.data;
              if (success === true) {
                this.codeValid = true;
                callback(new Error('该菜单下此按钮编码已存在'));
              } else {
                this.codeValid = false;
              }
            });
            // this.$http.get(this.$api_prefix + '/button/check/' + this.form.menuId + '/' + (this.form.code).trim(), options).then((res) => {
            //   // 响应成功回调
            //   let success = res.body.data;
            //   if (success === true) {
            //     this.codeValid = true;
            //     callback(new Error('该菜单下此按钮编码已存在'));
            //   } else {
            //     this.codeValid = false;
            //     callback();
            //   }
            // }, (res) => {
            //   // 响应错误回调
            //   this.codeValid = false;
            //   callback(new Error('编码验证失败'));
            // });
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
        theVisible: this.addEditVisible, // 页面可见性
        innerVisible: false,
        theOperated: false,
        finished: false,
        codeValid: false,
        form: this.propForm,
        rules: {
          name: [
            { required: true, message: '请输入名称', trigger: 'blur' }
          ],
          code: [
            // { required: true, message: '请输入编码', trigger: 'blur,change' }
            { required: true, validator: checkCode, trigger: 'blur' }
          ],
          menuName: [
             { required: true, message: '请选择所属菜单', trigger: 'blur,change' }
            /* { validator: validCode1, trigger: 'blur,change' } */
          ],
          sortno: [
            { validator: checkSortno, trigger: 'blur' }
          ],
          icon: [{ }],
          remark: [{ }]
        },
        treeData1: [], // 菜单树
        MenuData: {
          id: '',
          label: ''
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
      seachMenuTree () { // 查询菜单树
        request({
          url: '/menu/tree'
        }).then(res => {
          this.treeData1 = res.data.data;
        });
      },
      close (formName) {
        if (formName !== '') {
          this.$refs[formName].resetFields(); // 清空验证信息
          this.$emit('headCallBack', formName);
        } else {
          this.$refs['addEditButtonform'].resetFields(); // 清空验证信息
          this.$emit('headCallBack', '');
        }
      },
      chooseMenu (data) {
        this.MenuData.id = data.id;
        this.MenuData.label = data.label;
        this.innerVisible = false;
        this.form.menuName = data.label;
        this.form.menuId = data.id;
      },
      validCode () {
        let options = {
          params: {}
        };
        if (this.form.menuId !== '') {
          options.params.menuId = this.form.menuId;
        } else {
          // this.$message.error('请选择所属菜单');
          return false;
        }
        request({
          url: '/button/check/' + this.form.menuId + '/' + (this.form.code).trim(),
          params: options.params
        }).then(res => {
          let success = res.data.data;
          if (success === true) {
            this.codeValid = true;
            this.$message.error('该菜单下此按钮编码已存在');
          } else {
            this.codeValid = false;
          }
        });
      },
      add (formName) {
        var Self = this;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            Self.saveButtons();
          } else {
            return false;
          }
        });
      },
      saveButtons () {
        if (this.codeValid) {
          return false;
        }
        let options = {};
        var Self = this;
        Self.finished = true;
        options.params = Self.form;
        let url = '';
        if (Self.title === '新增按钮') {
          url = '/button/add';
        } else if (Self.title === '编辑按钮') {
          url = '/button/update';
        }
        request({
          url: url,
          params: options.params
        }).then(res => {
          Self.finished = false;
          Self.codeValid = false;
          Self.$message({
            type: 'success',
            message: res.data.message
          });
          Self.$refs['addEditButtonform'].clearValidate();
          Self.close('addEditButtonform'); // 操作结束后 关闭窗口
        });
      }
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        this.seachMenuTree();
      });
    }
  };
</script>

<style type="stylus">

</style>
