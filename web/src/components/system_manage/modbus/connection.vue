<template>
  <div id="connection">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="">
          <el-button class="button" size="mini" icon="icon-third-qidong" @click="startAll">重启所有任务</el-button>
          <el-button class="button" size="mini" icon="icon-third-tingzhi" @click="startAll">关闭所有任务</el-button>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <div class="db-title">
            <span>新建{{moduleName}}</span>
          </div>
          <img src="../../../../static/images/add.png" class="image" @click="openAddDetail">
          <div style="padding: 16px 5px;">
            <div class="bottom">
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4" v-for="(o, index) in dataSources" :key="index">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <div class="db-title">
            <!-- <el-badge v-if="o.running==1" :value="200" :max="99" class="item">
            </el-badge> -->
            <el-row style="margin:0px !important;" type="flex" justify="center">
              <el-col :span="3">
                <div v-if="o.running==1" style="width:12px;height:12px;background-color:#00B050;border-radius:50%;margin-top:4px;"></div>
                <div v-if="o.running!=1" style="width:12px;height:12px;background-color:gray;border-radius:50%;margin-top:4px;"></div>
              </el-col>
              <el-col :span="21"><span style="float:left;">{{ o.ip +':'+ o.port}}</span></el-col>
            </el-row>
          </div>
          <img src="../../../../static/images/conn.png" class="image" @click="openDetail(index, 'look')">
          <div style="padding: 3px 5px;">
            <div class="db-bottom clearfix">
              <el-button class="button" type="text" icon="icon-third-qidong" @click="start(o.id)"></el-button>
              <el-button class="button" type="text" icon="icon-third-tingzhi" @click="stop(o.id)"></el-button>
              <el-button class="button" type="text" icon="el-icon-edit" @click="openDetail(index, 'edit')"></el-button>
              <el-button class="button" type="text" icon="el-icon-delete" @click="deleteDbDetail(o.id)"></el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-dialog width="60%" :title="dialogTitle" :visible="dialogVisible" @close="closeDbDetail">
      <dbDetail v-model="detailForm"  :operation="operation" :detailForm="detailForm" :saveBtnVisible="saveBtnVisible" @close="closeDbDetail" @refreshDb="refreshDb"></dbDetail>
    </el-dialog>
  </div>
</template>

<script>
  import dbDetail from './conDetail';
  import request from '@/assets/utils/request';
  export default {
    name: 'user',
    data: function () {
      return {
        moduleName: '连接',
        dbName: 'mysql',
        dataSources: [],
        detailForm: {
          id: '',
          ip: '',
          port: '',
          interval: '',
          remark: '',
          valid: false
        },
        formProperties: [
          'ip', 'port', 'interval', 'remark'
        ],
        dialogTitle: '查看' + this.moduleName,
        dialogVisible: false,
        saveBtnVisible: true,
        operation: 'add',
        api: {
          URL_list: '/modbus/modbusConnect/list',
          URL_get: '/modbus/modbusConnect/get/',
          URL_del: '/modbus/modbusConnect/delete/',
          URL_start: '/modbus/deviceDataSchedule/start/',
          URL_stop: '/modbus/deviceDataSchedule/stop/',
          URL_startAll: '/modbus/deviceDataSchedule/restartAll',
          URL_stopAll: '/modbus/deviceDataSchedule/stopAll'
        }
      };
    },
    components: {
      dbDetail: dbDetail
    },
    // created: function () {
    //   this.seachDatasources();
    // },
    mounted: function () {
      this.$nextTick(function () {
        this.seachDatasources();
      });
    },
    methods: {
      seachDatasources () {
        request({
          url: this.api.URL_list
        }).then(res => {
          this.dataSources = res.data.data;
        });
      },
      openAddDetail () {
        this.setFormProperties(this.initFormProperties());
        this.operation = 'add';
        this.dialogTitle = '新建' + this.moduleName;
        this.saveBtnVisible = true;
        this.showDbDetail();
      },
      openDetail (i, flag) {
        let detail = this.dataSources[i];
        if (flag === 'edit') {
          this.dialogTitle = '编辑' + this.moduleName;
          this.saveBtnVisible = true;
        } else {
          this.dialogTitle = '查看' + this.moduleName;
          this.saveBtnVisible = false;
        }
        request({
          url: this.api.URL_get + detail.id
        }).then(res => {
          let detail = res.data.data;
          this.setFormProperties(detail);
          this.operation = 'edit';
          this.showDbDetail();
        });
      },
      setFormProperties (detail) {
        // this.$set(this.detailForm, 'id', detail.id);

        for (var i = 0; i < this.formProperties.length; i++) {
          var prop = this.formProperties[i];
          this.detailForm[prop] = detail[prop];
        }
        // this.detailForm.id = detail.id;
        // this.detailForm.ip = detail.ip;
        // this.detailForm.port = detail.port;
        // this.detailForm.interval = detail.interval;
        // this.detailForm.remark = detail.remark;
      },
      initFormProperties () {
        let detail = {};
        detail.id = '';
        detail.ip = '';
        detail.port = '';
        detail.interval = '';
        detail.remark = '';
        return detail;
      },
      showDbDetail () {
        this.dialogVisible = true;
      },
      closeDbDetail () {
        this.dialogVisible = false;
      },
      refreshDb () {
        this.seachDatasources();
      },
      start (id) {
        request({
          url: this.api.URL_start + id
        }).then(res => {
          this.$message({
            message: '启动成功！',
            type: 'success'
          });
          this.refreshDb();
        });
      },
      stop (id) {
        request({
          url: this.api.URL_stop + id
        }).then(res => {
          this.$message({
            message: '停止成功！',
            type: 'success'
          });
          this.refreshDb();
        });
      },
      startAll () {
        request({
          url: this.api.URL_start
        }).then(res => {
          this.$message({
            message: '启动成功！',
            type: 'success'
          });
          this.refreshDb();
        });
      },
      stopAll () {
        request({
          url: this.api.URL_stop
        }).then(res => {
          this.$message({
            message: '停止成功！',
            type: 'success'
          });
          this.refreshDb();
        });
      },
      deleteDbDetail (id) {
        this.$confirm('是否确定删除？', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '删除',
          cancelButtonText: '放弃'
        })
        .then(() => {
          request({
            url: this.api.URL_del + id
          }).then(res => {
            this.$message({
              message: '删除成功！',
              type: 'success'
            });
            this.refreshDb();
          });
        })
        .catch(action => {
        });
      }
    }
  };
</script>

<style>
  #connection .el-row {
    margin: 15px!important;
    margin-top: 10px;
  }
  .el-card {
    border: 0px;
  }
  .time {
    font-size: 13px;
    color: #999;
  }
  .db-title {
    padding:3px 5px;
    margin:5px;
    font-size:12px;
    font-weight: bold;
    line-height: 20px;
    height:20px;
  }
  .db-bottom {
    margin-top: 5px;
    margin-bottom:5px;
    line-height: 20px;
    height:20px;
    float: right;
  }

  .db-bottom .button {
    padding: 2px 3px;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }
</style>
