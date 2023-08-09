<template>
  <div>
    <div style="margin-top: 15px">
      <el-row v-if="wfTaskStateVisible">
        <el-radio-group v-model="wfTaskState" size="medium" @change="searchWfTasks">
          <el-radio-button label="待办"></el-radio-button>
          <el-radio-button label="已办"></el-radio-button>
          <el-radio-button label="全部"></el-radio-button>
        </el-radio-group>
      </el-row>
      <el-row>
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item label="发起人">
            <el-input v-model="detailForm.starter" placeholder="发起人"></el-input>
          </el-form-item>
          <el-form-item label="发起时间">
            <el-col :span="8">
              <el-form-item prop="date1">
                <el-date-picker type="date" placeholder="开始日期" v-model="startTime" style="width: 100%;"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col class="line" :span="1">-</el-col>
            <el-col :span="8">
              <el-form-item prop="date2">
                <el-time-picker type="date" placeholder="结束日期" v-model="endTime" style="width: 100%;"></el-time-picker>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchWfTasks">查询</el-button>
          </el-form-item>
        </el-form>
      </el-row>
    </div>
    <el-row :gutter="20">
      <el-table
        v-loading="loading1"
        :data="undoWfTasks"
        max-height="350"
        style="">
        <el-table-column type="selection" width="45"></el-table-column>
        <el-table-column type="index" :index="indexMethod" label="序号" width="50"></el-table-column>
        <el-table-column prop="title" label="名称" width="200"></el-table-column>
        <el-table-column prop="taskName" label="任务名" width="100"></el-table-column>
        <!-- <el-table-column prop="starter" label="发起人" width="70"></el-table-column> -->
        <el-table-column prop="sender" label="发送人" width="70"></el-table-column>
        <el-table-column prop="sendTime" label="发送时间" width="200"></el-table-column>
        <el-table-column prop="message" label="任务描述" width=""></el-table-column>
        <el-table-column
          label="操作"
          fixed="right"
          width="70">
          <template slot-scope="scope">
            <el-button v-if="wfTaskState === '待办'" @click="handle" type="text" size="small">处置</el-button>
            <el-button v-if="wfTaskState !== '待办'" @click="openDetail" type="text" size="small">查看</el-button>
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
    </el-row>
    <el-dialog width="60%" :title="dialogTitle" :visible="dialogVisible" @close="closeDialog" @open="seachDatasources">
      <tlDetail v-model="detailForm" :operation="operation" :detailForm="detailForm" :detailVisible="saveBtnVisible" :datasources="datasources" @close="closeDialog" @refresh="refresh"></tlDetail>
    </el-dialog>
  </div>
</template>

<script>
  // import {mapGetters} from 'vuex';
  import tlDetail from './wfDetail';
  import request from '@/assets/utils/request';
  export default {
    name: 'user',
    props: ['wfTaskStateVisible', 'chooseWfTaskState'],
    data: function () {
      return {
        moduleName: '工作任务',
        undoWfTasks: [],
        wfTaskState: this.chooseWfTaskState ? this.chooseWfTaskState : '待办',
        loading1: false,
        detailForm: {
          id: '',
          valid: true,
          businessKey: '',
          instanceId: '',
          definitionKey: '',
          title: '',
          assignee: '',
          orgCode: null,
          roleCode: null,
          positionCode: null,
          message: null,
          taskKey: '',
          taskName: '',
          description: '',
          sendTime: '',
          completeTime: null,
          starter: '',
          sender: '',
          senderOrg: null
        },
        page: { // 分页的数据信息
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        page1: { // 分页的数据信息
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        page2: { // 分页的数据信息
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        page3: { // 分页的数据信息
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        // detailForm: {},
        datasources: [],
        dialogTitle: '查看' + this.moduleName,
        dialogVisible: false,
        saveBtnVisible: true,
        operation: 'add',
        startTime: '',
        endTime: ''
      };
    },
    components: {
      tlDetail: tlDetail
    },
    mounted: function () {
      this.$nextTick(function () {
        this.searchWfTasks();
      });
    },
    watch: {
      serialNumber: function (newValue, oldValue) {
        this.searchWfTasks();
      }
    },
    methods: {
      setCurrentPageSize (val) {
        let state = this.wfTaskState;
        this.page.pageSize = val;
        this.page.pageNum = 1;
        if (state === '待办') {
          this.page1 = this.page;
        } else if (state === '已办') {
          this.page2 = this.page;
        } else {
          this.page3 = this.page;
        }
      },
      setCurrentPage (val) {
        let state = this.wfTaskState;
        this.page.pageNum = val;
        if (state === '待办') {
          this.page1 = this.page;
        } else if (state === '已办') {
          this.page2 = this.page;
        } else {
          this.page3 = this.page;
        }
      },
      handleSizeChange (val) { // 设置每页总共几条
        // alert(val)
        this.setCurrentPageSize(val);
        this.searchWfTasks();
      },
      handleCurrentChange (val) { // 跳到设置的当前页
        this.setCurrentPage(val);
        this.searchWfTasks();
      },
      indexMethod (index) {
        return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);
      },
      searchWfTasks: function () {
        let index = this.wfTaskState;
        if (index === '待办') {
          this.page = this.page1;
          this.searchUndoWfTasks();
        } else if (index === '已办') {
          this.page = this.page2;
          this.searchDoneWfTasks();
        } else {
          this.page = this.page3;
          this.searchAllWfTasks();
        }
      },
      searchUndoWfTasks () {
        request({
          url: '/workflow/todoList?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
        }).then(res => {
          this.undoWfTasks = res.data.data.list;
          this.page.total = res.data.data.total;
        });
      },
      searchDoneWfTasks () {
        request({
          url: '/workflow/doneList?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
        }).then(res => {
          this.undoWfTasks = res.data.data.list;
          this.page.total = res.data.data.total;
        });
      },
      searchAllWfTasks () {
        request({
          url: '/workflow/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
        }).then(res => {
          this.undoWfTasks = res.data.data.list;
          this.page.total = res.data.data.total;
        });
      },
      handle (id) {
        this.$message('处理成功！');
      },
      setAssignee (id) {
        request({url: '/workflow/toUser?taskId=' + id + '&username='
        }).then(res => {
          this.datasources = res.data.data;
        });
      },
      seachDatasources () {
        request({url: '/workflow/list'
        }).then(res => {
          this.datasources = res.data.data;
        });
      },
      openAddDetail () {
        this.setFormProperties(this.initFormProperties());
        this.operation = 'add';
        this.saveBtnVisible = true;
        this.dialogTitle = '新增' + this.moduleName;
        this.showDialog();
      },
      openDetail (i, flag) {
        this.$message('查看成功！');
        // let detail = this.wfTasks[i];
        // this.operation = flag;
        // if (flag === 'edit') {
        //   this.dialogTitle = '编辑' + this.moduleName;
        //   this.saveBtnVisible = true;
        // } else {
        //   this.dialogTitle = '查看' + this.moduleName;
        //   this.saveBtnVisible = false;
        // }
        // this.setFormProperties(detail);
        // this.showDialog();
      },
      setFormProperties (detail) {
        this.detailForm.id = detail.id;
        this.detailForm.name = detail.name;
        this.detailForm.db = detail.db;
        this.detailForm.pjName = detail.pjName;
        this.detailForm.pkg = detail.pkg;
        this.detailForm.tprefix = detail.tprefix;
        this.detailForm.copyright = detail.copyright;
      },
      initFormProperties () {
        let detail = {};
        detail.id = '';
        detail.name = '';
        detail.db = '';
        detail.pjName = '';
        detail.pkg = '';
        detail.tprefix = '';
        detail.copyright = '';
        return detail;
      },
      showDialog () {
        this.dialogVisible = true;
      },
      closeDialog () {
        this.dialogVisible = false;
      },
      refresh () {
        this.searchUndoWfTasks();
      },
      deleteDetail (id) {
        this.$confirm('是否确定删除？', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '删除',
          cancelButtonText: '放弃'
        })
        .then(() => {
          let url = '/code/workflow/delete/' + id;
          // url += '&url=' + this.form.url;
          request({url: url
          }).then(res => {
            this.$message({
              message: '删除成功！',
              type: 'success'
            });
            this.searchUndoWfTasks();
          });
        })
        .catch(action => {
        });
      }
    }
  };
</script>

<style scoped>
  .el-row {
    margin: 15px!important;
    margin-top: 10px;
  }
  .pageDiv {
    background-color: #fff;
  }

  .db-bottom {
    margin-top: 5px;
    margin-bottom:5px;
    line-height: 12px;
    float: right;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }
</style>
