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
                    <el-form-item label="责任部门/单位" prop="duty_org_name">
                        <el-input v-model="form.duty_org_name" readonly  placeholder="请选择责任部门"> 
                            <el-button slot="append" icon="el-icon-search" disabled></el-button> 
                        </el-input>
                     </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="上报部门"> 
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
                    <el-input type="textarea" v-model="form.summary" readonly ></el-input>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item label="整改措施/计划" prop="plan">
                    <el-input type="textarea" v-model="form.plan" readonly ></el-input>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item>
                    <el-upload disabled ref="attachment3" :http-request="req3" :before-remove="beforeRemove3" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList3" :show-file-list="true" action="">
                        <el-button disabled size="small" type="primary">点击上传</el-button> 
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
                        <el-date-picker v-model="form.discovery_time" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间" readonly ></el-date-picker>
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
                    <el-upload disabled ref="attachment1" :http-request="req1" :before-remove="beforeRemove1" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList1" :show-file-list="true" action="">
                        <el-button disabled size="small" type="primary">点击上传</el-button> 
                    </el-upload>
                    <img v-if="checkIsImg(form.attachment1!=null ? form.attachment1.substr(form.attachment1.lastIndexOf('\\')+1) : '')" :src="'/api/file/download?filePath='+ encodeURI(encodeURI(form.attachment1))" style="width:100px;" />       
                </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="治理开始时间" prop="begin_deal">
                        <el-date-picker v-model="form.begin_deal" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" readonly ></el-date-picker>
                    </el-form-item>
                </el-col>  
            </el-row>
            <el-row>
                 <el-col :span="12">
                    <el-form-item label="整改后附件">  
                        <el-upload disabled ref="attachment2" :http-request="req2" :before-remove="beforeRemove2" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList2" :show-file-list="true" action="">
                            <el-button disabled size="small" type="primary">点击上传</el-button> 
                        </el-upload>
                        <img v-if="checkIsImg(form.attachment2!=null ? form.attachment2.substr(form.attachment2.lastIndexOf('\\')+1) : '')" :src="'/api/file/download?filePath='+ encodeURI(encodeURI(form.attachment2))" style="width:100px;" />       
                    </el-form-item>
                 </el-col>
                <el-col :span="12">
                    <el-form-item label="治理结束时间" prop="end_deal">
                        <el-date-picker v-model="form.end_deal" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" readonly ></el-date-picker>
                    </el-form-item>
                </el-col>  
            </el-row> 
            
             <el-row  v-if="securityDeptRearrange">
                <el-form-item label="处理意见"> 
                    <el-input type="textarea" v-model="process_comment"></el-input>
                </el-form-item>
                <el-form-item align="center">
                    <el-button type="primary" @click="arrangeRecheck">重新指派人员复查</el-button> 
                    <el-select v-model="arrangeReceiver" placeholder="请选择复查人员">
                        <el-option v-for="item in securitDeptUsers" :label="item.truename" :value="item.id" :key="item.id"></el-option>            
                    </el-select> 
                </el-form-item>
            </el-row>
         
    	</el-form> 

 

    </div>
</template>

<script>
/* eslint-disable */ 
import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
    export default {
        props:['formDetail','fileListA','fileListB','fileListC','securityDeptRearrange','taskId'],  
        data(){
            return {
                form: this.formDetail, 
                fileList1: this.fileListA,
                fileList2: this.fileListB,
                fileList3: this.fileListC,
                rearrange: this.securityDeptRearrange,
                arrangeReceiver:null,
                securitDeptUsers:[],
                taskId4Arrange: this.taskId,
                process_comment: null,
                rules:{
                    year: [
                        {required: true, message: '年份' }
                    ],
                    duty_org_name:[
                        {required: true, message: '隐患责任部门' }
                    ],
                    summary: [
                        {required: true, message: '隐患概要信息' },
                        {max: 100, message: '长度小于100个字符' }
                    ], 
                    discovery_time: [
                        {required: true, message: '发现时间' }
                    ],
                    expires: [
                        {required: true, message: '治理期限' }
                    ],
                    status: [
                        {required: true, message: '治理状态' }
                    ],
                    level: [
                        {required: true, message: '隐患等级' }
                    ]
                },
                api:{
                    URL_save: '/hiddenDanger/save/', 
                    URL_downloadFile: '/file/download/'
                }, 
                file1: null,
                file2: null,
                file3: null,
                selExpires: null 
            };
        },
        methods:{
            onChangeExpires(value){
                if(value!='') 
                  this.form.expires = value;
                else
                  this.form.expires = undefined;
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
            handleChange(file, fileList) {
                
            }, 
            close(){
                this.$emit('closeView', '');
            },
            resetForm(){
                let formName = 'form';
                this.$refs[formName].resetFields();
                this.$refs.attachment1.clearFiles();
                this.$refs.attachment2.clearFiles();
                this.file1 = null;
                this.file2 = null; 
                this.process_comment = null;
                this.arrangeReceiver = null;
            },
            arrangeRecheck(){
                if(this.arrangeReceiver == undefined || this.arrangeReceiver == null || this.arrangeReceiver == ""){
                    this.$message({message: '请选择重新指派的复查人员！',type: 'warning'});
                    return;
                }
                var params = { task_id: this.taskId, procInstId: this.form.proc_inst_id, comment: this.process_comment,prepare1: this.arrangeReceiver}; 
                this.$request({
                    url: "/hiddenDanger/arrangeRecheck",
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
            },
            checkIsImg(fileName){ 
                var isImg = false;
                var suffix = fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase(); 
                var imgFormat = ['bmp','jpg','jpeg','png','gif'];
                imgFormat.forEach(row => { 
                    if(suffix==row){ 
                    isImg = true; 
                    }
                });
                return isImg;
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
            }
        },
        mounted: function(){
            let params = {};
            params.organid = 61;
            this.$request({
              url: '/user/getUserList',
              method: 'post',
              data: params
            }).then(res => {
              this.securitDeptUsers = res.data.result_data;   
          });
        }
    };
</script>

