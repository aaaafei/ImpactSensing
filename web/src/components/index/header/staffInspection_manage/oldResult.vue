<template>
  <div id="oldResult1">
    <div id="body">
      <div id="search" class="search">
        <el-form ref="form" :model="form" :inline="true" label-width="110px">
          <div class="search-input">
            <el-form-item label="巡检时间：" for="startTime">
              <el-date-picker
              id="startTime"
              type="datetime"
              class="input"
              size="mini"
              v-model="form.startTime_start"
              format="yyyy-MM-dd HH:mm"
              value-format="yyyyMMddHHmm"
              placeholder="请输入巡检时间">
              </el-date-picker>~
              <el-date-picker
                id="startTime"
                type="datetime"
                class="input"
                size="mini"
                v-model="form.startTime_end"
                format="yyyy-MM-dd HH:mm"
                value-format="yyyyMMddHHmm"
                placeholder="请输入巡检时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="searchData">查询</el-button>
            </el-form-item>
          </div>
          <div class="search-button">
            <el-button type="success" class="right-button" size="mini" @click="exportResult">导出</el-button>
          </div>
        </el-form>
      </div>
      <div id="table">
        <el-table ref="multipleTable" :data="equipList" tooltip-effect="dark" v-loading="loading" style="max-height: 66vh;overflow-y: auto;">
            <el-table-column
                align="center"
                type="index"
                width="60"
                :index="indexMethod"
                label="序号">
            </el-table-column>
            <el-table-column
                align="center"
                prop="deviceName"
                label="设备名称">
            </el-table-column>
            <el-table-column
                align="center"
                prop="userName"
                label="巡检人">
            </el-table-column>
            <el-table-column
              align="center"
              prop="startTime"
              width="120"
              label="巡检时间">
            </el-table-column>
            <el-table-column
              align="center"
              prop="result"
              label="巡检结果">
            </el-table-column>
            <el-table-column
              align="center"
              prop="statusName"
              label="结果状态">
            </el-table-column>
            <el-table-column
              align="center"
              prop="img"
              label="图片">
              <template slot-scope="scope">
                <el-button
                  @click="queryImg(scope.$index, scope.row)" type="text">详情
                </el-button>
              </template>
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
      <showImg :showImgVisible= "showImgVisible" :pictureAttach="pictureAttach" v-on:headCallBack= "callBack">
      </showImg>
      </div>
    </div>
  </div>
</template>

<script>
import showImg from './showImg.vue';
export default {
  name: 'oldResult1',
  props: ['workCode'],
  data: function () {
    return {
      loading: false,
      showImgVisible: false,
      pageSize: 10,
      pageNum: 1,
      total: 0,
      form: {
        startTime_start: '', // 巡检时间
        startTime_end: ''// 巡检时间
      },
      equipList: [],
      pictureAttach: []
    };
  },
  components: {
    showImg: showImg
  },
  methods: {
    indexMethod (index) {
      return (this.pageNum - 1) * this.pageSize + index + 1;
    },
    handleSizeChange (val) { // 修改每页条数
      console.log(`每页 ${val} 条`);
      this.pageSize = val;
      this.pageNum = 1;
      this.search();
    },
    handleCurrentChange (val) { // 跳到下一页
      console.log(`当前页: ${val}`);
      this.pageNum = val;
      this.search();
    },
    searchData () {
      this.pageNum = 1;
      this.search();
    },
    search () { // 搜索
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        },
        params: {
          startTime_start: this.form.startTime_start,
          startTime_end: this.form.startTime_end,
          // workCode: this.$route.params.workCode
          workCode: this.workCode
        }
      };
      this.loading = true;
      this.$http.get(this.$api_prefix + '/inspection/listHistory?pageNum=' + this.pageNum + '&pageSize=' + this.pageSize, options).then((res) => { // 调用查询巡检计划列表接口
        if (res.body.code === 401) {
          this.$router.push('/login');
          return false;
        }
        let success = res.body.success;
        if (success === true) {
          this.equipList = res.body.data.list;
          this.total = res.body.data.total;
          this.loading = false; // 查询结束后，停止加载
        } else {
          this.loading = false; // 查询结束后，停止加载
          this.$message.error(res.body.message);
        }
      }, (res) => {
        this.$message.error('服务器暂时无法连接，请稍后再试！');
        this.loading = false; // 查询结束后，停止加载
      });
      // this.loading = false; // 查询结束后，停止加载
    },
    exportResult () {
      this.$confirm('确认导出巡检结果历史结果?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        location.href = this.$api_prefix + '/inspection/exportHistory?Authorization=' + this.$store.getters.getToken + '&startTime_start=' + this.form.startTime_start + '&startTime_end=' + this.form.startTime_end + '&workCode=' + this.workCode;
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消导出'
        });
      });
    },
    queryImg (index, row) {
      this.showImgVisible = true;
      this.pictureAttach = row.attach;
    },
    callBack (msg) {
      this.showImgVisible = msg;
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
  },
  watch: {
    workCode: function (val) { // 此处不要使用箭头函数    监听是否增删改的操作成功，成功就执行把刷新列表
      console.log('历史结果:' + val);
      this.form.startTime_start = '';
      this.form.startTime_end = '';
      if (val) {
        this.search();
      }
    }
  }
};
</script>

<style>
#oldResult1 .search{
  overflow: auto;
}
#oldResult1 .search-input{
  margin-top: 20px;
  background-color: rgb(255, 255, 255);
  padding-top: 10px;
  padding-bottom: 10px;
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
}
#oldResult1 .search-button{
  float: right;
  margin-bottom: 10px;
  margin-top: 10px
}
#oldResult1 .el-form {
  width: 100%;
  margin: 0 auto;
}
#oldResult1 .input{
  width: 150px;
}
#oldResult1 .search .el-date-editor .el-input{
   width: 150px;
}
#oldResult1 .el-form-item__label{
  font-size: 10px;
}
#oldResult1 .search .el-form-item{
  margin-bottom: 0px;
}
#oldResult1{
  background-color: #eef7ff;
  height: 100%;
}
#oldResult1 #body{
  margin-left: 20px;
  padding-bottom: 20px;
  margin-right: 20px;
}
#oldResult1 .left-button{
  width: 70px;
}
#oldResult1 .right-button{
  width: 70px;
}
#oldResult1 #table{
  border-bottom: 2px solid #d4d4d4;
  border-right: 2px solid #d4d4d4;
  border-radius: 5px;
}
#oldResult1 .pageDiv{
  height: 35px;
  background-color: rgb(255, 255, 255);
}
#oldResult1 .page{
  float: right;
}
#oldResult1 .el-pager li.active {
  color: rgb(255, 255, 255);
  background-color: #a7bbf5;
  border: 1px solid;
  width: 27px;
  border-radius: 6px;
}
#oldResult1 .el-pager li.number {
  width: 27px;
  border: 1px solid #d4d4d4;
  border-radius: 6px;
}
#oldResult1 .el-pager li{
  width: 27px;
  min-width: 0px;
  margin-left: 10px;
  font-family: 宋体;
}
#oldResult1 .el-pagination button{
  border: 1px solid #d4d4d4;
  border-radius: 6px;
  width: 27px;
  min-width: 0px;
}
#oldResult1 .el-pagination .btn-next {
  margin-left: 10px;
  padding-left: 6px;
}
</style>
