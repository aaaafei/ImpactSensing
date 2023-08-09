<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.title" placeholder="设备名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
    
      <el-select v-model="listQuery.type" placeholder="左右线" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name+'('+item.key+')'" :value="item.key" />
      </el-select>
       
      <el-button  class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus" @click="handleCreate" v-if="adminUser">
        新增
      </el-button>
      <el-button  :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
        导出
      </el-button>
    </div>
    <div style="height:20px;"></div>
    <el-table
      :height="600"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      ref="table"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
      :header-cell-style="{background:'#409eff',color:'#fff'}"
    >
      <el-table-column type="selection" width="40" header-align="center"></el-table-column>
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" align="center"></el-table-column>
      <el-table-column prop="code" label="设备编号" width="150"></el-table-column>
      <el-table-column prop="deviceType" label="传感器类型" width="120"></el-table-column>
      <el-table-column prop="model" label="型号"></el-table-column>
      <el-table-column prop="type" label="监测项目" width="100"></el-table-column>
      <el-table-column prop="segment" label="监测区段"></el-table-column> 
      <el-table-column prop="stakeNumber" label="桩号"></el-table-column>
      <!-- <el-table-column prop="signalOut" label="信号输出"></el-table-column> -->
      <el-table-column prop="measureRange" label="量程"></el-table-column>
      <!-- <el-table-column prop="electricitySupply" label="供电"></el-table-column> -->
      <el-table-column prop="installPosition" label="安装位置" width="120"></el-table-column>
      <el-table-column label="运行状态" width="80" align="center">
            <template slot-scope="scope"> 
                <el-button type="success" circle  v-if="scope.row.status==1"></el-button>
                <el-button type="warning" circle v-if="scope.row.status==2"></el-button>
                <el-button type="danger" circle v-if="scope.row.status==3"></el-button>
                <el-button type="info" circle v-if="scope.row.status==4"></el-button>
            </template>
      </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
              <el-button size="mini" plain @click="handleEdit(scope.$index, scope.row)" v-if="!adminUser">查看</el-button>
              <el-button size="mini" plain @click="handleEdit(scope.$index, scope.row)" v-if="adminUser">编辑</el-button>
              <el-button size="mini" plain @click="handleDelete(scope.$index, scope.row)" v-if="adminUser">删除</el-button> 
          </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.pageNum"
      :page-sizes="[10, 20, 50]"
      :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total"
    ></el-pagination>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="deviceInfoForm" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备编号" prop="code">
              <el-input  v-model="deviceInfoForm.code"></el-input> 
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仪器型号" prop="model">
              <el-input v-model="deviceInfoForm.model" ></el-input>
            </el-form-item>
          </el-col>  
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="传感器类型">  
                <el-input v-model="deviceInfoForm.deviceType" ></el-input>
            </el-form-item>
          </el-col>  
          <el-col :span="12">
            <el-form-item label="监测类型">  
              <el-input v-model="deviceInfoForm.type" ></el-input>
            </el-form-item>
          </el-col>  
        </el-row>

        <el-row>
            <el-col :span="12">
              <el-form-item label="监测区段"> 
                  <el-input v-model="deviceInfoForm.segment" ></el-input>
              </el-form-item> 
            </el-col> 
            <el-col :span="12">
              <el-form-item label="桩号">   
                  <el-input v-model="deviceInfoForm.stakeNumber" ></el-input>
              </el-form-item> 
            </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="安装位置" prop="warning_data">
                <el-input  v-model="deviceInfoForm.installPosition"></el-input>
            </el-form-item>
          </el-col> 
          <el-col :span="12">
            <el-form-item label="运行状态" prop="status">
                <el-input v-model="deviceInfoForm.status"></el-input>
            </el-form-item>
          </el-col> 
        </el-row>
  
        <el-row>
          <el-col :span="12">
            <el-form-item label="量程" prop="measureRange">
                <el-input v-model="deviceInfoForm.measureRange"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12"> 
            <el-form-item label="供电" prop="electricitySupply">
                <el-input v-model="deviceInfoForm.electricitySupply"></el-input>
            </el-form-item>
          </el-col> 
        </el-row> 

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          关闭
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()" v-if="adminUser">
          保存
        </el-button>
      </div>
    </el-dialog>
  
  </div>
</template>

<script>
/* eslint-disable */    

const calendarTypeOptions = [
  { key: 'LK', display_name: '左线' },
  { key: 'RK', display_name: '右线' }
]

const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  // name: 'DeviceDataTable',  
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      page: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      deviceInfoForm: {},
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '修改',
        create: '新增'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      downloadLoading: false,
      adminUser: this.$checkRoleAdmin(),
    }
  },
  created() {
    this.getList()
  },
  methods: {
    indexMethod (index) {
      return (this.listQuery.page - 1) * this.listQuery.limit + (index + 1);  
    },
    getList() {
      this.$request({
        url:'tmDevice/getDevicePageData/'+this.page.pageNum+'/'+this.page.pageSize,
        method:'post',
        data: {}
      }).then(res => {
        let datalist = res.data.result_data.list;
        this.page.total = res.data.result_data.total;
        this.list = datalist;
      });
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getList();
      this.$refs.table.bodyWrapper.scrollTop =0;
    },
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getList();
      this.$refs.table.bodyWrapper.scrollTop =0;
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      // this.resetTemp();
      this.dialogStatus = 'create';
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(index,rowData){
      let _this = this;
      this.$confirm('确认删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request({
          url:'tmDevice/deleteDevice',
          method:'post',
          params: {id:rowData.id}
        }).then(res => {
          this.$notify({
            title: '',
            message: '删除成功',
            type: 'success',
            duration: 2000
          });
          _this.getList();
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        }); 
      }); 
    },
    handleEdit(index,rowData){
      this.resetTemp();
      this.dialogStatus = 'update';
      this.dialogFormVisible = true;
      this.$nextTick(() => {
         this.deviceInfoForm = rowData;
      })
    },
    createData() {
      let _this = this;
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$request({
            url:'tmDevice/saveDevice',
            method:'post',
            data: _this.deviceInfoForm
          }).then(res => {
            this.$notify({
              title: '',
              message: '保存成功',
              type: 'success',
              duration: 2000
            });
            this.dialogFormVisible = false;
           });
         
        }
        
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) 
      this.temp.timestamp = new Date(this.temp.timestamp);
      this.dialogStatus = 'update';
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      let _this = this;
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$request({
            url:'tmDevice/saveDevice',
            method:'post',
            data: _this.deviceInfoForm
          }).then(res => {
            this.$notify({
              title: '',
              message: '保存成功',
              type: 'success',
              duration: 2000
            });
            this.dialogFormVisible = false;
           });
         
        }
      })
    }, 
    handleFetchPv(pv) {
      fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      })
    },
    handleDownload() { 
       
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
