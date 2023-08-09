
<template>

  <div id="DICT">
    <!--<el-row>
      <el-col :span="24" style="padding: 15px 0">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>字典管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
    </el-row>-->
    <el-row>
      <el-col :span="5" class="left-content">
        <div id="dic_tree" class="grid-content bg-purple the_left_tree" >
         <!-- style="text-align: center;padding-top: 6px;height: 65vh;overflow-y: auto;border-right: 1px solid #e5e5e5"-->
          <div class="thefilterInput">
          <el-input placeholder="输入字典进行过滤" v-model="filterText"></el-input>
          </div>
          <div style="width: 100%;height: 75vh;overflow: auto;padding-top: 10px">
            <el-tree
              class="filter-tree"
              v-loading="treeLoading"
              :props="dictProps"
              :data="dictData"
              highlight-current
              :filter-node-method="filterNode"
              ref="tree2"
              node-key="id"
              :expand-on-click-node="false"
              :default-expanded-keys="openDict"
              @node-click="searchDict">
            </el-tree>
          </div>
        </div>
      </el-col>
      <el-col :span="19"  class="right-content">
        <el-col :span="24">
          <el-input v-model="queryText" placeholder="请输入关键字" style="width: 25%;" size="medium" clearable></el-input>
          <el-button type="primary"  @click="queryDict" size="small" v-if="checkButtonPerm('dict:list')"> 查询</el-button>

          <el-button type="danger"  @click="deleteDict()" size="small" style="float: right" v-if="checkButtonPerm('dict:delete')"> 删除</el-button>
          <el-button type="primary"   size="small"  @click="editDict()" style="float: right" v-if="checkButtonPerm('dict:update')">编辑</el-button>
          <el-button type="primary"  @click="addDict()" size="small" style="float: right" v-if="checkButtonPerm('dict:add')">新增</el-button>
        </el-col>
        <el-col :span="24">
          <el-table
              v-loading="loading"
              :data="tableData"
              max-height="370"
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
              <el-table-column prop="text" label="字典名称" width="">
                <template slot-scope="scope">
                  <el-button  type="text" class="clickName" @click="detailDic(scope.row)" v-if="checkButtonPerm('dict:get')">{{scope.row.text}}</el-button>
                  <span v-else>{{scope.row.text}}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="code"
                label="字典编码"
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
              :page-sizes="[10, 20, 30]"
              :page-size="page.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="page.total">
            </el-pagination>
          </div>
        </el-col>
      </el-col>
    </el-row>
    <dicDetail :detailVisible= "detailVisible" :detailForm="detailForm"  v-on:headCallBackDetail="callBackDetail">
    </dicDetail>
    <addEditDict :addEditVisible= "addEditVisible" :preLabel="preLabel" :codeEdit="codeEdit" :isState="isState" :propForm="propForm" :title="title" v-on:headCallBack="callBack"></addEditDict>
  </div>
</template>

<script>
  import addEditDict from './addEditDict';
  import dicDetail from './dicDetail';
  import request from '@/assets/utils/request';
  export default {
    name: 'dictionary',
    data: function () {
      return {
        loading: false, // 是否加载
        treeLoading: false, // 是否加载
        operated: false,
        isState: true,
        openDict: ['0'],
        filterText: '',
        queryText: '', // 查询输入的内容
        dictData: [{'id': '0', 'label': '字典', 'leaf': false, 'children': []}], // 字典树结构数据
        propForm: { // 新增与修改的prop数据
          id: '',
          pid: '',
          label: '',
          leaf: '',
          code: '',
          remark: ''
        },
        detailVisible: false,
        detailForm: { // 详情框
          id: '',
          createTime: '',
          updateTime: '',
          state: '',
          createUser: '',
          updateUser: '',
          pid: '',
          pcode: '',
          text: '',
          code: '',
          leaf: '',
          sortno: '',
          remark: ''
        },
        selectNode: {
          label: '',
          leaf: false,
          data: {
            id: '',
            pid: '',
            pcode: ''
          }
        },
        preLabel: '',
        codeEdit: false,
        addEditVisible: false, // 新增与编辑的页面可见性
        title: '', // 页面标题
        tableData: [], // 字典表格数据
        page: {
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        multipleSelection: [],
        dictProps: {
          label: 'label',
          children: 'children',
          isLeaf: 'leaf'
        },
        theButtons: []
      };
    },
    components: {
      addEditDict: addEditDict,
      dicDetail: dicDetail
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        // console.log(token);
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        this.receiveButtons();
        this.getDictData();
        this.searchAllDict();
      });
    },
    // activated: function () {
    //   this.getDictData();
    //   this.queryDict();
    // },
    watch: {
      filterText: function (val) {
        this.$refs.tree2.filter(val);
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
        return this.$checkButtonPermission(operate);
      },
      indexMethod (index) {
        return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);
      },
      getDicts () {
        let options = {
          params: {
            keywords: this.queryText
          }
        };
        request({
          url: '/dict/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.tableData = res.body.data.list;
          this.page.total = res.body.data.total;
        });
      },
      getDictData () { // 查询字典树
        this.treeLoading = true;
        request({
          url: '/dict/tree?leaf=false'
        }).then(res => {
          this.treeLoading = false;
          this.dictData[0].children = (res.data.data);
        });
      },
      searchDict (node) { // 查询字典列表
        console.log(node);
        // this.queryText = '';
        this.selectNode.leaf = node.leaf;
        console.log(this.selectNode.leaf);
        if (node.label !== '字典') {
          this.preLabel = node.label;
        } else {
          this.preLabel = '';
        }
        if (!node.leaf) {
          let pid = '';
          if (node.data !== undefined) {
            this.selectNode.data.id = node.data.id;
            this.selectNode.data.pid = node.data.pid;
            this.selectNode.data.pcode = node.data.pcode;
            this.selectNode.label = node.label;
            pid = node.data.id;
          } else {
            this.selectNode.data.id = '';
            this.selectNode.data.pid = '';
            this.selectNode.label = '字典';
            this.selectNode.data.pcode = '';
          }
          this.loading = true;
          let options = {
            params: {
              pid: pid,
              keywords: this.queryText
            }
          };
          request({
            url: '/dict/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
            params: options.params
          }).then(res => {
            this.loading = false;
            this.tableData = res.data.data.list;
            this.page.total = res.data.data.total;
          });
        } else {
          this.tableData = [];
        }
      },
      searchAllDict () { // 查询所有字典列表
        this.loading = true;
        request({
          url: '/dict/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
        }).then(res => {
          this.loading = false;
          this.tableData = res.data.data.list;
          this.page.total = res.data.data.total;
        });
      },
      queryDict () {
        this.loading = true;
        this.page.pageNum = 1;
        let options = {
          params: {
            pid: this.selectNode.data.id,
            pcode: this.selectNode.data.pcode,
            keywords: this.queryText
          }
        };
        request({
          url: '/dict/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.loading = false;
          this.tableData = res.data.data.list;
          this.page.total = res.data.data.total;
        });
      },
      deleteDict () { // 删除字典
        let self = this;
        if (this.multipleSelection.length > 1) {
          this.$message.error('请只选择一条记录');
        } else if (this.multipleSelection.length !== 0) {  // 当有选择项时
          this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let theid = this.multipleSelection[0].id; // 字典id
            request({
              url: '/dict/delete/' + theid
            }).then(res => {
              self.openDict.push(self.selectNode.data.id);
              self.getDictData();
              self.searchDict(self.selectNode);
            });
          }).catch(() => {
          });
        } else {
          self.$message({
            showClose: true,
            message: '请选择需要删除的项！',
            type: 'warning'
          });
        }
      },
      addDict () { // 新增字典
        this.propForm.id = '';
        this.propForm.pid = this.selectNode.data.id;
        this.propForm.text = '';
        this.propForm.leaf = '';
        this.propForm.code = '';
        this.propForm.remark = '';
        this.propForm.sortno = '';
        if (this.selectNode.leaf === true) {
          this.$message({
            showClose: true,
            message: '该节点已经是叶节点，无法新增！',
            type: 'warning'
          });
          return false;
        }
        this.isState = true;
        this.codeEdit = false;
        this.addEditVisible = true;
        this.title = '新增字典';
      },
      editDict () { // 编辑字典
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
          this.propForm.pid = pojo.pid;
          this.propForm.text = pojo.text;
          this.propForm.code = pojo.code;
          this.propForm.leaf = pojo.leaf;
          this.propForm.remark = pojo.remark;
          this.propForm.sortno = pojo.sortno;
          this.codeEdit = false;
          this.isState = false;
          this.addEditVisible = true;
          this.title = '编辑字典';
        }
      },
      filterNode (value, data) {  //  树结构的过滤函数
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      handleSelectionChange (val) { // 储存被选中的数据项
        this.multipleSelection = val;
      },
      callBack (msg, theoperated) { // 回调函数
        this.addEditVisible = false;
        if (msg !== '') {
          this.openDict.push(this.selectNode.data.id);
          this.getDictData();
          this.searchDict(this.selectNode);
        }
      },
      detailDic (row) {
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
        this.detailForm.pcode = row.pcode;
        this.detailForm.pid = row.pid;
        this.detailForm.pidName = row.pidName;
        this.detailForm.text = row.text;
        this.detailForm.code = row.code;
        this.detailForm.remark = row.remark;
        this.detailForm.sortno = row.sortno;
        this.detailForm.leaf = row.leaf;
        if (row.leaf === '0') {
          this.detailForm.leaf = '否';
        } else if (row.leaf === '1') {
          this.detailForm.leaf = '是';
        } else {
          this.detailForm.leaf = '';
        }
        this.detailVisible = true;
      },
      callBackDetail () {
        this.detailVisible = false;
      },
      handleSizeChange (val) { // 设置每页总共几条
        this.page.pageSize = val;
        this.searchDict(this.selectNode);
      },
      handleCurrentChange (val) { // 跳到设置的当前页
        this.page.pageNum = val;
        this.searchDict(this.selectNode);
      }
    }
  };
</script>

<style>
  #dic_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #DICT .el-tree-node__label {
    font-size: 12px;
  }
  #DICT th{
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #DICT .clickName{
    color: #606266;
    font-size: 12px;
  }
  #DICT .clickName:hover{
    color: #409EFF;
    text-decoration: underline;
  }
  #DICT input {
    height: 30px;
  }
  #DICT .el-table td{
    padding: 7px 0;
  }
  #DICT .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #DICT .el-row {
    /*padding: 10px 20px 20px 20px;*/
    margin: 15px;
    margin-top: 10px;
  }
  #DICT .el-button--text {
    width: auto;
    height: auto;
    padding: 9px 0px;
  }
  #DICT .left-content {
    padding-right: 15px;
    /*min-height: 400px;*/
  }
  #DICT .thefilterInput {
    width: 90%;
    margin-left: 5%;
    border-bottom: 1px solid #e5e5e5;
    padding-bottom: 10px
  }
  #DICT .the_left_tree {
    padding-top: 10px;
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
    background-color: #fff;
    /* overflow-y: auto;
     overflow-x: hidden;*/
  }
  #DICT .el-button {
    padding: 9px 29px;
  }
  #DICT .right-content > .el-col {
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
  }
  #DICT .right-content > .el-col:first-child {
    padding: 12px 15px 10px 15px;
  }
  #DICT .right-content > .el-col:last-child {
    padding-left: 10px;
    height: 75vh;
    padding-right: 10px;
    margin-top: 10px;
  }
  #DICT .el-form--inline .el-form-item {
    margin-right: 30px;
  }
  #DICT .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 10px;
  }
  /* #DICT .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #DICT .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #DICT .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  } */
  /*分页样式*/
  #DICT .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #DICT .pageDiv .page {
    float: right;
  }
  #DICT .el-pager {
    margin-left: 12px;
  }
  #DICT  .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #DICT .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #DICT .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #DICT .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #DICT .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }

  /**************/
  #DICT .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #DICT .el-select{
    width :100%;
  }
</style>
