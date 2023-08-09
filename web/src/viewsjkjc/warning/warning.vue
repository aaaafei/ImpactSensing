<template>
  <div>
    <el-tabs v-model="activeName" type="border-card">
      <el-tab-pane label="监测数据预警" name="status">
        <div>
          <!-- <el-input size="small" v-model="listQuery.title" placeholder="设备编号" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
          <el-select size="small" v-model="listQuery.type" placeholder="监测项目" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in monitorTypeOptions" :key="item.key" :label="item.display_name+'('+item.key+')'" :value="item.key" />
          </el-select>
          <el-select size="small" v-model="listQuery.type" placeholder="监测区段" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in segmentOptions" :key="item.key" :label="item.display_name+'('+item.key+')'" :value="item.key" />
          </el-select>
          <el-select size="small" v-model="listQuery.type" placeholder="左右线" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in lineOptions" :key="item.key" :label="item.display_name+'('+item.key+')'" :value="item.key" />
          </el-select>
          <el-input size="small" v-model="listQuery.title" placeholder="桩号" style="width: 100px;" class="filter-item" @keyup.enter.native="handleFilter" /> -->
          <!-- <el-button size="small"  class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
            查询
          </el-button>  -->
          <el-checkbox v-model="latestBatchSwitcher" :indeterminate="false" @change="getList" style="margin-left:20px;margin-bottom:20px;">最近一批采集值</el-checkbox>
        </div>

        <el-table
          :height="600"
          :key="tableKey"
          v-loading="listLoading"
          :data="list"
          border
          fit
          highlight-current-row
          style="width: 100%;"
          @sort-change="sortChange"
          :header-cell-style="{background:'#409eff',color:'#fff'}"
        >
          <el-table-column label="序号" type="index" :index="indexMethod" width="50" align="center"></el-table-column>
          <el-table-column prop="deviceCode" label="设备编号" width="280" align="center">
            <template slot-scope='scope'>
              <div v-if="scope.row.monitorType=='纵向不均匀沉降'"><span style="cursor:pointer;color:#409eff;" @click="showDeviceData(scope.row)">{{scope.row.deviceCode}}~{{scope.row.device2Code}}</span></div>
              <div v-else><span style="cursor:pointer;color:#409eff;" @click="showDeviceData(scope.row)">{{scope.row.deviceCode}}</span></div>
            </template>  
          </el-table-column> 
          <el-table-column prop="monitorType" label="监测项目" align="center"></el-table-column> 
          <el-table-column prop="segment" label="监测区间段" align="center"></el-table-column> 
          <el-table-column prop="stakeNumber" label="桩号" align="center"></el-table-column>
          <el-table-column prop="installPosition" label="安装位置" align="center"></el-table-column>
          <el-table-column prop="warningLevel" label="安全度预警值" align="center"></el-table-column>   
          <el-table-column prop="collectTime" label="时间" width="140" align="center"></el-table-column>
          
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.pageNum"
          :page-sizes="[100, 200, 500]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
        ></el-pagination>
      </el-tab-pane>

      <el-tab-pane label="其他记录" name="device">
        <div>
          
          <el-button size="small" class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
            新增
          </el-button> 
          
        </div>

        <el-table
          :height="600"
          :key="tableKey"
          v-loading="listLoading"
          :data="alarmList"
          border
          fit
          highlight-current-row
          style="width: 100%;"
          @sort-change="sortChange"
          :header-cell-style="{background:'#409eff',color:'#fff'}"
        >
          <el-table-column label="序号" type="index" :index="indexMethod2" width="50" align="center"></el-table-column> 
          <el-table-column prop="title" label="类型" align="center"></el-table-column>
          <el-table-column prop="content" label="内容" align="center"></el-table-column>
          <el-table-column prop="reportTime" label="时间" align="center"></el-table-column> 
          <el-table-column prop="disposal" label="处置说明" align="center"></el-table-column>     
          </el-table-column>
          <el-table-column label="操作" width="220" align="center">
            <template slot-scope="scope">
                <el-button size="mini" plain type="success" @click="handleView(scope.$index, scope.row)">查看</el-button>
                <el-button size="mini" plain type="warning" @click="handleEdit(scope.$index, scope.row)" v-if="adminUser">编辑</el-button>
                <el-button size="mini" plain type="danger" @click="handleDelete(scope.$index, scope.row)" v-if="adminUser">删除</el-button> 
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange2"
          @current-change="handleCurrentChange2"
          :current-page="page2.pageNum"
          :page-sizes="[10, 20, 50]"
          :page-size="page2.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page2.total"
        ></el-pagination>
      </el-tab-pane> 
    </el-tabs>

    <!-- dynamic chart -->
    <el-dialog :visible.sync="deviceDataVisible" width="70%" title="监测数据" append-to-body>
      <device-dynamic-chart :deviceCode="selectDevicesStr" :unit="unitMap[monitorType]" :showtype="'onlydata'" v-if="selectDevicesStr"></device-dynamic-chart>
    </el-dialog>

    <el-dialog :title="'其他记录'" :visible.sync="alarmFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="alarmForm" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="类型" prop="title">
              <el-input  v-model="alarmForm.title"></el-input> 
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="内容" prop="content">
              <el-input  v-model="alarmForm.content"></el-input> 
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="时间" prop="reportTime">
              <el-date-picker
                v-model="alarmForm.reportTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="处置说明" prop="disposal">
              <el-input  v-model="alarmForm.disposal"></el-input> 
            </el-form-item>
          </el-col>
        </el-row>
        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="alarmFormVisible = false">
          关闭
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()" v-if="adminUser && dialogStatus!='view'">
          保存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */    
import deviceDynamicChart from '../monitor/deviceDynamicChart';
const calendarTypeOptions = [
  { key: 'LK', display_name: '左线' },
  { key: 'RK', display_name: '右线' }
];

const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {});

export default {
  name: 'DeviceDataTable',  
  components: {
    deviceDynamicChart,
  },
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
      activeName:'status',
      page: {
        pageNum: 1,
        pageSize: 100,
        total: 0
      },
      page2: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      tableKey: 0,
      list: [],
      alarmList: [],
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
      monitorTypeOptions: [
        { key: '-', display_name: '断面环缝伸缩量' },
        { key: '-', display_name: '断面纵缝伸缩量' },
        { key: '-', display_name: '结构应力-隧道纵向' },
        { key: '-', display_name: '结构应力-隧道环向' },
        { key: '-', display_name: '管片错台' },
        { key: '-', display_name: '纵向不均匀沉降' },
      ],
      lineOptions: [
        { key: 'LK', display_name: '左线' },
        { key: 'RK', display_name: '右线' }
      ],
      segmentOptions: [
        { key: 'PJ', display_name: '浦口竖井' },
        { key: 'PD', display_name: '浦口大堤' },
        { key: 'JZ', display_name: '江中' },
        { key: 'FX', display_name: '覆土厚度最小' },
        { key: 'MD', display_name: '梅子洲大堤' },
        { key: 'MJ', display_name: '梅子洲竖井' }
      ],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      form: {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        type: '',
        status: 'published'
      },
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
      unitMap:{
        '断面环缝伸缩量':'mm',
        '断面间环缝伸缩量':'mm',
        '断面纵缝伸缩量':'mm',
        '管片错台':'mm',
        '管片纵缝错台':'mm',
        '管片环缝错台':'mm',
        '纵向不均匀沉降':'mm',
        '断面结构应力':'με',
        '管片倾斜与偏转':'º',
      },
      deviceDataVisible:false,
      selectDevicesStr:'',
      monitorType:'',
      latestBatchSwitcher:true,
      adminUser: this.$checkRoleAdmin(),
      alarmFormVisible: false,
      alarmForm:{},
    }
  },
  created() {
    this.getList();
    this.getAlarmList();
  },
  methods: {
    indexMethod (index) {
      return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);  
    },
    indexMethod2 (index) {
      return (this.page2.pageNum - 1) * this.page2.pageSize + (index + 1);  
    },
    getList() {
      this.$request({
        url:'tmWarningRecords/getPageList/' + this.page.pageNum + '/' + this.page.pageSize,
        method:'post',
        data: {
          'latestBatch':this.latestBatchSwitcher
        }
      }).then(res => {
        let dataList = res.data.result_data.list;
        this.page.total = res.data.result_data.total;
        this.list = dataList;
      });
    },
    getAlarmList() {
      this.$request({
        url:'tmOtherAlarmRecords/getPageDataList/'+ this.page2.pageNum + '/' + this.page2.pageSize,
        method:'post',
        data: {}
      }).then(res => {
        let dataList = res.data.result_data.list;
        this.page2.total = res.data.result_data.total;
        this.alarmList = dataList;
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
    handleSizeChange2(val) {
      this.page2.pageSize = val;
      this.getAlarmList(); 
    },
    handleCurrentChange2(val) {
      this.page2.pageNum = val;
      this.getAlarmList(); 
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
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
      this.resetTemp();
      this.dialogStatus = 'create';
      this.alarmFormVisible = true;
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(index,rowData){
      this.$confirm('确认删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$request({
            url:'tmOtherAlarmRecords/deleteItem',
            method:'post',
            params: {id:rowData.id}
          }).then(res => {
            this.$notify({
              title: '',
              message: '删除成功',
              type: 'success',
              duration: 2000
            });
            this.getAlarmList();
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
      this.alarmFormVisible = true;
      this.$nextTick(() => {
         this.alarmForm = rowData;
      })
    },
    handleView(index,rowData){
      this.resetTemp();
      this.dialogStatus = 'view';
      this.alarmFormVisible = true;
      this.$nextTick(() => {
         this.alarmForm = rowData;
      })
    },
    createData() {
      console.log(this.alarmForm);
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$request({
            url:'tmOtherAlarmRecords/saveItem',
            method:'post',
            data: this.alarmForm
          }).then(res => {
            this.$notify({
              title: '',
              message: '保存成功',
              type: 'success',
              duration: 2000
            });
            this.alarmFormVisible = false;
            this.getAlarmList();
          });
          
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$request({
              url:'tmOtherAlarmRecords/saveItem',
              method:'post',
              data: this.alarmForm
            }).then(res => {
              this.$notify({
                title: '',
                message: '保存成功',
                type: 'success',
                duration: 2000
              });
              this.alarmFormVisible = false;
              this.getAlarmList();
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
    },
    showDeviceData(row){
      this.selectDevicesStr = null;
      this.monitorType = row.monitorType;
      if(row.device2Code){
        let arr=[row.deviceCode, row.device2Code];
        this.selectDevicesStr = arr.join("、");
      }else{
        this.selectDevicesStr = row.deviceCode;
      }
      this.deviceDataVisible = true;
    }
  }
}
</script>
