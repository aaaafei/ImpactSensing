<template>
  <div id="overhaul1">
    <div id="body">
      <div id="table">
        <el-table
          v-loading="loading"
          :data="overhaulList"
          stripe
          height="300px"
          style="width: 100%">
          <el-table-column
            align="center"
            type="index"
            width="60"
            fixed
            :index="indexMethod"
            label="序号">
          </el-table-column>
          <el-table-column
            align="center"
            prop="name"
            show-overflow-tooltip
            label="工单名称">
          </el-table-column>
          <el-table-column
            align="center"
            prop="comptName"
            show-overflow-tooltip
            label="检修分区">
          </el-table-column>
          <el-table-column
            align="center"
            prop="startTime"
            show-overflow-tooltip
            label="开始时间">
          </el-table-column>
          <el-table-column
            align="center"
            prop="endTime"
            show-overflow-tooltip
            label="完成期限">
          </el-table-column>
          <el-table-column
            align="center"
            prop="orgName"
            show-overflow-tooltip
            label="检修部门">
          </el-table-column>
          <el-table-column
            align="center"
            prop="workFromName"
            show-overflow-tooltip
            label="工单来源">
          </el-table-column>
          <el-table-column
            align="center"
            prop="deviceName"
            show-overflow-tooltip
            label="检修设备">
          </el-table-column>
          <el-table-column
            align="center"
            prop="remark"
            show-overflow-tooltip
            label="备注">
          </el-table-column>
          <el-table-column
            align="center"
            prop="statusName"
            show-overflow-tooltip
            label="状态">
          </el-table-column>
          <el-table-column
            align="center"
            label="操作"
            fixed="right"
            width="150">
            <template slot-scope="scope">
              <el-button v-show="scope.row.statusName === '已保存'"
                         @click="editOrder(scope.$index, scope.row)" type="text">编辑
              </el-button>
              <el-button v-show="scope.row.statusName === '已保存'"
                         @click="deleteOrder(scope.$index, scope.row)" type="text">删除
              </el-button>
              <el-button
                @click="queryOrder(scope.$index, scope.row)" type="text">查看
              </el-button>
              <el-button v-show="scope.row.statusName === '已上报'"
                         @click="auditOrder(scope.$index, scope.row)" type="text">审核
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pageDiv">
          <el-pagination
          class="page"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :page-sizes="[10, 20, 30, 40, 50]"
            :current-page.sync="pageNum"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </div>
        <addOverhaul :addVisible="addVisible" :propForm="propForm" :sourceList="sourceList" :sectionList="sectionList"
                     :title="title" v-on:headCallBack="callBack"  v-on:headCallBackSubmit="callBackSubmit">
        </addOverhaul>
        <auditOrder :auditVisible="auditVisible" :auditId="auditId" v-on:headCallBack="callBack" v-on:headCallBackAuditOrderSubmit="submitCallBackAuditOrder">
        </auditOrder>
        <queryOverhaul :queryVisible="queryVisible" :propForm="propForm"
                       :title="title" v-on:headCallBack="queryCallBack">
        </queryOverhaul>

      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery';
import addOverhaul from './addOverhaul.vue';
import auditOrder from './auditOrder.vue';
import queryOverhaul from './queryOverhaul.vue';
// import orgTree from '../../../maintenance_manage/routeInspection_manage/orgTree.vue';
export default {
  name: 'overhaul1',
  props: ['agencyFlag'],
  data: function () {
    return {
      loading: false,
      addVisible: false, // 新增窗口
      auditVisible: false, // 审核窗口
      queryVisible: false, // 查看窗口
      pageSize: 10,
      pageNum: 1,
      total: 0,
      title: '',
      propForm: {
        id: '', // 工单id
        code: '', // 工单编码
        name: '', // 工单名称
        startTime: '', // 开始时间
        endTime: '', // 完成期限
        workFrom: '', // 工单来源
        workFromName: '', // 工单来源名称
        section: '', // 路段id
        compt: '', // 分区id
        comptName: '', // 检修分区
        org: '', // 检修部门id
        orgName: '', // 检修部门
        deviceType: '', // 设备类型
        device: '', // 设备id
        deviceName: '', // 设备名称
        status: '', // 工单状态
        statusName: '', // 状态名称
        remark: ''// 备注
      },
      auditId: '', // 审核id
      overhaulList: [],
      sectionList: [], // 巡检分段
      sourceList: [] // 工作来源
    };
  },
  components: {
    addOverhaul: addOverhaul,
    queryOverhaul: queryOverhaul,
    // orgTree: orgTree,
    auditOrder: auditOrder
  },
  methods: {
    indexMethod (index) {
      return (this.pageNum - 1) * this.pageSize + index + 1;
    },
    search () { //
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        }
      };
      this.loading = true;
      this.$http.get(this.$api_prefix + '/overhaul/todo?pageNum=' + this.pageNum + '&pageSize=' + this.pageSize, options).then((res) => { // 调用查询巡检计划列表接口
        let code = res.body.code;
        if (code === 401) {
          this.$message.error('请重新登录！');
          this.$router.push('/login');
          return false;
        }
        let success = res.body.success;
        if (success === true) {
          this.overhaulList = res.body.data.list;
          this.total = res.body.data.total;
          this.loading = false; // 查询结束后，停止加载
        } else {
          this.loading = false; // 查询结束后，停止加载
          this.$message.error(res.body.message);
        }
      }, (res) => {
        this.$message.error('服务器暂时无法连接，请稍后再试！');
        this.loading = false; // 查询结束后，停止加载
      });
      // this.loading = false; // 查询结束后，停止加载
    },
    editOrder (index, rows) { // 修改
      var pojo = this.overhaulList[index];
      this.propForm.id = pojo.id;
      this.propForm.name = pojo.name;
      this.propForm.startTime = pojo.startTime;
      this.propForm.endTime = pojo.endTime;
      this.propForm.workFrom = pojo.workFrom;
      this.propForm.workFromName = pojo.workFromName;
      this.propForm.compt = pojo.compt;
      this.propForm.section = pojo.section;
      this.propForm.comptName = pojo.comptName;
      this.propForm.org = pojo.org;
      this.propForm.orgName = pojo.orgName;
      this.propForm.deviceType = pojo.deviceType;
      this.propForm.device = pojo.device;
      this.propForm.code = pojo.code;
      this.propForm.deviceName = pojo.deviceName;
      this.propForm.status = pojo.status;
      this.propForm.statusName = pojo.statusName;
      this.propForm.remark = pojo.remark;
      this.addVisible = true;
      this.title = '修改检修工单';
      setTimeout(function () {
        $('.v-modal').css('z-index', '0 !important');
      }, 100);
    },
    auditOrder (index, rows) { // 审核
      this.auditId = rows.id;
      this.auditVisible = true;
      setTimeout(function () {
        $('.v-modal').css('z-index', '0 !important');
      }, 100);
    },
    deleteOrder (index, rows) { // 删除
      this.$confirm('确认删除检修记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        let options = {
          headers: {
            Authorization: this.$store.getters.getToken
          }
        };
        this.$http.get(this.$api_prefix + '/overhaul/delete/' + this.overhaulList[index].id, options).then((res) => {
          if (res.body.code === 401) {
            this.$router.push('/login');
          }
          let success = res.body.success;
          if (success === true) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            // this.overhaulList.splice(index, 1);
            this.search();
          } else {
            this.$message.error(res.body.message);
          }
        }, (res) => {
          this.$message.error('服务器暂时无法连接，请稍后再试！');
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    queryOrder (index, rows) { // 查看
      var pojo = this.overhaulList[index];
      this.propForm.id = pojo.id;
      this.propForm.name = pojo.name;
      this.propForm.startTime = pojo.startTime;
      this.propForm.endTime = pojo.endTime;
      this.propForm.workFrom = pojo.workFrom;
      this.propForm.workFromName = pojo.workFromName;
      this.propForm.compt = pojo.compt;
      this.propForm.comptName = pojo.comptName;
      this.propForm.org = pojo.org;
      this.propForm.orgName = pojo.orgName;
      this.propForm.device = pojo.device;
      this.propForm.code = pojo.code;
      this.propForm.deviceName = pojo.deviceName;
      this.propForm.status = pojo.status;
      this.propForm.statusName = pojo.statusName;
      this.propForm.remark = pojo.remark;
      this.queryVisible = true;
      // console.log($('.v-modal'));
      setTimeout(function () {
        $('.v-modal').css('z-index', '0 !important');
      }, 100);
    },
    queryworkfromList () { // 查询工单来源list
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        }
      };
      this.$http.get(this.$api_prefix + '/dict/codeMap/work_from_type', options).then((res) => { // 调用查询巡检计划列表接口
        if (res.body.code === 401) {
          this.$router.push('/login');
        }
        let success = res.body.success;
        if (success === true) {
          let result = res.body.data.work_from_type;
          for (let i in result) {
            let source = {
              value: i,
              label: result[i]
            };
            this.sourceList.push(source);
          }
        } else {
          this.$message.error(res.body.message);
        }
      }, (res) => {});
    },
    querysectionList () { // 查询巡检路段
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        }
      };
      this.$http.get(this.$api_prefix + '/section/list', options).then((res) => { // 调用查询巡检计划列表接口
        if (res.body.code === 401) {
          this.$router.push('/login');
        }
        let success = res.body.success;
        if (success === true) {
          let result = res.body.data;
          for (let i = 0; i < result.length; i++) {
            let section = {
              value: result[i]['id'],
              label: result[i]['name']
            };
            this.sectionList.push(section);
          }
        } else {
          this.$message.error(res.body.message);
        }
      }, (res) => {});
    },
    handleSizeChange (val) { // 修改每页条数
      this.pageSize = val;
      this.search();
    },
    handleCurrentChange (val) { // 跳到下一页
      this.pageNum = val;
      this.search();
    },
    callBack (msg, depart) {
      if (depart !== null && depart !== undefined) {
        this.orgVisible = msg;
        this.form.org = depart['id'];
        this.form.orgName = depart['label'];
      } else {
        this.addVisible = msg;
        this.queryVisible = msg;
        this.auditVisible = msg;
      }
    },
    submitCallBackAuditOrder (msg, depart) {
      if (depart !== null && depart !== undefined) {
        this.orgVisible = msg;
        this.form.org = depart['id'];
        this.form.orgName = depart['label'];
      } else {
        this.addVisible = msg;
        this.queryVisible = msg;
        this.auditVisible = msg;
      }
      this.search();
    },
    callBackSubmit (msg, depart) {
      if (depart !== null && depart !== undefined) {
        this.orgVisible = msg;
        this.form.org = depart['id'];
        this.form.orgName = depart['label'];
      } else {
        this.addVisible = msg;
        this.queryVisible = msg;
        this.auditVisible = msg;
      }
      this.search();
    },
    queryCallBack (msg, depart) {
      if (depart !== null && depart !== undefined) {
        this.orgVisible = msg;
        this.form.org = depart['id'];
        this.form.orgName = depart['label'];
      } else {
        this.addVisible = msg;
        this.queryVisible = msg;
        this.auditVisible = msg;
      }
    }
  },
  mounted: function () {
    this.$nextTick(function () {
      this.search();
      this.queryworkfromList();
      this.querysectionList();
    });
  }
  // watch: {
  //   agencyFlag: function (val) { // 此处不要使用箭头函数    监听是否增删改的操作成功，成功就执行把刷新列表
  //     console.log('代表1:' + val);
  //     this.overhaulList = [];
  //     if (val) {
  //       this.search();
  //     }
  //   }
  // }
};
</script>

<style>
#overhaul1 .search{
  overflow: auto;
}
#overhaul1 .search-input{
  margin-top: 20px;
  background-color: rgb(255, 255, 255);
  padding-top: 10px;
  padding-bottom: 10px;
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
}
#overhaul1 .search-button{
  margin-bottom: 10px;
  margin-top: 10px
}
#overhaul1 .input{
  width: 150px;
}
#overhaul1 .el-date-editor .el-input{
   width: 150px;
}
#overhaul1 .el-form-item__label{
  font-size: 10px;
}
#overhaul1 .search .el-form-item{
  margin-bottom: 0px;
  margin-right: 0px;
}
#overhaul1 .search .el-form-item button{
  margin-left: 30px;
}
#overhaul #body{
  margin-left: 20px;
  padding-bottom: 20px;
  margin-right: 20px;
}
#overhaul1 .left-button{
  width: 70px;
}
#overhaul1 .right-button{
  float: right;
  width: 70px;
}
#overhaul1 #table{
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
}
#overhaul1 .pageDiv{
  height: 35px;
  background-color: rgb(255, 255, 255);
}
#overhaul1 .page{
  float: right;
}
#overhaul1 .el-pager li.active {
  color: rgb(255, 255, 255);
  background-color: #8c9ec4;
  border: 1px solid;
  width: 27px;
  border-radius: 6px;
}
#overhaul1 .el-pager li.number {
  width: 27px;
  border: 1px solid #d4d4d4;
  border-radius: 6px;
}
#overhaul1 .el-pager li{
  width: 27px;
  min-width: 0px;
  margin-left: 10px;
  font-family: 宋体;
}
#overhaul1 .el-pagination button{
  border: 1px solid #d4d4d4;
  border-radius: 6px;
  width: 27px;
  min-width: 0px;
}
#overhaul1 .el-pagination .btn-next {
  margin-left: 10px;
  padding-left: 6px;
}
.el-icon-circle-close{
  cursor:pointer;
  font-size:14px;
  color:#c0c4cc00;
}
#CompartmentDialogId1 .el-dialog__body{
  height: 300px;
  overflow: auto;
}
#overhaul1 .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
  background-color: #4baff2;
  color: #fff;
}
</style>
