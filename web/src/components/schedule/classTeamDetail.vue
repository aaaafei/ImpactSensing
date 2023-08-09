<template>
  <!-- <div>
    <el-dialog title="按钮详情" @close="close('')" :visible="detailVisible"> -->
      <el-form  :model="form"  label-width="130px">
        <el-form-item label="名称："  prop="name" >
          <el-input  v-model="form.name"/>
        </el-form-item>
        <el-form-item label="人员：" prop="monitor" >
          <el-select v-model="form.monitor" @change="changeMonitorValue" placeholder="请选择">
            <el-option
              v-for="item in userList"
              :key="item.id"
              :label="item.truename"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="副班长：" prop="sub_monitor">
          <el-select v-model="form.sub_monitor" @change="changeSubMonitorValue" placeholder="请选择">
            <el-option
              v-for="item in userList"
              :key="item.id"
              :label="item.truename"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item> 
        <el-form-item label="人员：" prop="member">
          <el-checkbox-group v-model="memberCheckList">
            <el-checkbox v-for="item in userList" :label="item.id" :key="item.id">{{item.truename}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>-->
        <el-form-item>
          <el-button type="primary" v-if="saveBtnVisible" @click="onSubmit">保存</el-button>
          <!-- <el-button>取消</el-button> -->
        </el-form-item>
      </el-form>
    <!-- </el-dialog>
  </div> -->
</template>

<script>
  export default {
    name: 'teamDetailBox',
    props: ['saveBtnVisible', 'detailForm', 'operation', 'formProperties', 'queryD', 'dept_id'],
    data: function () {
      return {
        form: this.detailForm,
        saveVisible: this.saveBtnVisible,
        memberCheckList: [],
        checkedIds: null,
        userList: [
          {id: '1', name: '张三'}, {id: '2', name: '李四'}
        ],
        api: {
          URL_add: '/team/add',
          URL_update: '/team/update',
          URL_users: '/user/getFsUserList',
          URL_dict: '/dict/subCodes/'
        }
      };
    },
    watch: {
      queryD: function () {
        this.getUerList();
      }
    },
    methods: {
      close () {
        this.$emit('closeDetail', '');
      },
      getUerList () {
        let url = this.api.URL_users;
        let params = {};
        params.organid = this.dept_id;// 收费站
        this.$request({
          url: url,
          method: 'post',
          data: params
        }).then(res => {
          this.userList = res.data.result_data;
          this.check();
        });
      },
      onSubmit () {
        // this.handleCheckedChange();
        if (!this.form.monitor) {
          this.$message({
            message: '请选择人员！',
            type: 'warning'
          });
          return;
        }
        if (this.operation === 'add') {
          let url = this.api.URL_add;
          let params = this.urlAddParam();
          params.id = '';
          params.type = 1;// 收费站
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
            this.$emit('refreshDb');
          });
        } else {
          let url = this.api.URL_update;
          let params = this.urlAddParam();
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
            this.$emit('refreshDb');
          });
        }
      },
      urlAddParam () {
        // url += '&name=' + this.form.name;
        // url += '&url=' + encodeURI(this.form.url);
        // url += '&driverClass=' + this.form.driverClass;
        // url += '&username=' + this.form.username;
        // url += '&password=' + this.form.password;
        let params = {};
        for (var i = 0; i < this.formProperties.length; i++) {
          var prop = this.formProperties[i];
          params[prop] = this.form[prop];
        }
        params.user_ids = params.monitor;
        // if (this.memberCheckList) {
        //   params.monitor = this.memberCheckList[0];
        // }
        // params.dept_id =;
        // params.type = 1;
        // let params = {
        //   ip: this.form.ip,
        //   port: this.form.port,
        //   interval: this.form.interval,
        //   remark: this.form.remark,
        //   valid: true
        // };
        return params;
      },
      check () {
        if (this.form.memberList) {
          for (var i in this.form.memberList) {
            var obj = this.form.memberList[i];
            if (this.memberCheckList.indexOf(parseInt(obj.id)) == -1) {
              this.memberCheckList.push(parseInt(obj.id));
            }
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
        console.log(this.form.monitor);
      },
      changeSubMonitorValue (val) {
        console.log(this.form.sub_monitor);
      },
      handleCheckedChange () {
        // console.log(this.memberCheckList);
        let checkedIds = '';
        var val = this.memberCheckList;
        for (var i in val) {
          checkedIds += val[i] + ',';
        }
        if (checkedIds.length > 0) {
          checkedIds = checkedIds.substr(0, checkedIds.length - 1);
        }
        this.checkedIds = checkedIds;
        console.log(this.checkedIds);
      }
    },
    mounted: function () {
      this.getUerList();
      // this.check();
    }
  };
</script>

<style type="stylus">

</style>
