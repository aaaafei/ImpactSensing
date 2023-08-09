<template>
    <div id="addEditMenu">
    <el-dialog :title="title" :visible="addEditVisible" @close="close('')" > 
      <el-form ref="addEditMenuform" :status-icon="isState" :model="form" :rules="rules" label-width="120px">

        <el-form-item label="所属菜单：">
          <el-input  v-model="form.pname" placeholder="点击输入框选择" :disabled="isDisabled" readonly="readonly" @focus="innerVisible = isShowOrg"/>
        </el-form-item>
        <el-form-item label="名称：" prop="menuname" for="menuname">
          <el-input id="menuname" v-model="form.menuname" placeholder="请输入名称"/>
        </el-form-item>
        <!-- <el-form-item label="编码："  prop="code" for="code">
          <el-input id="code" v-model="form.code" :disabled="isDisabled" placeholder="请输入编码"/>
        </el-form-item> -->
        <el-form-item label="菜单类型："  prop="type" for="type">
          <el-select id="type" v-model="form.type" filterable placeholder="请选择菜单类型">
            <el-option label="模块" :value="10"></el-option>
            <el-option label="页面" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="菜单链接地址："  prop="url" for="url" v-if="form.type==2">
          <el-input id="url" v-model="form.url" placeholder="菜单地址(如：sys/user、sys/role)"/>
        </el-form-item>
        <el-form-item label="图标："  prop="iconClass" for="iconClass">
          <el-input id="iconClass"  v-model="form.iconClass" placeholder="请输入图标样式" />
        </el-form-item>
        <!-- <el-form-item label="是否末级菜单："  prop="leaf" for="leaf">
          <el-select id="leaf" v-model="form.leaf" filterable placeholder="是否为末级菜单">
            <el-option label="是" value="Y"></el-option>
            <el-option label="否" value="N"></el-option>
          </el-select>
      </el-form-item>
        <el-form-item label="是否外部链接："  prop="outLink" for="outLink">
          <el-select id="outLink" v-model="form.outLink" filterable placeholder="是否外部链接">
            <el-option label="是" value="Y"></el-option>
            <el-option label="否" value="N"></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="排序：" prop="sortNo" for="sortNo">
          <el-input-number controls-position="right" v-model="form.sortNo" :step="1" :min="1" ></el-input-number>  
          系统菜单以升序方式排列
        </el-form-item>
        <el-form-item label="备注：">
          <el-input v-model="form.remark"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close('')">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="add('addEditMenuform')">确 定</el-button>
      </div>
    </el-dialog>

        <el-dialog  width="30%" title="选择所属菜单" :visible.sync="innerVisible" append-to-body >
            <div id="menuTree1" class="grid-content" style="text-align: center;padding-top: 6px">
              <el-tree
                :data="treeData1"
                :props="defaultProps"
                default-expand-all
                :expand-on-click-node="false"
                ref="tree"
                @node-click="chooseMenu">
              </el-tree>
            </div>
        </el-dialog>
    </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  export default {
    name: 'addEditMenuBox',
    props: ['addEditVisible', 'propForm', 'title', 'isDisabled', 'isState', 'isShowOrg'],
    data: function () {
      // var checkCode = (rule, value, callback) => {
      //   if (!value) {
      //     return callback(new Error('请输入编码'));
      //   }
      //   if (this.title === '编辑菜单') {
      //     this.codeValid = false;
      //     callback();
      //   } else {
      //     setTimeout(() => {
      //       this.$request({
      //         url: '/menu/check/' + (this.form.code).trim()
      //       }).then(res => {
      //         let success = res.data.data;
      //         if (success === true) {
      //           this.codeValid = true;
      //           callback.error('该菜单编码已存在');
      //         } else {
      //           this.codeValid = false;
      //           callback();
      //         }
      //       });
      //     }, 10);
      //   }
      // };
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
      var checkIcon = (rule, value, callback) => {
          if(value == ''){ 
                callback(new Error('请设置菜单图标')); 
          }
          else{
            return callback();
          }
      };
      var checkUrl = (rule, value, callback) => {
          if(this.form.type == '2' && value == ''){ 
            callback(new Error('页面类型菜单，请输入菜单地址')); 
          }
          else{
            return callback();
          }
      };
      return {
        theVisible: this.addEditVisible, // 页面可见性
        innerVisible: false,
        codeValid: false,
        finished: false,
        form: this.propForm,
        rules: {
          menuname: [
            { required: true, message: '请输入菜单名称', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '请选择菜单类型', trigger: 'change' }
          ],
          sortNo: [
            {validator: checkSortno, trigger: 'blur' }
          ],
          iconClass : [
              {validator: checkIcon, trigger: 'blur'}
          ],
          url: [
                {validator: checkUrl, trigger: 'blur'}     
          ]
        },
        treeData1: [
          { // 菜单树
            id: 0,
            label: '总系统',
            data: null,
            leaf: false,
            children: []
          }
        ],
        pidData: {
          id: '',
          label: ''
        },
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
    },
    methods: {
      seachMenuTree () { // 查询菜单树
        // this.$request({
        //   url: '/menu/tree'
        // }).then(res => {
        //   this.treeData1[0].children = res.data.data;
        // });
        this.$request({
          url: '/menu/get/all',
          params: {judge: 1}
        }).then(res => {
          this.treeLoading = false;
          this.treeData1 = res.data.result_data;
        });
      },
      close (formName) {
        if (formName !== '') {
          this.$refs[formName].resetFields(); // 清空验证信息
          this.$emit('headCallBack', formName);
        } else {
          this.$refs['addEditMenuform'].resetFields(); // 清空验证信息
          this.$emit('headCallBack', '');
        } 
      },
      chooseMenu (data) {
        this.pidData.id = data.id;
        this.pidData.label = data.label;
        // if (this.pidData.id === 0) {
        //   this.form.pname = '';
        //   this.form.pid = '';
        //   this.$message({
        //     showClose: true,
        //     message: '本节点为系统总称，不属于菜单，故默认为新增一级菜单！',
        //     type: 'warning'
        //   });
        // } else {
        //   this.form.pname = data.label;
        //   this.form.pid = data.id;
        // }
        this.form.pname = data.label;
        this.form.pid = data.id;
        this.form.parentId = data.id;
        this.innerVisible = false;
      },
      validCode () {
        this.$request({
          url: '/menu/check/' + (this.form.code).trim()
        }).then(res => {
          let success = res.data.data;
          if (success === true) {
            this.codeValid = true;
            this.$message.error('该菜单编码已存在');
          } else {
            this.codeValid = false;
          }
        });
      },
      add (formName) {
        var Self = this;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            Self.saveMenu();
          } else {
            return false;
          }
        });
      },
      saveMenu () {
        if (this.codeValid) {
          return false;
        }
        this.finished = true; 
        var Self = this; 
        let url = '';
        if (Self.title === '新增菜单') {
          url = '/menu/addMenu';
        } else if (Self.title === '编辑菜单') {
          url = '/menu/updateMenu/' + Self.form.id;
        }
        this.$request({
          url: url,
          method: 'post',
          data: this.form
        }).then(res => {
          Self.$message({
            type: 'success',
            message: res.data.result_desc
          });
          Self.$refs['addEditMenuform'].resetFields();
          Self.theOperated = true;
          Self.finished = false;
          Self.codeValid = false;
          Self.close('addEditMenuform'); // 操作结束后 关闭窗口
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
