<template>
  <div class="app-container">
    <div style="padding-bottom: 10px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>设备信息</el-breadcrumb-item>
        <el-breadcrumb-item>终端装置</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-row type="flex" align="middle" style="height: 80px;background-color: #CECECE;">
      <el-col :span="17">
        <div style="padding-left: 30px;;">
          
        </div>
      </el-col>
      <el-col :span="5">
        <el-input placeholder="设备编码、设备名称、线路、桩号、桥墩号" prefix-icon="el-icon-search" size="small" v-model="search">
        </el-input>
      </el-col>
      <el-col :span="3" style="text-align: center;">
        <el-button type="primary" size="small" @click="searchData">查询</el-button>
        <el-button type="warning" size="small" @click="handleAdd">新增</el-button>
      </el-col>

    </el-row>
    <div style="height:10px;"></div>

    <el-table :data="deviceList" :height="600" border stripe 
      :header-cell-style="{ background: '#A1B6D8', color: '#fff' }">
      <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
      <el-table-column prop="code" label="设备IMEI" align="center"></el-table-column>
      <el-table-column prop="type" label="监测项" align="center"></el-table-column>
      <el-table-column prop="line" label="线路" align="center"></el-table-column>
      <el-table-column prop="segment" label="区间" align="center"></el-table-column>
      <el-table-column prop="stakeNumber" label="桩号、桥墩号" align="center"></el-table-column>
      <el-table-column prop="_pic" label="现场安装图片" width="280" align="center">
        <template slot-scope='scope'>
          <img :src="$STATIC_PREFIX+'images/demo/'+item" alt="" v-for="(item,index) in scope.row.pic1.split(',')" :key="index" style="height: 40px;margin-left: 2px;" @click="previewImage($STATIC_PREFIX+'images/demo/'+item)">
        </template>
      </el-table-column>
      <el-table-column prop="_tx" label="通信部件" width="120" align="center">
        <template slot-scope='scope'>
          <el-button type="success" size="mini" :circle="true" icon="el-icon-check"></el-button>
          <span style="color:#1684FC">运行正常</span>
        </template>
      </el-table-column>
      <el-table-column prop="_sz" label="受撞部件" width="120" align="center">
        <template slot-scope='scope'>
          <el-button type="primary" size="mini" :circle="true" icon="el-icon-check"></el-button>
          <span style="color:#1684FC">无撞击</span>
        </template>
      </el-table-column>
      <el-table-column prop="_oper" label="操作" width="250" align="center">
        <template slot-scope='scope'>
          <el-button size="mini" plain type="warning" @click="handleEdit(scope.row.code)">编辑</el-button>
          <el-button size="mini" plain type="primary" @click="openDeviceInfoDialog(scope.row.code)">设备详情</el-button>
          <el-button size="mini" plain type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.pageNum" :page-sizes="[10, 20, 50]" :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="float: left;"></el-pagination>

    <device-info :deviceInfoDialog = "deviceInfoDialog" :deviceCode="deviceCode" @changeDeviceInfoDialog="changeDeviceInfoDialog"></device-info>

    <el-dialog title="" :visible.sync="imgDialogVisible" width="50%">
      <div style="height:600px;text-align: center;">
        <img :src="previewImg" alt="" style="height: 100%;">
      </div>
    </el-dialog>

    <el-dialog title="编辑信息" :visible.sync="formDialogVisible" width="50%">
      <div style="height:500px;text-align: center;">
        <el-form ref="form" :model="form" label-width="80px"  size="small" height="500">
          <el-form-item label="设备IMEI">
            <el-input v-model="form.code"></el-input>
          </el-form-item>
          <el-form-item label="监测项">
            <el-input v-model="form.type"></el-input>
          </el-form-item>
          <el-form-item label="线路">
            <el-input v-model="form.line"></el-input>
          </el-form-item>
          <el-form-item label="区间名称">
            <el-input v-model="form.segment"></el-input>
          </el-form-item>
          <el-form-item label="桩号、桥墩号">
            <el-input v-model="form.stakeNumber"></el-input>
          </el-form-item>
          <el-form-item label="安装位置">
            <el-input v-model="form.installPosition"></el-input>
          </el-form-item>
          <el-form-item label="经度">
            <el-input v-model="form.longitude"></el-input>
          </el-form-item>
          <el-form-item label="纬度">
            <el-input v-model="form.latitude"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
/* eslint-disable */
import DeviceInfo from '../index/admin/components/DeviceInfo.vue'
export default {
  components: {
    DeviceInfo,
  },
  data() {
    return {
      page: {
        pageNum: 1,
        pageSize: 50,
        total: 15
      },
      deviceList: [],
      search: '',
      deviceInfoDialog: false,
      imgDialogVisible: false,
      formDialogVisible: false,
      previewImg: '',
      deviceCode:'',
      form:{},
    };
  },
  methods: {
    changeDeviceInfoDialog(value) {
      this.deviceInfoDialog = value;
    },
    openDeviceInfoDialog (code) {
      this.deviceInfoDialog = true;
      this.deviceCode = code;
    },
    previewImage(value) {
      this.previewImg = value;
      this.imgDialogVisible = true;
    },
    resetForm() {
      this.form.code = '';
      this.form.installPosition = '';
      this.form.line = '';
      this.form.longitude = '';
      this.form.latitude = '';
      this.form.collectTime = '';
      this.form.segment = '';
      this.form.stakeNumber = '';
      this.form.type = '';
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.searchData();
    },
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.searchData();
    },
    handleAdd() {
      this.formDialogVisible = true;
      this.resetForm();
    },
    handleEdit(code) {
      this.$request({
        url: '/tmDevice/getDeviceDataByCode/' + code,
        method: 'post',
        data: {}
      }).then(res => {
        this.form = res.data.result_data;
        this.formDialogVisible = true;
      });
    },
    handleDelete(row) {
      this.$confirm('是否删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request({
          url: '/tmDevice/deleteDevice/'+ row.id + '/' + row.code,
          method: 'post',
        }).then(res => {
          if(res.data.result_code != 0) {
            this.$message({
              message: res.data.result_desc,
              type: 'danger'
            });
          }
          this.$message({
            message: '删除成功',
            type: 'success'
          });
          this.searchData();
        });
      }).catch(() => {
                  
      });
    },
    searchData() {
      let param = {};
      if (this.search != undefined) param.search = this.search;
      this.$request({
        url: '/tmDevice/getDevicePageData/' + this.page.pageNum + '/' + this.page.pageSize,
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
    save() {
      this.$request({
        url: '/tmDevice/saveDevice',
        method: 'post',
        data: this.form
      }).then(res => {
        this.$message({
          message: '保存成功',
          type: 'success'
        });
        this.searchData();
        this.formDialogVisible = false;
      });
    },
  },
  mounted: function () {
    this.searchData();
  }
};
</script>

<style scoped>
.qr-code-icon-default {
  /deep/ .el-icon-my-crash {
    background: url('../../../static/images/project/crash.png') no-repeat;
  }
}
.qr-code-icon-disabled {
  /deep/ .el-icon-my-crash {
    background: url('../../../static/images/project/crash.png') no-repeat;
  }
}
/deep/.el-icon-my-crash {
  font-size: 12px;
  background-size: cover;
}
/deep/ .el-icon-my-crash:before {
  content: '替';
  font-size: 12px;
  visibility: hidden;
}
</style>
