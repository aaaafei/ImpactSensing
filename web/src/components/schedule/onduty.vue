<template>
  <div id="home">
    <el-row type="flex" style="padding-left:15px;dispaly:flex;">
      <div>
        <el-form  label-width="70px">
          <el-form-item label="巡查部门">
            <el-select v-model="config.user_dept_id" @change="changeFeeSelect" style="width:200px;" placeholder="请选择" :disabled="!config.isAdmin">
              <el-option
                v-for="item in feeList"
                :key="item.deptid"
                :label="item.name"
                :value="item.deptid">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-left:20px;">
        <el-button type="primary" @click="queryData">查询</el-button>
      </div>
      <div style="margin-left:30px;">
        <el-button @click="showTeam">班组管理</el-button>
      </div>
    </el-row>
    <el-row style="padding-left:15px;padding-top:15px;">
      <el-col :span="24" >
        <div id="calendar" class="calendar"></div>
      </el-col>
    </el-row>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <el-table :data="gridData">
        <el-table-column property="date" label="日期" width="150"></el-table-column>
        <el-table-column property="name" label="姓名" width="200"></el-table-column>
        <el-table-column property="address" label="地址"></el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog
      :title="teamDialogTitle"
      :visible.sync="teamDialogVisible"
      width="50%" opened="showData">
      <classTeam :query="query" :config="config"></classTeam>
    </el-dialog>
    <el-dialog
      :title="dutyTeamDialogTitle"
      :visible.sync="dutyTeamDialogVisible"
      width="40%"
      center>
      <dutyTeamDetail @closeAndRefreshCal="closeAndrefreshCal" :operation="operation" :config="config" :query="query" :saveBtnVisible="teamSaveBtnVisible" :teamData="teamData" :classCode="classCodeMap"></dutyTeamDetail>
    </el-dialog>
    <el-dialog
      :title="dutyClassDialogTitle"
      :visible.sync="dutyClassDialogVisible"
      width="40%"
      center>
      <dutyClassDetail @closeDutyClassDialog="closeDutyClassDialog" :config="config" :query="queryClassFlag" :saveBtnVisible="teamSaveBtnVisible" :teamData="teamData" :classCode="classCodeMap"></dutyClassDetail>
    </el-dialog>
    <el-dialog
      :title="taskDialogTitle"
      :visible.sync="taskDialogVisible"
      width="70%"
      center>
      <tasks @closeDutyClassDialog="closeTaskDialog" :config="config" :query="queryTaskFlag" :saveBtnVisible="teamSaveBtnVisible" :teamData="teamData" :classCode="classCodeMap"></tasks>
    </el-dialog>
  </div>
</template>

<script>
  /* eslint-disable */
  import Vue from 'vue';
  import VueStomp from 'vue-stomp';
  import ElRow from 'element-ui/packages/row/src/row';
  import ElCol from 'element-ui/packages/col/src/col';
  import classTeam from './classTeam';
  import dutyTeamDetail from './dutyTeamDetail';
  import dutyClassDetail from './dutyClassDetail';
  import tasks from './tasks';
  // import { Calendar } from 'dayspan';
  import '../../../node_modules/zui/dist/css/zui.min.css';
  // import $ from '../../../node_modules/zui/dist/lib/jquery/jquery.js';
  // import jQuery from '../../../node_modules/zui/dist/lib/jquery/jquery.js';
  import '../../../node_modules/zui/dist/js/zui.min.js';
  import '../../../node_modules/zui/dist/lib/calendar/zui.calendar.css';
  import '../../../node_modules/zui/dist/lib/calendar/zui.calendar.min.js';

  export default {
    name: 'home',
    data: function () {
      return {
        calendar: null,
        dialogVisible: false,
        teamDialogVisible: false,
        dutyTeamDialogVisible: false,
        dutyClassDialogVisible: false,
        taskDialogVisible: false,
        teamDialogTitle: '班组管理',
        dutyTeamDialogTitle: '班组排班',
        dutyClassDialogTitle: '人员排班',
        taskDialogTitle: '任务情况',
        teamSaveBtnVisible: false,
        operation: '',
        gridData: null,
        teamData: null,
        query: null,
        queryClassFlag: null,
        queryTaskFlag: null,
        dialogTitle: '添加班次',
        feeList: [],
        selectFee: null,
        // query: null,
        config: {
          isAdmin: false,
          curDay: '',
          user_dept_id: '',
          date_cursor: 0,
          team_type: 1,
          user_type: 1,
          deptInfo: null
        },
        classCodeMap: null,
        page: { // 分页的数据信息
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        api: {
          URL_feeList: '/dept/getAllFs',
          URL_getTime: '/comm/getTime'
        }
      };
    },
    components: {
      ElCol, ElRow, classTeam, dutyTeamDetail, dutyClassDetail, tasks
    },
    watch: {
      'config.user_dept_id' (v) {
        for (var i in this.feeList) {
          if (this.feeList[i].deptid == v) {
            this.config.deptInfo = this.feeList[i];
            break;
          }
        }
      }
    },
    filters: {
    },
    computed: {
    },
    methods: {
      addEvent: function () {
        var calendar = $('#calendar').data('zui.calendar');
        var newEvent = [];
        calendar.addEvents(newEvent);
      },
      getClassCode () {
        this.$request({
          url: 'teamClass/getClassCode',
          method: 'post',
          data: {},
        }).then(res => {
          let data = res.data.result_data;
          var map = new Map();
          for (var i in data) {
            var c = data[i];
            map.set(c.code, c);
          }
          this.classCodeMap = map;
        });
      },
      getFeeStationList () {
        var _this = this;
        this.$request({
          url: this.api.URL_feeList 
        }).then(res => {
          _this.feeList = res.data.result_data;
          if (_this.isAdmin()) { //_this.config.user_dept_id
            _this.config.isAdmin = true;
          } else {
            var user = _this.$store.getters.getUserInfo;
            _this.config.user_dept_id = user.organid + "";
          }
          this.calendarInit();
        });
      },
      isAdmin () {
        var user = this.$store.getters.getUserInfo;
        var roles = user.roles;
        var isAdmin = false;
        for (var i in roles) {
          if (roles[i].name == "ROLE_ADMIN" || roles[i].name == "ROLE_COMPANY_ADMIN") {
            isAdmin = true;
            break;
          }
        }
        return isAdmin;
      },
      changeFeeSelect (data) {
        this.config.user_dept_id = data;
      },
      getTeamClass: function (params, callback) {
        var startTime = this.getMonthByCondition(this.config.date_cursor, 'first').Format("yyyy-MM-dd HH:mm:ss");
        var endTime = this.getMonthByCondition(this.config.date_cursor, 'last').Format("yyyy-MM-dd HH:mm:ss");
        var param = {
          dept_id: this.config.user_dept_id,
          start_time: startTime,
          end_time: endTime,
          type: this.config.team_type
        }
        if (params) {
          param = params;
        }
        this.$request({
          url: 'teamClass/getClassAndUserList',
          method: 'post',
          data: param,
        }).then(res => {
          // this.undoWfTasks = res.data.data.list;
          // this.undoTaskCount = res.data.data.total;
          let data = res.data.result_data;
          // console.log(data);
          // this.addClass2Calendar(data);
          callback(data);
        });
      },
      addClass2Calendar (data, data_param) {
        this.addInitEvent(data_param);
        var newEvents = [];
        if (data && data.length > 0) {
          for (var i = 0; i < data.length; i++) {
            if (i < 20) {
              data[i].isDone = true;
            }
            var obj = '';
            var users = '';
            if (data[i].users && data[i].users.length > 0) {
              users = data[i].users[0].truename;
            }
            var userSpan = "-<span style='background-color:none;'>" + users + "</span>-";
            if (data[i].isDone) {
              userSpan = "-<span style='background-color:none;'>" + users + "</span>-";
            }
            var title = this.getTeamName(data[i].class_type) + userSpan + data[i].team_name;
            if (data[i].class_type == 1) {
                obj = { id: data[i].id, desc: data[i].team_name, title: title, start: data[i].start_time + ":03", end: data[i].start_time + ":04" };
            } else if (data[i].class_type == 2) {
                obj = { id: data[i].id, desc: data[i].team_name, title: title, start: data[i].start_time + ":02", end: data[i].start_time + ":03" };
            } else {
                obj = { id: data[i].id, desc: data[i].team_name, title: title, start: data[i].start_time + ":01", end: data[i].start_time + ":02" };
            }
            newEvents.push(obj);
          }
        }
        this.calendar.addEvents(newEvents);
      },
      addInitEvent (data_param) {
        var newEvents = [];
         // 这里轮训给每天都增加一个事件
        var date = this.getMonthByCondition(this.config.date_cursor, 'first'); //new Date();
        var end_date = this.getMonthByCondition(this.config.date_cursor, 'last'); //new Date(getCurrentMonthLast2()) ;
        if (data_param != undefined) {
            date = new Date(data_param.start_time);
            end_date = new Date(data_param.end_time);
        }

        for (var i = 0; i < 31; i++) {
            if (date.setDate(i + 1) < end_date) {
                // var obj = { id: -1, desc: '详细排班', title: '+详细排班', start: date.Format('yyyy-MM-dd 00:00:05'), end: date.Format('yyyy-MM-dd 00:00:06'), color: '#bd7b46' };
                // newEvents.push(obj);
                // 任务信息
                var obj = { id: 0, desc: '任务', title: '任务', start: date.Format('yyyy-MM-dd 00:00:05'), end: date.Format('yyyy-MM-dd 00:00:06'), color: '#bd7b46' };
                newEvents.push(obj);
            }
        }
        this.calendar.addEvents(newEvents);
      },
      // 上一个月或者下个月的第一天或者最后一天
      getMonthByCondition(pre_or_after, first_or_last) {
          if (first_or_last == 'first') {
              var date = new Date();
              date.setDate(1);
              date.setHours(0);
              date.setMinutes(0);
              date.setSeconds(0);
              date.setMonth(date.getMonth() + pre_or_after);
              return date;
          } else if (first_or_last == 'last') {
              var date = new Date();
              date.setMonth(date.getMonth() + pre_or_after); //此处月份加减，不能放到最后加减，会引起月份天数不对
              var currentMonth = date.getMonth();
              var nextMonth = ++currentMonth;
              var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);

              var date1 = nextMonthFirstDay;
              date1.setDate(date1.getDate() - 1);
              date1.setHours(23);
              date1.setMinutes(59);
              date1.setSeconds(59);
              return date1;
          }
      },
      getTeamName(class_type) {
          if (class_type == 1) {
              return "早班";
          } else if (class_type == 2) {
              return "中班";
          } else if (class_type == 3) {
              return "夜班";
          } else if (class_type == 4) {
              return "全班";
          }
      },
      closeAndrefreshCal () {
        this.dutyTeamDialogVisible = false;
        this.updateCalendar();
      },
      closeDutyClassDialog() {
        this.dutyClassDialogVisible = false;
        this.updateCalendar();
      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      showTeam() {
        if (!this.config.user_dept_id) {
          this.$message.error('请选择一个部门再进行班组查询！');
          return;
        }
        this.teamDialogVisible = true;
        this.query = new Date();
      },
      closeUndoDialog: function () {
        this.undoDialogVisible = false;
      },
      closeTaskDialog: function () {
        this.taskDialogVisible = false;
      },
      cellBtnClick (event) {
          // console.log(event。event.id);
          this.config.curDay = event.event.start.Format("yyyy-MM-dd");
          if (event.event.id == -1) {
            this.queryClassFlag = new Date();
            this.dutyClassDialogVisible = true;
          } else if (event.event.id == 0) {
            this.queryTaskFlag = new Date();
            this.taskDialogVisible = true;
          } else {
            this.eventClick(event.event.start);
          }
      },
      /**
       * 点击日历触发函数
       */
      eventClick: function (date) {
        var _this = this;
        // var date = obj.date;
        this.$request({
          url: this.api.URL_getTime
        }).then(res => {
          // this.close();
          var nowStr = res.data.result_data;
          var now = new Date(nowStr);
          var dayStr = now.Format("yyyy-MM-dd");
          var now = new Date(dayStr + ' 00:00:00');
          if (date < now) {// 不能生成过期排班
            _this.teamSaveBtnVisible = false;
          } else if (date.getDate() == now.getDate()){
            // 今天的排班也不能修改
            _this.teamSaveBtnVisible = false;
          }else {
            _this.teamSaveBtnVisible = true;
          }
          _this.config.curDay = date.Format("yyyy-MM-dd");
          _this.dialogTitle = '添加班次：' + date.format("yyyy-MM-dd");
          // 保存当前日期
          // $("#save_term_class").data("date", date.format("yyyy-MM-dd"));
          // $("#team_detail").val("");
          // var date = obj.start;
          var param = {
            "dept_id": _this.config.user_dept_id,
            "start_time": date.format("yyyy-MM-dd 00:00:00"),
            "end_time": date.format("yyyy-MM-dd 00:00:00"),
            'type': _this.config.team_type
          }
          _this.getTeamClass(param, function(data) {
            var teamDutyForm = {};
            if(!data || data.length == 0) {
              _this.operation = 'add';
            }else {
              _this.operation = 'update';
              for (var i in data) {
                if (data[i].class_type == 1) {
                  teamDutyForm.morningClass = data[i].team_id;
                } else if (data[i].class_type == 2) {
                  teamDutyForm.noonClass = data[i].team_id;
                } else {
                  teamDutyForm.nightClass = data[i].team_id;
                }
              }
            }
            _this.teamData = teamDutyForm;
            // _this.teamSaveBtnVisible = true;
            _this.dutyTeamDialogVisible = true;
            // _this.teamDetailForm = 
            _this.query = new Date();
          })
        });

      },
      queryData () {
        if(!this.config.user_dept_id) {
          this.$message.error('请选择一个部门再进行班组查询！');
        }
        this.calendar.events = [];
        this.calendar.display();
        this.dataInit();
      },
      dataInit () {
        if(this.config.user_dept_id) {
          this.getTeamClass(null, this.addClass2Calendar);
        }else{
          // this.$message({
          //   message: '请选择一个收费站再进行班次查询！',
          //   type: 'warning'
          // });
        }
      },
      calendarInit () {
        const _this = this;
        this.config.curDay = new Date().Format('yyyy-MM-dd');
        $('#calendar').calendar({
          clickEvent: function(event) {
            // console.log(event);
            // console.log("你点击了一个事件");
            // 处理 clickEvent 事件
            _this.cellBtnClick(event);
          }
        });
        this.calendar = $('#calendar').data('zui.calendar');
        this.dataInit();
        var date_cursor = this.config.date_cursor;
        $('#calendar').calendar().on("clickCell.zui.calendar", function(event) {
            // console.log(event);
            _this.eventClick(event.date);
        });
        // 下一个日期
        $('#calendar').calendar().on("clickNextBtn.zui.calendar", function(event) {
            // console.log(event);
            _this.config.date_cursor++;
            var data = {
                "dept_id": _this.config.user_dept_id,
                "start_time": _this.getMonthByCondition(date_cursor, 'first').Format("yyyy-MM-dd HH:mm:ss"),
                "end_time": _this.getMonthByCondition(date_cursor, 'last').Format("yyyy-MM-dd HH:mm:ss"),
                'type': _this.config.team_type
            }
            _this.updateCalendar(data);
        });

        // 上一个日期
        $('#calendar').calendar().on("clickPrevBtn.zui.calendar", function(event) {
            _this.config.date_cursor--;
            var data = {
                "dept_id": _this.config.user_dept_id,
                "start_time": _this.getMonthByCondition(date_cursor, 'first').Format("yyyy-MM-dd HH:mm:ss"),
                "end_time": _this.getMonthByCondition(date_cursor, 'last').Format("yyyy-MM-dd HH:mm:ss"),
                'type': _this.config.team_type
            }
            _this.updateCalendar(data);
        });

        // 今天
        $('#calendar').calendar().on("clickTodayBtn.zui.calendar", function(event) {
            // console.log(event);
            _this.config.date_cursor = 0;
            _this.dataInit();
        });
      },
      // 更新日历数据
      updateCalendar(data) {
        this.calendar.events = [];
        this.calendar.display();
        this.dataInit(data);
      },
      refresh: function () {

      },
      showDoneTask: function () {
        this.wfTaskState = '已办';
        this.undoDialogTitle = this.wfTaskState + '任务';
        this.serialNum = new Date().toString();
        this.undoDialogVisible = true;
      },
      closeDoneDialog: function () {
        this.undoDialogVisible = false;
      }
    },
    mounted: function () {
      Date.prototype.Format = function(fmt) {
        var o = {
          "M+": this.getMonth() + 1,
          "d+": this.getDate(),
          "H+": this.getHours(),
          "m+": this.getMinutes(),
          "s+": this.getSeconds(),
          "q+": Math.floor((this.getMonth() + 3) / 3),
          "S": this.getMilliseconds()
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
      };
      this.$nextTick(function () {
        // if(!this.config.user_dept_id) {
        //     this.$message({
        //       message: '请选择一个收费站再进行班次查询！',
        //       type: 'warning'
        //     });
        //   }
        this.getClassCode();
        this.getFeeStationList();
        
      });
    }
  };
</script>

<style scope>
  #home {
    padding-top:10px;
    height: 90vh;
  }
  #home .el-card {
    border: 0px;
  }
  .title-btn {
    font-size: 18px !important;
    font-weight: bold !important;
  }
  .btn-toolbar  .btn {
    color: #353535;
    background-color: #fff !important;
    border:none;
    /* border-color: #bfbfbf; */
  }
  .content .events {
    text-align: center;
  }
  .content .events div:first-child {
    background-color: #B84A5B;
  }

  /* .content .events div:nth-child(2) {
    background-color: orange;
  } */
  .content .events .event .time {
    display:none!important;
  }

  /* .content .events .event:not(:first-child){
    text-align: left;
  } */
  
  .content .events .event:nth-child(n+2){
    text-align: left;
  }
</style>
