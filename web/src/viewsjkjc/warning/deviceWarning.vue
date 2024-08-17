<template>
  <div class="app-container">
    <div style="padding-bottom: 10px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>告警中心</el-breadcrumb-item>
        <el-breadcrumb-item>受撞告警</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-row type="flex" align="middle" style="height: 80px;background-color: #CECECE;">
      <el-col :span="17">
        <div style="padding-left: 30px;;">
          <span style="color: #568AF2;">日期</span>
          <el-date-picker v-model="date1" type="date" placeholder="选择日期">
          </el-date-picker>
          <span style="color: #568AF2;">至</span>
          <el-date-picker v-model="date2" type="date" placeholder="选择日期">
          </el-date-picker>
        </div>
      </el-col>
      <el-col :span="5">
        <el-input placeholder="设备编码、设备名称、线路、桩号、桥墩号" prefix-icon="el-icon-search" v-model="search">
        </el-input>
      </el-col>
      <el-col :span="2" style="text-align: center;">
        <el-button type="primary" @click="searchData">查询</el-button>
      </el-col>

    </el-row>
    <div style="height:10px;"></div>

    <el-table :data="deviceList" :height="tableHeight" border stripe 
      :header-cell-style="{ background: '#A1B6D8', color: '#fff' }">
      <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
      <el-table-column prop="deviceCode" label="设备IMEI" width="180" align="center"></el-table-column>
      <el-table-column prop="monitorType" label="监测项目" align="center"></el-table-column>
      <el-table-column prop="line" label="线路" align="center"></el-table-column>
      <el-table-column prop="segment" label="区间" align="center"></el-table-column>
      <el-table-column prop="stakeNumber" label="桥墩号" align="center"></el-table-column>
      <el-table-column prop="remark" label="告警内容" width="280" align="center"></el-table-column>
      <el-table-column prop="collectTime" label="时间" width="180" align="center"></el-table-column>
      <el-table-column prop="status" label="当前处置进度" align="center">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.status == '1'">待班组长任务分配</el-tag>
          <el-tag type="warning" v-if="scope.row.status == '2'">待班组成员现场确认、申请消警</el-tag>
          <el-tag type="success" v-if="scope.row.status == '3'">消警审核（班组长）</el-tag>
          <el-tag type="success" v-if="scope.row.status == '99'">已处置完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="_oper" label="操作" width="280" align="center">
        <template slot-scope='scope'>
          <el-button size="mini" plain type="primary" @click="openDialog1(scope.row.id)" v-if="scope.row.status == '1'">任务分配</el-button>
          <el-button size="mini" plain type="primary" @click="openDialog2(scope.row.id)" v-if="scope.row.status == '2'">消警申请</el-button>
          <el-button size="mini" plain type="primary" @click="openDialog3(scope.row)" v-if="scope.row.status == '3'">消警审核</el-button>
          <el-button size="mini" plain type="success" @click="openDisposeRecordDialog(scope.row)">处置记录</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="page.pageNum" :page-sizes="[10, 20, 50]" :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="float: left;"></el-pagination>

    <el-dialog title="告警处置记录" :visible.sync="dialogVisible" width="50%" align="center">
      <div class="dialog-main">
        <el-form :model="alarmForm" label-width="150px">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>告警内容</span>
            </div>
            <el-form-item label="设备IMEI">
              <el-input v-model="alarmForm.deviceCode"></el-input>
            </el-form-item>
            <el-form-item label="线路">
              <el-input v-model="alarmForm.line"></el-input>
            </el-form-item>
            <el-form-item label="区间">
              <el-input v-model="alarmForm.segment"></el-input>
            </el-form-item>
            <el-form-item label="桥墩号">
              <el-input v-model="alarmForm.stakeNumber"></el-input>
            </el-form-item>
            <el-form-item label="监测项目">
              <el-input v-model="alarmForm.monitorType"></el-input>
            </el-form-item>
            <el-form-item label="告警内容">
              <el-input type="textarea" v-model="alarmForm.remark"></el-input>
            </el-form-item>
            <el-form-item label="告警时间">
              <el-col :span="12">
                <el-date-picker type="datetime" placeholder="告警时间" v-model="alarmForm.collectTime" style="width: 100%;"></el-date-picker>
              </el-col>
            </el-form-item>
          </el-card>
        </el-form>

        <el-form :model="resultForm" label-width="150px">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>指派处理</span>
            </div>
            <el-form-item label="责任班组">
              <el-input v-model="resultForm.orgname"></el-input>
            </el-form-item>
            <el-form-item label="班组长">
              <el-input v-model="resultForm.teamLeaderUsername"></el-input>
            </el-form-item>
            <el-form-item label="处理人">
              <el-input v-model="resultForm.disposePricipleUsername"></el-input>
            </el-form-item>
            <el-form-item label="指派时间">
              <el-col :span="12">
                <el-date-picker type="datetime" placeholder="时间" v-model="resultForm.dispatchDatetime" style="width: 100%;"></el-date-picker>
              </el-col>
            </el-form-item>
          </el-card>

          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>现场确认及申请消警</span>
            </div>
            <el-form-item label="处置结果">
              <el-input v-model="resultForm.disposalResult"></el-input>
            </el-form-item>
            <el-form-item label="消警申请人">
              <el-input v-model="resultForm.eliminationApplyUsername"></el-input>
            </el-form-item>
            <el-form-item label="申请时间">
              <el-col :span="12">
                <el-date-picker type="datetime" placeholder="时间" v-model="resultForm.applyDatetime" style="width: 100%;"></el-date-picker>
              </el-col>
            </el-form-item>
          </el-card>
          </el-form>

          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>消警审核</span>
            </div>
            <el-table :data="processList" :height="200" border stripe >
              <el-table-column fixed type="index" label="序号" width="50" align="center"></el-table-column>
              <el-table-column fixed prop="username" label="审核人" align="center"></el-table-column>
              <el-table-column fixed prop="result" label="审核结果" align="center">
                <template slot-scope="scope">
                  <span v-if="scope.row.result == '1'">同意</span>
                  <span v-else>退回</span>
                </template>
              </el-table-column>
              <el-table-column fixed prop="comment" label="审核意见" align="center"></el-table-column>
              <el-table-column fixed prop="createTime" label="审核时间" align="center"></el-table-column>
            </el-table>
          </el-card>

      </div>
    </el-dialog>

    <el-dialog title="任务分配" :visible.sync="dialog1Visible" width="50%">
      <el-form :model="resultForm" label-width="150px">
        <el-input type="hidden" v-model="resultForm.id"></el-input>
        <el-form-item label="责任班组">
          <el-input v-model="resultForm.orgname"></el-input>
        </el-form-item>
        <!-- <el-form-item label="班组长">
          <el-input v-model="form.position"></el-input>
        </el-form-item> -->
        <el-form-item label="处理人">
          <el-select v-model="resultForm.disposePriciple" placeholder="请选择处理人">
            <el-option
              v-for="item in userlist"
              :key="item.id"
              :label="item.truename"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveDispatch">提交</el-button>
      </span>
    </el-dialog>

    <el-dialog title="消警申请" :visible.sync="dialog2Visible" width="50%">
      <el-form :model="resultForm" label-width="150px">
        <el-input type="hidden" v-model="resultForm.id"></el-input>
        <!-- <el-form-item label="现场图片">
          <el-input v-model="form.name"></el-input>
        </el-form-item> -->
        <el-form-item label="处置结果">
          <el-input type="textarea" :rows="3" v-model="resultForm.disposalResult"></el-input>
        </el-form-item>
        
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveApply">提交</el-button>
      </span>
    </el-dialog>

    <el-dialog title="消警审核" :visible.sync="dialog3Visible" width="50%">
      <el-table :data="processList" :height="200" border stripe >
        <el-table-column fixed type="index" label="序号" width="50" align="center"></el-table-column>
        <el-table-column fixed prop="username" label="审核人" align="center"></el-table-column>
        <el-table-column fixed prop="result" label="审核结果" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.result == '1'">同意</span>
            <span v-else>退回</span>
          </template>
        </el-table-column>
        <el-table-column fixed prop="comment" label="审核意见" align="center"></el-table-column>
        <el-table-column fixed prop="createTime" label="审核时间" align="center"></el-table-column>
      </el-table>
      <br>
      <el-form :model="processForm" label-width="150px">
        <el-form-item label="审核意见">
          <el-input v-model="processForm.comment"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="refuse">退回</el-button>
        <el-button type="primary" @click="approve">同意</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
/* eslint-disable */
import DisposeRecordForm from './components/DisposeRecordForm';
export default {
  name: 'deviceWarning',
  components: {
    DisposeRecordForm,
  },
  data() {
    return {
      tableHeight:document.body.clientHeight-330,
      page: {
        pageNum: 1,
        pageSize: 50,
        total: 15
      },
      deviceList: [],
      date1: null,
      date2: null,
      search: "",
      disposeRecordDialog: false,
      dialogVisible: false,
      dialog1Visible: false,
      dialog2Visible: false,
      dialog3Visible: false,
      form: {},
      alarmForm: {},
      resultForm:{},
      processForm:{},
      userlist: [],
      processList: [],
    };
  },
  methods: {
    changeDisposeRecordDialog(value) {
      this.disposeRecordDialog = value;
    },
    openDisposeRecordDialog(form) {
      this.dialogVisible = true;
      this.alarmForm = form;
      this.$request({
        url: '/tmdispositionresult/getTmDispositionResultForAlarm/' + form.id,
        method: 'post',
        // data: param
      }).then(res => {
        this.resultForm  = res.data.result_data;
      });
      this.processForm.alarmRecordId = form.id;
      this.processForm.alarmType = "1";
      this.$request({
        url: '/tmdispositionprocess/getPageData/1/50',
        method: 'post',
        data: this.processForm
      }).then(res => {
        this.processList  = res.data.result_data.list;
      });
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.searchData();
    },
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.searchData();
    },
    openDialog1(id){
      this.dialog1Visible = true;
      this.$request({
        url: '/tmdispositionresult/getTmDispositionResultForAlarm/' + id,
        method: 'post',
        // data: param
      }).then(res => {
        this.resultForm  = res.data.result_data;
        this.$request({
          url: '/user/getUserList',
          method: 'post',
          data: {organid: this.resultForm.orgid}
        }).then(res => {
          this.userlist  = res.data.result_data;
          console.log(this.userlist);
        });
      });
    },
    openDialog2(id){
      this.dialog2Visible = true;
      this.$request({
        url: '/tmdispositionresult/getTmDispositionResultForAlarm/' + id,
        method: 'post',
        // data: param
      }).then(res => {
        this.resultForm  = res.data.result_data;
      });
    },
    openDialog3(form){
      this.dialog3Visible = true;
      this.processForm.alarmRecordId = form.id;
      this.processForm.alarmType = "1";
      this.$request({
        url: '/tmdispositionprocess/getPageData/1/50',
        method: 'post',
        data: this.processForm
      }).then(res => {
        this.processList  = res.data.result_data.list;
      });
    },
    searchData() {
      let param = {};
      if (this.beginDate != undefined) param.beginDate = this.beginDate;
      if (this.endDate != undefined) param.endDate = this.endDate;
      if (this.search != undefined) param.search = this.search;
      this.$request({
        url: '/tmWarningRecords/getPageList/' + this.page.pageNum + '/' + this.page.pageSize,
        method: 'post',
        data: param
      }).then(res => {
        let data = res.data.result_data;
        this.page.total = data.total;
        this.page.pageNum = data.pageNum;
        this.page.pageSize = data.pageSize;
        this.deviceList = data.list;
      });
    },
    saveDispatch() {
      this.$request({
        url: '/tmdispositionresult/saveDispatch',
        method: 'post',
        data: this.resultForm
      }).then(res => {
        this.$message({
          message: '操作成功',
          type: 'success'
        });
        this.searchData();
        this.dialog1Visible = false;
      });
    },
    saveApply() {
      this.$request({
        url: '/tmdispositionresult/saveApply',
        method: 'post',
        data: this.resultForm
      }).then(res => {
        this.$message({
          message: '操作成功',
          type: 'success'
        });
        this.searchData();
        this.dialog2Visible = false;
      });
    },
    approve() {
      this.$request({
        url: '/tmdispositionprocess/approve',
        method: 'post',
        data: this.processForm
      }).then(res => {
        this.$message({
          message: '操作成功',
          type: 'success'
        });
        this.searchData();
        this.dialog3Visible = false;
      });
    },
    refuse() {
      this.$request({
        url: '/tmdispositionprocess/refuse',
        method: 'post',
        data: this.processForm
      }).then(res => {
        this.$message({
          message: '操作成功',
          type: 'success'
        });
        this.searchData();
        this.dialog3Visible = false;
      });
    }
  },
  mounted: function () {
    this.searchData();
  }
};
</script>

<style scoped>
  .dialog-main {
    height: 60vh;
    overflow:auto;
  }
  .box-card {
    margin-bottom: 10px;
  }
  
  /deep/ .el-card__header {
    padding: 5px 20px;
  }
  /deep/ .el-card__body {
    padding: 5px 20px;
  }
  /deep/ .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
