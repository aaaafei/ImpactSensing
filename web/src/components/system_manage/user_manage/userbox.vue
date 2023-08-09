<template>

  <div id="USER">
    <!--<el-row>
      <el-col :span="24" style="padding: 15px 0">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
    </el-row>-->
    <el-row>
      <el-col :span="5" class="left-content">
        <div id="uesr_tree" class="grid-content bg-purple the_left_tree" >
          <!-- <div class="thefilterInput">
            <el-input placeholder="输入部门进行过滤" v-model="filterText" ></el-input>
          </div> -->
          <div style="width: 100%;height: 70vh;overflow: auto;padding-top: 10px;position: relative;">
            <el-button type="text" icon="el-icon-refresh" @click="seachOrg" style="padding: 0px;position: absolute;right: 02%;top: 15px;color: #4576a9;"></el-button>
            <el-tree
              class="filter-tree"
              v-loading="treeLoading"
              :data="treeData"
              node-key="id"
              :props="defaultProps"
              default-expand-all
              highlight-current
              :filter-node-method="filterNode"
              :expand-on-click-node="false"
              ref="tree2"
              @node-click="seachUser"
              style="display: inline-block;/*min-width: 11.8vw*/">
            </el-tree>
          </div>
        </div>
      </el-col>

      <el-col :span="19" class="right-content">

        <el-col :span="24" >
            <el-form :inline="true" label-width="40px" :model="formInline" class="demo-form-inline" size="mini" ref="queryForm">
              <el-form-item label="姓名:" prop="username" >
                <el-input v-model="formInline.username" placeholder="请输入姓名"></el-input>
              </el-form-item> 
              <el-button type="primary" @click="queryUser('queryForm',true)">查询</el-button>
              <el-button type="info" @click="resetForm('queryForm')" v-if="false">重置</el-button>  
            <el-button type="primary" size="small"  @click="addUser" v-if="checkButtonPerm('user:add')">新增</el-button> 
            <el-button type="danger" size="small"  @click="deleteUser" v-if="checkButtonPerm('user:delete')"> 删除</el-button> 
            </el-form> 
        </el-col> 
        <el-col :span="24" style="height:64vh;margin-top:10px;">
            <el-table v-loading="loading" ref="multipleTable" :data="tableData3" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="40"></el-table-column>
              <el-table-column type="index" label="序号" :index="indexMethod" width="50"></el-table-column>
              <el-table-column prop="truename" label="姓名" width="150">
                <template slot-scope="scope">
                  <el-button  type="text" class="clickName" @click="detailUser(scope.row)" v-if="checkButtonPerm('user:get')">{{scope.row.truename}}</el-button>
                  <span v-else>{{scope.row.truename}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="username" label="帐号" width="100">
              </el-table-column>
              <el-table-column prop="organname" label="部门"  width="150">
              </el-table-column> 
              <el-table-column prop="phone" label="联系电话" >
              </el-table-column>
                <!-- <el-table-column prop="state" label="状态" :formatter="formatState" width="50"></el-table-column>  -->
            <el-table-column label="操作" fixed="right" width="250">
                <template slot-scope="scope" class="theoprations">
                  <!-- <el-button @click="resetPassword(scope.$index,scope.row)" type="text" size="mini" v-if="checkButtonPerm('user:resetPWD')">重置密码</el-button>
                  <el-button type="text" size="mini" @click="userAllMenus(scope.$index, scope.row)" style=" margin-left: 0px;">查看权限</el-button>
                  <el-button type="text" size="mini" @click="postManege(scope.row)" style=" margin-left: 0px;" v-if="checkButtonPerm('user:position')">岗位管理</el-button> -->
                <el-button size="mini" type="success" plain @click="editUser(scope.row)">编辑</el-button>
                <el-button size="mini" type="success" @click="resetPassword(scope.$index,scope.row)">重置密码</el-button> 
                </template>
              </el-table-column> 
            </el-table>
          <div class="pageDiv">
            <el-pagination
              class="page"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="page.pageNum"
              :page-sizes="[5, 10, 15, 20]"
              :page-size="page.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="page.total">
            </el-pagination>
          </div>
        </el-col> 
      </el-col>

      <el-col :span="24" :class="userPostVisible">
        <div class="userPostBox" >
        <el-col :span="24" style="background-color: #fff;padding:10px 20px 30px 20px;border-radius: 5px;height: 75vh">
          <el-col :span="24" style="font-size: 16px">
            <span>岗位管理</span> ( <span style="font-size: 14px;font-weight: bold">{{userPostData.thename}}</span> )
            <el-button type="text" @click="closeUserPost()" style="float: right;color: #666666;font-size: 18px"><i class="el-icon-close"></i></el-button>
          </el-col>
          <el-col :span="24">
            <el-button type="primary"  @click="addUserPost()" size="small" style="margin-bottom: 10px" v-if="checkButtonPerm('user:addPositions')">新增</el-button>
            <el-button type="danger"  @click="deleteUserPost()" size="small" style="margin-bottom: 10px" v-if="checkButtonPerm('user:removePositions')">删除</el-button>
            <el-table
              v-loading="loading1"
              :data="userPostData.userPostTable"
              max-height="350"
              @selection-change="handleSelectionChange1"
            style="">
              <el-table-column
                type="selection"
                width="45">
              </el-table-column>
              <el-table-column
                type="index"
                :index="indexMethod"
                label="序号"
                width="50">
              </el-table-column>
              <el-table-column prop="name" label="岗位名称" width="">
                <!--<template slot-scope="scope">
                  <el-button  type="text" class="clickName" @click="detailPost(scope.row)">{{scope.row.name}}</el-button>
                </template>-->
              </el-table-column>
              <el-table-column
                prop="roleName"
                label="所属角色"
                width="">
              </el-table-column>
              <el-table-column
                prop="organname"
                label="所在组织"
                width="">
              </el-table-column>
              <!--<el-table-column
                label="操作"
                fixed="right"
                width="70">
                <template slot-scope="scope">
                  <el-button @click="deleteUserPost(scope.$index, userPostTable)" type="text" size="small">删除</el-button>
                </template>
              </el-table-column>-->
            </el-table>
          </el-col>
        </el-col>
        </div>
      </el-col>
    </el-row>

    <el-dialog  width="30%" title="选择组织" :visible.sync="innerVisible" append-to-body>
      <organization v-on:headCallBack1="callBack1"></organization>
    </el-dialog>

    <addEditUser :addEditVisible= "addEditVisible" :propForm="propForm" :roleids="roleids" :title="title" :isDisabled="isDisabled" :isShowOrg="isShowOrg" :isState="isState" v-on:headCallBack="callBack"></addEditUser>

    <userAllMenus :menusVisible= "menusVisible" :menusData="menusData" :treePermisLoading="treePermisLoading" v-on:headCallBack2="callBack2"></userAllMenus>

    <userDetail :detailVisible= "detailVisible" :detailForm="detailForm"  v-on:headCallBackDetail="callBackDetail">
    </userDetail>

    <posts :postsVisible= "postsVisible" :postsData="postsData" :treeData="treeData"  v-on:headCallBackPosts="callBackPosts">
    </posts>

  </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  import addEditUser from './addEditUser';
  import userAllMenus from './userAllMenus';
  import userDetail from './userDetail';
  import organization from '../organization';
  import posts from './posts';
  import request from '@/assets/utils/request';
  export default {
    name: 'user',
    data: function () {
      return {
        loading: false, // 用户列表是否加载
        loading1: false, // 岗位管理列表是否加载
        treeLoading: false, // 组织树是否加载
        treePermisLoading: false, // 权限树是否加载loading
        operated: false,
        innerVisible: false,
        filterText: '',
        treeData: [],
        propForm: { // 新增与编辑的prop数据
          id: '',
          realName: '',
          loginName: '',
          organid: '',
          organname: '',
          roleids: '',
          wechatid: '',
          phone: '',
          email: '',
          truename: '',
          username: ''
        },
        menusData: [], // 权限获取的数据
        treeNode: { // 点击树节点的数据
          id: '',
          label: '',
          code: ''
        },
        addEditVisible: false, // 新建编辑页面可见性
        isDisabled: false,
        isState: true,
        isShowOrg: true,
        isAdd: false,
        menusVisible: false, // 权限查看页面可见性
        detailVisible: false, // 用户详情页面可见性
        roleids: [],
        detailForm: { // 详情框 
          realName: '',
          loginName: '', 
          organname: '',
          roles: '', 
          phone: '' 
        },
        title: '', // 页面标题
        formInline: { // 条件查询的数据
          username: '',
          organid: '',
          organname: '',
          wechatid: '',
          phone: '',
          email: ''
        },
        tableData3: [], // 用户列表的数据
        page: { // 分页的数据信息
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        multipleSelection: [], // 用户表的选中项
        multipleSelection1: [], // 用户岗位表的选中项
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        userPostVisible: 'posthide',
        userPostData: { // 岗位管理列表的数据块
          userId: '',
          thename: '',
          userPostTable: []
        },
        postsVisible: false, // 岗位管理里面的新增按钮的弹窗（岗位组织树）
        postsData: { // 传递给岗位管理的新增弹框的数据
          userId: '',
          postTreeLoading: false,
          postIds: []
        },
        theButtons: []
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      addEditUser: addEditUser,
      userAllMenus: userAllMenus,
      userDetail: userDetail,
      posts: posts,
      organization: organization // 获取组织的组件
    },
    mounted: function () {
      this.$nextTick(function () { 
        this.seachOrg();
        this.seachAllUser();
      });
    }, 
    watch: {
      filterText (val) { // 监听左侧树结构的过滤操作
        this.$refs.tree2.filter(val);
      },
      operated: function (val) { // 此处不要使用箭头函数    监听是否增删改的操作成功，成功就执行把刷新列表 
        if (val) {
          // this.seachRole();
        }
      }
    },
    methods: {
      receiveButtons () {
        request({
          url: '/permissions?menu=' + sessionStorage.menuId
        }).then(res => {
          this.theButtons = res.data.data.buttons;
        });
      },
      checkButtonPerm (operate) {
        return true;
        // return this.$checkButtonPermission(operate);
      },
      indexMethod (index) {
        return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);
      },
      formatState: function (row, column, cellValue) { 
        if (cellValue === '1') {
          return '有效';
        } else if (cellValue === '0') {
          return '无效';
        } else {
          return '过期';
        }
      },
      seachOrg () { // 查询组织
        this.treeLoading = true;
        this.postsData.postTreeLoading = true;
        request({
          url: '/dept/get/all'
        }).then(res => {
          // this.treeData = [{ // 组织树
          //   id: 0,
          //   label: '总系统',
          //   data: null,
          //   leaf: false,
          //   children: []
          // }];
          // this.treeData[0].children = res.data.data;
          this.treeData = res.data.result_data;
          let Self = this;
          setTimeout(function () {
            Self.$refs.tree2.setCurrentKey(Self.treeNode.id);
          }, 500);
          this.treeLoading = false;
          this.postsData.postTreeLoading = false;
        });
      },
      seachUser (data) {
        // 根据组织查询用户列表 或者 （刷新列表）
        this.page.pageNum = 1;
        // this.$refs['queryForm'].resetFields();
        this.treeNode.id = data.id;
        this.treeNode.label = data.label;
        if (data.id !== 0) {
          // this.treeNode.code = data.data.code;
        }
        this.queryUser('queryForm', true);
      },
      seachAllUser () { // 根据组织查询用户列表 或者 （刷新列表）
        this.loading = true;
        request({
          url: '/user/get/list/' + this.page.pageNum + '/' + this.page.pageSize,
          method: 'post',
          data: {}
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.result_data.total;
          this.tableData3 = res.data.result_data.list;
        });
      },
      filterNode (value, data) {  //  树结构的过滤函数
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      resetForm (formName) {
        if (this.formInline.keywords !== '' || this.formInline.wechatid !== '' || this.formInline.phone !== '' || this.formInline.email !== '') {
          this.$refs[formName].resetFields();
          this.queryUser(formName, true);
        }
        this.$refs[formName].resetFields();
      },
      queryUser (formName, isTrue) { // 按条件查询用户列表
        if (isTrue) { // 当条件查询时，按下查询按钮或者切换每页的条数时，默认显示第一页。
          this.page.pageNum = 1;
        }
        let options = {
          // headers: {
          //   Authorization: this.$store.getters.getToken
          // },
          params: {}
        };
        var Self = this;
        // var apiPrefix = this.$api_prefix;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            if (Self.formInline.username !== '') {
              options.params.truename = Self.formInline.username;
            }
            if (Self.formInline.wechatid !== '') {
              options.params.wechatid = Self.formInline.wechatid;
            }
            if (Self.formInline.phone !== '') {
              options.params.phone = Self.formInline.phone;
            }
            if (Self.formInline.email !== '') {
              options.params.email = Self.formInline.email;
            }
            if (Self.formInline.organid !== '') {
              options.params.organid = Self.formInline.organid;
            }
            if (Self.treeNode.id !== '' && Self.treeNode.id !== 0) {
              options.params.organid = Self.treeNode.id;
            }
            Self.loading = true; // 开始加载
            request({
              url: '/user/get/list/' + Self.page.pageNum + '/' + Self.page.pageSize,
              method: 'post',
              data: options.params
            }).then(res => {
              Self.operated = false; // 每次查询结束后把操作成功项归原
              Self.loading = false; // 查询结束后，停止加载
              Self.page.total = res.data.result_data.total;
              Self.tableData3 = res.data.result_data.list;
            });
          } else {
            return false;
          }
        });
      },
      deleteUser () { // 删除用户
        if (this.multipleSelection.length > 0) {  // 当有选择项时
          this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {  
            let selectIDs = []; 
            this.multipleSelection.forEach(row => {
              if (row) {
                selectIDs.push(row.id);
              }
            });
             
            request({
              url: '/user/deleteUser/' + selectIDs.join(",") + "/"
            }).then(res => {
              this.$message({
                type: 'success',
                message: res.data.result_desc
              });
              this.queryUser('queryForm', false);
            });

          }).catch(() => {
          });
        } else  {
          this.$alert('请选择需要删除的数据！','提示');
        }  
      },
      addUser () { // 新增用户 
        // if (this.treeNode.id !== '') {
        //   this.isShowOrg = false;
        // }
        this.propForm.organid = null;
        this.propForm.organname = null;
        if (this.treeNode.id === 0) {
          this.$message({
            showClose: true,
            message: '本节点为系统总称，不属于组织，无法添加用户！',
            type: 'warning'
          });
          return false;
        } else {
          this.propForm.organid = this.treeNode.id;
          this.propForm.organname = this.treeNode.label;
        }


        this.propForm.id = '';
        this.propForm.truename = null;
        this.propForm.username = null; 
        this.propForm.wechatid = null;
        this.propForm.phone = null;
        this.propForm.email = null;
        this.roleids = [];
        this.isAdd = true;
        this.addEditVisible = true;
        this.isDisabled = false;
        this.isState = true;
        this.title = '新增用户';
      },
      editUser (rowData) { // 编辑用户 
          var pojo = rowData;
          this.propForm.id = pojo.id;
          this.propForm.truename = pojo.truename;
          this.propForm.username = pojo.username;
          this.propForm.organid = pojo.organid;
          this.propForm.organname = pojo.organname;
          this.propForm.wechatid = pojo.wechatid;
          this.propForm.phone = pojo.phone;
          this.propForm.email = pojo.email;
          var roles = [];
          for (var i in pojo.roles) {
            roles.push(pojo.roles[i].id);
          }
          this.roleids = roles;
          this.isAdd = false;
          this.addEditVisible = true;
          this.isDisabled = false;
          this.isState = false;
          this.title = '编辑用户';
      
      },
      resetPassword (index, rows) { // 重置用户密码
        this.$confirm('此操作将重置密码为默认密码, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          request({
            url: '/user/resetPWD/' + rows.id
          }).then(res => {
            this.$message({
              type: 'success',
              message: res.data.result_desc
            });
          });
        }).catch(() => {
        });
      },
      userAllMenus (index, rows) {
        this.menusData = [];
        this.treePermisLoading = true; // 开始加载
        this.menusVisible = true;
        console.log(rows.loginName);
        request({
          url: '/user/permissions?username=' + rows.loginName
        }).then(res => {
          this.treePermisLoading = false;
          this.menusData = res.data.data;
        });
      },
      handleSelectionChange (val) { // 储存用户列表中被勾选的数据项
        this.multipleSelection = val; 
      },
      callBack (msg) { // 回调函数，根据子组件传回的参数进行操作
        this.addEditVisible = false; // 新增编辑
        if (msg !== '') {
          this.queryUser('queryForm', false);
        }
      },
      callBack2 () { // 回调函数，根据子组件传回的参数进行操作
        this.menusVisible = false;
        this.menusData = [];
      },
      detailUser (row) { 
        this.detailForm.realName = row.truename;
        this.detailForm.loginName = row.username;
        this.detailForm.organname = row.organname;
        this.detailForm.roles = row.roles;
        this.detailForm.phone = row.phone;  
        this.detailVisible = true;
      },
      callBackDetail () {
        this.detailVisible = false;
      },
      handleSizeChange (val) { // 设置每页总共几条 
        // alert(val)
        this.page.pageSize = val;
        this.page.pageNum = 1;
        this.queryUser('queryForm', true);
      },
      handleCurrentChange (val) { // 跳到设置的当前页
        this.page.pageNum = val;
        this.queryUser('queryForm', false);
      },
      postManege (row) { // 打开岗位管理
        this.userPostVisible = 'postshow';
        this.serchUserPost(row.id);
        this.userPostData.thename = row.realName;
        this.userPostData.userId = row.id;
      },
      serchUserPost (data1) {
        this.loading1 = true;
        request({
          url: '/position/user/' + data1
        }).then(res => {
          this.loading1 = false;
          this.userPostData.userPostTable = res.data.data;
          this.postsData.postIds = [];
          for (let j = 0; j < this.userPostData.userPostTable.length; j++) {
            this.postsData.postIds.push(this.userPostData.userPostTable[j].id);
          }
        });
      },
      addUserPost () {
        console.log(this.postsData.postIds);
        this.postsData.userId = this.userPostData.userId;
        this.postsVisible = true;
      },
      deleteUserPost () {
        if (this.multipleSelection1.length === 0) {  // 当有选择项时
          this.$message({
            showClose: true,
            message: '请选择需要删除的项！',
            type: 'warning'
          });
        } else {
          this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            var positions = '';
            for (let i = 0; i < this.multipleSelection1.length; i++) {
              positions = positions + this.multipleSelection1[i].id + ',';
            }
            positions = positions.substring(0, positions.length - 1);
            request({
              url: '/user/removePositions?userId=' + this.userPostData.userId + '&positions=' + positions
            }).then(res => {
              this.serchUserPost(this.userPostData.userId);
              this.$message({
                type: 'success',
                message: res.body.message
              });
            });
          }).catch(() => {
          });
        }
      },
      closeUserPost () {
        this.userPostVisible = 'posthide';
        this.postsData.postIds = [];
      },
      callBackPosts (msg) {
        this.postsVisible = false;
        if (msg) {
          this.serchUserPost(this.userPostData.userId);
        }
      },
      handleSelectionChange1 (val) { // 储存用户列表中被勾选的数据项
        this.multipleSelection1 = val;
        console.log(this.multipleSelection1);
      },
      callBack1 (msg) { // 选选择组织的回调函数
        if (msg.isClose) {
          this.innerVisible = false;
        }
        this.formInline.organname = msg.label;
        this.formInline.organid = msg.id;
      }
    }
  };
</script>

<style >
  #USER .userPostBox {
/*    height: 100vh;
    width: 100vm;*/
    background-color:rgba(0, 0, 0, 0.5);
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    z-index: 2000;
    padding: 8vh 15vw;
  }
  #USER .posthide {
    display: none;
  }
  #USER .postshow {
    display: block;
  }
  #USER label {
    font-size: 12px;
  }
  #USER .el-tree-node__label {
    font-size: 12px;
  }
  #USER th {
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #USER input {
  height: 30px;
}
#USER .el-table td{
  padding: 7px 0;
  color: #666666;
}
#USER .el-table th,td {
  text-align: center;
  font-size: 12px;
}
#USER .el-row {
  margin: 15px;
  margin-top: 10px;
}
#USER .left-content {
  padding-right: 15px;
  /*min-height: 400px;*/
}
#USER .el-button {
  /*width: 84px;
  height: 32px;*/
  padding: 9px 29px;
}
#USER .right-content > .el-col:not(.middle-operate) {
  background-color: #fff;
  border-radius: 5px;
  /* box-shadow: 2px 2px 5px #999; */
}
#USER .right-content > .el-col:first-child { 
   padding: 12px 0px 5px 15px;
 }
 
#USER .el-form--inline .el-form-item {
  margin-right: 30px;
}
#USER .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
  margin-bottom: 15px;
}
#USER .el-button--text{
  padding: 9px 0px;
}
#USER .thefilterInput {
  width: 90%;
  margin-left: 5%;
  border-bottom: 1px solid #e5e5e5;
  padding-bottom: 10px
}
  #uesr_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
#USER .the_left_tree {
  padding-top: 10px;
  background-color: #fff;
  border-radius: 5px;
  /* box-shadow: 2px 2px 5px #999; */
  background-color: #fff;
 /* overflow-y: auto;
  overflow-x: hidden;*/
}
#USER .middle-operate {
  text-align: right;
  padding: 8px 0;
}
#USER .clickName{
  color: #606266;
  font-size: 12px;
  width: auto !important;
  height: auto !important;
   text-decoration: underline;
 }
  #USER .clickName:hover{
   color: #409EFF;
 }
/* #USER .el-button--primary {
  background-color: #4baff2;
  border-color: #4baff2;
}
#USER .el-button--danger {
  background-color: #f36f6f;
  border-color: #f36f6f;
}
#USER .el-button--info {
  background-color: #b1bac0;
  border-color: #b1bac0;
} */
/*分页样式*/
#USER .pageDiv {
  padding: 10px;
  height: 35px;
  background-color: rgb(255, 255, 255);
}

#USER .pageDiv .page {
  float: right;
}
#USER .el-pager {
  margin-left: 12px;
}
#USER .el-pager li.active {
  color: rgb(255, 255, 255);
  background-color: #a7bbf5;
  border: 1px solid;
  border-radius: 6px;
}

#USER .el-pager li.number {
  border: 1px solid #d4d4d4;
  border-radius: 6px;
}

#USER .el-pager li {
   padding: 0 10px;
  min-width: 0px;
  font-family: 宋体;
   margin: 0 6px;
}

#USER .el-pagination button {
  border: 1px solid #d4d4d4;
  border-radius: 6px;
  width: 27px;
  min-width: 0px;
}

#USER .el-pagination .btn-next {
  margin-left: 10px;
  padding-left: 6px;
}

/**************/
#USER .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background-color: #4baff2;
  color: #fff;
}
</style>
