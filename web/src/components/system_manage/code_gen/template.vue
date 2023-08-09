<template>
  <div id="template">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <div class="db-title">
            <span>新建模板</span>
          </div>
          <img src="../../../../static/images/add.png" class="image" @click="openAddDetail">
          <div style="padding: 16px 5px;">
            <div class="bottom">
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4" v-for="(o, index) in templates" :key="index">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <div class="db-title">
            <span>{{ o.name }}</span>
          </div>
          <img src="../../../../static/images/template.png" class="image" @click="openDetail(index, 'look')">
          <div style="padding: 3px 5px;">
            <div class="db-bottom clearfix">
              <!-- <el-button class="button">测试连接</el-button> -->
              <el-button class="button" type="text" icon="el-icon-edit" @click="openDetail(index, 'edit')"></el-button>
              <el-button class="button" type="text" icon="el-icon-tickets" @click="copyDetail(o.id)" v-popover:popover1></el-button>
              <el-button class="button" type="text" icon="el-icon-delete" @click="deleteDetail(o.id)"></el-button>
              <el-popover ref="popover1" placement="top-start" title="复制" width="100" trigger="hover" content="ff">
              </el-popover>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-dialog width="60%" :title="dialogTitle" :visible="dialogVisible" @close="closeDialog" @open="seachDatasources">
      <tlDetail v-model="detailForm"  :operation="operation" :detailForm="detailForm" :detailVisible="saveBtnVisible" :datasources="datasources" @close="closeDialog" @refresh="refresh"></tlDetail>
    </el-dialog>
  </div>
</template>

<script>
  // import {mapGetters} from 'vuex';
  import tlDetail from './tlDetail';
  import request from '@/assets/utils/request';
  export default {
    name: 'user',
    data: function () {
      return {
        moduleName: '模板',
        templates: [],
        detailForm: {
          id: '',
          name: '',
          db: '',
          pjName: '',
          pkg: '',
          tprefix: '',
          copyright: ''
        },
        // detailForm: {},
        datasources: [],
        dialogTitle: '查看' + this.moduleName,
        dialogVisible: false,
        saveBtnVisible: true,
        operation: 'add'
      };
    },
    components: {
      tlDetail: tlDetail
    },
    mounted: function () {
      this.$nextTick(function () {
        this.searchTemplates();
      });
    },
    watch: {
    },
    methods: {
      searchTemplates () {
        request({
          url: '/code/template/list'
        }).then(res => {
          this.templates = res.data.data;
        });
      },
      seachDatasources () {
        request({url: '/code/db/list'
        }).then(res => {
          this.datasources = res.data.data;
        });
      },
      openAddDetail () {
        this.setFormProperties(this.initFormProperties());
        this.operation = 'add';
        this.saveBtnVisible = true;
        this.dialogTitle = '新增' + this.moduleName;
        this.showDialog();
      },
      openDetail (i, flag) {
        let detail = this.templates[i];
        this.operation = flag;
        if (flag === 'edit') {
          this.dialogTitle = '编辑' + this.moduleName;
          this.saveBtnVisible = true;
        } else {
          this.dialogTitle = '查看' + this.moduleName;
          this.saveBtnVisible = false;
        }
        this.setFormProperties(detail);
        this.showDialog();
      },
      setFormProperties (detail) {
        this.detailForm.id = detail.id;
        this.detailForm.name = detail.name;
        this.detailForm.db = detail.db;
        this.detailForm.pjName = detail.pjName;
        this.detailForm.pkg = detail.pkg;
        this.detailForm.tprefix = detail.tprefix;
        this.detailForm.copyright = detail.copyright;
      },
      initFormProperties () {
        let detail = {};
        detail.id = '';
        detail.name = '';
        detail.db = '';
        detail.pjName = '';
        detail.pkg = '';
        detail.tprefix = '';
        detail.copyright = '';
        return detail;
      },
      copyDetail: function (id) {
        if (id) {
          this.$confirm('是否确定复制该模板？', '确认信息', {
            distinguishCancelAndClose: true,
            confirmButtonText: '复制',
            cancelButtonText: '放弃'
          })
          .then(() => {
            request({
              url: '/code/template/duplicate/' + id
            }).then(res => {
              this.$message({
                message: '复制成功！',
                type: 'success'
              });
              this.searchTemplates();
            });
          })
          .catch(action => {
          });
        }
      },
      showDialog () {
        this.dialogVisible = true;
      },
      closeDialog () {
        this.dialogVisible = false;
      },
      refresh () {
        this.searchTemplates();
      },
      deleteDetail (id) {
        this.$confirm('是否确定删除？', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '删除',
          cancelButtonText: '放弃'
        })
        .then(() => {
          let url = '/code/template/delete/' + id;
          // url += '&url=' + this.form.url;
          request({url: url
          }).then(res => {
            this.$message({
              message: '删除成功！',
              type: 'success'
            });
            this.searchTemplates();
          });
        })
        .catch(action => {
        });
      }
    }
  };
</script>

<style>
  #template .el-row {
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
