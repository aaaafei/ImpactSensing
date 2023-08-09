<template>
  <div>
    <div style="margin-top: 15px">
      <!-- <el-row>
        <el-radio-group v-model="wfTaskState" size="medium" @change="searchWfTasks">
          <el-radio-button label="待办"></el-radio-button>
          <el-radio-button label="已办"></el-radio-button>
          <el-radio-button label="全部"></el-radio-button>
        </el-radio-group>
      </el-row> -->
      <!-- <el-row>
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item label="发起人">
            <el-input v-model="detailForm.starter" placeholder="发起人"></el-input>
          </el-form-item>
          <el-form-item label="发起时间">
            <el-col :span="8">
              <el-form-item prop="date1">
                <el-date-picker type="date" placeholder="开始日期" v-model="startTime" style="width: 100%;"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col class="line" :span="1">-</el-col>
            <el-col :span="8">
              <el-form-item prop="date2">
                <el-time-picker type="date" placeholder="结束日期" v-model="endTime" style="width: 100%;"></el-time-picker>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchWfTasks">查询</el-button>
          </el-form-item>
        </el-form>
      </el-row> -->
    </div>
    <el-row :gutter="20">
      <el-table
        v-loading="loading1"
        :data="workflows"
        max-height="350"
        style="">
        <el-table-column type="selection" width="45"></el-table-column>
        <el-table-column type="index" :index="indexMethod" label="序号" width="50"></el-table-column>
        <el-table-column prop="name" label="名称" width="200"></el-table-column>
        <el-table-column prop="category" label="类型" width=""></el-table-column>
        <el-table-column prop="desc" label="描述" width=""></el-table-column>
        <!-- <el-table-column
          label="设置"
          fixed="right"
          width="300" prop="positionCode" >
          <template slot-scope="scope">
            <el-button @click="handle" type="text" size="small">处理人</el-button>
            <el-button @click="handle" type="text" size="small">处理组织</el-button>
            <el-button @click="handle" type="text" size="small">处理角色</el-button>
            <el-button @click="handle" type="text" size="small">处理岗位</el-button>
          </template>
        </el-table-column> -->
      </el-table>
      <div class="pageDiv">
        <el-pagination
          class="page"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total">
        </el-pagination>
      </div>
    </el-row>
    <el-dialog width="60%" :title="dialogTitle" :visible="dialogVisible" @close="closeDialog" @open="seachDatasources">
      <tlDetail v-model="detailForm" :operation="operation" :detailForm="detailForm" :detailVisible="saveBtnVisible" :datasources="datasources" @close="closeDialog" @refresh="refresh"></tlDetail>
    </el-dialog>
  </div>
</template>

<script>
  // import {mapGetters} from 'vuex';
  import tlDetail from './wfDetail';
  import request from '@/assets/utils/request';
  import userbox from './chooseUserbox';
  import postbox from './choosePostbox';
  export default {
    name: 'user',
    props: ['wfTaskStateVisible', 'chooseWfTaskState'],
    data: function () {
      return {
        moduleName: '工作任务',
        workflows: [],
        wfTaskState: '全部',
        loading1: false,
        detailForm: {
          id: '',
          valid: true,
          businessKey: '',
          instanceId: '',
          definitionKey: '',
          title: '',
          assignee: '',
          orgCode: null,
          roleCode: null,
          positionCode: null,
          message: null,
          taskKey: '',
          taskName: '',
          description: '',
          sendTime: '',
          completeTime: null,
          starter: '',
          sender: '',
          senderOrg: null
        },
        page: { // 分页的数据信息
          pageNum: 1,
          pageSize: 5,
          total: 0
        },
        // detailForm: {},
        selectedtaskId: '',
        datasources: [],
        dialogTitle: '查看' + this.moduleName,
        dialogVisible: false,
        saveBtnVisible: true,
        org: {
          orgdialogTitle: '选择组织',
          orgdialogVisible: false,
          treeLoading: false,
          treeData: null,
          selectedOrg: '',
          selectedOrgName: ''
        },
        role: {
          dialogTitle: '选择角色',
          roleData: null,
          selected: '',
          dialogVisible: false
        },
        position: {
          dialogTitle: '选择职位',
          roleData: null,
          selected: '',
          dialogVisible: false
        },
        user: {
          dialogTitle: '选择用户',
          roleData: null,
          selected: '',
          dialogVisible: false
        },
        operation: 'add',
        startTime: '',
        endTime: ''
      };
    },
    components: {
      tlDetail: tlDetail,
      userbox: userbox,
      postbox: postbox
    },
    mounted: function () {
      this.$nextTick(function () {
        this.searchWFs();
      });
    },
    // watch: {
    //   serialNumber: function (newValue, oldValue) {
    //     this.searchWfTasks();
    //   }
    // },
    methods: {
      setCurrentPageSize (val) {
        this.page.pageSize = val;
      },
      setCurrentPage (val) {
        this.page.pageNum = val;
      },
      handleSizeChange (val) { // 设置每页总共几条
        // alert(val)
        this.setCurrentPageSize(val);
        this.searchWfTasks();
      },
      handleCurrentChange (val) { // 跳到设置的当前页
        this.setCurrentPage(val);
        this.searchWfTasks();
      },
      indexMethod (index) {
        return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);
      },
      // searchWfTasks: function () {
      //   let index = this.wfTaskState;
      //   if (index === '待办') {
      //     this.page = this.page1;
      //     this.searchUndoWfTasks();
      //   } else if (index === '已办') {
      //     this.page = this.page2;
      //     this.searchDoneWfTasks();
      //   } else {
      //     this.page = this.page3;
      //     this.searchAllWfTasks();
      //   }
      // },
      searchWFs () {
        request({
          url: '/workflow/definitions'
        }).then(res => {
          this.workflows = res.data.data;
          // this.page.total = res.data.data.total;
        });
      },
      // searchDoneWfTasks () {
      //   request({
      //     url: '/workflow/doneList?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
      //   }).then(res => {
      //     this.undoWfTasks = res.data.data.list;
      //     this.page.total = res.data.data.total;
      //   });
      // },
      // searchAllWfTasks () {
      //   request({
      //     url: '/workflow/list?pageSize=' + this.page.pageSize + '&pageNum=' + this.page.pageNum
      //   }).then(res => {
      //     this.undoWfTasks = res.data.data.list;
      //     this.page.total = res.data.data.total;
      //   });
      // },
      handle (id) {
        request({url: '/workflow/toUser?taskId=' + id + '&username='
        }).then(res => {
          this.datasources = res.data.data;
        });
      },
      handleUser (id) {
        this.selectedtaskId = id;
        this.user.dialogVisible = true;
      },
      handleOrg (id) {
        this.selectedtaskId = id;
        this.org.orgdialogVisible = true;
      },
      handleNodeClick (obj, node) {
        this.org.selectedOrg = obj.id;
        this.org.selectedOrgName = obj.label;
      },
      seachOrg () { // 查询组织
        this.org.treeLoading = true;
        this.$request({
          url: '/org/tree'
        }).then(res => {
          this.org.treeLoading = false;
          this.org.treeData = res.data.data;
          // let Self = this;
          // setTimeout(function () {
          //   Self.$refs.tree.setCurrentKey(Self.treeNode.id);
          // }, 500);
        });
        this.operated = false; // 每次查询结束后把操作成功项归原
      },
      handleRole (id) {
        this.selectedtaskId = id;
        this.role.dialogVisible = true;
      },
      selectRole (val) {
        this.role.selected = val;
      },
      changeOrg () {
        if (this.org.selectedOrg === '') {
          this.$message('未选择组织！');
          return;
        }
        request({url: '/workflow/toOrg?taskId=' + this.selectedtaskId + '&orgCode=' + this.org.selectedOrg
        }).then(res => {
          // this.datasources = res.data.data;
        });
      },
      queryRole (flag) { // 按条件查询菜单列表
        // if (flag) {
        //   this.role.page.pageNum = 1;
        // }
        // let options = {
        //   params: {}
        // };
        // if (this.queryText !== '') {
        //   options.params.keywords = this.queryText;
        // }
        // this.loading = true; // 开始加载
        this.$request({
          url: '/role/list'
          // params: options.params
        }).then(res => {
          // this.operated = false; // 每次查询结束后把操作成功项归原
          // this.loading = false; // 查询结束后，停止加载
          // this.page.total = res.data.data.total;
          this.role.roleData = res.data.data;
        });
      },
      changeRole () {
        if (this.role.selected === '') {
          this.$message('未选择角色！');
          return;
        }
        request({url: '/workflow/toRole?taskId=' + this.selectedtaskId + '&roleCode=' + this.role.selected
        }).then(res => {
          // this.datasources = res.data.data;
        });
      },
      queryPosition () {

      },
      handlePost (id) {
        this.selectedtaskId = id;
        this.position.dialogVisible = true;
      },
      changePost () {
        this.position.selected = this.$refs.choosePost.selectedPost;
        if (this.position.selected === null) {
          this.$message('未选择职位！');
          return;
        }
        if (this.position.selected.length > 1) {
          this.$message('只能选择一个职位！');
          return;
        }
        request({url: '/workflow/toPosition?taskId=' + this.selectedtaskId + '&positionCode=' + this.position.selected
        }).then(res => {
          // this.datasources = res.data.data;
        });
      },
      changeUser () {
        this.user.selected = this.$refs.chooseUser.selectedUser;
        if (this.user.selected === null) {
          this.$message('未选择用户！');
          return;
        }
        if (this.user.selected.length > 1) {
          this.$message('只能选择一个用户！');
          return;
        }
        request({url: '/workflow/toUser?taskId=' + this.selectedtaskId + '&username=' + this.user.selected
        }).then(res => {
          // this.datasources = res.data.data;
        });
      },
      seachDatasources () {
        request({url: '/workflow/list'
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
        let detail = this.wfTasks[i];
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
      showDialog () {
        this.dialogVisible = true;
      },
      closeDialog (flag) {
        if (!flag || flag === '') {
          this.dialogVisible = false;
        } else if (flag === 'org') {
          this.org.selectedOrg = '';
          this.org.selectedOrgName = '';
          this.org.orgdialogVisible = false;
        } else if (flag === 'role') {
          this.role.selected = '';
          this.role.dialogVisible = false;
        } else if (flag === 'user') {
          this.user.selected = '';
          this.user.dialogVisible = false;
        } else if (flag === 'position') {
          this.position.selected = '';
          this.position.dialogVisible = false;
        }
      },
      refresh () {
        this.searchUndoWfTasks();
      },
      deleteDetail (id) {
        this.$confirm('是否确定删除？', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '删除',
          cancelButtonText: '放弃'
        })
        .then(() => {
          let url = '/code/workflow/delete/' + id;
          // url += '&url=' + this.form.url;
          request({url: url
          }).then(res => {
            this.$message({
              message: '删除成功！',
              type: 'success'
            });
            this.searchUndoWfTasks();
          });
        })
        .catch(action => {
        });
      }
    }
  };
</script>

<style scoped>
  .el-row {
    margin: 15px!important;
    margin-top: 10px;
  }
  .pageDiv {
    background-color: #fff;
  }

  .db-bottom {
    margin-top: 5px;
    margin-bottom:5px;
    line-height: 12px;
    float: right;
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
