<template>
  <div id="BUTTONS">
    <!--<el-row>
      <el-col :span="24" style="padding: 15px 0">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>按钮管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
    </el-row>-->
    <el-row >
      <el-col :span="4" class="left-content">
        <div id="menuButtonTree" class="grid-content bg-purple the_left_tree" >
          <div class="thefilterInput">
           <el-input placeholder="输入菜单名进行过滤" v-model="filterText" size="small"></el-input>
          </div>
          <div style="width: 100%;height: 75vh;overflow: auto;padding-top: 10px;position: relative;">
            <el-button type="text" icon="el-icon-refresh" @click="seachMenuTree" style="padding: 0px;position: absolute;right: 02%;top: 15px;color: #4576a9;"></el-button>
            <el-tree
              class="filter-tree"
              v-loading="treeLoading"
              :data="treeData"
              default-expand-all
              node-key="id"
              highlight-current
              :filter-node-method="filterNode"
              :expand-on-click-node="false"
              ref="tree2"
              @node-click="seachButton"
              style="display: inline-block;min-width: 11.8vw">
            </el-tree>
          </div>
        </div>
      </el-col>
      <el-col :span="20"  class="right-content">
        <el-col :span="24">
          <el-input v-model="queryText" placeholder="请输入按钮名称/编码/备注" style="width: 25%;" size="small"></el-input>
          <el-button type="primary"  @click="queryButton" size="small" v-if="checkButtonPerm('button:list') "> 查询</el-button>

          <el-button type="danger"  @click="deleteButton()" size="small" style="float: right" v-if="checkButtonPerm('button:delete') "> 删除</el-button>
          <el-button type="primary"   size="small"  @click="editButton()" style="float: right" v-if="checkButtonPerm('button:update') ">编辑</el-button>
          <el-button type="primary"  @click="addButton()" size="small" style="float: right" v-if="checkButtonPerm('button:add') ">新增</el-button>
        </el-col>
        <el-col :span="24">
          <el-table
            v-loading="loading"
            :data="tableData"
            max-height="380"
            @selection-change="handleSelectionChange">
            <el-table-column
              type="selection"
              width="40">
            </el-table-column>
            <el-table-column
              type="index"
              :index="indexMethod"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column prop="name" label="名称" width="">
              <template slot-scope="scope">
                <el-button  type="text" class="clickName" @click="detailButton(scope.row)" v-if="checkButtonPerm('button:get') ">{{scope.row.name}}</el-button>
                <span v-else>{{scope.row.name}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="code"
              label="编码"
              width="">
            </el-table-column>
            <el-table-column
              prop="icon"
              label="图标"
              width="">
            </el-table-column>
            <el-table-column
              prop="menuName"
              label="所属菜单"
              width="">
            </el-table-column>
            <el-table-column
              prop="remark"
              label="备注"
              width="">
            </el-table-column>
            <!-- <el-table-column
              prop="state"
              label="状态"
              :formatter="formatState"
              width="50">
            </el-table-column> -->
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
    </el-row>
    <addEditButton :addEditVisible= "addEditVisible" :propForm="propForm" :title="title" :isDisabled="isDisabled" :isState="isState" :isShowOrg="isShowOrg"  v-on:headCallBack="callBack"></addEditButton>
    <buttonsDetail :detailVisible= "detailVisible" :detailForm="detailForm"  v-on:headCallBackDetail="callBackDetail">
    </buttonsDetail>
  </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  import addEditButton from './addEditButtons';
  import buttonsDetail from './buttonsDetail';
  import request from '@/assets/utils/request';
  export default {
    name: 'button',
    data: function () {
      return {
        loading: false, // 是否加载
        treeLoading: false, // 是否加载
        operated: false,
        filterText: '',
        queryText: '',
        treeData: [{ // 菜单树
          id: 0,
          label: '总系统',
          data: null,
          leaf: false,
          children: []
        }],
        propForm: {
          id: '',
          name: '',
          code: '',
          menuId: '',
          menuName: '',
          icon: '',
          sortno: '',
          remark: ''
        },
        isAdd: false,
        page: {
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        addEditVisible: false, // 新增与编辑的页面可见性
        isDisabled: true,
        isState: true,
        isShowOrg: true,
        detailVisible: false,
        detailForm: { // 详情框
          id: '',
          createTime: '',
          updateTime: '',
          state: '',
          createUser: '',
          updateUser: '',
          menuId: '',
          menuName: '',
          menuCode: '',
          name: '',
          code: '',
          icon: '',
          sortno: '',
          remark: ''
        },
        title: '', // 页面标题
        treeNode: { // 选中的节点信息
          id: '', // 菜单id
          label: '' // 菜单名称
        },
        tableData: [],
        multipleSelection: [],
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
      addEditButton: addEditButton,
      buttonsDetail: buttonsDetail
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        this.receiveButtons();
        this.seachMenuTree();
        this.seachAllButton();
      });
    },
    activated: function () {
      // this.seachMenuTree();
      // this.seachAllButton();
    },
    watch: {
      filterText (val) { // 监听左侧树结构的过滤操作
        this.$refs.tree2.filter(val);
      },
      operated: function (val) { // 此处不要使用箭头函数    监听是否增删改的操作成功，成功就执行把刷新列表 
        if (val) {
          this.seachAllPost();
        }
      }
    },
    methods: {
      checkButtonPerm (operate) {
        return this.$checkButtonPermission(operate);
      },
      receiveButtons () {
        request({
          url: '/permissions?menu=' + sessionStorage.menuId
        }).then(res => {
          this.theButtons = res.data.data.buttons;
        });
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
      indexMethod (index) {
        return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);
      },
      seachMenuTree () { // 查询菜单树
        this.treeLoading = true;
        request({
          url: '/menu/tree'
        }).then(res => {
          this.treeLoading = false;
          this.treeData[0].children = res.data.data;
          let Self = this;
          setTimeout(function () {
            Self.$refs.tree2.setCurrentKey(Self.treeNode.id);
          }, 500);
        });
      },
      seachButton (data) { // 查询按钮列表
        this.loading = true;
        if (!data.isTrue) {
          console.log('yeah');
          this.page.pageNum = 1;
          // this.queryText = '';
        }
        this.treeNode.id = data.id;
        this.treeNode.label = data.label;
        let options = {
          params: {}
        };
        if (data.id !== 0) {
          options.params.menuId = data.id;
        }
        options.params.keywords = this.queryText;
        // var data={}//定义一个json对象
        // data['result']={name: 'caod'};//添加属性
        // console.log(this.options);
        request({
          url: '/button/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      },
      seachAllButton () { // 根据菜单查询按钮列表 或者 （刷新列表）
        this.loading = true;
        let options = {
          params: {
            keywords: this.queryText
          }
        };
        request({
          url: '/button/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      },
      queryButton () { // 按条件查询按钮列表
        this.loading = true; // 开始加载
        this.page.pageNum = 1;
        let options = {
          params: {
          }
        };
        if (this.queryText !== '') {
          options.params.keywords = this.queryText;
        }
        if (this.treeNode.id !== '' && this.treeNode.id !== 0) { // 菜单id
          options.params.menuId = this.treeNode.id;
        }
        request({
          url: '/button/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      },
      deleteButton () { // 删除按钮
        // let options = {
        //   headers: {
        //     Authorization: this.$store.getters.getToken
        //   }
        // };
        if (this.multipleSelection.length === 1) {  // 当有选择项时
          this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            // let theid = '6AF203482EFE43949BF474C9AC3A1187'; // 按钮id
            let theid = this.multipleSelection[0].id; // 按钮id
            request({
              url: '/button/delete/' + theid
            }).then(res => {
              this.$message({
                type: 'success',
                message: res.data.message
              });
              if (this.treeNode.id !== '') {  // 选择了菜单时
                var data = {
                  id: this.treeNode.id,
                  label: this.treeNode.label
                };
                data.isTrue = true;
                this.seachButton(data);
              } else { // 用户还未选择菜单时
                this.seachAllButton();
              }
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        } else if (this.multipleSelection.length === 0) {
          this.$message({
            showClose: true,
            message: '请选择需要删除的项！',
            type: 'warning'
          });
        } else {
          this.$message({
            showClose: true,
            message: '只需选择一条需删除的项！',
            type: 'warning'
          });
        }
      },
      addButton () { // 新增岗位
        if (this.treeNode.id !== '') {
          this.isShowOrg = false;
        }
        this.propForm.id = '';
        this.propForm.name = '';
        this.propForm.code = '';
        if (this.treeNode.id === 0) {
          // this.propForm.menuId = '';
          // this.propForm.menuName = '';
          this.$message({
            showClose: true,
            message: '本节点为系统总称，不属于菜单，无法添加按钮！',
            type: 'warning'
          });
          return false;
        } else {
          this.propForm.menuId = this.treeNode.id;
          this.propForm.menuName = this.treeNode.label;
        }
        this.propForm.icon = '';
        this.propForm.sortno = '';
        this.propForm.remark = '';
        this.isAdd = true;
        this.isDisabled = false;
        this.isState = true;
        this.addEditVisible = true;
        this.title = '新增按钮';
      },
      editButton () { // 编辑按钮
        if (this.multipleSelection.length === 0) {  // 当无选择项时
          this.$message({
            showClose: true,
            message: '请选择需要修改的项！',
            type: 'warning'
          });
        } else if (this.multipleSelection.length !== 1) {
          this.$message({
            showClose: true,
            message: '只能修改一条数据！',
            type: 'warning'
          });
        } else {
          var pojo = this.multipleSelection[0];
          this.propForm.id = pojo.id;
          this.propForm.name = pojo.name;
          this.propForm.code = pojo.code;
          this.propForm.menuName = pojo.menuName;
          this.propForm.menuId = pojo.menuId;
          this.propForm.icon = pojo.icon;
          this.propForm.sortno = pojo.sortno;
          this.propForm.remark = pojo.remark;
          this.isAdd = false;
          this.isDisabled = false;
          this.isState = false;
          this.addEditVisible = true;
          this.title = '编辑按钮';
        }
      },
      detailButton (row) {
        this.detailForm.id = row.id;
        this.detailForm.createTime = row.createTime;
        this.detailForm.updateTime = row.updateTime;
        if (row.state === '0') {
          this.detailForm.state = '无效';
        } else if (row.state === '1') {
          this.detailForm.state = '有效';
        } else {
          this.detailForm.state = '过期';
        }
        this.detailForm.createUser = row.createUser;
        this.detailForm.updateUser = row.updateUser;
        this.detailForm.menuId = row.menuId;
        this.detailForm.menuName = row.menuName;
        this.detailForm.menuCode = row.menuCode;
        this.detailForm.name = row.name;
        this.detailForm.code = row.code;
        this.detailForm.icon = row.icon;
        this.detailForm.remark = row.remark;
        this.detailForm.sortno = row.sortno;
        this.detailVisible = true;
      },
      filterNode (value, data) {  //  树结构的过滤函数
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      handleSelectionChange (val) { // 储存被选中的数据项
        this.multipleSelection = val;
        console.log(this.multipleSelection);
      },
      callBack (msg) { // 回调函数
        this.addEditVisible = false;
        if (msg !== '') {
          if (this.treeNode.id !== '') {  // 用户选择了菜单时
            var data = {
              id: this.treeNode.id,
              label: this.treeNode.label
            };
            data.isTrue = true;
            this.seachButton(data);
          } else { // 用户还未选择菜单时
            this.seachAllButton();
          }
        }
      },
      callBackDetail () {
        this.detailVisible = false;
      },
      handleSizeChange (val) { // 设置每页总共几条
        console.log(`每页 ${val} 条`);
        this.page.pageSize = val;
        this.pageSearch();
      },
      handleCurrentChange (val) { // 跳到设置的当前页
        console.log(`当前页: ${val}`);
        this.page.pageNum = val;
        this.pageSearch();
      },
      pageSearch () { // 分页查询
        this.loading = true;
        let options = {
          params: {}
        };
        if (this.treeNode.id !== '' && this.treeNode.id !== 0) {
          options.params.menuId = this.treeNode.id;
        }
        options.params.keywords = this.queryText;
        request({
          url: '/button/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      }
    }
  };
</script>

<style>
  #menuButtonTree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #BUTTONS .el-tree-node__label {
    font-size: 12px;
  }
  #BUTTONS th{
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #BUTTONS .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  #BUTTONS .clickName{
    color: #606266;
    font-size: 12px;
  }
  #BUTTONS .clickName:hover{
    color: #409EFF;
    text-decoration: underline;
  }
  #BUTTONS input {
    height: 30px !important;
  }
  #BUTTONS .el-table td{
    padding: 7px 0;
    color: #666666;
  }
  #BUTTONS .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #BUTTONS .el-row {
    /*padding: 10px 20px 20px 20px;*/
    margin: 15px;
    margin-top: 10px;
  }
  #BUTTONS .left-content {
    padding-right: 15px;
    /*min-height: 400px;*/
  }
  #BUTTONS .the_left_tree {
    padding-top: 10px;
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
    background-color: #fff;
    /* overflow-y: auto;
     overflow-x: hidden;*/
  }
  #BUTTONS .thefilterInput {
    width: 90%;
    margin-left: 5%;
    border-bottom: 1px solid #e5e5e5;
    padding-bottom: 10px
  }
  #BUTTONS .el-button {
    padding: 9px 29px;
  }
  #BUTTONS .el-button--text{
    width: auto;
    height: auto;
    padding: 9px 0px;
  }
  #BUTTONS .right-content > .el-col {
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
  }
  #BUTTONS .right-content > .el-col:first-child {
    padding: 12px 15px 10px 15px;
  }
  #BUTTONS .right-content > .el-col:last-child {
    padding-left: 10px;
    height: 75vh;
    padding-right: 10px;
    margin-top: 10px;
  }
  #BUTTONS .el-form--inline .el-form-item {
    margin-right: 30px;
  }
  #BUTTONS .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 10px;
  }
  /* #BUTTONS .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #BUTTONS .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #BUTTONS .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  } */
  /*分页样式*/
  #BUTTONS .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #BUTTONS .pageDiv .page {
    float: right;
  }
  #BUTTONS .el-pager {
    margin-left: 12px;
  }
  #BUTTONS  .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #BUTTONS .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #BUTTONS .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #BUTTONS .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #BUTTONS .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }

  /**************/
  #BUTTONS .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
</style>
