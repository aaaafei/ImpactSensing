<template>

  <div id="POST">
    <el-row >
      <el-col :span="5" class="left-content">
        <div id="post_tree" class="grid-content bg-purple the_left_tree" >
          <div class="thefilterInput">
           <el-input placeholder="输入部门进行过滤" v-model="filterText"></el-input>
          </div>
          <div style="width: 100%;height: 45vh;overflow: auto;padding-top: 10px;position: relative;">
            <el-button type="text" icon="el-icon-refresh" @click="seachOrg" style="padding: 0px;position: absolute;right: 02%;top: 15px;color: #4576a9;"></el-button>
            <el-tree
              class="filter-tree"
              v-loading="treeLoading"
              :data="treeData"
              node-key="id"
              :props="defaultProps"
              max-height="300"
              default-expand-all
              highlight-current
              :filter-node-method="filterNode"
              :expand-on-click-node="false"
              ref="tree2"
              @node-click="seachPost"
              style="display: inline-block;min-width: 11.8vw">
            </el-tree>
          </div>
        </div>
      </el-col>
      <el-col :span="19"  class="right-content">
        <el-col :span="24">
          <el-input v-model="queryText" placeholder="请输入岗位名称/编码/备注" style="width: 25%;" size="medium"></el-input>
          <el-button type="primary"  @click="queryPost" size="small" v-if="checkButtonPerm('position:list')"> 查询</el-button>
        </el-col>
        <el-col :span="24">
          <el-table
              v-loading="loading"
              :data="tableData"
              max-height="380"
              @selection-change="handleSelectionChange">
              <el-table-column
                type="selection"
                width="38">
              </el-table-column>
              <el-table-column
                type="index"
                :index="indexMethod"
                label="序号"
                width="50">
              </el-table-column>
              <el-table-column prop="name" label="岗位名称" width="">
                <template slot-scope="scope">
                  <el-button  type="text" class="clickName" @click="detailPost(scope.row)" v-if="checkButtonPerm('position:get')">{{scope.row.name}}</el-button>
                  <span v-else>{{scope.row.name}}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="code"
                label="岗位编码"
                width="100">
              </el-table-column>
              <el-table-column
                prop="roleName"
                label="所属角色"
                width="">
              </el-table-column>
              <el-table-column
                prop="orgName"
                label="所在组织"
                width="">
              </el-table-column>
              <el-table-column
                prop="remark"
                label="备注"
                width="">
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
    </el-row>
  </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  export default {
    name: 'post',
    data: function () {
      return {
        loading: false, // 是否加载
        treeLoading: false, // 是否加载
        treePermisLoading: false, // 权限树是否加载loading
        operated: false,
        filterText: '',
        selectedPost: null,
        treeData: [{ // 组织树
          id: 0,
          label: '总系统',
          data: null,
          leaf: false,
          children: []
        }],
        treeNode: { // 选中的组织节点信息
          id: '', // 组织id
          code: '',
          label: '' // 组织名称
        },
        tableData: [],
        propForm: {
          id: '',
          name: '',
          code: '',
          roleId: '',
          roleName: '',
          orgId: '',
          sortno: '',
          orgName: '',
          remark: ''
        },
        isAdd: false,
        isDisabled: false,
        isShowOrg: true,
        isState: true,
        detailVisible: false,
        detailForm: { // 详情框
          id: '',
          createTime: '',
          updateTime: '',
          state: '',
          createUser: '',
          updateUser: '',
          orgName: '',
          roleName: '',
          name: '',
          code: '',
          orgId: '',
          roleId: '',
          sortno: '',
          remark: ''
        },
        page: {
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        permisData: { // 权限分配的prop数据，传递角色id
          id: '',
          name: 'post',
          useButton: false,
          authTreeData: []
        },
        checkedData: [], // 权限分配时需要自动选中的数据
        queryText: '',
        permisVisible: false, // 权限弹框可见性
        addEditVisible: false, // 新增与编辑的页面可见性
        title: '', // 页面标题
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
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        this.receiveButtons();
        this.seachOrg();
        this.seachAllPost();
      });
    },
    activated: function () {
      // console.log(this.treeNode.label)
      // alert(1)
      // this.seachOrg();
      // this.seachAllPost();
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
        this.$request({
          url: '/permissions?menu=' + sessionStorage.menuId
        }).then(res => {
          this.theButtons = res.data.data.buttons;
          this.permisData.useButton = (res.data.data.buttons.indexOf('position:saveAuth') > -1);
        });
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
      seachOrg () {
        // 查询组织
        // console.log('刷新啦');
        // console.log(this.treeNode.label);
        // setTimeout(function () {
        //   Self.$refs.tree.setCurrentKey(Self.treeNode.id);
        // }, 500);
        this.treeLoading = true;
        this.$request({
          url: '/org/tree'
        }).then(res => {
          this.treeLoading = false;
          this.treeData[0].children = res.data.data;
          let Self = this;
          setTimeout(function () {
            Self.$refs.tree2.setCurrentKey(Self.treeNode.id);
          }, 500);
        });
      },
      seachPost (data) { // 查询岗位列表
        if (!data.isTrue) {
          console.log('yeah');
          this.page.pageNum = 1;
          // this.queryText = '';
        }
        this.treeNode.id = data.id;
        this.treeNode.label = data.label;
        if (data.id !== 0) {
          this.treeNode.code = data.data.code;
        }
        console.log(this.treeNode.label);
        this.loading = true;
        let options = {
          params: {}
        };
        if (data.id !== 0) {
          // options.params.orgId = data.id;
          options.params.orgCode = data.data.code;
        }
        options.params.keywords = this.queryText;
        // var data={}//定义一个json对象
        // data['result']={name: 'caod'};//添加属性
       // console.log(this.options);
        this.$request({
          url: '/position/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      },
      seachAllPost () { // 根据组织查询岗位列表 或者 （刷新列表）
        this.loading = true;
        let options = {
          params: {
            keywords: this.queryText
          }
        };
        this.$request({
          url: '/position/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      },
      queryPost () { // 按条件查询岗位列表
        this.page.pageNum = 1;
        let options = {
          params: {}
        };
        if (this.queryText !== '') {
          options.params.keywords = this.queryText;
        }
        if (this.treeNode.id !== '' && this.treeNode.id !== 0) {
          // options.params.orgId = this.treeNode.id;
          options.params.orgCode = this.treeNode.code;
        }
        this.loading = true; // 开始加载
        this.$request({
          url: '/position/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      },
      filterNode (value, data) {  //  树结构的过滤函数
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      handleSelectionChange (val) { // 储存被选中的数据项
        this.selectedPost = val;
        console.log(this.selectedPost);
      },
      close1 () { // 组织回调函数
        this.organizationVisible = false;
      },
      callBack (msg) { // 新建编辑的回调函数
        this.addEditVisible = false;
        this.permisVisible = false;
        if (msg !== '') {
          if (this.treeNode.id !== '') {  // 用户选择了组织时
            var data = {
              id: this.treeNode.id,
              label: this.treeNode.label,
              data: {
                code: this.treeNode.code
              }
            };
            data.isTrue = true;
            this.seachPost(data);
          } else { // 用户还未选择组织时
            this.seachAllPost();
          }
        }
      },
      detailPost (row) { // 详情框
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
        this.detailForm.orgName = row.orgName;
        this.detailForm.roleName = row.roleName;
        this.detailForm.name = row.name;
        this.detailForm.code = row.code;
        this.detailForm.remark = row.remark;
        this.detailForm.sortno = row.sortno;
        this.detailVisible = true;
      },
      callBackDetail () { // 详情框的回调函数
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
          options.params.orgCode = this.treeNode.code;
        }
        options.params.keywords = this.queryText;
        this.$request({
          url: '/position/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      },
      callBackPermin (msg) {
        this.checkedData = [];
        this.permisVisible = false;
        this.permisData.authTreeData = [];
        this.permisData.id = '';
      },
      permAssign (row) { // 分配权限
        this.treePermisLoading = true; // 开始加载
        this.permisVisible = true;
        console.log('岗位id：');
        console.log(row.id);
        this.permisData.id = row.id;
        let options = {
          params: {
            positionId: row.id
          }
        };
        this.$request({
          url: '/position/authTree',
          params: options.params
        }).then(res => {
          this.treePermisLoading = false;
          this.permisData.authTreeData = res.data.data;
          this.checkedData = [];
          this.judgeChecked(res.data.data);
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
  #POST .el-tree-node__label {
    font-size: 12px;
  }
 #POST th{
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #POST .clickName{
    color: #606266;
    font-size: 12px;
  }
  #POST .clickName:hover{
    color: #409EFF;
    text-decoration: underline;
  }

  #post_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #POST input {
    height: 30px;
  }
  #POST .el-table td{
    padding: 7px 0;
    color: #666666;
  }
  #POST .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #POST .el-row {
   /* padding: 10px 20px 20px 20px;*/
    margin: 15px;
    margin-top: 10px;
  }
  #POST .left-content {
    padding-right: 15px;
    /*min-height: 400px;*/
  }
  #POST .thefilterInput {
    width: 90%;
    margin-left: 5%;
    border-bottom: 1px solid #e5e5e5;
    padding-bottom: 10px
  }
  #POST .the_left_tree {
    padding-top: 10px;
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
    background-color: #fff;
    /* overflow-y: auto;
     overflow-x: hidden;*/
  }
  #POST .el-button {
    padding: 9px 29px;
  }
  #POST .el-button--text{
    width: auto;
    height: auto;
    padding: 9px 0px;
  }
  #POST .right-content > .el-col {
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
  }
  #POST .right-content > .el-col:first-child {
    padding: 12px 15px 10px 15px;
  }
  #POST .right-content > .el-col:last-child {
    padding-left: 10px;
    height: 35.5vh;
    padding-right: 10px;
    margin-top: 10px;
  }
  #POST .el-form--inline .el-form-item {
    margin-right: 30px;
  }
  #POST .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 10px;
  }
  /* #POST .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #POST .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #POST .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  } */
  /*分页样式*/
  #POST .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #POST .pageDiv .page {
    float: right;
  }
  #POST .el-pager {
    margin-left: 12px;
  }
  #POST  .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #POST .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #POST .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #POST .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #POST .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }

  /**************/
  #POST .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #POST .el-select{
    width :100%;
  }

</style>
