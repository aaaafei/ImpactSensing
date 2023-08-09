<template>
    <div>   

        <el-card> 
            <div style="margin-top: 30px;font-size:20px;font-weight:bold;text-align:center;">
               风险源巡查人设置 
            </div>  
            <el-row>
                <el-col :span="24">
                <el-button-group>
                    <el-button type="primary" icon="el-icon-plus" @click="addPatrol">新增</el-button>
                    <el-button type="primary" icon="el-icon-delete" @click="deletePatrolDatas">删除</el-button> 
                </el-button-group> 
                </el-col>
            </el-row>   
            
            <el-table :data="setDatas" width="100%" height="500px" @selection-change="handleSelectionChange_patrol">
                <el-table-column type="selection" width="40" header-align="center"></el-table-column>
                <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
                <el-table-column label="巡查人" prop="administrator_name" width="100" align="center"></el-table-column>
                <el-table-column label="巡查周期" prop="administrator_period" width="100" align="center"></el-table-column>
                <el-table-column label="启动巡检任务" prop="enabled" width="120" align="center">  
                    <template slot-scope="scope"> 
                        {{scope.row.enabled=='1'?'是':'否'}}
                    </template>
                </el-table-column>
                <el-table-column label="任务开始时间" prop="start_time" width="140" align="center"></el-table-column>
                <el-table-column label="备注" prop="remark" header-align="center"></el-table-column>
                <el-table-column label="操作" width="150" align="center">
                    <template slot-scope="scope">
                    <el-button size="mini" @click="editPatrol(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="deletePatrol(scope.$index, scope.row)">删除</el-button> 
                    </template>
                </el-table-column>
            </el-table> 
        </el-card>
   
        <el-dialog :visible.sync="addEditPatrolVisible" width="50%" :title="addEditPatrolTitle" append-to-body>
            <riskSourcePatrolAddEdit @closeAddEdit="closePatrolAddEdit" :detailForm="patrolForm" :op="op"></riskSourcePatrolAddEdit>
        </el-dialog>


 
        <el-card> 
            <div style="margin-top: 30px;font-size:20px;font-weight:bold;text-align:center;">
               风险源巡查内容设置 
            </div> 
           
            <el-row>
                <el-col :span="24">
                <el-button-group>
                    <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
                    <el-button type="primary" icon="el-icon-delete" @click="deleteDatas">删除</el-button> 
                </el-button-group> 
                </el-col>
            </el-row>   
            
            <el-table :data="contentDatas" width="100%" height="500px" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="40" header-align="center"></el-table-column>
                <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
                <el-table-column label="巡查项内容" prop="content" header-align="center"></el-table-column>
                <el-table-column label="检查结果待选项" prop="options" align="center"></el-table-column>
                <el-table-column label="排序号" prop="sort_num" width="160" align="center"></el-table-column>
                <el-table-column label="操作" width="150" header-align="center">
                    <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button> 
                    </template>
                </el-table-column>
            </el-table> 
        </el-card>

        <el-dialog :visible.sync="addEditContentVisible" width="50%" :title="addEditContentTitle" append-to-body>
            <riskSourceContentAddEditComponent :detailForm="addForm" @closeContentAddEdit="closeContentAddEdit" :addSaveBtn="addSaveBtn" :editSaveBtn="editSaveBtn"></riskSourceContentAddEditComponent>
        </el-dialog> 


    </div>
</template>

<script>
/* eslint-disable */
    import riskSourcePatrolAddEdit from './risk_source_patrol_add_edit.vue';
    import riskSourceContentAddEditComponent from './risk_source_content_add_edit.vue';
    export default{
        props:['riskSourceUUID','patrolContentData','patrolSetData'],
        components:{riskSourceContentAddEditComponent, riskSourcePatrolAddEdit},
        data(){
            return {
                userChoosePage_admin:false,
                userChoosePage_station:false,
                addEditContentTitle: '',
                addEditContentVisible:false,
                addSaveBtn: false,
                editSaveBtn: false,
                addEditPatrolVisible: false,
                addEditPatrolTitle: '', 
                pageProps:['uuid','risk_source_uuid','content','options','sort_num'],
                addForm: {uuid:null,risk_source_uuid:null,content:null,options:null,sort_num:null},
                api: {
                    URL_list: '/riskSourcePatrolContent/getPatrolContentByRiskSourceUUID/',
                    URL_del: '/riskSourcePatrolContent/delete/'
                },
                contentDatas: this.patrolContentData,
                selectUUIDs: [], 
                setDatas: this.patrolSetData,  
                patrolSetProps: [
                    "uuid",
                    "risk_source_uuid",
                    "administrator_id",
                    "administrator_name",
                    "administrator_period",
                    "start_time", 
                    "enabled",
                    "remark"
                ],
                patrolForm: {
                    uuid: null,
                    risk_source_uuid: null,
                    administrator_id: null,
                    administrator_name: null,
                    administrator_period: null,
                    start_time: null, 
                    enabled: "1",
                    remark: null
                },
                selectUUIDs_patrol: [],
                op: null
            };
        },
        methods:{
            handleSelectionChange(rows) {
                this.selectUUIDs=[];
                if(rows.length>0){
                    rows.forEach(row => {
                        if (row) {
                            this.selectUUIDs.push(row.uuid);
                        }
                    });
                }
            },
            closeContentAddEdit(){
                this.addEditContentVisible = false;
                this.queryTableDatas();
            },
            handleAdd(){
                this.addEditContentTitle = '添加';
                this.addSaveBtn = true;
                this.editSaveBtn = false;
                this.addEditContentVisible = true;
                for(var i=0;i<this.pageProps.length;i++){
                    this.addForm[this.pageProps[i]] = null;
                }
                this.addForm.risk_source_uuid = this.riskSourceUUID; 
            },
            handleEdit(index,rowData){
                this.addEditContentTitle =  '修改';
                this.addSaveBtn = false;
                this.editSaveBtn = true;
                this.addEditContentVisible = true;
                for(var i=0;i<this.pageProps.length;i++){
                    this.addForm[this.pageProps[i]] = rowData[this.pageProps[i]];
                }
            },
            handleDelete(index,rowData){
                this.$confirm('确认删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request({
                    url: this.api.URL_del + rowData.uuid
                    }).then(res => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.queryTableDatas();
                    }); 
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                }); 
            },
            deleteDatas(){
                if(this.selectUUIDs.length==0){
                    this.$alert('请选择要删除的列数据！','提示');
                }
                else{
                    this.$confirm('确认删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                    }).then(() => {
                        this.$request({
                            url: this.api.URL_del + this.selectUUIDs.join(',') +"/"
                        }).then(res => {
                            this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                            this.queryTableDatas();
                            this.selectUUIDs=[];
                        });
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    }); 
                }
            },
            queryTableDatas(){
                var url = this.api.URL_list + this.riskSourceUUID;
                this.$request({
                    url: url,
                    method: 'post'
                }).then(res => {
                    let data = res.data;
                    this.contentDatas = data.result_data;
                });
            },
            closePatrolAddEdit(){
                this.addEditPatrolVisible = false;
                this.queryPatrolSetDatas();
            },
            queryPatrolSetDatas(){
                var url =  "/riskSourcePersonLiable/getByRiskSourceId/" + this.riskSourceUUID;
                this.$request({
                    url: url,
                    method: "post"
                }).then(res => {
                    let data = res.data;
                    this.setDatas = data.result_data;
                });
            }, 
            handleSelectionChange_patrol(rows) {
                this.selectUUIDs_patrol=[];
                if(rows.length>0){
                    rows.forEach(row => {
                        if (row) {
                            this.selectUUIDs_patrol.push(row.uuid);
                        }
                    });
                }
            },
            addPatrol(){
                this.addEditPatrolTitle = '添加';
                this.op = 'add'; 
                this.addEditPatrolVisible = true;
                for(var i=0;i<this.patrolSetProps.length;i++){
                    this.patrolForm[this.patrolSetProps[i]] = null;
                }
                this.patrolForm.risk_source_uuid = this.riskSourceUUID; 
            },
            editPatrol(index,rowData){
                this.addEditPatrolTitle =  '修改';
                this.op = 'edit'; 
                this.addEditPatrolVisible = true;
                for(var i=0;i<this.patrolSetProps.length;i++){ 
                    this.patrolForm[this.patrolSetProps[i]] = rowData[this.patrolSetProps[i]];
                } 
            },
            deletePatrolDatas(){
                if(this.selectUUIDs_patrol.length==0){
                    this.$alert('请选择要删除的列数据！','提示');
                }
                else{
                    this.$confirm('确认删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                    }).then(() => {
                        this.$request({
                            url: '/riskSourcePersonLiable/delete/' + this.selectUUIDs_patrol.join(',') +"/"
                        }).then(res => {
                            this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                            this.queryPatrolSetDatas();
                            this.selectUUIDs_patrol=[];
                        });
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    }); 
                }
            },
            deletePatrol(index,rowData){
                this.$confirm('确认删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request({
                    url: '/riskSourcePersonLiable/delete/' + rowData.uuid
                    }).then(res => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.queryPatrolSetDatas();
                    }); 
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                }); 
            }
        },
        watch:{
            patrolContentData(value,oldvalue){ 
                this.contentDatas = value;
            },
            patrolSetData(value,oldvalue){
                this.setDatas = value;
            }
        }
    }    

</script>
