<template>
  <div id="historytask">
    <el-row>
      <el-col :span="24">
        <div style="padding: 15px">
          <el-form :inline="true" :model="historyForm" class="demo-form-inline" size="mini" ref="queryHistoryForm">
            <el-form-item label="关键字:" prop="keywords" >
              <el-input v-model="historyForm.keywords" placeholder="请输入关键字"></el-input>
            </el-form-item>
            <el-form-item label="开始时间:" prop="time_start">
              <!--<el-date-picker type="date"   v-model="formInline.time_start" format="yyyy-MM-dd" value-format="yyyyMMdd" placeholder="请选择起始时间" size="small">
              </el-date-picker>-->
              <el-date-picker
                v-model="historyForm.startTime_start"
                type="datetime"
                size="small"
                :clearable = "true"
                format="yyyy-MM-dd HH:mm:ss" value-format="yyyyMMddHHmmss"
                placeholder="请选择起始时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="~" prop="time_end">
              <!--<el-date-picker type="date"  v-model="formInline.time_end" format="yyyy-MM-dd HH:mm:ss" value-format="yyyyMMdd" placeholder="请选择终止时间" size="small">
              </el-date-picker>-->
              <el-date-picker
                v-model="historyForm.startTime_end"
                type="datetime"
                size="small"
                :clearable = "true"
                format="yyyy-MM-dd HH:mm:ss" value-format="yyyyMMddHHmmss"
                placeholder="请选择终止时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="类型:" prop="type">
              <el-select v-model="historyForm.type" clearable filterable placeholder="请选择任务类型">
                <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item >
              <el-button type="primary" @click="queryHistoryTask(true)">查询</el-button>
              <el-button type="info" @click="resetForm('queryHistoryForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="24"  style="padding: 10px 10px 0 10px;margin-top: 10px;height: 66vh">
        <el-table
          v-loading="loading"
          :data="tableData"
          max-height="320">
          <el-table-column
            type="index"
            :index="indexMethod"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            prop="address"
            label="执行地址"
            width="">
            <template slot-scope="scope">
              <el-button  type="text" class="clickName" @click="detailTask(scope.row)">{{scope.row.address}}</el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="params"
            label="参数"
            width="">
          </el-table-column>
          <el-table-column
            prop="msg"
            label="信息"
            width="">
          </el-table-column>
          <el-table-column
            prop="resultName"
            label="结果"
            width="60">
          </el-table-column>
          <el-table-column
            prop="typeName"
            label="任务类型"
            width="">
          </el-table-column>
          <el-table-column
            prop="startTime"
            label="开始时间"
            width="">
          </el-table-column>
          <el-table-column
            prop="endTime"
            label="结束时间"
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
    </el-row>
    <el-dialog  width="62%" title="任务详情" :visible="detailTaskVisible" @close="close('')">
      <taskDetail  :detailForm="detailForm"></taskDetail>
    </el-dialog>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';
  import taskDetail from './taskDetail';
  export default {
    name: 'historyTask',
    props: ['typeOptions', 'historyRefresh'],
    data: function () {
      return {
        loading: false, // 是否加载
        operated: false,
        historyForm: { // 历史任务的条件查询的数据
          keywords: '',
          type: '',
          startTime_start: '',
          startTime_end: ''
        },
        detailForm: { // 详情框
          id: '',
          address: '',
          params: '',
          cron: '',
          msg: '',
          resultName: '',
          typeName: '',
          startTime: '',
          endTime: '',
          remark: ''
        },
        tableData: [],
        detailTaskVisible: false,
        multipleSelection: [], // 选择的数据项
        page: {
          pageNum: 1,
          pageSize: 5,
          total: 0
        }
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      taskDetail: taskDetail
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        this.timeSwitch();
        this.serchHistoryTask();
      });
    },
    watch: {
      historyRefresh: function (val) { // 此处不要使用箭头函数    监听是否增删改的操作成功，成功就执行把刷新列表
        this.tableData = [];
        if (val) {
          this.serchHistoryTask();
        }
      }
    },
    methods: {
      timeSwitch () { // 一进去页面自动填写时间限制的输入框
        let timeStr1 = '';
        let timeStr3 = '';
        let myDate = new Date();// 获取系统当前时间
        timeStr1 = '' + myDate.getFullYear() + this.numSwitch(myDate.getMonth() + 1) + this.numSwitch(myDate.getDate()) + '000000';
        // console.log(timeStr1);
        timeStr3 = '' + myDate.getFullYear() + this.numSwitch(myDate.getMonth() + 1) + this.numSwitch(myDate.getDate()) +
          this.numSwitch(myDate.getHours()) + this.numSwitch(myDate.getMinutes()) + this.numSwitch(myDate.getSeconds());
        this.historyForm.startTime_start = timeStr1;
        this.historyForm.startTime_end = timeStr3;
        // this.timer = self.setInterval(this.time2, 5000);
      },
      numSwitch (num) { // 对时间的数字的判断。小于10时，加0显示
        if (num < 10) {
          num = '0' + num;
        }
        return num;
      },
      close () { // 关闭历史任务
        this.detailTaskVisible = false;
      },
      indexMethod (index) {
        return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);
      },
      formatState: function (row, column, cellValue) {
        if (cellValue === '1') {
          return '成功';
        } else if (cellValue === '0') {
          return '失败';
        } else {
          return '**';
        }
      },
      detailTask (row) { // 详情框
        this.detailForm.id = row.id;
        this.detailForm.address = row.address;
        this.detailForm.params = row.params;
        // if (row.state === '0') {
        //   this.detailForm.state = '无效';
        // } else if (row.state === '1') {
        //   this.detailForm.state = '有效';
        // } else {
        //   this.detailForm.state = '过期';
        // }
        this.detailForm.cron = row.cron;
        this.detailForm.msg = row.msg;
        this.detailForm.resultName = row.resultName;
        this.detailForm.typeName = row.typeName;
        this.detailForm.startTime = row.startTime;
        this.detailForm.endTime = row.endTime;
        this.detailForm.remark = row.remark;
        this.detailTaskVisible = true;
      },
      serchHistoryTask () {
        this.loading = true;
        this.$request({
          url: '/jobLog/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.body.data.total;
          this.tableData = res.body.data.list;
        });
      },
      queryHistoryTask (flag) { // 按条件查询菜单列表
        if (flag) {
          this.page.pageNum = 1;
        }
        let options = {
          params: {}
        };
        if (this.historyForm.startTime_start !== '' && this.historyForm.startTime_start !== null) {
          if (this.historyForm.startTime_end !== '' && this.historyForm.startTime_end !== null) {
            if (this.historyForm.startTime_end < this.historyForm.startTime_start) {
              this.$message.error('开始时间止不能小于开始时间起');
              return;
            }
          }
        }
        if (this.historyForm.startTime_end !== '' && this.historyForm.startTime_end !== null) {
          if (this.historyForm.startTime_start !== '' && this.historyForm.startTime_start !== null) {
            if (this.historyForm.startTime_end < this.historyForm.startTime_start) {
              this.$message.error('开始时间止不能小于开始时间起');
              return;
            }
          }
        }
        options.params = this.historyForm;
        this.loading = true; // 开始加载
        this.$request({
          url: '/jobLog/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum,
          params: options.params
        }).then(res => {
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.body.data.total;
          this.tableData = res.body.data.list;
        });
      },
      resetForm (formName) {
        if (this.historyForm.keywords !== '' || this.historyForm.type !== '' || this.historyForm.startTime_start !== '' || this.historyForm.startTime_end !== '') {
          this.$refs[formName].resetFields();
          this.queryHistoryTask(true);
        }
        this.$refs[formName].resetFields();
        this.timeSwitch();
      },
      handleSizeChange (val) { // 设置每页总共几条
        this.page.pageSize = val;
        this.queryHistoryTask(false);
      },
      handleCurrentChange (val) { // 跳到设置的当前页
        this.page.pageNum = val;
        this.queryHistoryTask(false);
      }
    }
  };
</script>

<style>
  #historytask th{
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #historytask .el-table td{
    padding: 7px 0;
    color: #666666;
  }
  #historytask .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #historytask .el-button {
    padding: 9px 29px;
  }
  #historytask  .el-col{
    background-color: #fff;
    border-radius: 5px;
    box-shadow: none;
  }
  #historytask input {
    height: 30px;
  }
  #historytask .el-form .el-input {
    width: 183px;
  }
  #historytask .el-form--inline .el-form-item {
    margin-right: 15px;
  }
  #historytask .el-button--text{
    width: auto;
    height: auto;
    padding: 9px 0px;
  }
  #historytask .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #historytask .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #historytask .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  }
  #historytask .el-row {
    margin: 15px;
    margin-top: 10px;
  }
  #historytask .clickName{
    color: #606266;
    font-size: 12px;
  }
  #historytask .clickName:hover{
    color: #409EFF;
    text-decoration: underline;
  }
  /*分页样式*/
  #historytask .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #historytask .pageDiv .page {
    float: right;
  }
  #historytask .el-pager {
    margin-left: 12px;
  }
  #historytask .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #historytask .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #historytask .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #historytask .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #historytask .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }


</style>
