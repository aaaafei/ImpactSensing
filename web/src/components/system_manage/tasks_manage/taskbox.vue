<template>

  <div id="TASK">
    <el-row>
      <el-col :span="24" class="right-content">
        <el-col :span="24">
          <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini" ref="queryForm">
            <el-form-item label="关键字:" prop="keywords" >
              <el-input v-model="formInline.keywords" placeholder="请输入关键字"></el-input>
            </el-form-item>
            <el-form-item label="类型:" prop="type">
              <el-select v-model="formInline.type" clearable filterable placeholder="请选择任务类型">
                <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item >
              <el-button type="primary" @click="queryTask('queryForm',true)" v-if="checkButtonPerm('job:list') ">查询</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24" class="middle-operate">
          <el-button type="primary" size="small"  @click="addTask" style="float: left" v-if="checkButtonPerm('job:add') ">新增</el-button>
          <!--<el-button type="primary" size="small"  @click="recoverTask" > 恢复</el-button>-->
          <!-- <el-button type="primary" size="small"  @click="showCron" > 显示</el-button> -->
          <el-button type="primary" size="small"  @click="runTask" v-if="checkButtonPerm('job:run') "> 执行</el-button>
          <el-button type="primary" size="small"  @click="taskHistory" style="background-color: #74be41;border-color: #74be41;" v-if="checkButtonPerm('job:listLog') "> 历史</el-button>
          <!--v-if="checkButtonPerm('job:listLog') "-->
        </el-col>
        <el-col :span="24">
          <el-table
            v-loading="loading"
            ref="multipleTable"
            :data="tableData3"
            tooltip-effect="dark"
            max-height="320"
            style="width: 100%"
            @selection-change="handleSelectionChange">
            <el-table-column
              type="selection"
              width="42">
            </el-table-column>
            <el-table-column
              type="index"
              label="序号"
              :index="indexMethod"
              width="50">
            </el-table-column>
            <el-table-column prop="address" label="执行地址" >
              <!--<template slot-scope="scope">-->
                <!--<el-button  type="text" class="clickName" @click="detailUser(scope.row)">{{scope.row.realName}}</el-button>-->
              <!--</template>-->
            </el-table-column>
            <el-table-column
              prop="params"
              label="参数">
            </el-table-column>
            <el-table-column
              prop="cron"
              label="cron表达式"
              width="">
            </el-table-column>
            <el-table-column
              prop="runTime"
              label="上次运行时间"
              width="">
            </el-table-column>
            <el-table-column
              prop="type"
              label="任务类型"
              width="">
            </el-table-column>
            <el-table-column
              prop="status"
              label="任务状态"
              width="">
            </el-table-column>
            <el-table-column
              prop="remark"
              label="备注"
              width="">
            </el-table-column>
            <!--<el-table-column
              prop="state"
              label="记录状态"
              :formatter="formatState"
              width="">
            </el-table-column>-->
            <el-table-column
              label="操作"
              fixed="right"
              width="160">
              <template slot-scope="scope" class="theoprations">
                <el-button  @click="recoverTask(scope.$index, scope.row)" type="text" size="mini" v-if="scope.row.status === '1'" v-show="checkButtonPerm('job:resume') ">恢复</el-button>
                <el-button  @click="pauseTask(scope.$index, scope.row)" size="mini" type="text" v-else v-show="checkButtonPerm('job:pause') ">暂停</el-button>
                <el-button @click="editTask(scope.$index,scope.row)" type="text" size="mini" style=" margin-left: 0px;" v-if="checkButtonPerm('job:update') ">编辑</el-button>
                <el-button type="text" size="mini" @click="deleteTask(scope.$index, scope.row)" style=" margin-left: 0px;" v-if="checkButtonPerm('job:delete') ">删除</el-button>
                <el-button type="text" size="mini" @click="reloadTask(scope.row)" style=" margin-left: 0px;" v-if="checkButtonPerm('job:reload') ">重新加载</el-button>
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
    <el-dialog  width="95%" title="任务历史" :visible="historyTaskVisible" @close="close('')">
      <historyTask :typeOptions="typeOptions" :historyRefresh="historyRefresh"></historyTask>
    </el-dialog>
    <addEditTask :addEditVisible= "addEditVisible" :propForm="propForm" :title="title" :isDisabled="isDisabled" :typeOptions="typeOptions" :isState="isState" v-on:headCallBack="callBack"></addEditTask>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';
  import addEditTask from './addEditTask';
  import historyTask from './historyTask';
  
  // import userAllMenus from './userAllMenus';
  // import userDetail from './userDetail';
  // import organization from '../organization';
  // import posts from './posts';
  export default {
    name: 'user',
    data: function () {
      return {
        loading: false, // 用户列表是否加载
        propForm: { // 新增与编辑的prop数据
          id: '',
          address: '',
          params: '',
          cron: '',
          type: '',
          remark: ''
        },
        addEditVisible: false, // 新建编辑页面可见性
        cronVisible: false,
        isDisabled: false,
        isState: true, // 新增时显示验证状态，编辑时不显示验证状态
        isAdd: false,
        title: '', // 页面标题
        formInline: { // 条件查询的数据
          keywords: '',
          type: ''
        },
        tableData3: [], // 用户列表的数据
        page: { // 分页的数据信息
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        typeOptions: [], // 任务类型
        historyRefresh: false,
        multipleSelection: [], // 用户表的选中项
        // historyTaskVisible: 'posthide',
        historyTaskVisible: false,
        theButtons: []
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      addEditTask: addEditTask,
      historyTask: historyTask
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        this.receiveButtons();
        this.getTaskType();
        this.seachAllTask();
      });
    },
    activated: function () {
      // this.seachOrg();
      // this.seachAllUser();
    },
    watch: {
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
      seachAllTask () { // 根据组织查询用户列表 或者 （刷新列表）
        this.loading = true;
        this.$request({
          url: '/job/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.data.total;
          this.tableData3 = res.data.data.list;
        });
      },
      getTaskType () { // 获取任务类型列表
        this.$request({
          url: '/dict/map/f_job_type'
        }).then(res => {
          let theTypes = res.data.data.f_job_type;
          let theTypes1 = [];
          for (var item in theTypes) {
            let theTypes2 = {};
            // console.log(item);// 得到键
            // console.log(theTypes[item]);// 得到键对应的值
            theTypes2.label = theTypes[item];
            theTypes2.value = item;
            theTypes1.push(theTypes2);
          }
          console.log(theTypes1);
          this.typeOptions = theTypes1;
        });
      },
      queryTask (formName, isTrue) { // 按条件查询用户列表
        if (isTrue) { // 当条件查询时，按下查询按钮或者切换每页的条数时，默认显示第一页。
          this.page.pageNum = 1;
        }
        let options = {
          headers: {
            Authorization: this.$store.getters.getToken
          },
          params: {}
        };
        var Self = this;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            if (Self.formInline.keywords !== '') {
              options.params.keywords = Self.formInline.keywords;
            }
            if (Self.formInline.type !== '') {
              options.params.type = Self.formInline.type;
            }
            Self.loading = true; // 开始加载
            Self.$request({
              url: '/job/list?pageSize=' + Self.page.pageSize + '&pageNum=' + Self.page.pageNum,
              params: options.params
            }).then(res => {
              Self.loading = false; // 查询结束后，停止加载
              Self.page.total = res.data.data.total;
              Self.tableData3 = res.data.data.list;
            });
          } else {
            return false;
          }
        });
      },
      deleteTask (index, row) { // 删除任务
        this.$confirm('此操作将永久删除任务, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let theid = row.id;
          this.$request({
            url: '/job/delete/' + theid
          }).then(res => {
            this.$message({
              type: 'success',
              message: res.data.message
            });
            this.queryTask('queryForm', false);
          });
        }).catch(() => {
        });
      },
      reloadTask (row) { // 重新加载任务
        let theid = row.id;
        this.$request({
          url: '/job/reload/' + theid
        }).then(res => {
          this.$message({
            type: 'success',
            message: res.data.message
          });
          this.queryTask('queryForm', false);
        });
      },
      addTask () { // 新增
        this.propForm.id = '';
        this.propForm.address = '';
        this.propForm.params = '';
        this.propForm.cron = '';
        this.propForm.type = '';
        this.propForm.remark = '';
        this.isAdd = true;
        this.addEditVisible = true;
        this.isDisabled = false;
        this.isState = true;
        this.title = '新增任务';
      },
      editTask (index, row) { // 编辑
        var pojo = row;
        this.propForm.id = pojo.id;
        this.propForm.address = pojo.address;
        this.propForm.params = pojo.params;
        this.propForm.cron = pojo.cron;
        this.propForm.type = pojo.type;
        this.propForm.remark = pojo.remark;
        this.isAdd = false;
        this.addEditVisible = true;
        this.isDisabled = false;
        this.isState = false;
        this.title = '编辑任务';
      },
      runTask () { // 执行任务
        if (this.multipleSelection.length !== 0) {  // 当有选择项时
          this.$confirm('确定执行选中的任务?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let options = {
              params: {}
            };
            var ids = '';
            for (let i = 0; i < this.multipleSelection.length; i++) {
              ids = ids + this.multipleSelection[i].id + ',';
            }
            ids = ids.substring(0, ids.length - 1);
            options.params.ids = ids;
            this.$request({
              url: '/job/run',
              params: options.params
            }).then(res => {
              this.$message({
                type: 'success',
                message: res.data.message
              });
              this.queryTask('queryForm', false);
            });
          }).catch(() => {
          });
        } else if (this.multipleSelection.length === 0) {
          this.$message({
            showClose: true,
            message: '请选择需要暂停的任务！',
            type: 'warning'
          });
        }
      },
      // showCron () { // 新增
      //   this.cronVisible = true;
      // },
      recoverTask (index, row) { // 恢复任务
        this.$confirm('确定恢复该任务?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let options = {
            params: {}
          };
          options.params.ids = row.id;
          this.$request({
            url: '/job/resume',
            params: options.params
          }).then(res => {
            this.$message({
              type: 'success',
              message: res.data.message
            });
            this.queryTask('queryForm', false);
          });
        }).catch(() => {
        });
      },
      pauseTask (index, row) { // 暂停任务
        this.$confirm('确定暂停该任务?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let options = {
            params: {}
          };
          options.params.ids = row.id;
          this.$request({
            url: '/job/pause',
            params: options.params
          }).then(res => {
            this.$message({
              type: 'success',
              message: res.data.message
            });
            this.queryTask('queryForm', false);
          });
        }).catch(() => {
        });
      },
      handleSelectionChange (val) { // 储存用户列表中被勾选的数据项
        this.multipleSelection = val;
        console.log(this.multipleSelection);
      },
      callBack (msg) { // 回调函数，根据子组件传回的参数进行操作
        this.addEditVisible = false; // 新增编辑
        if (msg !== '') {
          this.queryTask('queryForm', false);
        }
      },
      handleSizeChange (val) { // 设置每页总共几条
        console.log(`每页 ${val} 条`);
        // alert(val)
        this.page.pageSize = val;
        this.page.pageNum = 1;
        this.queryTask('queryForm', true);
      },
      handleCurrentChange (val) { // 跳到设置的当前页
        this.page.pageNum = val;
        this.queryTask('queryForm', false);
      },
      taskHistory () { // 打开历史任务表
        this.historyRefresh = true;
        this.historyTaskVisible = true;
      },
      close () { // 关闭历史任务
        this.historyRefresh = false;
        this.historyTaskVisible = false;
        this.cronVisible = false;
      }
      // ,
      // closeCron () { // 关闭cron编辑器
      //   this.cronVisible = false;
      // }
    }
  };
</script>

<style >
  #TASK .historyTaskBox {
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
  #TASK .posthide {
    display: none;
  }
  #TASK .postshow {
    display: block;
  }
  #TASK label {
    font-size: 12px;
  }
  #TASK .el-tree-node__label {
    font-size: 12px;
  }
  #TASK th {
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #TASK .el-dialog__body {
    padding-top: 15px;
  }
  #TASK input {
    height: 30px;
  }
  #TASK .el-table td{
    padding: 7px 0;
    color: #666666;
  }
  #TASK .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #TASK .el-row {
    margin: 15px;
    margin-top: 10px;
  }
  #TASK .left-content {
    padding-right: 15px;
    /*min-height: 400px;*/
  }
  #TASK .el-button {
    /*width: 84px;
    height: 32px;*/
    padding: 9px 29px;
  }
  #TASK .right-content > .el-col:not(.middle-operate) {
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
  }
  #TASK .right-content > .el-col:first-child {
    padding: 12px 0px 0px 15px;
  }
  #TASK .right-content > .el-col:last-child {
    padding-left: 10px;
    height: 68vh;
    padding-right: 10px;
  }
  #TASK .el-form--inline .el-form-item {
    margin-right: 30px;
  }
  #TASK .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 15px;
  }
  #TASK .el-button--text{
    padding: 9px 0px;
  }
  #TASK .thefilterInput {
    width: 90%;
    margin-left: 5%;
    border-bottom: 1px solid #e5e5e5;
    padding-bottom: 10px
  }
  #TASK .the_left_tree {
    padding-top: 10px;
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
    background-color: #fff;
    /* overflow-y: auto;
     overflow-x: hidden;*/
  }
  #TASK .middle-operate {
    text-align: right;
    padding: 8px 0;
  }
  #TASK .clickName{
    color: #606266;
    font-size: 12px;
    width: auto !important;
    height: auto !important;
  }
  #TASK .clickName:hover{
    color: #409EFF;
    text-decoration: underline;
  }
  /* #TASK .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #TASK .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #TASK .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  } */
  #TASK .el-select {
    width: 100%;
  }
  /*分页样式*/
  #TASK .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #TASK .pageDiv .page {
    float: right;
  }
  #TASK .el-pager {
    margin-left: 12px;
  }
  #TASK .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #TASK .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #TASK .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #TASK .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #TASK .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }

</style>
