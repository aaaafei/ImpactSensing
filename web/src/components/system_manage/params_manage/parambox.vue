
<template>
  <div id="PARAM">
    <!--<el-row>
      <el-col :span="24">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>参数管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
    </el-row>-->
    <el-row>
      <el-col :span="24">
          <div style="padding: 15px">
            <el-input v-model="queryText" placeholder="请输入关键字" style="width: 25%;" size="medium"></el-input>
            <el-button type="primary"  @click="queryParam" size="small" v-if="checkButtonPerm('param:list') "> 查询</el-button>

            <el-button type="danger"   @click="deleteParam()" size="small" style="float: right" v-if="checkButtonPerm('param:delete') "> 删除</el-button>
            <el-button type="primary"   @click="editParam" size="small" style="float: right" v-if="checkButtonPerm('param:update') ">编辑</el-button>
            <el-button type="primary"  @click="addParam" size="small" style="float: right" v-if="checkButtonPerm('param:add') ">新增</el-button>
          </div>
      </el-col>
      <el-col :span="24" style="padding: 10px 0 0 10px;margin-top: 10px;height: 75vh">
          <el-table
            v-loading="loading"
            :data="tableData"
            max-height="360"
            @selection-change="handleSelectionChange">
            <el-table-column
              type="selection"
              fixed
              width="40">
            </el-table-column>
            <el-table-column
              type="index"
              :index="indexMethod"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column prop="paramKey" label="参数名" width="150">
              <template slot-scope="scope">
                <el-button  type="text" class="clickName" @click="detailParam(scope.row)"  v-if="checkButtonPerm('param:get') ">{{scope.row.paramKey}}</el-button>
                <span v-else>{{scope.row.paramKey}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="paramValue"
              label="参数值"
              width="200">
            </el-table-column>
            <el-table-column
              prop="clazz"
              label="参数设置类"
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
      <addEditParm :addEditVisible= "addEditVisible" :propForm="propForm" :title="title" :isDisabled="isDisabled" :isState="isState" v-on:headCallBack="callBack">
      </addEditParm>
      <paramDetail :detailVisible= "detailVisible" :detailForm="detailForm"  v-on:headCallBackDetail="callBackDetail">
      </paramDetail>
    </el-row>
  </div>
</template>

<script>
  import addEditParm from './addEditParm';
  import paramDetail from './paramDetail';
  export default {
    name: 'params',
    data: function () {
      return {
        loading: false, // 是否加载
        operated: false,
        propForm: { // 新增与编辑的prop数据
          id: '',
          paramKey: '',
          paramValue: '',
          clazz: '',
          remark: ''
        },
        detailVisible: false,
        detailForm: { // 详情框
          id: '',
          paramKey: '',
          paramValue: '',
          clazz: '',
          remark: '',
          createTime: '',
          updateTime: '',
          state: '',
          createUser: '',
          updateUser: ''
        },
        addEditVisible: false, // 新建与编辑页面可见性
        isDisabled: false,
        isState: true,
        title: '', // 页面标题
        tableData: [],
        page: {
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        queryText: '',
        multipleSelection: [], // 选择的数据项
        theButtons: []
      };
    },
    components: {
      paramDetail: paramDetail,
      addEditParm: addEditParm // 新增与编辑的弹框
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        // this.receiveButtons();
        this.queryParam();
      });
    },
    // activated: function () {
    //   this.queryParam();
    // },
    methods: {
      checkButtonPerm (operate) {
        return this.$checkButtonPermission(operate);
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
      queryParam () {
        this.page.pageNum = 1;
        this.seachParam();
      },
      seachParam () {
        this.loading = true;
        let options = {
          params: {
            keywords: this.queryText
          }
        };
        this.$request({
          url: '/param/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      },
      addParam () { // 新增
        this.propForm.id = ''; // 根据该rows节点对象获取数据
        this.propForm.paramKey = '';
        this.propForm.paramValue = '';
        this.propForm.clazz = '';
        this.propForm.remark = '';
        this.addEditVisible = true;
        this.isDisabled = false;
        this.isState = true;
        this.title = '新增参数';
      },
      editParam () { // 编辑角色
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
          // console.log(this.multipleSelection[0])
          var pojo = this.multipleSelection[0];
          // var pojo = this.tableData[index];
          this.propForm.id = pojo.id; // 根据该rows节点对象获取数据
          this.propForm.paramKey = pojo.paramKey;
          this.propForm.paramValue = pojo.paramValue;
          this.propForm.clazz = pojo.clazz;
          this.propForm.remark = pojo.remark;
          this.isDisabled = false;
          this.isState = false;
          this.addEditVisible = true;
          this.title = '编辑参数';
        }
      },
      deleteParam () { // 删除参数
        if (this.multipleSelection.length === 1) {  // 当有选择项时
          this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            // alert(this.multipleSelection[0].id)
            this.$request({
              url: '/param/delete/' + this.multipleSelection[0].id
            }).then(res => {
              this.$message({
                type: 'success',
                message: res.data.message
              });
              this.seachParam();
            });
          }).catch(() => {
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
            message: '只需选择一条删除的项！',
            type: 'warning'
          });
        }
      },
      detailParam (row) {
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
        this.detailForm.paramKey = row.paramKey;
        this.detailForm.paramValue = row.paramValue;
        this.detailForm.clazz = row.clazz;
        this.detailForm.remark = row.remark;
        this.detailVisible = true;
      },
      handleSelectionChange (val) { // 储存被选中的数据项
        this.multipleSelection = val;
        console.log(this.multipleSelection);
      },
      callBack (msg) { // 回调函数
        if (msg !== '') {
          this.seachParam();
        }
        this.addEditVisible = false;
      },
      callBackDetail () {
        this.detailVisible = false;
      },
      handleSizeChange (val) { // 设置每页总共几条
        this.page.pageSize = val;
        this.page.pageNum = 1;
        this.seachParam();
      },
      handleCurrentChange (val) { // 跳到设置的当前页
        this.page.pageNum = val;
        this.seachParam();
      }
    }
  };
</script>

<style>
  #PARAM th{
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #PARAM .clickName{
    color: #606266;
    font-size: 12px;
  }
  #PARAM .clickName:hover{
    color: #409EFF;
    text-decoration: underline;
  }
  #PARAM .el-table td {
    padding: 7px 0;
    color: #666666;
  }
  #PARAM .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #PARAM .el-button {
    padding: 9px 29px;
  }
  #PARAM .el-row {
    margin: 15px;
    margin-top: 10px;
  }
  #PARAM  .el-col{
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
  }
  #PARAM input {
    height: 30px;
  }
  #PARAM .el-button--text{
    width: auto;
    height: auto;
    padding: 9px 0px;
  }
  /* #PARAM .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #PARAM .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #PARAM .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  } */

  /*分页样式*/
  #PARAM .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #PARAM .pageDiv .page {
    float: right;
  }
  #PARAM .el-pager {
    margin-left: 12px;
  }
  #PARAM .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #PARAM .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #PARAM .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #PARAM .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #PARAM .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }

  /**************/
  #PARAM .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
</style>
