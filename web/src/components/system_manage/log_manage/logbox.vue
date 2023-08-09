<template>
  <div id="LOG">
    <el-row>
      <el-col :span="24">
        <div style="padding: 10px 10px 5px 15px">
          <el-form :inline="true" :model="formInline" :rules="rules" class="demo-form-inline" size="mini" ref="queryForm">
            <el-form-item label="关键字:" prop="keywords" >
              <el-input v-model="formInline.keywords" placeholder="模糊查询地址/方法/参数/备注"></el-input>
            </el-form-item>
            <el-form-item label="方法:" prop="method" >
              <el-input v-model="formInline.method" placeholder="请输入完整的方法名"></el-input>
            </el-form-item>
            <el-form-item label="请求地址:" prop="url">
              <el-input  v-model="formInline.url" placeholder="请输入完整的请求地址"></el-input>
            </el-form-item>
            <el-form-item label="访问人:" prop="user">
              <el-input v-model="formInline.user" placeholder="请输入访问人"></el-input>
            </el-form-item>
            <el-form-item label="日志时间:" prop="time_start">
              <!--<el-date-picker type="date"   v-model="formInline.time_start" format="yyyy-MM-dd" value-format="yyyyMMdd" placeholder="请选择起始时间" size="small">
              </el-date-picker>-->
              <el-date-picker
                v-model="formInline.time_start"
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
                v-model="formInline.time_end"
                type="datetime"
                size="small"
                :clearable = "true"
                format="yyyy-MM-dd HH:mm:ss" value-format="yyyyMMddHHmmss"
                placeholder="请选择终止时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item  v-if="checkButtonPerm('log:list') ">
              <el-button type="primary" @click="queryLog('queryForm')"  v-if="checkButtonPerm('log:list') ">查询</el-button>
              <el-button type="info" @click="resetForm('queryForm')">重置</el-button>
             <!-- <el-button type="primary"   @click="refreshLog" size="mini" style="background-color: #74be41;border-color: #74be41;"> 重新加载</el-button>-->
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="24" style="padding: 10px 10px 0 10px;margin-top: 10px;height: 62vh;">
        <el-table
          v-loading="loading"
          :data="tableData"
          max-height="520"
          @selection-change="handleSelectionChange">
          <el-table-column
            type="index"
            :index="indexMethod"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            prop="url"
            label="请求地址"
            width="200">
          </el-table-column>
          <el-table-column
            prop="method"
            label="方法名"
            width="260">
          </el-table-column>
          <el-table-column
            prop="param"
            label="参数"
            width="200">
          </el-table-column>
          <el-table-column
            prop="result"
            label="操作结果"
            :formatter="formatResult"
            width="80">
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            width="200">
          </el-table-column>
          <el-table-column
            label="操作"
            width=""
            fixed="right">
            <template slot-scope="scope">
             <!-- <el-button @click="permAssign(scope.row)" type="text" size="small">查看详情</el-button>-->
              <el-button  type="text" size="small" @click="detailLog(scope.row)" v-if="checkButtonPerm('log:get') ">查看详情</el-button>
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
      <logDetail :detailVisible= "detailVisible" :propForm="propForm"  v-on:headCallBack="callBack">
      </logDetail>
    </el-row>
  </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  import logDetail from './logDetail';
  // import request from '@/assets/utils/request';
  export default {
    name: 'log',
    data: function () {
      return {
        loading: false, // 是否加载
        operated: false,
        title: '', // 页面标题
        rules: {
        },
        detailVisible: false,
        tableData: [],
        page: {
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        timer: null,
        flag: true,
        formInline: { // 条件查询的数据
          keywords: '',
          method: '',
          url: '',
          user: '',
          time_start: '',
          time_end: ''
        },
        propForm: {
          id: '',
          createTime: '',
          updateTime: '',
          state: '',
          createUser: '',
          updateUser: '',
          url: '',
          method: '',
          param: null,
          result: '',
          remark: null
        },
        multipleSelection: [], // 选择的数据项
        theButtons: []
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
      logDetail: logDetail
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        this.receiveButtons();
        this.timeSwitch();
        this.queryLog('queryForm');
      });
    },
    activated: function () {
    },
    watch: {
      operated: function (val) { // 此处不要使用箭头函数    监听是否增删改的操作成功，成功就执行把刷新列表 
        if (val) {
          // this.seachLog();
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
        });
      },
      timeSwitch () { // 一进去页面自动填写时间限制的输入框
        let timeStr1 = '';
        let timeStr3 = '';
        let myDate = new Date();// 获取系统当前时间
        timeStr1 = '' + myDate.getFullYear() + this.numSwitch(myDate.getMonth() + 1) + this.numSwitch(myDate.getDate()) + '000000';
        // console.log(timeStr1);
        timeStr3 = '' + myDate.getFullYear() + this.numSwitch(myDate.getMonth() + 1) + this.numSwitch(myDate.getDate()) +
          this.numSwitch(myDate.getHours()) + this.numSwitch(myDate.getMinutes()) + this.numSwitch(myDate.getSeconds());
        this.formInline.time_start = timeStr1;
        this.formInline.time_end = timeStr3;
        // this.timer = self.setInterval(this.time2, 5000);
      },
      time2 () { // 定时更新终止时间的值
        if (this.flag) {
          var Self = this;
          let timeStr2 = '';
          let myDate1 = new Date();// 获取系统当前时间
          timeStr2 = '' + myDate1.getFullYear() + Self.numSwitch(myDate1.getMonth() + 1) + Self.numSwitch(myDate1.getDate()) +
            Self.numSwitch(myDate1.getHours()) + Self.numSwitch(myDate1.getMinutes()) + Self.numSwitch(myDate1.getSeconds());
          this.formInline.time_end = timeStr2;
        } else {
          // clearInterval(this.timer);
          this.timer = null;
        }
      },
      numSwitch (num) { // 对时间的数字的判断。小于10时，加0显示
        if (num < 10) {
          num = '0' + num;
        }
        return num;
      },
      indexMethod (index) {
        return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);
      },
      seachLog () { // 查询日志列表
        this.loading = true;
        this.$request({
          url: '/ctlLog/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
        }).then(res => {
          this.operated = false; // 每次查询结束后把操作成功项归原
          this.loading = false; // 查询结束后，停止加载
          this.page.total = res.data.data.total;
          this.tableData = res.data.data.list;
        });
      },
      detailLog (row) {
        let theid = row.id; // 岗位id
        this.$request({
          url: '/ctlLog/get/' + theid
        }).then(res => {
          this.propForm.id = res.data.data.id;
          this.propForm.createTime = res.data.data.createTime;
          this.propForm.updateTime = res.data.data.updateTime;
          // this.propForm.state = res.body.data.state;
          if (res.data.data.state === '0') {
            this.propForm.state = '无效';
          } else if (res.data.data.state === '1') {
            this.propForm.state = '有效';
          } else {
            this.propForm.state = '过期';
          }
          this.propForm.createUser = res.data.data.createUser;
          this.propForm.updateUser = res.data.data.updateUser;
          this.propForm.url = res.data.data.url;
          this.propForm.method = res.data.data.method;
          this.propForm.param = res.data.data.param;
          if (res.data.data.result === '1') {
            this.propForm.result = '成功';
          } else {
            this.propForm.result = '失败';
          }

          this.propForm.remark = res.data.data.remark;
          this.detailVisible = true;
        });
      },
      formatResult: function (row, column, cellValue) {
        if (cellValue === '1') {
          return '成功';
        } else if (cellValue === '0') {
          return '失败';
        }
      },
      callBack () { // 回调函数
        this.detailVisible = false;
      },
      resetForm (formName) {
        if (this.formInline.keywords !== '' || this.formInline.user !== '' || this.formInline.method !== '' || this.formInline.url !== '' || this.formInline.time_start !== '' || this.formInline.time_end !== '') {
          this.$refs[formName].resetFields();
          this.queryLog(formName);
        }
        this.$refs[formName].resetFields();
        this.flag = false;
        this.timer = null;
        this.timeSwitch();
      },
      queryLog (formName) { // 按条件查询用户列表
        this.loading = true;
        let options = {
          headers: {
            Authorization: this.$store.getters.getToken
          },
          params: {}
        };
        var Self = this;
        let apiPrefix = this.$api_prefix;
        this.$refs[formName].validate(function (valid) {
          if (valid) {
            if (Self.formInline.keywords !== '') {
              options.params.keywords = Self.formInline.keywords;
            }
            if (Self.formInline.user !== '') {
              options.params.user = Self.formInline.user;
            }
            if (Self.formInline.url !== '') {
              options.params.url = encodeURI(Self.formInline.url);
            }
            if (Self.formInline.method !== '') {
              options.params.method = encodeURI(Self.formInline.method);
            }
            if (Self.formInline.time_start !== '' && Self.formInline.time_start !== null) {
              if (Self.formInline.time_end !== '' && Self.formInline.time_end !== null) {
                if (Self.formInline.time_end < Self.formInline.time_start) {
                  Self.$message.error('终止时间不能小于起始时间');
                  return;
                }
              }
              let timeStart = Self.formInline.time_start;
              options.params.time_start = timeStart;
            }
            if (Self.formInline.time_end !== '' && Self.formInline.time_end !== null) {
              if (Self.formInline.time_start !== '' && Self.formInline.time_start !== null) {
                if (Self.formInline.time_end < Self.formInline.time_start) {
                  Self.$message.error('终止时间不能小于起始时间');
                  return;
                }
              }
              let timeEnd = Self.formInline.time_end;
              options.params.time_end = timeEnd;
            }
            Self.loading = true; // 开始加载
            let url = apiPrefix + '/ctlLog/list?pageSize=' + Self.page.pageSize + '&pageNum=' + Self.page.pageNum;
            Self.$http.get(url, options).then((res) => {
              if (res.body.code === 401) {
                Self.$router.push('/login');
                return false;
              }
              let success = res.body.success;
              if (success === true) {
                Self.page.total = res.body.data.total;
                Self.tableData = res.body.data.list;
                Self.loading = false; // 查询结束后，停止加载
                // console.log(res.body);
                // Self.$message({
                //   type: 'success',
                //   message: res.body.message
                // });
              } else {
                Self.$message.error(res.body.message);
                Self.loading = false; // 查询结束后，停止加载
              }
            }, (res) => {
              Self.$message.error('服务器暂时无法连接，请稍后再试！');
              Self.loading = false; // 查询结束后，停止加载
            });
          } else {
            return false;
          }
        });
      },
      handleSelectionChange (val) { // 储存被选中的数据项
        this.multipleSelection = val;
        console.log(this.multipleSelection);
      },
      handleSizeChange (val) { // 设置每页总共几条
        console.log(`每页 ${val} 条`);
        this.page.pageNum = 1;
        this.page.pageSize = val;
        this.queryLog('queryForm');
      },
      handleCurrentChange (val) { // 跳到设置的当前页
        console.log(`当前页: ${val}`);
        this.page.pageNum = val;
        this.queryLog('queryForm');
      }
    }
  };
</script>

<style>
  #LOG th{
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #LOG .el-form--inline .el-form-item {
    margin-right: 1%;
  }
  #LOG .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 10px;
  }
  #LOG .el-button {
    padding: 9px 29px;
  }
  #LOG .el-table td{
    padding: 7px 0;
    color: #666666;
  }
  #LOG .el-table th,td {
    text-align: center;
    font-size: 12px;
  }
  #LOG .el-row {
    margin: 15px;
    margin-top: 10px;
  }
  #LOG  .el-col{
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 2px 2px 5px #999; */
  }
  #LOG .el-input--mini input {
    width: 190px;
  }
  #LOG input {
    height: 30px;
  }
  #LOG .el-button--text{
    width: auto;
    height: auto;
    padding: 9px 0px;
  }
  /* #LOG .el-button--primary {
    background-color: #4baff2;
    border-color: #4baff2;
  }
  #LOG .el-button--danger {
    background-color: #f36f6f;
    border-color: #f36f6f;
  }
  #LOG .el-button--info {
    background-color: #b1bac0;
    border-color: #b1bac0;
  } */
  /*分页样式*/
  #LOG .pageDiv {
    padding: 10px;
    height: 35px;
    background-color: rgb(255, 255, 255);
  }

  #LOG .pageDiv .page {
    float: right;
  }
  #LOG .el-pager {
    margin-left: 12px;
  }
  #LOG .el-pager li.active {
    color: rgb(255, 255, 255);
    background-color: #a7bbf5;
    border: 1px solid;
    border-radius: 6px;
  }

  #LOG .el-pager li.number {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
  }

  #LOG .el-pager li {
    padding: 0 10px;
    min-width: 0px;
    font-family: 宋体;
    margin: 0 6px;
  }

  #LOG .el-pagination button {
    border: 1px solid #d4d4d4;
    border-radius: 6px;
    width: 27px;
    min-width: 0px;
  }

  #LOG .el-pagination .btn-next {
    margin-left: 10px;
    padding-left: 6px;
  }

  /**************/
  #LOG .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
</style>
