<template>
  <div id="staffInspection1">
    <el-row >
      <el-col :span="24">
        <div id="body">
          <div id="table">
            <el-table ref="multipleTable" :data="inspectionList" tooltip-effect="dark" height="300px" v-loading="loading">
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
                  label="任务名称">
              </el-table-column>
              <el-table-column
                  align="center"
                  prop="comptNames"
                  show-overflow-tooltip
                  label="巡检分区">
              </el-table-column>
              <el-table-column
                align="center"
                prop="startTime"
                show-overflow-tooltip
                label="巡检时间">
              </el-table-column>
              <el-table-column
                align="center"
                prop="hasError"
                show-overflow-tooltip
                label="有无异常">
              </el-table-column>
              <el-table-column
                align="center"
                prop="statusName"
                show-overflow-tooltip
                label="任务状态">
              </el-table-column>
              <el-table-column
                align="center"
                label="操作"
                fixed="right"
                width="250">
                <template slot-scope="scope">
                  <el-button
                    @click="queryNewResult(scope.$index, scope.row)" type="text">查看最新结果
                  </el-button>
                  <el-button
                    @click="queryAllResult(scope.$index, scope.row)" type="text">查看历史结果
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pageDiv">
              <el-pagination class="page" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="[10, 20, 30, 40]" :current-page.sync="pageNum":page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
              </el-pagination>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-dialog  width="85%" title="最新结果" :visible="newResultVisible" @close="close1()" >
      <newResult :workId="workId" v-on:headCallBack1="callBackNew"></newResult>
    </el-dialog>
    <el-dialog  width="85%" title="历史结果" :visible="oldResultVisible" @close="close2()" >
      <oldResult :workCode="workCode" v-on:headCallBack1="callBackOld"></oldResult>
    </el-dialog>
  </div>
</template>
<script>
// import orgTree from  '../../../maintenance_manage/routeInspection_manage/orgTree.vue';
import newResult from './newResult.vue';
import oldResult from './oldResult.vue';
// 地点的数据 获取接口数据
export default {
  name: 'staffInspection1',
  props: ['agencyFlag'],
  data: function () {
    return {
      newResultVisible: false,
      oldResultVisible: false,
      inspectionList: [], // 巡检计划列表数据
      loading: false,
      pageSize: 10,
      pageNum: 1,
      total: 1,
      workId: '',
      workCode: ''
    };
  },
  components: {
    // orgTree: orgTree,
    newResult: newResult,
    oldResult: oldResult
  },
  methods: {
    indexMethod (index) {
      return (this.pageNum - 1) * this.pageSize + index + 1;
    },
    handleSizeChange (val) { // 修改每页条数
      this.pageSize = val;
      this.search();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange (val) { // 跳到下一页
      this.pageNum = val;
      this.search();
      console.log(`当前页: ${val}`);
    },
    queryNewResult (index, row) {
      // this.$router.push({ name: 'newResult', params: {workId: row.id} });    // 选中的行的数据传到跳转路由界面
      this.workId = row.id;
      // console.log(this.workId)
      this.newResultVisible = true;
    },
    close1 () {
      this.newResultVisible = false;
      this.workId = '';
    },
    callBackNew () {
      this.workId = '';
    },
    queryAllResult (index, row) {
      // this.$router.push({ name: 'oldResult', params: {workCode: row.code} });    // 选中的行的数据传到跳转路由界面
      this.workCode = row.code;
      this.oldResultVisible = true;
    },
    close2 () {
      this.oldResultVisible = false;
      this.workCode = '';
    },
    callBackOld () {
      this.workCode = '';
    },
    search () {
      this.loading = true;
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        },
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
      };
      this.$http.get(this.$api_prefix + '/inspection/todo', options).then((res) => {
        if (res.body.code === 401) {
          this.$router.push('/login');
          return false;
        }
        let success = res.body.success;
        if (success === true) {
          this.inspectionList = res.body.data.list;
          if (res.body.data.pageNum !== 0) {
            this.pageNum = res.body.data.pageNum;
          }
          this.pageSize = res.body.data.pageSize;
          this.total = res.body.data.total;
          this.loading = false;
        } else {
          this.loading = false;
          this.$message.error(res.body.message);
        }
      }, (res) => {
        this.loading = false;
        this.$message.error('服务器暂时无法连接，请稍后再试！');
      });
    }
  },
  mounted: function () {
    this.$nextTick(function () {
      let token = this.$store.getters.getToken;
      if (token === '') {
        console.log('token没有，重新赋值');
        this.$store.dispatch('refresh');
      }
      this.search();
    });
  }
  // watch: {
  //   agencyFlag: function (val) { // 此处不要使用箭头函数    监听是否增删改的操作成功，成功就执行把刷新列表
  //     console.log('代表2:' + val);
  //     this.inspectionList = [];
  //     if (val) {
  //       this.search();
  //     }
  //   }
  // }
};
</script>

<style>
#staffInspection1 .search{
  overflow: auto;
}
#staffInspection1 .search-input{
  margin-top: 20px;
  background-color: rgb(255, 255, 255);
  padding-top: 10px;
  padding-bottom: 10px;
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
}
#staffInspection1 .search-button{
  float: right;
  margin-bottom: 10px;
  margin-top: 10px
}
#staffInspection1 .input{
  width: 150px;
}
#staffInspection1 .search .el-date-editor .el-input{
   width: 150px;
}
#staffInspection1 .el-form-item__label{
  font-size: 10px;
  width: 81px;
}
#staffInspection1 .search .el-form-item{
  margin-bottom: 0px;
  margin-right: 0px;
}
#staffInspection1 .search .el-form-item button{
  margin-left: 30px;
}
#staffInspection1 #body{
  margin-left: 20px;
  padding-bottom: 20px;
  margin-right: 20px;
}
#staffInspection1 .left-button{
  width: 70px;
}
#staffInspection1 .right-button{
  width: 70px;
}
#staffInspection1 #table{
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
}
#staffInspection1 .pageDiv{
  height: 35px;
  background-color: rgb(255, 255, 255);
}
#staffInspection1 .page{
  float: right;
}
#staffInspection1 .el-pager li.active {
  color: rgb(255, 255, 255);
  background-color: #a7bbf5;
  border: 1px solid;
  width: 27px;
  border-radius: 6px;
}
#staffInspection1 .el-pager li.number {
  width: 27px;
  border: 1px solid #d4d4d4;
  border-radius: 6px;
}
#staffInspection1 .el-pager li{
  width: 27px;
  min-width: 0px;
  margin-left: 10px;
  font-family: 宋体;
}
#staffInspection1 .el-pagination button{
  border: 1px solid #d4d4d4;
  border-radius: 6px;
  width: 27px;
  min-width: 0px;
}
#staffInspection1 .el-pagination .btn-next {
  margin-left: 10px;
  padding-left: 6px;
}
#staffInspection1 .select-tree{
  margin-top: 20px;
  background-color: #fff;
  margin-right: 20px;
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
  margin-left: 20px;
  padding: 3px;
  height: 500px;
}
.el-icon-circle-close{
  cursor:pointer;
  font-size:14px;
  color:#c0c4cc00;
}
#staffInspection1 .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
  background-color: #4baff2;
  color: #fff;
}
#CompartmentDialogId1 .el-dialog__body{
    height: 300px;
    overflow: auto;
}
</style>
