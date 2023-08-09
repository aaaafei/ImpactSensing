<template>
  <div id="POSTS">
    <el-dialog title="新增用户岗位" :visible="postsVisible" @close="close('')" @open="open">
    <el-row >
      <el-col :span="7" class="left-content">
        <div id="post_tree" class="grid-content bg-purple the_left_tree" >
          <div class="thefilterInput">
            <el-input placeholder="输入部门进行过滤" v-model="filterText"></el-input>
          </div>
          <div style="width: 100%;height: 60vh;overflow: auto;">
            <el-tree
              class="filter-tree"
              :data="treeData"
              :props="defaultProps"
              max-height="300"
              default-expand-all
              highlight-current
              :filter-node-method="filterNode"
              :expand-on-click-node="false"
              ref="tree2"
              @node-click="seachPost">
            </el-tree>
          </div>
        </div>
      </el-col>
      <el-col :span="17"  class="right-content">
        <el-col :span="24">
          <el-input v-model="queryText" placeholder="请输入岗位名称" style="width: 25%;" size="medium"></el-input>
          <el-button type="primary"  @click="queryPost" size="small"> 查询</el-button>
        </el-col>
        <el-col :span="24">
          <el-table
            v-loading="loading"
            :data="tableData"
            max-height="350"
            @selection-change="handleSelectionChange">
            <el-table-column
              type="index"
              :index="indexMethod"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column prop="name" label="岗位名称" width="">
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
              label="操作"
              fixed="right"
              width="70">
              <template slot-scope="scope">
                <!--<el-button @click="addUserPosts(scope.row)" type="text" size="small">添加</el-button>-->
                <el-button  type="text" size="small" style="cursor: text" v-if="judgeId(scope.row.id)" > &#45;&#45;</el-button>
                <el-button  type="text" size="small"  @click="addUserPosts(scope.row)"  v-else>添加</el-button>
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
    </el-row>
    </el-dialog>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';
  import request from '@/assets/utils/request';
  export default {
    name: 'posts',
    // props: ['postsVisible', 'postsData', 'treeData', 'allPostData', 'postTotal'],
    props: ['postsVisible', 'postsData', 'treeData'],
    data: function () {
      return {
        thePostsData: this.postsData,
        loading: false, // 是否加载
        operated: false,
        filterText: '',
        // treeData: this.treeData,
        page: {
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        queryText: '',
        title: '', // 页面标题
        treeNode: { // 选中的组织节点信息
          id: '', // 组织id
          label: '' // 组织名称
        },
        tableData: [],
        multipleSelection: [],
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
      // organization: organization, // 组织的弹框
      // permission: permission, // 权限的弹框
      // addEditPost: addEditPost,
      // postDetail: postDetail
    },
    mounted: function () {
      this.$nextTick(function () {
        if (this.postsVisible) {
          let token = this.$store.getters.getToken;
          if (token === '') {
            this.$store.dispatch('refresh');
          }
          // this.seachOrg();
          // this.seachAllPost();
        }
      });
    },
    watch: {
      filterText (val) { // 监听左侧树结构的过滤操作
        this.$refs.tree2.filter(val);
      }
    },
    methods: {
      open () {
        this.seachAllPost();
      },
      close () {
        this.$emit('headCallBackPosts', this.operated);
        this.seachAllPost();
      },
      indexMethod (index) {
        return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);
      },
      judgeId (theid) {
        if (this.thePostsData.postIds.indexOf(theid) !== -1) {
          return true;
        } else {
          return false;
        }
      },
      seachPost (data) { // 查询岗位列表
        if (!data.isTrue) {
          this.page.pageNum = 1;
        }
        // this.queryText = '';
        this.treeNode.id = data.id;
        this.treeNode.label = data.label;
        console.log(this.treeNode.label);
        this.loading = true;
        let options = {
        };
        if (data.id !== 0) {
          options['params'] = {
            orgId: data.id,
            keywords: this.queryText
          };
        } else {
          options['params'] = {
            keywords: this.queryText
          };
        }
        request({
          url: '/position/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options['params']
        }).then(res => {
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
        this.operated = false; // 每次查询结束后把操作成功项归原
        this.loading = false; // 查询结束后，停止加载
      },
      seachAllPost () { // 根据组织查询岗位列表 或者 （刷新列表）
        this.loading = true;
        request({
          url: '/position/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
        }).then(res => {
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
        this.loading = false; // 查询结束后，停止加载
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
          options.params.orgId = this.treeNode.id;
        }
        this.loading = true; // 开始加载
        request({
          url: '/position/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options['params']
        }).then(res => {
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
        this.operated = false; // 每次查询结束后把操作成功项归原
        this.loading = false; // 查询结束后，停止加载
      },
      addUserPosts (row) {
        let options = {
          params: {
            userId: ''
          }
        };
        options.params.positions = row.id;
        options.params.userId = this.thePostsData.userId;
        request({
          url: '/user/addPositions',
          params: options['params']
        }).then(res => {
          this.thePostsData.postIds.push(row.id);
          if (this.treeNode.id !== '') {  // 选择了组织时
            var data = {
              id: this.treeNode.id,
              label: this.treeNode.label,
              isTrue: true
            };
            this.seachPost(data);
          } else { // 用户还未选择组织时
            this.seachAllPost();
          }
          // this.serchUserPost(this.userPostData.userId);
          this.operated = true;
          this.$message({
            type: 'success',
            message: res.data.message
          });
        });
      },
      filterNode (value, data) {  //  树结构的过滤函数
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      handleSelectionChange (val) { // 储存被选中的数据项
        this.multipleSelection = val;
        console.log(this.multipleSelection);
      },
      handleSizeChange (val) { // 设置每页总共几条
        console.log(`每页 ${val} 条`);
        this.page.pageSize = val;
        this.page.pageNum = 1;
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
          options.params.orgId = this.treeNode.id;
        }
        options.params.keywords = this.queryText;
        request({
          url: '/position/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options['params']
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
  #POSTS .el-dialog {
    width: 80%;
    margin-top: 8vh !important;
  }
  #POSTS .el-dialog__wrapper{
    /*z-index: 2023 !important;*/
  }
  #POSTS .el-tree-node__label {
    font-size: 12px;
  }
  #POSTS th{
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #POSTS .clickName{
    color: #606266;
    font-size: 12px;
  }
  #POSTS .clickName:hover{
    color: #409EFF;
    text-decoration: underline;
  }
  #uesr_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #org_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #org_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content .el-button--text {
    color: #fff;
  }
  #post_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #dic_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #menuTree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #menuButtonTree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #POSTS input {
    height: 30px;
  }
  #POSTS .el-table td{
    padding: 7px 0;
    color: #666666;
  }
  #POSTS .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #POSTS .el-row {
    padding: 10px 20px 20px 20px;
  }
  #POSTS .left-content {
    padding-right: 15px;
    /*min-height: 400px;*/
  }
  #POSTS .thefilterInput {
    width: 90%;
    margin-left: 5%;
    border-bottom: 1px solid #e5e5e5;
    padding-bottom: 10px
  }
  #POSTS .the_left_tree {
    padding-top: 10px;
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
    background-color: #fff;
    /* overflow-y: auto;
     overflow-x: hidden;*/
  }
  #POSTS .el-button {
    padding: 9px 29px;
  }
  #POSTS .el-button--text{
    width: auto;
    height: auto;
    padding: 9px 0px;
  }
  #POSTS .right-content > .el-col {
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
  }
  #POSTS .right-content > .el-col:first-child {
    padding: 8px;
  }
  #POSTS .right-content > .el-col:last-child {
    padding-left: 10px;
    height: 61vh;
    padding-right: 10px;
    margin-top: 10px;
  }
  #POSTS .el-form--inline .el-form-item {
    margin-right: 30px;
  }
  #POSTS .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 10px;
  }
  #POSTS .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #POSTS .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #POSTS .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  }
  /*分页样式*/
  #POSTS .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #POSTS .pageDiv .page {
    float: right;
  }
  #POSTS .el-pager {
    margin-left: 12px;
  }
  #POSTS  .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #POSTS .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #POSTS .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #POSTS .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #POSTS .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }

  /**************/
  #POSTS .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }

</style>
