<template>
  <div>
    <el-row>
      <el-col :span="8" v-for="u in userList" :key="u.name">
        <el-card class="box-card" shadow="hover" style="margin-right:20px;background-color:#e2e2e2;">
          <div slot="header" class="clearfix">
            <span>{{u.name}}</span>
          </div>
          <div v-for="o in u.taskList" :key="o.id" class="task-item">
            <div v-if="o.done && o.max_patrol_result == 0">
              <i class="el-icon-success"></i>
              <el-button @click="showPatrolContents(o)" type="text" style="color:#000;">{{o.name}} [{{o.check_time.substring(11)}}]</el-button>
            </div>
            <div v-if="o.done && o.max_patrol_result == 1">
              <i style="color:#ffbf02;" class="el-icon-warning"></i>
              <el-button @click="showPatrolContents(o)" type="text" style="color:#000;">{{o.name}} [{{o.check_time.substring(11)}}]</el-button>
            </div>
            <div v-if="!o.done">
              <i style="color:#F60000;" class="el-icon-error"></i>
              <span style="color:#000;">{{o.name }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-dialog
      :title="detailDialogTitle"
      :visible.sync="detailDialogVisible"
      width="70%" append-to-body>
      <!-- <detail @closeDetail="closeAndRefreshDetail" :queryD="queryD" :detailForm="detailForm" :formProperties="formProperties" :operation="operation" :saveBtnVisible="saveBtnVisible"></detail> -->
      <patrolResult :patrolTask="patrolTask" :tableData="tableData" :allNormal="allNormal" :allowUpdate="allowUpdate" @closeDetail="closeDetail"></patrolResult>
    </el-dialog>
    
  </div>
</template>

<script>
  /* eslint-disable */
  import Vue from 'vue';
  import VueStomp from 'vue-stomp';
  import ElRow from 'element-ui/packages/row/src/row'
  import ElCol from 'element-ui/packages/col/src/col'
  import detail from './classTeamDetail'
  import patrolResult from './patrolResult'
  export default {
    name: 'home',
    props: ['query', 'config'],
    data: function () {
      return {
        moduleName: '检查详细内容',
        detailDialogTitle: '检查项',
        detailDialogVisible: false,
        saveBtnVisible: false,
        operation: null,
        queryD: null,// 用以刷新弹出框数据
        dept_id: this.config.user_dept_id,
        userList: [],
        patrolTask: [],
        tableData: [],
        allNormal: true,
        allowUpdate: true,
        detailForm: {
          id: '',
          name: '',
          monitor: '',
          sub_monitor: '',
          member: '',
          menberList: ''
        },
        formProperties: [
          'id', 'name', 'monitor', 'sub_monitor', 'user_ids', 'member', 'memberList', 'dept_id'
        ],
        page: { // 分页的数据信息
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        api: {
          URL_list: '/patrol/getPatroInfoList',
          URL_get: '/modbus/modbusDefinition/get/',
          URL_classList: '/userClass/getList',
          URL_del: '/team/delete/'
        }
      };
    },
    watch: {
      query: function() {
        this.refreshDataList();
      }
    },
    components: {
      ElCol, ElRow, detail, patrolResult
    },
    filters: {
    },
    computed: {
    },
    methods: {
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.page.pageNum = val;
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.page.pageSize = val;
      },
      getClassList () {
        let url = this.api.URL_classList;
        let params = {};
        if (this.config.user_dept_id) {
          params.dept_id = this.config.user_dept_id;// 收费站
          params.work_date = this.config.curDay + ' 00:00:00';
          var _this = this;
          this.$request({
            url: url,
            method: 'post',
            data: params
          }).then(res => {
            var classList = res.data.result_data;
            // if (classList && classList.length > 1) {
            //   this.operation = 'update';
            // } else {
            //   this.operation = 'add';
            // }
            for (var i in classList) {
              var d = classList[i];
              d.id = d.user_id;
              if (d.class_type === 1) {
                d.name = d.user_name + '(早班)';
              } else if (d.class_type === 2) {
                d.name = d.user_name + '(中班)';
              } else if (d.class_type === 3) {
                d.name = d.user_name + '(夜班)';
              }
              d.taskList = this.taskList;
            }
            this.userList = classList;
            // // this.check();
            // console.log(_this.tableData);
            this.getTaskList();
          });
        }
      },
      getTaskList () {
        let url = this.api.URL_list;
        let params = {};
        if (this.config.user_dept_id) {
          params.org_id = this.config.user_dept_id;// 收费站
          params.work_date = this.config.curDay + ' 00:00:00';
          var _this = this;
          this.$request({
            url: url,
            method: 'post',
            data: params
          }).then(res => {
            var taskList = res.data.result_data;
            var pMap = new Map();
            for (var i in taskList) {
              var d = taskList[i];
              d.id = d.uuid;
              d.name = d.risk_source_name;
              d.done = false;
              if(d.has_done === '1') {
                d.done = true;
              }
              var key = d.userid + '_' + d.period_type;
              if(pMap.get(key) == null) {
                pMap.set(key, []);
              }
              pMap.get(key).push(d);
            }
            for (var j in _this.userList) {
              var u =  _this.userList[j];
              var key = u.user_id + '_' + u.class_type;
              u.taskList = pMap.get(key);
            }
            console.log(_this.userList);
          });
        }
        
      },
      openAddDetail () {
        this.setFormProperties(this.initFormProperties());
        this.operation = 'add';
        this.dialogTitle = '新建' + this.moduleName;
        this.saveBtnVisible = true;
        this.showDetail();
      },
      openDetail (item, flag) {
        let detail = item;
        if (flag === 'edit') {
          this.dialogTitle = '编辑' + this.moduleName;
          this.saveBtnVisible = true;
        } else {
          this.dialogTitle = '查看' + this.moduleName;
          this.saveBtnVisible = false;
        }
        if (detail.sub_monitor===0) {
          detail.sub_monitor = "";
        }
        this.setFormProperties(detail);
        this.operation = 'edit';
        this.showDetail();
        // request({
        //   url: this.api.URL_get + detail.id
        // }).then(res => {
        //   let detail = res.data.data;
        //   this.setFormProperties(detail);
        //   this.operation = 'edit';
        //   this.showDetail();
        // });
      },
      setFormProperties (detail) {
        // this.$set(this.detailForm, 'id', detail.id);

        for (var i = 0; i < this.formProperties.length; i++) {
          var prop = this.formProperties[i];
          this.detailForm[prop] = detail[prop];
        }
        // this.copyObj(detail, this.detailForm, this.formProperties);
        this.detailForm.id = detail.id;
        // this.detailForm.ip = detail.ip;
        // this.detailForm.port = detail.port;
        // this.detailForm.interval = detail.interval;
        // this.detailForm.remark = detail.remark;
      },
      initFormProperties () {
        let detail = {};
        for (var i = 0; i < this.formProperties.length; i++) {
          var prop = this.formProperties[i];
          detail[prop] = '';
        }
        detail.id = '';
        detail.dept_id = this.config.user_dept_id;
        return detail;
      },
      showDetail () {
        this.detailDialogVisible = true;
        this.queryD = new Date();
      },
      closeDetail () {
        this.detailDialogVisible = false;
      },
      closeAndRefreshDetail () {
        this.refreshDataList();
        this.closeDetail();
      },
      refreshDataList () {
        this.getClassList();
      },
      deleteDetail (id) {
        this.$confirm('是否确定删除？', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '删除',
          cancelButtonText: '放弃'
        })
        .then(() => {
          this.$request({
            url: this.api.URL_del + id
          }).then(res => {
            this.$message({
              message: '删除成功！',
              type: 'success'
            });
            this.refreshDataList();
          });
        })
        .catch(action => {
        });
      },
      copyObj (src, to, props) {
        if(!to) to = {};
        if(props) {
          for (var i = 0; i < props.length; i++) {
            var prop = props[i];
            var obj = src[prop];
            if(obj instanceof Array || obj instanceof Object) {
              var copy = (obj instanceof Array)?[]:{};
              for (var attr in obj) {
                  if (obj.hasOwnProperty(attr))
                    copy[attr] = this.copyObj(obj[attr]);
              }
              to[prop] = copy;
            }
            to[prop] = obj;
          }
        } else {
          var obj = src;
          if(obj instanceof Array || obj instanceof Object) {
            var copy = (obj instanceof Array)?[]:{};
            for (var attr in obj) {
                if (obj.hasOwnProperty(attr))
                  copy[attr] = this.copyObj(obj[attr]);
            }
            return copy;
          }
        }
        // to.id = '';
        return to;
      },
      showPatrolContents (obj) {
        this.detailDialogVisible = true;
        this.patrolTask = obj;
        this.getPatrolResult(this.patrolTask);
        
      },
      getPatrolResult(obj){
        this.judgeAllowUpdate(obj);

				let patrolUUID = obj.uuid;
				this.$request({
					url: '/patrol/getPatrolResult/' + patrolUUID, 
					method: 'post'
				}).then(res => {
					let data = res.data;
          this.tableData = data.result_data;
          this.allNormal = this.judgeAllNormal(this.tableData);
				});
      },
      judgeAllNormal(arrayPatrolResult) {
        let r = true;
        arrayPatrolResult.forEach(element => {
          if(element.result == 1){
            r = false;
          }
        });
        return r;
      },
      judgeAllowUpdate(patrolDetail) {
        // 任务结束时间之后24小时内可以修改数据，超过24小时则不能修改数据
        let operClosedTime = Date.parse(patrolDetail.end_time);
        operClosedTime = new Date(operClosedTime);
        operClosedTime.setHours(operClosedTime.getHours() + 24);
        let now = new Date();
        if(now > operClosedTime) {
          this.allowUpdate = false;
        }
      }
    },
    mounted: function () {
      this.refreshDataList();
      this.$nextTick(function () {
        // this.searchUndoWfTasks();
        // this.searchDoneWfTasks();
      });
    }
  };
</script>

<style scoped>
  .title-btn {
    font-size: 18px !important;
    font-weight: bold !important;
  }
  .task-item {
    padding: 5px 10px;
  }
  .task-item i{
    color: #00B050;
  }
  .el-button {
    padding: 0 0;
  }
</style>
