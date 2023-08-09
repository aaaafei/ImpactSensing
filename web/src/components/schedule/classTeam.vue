<template>
  <div>
    <el-row style="padding:10px;"><el-button size="small"  type="primary" @click="openAddDetail">新增</el-button></el-row>
    <el-table
      :data="tableData"
      border
      style="width: 100%"> 
      <el-table-column label="序号" type="index" :index="indexMethod" width="60" align="center"></el-table-column>
      <el-table-column 
        prop="name"
        label="班组名称"  align="center"
        width="200">
      </el-table-column>
      <!-- <el-table-column
        prop="monitor_name"
        label="班长"
        width="120">
      </el-table-column>
      <el-table-column
        prop="sub_monitor_name"
        label="副班长"
        width="120">
      </el-table-column> -->
      <el-table-column
        prop="user_ids_name"  align="center"
        label="人员">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="200" align="center">
        <template slot-scope="scope">
          <!-- <el-button @click="openDetail(scope.row, 'look')" type="text" size="small">查看</el-button> -->
          <el-button @click="openDetail(scope.row, 'edit')" type="text" size="small">编辑</el-button>
          <el-button type="text" size="small" @click="deleteDetail(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.pageNum"
      :page-size="page.pageSize"
      layout="total, prev, pager, next, jumper"
      :total="page.total">
    </el-pagination>
    <el-dialog
      :title="detailDialogTitle"
      :visible.sync="detailDialogVisible"
      width="40%" append-to-body>
      <detail @closeDetail="closeAndRefreshDetail" :queryD="queryD" :detailForm="detailForm" :dept_id="dept_id" :formProperties="formProperties" :operation="operation" :saveBtnVisible="saveBtnVisible"></detail>
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
  export default {
    name: 'home',
    props: ['query', 'config'],
    data: function () {
      return {
        moduleName: '班组',
        detailDialogTitle: '新增班组',
        detailDialogVisible: false,
        saveBtnVisible: false,
        operation: null,
        queryD: null,// 用以刷新弹出框数据
        dept_id: this.config.user_dept_id,
        tableData: [{
          name: '班组1',
          monitor: '上海',
          sub_monitor: '上海1',
          member: '张三、李四',
          memberList: [
              {id: '1',name:'张三'}
          ]
        }],
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
          URL_list: '/team/getTeamByName/',
          URL_get: '/modbus/modbusDefinition/get/',
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
      ElCol, ElRow, detail
    },
    filters: {
    },
    computed: {
    },
    methods: { 
    indexMethod (index) {
      return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);  
    },
      handleCurrentChange(val) { 
        this.page.pageNum = val;
        this.seachDataList();
      },
      seachDataList () {
        if (!this.config.user_dept_id) {
          this.$message({
            message: '请选择一个部门再进行班组查询！',
            type: 'warning'
          });
          return;
        }
        let params = {
          dept_id: this.config.user_dept_id,
          type: 1
        }
        this.$request({
          url: this.api.URL_list + this.page.pageNum + '/' + this.page.pageSize,
          method: 'post',
          data: params
        }).then(res => {
          let data = res.data.result_data.list;
          for (var i in data) {
            var obj = data[i];
            if(obj.user_ids) {
              var userIdArr = obj.user_ids.split(',');
              var userNameArr = obj.user_ids_name.split('、');
              var arr = [];
              for(var j in userIdArr) {
                arr.push({id: userIdArr[j], name: userNameArr[j]});
              }
              obj.memberList = arr;
            }
          }
          this.tableData = data;
          this.page.total = res.data.result_data.total;
          this.page.pageNum = res.data.result_data.pageNum;
        });
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
        this.seachDataList();
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
</style>
