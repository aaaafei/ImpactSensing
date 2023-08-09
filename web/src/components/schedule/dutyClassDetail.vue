<template>
  <div style="display:flex; flex-direction:column;justify-content: center;align-items: center;">
    <el-table
      :data="tableData"
      row-class-name="tb_row"
      style="">
      <el-table-column
        label="班次"
        width="120" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.class_name}}</span><br/>
          <span style="margin-left: 10px">{{ scope.row.class_start_time +'-'+ scope.row.class_end_time}}</span>
        </template>
      </el-table-column>
      <el-table-column v-for="item in lanesArr" :key="item.id" :label="item.lane" align="center">
        <!-- <template slot="header" slot-scope="scope">
          <span>{{title}}</span>
        </template> -->
        <template slot-scope="scope">
          <el-row>
            <el-col :span="20">
              <el-select style="" v-model="scope.row.data[item.id].user_id" @change="changeOndutyUserValue(item.id)" placeholder="请选择">
                <el-option
                  v-for="item1 in scope.row.users"
                  :key="item1.id"
                  :label="item1.truename"
                  :value="item1.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button v-if="item.id!='lane_0'" type="text" icon="el-icon-news" @click="showUnRegular(item.id, scope.row.class_type)"></el-button>
            </el-col>
          </el-row>
          <el-row v-for="item2 in scope.row.data[item.id].list" :key="item2.id">
            <span style="font-size:12px;">{{item2.start_time.substr(11,5) + '-' + item2.end_time.substr(11,5) + item2.user_name}}</span>
          </el-row>
          <!-- <span>{{scope.row.data[item.id]}}</span>
          <span>{{item.id}}</span> -->
        </template>
      </el-table-column>
      <!-- <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    <el-button type="primary" style="width:100px;margin-top:20px;" @click="onSubmit">确定修改</el-button>

    <el-dialog
      title="顶班管理"
      :visible.sync="unRegularDialogVisible"
      width="50%" append-to-body>
      <el-table
        :data="unRegularClassTableData"
        max-height="250">
        <el-table-column
          fixed
          prop="num"
          label="序号"
          width="50">
        </el-table-column>
        <el-table-column
          prop="start_time"
          label="开始时间"
          width="160">
        </el-table-column>
        <el-table-column
          prop="end_time"
          label="结束时间"
          width="160">
        </el-table-column>
        <el-table-column
          prop="user_name"
          label="人员"
          width="100">
        </el-table-column>
        <el-table-column
          label="操作"
          width="60">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="deleteRow(scope.$index, unRegularClassTableData)"
              type="text"
              size="small">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row style="padding-top:30px;text-align:center;">
        <el-button type="primary"  @click="showNewUnRegularDetail">新增</el-button>
        <el-button type="success"  @click="unRegularDialogVisible=false">关闭</el-button>
      </el-row>
    </el-dialog>
    <el-dialog
      title="新增顶班"
      :visible.sync="newUnRegularDialogVisible"
      width="35%" append-to-body>
      <el-form  :model="unRegularDetail"  label-width="130px">
        <el-form-item label="人员" prop="class1" >
          <el-select v-model="unRegularDetail.user_id" @change="unRegularUserChange" placeholder="请选择">
            <el-option
              v-for="item in onDutyUserList"
              :key="item.id"
              :label="item.truename"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="class2">
           <el-time-picker
            v-model="unRegularDetail.start_time"
            placeholder="任意时间点">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="class3">
          <el-time-picker
            v-model="unRegularDetail.end_time"
            placeholder="任意时间点">
          </el-time-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveUnRegular">保存</el-button>
          <!-- <el-button>取消</el-button> -->
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'dutyClassDetail',
    props: ['saveBtnVisible', 'detailForm', 'teamData', 'classCode', 'query', 'config'],
    data: function () {
      return {
        form: this.detailForm ? this.detailForm : {
          morningClass: null,
          noonClass: null,
          nightClass: null
        },
        lanePrefix: 'lane_',
        operation: 'add',
        saveVisible: this.saveBtnVisible,
        tableData: [],
        checkedIds: null,
        teamList: [],
        lanesArr: [],
        unRegularDialogVisible: false,
        unRegularClassTableData: [],
        newUnRegularDialogVisible: false,
        unRegularDetail: {},
        onDutyUserList: [],
        classList: [
          {code: '1', name: '晚班'}, {id: '2', name: '早班'}
        ],
        api: {
          URL_add: '/userClass/addList',
          URL_update: '/userClass/updateList',
          URL_teamClassList: '/teamClass/getClassListAndUsers',
          URL_classList: '/userClass/getList',
          URL_dict: '/dict/subCodes/'
        }
      };
    },
    watch: {
      query: function () {
        this.initAll();
      },
      unRegularClassTableData: function () {
        for (var i = 0; i < this.unRegularClassTableData.length; i++) {
          this.unRegularClassTableData[i].num = i + 1;
        }
      }
    },
    methods: {
      renderheader (h, { column, $index }) {
        return (
          '<div>' +
              '<span>实收总金额(元) </span>' +
          '</div>'
        );
      },
      close () {
        this.$emit('closeDetail', '');
      },
      initAll () {
        this.initLane();
        this.getUerList();
      },
      initLane () {
        var lanesArr = [];
        // var classTableData = [];
        var prefix = this.lanePrefix;
        var obj = {
          id: prefix + '0',
          lane: '值班班长'
        };
        lanesArr.push(obj);
        // 暂时只排值机班长
        // var deptinfo = this.config.deptInfo;
        // var entrance_number = deptinfo.entrance_numer;
        // var exit_number = deptinfo.exit_numer;
        // for (var i = 0; i < entrance_number; i++) {
        //     // var v = item.class_type + '_+' + i;
        //   var obj1 = {
        //     id: prefix + (i + 1),
        //     lane: '入口' + (i + 1)
        //   };
        //   lanesArr.push(obj1);
        // }
        // for (var j = 0; j < exit_number; j++) {
        //   // lanesArr.push('出口' + (j + 1));
        //   var obj2 = {
        //     id: prefix + '-' + (j + 1),
        //     lane: '出口' + (j + 1)
        //   };
        //   lanesArr.push(obj2);
        // }
        this.lanesArr = lanesArr;
      },
      getUerList () {
        let url = this.api.URL_teamClassList;
        var params = {
          dept_id: this.config.user_dept_id,
          start_time: this.config.curDay + ' 00:00:00',
          end_time: this.config.curDay + ' 00:00:00',
          type: this.config.team_type
        };
        if (this.config.user_dept_id) {
          // params.dept_id = this.config.user_dept_id;// 收费站
          var _this = this;
          this.$request({
            url: url,
            method: 'post',
            data: params
          }).then(res => {
            var tableData = res.data.result_data;
            var dataMap = new Map();
            for (var i in tableData) {
              var d = tableData[i];
              dataMap.set(d.class_type, d);
            }
            var classTableData = [];
            _this.classCode.forEach(function (item, key, mapObj) {
              var newItem = {};
              _this.copyProps(item, newItem);
              newItem.class_start_time = item.start_time;
              newItem.class_end_time = item.end_time;
              classTableData.push(newItem);
            });
            for (var j in classTableData) {
              var cdd = classTableData[j];
              _this.copyProps(dataMap.get(cdd.code), cdd);
              var data = {};
              for (var jj in _this.lanesArr) {
                var l = _this.lanesArr[jj];
                data[l.id] = {user_id: '', list: []};
              }
              cdd.data = data;
            }
            _this.tableData = classTableData;
            // this.check();
            _this.getClassList();
          });
        }
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
            if (classList && classList.length > 1) {
              this.operation = 'update';
            } else {
              this.operation = 'add';
            }
            for (var i in classList) {
              var d = classList[i];
              var obj = _this.getFromTableData(d.class_type);
              obj.monitor_user_id = d.monitor_user_id;
              if (obj != null) {
                obj.data[this.lanePrefix + d.lane].user_id = d.user_id;
                obj.data[this.lanePrefix + d.lane].list = d.list;
              }
            }
            // this.check();
            console.log(_this.tableData);
          });
        }
      },
      getFromTableData (class_type) {
        var classTableData = this.tableData;
        for (var j in classTableData) {
          var cdd = classTableData[j];
          if (cdd.code == class_type) {
            return cdd;
          }
        }
        return null;
      },
      onSubmit () {
        let params = this.urlAddParam();
        if (this.operation === 'add') {
          let url = this.api.URL_add;
          // params.id = '';
          // params.type = 1;// 收费站
          this.$request({
            url: url,
            method: 'post',
            data: params
          }).then(res => {
            this.close();
            this.$message({
              message: '添加成功！',
              type: 'success'
            });
            this.$emit('closeDutyClassDialog');
          });
        } else {
          let url = this.api.URL_update;
          // let params = this.urlAddParam();
          this.$request({
            url: url,
            method: 'post',
            data: params
          }).then(res => {
            this.close();
            this.$message({
              message: '修改成功！',
              type: 'success'
            });
            this.$emit('closeDutyClassDialog');
          });
        }
      },
      getClassObj (i, teamId) {
        var curDay = this.config.curDay;
        var classCode = this.classCode;
        var start_time = curDay + ' ' + classCode.get(i).start_time + ':00';
        if (i == 0) {
          var date = new Date(curDay.replace(/-/, '/'));
          date.setDate(date.getDate() - 1);
          start_time = date.Format('yyyy-MM-dd') + ' ' + classCode.get(i).start_time + ':00';
        }
        var obj = {
          class_type: i,
          team_id: teamId,
          work_date: curDay + ' 00:00:00',
          start_time: start_time,
          end_time: curDay + ' ' + classCode.get(i).end_time + ':00',
          dept_id: this.config.user_dept_id,
          user_type: this.config.user_type // 表示收费站
        };
        return obj;
      },
      copyProps (from, to) {
        if (!from) {
          return to;
        };
        for (var attr in from) {
          var prop = attr;
          to[prop] = from[prop];
        }
        return to;
      },
      urlAddParam () {
        var params = [];
        var tableData = this.tableData;
        for (var i in tableData) {
          var tobj = tableData[i];
          var data = tobj.data;
          if (data) {
            var monitor_id = '0';
            if (data[this.lanePrefix + '0']) {
              monitor_id = data[this.lanePrefix + '0'].user_id;
            }
            for (var attr in data) {
              if (data[attr].user_id) {
                var lane = attr.substring(5, attr.length);
                var obj = {
                  list: data[attr].list,
                  work_date: tobj.work_date + ':00',
                  dept_id: tobj.dept_id,
                  user_id: data[attr].user_id,
                  class_type: tobj.class_type,
                  team_id: tobj.team_id,
                  start_time: tobj.start_time + ':00',
                  end_time: tobj.end_time + ':00',
                  lane: lane,
                  monitor_user_id: monitor_id
                };
                params.push(obj);
              }
            }
          }
        }
        return params;
      },
      check () {
        if (this.form.memberList) {
          for (var i in this.form.memberList) {
            var obj = this.form.memberList[i];
            this.memberCheckList.push(parseInt(obj.id));
          }
        } else {
          this.memberCheckList = [];
        }
        // for (var j in this.userList) {
        //   if (m.has(this.userList[j].id)) {
        //     this.userList[j].checked = true;
        //   }
        // }
      },
      showUnRegular (val, calss_type) {
        this.unRegularDialogVisible = true;
        this.unRegularClassTableData = this.getFromTableData(calss_type).data[val].list;
        this.monitor_user_id = this.getFromTableData(calss_type).monitor_user_id;
        for (var i = 0; i < this.unRegularClassTableData.length; i++) {
          this.unRegularClassTableData[i].num = i + 1;
        }
        this.onDutyUserList = this.getFromTableData(calss_type).users;
        console.log(this.unRegularClassTableData);
      },
      showNewUnRegularDetail (id) {
        this.newUnRegularDialogVisible = true;
        this.unRegularDetail = {};
      },
      deleteRow (index, rows) {
        rows.splice(index, 1);
      },
      handleClose () {
      },
      saveUnRegular () {
        var obj = this.copyProps({}, this.unRegularDetail);
        obj.monitor_user_id = this.monitor_user_id;
        obj.start_time = obj.start_time.Format('yyyy-MM-dd HH:mm:00');
        obj.end_time = obj.end_time.Format('yyyy-MM-dd HH:mm:00');
        this.unRegularClassTableData.push(obj);
        this.newUnRegularDialogVisible = false;
        // this.showUnRegular();
        console.log(this.tableData);
      },
      unRegularUserChange () {
        for (var i in this.onDutyUserList) {
          if (this.onDutyUserList[i].id == this.unRegularDetail.user_id) {
            this.unRegularDetail.user_name = this.onDutyUserList[i].truename;
            break;
          }
        }
      },
      changeOndutyUserValue (val) {
        console.log(val);
        console.log(this.tableData);
      },
      changeSubMonitorValue (val) {
        console.log(this.form.name);
      },
      handleCheckedChange (val) {
        // console.log(this.memberCheckList);
        let checkedIds = '';
        for (var i in val) {
          checkedIds += val[i] + ',';
        }
        if (checkedIds.length > 0) {
          checkedIds = checkedIds.substr(0, checkedIds.length - 1);
        }
        this.checkedIds = checkedIds;
        console.log(this.checkedIds);
      },
      getClassCode () {

      }
    },
    mounted: function () {
      this.initAll();
      // this.check();
    }
  };
</script>

<style>
  .tb_row td {
    vertical-align:top
  }

  /* .el-dialog__body {
    text-align: center;
  } */
</style>
