<template>
  <div id="CODE">
    <el-row>
      <el-form ref="form" label-width="80px">
        <el-form-item label="选择模板">
          <el-select v-model="template.selectedTemplate" placeholder="请选择模板">
            <el-option
              v-for="item in template.templates"
              :key="item.name"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
          <el-button type="primary" @click="seachTables">查询</el-button>
          <el-button type="danger" @click="genCode">生成</el-button>
        </el-form-item>
        <el-form-item label="表名">
          <el-checkbox :indeterminate="table.isIndeterminate" v-model="table.checkAll" @change="handleCheckAllChange" style="padding-left:3px;">全选</el-checkbox>
          <div style="width:100%;height:400px;overflow-y:auto;background:#fff;padding:3px;">
            <el-checkbox-group v-model="table.selectedTables" @change="handleCheckedChange" style="width:200px;" >
              <el-checkbox  v-for="item in table.tables"
                :key="item.tableName"
                :label="item.tableName">{{item.tableName+'('+item.remarks+')'}}</el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form-item>
      </el-form>
    </el-row>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';
  import request from '@/assets/utils/request';
  export default {
    name: 'user',
    data: function () {
      return {
        template: {
          templates: [],
          selectedTemplate: ''
        },
        table: {
          tables: [],
          selectedTables: [],
          selectedAllTables: [],
          checkAll: false,
          isIndeterminate: true
        }
      };
    },
    computed: {
      ...mapGetters({
        token: 'getToken'
      }),
      selectedTableStr: function () {
        if (this.table.selectedTables) {
          let value = '';
          for (var i in this.table.selectedTables) {
            value += this.table.selectedTables[i] + ',';
          }
          if (value.length > 0) {
            value = value.substr(0, value.length - 1);
          }
          return value;
        }
      }
    },
    components: {
    },
    mounted: function () {
      this.$nextTick(function () {
        this.seachTemplates();
        // this.seachTables();
      });
    },
    activated: function () {
      // this.seachOrg();
      // this.seachAllUser();
    },
    watch: {
    },
    methods: {
      seachTemplates () {
        request({
          url: '/code/template/list'
        }).then(res => {
          this.template.templates = res.data.data;
        });
      },
      seachTables () {
        if (this.template.selectedTemplate.length === 0) {
          this.$message.error('请选择模板！');
          return;
        }
        request({
          url: '/code/generator/list?templateId=' + this.template.selectedTemplate
        }).then(res => {
          this.table.tables = res.data.data;
          this.table.selectedAllTables = [];
          if (this.table.tables) {
            for (var i in this.table.tables) {
              this.table.selectedAllTables.push(this.table.tables[i].tableName);
            }
          }
        });
      },
      genCode () {
        if (this.template.selectedTemplate.length === 0) {
          this.$message.error('请选择模板！');
          return;
        }
        if (this.table.selectedTables.length === 0) {
          this.$message.error('请选择表！');
          return;
        }
        this.$request({
          url: '/code/generator/fileKey'
        }).then(res => {
          const fileKey = res.data.data;
          let url = this.$api_prefix + '/code/generator/run?templateId=' + this.template.selectedTemplate + '&tables=' + this.selectedTableStr;
          url += '&key=' + fileKey;
          window.open(url);
        });
      },
      getToken () {
        let options = {
          headers: {
            Authorization: this.$store.getters.getToken
          }
        };
        return options;
      },
      handleCheckAllChange (val) {
        this.table.selectedTables = val ? this.table.selectedAllTables : [];
        this.isIndeterminate = false;
      },
      handleCheckedChange (value) {
        let checkedCount = value.length;
        this.checkAll = checkedCount === this.table.tables.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.table.tables.length;
      }
    }
  };
</script>

<style>
  #CODE .el-row {
    margin: 15px;
    margin-top: 10px;
  }
  .el-card {
    border: 0px;
  }
  .time {
    font-size: 13px;
    color: #999;
  }
  .bottom {
    margin-top: 13px;
    line-height: 12px;
    float: right;
  }

  .bottom .button {
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

  .el-checkbox+.el-checkbox {
      margin-left: 0px !important;
  }
</style>
