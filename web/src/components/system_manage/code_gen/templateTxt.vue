<template>
  <div id="template">
    <el-select v-model="selectedTemplate" @change="searchTemplateTxts" placeholder="请选择" style="margin-left:25px;margin-top:10px;">
      <el-option
        v-for="item in templates"
        :key="item.name"
        :label="item.name"
        :value="item.id">
      </el-option>
    </el-select>
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card :body-style="{ padding: '0px'}" style="margin-bottom:10px;" shadow="hover">
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
      <el-col :span="4" v-for="(o, index) in templateTxts" :key="index">
        <el-card :body-style="{ padding: '0px'}" style="margin-bottom:10px;" shadow="hover">
          <div class="db-title">
            <span>{{ o.name }}</span>
          </div>
          <img src="../../../../static/images/templateTxt.png" class="image" @click="openDetail(index, 'look')">
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
    <el-dialog width="60%" :title="dialogTitle" :visible="dialogVisible" @close="closeDialog" @open="searchTemplates">
      <tlDetail v-model="detailForm" :operation="operation" :detailForm="detailForm" :detailVisible="saveBtnVisible" :datasources="datasources" @close="closeDialog" @refresh="refresh"></tlDetail>
    </el-dialog>
  </div>
</template>

<script>
  // import {mapGetters} from 'vuex';
  import tlDetail from './tlTxtDetail';
  import request from '@/assets/utils/request';
  export default {
    name: 'user',
    data: function () {
      return {
        moduleName: '模板内容',
        value: '',
        templates: [],
        templateTxts: [],
        selectedTemplate: '',
        detailForm: {
          id: '',
          name: '',
          templateId: '',
          templateName: '',
          pkg: '',
          content: ''
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
      searchTemplateTxts () {
        // let obj = {};
        // obj = this.templates.find((item) => {
        //   return item.id === val;
        // });
        // this.selectedTemplate.id = val;
        // this.selectedTemplate.name = obj.name;
        request({url: '/code/templateContent/list?templateId=' + this.selectedTemplate
        }).then(res => {
          this.templateTxts = res.data.data;
        });
      },
      openAddDetail () {
        if (this.selectedTemplate) {
          this.setFormProperties(this.initFormProperties());
          this.operation = 'add';
          this.saveBtnVisible = true;
          this.dialogTitle = '新增' + this.moduleName;
          this.showDialog();
        } else {
          this.$message({
            message: '请先选择模板！',
            type: 'warning'
          });
        }
      },
      openDetail (i, flag) {
        let detail = this.templateTxts[i];
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
        this.detailForm.templateId = detail.templateId;
        this.detailForm.pkg = detail.pkg;
        this.detailForm.content = detail.content;
      },
      initFormProperties () {
        let detail = {};
        detail.id = '';
        detail.name = '';
        detail.templateId = this.selectedTemplate;
        detail.pkg = '';
        detail.content = '';
        return detail;
      },
      showDialog () {
        this.dialogVisible = true;
      },
      closeDialog () {
        this.dialogVisible = false;
      },
      refresh () {
        this.searchTemplateTxts();
      },
      deleteDbDetail (id) {
        this.$confirm('是否确定删除？', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '删除',
          cancelButtonText: '放弃'
        })
        .then(() => {
          let url = '/code/templateContent/delete/' + id;
          // url += '&url=' + this.form.url;
          request({url: url
          }).then(res => {
            this.$message({
              message: '删除成功！',
              type: 'success'
            });
            this.searchTemplateTxts();
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
