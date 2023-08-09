<template>
  <div id="dbsource">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <div class="db-title">
            <span>新建数据库</span>
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
            <span>{{ o.name }}</span>
          </div>
          <img src="../../../../static/images/mysql.png" class="image" @click="openDetail(index, 'look')">
          <div style="padding: 3px 5px;">
            <div class="db-bottom clearfix">
              <!-- <el-button class="button">测试连接</el-button> -->
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
  import dbDetail from './dbDetail';
  import request from '@/assets/utils/request';
  export default {
    name: 'user',
    data: function () {
      return {
        moduleName: '数据库',
        dbName: 'mysql',
        dataSources: [],
        detailForm: {
          id: '',
          name: '',
          url: '',
          driverClass: '',
          username: '',
          password: '',
          valid: false
        },
        dialogTitle: '查看' + this.moduleName,
        dialogVisible: false,
        saveBtnVisible: true,
        operation: 'add'
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
          url: '/code/db/list'
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
          url: '/code/db/get/' + detail.id
        }).then(res => {
          let detail = res.data.data;
          this.setFormProperties(detail);
          this.operation = 'edit';
          this.showDbDetail();
        });
      },
      setFormProperties (detail) {
        this.detailForm.id = detail.id;
        this.detailForm.name = detail.name;
        this.detailForm.url = detail.url;
        this.detailForm.driverClass = detail.driverClass;
        this.detailForm.username = detail.username;
        this.detailForm.password = detail.password;
      },
      initFormProperties () {
        let detail = {};
        detail.id = '';
        detail.name = '';
        detail.url = '';
        detail.driverClass = '';
        detail.username = '';
        detail.password = '';
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
      deleteDbDetail (id) {
        this.$confirm('是否确定删除？', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '删除',
          cancelButtonText: '放弃'
        })
        .then(() => {
          request({
            url: '/code/db/delete/' + id
          }).then(res => {
            this.$message({
              message: '删除成功！',
              type: 'success'
            });
            this.seachDatasources();
          });
        })
        .catch(action => {
        });
      }
    }
  };
</script>

<style>
  #dbsource .el-row {
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
    line-height: 14px;
  }
  .db-bottom {
    margin-top: 5px;
    margin-bottom:5px;
    line-height: 12px;
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
