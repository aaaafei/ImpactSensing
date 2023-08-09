<template>
  <div id="THEMENU">
    <!--<el-row>
      <el-col :span="24" style="padding: 15px 0">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>菜单管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
    </el-row>-->
    <el-row >
      <el-col :span="5" class="left-content">
        <div id="menuTree" class="grid-content bg-purple the_left_tree">
          <!-- <div class="thefilterInput">
          <el-input placeholder="输入菜单名进行过滤" v-model="filterText" size="small"></el-input>
          </div> -->
          <div style="width: 100%;height: 68vh;overflow: auto;padding-top: 10px;">
            <el-tree
              class="filter-tree"
              v-loading="treeLoading"p
              :data="treeData"
              node-key="id"
              :props="defaultProps"
              default-expand-all
              highlight-current
              :filter-node-method="filterNode"
              :expand-on-click-node="false"
              ref="tree"
              @node-click="seachMenuList"
              style="display: inline-block;min-width: 11.8vw">
            </el-tree>
          </div>
        </div>
      </el-col>
      <el-col :span="19"  class="right-content">
        <el-row>
        <el-col :span="24">
          <el-input v-model="queryText" placeholder="请输入菜单名称" style="width: 25%;" size="small"></el-input>
          <el-button type="primary"  @click="queryMenu" size="small" v-if="checkButtonPerm('menu:list') "> 查询</el-button>

          <!-- <el-button type="danger"  @click="deleteMenu()" size="small" style="float: right" v-if="checkButtonPerm('menu:delete') "> 删除</el-button>  -->
          <el-button type="primary"  @click="addMenu()" size="small" style="float: right" v-if="checkButtonPerm('menu:add') ">新增</el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-table v-loading="loading" :data="tableData" max-height="520" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="30"></el-table-column>
            <el-table-column type="index" :index="indexMethod" label="序号" width="45"></el-table-column>
            <el-table-column prop="name" label="名称" width="140">
              <template slot-scope="scope">
                <el-button  type="text" class="clickName" @click="detailMenu(scope.row)" v-if="false">{{scope.row.menuname}}</el-button>
                <span v-else>{{scope.row.menuname}}</span>
              </template>
            </el-table-column> 
            <el-table-column prop="iconClass" label="图标" width="150"></el-table-column>
            <el-table-column label="菜单类型" width="80">
                <template slot-scope="scope">
                    {{menuTypeMap[scope.row.type]}}
                </template>
            </el-table-column> 
            <el-table-column prop="url" label="菜单链接地址" width="140"></el-table-column>
            <el-table-column prop="sortNo" label="排序" width="50"></el-table-column>
            <el-table-column prop="remark" label="备注"></el-table-column>
            <el-table-column label="操作" width="203">
              <template slot-scope="scope">
                  <el-button size="mini" type="success" plain @click="editMenu(scope.row)">编辑</el-button>
                  <el-button size="mini" type="danger" @click="deleteMenu(scope.row)">删除</el-button>
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
          </el-row>
      </el-col>
    </el-row>

    <addEditMenu :addEditVisible= "addEditVisible" :propForm="propForm" :title="title" :isDisabled="isDisabled" :isState="isState" :isShowOrg="isShowOrg" v-on:headCallBack="callBack"></addEditMenu>

    <menuDetail :detailVisible= "detailVisible" :detailForm="detailForm"  v-on:headCallBackDetail="callBackDetail"></menuDetail>

  </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  import addEditMenu from './addEditMenu';
  import menuDetail from './menuDetail';
  export default { 
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
        detailVisible: false,
        detailForm: { // 详情框
          id: '',
          createTime: '',
          updateTime: '',
          state: '',
          createUser: '',
          updateUser: '',
          typeName: '',
          url: null,
          leaf: '',
          type: null,
          outLink: null,
          parentId: '',
          pname: '',
          name: '',
          code: '',
          iconClass: '',
          sortNo: '',
          remark: ''
        },
        propForm: {
          id: '',
          menuname: '',
          code: '',
          iconClass: '',
          type: '',
          parentId: '',
          pname: '',
          url: '',
          leaf: '',
          outLink: '',
          sortNo: '',
          remark: ''
        },
        isAdd: false,
        page: {
          pageNum: 1,
          pageSize: 10,
          total: 0
        },
        addEditVisible: false, // 新增与编辑的页面可见性
        isDisabled: false,
        isState: true,
        isShowOrg: true,
        title: '', // 页面标题
        treeNode: { // 选中的菜单节点信息
          id: '', // 菜单id
          label: '', // 菜单名称
          leaf: false,
          theCheckId: ''
        },
        tableData: [], // 菜单表
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        theButtons: [],
        menuTypeMap: {'10': '模块', '2': '页面'}
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      addEditMenu: addEditMenu,
      menuDetail: menuDetail
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        // this.receiveButtons();
        this.seachAllMenuList();
        this.seachMenuTree();
      });
    },
    // activated: function () {
    //   this.seachMenuTree();
    //   this.seachAllMenuList();
    // },
    watch: {
      filterText (val) { // 监听左侧树结构的过滤操作
        this.$refs.tree2.filter(val);
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
      seachMenuTree () { // 查询菜单树
        this.treeLoading = true;
        this.$request({
          url: '/menu/get/all',
          params: {judge: 1}
        }).then(res => {
          this.treeLoading = false;
          this.treeData = res.data.result_data;
          let Self = this;
          setTimeout(function () {
            Self.$refs.tree.setCurrentKey(Self.treeNode.id);
          }, 500);
        });
      },
      seachMenuList (data) { // 查询菜单列表
        this.loading = true;
        this.treeNode.leaf = data.leaf;
        if (!data.leaf) {
          if (!data.isTrue) { 
            this.page.pageNum = 1;
            // this.queryText = '';
          }
          this.treeNode.id = data.id;
          this.treeNode.label = data.label;
          let options = {
            params: {}
          };
          if (data.id !== 0) {
            options.params.parentId = data.id;
          } else {
            options.params.parentId = 0;
          }
          options.params.menuname = this.queryText;
          // var data={}//定义一个json对象
          // data['result']={name: 'caod'};//添加属性
          // console.log(this.options);
          this.$request({
            url: '/menu/getMenuByParent/' + options.params.parentId + '/' + this.page.pageNum + '/' + this.page.pageSize,
            method: 'post',
            data: options.params
          }).then(res => {
            this.operated = false; // 每次查询结束后把操作成功项归原
            this.loading = false; // 查询结束后，停止加载
            this.page.total = res.data.result_data.total;
            this.tableData = res.data.result_data.list;
          });
        }
      },
      seachAllMenuList () {
        this.loading = true;
        let options = {
          params: {
            menuname: this.queryText
          }
        };
        var parentId = 0;
        if (this.treeNode.id) {
          parentId = this.treeNode.id;
        }
        this.$request({
          url: '/menu/getMenuByParent/' + parentId + '/' + this.page.pageNum + '/' + this.page.pageSize,
          method: 'post',
          data: options.params
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.result_data.total;
          this.tableData = res.data.result_data.list;
        });
      },
      queryMenu () { // 按条件查询菜单列表
        this.loading = true; // 开始加载
        this.page.pageNum = 1;
        let options = {
          params: {}
        };
        if (this.queryText !== '') {
          options.params.menuname = this.queryText;
        }
        if (this.treeNode.id !== '' && this.treeNode.id !== 0) { // 菜单id
          options.params.parentId = this.treeNode.id;
        } else {
          options.params.parentId = 0;
        }
        this.$request({
          url: '/menu/getMenuByParent/' + options.params.parentId + '/' + this.page.pageNum + '/' + this.page.pageSize,
          method: 'post',
          data: options.params
        }).then(res => {
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.result_data.total;
          this.tableData = res.data.result_data.list;
        });
      },
      checkMenuChild(treeId,treeNode){  
            if(treeNode.id == treeId){
                if(treeNode.children != undefined && treeNode.children != null && treeNode.children.length > 0){ 
                  return true; 
                }
                else{
                  return false;
                }
            }else{ 
                if(treeNode.children != undefined && treeNode.children != null && treeNode.children.length > 0){ 
                    let flag = false;
                    for(var i=0;i<treeNode.children.length;i++){ 
                        flag = this.checkMenuChild(treeId,treeNode.children[i]);  
                        if(flag){
                            break;
                        }

                    } 
                    return flag; 
                }
                else{
                    return false;
                }
            } 
      },
      checkMenuHaveChild(treeId){
          for(var n = 0;n < this.treeData.length; n++){
            var flag = this.checkMenuChild(treeId,this.treeData[n]);
            if(flag) return true;
          }
          return false;
      },
      deleteMenu (rowData) { // 删除菜单
        // if (this.multipleSelection.length >0 ) {  // 当有选择项时
          this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => { 
            //let theid = this.multipleSelection[0].id; // 菜单id
            let theid = rowData.id;
            //检测是否还有子菜单，有子菜单不能删除
            if(this.checkMenuHaveChild(theid)){
                this.$alert('将此菜单的子菜单删除后才可删除！','提示');
                return false;
            }

            this.$request({
              url: '/menu/deleteMenu/' + theid
            }).then(res => {
              this.$message({
                type: 'success',
                message: res.data.result_desc
              });
              if (this.treeNode.id !== '') {  // 选择了菜单树节点时
                var data = {
                  id: this.treeNode.id,
                  label: this.treeNode.label,
                  leaf: false
                };
                data.isTrue = true;
                this.seachMenuList(data);
              } else { // 用户还未选择菜单树节点时
                this.seachAllMenuList();
              }
              this.seachMenuTree();
            });
          }).catch(() => {
          });
        // } else   {
        //    this.$alert('请选择需要删除的数据！','提示');
        // }  
      },
      addMenu () { // 新增菜单
        if (this.treeNode.leaf === true) {
          this.$message({
            showClose: true,
            message: '该节点已经是叶节点，无法新增！',
            type: 'warning'
          });
          return false;
        }
        this.propForm.id = '';
        this.propForm.menuname = '';
        this.propForm.code = '';
        this.propForm.iconClass = '';
        this.propForm.type = '';
        if (this.treeNode.id !== '') {
          // this.isShowOrg = false;
        }
        if (this.treeNode.id === 0) {
          // this.propForm.parentId = '';
          // this.propForm.pname = '无';
          // // this.isShowOrg = true;
          // this.$message({
          //   showClose: true,
          //   message: '本节点为系统总称，不属于菜单，故默认为新增一级菜单！',
          //   type: 'warning'
          // });
          // return false;
        } else {
        }
        this.propForm.parentId = this.treeNode.id;
        this.propForm.pname = this.treeNode.label;
        this.propForm.url = '';
        this.propForm.leaf = '';
        this.propForm.outLink = '';
        this.propForm.sortNo = '';
        this.propForm.remark = '';
        this.isAdd = true;
        this.isDisabled = false;
        this.isState = true;
        this.addEditVisible = true;
        this.title = '新增菜单';
      },
      editMenu (rowData) { // 编辑菜单    
        var pojo = rowData;  
        this.propForm.id = pojo.id;
        this.propForm.menuname = pojo.menuname;
        this.propForm.code = pojo.code;
        this.propForm.iconClass = pojo.iconClass;
        this.propForm.type = pojo.type;
        this.propForm.parentId = pojo.parentId;
        this.propForm.pname = this.getTreeName(pojo.parentId); 
        this.propForm.url = pojo.url;
        this.propForm.leaf = pojo.leaf;
        this.propForm.outLink = pojo.outLink;
        this.propForm.sortNo = pojo.sortNo;
        this.propForm.remark = pojo.remark;
        this.isAdd = false;
        this.isState = false;
        this.isDisabled = false;
        this.addEditVisible = true;
        this.title = '编辑菜单';  
      },
      getTreeNameById(treeId,treeNode){  
            if(treeNode.id== treeId){
                return treeNode.label; 
            }else{
                if(treeNode.children==undefined || treeNode.children==null || treeNode.children.length == 0){ 
                  return ''; 
                }else{
                    let nodeName='';
                    for(var i=0;i<treeNode.children.length;i++){
                        nodeName = this.getTreeNameById(treeId,treeNode.children[i]);
                        if(nodeName!=''){
                            return nodeName;
                        } 
                    }
                    return nodeName;
                }
            } 
      },
      getTreeName(treeId){
        for(var n = 0;n < this.treeData.length; n++){
            var name = this.getTreeNameById(treeId,this.treeData[n]);
            if(name!='') return name;
          }
        return '';
      },
      filterNode (value, data) {  //  树结构的过滤函数
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      handleSelectionChange (val) { // 储存被选中的数据项
        this.multipleSelection = val; 
      },
      callBack (msg) { // 回调函数
        this.addEditVisible = false;
        if (msg !== '') {
          if (this.treeNode.id !== '') {  // 选择了菜单树节点时
            var data = {
              id: this.treeNode.id,
              label: this.treeNode.label,
              leaf: false
            };
            data.isTrue = true;
            this.seachMenuList(data);
          } else { // 用户还未选择菜单树节点时
            this.seachAllMenuList();
          }
          this.seachMenuTree();
        }
      },
      detailMenu (row) {
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
        this.detailForm.typeName = row.typeName;
        this.detailForm.parentId = row.parentId;
        this.detailForm.pname = row.pname;
        this.detailForm.name = row.name;
        this.detailForm.code = row.code;
        this.detailForm.iconClass = row.iconClass;
        this.detailForm.remark = row.remark;
        this.detailForm.sortNo = row.sortNo;
        this.detailForm.url = row.url;
        this.detailForm.leaf = row.leaf;
        this.detailForm.type = row.type;
        this.detailForm.outLink = row.outLink;
        this.detailVisible = true;
      },
      callBackDetail () {
        this.detailVisible = false;
      },
      handleSizeChange (val) { // 设置每页总共几条 
        this.page.pageSize = val; 
        this.pageSearch();
      },
      handleCurrentChange (val) { // 跳到设置的当前页 
        this.page.pageNum = val;
        this.pageSearch();
      },
      pageSearch () { // 分页查询
        this.loading = true;
        let options = {
          params: {}
        };
        if (this.treeNode.id !== '' && this.treeNode.id !== 0) {
          options.params.parentId = this.treeNode.id;
        } else {
          options.params.parentId = 0;
        }
        options.params.menuname = this.queryText;
        this.$request({
          url: '/menu/getMenuByParent/' + options.params.parentId + '/' + this.page.pageNum + '/' + this.page.pageSize,
          method: 'post',
          data: options.params
        }).then(res => {
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.result_data.total;
          this.tableData = res.data.result_data.list;
        }); 
      }
    }
  };
</script>

<style>
  #menuTree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #THEMENU .el-tree-node__label {
    font-size: 12px;
  }
  #THEMENU th{
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #THEMENU .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  #THEMENU .clickName{
    color: #606266;
    font-size: 12px;
    text-decoration: underline;
  }
  #THEMENU .clickName:hover{
    color: #409EFF;
  }
  #THEMENU input {
    height: 30px !important;
  }
  #THEMENU .el-table td{
    padding: 7px 0;
    color: #666666;
  }
  #THEMENU .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #THEMENU .el-row {
   /* padding: 10px 20px 20px 20px;*/
    margin: 15px;
    margin-top: 10px;
  }
  #THEMENU .left-content {
    padding-right: 15px;
    /*min-height: 400px;*/
  }
  #THEMENU .the_left_tree {
    padding-top: 10px;
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #ddd; */
    background-color: #fff;
    /* overflow-y: auto;
     overflow-x: hidden;*/
  }
  #THEMENU .thefilterInput {
    width: 90%;
    margin-left: 5%;
    border-bottom: 1px solid #e5e5e5;
    padding-bottom: 10px
  }
  #THEMENU .el-button {
    padding: 9px 29px;
  }
  #THEMENU .el-button--text{
    width: auto;
    height: auto;
    padding: 9px 0px;
  }
  #THEMENU .right-content > .el-col {
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
  }
  #THEMENU .right-content > .el-col:first-child {
    padding: 12px 15px 10px 15px;
  }
  #THEMENU .right-content > .el-col:last-child {
    padding-left: 10px;
    height: 63vh;
    padding-right: 10px;
    margin-top: 10px;
  }
  #THEMENU .el-form--inline .el-form-item {
    margin-right: 30px;
  }
  #THEMENU .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 10px;
  }
  /* #THEMENU .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #THEMENU .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #THEMENU .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  } */
  /*分页样式*/
  #THEMENU .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #THEMENU .pageDiv .page {
    float: right;
  }
  #THEMENU .el-pager {
    margin-left: 12px;
  }
  #THEMENU  .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #THEMENU .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #THEMENU .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #THEMENU .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #THEMENU .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }

  /**************/
  #THEMENU .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
  #THEMENU .el-select{
    width :100%;
  }
</style>
