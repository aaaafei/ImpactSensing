<template>
    <div>
        <el-form :model="form" :rules="rules" ref="form" label-width="120px">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="年份" prop="year">
                        <el-input-number v-model="form.year" controls-position="right"></el-input-number> 
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="责任单位" prop="duty_org_name">
                        <el-input v-model="form.duty_org_name" readonly= "true" placeholder="请选择责任单位"> 
                            <el-button slot="append" icon="el-icon-search" @click="chooseOrg"></el-button>
                            </el-input>
                        </el-input>
                     </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="上报单位"> 
                        {{form.org_name}}
                    </el-form-item> 
                </el-col> 
                <el-col :span="12">
                    <el-form-item label="上报人">  
                        {{form.principal_name}} 
                    </el-form-item> 
                </el-col>
            </el-row>
            <el-row>
                <el-form-item label="隐患概要信息" prop="summary">
                    <el-input type="textarea" v-model="form.summary"></el-input>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item label="整改措施/计划" prop="plan">
                    <el-input type="textarea" v-model="form.plan"></el-input>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item>
                    <el-upload ref="attachment3" :http-request="req3" :before-remove="beforeRemove3" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList3" :show-file-list="true" action="">
                        <el-button size="small" type="primary">点击上传</el-button> 
                    </el-upload>
                </el-form-item>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="治理状态" prop="status"> 
                        <el-radio-group v-model="form.status">
                            <el-radio :label="1">未处理</el-radio>
                            <el-radio :label="2">处理中</el-radio>
                            <el-radio :label="3">处理完</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="隐患等级" prop="level"> 
                        <el-select v-model="form.level" placeholder="请选择隐患等级">
                           <el-option label="一般" :value="1"></el-option> 
                           <el-option label="重大" :value="3"></el-option>
                        </el-select>
                     </el-form-item>
                </el-col> 
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="发现时间" prop="discovery_time">
                        <el-date-picker v-model="form.discovery_time" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="12"> 
                    <el-form-item label="治理期限" prop="expires">
                        <el-select v-model="selExpires" @change="onChangeExpires" v-if="form.uuid==null">
                            <el-option label="7" value="7"></el-option>
                            <el-option label="15"  value="15"></el-option>
                            <el-option label="自定义" value=""></el-option>
                        </el-select>
                        <el-input-number v-model="form.expires" controls-position="right" :precision="0" :step="1" v-if="selExpires == ''||form.uuid!=null"></el-input-number> 天
                    </el-form-item>
                </el-col> 
            </el-row> 
            <el-row>
                <el-col :span="12">
                    <el-form-item label="整改前附件">
                    <el-upload ref="attachment1" :http-request="req1" :before-remove="beforeRemove1" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList1" :show-file-list="true" action="">
                        <el-button size="small" type="primary">点击上传</el-button> 
                    </el-upload>
                </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="治理开始时间" prop="begin_deal">
                        <el-date-picker v-model="form.begin_deal" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
                    </el-form-item>
                </el-col>  
            </el-row>
            <el-row>
                 <el-col :span="12">
                    <el-form-item label="整改后附件">  
                        <el-upload ref="attachment2" :http-request="req2" :before-remove="beforeRemove2" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList2" :show-file-list="true" action="">
                            <el-button size="small" type="primary">点击上传</el-button> 
                        </el-upload>
                    </el-form-item>
                 </el-col>
                <el-col :span="12">
                    <el-form-item label="治理结束时间" prop="end_deal">
                        <el-date-picker v-model="form.end_deal" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
                    </el-form-item>
                </el-col>  
            </el-row> 

            <el-row><hr/></el-row>
           
            <el-row>
                <el-form-item label="处理意见"> 
                    <el-input type="textarea" v-model="process_comment"></el-input>
                </el-form-item>
            </el-row>

          
            <el-row  v-if=" (actStep == 'report' || actStep == 'securityOfficer' || actStep =='deptHead') && workflowVariable.replenish!='true'  ">
                 <el-form-item label="接收人"> 
                    <el-select v-model="form.receiver" placeholder="退回操作无需选择">
                       <el-option v-for="item in users" :key="item.id" :label="item.truename" :value="item.id">
                        </el-option>
                    </el-select> 
                 </el-form-item>
            </el-row>

            <el-form-item align="center">
                <!-- 现场负责人上报隐患 -->
                <el-button type="primary" v-if="actStep=='report' && workflowVariable.replenish!='true'" @click="report2SecurityOfficer">报送安全员</el-button>
                <el-button type="primary" v-if="actStep=='report' && workflowVariable.replenish=='true'" @click="report2Replenish">补充材料，送安全部</el-button> 
                
                <!-- 责任部门安全员处理 -->
                <el-button type="primary" v-if="actStep=='securityOfficer' && workflowVariable.replenish !='true'" @click="securityOfficer2Pass">处理完，送确认</el-button>
                <el-button type="primary" v-if="actStep=='securityOfficer' && workflowVariable.replenish !='true'" @click="securityOfficer2Rearrange">送部门负责人重新指派</el-button>
                <el-button type="primary" v-if="actStep=='securityOfficer' && workflowVariable.replenish !='true'" @click="securityOfficer2Back">隐患非本部门责任，退回</el-button>
                <el-button type="primary" v-if="actStep=='securityOfficer' && workflowVariable.replenish=='true'" @click="securityOfficer2Replenish">补充材料，送安全部</el-button>
                
                <!-- 部门负责人指派 -->
                <el-button type="primary" v-if="actStep=='deptHead'" @click="deptHead2Rearrange">指派安全员处理</el-button>
                
                <!-- 责任部门负责人和现场负责人确认 -->
                <el-button type="primary" v-if="actStep=='reporterAndHead'" @click="reporterAndHead2Pass">通过</el-button>
                <el-button type="primary" v-if="actStep=='reporterAndHead'" @click="reporterAndHead2Refuse">不通过</el-button>
                
                <!-- 安全管理部复查 -->
                <el-button type="primary" v-if="actStep=='securityDept'" @click="securityDept2Pass">通过</el-button> 
                <el-button type="primary" v-if="actStep=='securityDept'" @click="securityDept2Back">退回</el-button>
                <el-select v-model="form.targetNode" placeholder="请选择退回目标节点" v-if="actStep=='securityDept'">
                    <el-option label="现场负责人上报隐患--补充材料" value="report@true"></el-option>
                    <el-option label="现场负责人上报隐患--重走流程" value="report@false"></el-option>
                    <el-option label="责任部门安全员处理--补充材料" value="securityOfficer@true"></el-option>
                    <el-option label="责任部门安全员处理--重走流程" value="securityOfficer@false"></el-option>
                    <el-option label="责任部门负责人和现场负责人确认" value="reporterAndHead@false"></el-option>
                </el-select> 
  
            </el-form-item>
        </el-form>

        <el-dialog :visible.sync="orgSelectPage" width="20%" title="责任单位选择" append-to-body>
            <org4Select  @closeOrg="closeOrg"></org4Select>
        </el-dialog>

          <el-dialog :visible.sync="userChoosePage" width="20%" title="人员选择" append-to-body>
            <user4Select @closeUseSel="closeUseSel" limitPermision="1"></user4Select>
        </el-dialog>

    </div>
</template>

<script>
/* eslint-disable */
import org4Select from './org4Select.vue';
import user4Select from './user4Select.vue';
import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
    export default {
        props:['formDetail','fileListA','fileListB','fileListC','usersSelect','procInstId','taskId','actStep','workflowVariable'], 
        components:{org4Select,user4Select},
        data(){
            return {
                form: this.formDetail, 
                fileList1: this.fileListA,
                fileList2: this.fileListB,
                fileList3: this.fileListC,
                rules:{
                    year: [
                        {required: true, message: '年份', trigger: 'blur'}
                    ],
                    duty_org_name:[
                        {required: true, message: '隐患责任单位', trigger: 'blur'}
                    ],
                    summary: [
                        {required: true, message: '隐患概要信息', trigger: 'blur'},
                        {max: 100, message: '长度小于100个字符', trigger: 'blur'}
                    ],
                    principal: [
                        {required: true, message: '整改责任人', trigger: 'blur'}
                    ],
                    discovery_time: [
                        {required: true, message: '发现时间', trigger: 'blur'}
                    ],
                    expires: [
                        {required: true, message: '治理期限', trigger: 'blur'}
                    ],
                    status: [
                        {required: true, message: '治理状态', trigger: 'change'}
                    ],
                    level: [
                        {required: true, message: '隐患等级', trigger: 'change'}
                    ]
                },
                api:{
                    URL_save: '/hiddenDanger/save/', 
                    URL_downloadFile: '/file/download/'
                }, 
                file1: null,
                file2: null,
                file3: null,
                selExpires: null,
                orgSelectPage: false,
                userChoosePage: false,
                process_comment: null,
                users: this.usersSelect
            };
        },
        methods:{
            onChangeExpires(value){
                if(value!='') 
                  this.form.expires = value;
                else
                  this.form.expires = undefined;
            },
            chooseUser(){
                this.userChoosePage = true; 
            },
            closeUseSel(name,id){
                this.userChoosePage = false;
                this.form.principal = id;
                this.form.principal_name = name;
            },
            chooseOrg(){
              this.orgSelectPage = true;
            },
            closeOrg(name,id){
              this.orgSelectPage = false;
              this.form.duty_org_name = name;
              this.form.duty_org_id = id;
              //设置责任部门安全员select
                var roleNames = 'ROLE_BASE_OFFICER,ROLE_DEPT_OFFICER';
                this.$request({
                    url: '/user/getUserByOrgRole/'+id+'/'+roleNames,
                    method: 'get'
                }).then(res => {
                    this.users = res.data.result_data;  
                    this.form.receiver = null;
                });

            },
            req1(f){
                this.file1 = f.file;  
                this.fileList1.push({name:f.file.name,url:''});
            },
            req2(f){
                this.file2 = f.file;  
                this.fileList2.push({name:f.file.name,url:''});
            },
            req3(f){
                this.file3 = f.file;  
                this.fileList3.push({name:f.file.name,url:''});
            },
            exceed(files, fileList){
                if(files.length+fileList.length>1){
                    this.$message({message:'请先删除原附件再上传！',type:'warning'});
                    return false;
                }
            },
            preview(fileInfo){ 
                fileDownloadUtil(fileInfo.url);  
            }, 
            beforeRemove1(file, fileList){ 
                this.file1 = null; 
                this.fileList1 = []; 
            },
            beforeRemove2(file, fileList){ 
                this.file2 = null;
                this.fileList2 = [];
            }, 
            beforeRemove3(file, fileList){ 
                this.file3 = null;
                this.fileList3 = [];
            },  
 
            close(){
                this.$emit('closeProcess', '');
            },
            resetForm(){
                let formName = 'form';
                this.$refs[formName].resetFields();
                this.$refs.attachment1.clearFiles();
                this.$refs.attachment2.clearFiles();
                this.$refs.attachment3.clearFiles();
                this.file1 = null;
                this.file2 = null; 
                this.file3 = null;
                this.process_comment = null;
            },
            report2SecurityOfficer(){
                if(this.form.receiver==undefined||this.form.receiver==null||this.form.receiver==""){
                    this.$message({message: '请选择接收人！',type: 'warning'});
                    return;
                }
 
                let fd = new FormData();
                fd.append('uuid',this.form.uuid);
                fd.append('year',this.form.year);
                fd.append('org_id',this.form.org_id);
                fd.append('duty_org_id',this.form.duty_org_id);
                fd.append('summary',this.form.summary);
                if(this.form.plan!=undefined)
                    fd.append('plan',this.form.plan);
                fd.append('expires',this.form.expires);
                fd.append('principal',this.form.principal);
                fd.append('status',2);
                fd.append('discovery_time',this.form.discovery_time);
                fd.append('level',this.form.level);
                if(this.form.begin_deal!=null)
                    fd.append('begin_deal',this.form.begin_deal);
                if(this.form.end_deal!=null)
                    fd.append('end_deal',this.form.end_deal);
                fd.append('attachment1',this.file1);
                fd.append('fileList1',this.fileList1.length>0?'yes':'');//传给后台做是否删除用
                fd.append('attachment2',this.file2);
                fd.append('fileList2',this.fileList2.length>0?'yes':'');
                fd.append('attachment3',this.file3);
                fd.append('fileList3',this.fileList3.length>0?'yes':'');
                //workflow relate
                fd.append('task_id',this.taskId);
                fd.append('proc_inst_id',this.procInstId);
                fd.append('comment',this.process_comment);
                fd.append('userId',this.form.receiver);

                this.$request({
                    url: "/hiddenDanger/report2SecurityOfficer",
                    method: 'post',
                    data:  fd
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            },
            report2Replenish(){
                let fd = new FormData();
                fd.append('uuid',this.form.uuid);
                fd.append('year',this.form.year);
                fd.append('org_id',this.form.org_id);
                fd.append('duty_org_id',this.form.duty_org_id);
                fd.append('summary',this.form.summary);
                if(this.form.plan!=undefined)
                    fd.append('plan',this.form.plan);
                fd.append('expires',this.form.expires);
                fd.append('principal',this.form.principal);
                fd.append('status',2);
                fd.append('discovery_time',this.form.discovery_time);
                fd.append('level',this.form.level);
                if(this.form.begin_deal!=null)
                    fd.append('begin_deal',this.form.begin_deal);
                if(this.form.end_deal!=null)
                    fd.append('end_deal',this.form.end_deal);
                fd.append('attachment1',this.file1);
                fd.append('fileList1',this.fileList1.length>0?'yes':'');//传给后台做是否删除用
                fd.append('attachment2',this.file2);
                fd.append('fileList2',this.fileList2.length>0?'yes':'');
                fd.append('attachment3',this.file3);
                fd.append('fileList3',this.fileList3.length>0?'yes':'');
                //workflow relate
                fd.append('task_id',this.taskId);
                fd.append('proc_inst_id',this.procInstId);
                fd.append('comment',this.process_comment); 

                this.$request({
                    url: "/hiddenDanger/report2Replenish",
                    method: 'post',
                    data:  fd
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            },
            securityOfficer2Pass(){
                //流程到部门安全员那里把整改措施/计划、治理开始/结束时间、整改前后附件和处理意见都设成必填项没问题吧？
                if(this.form.plan==undefined || this.form.plan==null || this.form.plan == ''){
                    this.$message({message: '请填写整改措施/计划！',type: 'warning'});
                    return;
                }
                if(this.fileList3.length ==0 && this.file3==null){
                    this.$message({ type: 'warning', message: '请上传整改措施/计划文件!' });
                    return false;
                } 

                if(this.fileList1.length ==0 && this.file1==null){
                    this.$message({ type: 'warning', message: '请上传整改前附件!' });
                    return false;
                }
                if(this.fileList2.length ==0 && this.file2==null){
                    this.$message({ type: 'warning', message: '请上传整改后附件!' });
                    return false;
                }

                if(this.form.begin_deal==undefined || this.form.begin_deal==null || this.form.begin_deal == ''){
                    this.$message({message: '请填治理开始时间！',type: 'warning'});
                    return;
                }

                if(this.form.end_deal==undefined || this.form.end_deal==null || this.form.end_deal == ''){
                    this.$message({message: '请填写治理结束时间！',type: 'warning'});
                    return;
                }

                if(this.process_comment==undefined|| this.process_comment==null || this.process_comment==""){
                    this.$message({message: '请填写处理意见！',type: 'warning'});
                    return;
                }


                if(this.form.receiver==undefined||this.form.receiver==null||this.form.receiver==""){
                    this.$message({message: '请选择接收人！',type: 'warning'});
                    return;
                }
 
                let fd = new FormData();
                fd.append('uuid',this.form.uuid);
                fd.append('year',this.form.year);
                fd.append('org_id',this.form.org_id);
                fd.append('duty_org_id',this.form.duty_org_id);
                fd.append('summary',this.form.summary);
                if(this.form.plan!=undefined)
                    fd.append('plan',this.form.plan); 
                fd.append('expires',this.form.expires);
                fd.append('principal',this.form.principal);
                fd.append('status',2);
                fd.append('discovery_time',this.form.discovery_time);
                fd.append('level',this.form.level);
                if(this.form.begin_deal!=null)
                    fd.append('begin_deal',this.form.begin_deal);
                if(this.form.end_deal!=null)
                    fd.append('end_deal',this.form.end_deal);
                fd.append('attachment1',this.file1);
                fd.append('fileList1',this.fileList1.length>0?'yes':'');//传给后台做是否删除用
                fd.append('attachment2',this.file2);
                fd.append('fileList2',this.fileList2.length>0?'yes':'');
                fd.append('attachment3',this.file3);
                fd.append('fileList3',this.fileList3.length>0?'yes':'');
                //workflow relate
                fd.append('task_id',this.taskId);
                fd.append('proc_inst_id',this.procInstId);
                fd.append('comment',this.process_comment);
                fd.append('userId',this.form.receiver);

                this.$request({
                    url: "/hiddenDanger/securityOfficer2Pass",
                    method: 'post',
                    data:  fd
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            },
            securityOfficer2Rearrange(){
                if(this.form.receiver==undefined||this.form.receiver==null||this.form.receiver==""){
                    this.$message({message: '请选择接收人！',type: 'warning'});
                    return;
                }
                this.$request({
                    url: "/hiddenDanger/securityOfficer2Rearrange",
                    method: 'post',
                    data:  { task_id: this.taskId, procInstId: this.procInstId, comment: this.process_comment, user_id: this.form.receiver}
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            },
            securityOfficer2Back(){ 
                this.$request({
                    url: "/hiddenDanger/securityOfficer2Back",
                    method: 'post',
                    data:  { task_id: this.taskId, procInstId: this.procInstId, comment: this.process_comment}
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            },
            securityOfficer2Replenish(){
                let fd = new FormData();
                fd.append('uuid',this.form.uuid);
                fd.append('year',this.form.year);
                fd.append('org_id',this.form.org_id);
                fd.append('duty_org_id',this.form.duty_org_id);
                fd.append('summary',this.form.summary);
                if(this.form.plan!=undefined)
                    fd.append('plan',this.form.plan);
                fd.append('expires',this.form.expires);
                fd.append('principal',this.form.principal);
                fd.append('status',2);
                fd.append('discovery_time',this.form.discovery_time);
                fd.append('level',this.form.level);
                if(this.form.begin_deal!=null)
                    fd.append('begin_deal',this.form.begin_deal);
                if(this.form.end_deal!=null)
                    fd.append('end_deal',this.form.end_deal);
                fd.append('attachment1',this.file1);
                fd.append('fileList1',this.fileList1.length>0?'yes':'');//传给后台做是否删除用
                fd.append('attachment2',this.file2);
                fd.append('fileList2',this.fileList2.length>0?'yes':'');
                fd.append('attachment3',this.file3);
                fd.append('fileList3',this.fileList3.length>0?'yes':'');
                //workflow relate
                fd.append('task_id',this.taskId);
                fd.append('proc_inst_id',this.procInstId);
                fd.append('comment',this.process_comment); 

                this.$request({
                    url: "/hiddenDanger/securityOfficer2Replenish",
                    method: 'post',
                    data:  fd
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });

            },
            deptHead2Rearrange(){
                if(this.form.receiver==undefined||this.form.receiver==null||this.form.receiver==""){
                    this.$message({message: '请选择接收人！',type: 'warning'});
                    return;
                }
                this.$request({
                    url: "/hiddenDanger/deptHead2Rearrange",
                    method: 'post',
                    data:  { task_id: this.taskId, procInstId: this.procInstId, comment: this.process_comment, user_id: this.form.receiver}
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            },
            reporterAndHead2Pass(){
                //责任部门负责人和现场负责人确认,通过
                this.$request({
                    url: "/hiddenDanger/reporterAndHead2Pass",
                    method: 'post',
                    data:  { task_id: this.taskId, procInstId: this.procInstId, comment: this.process_comment}
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            },
            reporterAndHead2Refuse(){
                //责任部门负责人和现场负责人确认,不通过
                this.$request({
                    url: "/hiddenDanger/reporterAndHead2Refuse",
                    method: 'post',
                    data:  { task_id: this.taskId, procInstId: this.procInstId, comment: this.process_comment}
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            },
            securityDept2Pass(){
                this.$request({
                    url: "/hiddenDanger/securityDept2Pass",
                    method: 'post',
                    data:  { task_id: this.taskId, procInstId: this.procInstId, comment: this.process_comment}
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            },
            securityDept2Back(){
                if(this.form.targetNode==undefined||this.form.targetNode==null||this.form.targetNode==""){
                    this.$message({message: '请选择退回目标节点！',type: 'warning'});
                    return;
                }
                var params = { task_id: this.taskId, procInstId: this.procInstId, comment: this.process_comment};
                var target = this.form.targetNode.split('@');
                params['prepare1'] = target[0];//targetNode
                params['prepare2'] = target[1];//replenish
                this.$request({
                    url: "/hiddenDanger/securityDept2Back",
                    method: 'post',
                    data:  params
                }).then(res => {
                    if(res.data.result_code == 0){
                        this.$message({message: '提交成功！',type: 'success'});
                        this.resetForm();
                        this.close();
                    }
                    else{ 
                        this.$message({message: res.data.result_desc,type: 'error'});
                    }
                });
            }
        },
        watch:{
            fileListA:{
                handler(newValue,oldValue){
                    this.fileList1 = newValue;
                }
            },
            fileListB:{
                handler(newValue,oldValue){
                    this.fileList2 = newValue;
                }
            },
            fileListC:{
                handler(newValue,oldValue){
                    this.fileList3 = newValue;
                }
            },
            usersSelect:{
                handler(newValue,oldValue){
                    this.users=newValue;
                }
            }
        }
    };
</script>