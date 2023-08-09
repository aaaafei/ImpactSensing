<template>
    <div>
        <div style="margin: 10px;">
      <el-row> 
        <el-col :span="8"> 
          <el-input style="width:200px;" v-model="qSummary" placeholder="请输入"></el-input>
          <el-button type="primary" icon="el-icon-search" @click="searchDataList">搜索</el-button>
        </el-col>
      </el-row>   
    </div> 
        <el-table :data="tableData" style="width: 100%" height="450" :cell-style="{padding:'3px 0'}"  highlight-current-row> 
            <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
            <el-table-column prop="processInfo" label="流程名" width="300" align="center"></el-table-column>
            <el-table-column label="当前步骤" align="center">
                    <template slot-scope='scope'> 
                         {{scope.row.taskName + 
                         (scope.row.variable.description!=null?('：'+scope.row.variable.description):'')}} 
                  </template>
            </el-table-column>
            <el-table-column prop="createTime" label="发起时间" width="200" align="center"></el-table-column>  
            <el-table-column label="操作" header-align="center" width="200">
                <template slot-scope="scope">
                    <el-button size="mini" type="success" plain @click="processDeal(scope.row)">流程处理</el-button>
                    <el-button size="mini" type="danger" @click="processInfo(scope.row.procInstId)">流转情况</el-button> 
                </template>
            </el-table-column>
        </el-table>

        <!-- 流程流转图 -->
        <el-dialog :visible.sync="process.picVisible" title="流程流转图" width="95%"> 
            <el-table :data="process.tableTaskProcess" style="width: 100%" height="300"   :cell-style="{padding:'3px 0'}" highlight-current-row> 
                <el-table-column label="序号" type="index" width="65" align="center"></el-table-column>
                <el-table-column prop="activityName" label="活动名称" width="250" align="center"></el-table-column>
                <el-table-column prop="assigneeName" label="办理人" width="150" align="center"></el-table-column>
                <el-table-column prop="comment" label="处理意见" show-overflow-tooltip="true" align="center"></el-table-column>
                <el-table-column prop="startTime" label="任务发送时间" width="160" align="center"></el-table-column>
                <el-table-column prop="endTime" label="处理结束时间" width="160" align="center"></el-table-column>
            </el-table>
            <img :src="process.picUrl"  style="width:90%">
        </el-dialog>

        <!-- workflow process dialogs: -->

        <el-dialog :visible.sync="HiddenDanger.visible" :title="HiddenDanger.title" width="80%" :before-close="HiddenDanger_beforeClose">
            <HiddenDanger ref="HiddenDanger" :formDetail="HiddenDanger.form" :fileListA="HiddenDanger.fileList1" :fileListB="HiddenDanger.fileList2" :fileListC="HiddenDanger.fileList3" :usersSelect="HiddenDanger.usersSelect" @closeProcess="HiddenDanger_close" :procInstId="proc_inst_id" :taskId="task_id" :actStep="actStep" :workflowVariable="workflowVariable"></HiddenDanger>
        </el-dialog>

        <el-dialog :visible.sync="RiskInfoSubmit.visible" :title="RiskInfoSubmit.title" width="80%" :before-close="RiskInfoSubmit_beforeClose">
            <RiskInfoSubmit ref="RiskInfoSubmit" :detailForm="RiskInfoSubmit.form" @closeProcess="RiskInfoSubmit_close" :procInstId="proc_inst_id" :taskId="task_id" :actStep="actStep"></RiskInfoSubmit>
        </el-dialog>
 
        <el-dialog :visible.sync="HiddenDangerMessage.visible" :title="HiddenDangerMessage.title" width="80%">
            <HiddenDangerMessage :formDetail="HiddenDanger.form" :fileListA="HiddenDanger.fileList1" :fileListB="HiddenDanger.fileList2" :fileListC="HiddenDanger.fileList3" :procInstId="proc_inst_id" :taskId="task_id" @closeProcess="HiddenDangerMessage_close"></HiddenDangerMessage>     
        </el-dialog>
    </div>
</template>

<script>
/* eslint-disable */
import HiddenDanger from './hidden_danger_process_deal.vue';
import HiddenDangerMessage from './hidden_danger_message.vue';
import RiskInfoSubmit from './risk_info_submit_process_deal.vue';
    export default {
        components: {HiddenDanger,RiskInfoSubmit,HiddenDangerMessage},
        data (){
            return {
                workflowCategory: ['HiddenDanger', 'RiskInfoSubmit'],
                workflowVariable:{},
                tableData: [],
                qSummary: null,
                HiddenDanger: {
                    title: null,
                    visible: false,
                    pageProps: ['uuid', 'year', 'summary','plan', 'principal','principal_name', 'discovery_time', 'expires', 'attachment1', 'attachment2', 'status', 'begin_deal', 'end_deal','org_name','org_id','level','duty_org_name','duty_org_id','receiver','targetNode'],
                    form:{uuid:null,year:null,summary:null,plan:null,principal:null,principal_name:null,discovery_time:null,expires:null,attachment1:null,attachment2:null,status:null,begin_deal:null,end_deal:null,org_name:null,org_id:null,level:null,duty_org_name:null,duty_org_id:null,receiver:null,targetNode:null},
                    fileList1:[],
                    fileList2:[],
                    fileList3:[],
                    usersSelect:[]
                },  
                proc_inst_id: null,
                task_id: null,
                actStep: null,
                process: {
                    picVisible: false,
                    tableTaskProcess: [],
                    picUrl: null
                }, 
                RiskInfoSubmit: {
                    title: null,
                    visible: false,
                    pageProps: ['uuid', 'user_id', 'org_id', 'title', 'content', 'submit_time', 'syn_status', 'check_status'],
                    form: {uuid: null, user_id: null, org_id: null, title:null,  content: null, submit_time: null,syn_status: null, check_status: null}
                },
                HiddenDangerMessage: {
                    title: null,
                    visible: false
                }
            };
        },
        methods: {
            processDeal(row){ 
                this.workflowVariable = row.variable; 
                this.proc_inst_id = row.procInstId;//流程实例id
                this.task_id = row.taskId;//任务id
                this.actStep = row.taskDefinitionKey;//流程当前步骤key 
                if(row.processKey == 'HiddenDanger'){
                    this.HiddenDanger.title = "流程处理--" + row.taskName;
                    var url = "/hiddenDanger/getFormDetailByProcInstId/" + this.proc_inst_id;
                    this.$request({
                        url: url,
                        method: 'get'
                    }).then(res => {
                        //初始化表单数据
                        let data = res.data.result_data;
                        for(var i=0;i<this.HiddenDanger.pageProps.length;i++){ 
                          this.HiddenDanger.form[this.HiddenDanger.pageProps[i]] = data[this.HiddenDanger.pageProps[i]];
                        }
                        //初始化附件列表：
                        if(data.attachment1!=null && data.attachment1!=''){
                          let path = data.attachment1;
                          let name = path.substr(path.lastIndexOf('\\')+1); 
                          this.HiddenDanger.fileList1 = [{name:name,url:path}]; 
                        }
                        else{
                          this.HiddenDanger.fileList1 = [];
                        }
                        if(data.attachment2!=null && data.attachment2!=''){
                          let path = data.attachment2;
                          let name = path.substr(path.lastIndexOf('\\')+1);
                          this.HiddenDanger.fileList2 = [{name:name,url:path}]; 
                        }
                        else{
                          this.HiddenDanger.fileList2 = [];
                        }
                        if(data.attachment3!=null && data.attachment3!=''){
                          let path = data.attachment3;
                          let name = path.substr(path.lastIndexOf('\\')+1);
                          this.HiddenDanger.fileList3 = [{name:name,url:path}]; 
                        }
                        else{
                          this.HiddenDanger.fileList3 = [];
                        }

                        var roleNames;
                        var orgId = this.HiddenDanger.form.duty_org_id;
                        if(this.actStep == 'report'|| this.actStep == 'deptHead'){
                            //责任部门安全员
                            roleNames = 'ROLE_BASE_OFFICER,ROLE_DEPT_OFFICER';
                            
                        }

                        if(this.actStep == 'securityOfficer'){
                            //责任部门负责人
                            roleNames = 'ROLE_STATION_AGENT,ROLE_ORG_HEAD,ROLE_MSS_PRINCIPAL';
                        }
 
                        this.$request({
                            url: '/user/getUserByOrgRole/'+orgId+'/'+roleNames,
                            method: 'get'
                        }).then(res => {
                          this.HiddenDanger.usersSelect = res.data.result_data; 
                          this.HiddenDanger.visible = true;
                        });

                    });
                }
                else if(row.processKey == 'RiskInfoSubmit'){
                    this.RiskInfoSubmit.title = "流程处理--" + row.taskName;
                    var url = "/riskInfoSubmit/getFormDetailByProcInstId/" + this.proc_inst_id;
                    this.$request({
                        url: url,
                        method: 'get'
                    }).then(res => {
                        let data = res.data.result_data;
                        for(var i=0;i<this.RiskInfoSubmit.pageProps.length;i++){
                            this.RiskInfoSubmit.form[this.RiskInfoSubmit.pageProps[i]] = data[this.RiskInfoSubmit.pageProps[i]];
                        }
                        this.RiskInfoSubmit.visible = true;
                    });
                }
                else if(row.processKey == 'Message'){ 
                        //消息提醒
                        if(row.variable.businessType == 'HiddenDanger'){ 
                            this.HiddenDangerMessage.title = "消息提醒"  ;
                            var url = "/hiddenDanger/getFormDetailById/" + this.workflowVariable.businessKey;
                            this.$request({
                                url: url,
                                method: 'get'
                            }).then(res => {
                                //初始化表单数据
                                let data = res.data.result_data;
                                for(var i=0;i<this.HiddenDanger.pageProps.length;i++){ 
                                  this.HiddenDanger.form[this.HiddenDanger.pageProps[i]] = data[this.HiddenDanger.pageProps[i]];
                                }
                                //初始化附件列表：
                                if(data.attachment1!=null && data.attachment1!=''){
                                  let path = data.attachment1;
                                  let name = path.substr(path.lastIndexOf('\\')+1); 
                                  this.HiddenDanger.fileList1 = [{name:name,url:path}]; 
                                }
                                else{
                                  this.HiddenDanger.fileList1 = [];
                                }
                                if(data.attachment2!=null && data.attachment2!=''){
                                  let path = data.attachment2;
                                  let name = path.substr(path.lastIndexOf('\\')+1);
                                  this.HiddenDanger.fileList2 = [{name:name,url:path}]; 
                                }
                                else{
                                  this.HiddenDanger.fileList2 = [];
                                }
                                if(data.attachment3!=null && data.attachment3!=''){
                                  let path = data.attachment3;
                                  let name = path.substr(path.lastIndexOf('\\')+1);
                                  this.HiddenDanger.fileList3 = [{name:name,url:path}]; 
                                }
                                else{
                                  this.HiddenDanger.fileList3 = [];
                                }

                                this.HiddenDangerMessage.visible = true;

                            });
                    }
                }

            },
            processInfo(procInstId){
                var url = "/api/file/traceProcess/" + procInstId;
                this.process.picUrl =  url;
                this.$request({
                    url: "/workflow/taskProcessInfo/" + procInstId,
                    method: 'get'
                }).then(res => {
                    let data = res.data;
                    this.process.tableTaskProcess = data.result_data;  
                    this.process.picVisible = true;
                });
            },
            searchDataList(){
                var url = "/workflow/queryProcessTask";
                this.$request({
                    url: url,
                    method: 'get'
                }).then(res => {
                    let data = res.data;
                    this.tableData = data.result_data; 
                });
            },
            HiddenDanger_beforeClose(done){
                this.$refs.HiddenDanger.resetForm();
                done();
            },
            HiddenDanger_close(){
                this.searchDataList();
                this.HiddenDanger.visible = false;
            },
            RiskInfoSubmit_beforeClose(done){
                this.$refs.RiskInfoSubmit.resetForm();
                done();
            },
            RiskInfoSubmit_close(){
                this.searchDataList();
                this.RiskInfoSubmit.visible = false;
            },
            HiddenDangerMessage_close(){
                this.searchDataList();
                this.HiddenDangerMessage.visible = false;
            }
        },
        mounted: function(){
            this.searchDataList();
        } 
    };
</script>