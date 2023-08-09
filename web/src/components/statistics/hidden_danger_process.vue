<template>
    <div>
        <div style="margin: 10px;">
            <el-row>
                <el-col :span="24">   
                    <el-select v-model="processStatus" placeholder="处理状态" clearable>
                        <el-option label="处理完" :value="3"></el-option>
                        <el-option label="处理中" :value="2"></el-option>
                        <el-option label="未处理" :value="1"></el-option>
                    </el-select>
                </el-col>
            </el-row>   
        </div> 
        <el-table :data="tableData" style="width: 100%" height="600" highlight-current-row>
            <el-table-column prop="year" label="年份" width="60" align="center"></el-table-column>
            <el-table-column prop="org_name" label="上报部门" width="120" align="center"></el-table-column>
            <el-table-column prop="principal_name" label="上报人" width="120" align="center"></el-table-column>
            <el-table-column prop="duty_org_name" label="责任部门" width="120" align="center"></el-table-column>
            <el-table-column prop="summary" label="隐患概要信息" header-align="center"></el-table-column>
            <el-table-column prop="discovery_time" label="发现时间" width="160" align="center"></el-table-column>
            <el-table-column prop="expires" label="治理期限（天）" width="120" align="center"></el-table-column>
            <el-table-column  label="整改前附件" align="center" width="130">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="downloadFile(scope.row.attachment1)">
                   {{ scope.row.attachment1!=null ? scope.row.attachment1.substr(scope.row.attachment1.lastIndexOf('\\')+1) : '' }}
                </el-button>
              </template>
            </el-table-column> 
            <el-table-column  label="整改后附件" align="center" width="130">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="downloadFile(scope.row.attachment2)">
                   {{ scope.row.attachment2!=null ? scope.row.attachment2.substr(scope.row.attachment2.lastIndexOf('\\')+1) : '' }}
                </el-button>
              </template>
            </el-table-column> 
            <el-table-column label="治理状态" align="center" width="80">
                <template slot-scope="scope"> 
                     {{statusStr(scope.row.status)}}
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
    </div>
</template>

<script>
/* eslint-disable */
import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
    export default {
        props: ['dutyOrg','status','hiddenDangerTableData','hiddenDangerTotal'],
        data() {
            return {
                tableData: this.hiddenDangerTableData, 
                page:{pageNum:1,pageSize:10,total:this.hiddenDangerTotal},
                processStatus: this.status
            };
        },
        methods: {
            statusStr(s){
                switch(s){
                    case 1:
                        return '未处理';
                    case 2:
                        return '处理中';
                    case 3:
                        return '处理完';
                }
            },
            searchDataList () {
                let q = {org_id:this.dutyOrg,status:this.processStatus}; 
                this.$request({
                  url: '/hiddenDanger/getPageData/' + this.page.pageNum + '/' + this.page.pageSize,
                  method: 'post',
                  data:q
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
        mounted(){
            this.searchDataList();
        },
        watch: {
            processStatus: {
                handler(newValue,oldValue){
                    this.searchDataList();
                }
            },
            status: {
                handler(newValue,oldValue){
                    this.processStatus = newValue;
                }
            },
            hiddenDangerTableData: {
                handler(newValue,oldValue){
                    this.tableData = newValue;
                }
            },
            hiddenDangerTotal: {
                handler(newValue,oldValue){
                    this.page.total = newValue;
                }
            }
        }
    };

</script>