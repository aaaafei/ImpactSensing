<template>
  <!-- <div>
    <el-dialog title="按钮详情" @close="close('')" :visible="detailVisible"> -->
      <el-form  :model="form"  label-width="180px">
        
        <el-form-item :label="'早班(' + classCode.get(1).start_time + '-' + classCode.get(1).end_time + ')：'" prop="class2">
          <el-select v-model="form.morningClass" clearable @change="changeSubMonitorValue" placeholder="请选择">
            <el-option
              v-for="item in teamList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="'中班(' + classCode.get(2).start_time + '-' + classCode.get(2).end_time + ')：'" prop="class3">
          <el-select v-model="form.noonClass" clearable @change="changeSubMonitorValue" placeholder="请选择">
            <el-option
              v-for="item in teamList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="'夜班(' + classCode.get(3).start_time + '-次日' + classCode.get(3).end_time + ')：'" prop="class1" >
          <el-select v-model="form.nightClass" clearable @change="changeMonitorValue" placeholder="请选择">
            <el-option
              v-for="item in teamList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-if="saveBtnVisible" @click="onSubmit">保存</el-button>
          <!-- <el-button>取消</el-button> -->
        </el-form-item>
      </el-form>
    <!-- </el-dialog>
  </div> -->
</template>

<script>
/* eslint-disable */
  export default {
    name: 'dutyTeamDetail',
    props: ['saveBtnVisible', 'detailForm', 'operation', 'teamData', 'classCode', 'query', 'config'],
    data: function () {
      return {
        form: this.teamData ? this.teamData : {
          morningClass: null,
          noonClass: null,
          nightClass: null
        },
        saveVisible: this.saveBtnVisible,
        memberCheckList: [],
        checkedIds: null,
        teamList: [],
        classList: [
          {code: '1', name: '晚班'}, {id: '2', name: '早班'}
        ],
        api: {
          URL_add: '/teamClass/add/list',
          URL_update: '/teamClass/update/list',
          URL_update_delete: '/teamClass/deleteByDeptAndTime',
          URL_teamList: '/team/getTeamByDeptId/1/50',
          URL_dict: '/dict/subCodes/'
        }
      };
    },
    watch: {
      query: function () {
        this.initAll();
      }
    },
    methods: {
      initAll () {
        this.form = this.teamData ? this.teamData : {
          morningClass: null,
          noonClass: null,
          nightClass: null
        };
        // for (var i in this.classCode) {
        //   var obj = this.classCode[i];
        //   var label
        // }
        this.classList = this.classCode;
        this.getUerList();
      },
      close () {
        this.$emit('closeDetail', '');
      },
      getUerList () {
        let url = this.api.URL_teamList;
        let params = {};
        if (this.config.user_dept_id) {
          params.dept_id = this.config.user_dept_id;// 收费站
          this.$request({
            url: url,
            method: 'post',
            data: params
          }).then(res => {
            this.teamList = res.data.result_data.list;
            for (var i in this.teamList) {
              var obj = this.teamList[i];
              obj.name = obj.name + '(' + obj.user_ids_name + ')';
            }
            // this.check();
          });
        }
      },
      onSubmit () {
        let params = this.urlAddParam();
        if (this.operation === 'add') {
          let url = this.api.URL_add; 
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
            this.$emit('closeAndRefreshCal');
          });
        } else {
          let url = this.api.URL_update; 
          if (params.length == 0) {
            params = {
              dept_id: this.config.user_dept_id,
              work_date: this.config.curDay + ' 00:00:00'
            };
            this.$request({
              url: this.api.URL_update_delete,
              method: 'post',
              data: params
            }).then(res => {
              this.close();
              this.$message({
                message: '修改成功！',
                type: 'success'
              });
              this.$emit('closeAndRefreshCal');
            });
          } else {
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
              this.$emit('closeAndRefreshCal');
            });
          }
        }
      },
      getClassObj (i, teamId) {
        var curDay = this.config.curDay;
        let date = new Date(curDay.replace(/-/, '/'));
        date.setDate(date.getDate() + 1);
        let nextDay = date.Format('yyyy-MM-dd');
        var classCode = this.classCode;
        var start_time = curDay + ' ' + classCode.get(i).start_time + ':00';
        if (i == 0) {
          date = new Date(curDay.replace(/-/, '/'));
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
        if (i == 3) {
          // 夜班跨夜，结束时间需要往后加一天
          obj.end_time = nextDay + ' ' + classCode.get(i).end_time + ':00';
        }
        return obj;
      },
      urlAddParam () {
        var data = [];
        if (this.form.nightClass) {
          data.push(this.getClassObj(3, this.form.nightClass));
        }
        if (this.form.morningClass) {
          data.push(this.getClassObj(1, this.form.morningClass));
        }
        if (this.form.noonClass) {
          data.push(this.getClassObj(2, this.form.noonClass));
        }
        return data;
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
      changeMonitorValue (val) {
        console.log(this.form.nightClass);
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

<style type="stylus">

</style>
