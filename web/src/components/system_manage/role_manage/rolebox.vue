<template>
  <div id="ROLE"> 
    <el-row>
      <el-col :span="24">
          <div style="padding: 15px">
            <el-input v-model="queryText" placeholder="请输入名称" style="width: 25%;" size="medium"></el-input>
            <el-button type="primary"  @click="queryRole(true)" size="small"> 查询</el-button> <!-- v-if="checkButtonPerm('role:list')"-->

            <el-button type="danger"  size="small"  @click="deleteRole()" style="float: right" v-if="checkButtonPerm('role:delete')"> 删除</el-button>
           
            <el-button type="primary"  size="small" @click="addRole()" style="float: right" v-if="checkButtonPerm('role:add')">新增</el-button>
          </div>
        </el-col>
        <el-col :span="24"  style="padding: 10px 10px 0 10px;margin-top: 10px;">
            <el-table v-loading="loading" :data="tableData"  @selection-change="handleSelectionChange" style="width: 100%" height="550">
              <el-table-column type="selection" fixed width="40"></el-table-column>
              <el-table-column type="index" :index="indexMethod" label="序号" width="50"></el-table-column>
              <el-table-column prop="des"  label="角色名称" width="200"></el-table-column>
              <el-table-column prop="name" label="角色编码" width="300"></el-table-column>
              <el-table-column prop="remark" label="备注"></el-table-column> 
              <el-table-column label="操作"  width="230">
                <template slot-scope="scope"> 
                    <el-button size="mini" type="success" plain  @click="editRole(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="permAssign(scope.row)">菜单授权</el-button> 
                </template>
              </el-table-column>
            </el-table>
 
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="page.pageNum"
                :page-sizes="[10, 20, 50]"
                :page-size="page.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="page.total">
            </el-pagination>
        </el-col>

        <addEditRole :addEditVisible= "addEditVisible" :propForm="propForm" :title="title" :isDisabled="isDisabled" :isState="isState" v-on:headCallBack="callBack">
        </addEditRole>

        <permission  :permisData="permisData"  :checkedData="checkedData" :permisVisible= "permisVisible" :treePermisLoading="treePermisLoading" v-on:headCallBackPermin="callBackPermin">
        </permission>

    </el-row>
  </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  import addEditRole from './addEditRole';
  import permission from '../permission';
  export default {
    name: 'role',
    data: function () {
      return {
        loading: false, // 是否加载
        operated: false,
        treePermisLoading: false, // 权限树是否加载loading
        filterText: '',
        queryText: '',
        isDisabled: false,
        isState: true,
        propForm: { // 新增与编辑的prop数据
          id: '',
          name: '',
          code: '',
          remark: '',
          sortno: ''
        },
        permisData: { // 权限分配的prop数据，传递角色id
          id: '',
          name: 'role',
          useButton: true,
          authTreeData: []
        },
        checkedData: [], // 权限分配时需要自动选中的数据
        permisVisible: false, // 权限分配页面可见性
        addEditVisible: false, // 新建与编辑页面可见性
        title: '', // 页面标题
        tableData: [],
        multipleSelection: [], // 选择的数据项
        page: {
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        theButtons: []
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      addEditRole: addEditRole, // 新增与编辑的弹框
      permission: permission  // 权限分配的弹框
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        } 
        this.queryRole();
      });
    },
    watch: {
        operated: function (val) { // 此处不要使用箭头函数    监听是否增删改的操作成功，成功就执行把刷新列表 
        if (val) {
          this.queryRole();
        }
      }
    },
    methods: {
      receiveButtons () {
        // this.$request({
        //   url: '/permissions?menu=' + sessionStorage.menuId
        // }).then(res => {
        //   this.theButtons = res.data.data.buttons;
        //   this.permisData.useButton = (res.data.data.buttons.indexOf('role:saveAuth') || res.data.data.buttons.indexOf('*:*') > -1);
        // });
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
      queryRole (flag) { // 按条件查询菜单列表
        if (flag) {
          this.page.pageNum = 1;
        }
        let options = {
          params: {}
        };
        if (this.queryText !== '') {
          options.params.des = this.queryText;
        }
        this.loading = true; // 开始加载
        this.$request({
          url: '/role/get/alldata/' + this.page.pageNum + '/' + this.page.pageSize,
          method: 'post',
          data: options.params
        }).then(res => {
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.result_data.total;
          this.tableData = res.data.result_data.list;
        });
      },
      addRole () { // 新增角色
        this.propForm.id = ''; // 根据该rows节点对象获取数据
        this.propForm.name = '';
        this.propForm.code = '';
        this.propForm.remark = '';
        this.propForm.sortno = '';
        this.isState = true;
        this.isDisabled = false;
        this.addEditVisible = true;
        this.title = '新增角色';
      },
      editRole (rowData) { // 编辑角色 
          var pojo = rowData;
          // var pojo = this.tableData[index];
          this.propForm.id = pojo.id; // 根据该rows节点对象获取数据
          this.propForm.name = pojo.name;
          this.propForm.des = pojo.des;
          this.propForm.remark = pojo.remark;
          this.propForm.sortno = pojo.sortno;
          this.isDisabled = false;
          this.isState = false;
          this.addEditVisible = true;
          this.title = '编辑角色'; 
      },
      deleteRole () { // 删除角色
        if (this.multipleSelection.length === 1) {  // 当有选择项时
          this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
 
            var Self = this;
            this.$request({
                url: '/role/deleteRole/' + this.multipleSelection[0].id
            }).then(res => {
                Self.$message({
                    type: 'success',
                    message: res.data.result_desc
                });
                Self.operated = true;
                Self.queryRole(false);
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        } else if (this.multipleSelection.length === 0) {
          // this.title = '提示';
          // this.deleteData.text = '请选择需要删除的项！';
          // this.deleteData.class = 'el-icon-warning el-alert--warning';
          this.$message({
            showClose: true,
            message: '请选择需要删除的项！',
            type: 'warning'
          });
        } else {
          this.$message({
            showClose: true,
            message: '只需选择一条删除的项！',
            type: 'warning'
          });
        }
      },
      handleSelectionChange (val) { // 储存被选中的数据项
        this.multipleSelection = val; 
      },
      callBack (msg) { // 回调函数
        this.addEditVisible = false;
        if (msg !== '') {
          // this.seachRole();
          this.queryRole(false);
        }
      },
      handleSizeChange (val) { // 设置每页总共几条 
        this.page.pageSize = val; 
        this.queryRole(false);
      },
      handleCurrentChange (val) { // 跳到设置的当前页 
        this.page.pageNum = val; 
        this.queryRole(false);
      },
      callBackPermin (msg) {
        this.permisVisible = false;
        this.checkedData = [];
        this.permisData.authTreeData = [];
        this.permisData.id = '';
      },
      permAssign (row) { // 分配权限
        this.treePermisLoading = true; // 开始加载
        this.permisVisible = true; 
        this.permisData.id = row.id; 
        var _this = this;
        this.$request({
          url: '/permission/getAllPermissionByRole/' + row.id
        }).then(res => {
          _this.treePermisLoading = false; // 结束加载
          // this.permisData.authTreeData = res.data.result_data;
          _this.checkedData = [];
          var data = res.data.result_data;
          for (var i in data) {
            _this.checkedData.push(data[i].id);
          }
          // this.judgeChecked(res.data.result_data);
        });
      },
      judgeChecked (data) {
        for (let o = 0; o < data.length; o++) {
          // console.log(data[o].label)
          if (!data[o].leaf) { // 不是叶节点时
            if (data[o].checked) {
              this.checkedData.push(data[o].id);
            }
            if (data[o].children.length !== 0) {
              this.judgeChecked(data[o].children);
            }
          } else { // 是叶节点时
            if (data[o].checked) {
              this.checkedData.push(data[o].id);
            }
          }
        }
      }
    }
  };
</script>

<style>
  #ROLE th{
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #ROLE .el-table td{
    padding: 7px 0;
    color: #666666;
  }
  #ROLE .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #ROLE .el-button {
    padding: 9px 29px;
  }
  #ROLE  .el-col{
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
  }
#ROLE input {
  height: 30px;
}
  #ROLE .el-button--text{
    width: auto;
    height: auto;
    padding: 9px 0px;
  }
  #ROLE .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #ROLE .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #ROLE .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  }
  #ROLE .el-row {
    margin: 15px;
    margin-top: 10px;
  }
  /*分页样式*/
  #ROLE .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #ROLE .pageDiv .page {
    float: right;
  }
  #ROLE .el-pager {
    margin-left: 12px;
  }
  #ROLE .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #ROLE .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #ROLE .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #ROLE .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #ROLE .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }

  /**************/
  #ROLE .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }

</style>
