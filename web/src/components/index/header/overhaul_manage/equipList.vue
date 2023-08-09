<template>
  <el-dialog title="选择设备" class="equipList" :visible="equipVisible" append-to-body @open="open()" @close="close()">
    <el-dialog width="30%" title="设备类型" :visible.sync="deviceTypeManageVisible" append-to-body :before-close="handleClose" id="deviceTypeDialogId1">
      <el-tree :data="deviceTypeList"  @node-click="handleNodeClick"  highlight-current default-expand-all v-loading="loadingDeviceTypeTree"></el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="addDeviceTypeSubmit">确定</el-button>
      </div>
    </el-dialog>
    <el-dialog width="30%" title="所属分区" :visible="CompartmentManageVisible" append-to-body :before-close="handleComptClose" id="CompartmentDialogId1">
      <el-tree :data="comptList" @node-click="handleComptNodeClick"  highlight-current default-expand-all v-loading="loadingCompartmentTree"></el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleComptClose">取 消</el-button>
        <el-button type="primary" @click="addCompartmentSubmit">确定</el-button>
      </div>
    </el-dialog>
    <el-row id="equipList1">
      <el-row class="body">
        <el-col :span="24">
          <div id="body">
            <div class="search">
              <el-form ref="form" :model="form" :inline="true" label-width="100px">
                <div class="search-input">
                  <el-form-item label="设备类型:" prop="deviceType" >
                    <el-tooltip :content="form.deviceTypeName" placement="top" v-if="form.deviceTypeName!=''">
                      <el-input v-model="form.deviceTypeName" placeholder="请选择设备类型" size="small"  readonly="readonly" @click.native="chooseDeviceType">
                      </el-input>
                      <el-input v-model="form.deviceType" v-if="type === 'show'">
                      </el-input>
                    </el-tooltip>
                    <template v-else="form.deviceTypeName!=''">
                      <el-input v-model="form.deviceTypeName" placeholder="请选择设备类型" size="small"  readonly="readonly" @click.native="chooseDeviceType" >
                      </el-input>
                      <el-input v-model="form.deviceType" v-if="type === 'show'">
                      </el-input>
                    </template>
                    <span v-if="form.deviceTypeName !== ''" style="position:absolute;margin-top:-41px;margin-left: 85%;" v-on:mouseover="toggleShow()" v-on:mouseout="toggleHidden()">
                      <i @click="clearDeviceTypeName"  class = "el-icon-circle-close" id = "clearDeviceTypeId2"></i>
                    </span>
                  </el-form-item>
                  <el-form-item label="所属分区:" prop="compt">
                    <el-input v-model="form.comptName" placeholder="请选择所属分区" size="small" @click.native="chooseCompartment" v-if="form.sectionName === ''">
                    </el-input>
                    <el-input v-model="form.sectionName" placeholder="请选择所属分区" size="small" @click.native="chooseCompartment" v-if="form.sectionName !== ''">
                    </el-input>
                    <span v-if="form.comptName !== ''|| form.sectionName !== ''" style="position:absolute;margin-top:-41px;margin-left: 85%;" v-on:mouseover="toggleComptShow()" v-on:mouseout="toggleComptHidden()">
                      <i @click="clearComptName"  class = "el-icon-circle-close" id = "clearCompartmentId"></i>
                    </span>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" size="mini" class="left-button" @click="searchEquipment">查询</el-button>
                  </el-form-item>
                </div>
              </el-form>
            </div>
            <div id="table">
              <el-table ref="multipleTable" :data="equipsList" tooltip-effect="dark" height="300px"
                highlight-current-row @current-change="handleChange" v-loading="loading">
               <el-table-column
                  align="center"
                  type="index"
                  label="序号"
                  width="60">
                </el-table-column>
                <el-table-column
                  align="center"
                  prop="name"
                  show-overflow-tooltip
                  label="设备名称">
                </el-table-column>
                <el-table-column
                  align="center"
                  prop="code"
                  label="设备编号">
                </el-table-column>
                <el-table-column
                  align="center"
                  prop="deviceTypeName"
                  label="设备类型">
                </el-table-column>
                <el-table-column
                  align="center"
                  prop="comptName"
                  label="巡检分区">
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
            </div>
          </div>
        </el-col>
      </el-row>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button  @click="close()">取 消</el-button>
      <el-button  type="primary" @click="save()">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'equipList1',
  props: ['equipVisible'],
  data: function () {
    return {
      form: {
        deviceType: '',
        deviceTypeName: '',
        compt: '',
        comptName: '',
        sectionName: '',
        section: ''
      },
      compt: [],
      loading: false,
      equipVisible2: this.equipVisible,
      pageSize: 10,
      pageNum: 1,
      total: 0,
      treeData: [], // 设备类型树
      sectionList: [],  // 所在路段
      comptList: [], // 所属分区
      deviceTypeList: [], // 设备类型
      equipsList: [],
      typeList: [], // 设备类型多选值
      multipleSelection: '',
      tableLoad: false,
      loadingDeviceTypeTree: false,
      loadingCompartmentTree: false,
      deviceTypeManageVisible: false,
      CompartmentManageVisible: false,
      type: 'hide',
      currentDeviceType: '',
      currentDeviceTypeName: '',
      currentCompt: '',
      currentComptName: ''
    };
  },
  methods: {
    clearDeviceTypeName () {
      this.form.deviceTypeName = '';
      this.form.deviceType = '';
    },
    toggleShow: function () {
      document.getElementById('clearDeviceTypeId2').style.color = '#909399';
    },
    toggleHidden: function () {
      document.getElementById('clearDeviceTypeId2').style.color = '#c0c4cc00';
    },
    chooseDeviceType () {   // 查询设备类型树
      this.deviceTypeManageVisible = true;
      this.loadingDeviceTypeTree = true;  // 开始查询设备类型树，开始Loading 加载
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        }
      };
      this.$http.get(this.$api_prefix + '/dict/tree?pcode=dev_type', options).then((res) => {
        if (res.body.code === 401) {
          this.$router.push('/login');
        }
        let success = res.body.success;
        if (success === true) {
          this.deviceTypeList = res.body.data;
          this.loadingDeviceTypeTree = false;// 查询设备类型树成功，关闭Loading 加载
        } else {
          this.loadingDeviceTypeTree = false;// 查询设备类型树失败，关闭Loading 加载
          this.$message.error('请求错误');
        }
      }, (res) => {
        this.$message.error('服务器暂时无法连接，请稍后再试！');
      });
    },
    handleClose () {
      this.deviceTypeManageVisible = false;
    },
    handleNodeClick (data) {
      this.currentDeviceType = data.data.code;
      this.currentDeviceTypeName = data.label;
    },
    addDeviceTypeSubmit () {
      this.form.deviceTypeName = this.currentDeviceTypeName;
      this.form.deviceType = this.currentDeviceType;
      this.deviceTypeManageVisible = false;
    },
    clearComptName () {
      this.form.comptName = '';
      this.form.compt = '';
      this.form.sectionName = '';
      this.form.section = '';
    },
    toggleComptShow: function () {
      document.getElementById('clearCompartmentId').style.color = '#909399';
    },
    toggleComptHidden: function () {
      document.getElementById('clearCompartmentId').style.color = '#c0c4cc00';
    },
    chooseCompartment () {   // 查询分区树
      this.CompartmentManageVisible = true;
      this.loadingCompartmentTree = true;  // 开始查询分区树，开始Loading 加载
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        }
      };
      this.$http.get(this.$api_prefix + '/compartment/tree', options).then((res) => {
        if (res.body.code === 401) {
          this.$router.push('/login');
        }
        let success = res.body.success;
        if (success === true) {
          this.comptList = res.body.data;
          this.loadingCompartmentTree = false;// 查询分区树成功，关闭Loading 加载
        } else {
          this.loadingCompartmentTree = false;// 查询分区树失败，关闭Loading 加载
          this.$message.error(res.body.message);
        }
      }, (res) => {
        this.$message.error('服务器暂时无法连接，请稍后再试！');
      });
    },
    handleComptClose () {
      this.CompartmentManageVisible = false;
    },
    handleComptNodeClick (data) {
      if (data.data.section !== undefined) {
        this.currentCompt = data.data.id;
        this.currentComptName = data.label;
        this.currentSection = '';
        this.currentSectionName = '';
      } else {
        this.currentSection = data.id;
        this.currentSectionName = data.label;
        this.currentCompt = '';
        this.currentComptName = '';
      }
    },
    addCompartmentSubmit () {
      this.form.comptName = this.currentComptName;
      this.form.compt = this.currentCompt;
      this.form.sectionName = this.currentSectionName;
      this.form.section = this.currentSection;
      this.CompartmentManageVisible = false;
    },
    check (node, data) {
    },
    open () {
      this.searchEquipment();  // 查询设备资产
    },
    close () {
      this.equipVisible2 = false;
      this.$emit('headCallBack', this.equipVisible2); // 给父组件传值，隐藏弹出框
      this.comptList = [];
      this.sectionList = [];
    },
    handleSizeChange (val) {
      this.pageSize = val;
      this.searchEquipment();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange (val) {
      this.pageNum = val;
      this.searchEquipment();
    },
    handleSelectionChange (val) {
      this.multipleSelection = val;
    },
    searchEquipment () {
      let self = this;
      this.loading = true;  // 查询结开始，开始加载
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        },
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          deviceType: this.form.deviceType,
          compt: this.form.compt,
          section: this.form.section
        }
      };
      this.$http.get(this.$api_prefix + '/device/list', options).then((res) => {
        if (res.body.code === 401) {
          this.$router.push('/login');
        }
        let success = res.body.success;
        if (success === true) {
          self.equipsList = res.body.data.list;
          if (res.body.data.pageNum !== 0) {
            this.pageNum = res.body.data.pageNum;
          }
          this.pageSize = res.body.data.pageSize;
          this.total = res.body.data.total;
          this.loading = false; // 查询结束后，停止加载
        } else {
          this.loading = false; // 查询结束后，停止加载
          self.$message.error(res.body.message);
        }
      }, (res) => {
        this.loading = false; // 查询结束后，停止加载
        this.$message.error('服务器暂时无法连接，请稍后再试！');
      });
    },
    handleChange (val) {
      this.multipleSelection = val;
    },
    save () {
      this.equipVisible2 = false;
      this.$emit('headCallBack', this.equipVisible2, null, this.multipleSelection); // 给父组件传值，隐藏弹出框
    }
  },
  mounted: function () {
    this.$nextTick(function () {
      let token = this.$store.getters.getToken;
      if (token === '') {
        console.log('token没有，重新赋值');
        this.$store.dispatch('refresh');
      }
    });
  }
};
</script>

<style type="stylus">
#equipList1 .body{
  width: 100%;
  height: 100%;
  background-color: #eef7ff;
}
#equipList1 .el-select .el-input{
width: 150px;
}
#equipList1 .search-input{
  background-color: rgb(255, 255, 255);
  padding-top: 10px;
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
}
#equipList1 #table{
  margin-top: 20px;
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
}
#equipList1 #body{
  margin-left: 20px;
  padding-bottom: 20px;
}
#equipList1 .pageDiv{
  height: 35px;
  background-color: rgb(255, 255, 255);
}
#equipList1 .page{
  float: right;
}
#equipList1 .el-pager li.active {
  color: rgb(255, 255, 255);
  background-color: #a7bbf5;
  border: 1px solid;
  width: 27px;
  border-radius: 6px;
}
#equipList1 .el-pager li.number {
  width: 27px;
  border: 1px solid #d4d4d4;
  border-radius: 6px;
}
#equipList1 .el-pager li{
  width: 27px;
  min-width: 0px;
  margin-left: 10px;
  font-family: 宋体;
}
#equipList1 .el-pagination button{
  border: 1px solid #d4d4d4;
  border-radius: 6px;
  width: 27px;
  min-width: 0px;
}
#equipList1 .el-pagination .btn-next {
  margin-left: 10px;
  padding-left: 6px;
}
#equipList1 .select-tree{
  width: 160px;
  background-color: #fff;
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
  padding: 3px;
  height: 424px;
}
#equipList1 .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
  background-color: #4baff2;
  color: #fff;
}
.equipList .el-dialog__body{
  background-color: #eef7ff;
}
/*.equipList1 .el-dialog {
  overflow: auto;
  height: 600px;
  width: 800px;
}*/
.equipList1 .el-tree-node__label {
  font-size: 10px;
}
#deviceTypeDialogId1 .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
  background-color: #4baff2;
  color: #fff;
}
#deviceTypeDialogId1 .el-dialog__body{
  height: 300px;
  overflow: auto;
}
#CompartmentDialogId1 .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
  background-color: #4baff2;
  color: #fff;
}
.equipList .el-input--small{
  width: 141px;
}
</style>
