<template>
    <div>
        <el-row>
            <el-col :span="24"> 
                <el-select v-model="taskStatus" placeholder="完成状态" clearable>
                    <el-option label="已完成" :value="1"></el-option>
                    <el-option label="未完成" :value="0"></el-option> 
                </el-select>
            </el-col>
        </el-row>   
        <el-table :data="tableData" style="width: 100%" height="600"> 
          <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
          <el-table-column prop="org_name" label="单位" width="150" align="center"></el-table-column>
          <el-table-column prop="username" label="任务执行人" width="90" align="center"></el-table-column>
          <el-table-column prop="risk_source_name" label="风险源" align="center"></el-table-column>
          <el-table-column prop="period" label="任务周期" width="100" align="center"></el-table-column> 
          <el-table-column prop="start_time" label="任务开始时间" align="center" width="200"></el-table-column>  
          <el-table-column prop="end_time" label="任务结束时间" align="center" width="200"></el-table-column>  
          <el-table-column label="期数" width="100" align="center">
            <template slot-scope="scope">
                {{scope.row.patrol_type!=3?scope.row.serial_num:''}}
            </template>
          </el-table-column>  
          <el-table-column label="状态" width="100" align="center">
              <template slot-scope="scope">  
                <el-button type="text" v-if="scope.row.has_done==1" @click="viewDetail(scope.row.uuid)">
                    已完成
                </el-button>
                {{scope.row.has_done==0?'未完成':''}}
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
          :total="page.total">
        </el-pagination> 

        <el-dialog :visible.sync="dialogVisible" width="60%" title="巡检结果" append-to-body> 
            <patrolResult :tableData="detailTableData" :picsData="detailPicsData"></patrolResult>
        </el-dialog>

    </div>
</template>

<script>
/* eslint-disable */
import patrolResult from '../task/patrolResult';
    export default {
        components :{patrolResult},
        props: ['dutyOrg','status','taskTableData', 'taskTotal'],
        data(){
            return {
                tableData: this.taskTableData, 
                page:{pageNum:1,pageSize:10,total:this.taskTotal},
                taskStatus: this.status,
                detailTableData: [],
                dialogVisible: false,
                detailPicsData: null
            };
        },
        methods: { 
            viewDetail(patrolUUID){
                this.$request({
                    url: '/patrol/getPatrolResult/' + patrolUUID, 
                    method: 'post'
                }).then(res => {
                    let data = res.data;
                    this.detailTableData = data.result_data;
                    
                    //pictures:
                    this.$request({
                        url: '/patrol/getPatrolPics/' + patrolUUID,
                        method: 'post'
                    }).then(res => {
                        let data = res.data; 
                        if(data.result_data[0].pic_path!=undefined && data.result_data[0].pic_path!=null)
                            this.detailPicsData = data.result_data[0].pic_path.split(';');
                        else
                            this.detailPicsData = [];
                        this.dialogVisible = true;
                    });
                });


            },
            searchDataList(){ 
                let q = {org_id:this.dutyOrg,has_done:this.taskStatus};
                this.$request({
                    url: '/patrol/getPageInfoList/' + this.page.pageNum + '/' + this.page.pageSize,
                    data: q,
                    method: 'post'
                }).then(res => {
                    let data = res.data;
                    this.tableData = data.result_data.list;
                    this.page.total=data.result_data.total;
                });
            },
            handleSizeChange(val){
                this.page.pageSize = val;
                this.searchDataList ();
            },
            handleCurrentChange(val){
                this.page.pageNum = val;
                this.searchDataList();
            }
        },
        watch: {
            taskStatus: {
                handler(newValue,oldValue){
                    this.searchDataList();
                }
            },
            taskTableData: {
                handler(newValue,oldValue){
                    this.tableData = newValue;
                }
            },
            taskTotal: {
                handler(newValue,oldValue){
                    this.page.total = newValue;
                }
            },
            status: {
                handler(newValue,oldValue){
                    this.taskStatus = newValue;
                }
            }
        }
    };

</script>