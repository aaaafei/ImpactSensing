<template>

  <div id="ORG">
    <!--<el-row>
      <el-col :span="24">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>组织管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
    </el-row>
    <el-row>
    <el-col :span="24">
        <div style="te1xt-align: right; padding-top: 15px">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="addOrg()" size="small">新增一级组织</el-button>
          </el-button-group>
        </div>
      </el-col>
    </el-row>-->
    <el-row style="margin-top: 10px;">
      <el-col :span="14">
        <el-card class="box-card" shadow="hover">
          <el-input placeholder="输入组织名进行过滤" v-model="filterText" style="margin-bottom: 10px;">
          </el-input>
          <div id="org_tree" class="grid-content" style="text-align: center;padding-top: 6px;padding-right: 25px;height: 60vh;overflow: auto;">
            <el-tree
                class="filter-tree"
                :data="treeData"
                v-loading="treeLoading"
                ref="tree"
                :props="defaultProps"
                highlight-current
                node-key="id"
                default-expand-all
                :filter-node-method="filterNode"
                :expand-on-click-node="false"
                @node-click="handleNodeClick"
                style="display: inline-block;min-width: 29vw">
                <span class="custom-tree-node" slot-scope="{ node, data }">
                  <span>{{ node.label }}</span>
                  <span style="float: right">
                    <el-button
                      type="text"
                      size="mini"
                      @click="() => append(data)" v-if="checkButtonPerm('org:add')">
                      添加子部门
                    </el-button>
                    <el-button
                      type="text"
                      size="mini"
                      @click.stop="() => remove(node, data)" v-if="checkButtonPerm('org:delete')">
                      删除
                    </el-button>
                  </span>
                </span>
              </el-tree>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10" style="padding: 0 18px;">
        <div class="grid-content bg-purple">
            <el-card class="box-card" shadow="hover">
              <div slot="header" class="clearfix">
                <span>部门详细信息</span> 
                <el-button style="float: right; padding: 3px 0 " type="text"  @click="editOrg()" v-if="checkButtonPerm('org:update')">编辑</el-button>
              </div>
              <div  class="text item" style="height: 58vh; overflow-y: auto;overflow-x: hidden;text-align: center" >
                <el-button class="orgButton" :loading="editLoading" :style="editBtnShow">{{editText}}</el-button>
                <el-form :model="form" :rules="rules" ref="orgEditForm" label-width="100px" class="demo-ruleForm" :disabled="forbidden" :style="editFormShow">
                  <el-form-item label="组织名称：" prop="organname">
                    <el-input v-model="form.organname" placeholder="请输入组织名称"/>
                  </el-form-item>
                  <el-form-item label="组织类型：" prop="deptype"> 
                    <el-radio-group v-model="form.deptype">
                        <el-radio v-for="item in orgTypeList" :label="item.value" :key="item.value">{{item.label}}</el-radio> 
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="备注：" prop="remark">
                    <el-input id="remark" v-model="form.remark"></el-input>
                  </el-form-item>
                  <el-form-item label="排序号：" prop="sortno">
                    <el-input id="sortno" v-model="form.sortno"></el-input>
                  </el-form-item>
                  <el-form-item style="text-align: center" v-show="btnVisible">
                    <el-button @click="close('orgEditForm',false)">取消</el-button>
                    <el-button type="primary" :loading="finished" @click="submitForm('orgEditForm')">提交</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-card>
        </div>
      </el-col>
    </el-row>

    <addEditOrg :title="title" :addVisible= "addVisible" :propForm="propForm" :orgTypeList="orgTypeList"  v-on:headCallBack="callBack"></addEditOrg>

    <el-dialog  width="30%" title="选择组织" :visible="organizationVisible" @close="close1()">
        <organization  v-on:headCallBack1="callBack1"></organization>
    </el-dialog>

  </div>
</template>

<script>
/* eslint-disable */ 
  import {mapGetters} from 'vuex';
  import addEditOrg from './addEditOrg';
  import organization from '../organization';
  export default {
    name: 'orga',
    data () {
      var checkSortno1 = (rule, value, callback) => {
        if (!value) {
          return callback();
        }
        if (value.length > 8) {
          callback(new Error('最大为8位数'));
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
        treeLoading: false, // 是否加载
        forbidden: true, // 表单是否禁用
        editLoading: false,
        editText: '暂无详情',
        editFormShow: {
          display: 'none'
        },
        editBtnShow: {
          display: 'inline-block'
        },
        btnVisible: false, // 表单按钮是否可见
        finished: false,
        operated: false,
        filterText: '', // 过滤关键字
        treeNode: { // 右侧编辑表单的组织详情信息 的数据
          id: '',
          name: '无',
          code: '',
          orgType: '',
          state: '',
          parentid: '',
          remark: '',
          createTime: '',
          createUser: '',
          updateTime: '',
          updateUser: '',
          sortno: ''
        },
        removeNode: { // 缓存进行‘移动到’操作的节点
          id: ''
        },
        treeData: [], // 组织树数据
        organizationVisible: false, // 移动到的弹框
        form: { // 右侧编辑表单的组织详情信息 的数据
          id: '',
          organname: '',
          parentid: '',
          status: '',
          organcode: '',
          leaf: '',
          isshow: '',
          deptype: '',
          classtype: '',
          sortno: 0
        },
        propForm: { // 新增组织的prop数据
          id: '',
          organname: '',
          parentid: '',
          status: '',
          organcode: '',
          leaf: '',
          isshow: '',
          deptype: '',
          classtype: '',
          sortno: 0
        },
        isHide: 'ishide',
        moreHide: 'isshow',
        orgTypeList: [  // 组织类型
          {
            value: '10',
            label: '部门'
          }, {
            value: '20',
            label: '基层单位'
          }, {
            value: '30',
            label: '委外单位'
          }],
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
          }],
        
        addVisible: false, // 新建页面可见性
        title: '', // 页面标题
        rules: {
          organname: [
            {required: true, message: '请输入组织名称', trigger: 'blur'}
          ],
          sortno: [
            { validator: checkSortno1, trigger: 'blur' }
          ]
        },
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        theButtons: []
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      addEditOrg: addEditOrg,
      organization: organization
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        // this.receiveButtons();
        this.seachOrg();
        // this.getOrgType();
      });
    },
    activated: function () {
      // this.seachOrg();
      // this.getOrgType();
    },
    watch: {
      filterText (val) {
        this.$refs.tree.filter(val);
      },
      operated: function (val) { // 此处不要使用箭头函数    监听是否增删改的操作成功，成功就执行把刷新列表 
        if (val) {
          this.seachOrg();
        }
      }
    },
    methods: {
      checkButtonPerm (operate) {
        return true;
        // return this.$checkButtonPermission(operate);
      },
      receiveButtons () {
        this.$request({
          url: '/permissions?menu=' + sessionStorage.menuId
        }).then(res => {
          this.theButtons = res.data.data.buttons;
        });
      },
      seachOrg () { // 查询组织
        this.treeLoading = true; 
        this.$request({
          url: '/dept/get/all',
          method: 'get'
        }).then(res => {
          this.treeData = res.data.result_data;
          let Self = this;
          setTimeout(function () {
            Self.$refs.tree.setCurrentKey(Self.treeNode.id);
          }, 500);
          this.treeLoading = false; 
        });
        this.operated = false; // 每次查询结束后把操作成功项归原
      },
      getOrgType () {
        this.$request({
          url: '/constant/getConstantByFlag/org_type'
        }).then(res => {
          let theTypes = res.data.result_data;
          let theTypes1 = [];
          for (var item in theTypes) {
            let theTypes2 = {};
            // console.log(item);// 得到键
            // console.log(theTypes[item]);// 得到键对应的值
            theTypes2.label = theTypes[item];
            theTypes2.value = item;
            theTypes1.push(theTypes2);
          }
          // console.log(theTypes1);
          this.orgTypeList = theTypes1;
        });
      },
      filterNode (value, data) {  //  树结构的过滤函数 
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      append (data) { // 添加子部门 
        this.$refs.tree.setCurrentKey(data.id);
        this.title = '添加子部门';
        //init form values:
        this.propForm.id = '';
        this.propForm.organname = '';
        this.propForm.code = '';
        this.propForm.deptype = '10';
        this.propForm.parentid = data.id;
        this.propForm.pidName = data.label;
        this.propForm.state = '';
        this.propForm.sortno = 0;
        this.propForm.remark = '';

        this.addVisible = true;
      },
      remove (node, data) { // 移除部门
        this.$refs.tree.setCurrentKey(data.id);
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            let theid = data.id;
            this.$request({
                url: '/dept/deleteDept/' + theid
            }).then(res => {
                this.operated = true;
                this.$message({
                    type: 'success',
                    message: res.data.result_desc
                }); 
                this.editFormShow.display = 'none';
                this.editBtnShow.display = 'inline-block';
                this.editText = '暂无详情';
                this.editLoading = false;
            });
        }).catch(() => {
        });
      },
      moveto (node, data) { // 移动到  函数
        this.removeNode.id = data.id;
        this.organizationVisible = true;
      },
      close (formName, isCommint) {
        if (!isCommint) {
          this.$refs[formName].resetFields(); // 清空验证信息
        }
        this.$request({
          url: '/dept/getDeptById/' + this.treeNode.id
        }).then(res => {  
          this.form = res.data.result_data;  
        });
        this.btnVisible = false;
        this.forbidden = true;
      },
      editOrg () {
        this.btnVisible = true;
        this.forbidden = false;
      },
      submitForm (formName) { // 修改组织 
        var Self = this;
        this.$refs[formName].validate(function (valid) {
            if (valid) {
                Self.finished = true;  
                Self.$request({
                    url: '/dept/updateDept/' + Self.form.id,
                    method: 'post',
                    data: Self.form
                }).then(res => {
                  Self.$message({
                    type: 'success',
                    message: res.data.result_desc
                  });
    
                  Self.finished = false;
                  Self.seachOrg(); 
                  Self.treeNode.name = Self.form.name;
                  setTimeout(function () {
                    Self.$refs.tree.setCurrentKey(Self.treeNode.id); 
                  }, 1000);
                  Self.close('orgEditForm', true);
                }); 
                Self.$refs[formName].clearValidate(); // 清空验证信息 
            } else {
                console.log('error submit!!');
                return false;
            }
        });
      },
      handleNodeClick (data) {  // 节点单击时,组织详情查询
        if (!this.checkButtonPerm('org:get')) {
          this.editText = '抱歉，您没有查看权限！';
          return false;
        }
        this.editFormShow.display = 'none';
        this.editBtnShow.display = 'inline-block';
        this.editText = '正在加载中...';
        this.editLoading = true;
        this.$refs['orgEditForm'].resetFields(); // 清空验证信息
        // console.log(this.$refs.tree.getCurrentKey());
        this.$request({
          url: '/dept/getDeptById/' + data.id
        }).then(res => {
          this.form = res.data.result_data;  
          this.editFormShow.display = 'block';
          this.editBtnShow.display = 'none';
        }); 
        this.treeNode.name = data.label;
        this.treeNode.id = data.id;
        //this.treeNode.id = data.data.id;
        //this.treeNode.name = data.data.name;
        //this.treeNode.code = data.data.code;
        //this.treeNode.orgType = data.data.orgType;
        //this.treeNode.state = data.data.state;
        //this.treeNode.remark = data.data.remark;
        //this.treeNode.sortno = data.data.sortno;
        //this.treeNode.createTime = data.data.createTime;
        //this.treeNode.createUser = data.data.createUser;
        //this.treeNode.updateTime = data.data.updateTime;
        //this.treeNode.updateUser = data.data.updateUser; 
      },
      callBack (msg) { // 回调函数
        if (msg !== '') {
          this.seachOrg();
          let Self = this;
          setTimeout(function () {
            Self.$refs.tree.setCurrentKey(Self.treeNode.id);
          }, 1000);
        }
        this.addVisible = false;
      },
      close1 () { // 移动到的关闭函数
        this.organizationVisible = false;
      },
      callBack1 (msg) { // 移动到的回调函数
        this.organizationVisible = false;
        this.removeDept(this.removeNode.id, msg.id);
      },
      removeDept (theid, thepid) { // 移动部门到
        let options = {
          params: {
            parentid: thepid,
            id: theid
          }
        };
        this.$request({
          url: '/org/save',
          params: options.params
        }).then(res => {
          this.operated = true;
          this.$message({
            type: 'success',
            message: res.data.msg
          });
        });
      },
      moreDetail () {
        this.isHide = 'isshow';
        this.moreHide = 'ishide';
      },
      closeDetail () {
        this.isHide = 'ishide';
        this.moreHide = 'isshow';
      }
    }
  };
</script>

<style>
  #ORG .orgButton {
    border: none;
    font-size: 20px;
    color: inherit;
    cursor: text;
  }
  #ORG .orgButton:hover {
    border: none;
    background: none;
    cursor: text;
  }
  #ORG label {
    font-size: 12px;
  }
  #ORG .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 12px;
    padding-right: 8px;
  }
  #ORG .item div {
    height: 35px;
    padding: 0px 5px;
    font-size: 14px;
  }
  #ORG .el-button {
    padding: 9px 29px;
  }
  #ORG .el-button--text{
    padding: 9px 0px;
  }
  #ORG .ishide {
    display: none;
  }
  #ORG .isshow {
    display: block;
  }
  #ORG .el-input__inner{
    height: 30px;
  }
  #ORG .el-card__body {
    padding-top: 15px;
  }
  #org_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #org_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content .el-button--text {
    color: #fff;
  }
  #ORG .el-row {
    margin: 15px;
    margin-top: 10px;
  }
  #ORG .el-select{
    width :100%;
  }
  .el-card{
    border: none;
    box-shadow: none;
  }
</style>
