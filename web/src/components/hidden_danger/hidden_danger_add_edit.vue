<template>
    <div>
        <el-form :model="form" :rules="rules" ref="form" label-width="120px">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="年份" prop="year">
                        <el-input-number v-model="form.year" controls-position="right"></el-input-number> 
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="责任部门/单位" prop="duty_org_name">
                        <el-input v-model="form.duty_org_name" readonly= "true" placeholder="请选择责任部门"> 
                            <el-button slot="append" icon="el-icon-search" @click="chooseOrg"></el-button>
                            </el-input>
                        </el-input>
                     </el-form-item>
                </el-col> 
                <el-col :span="6"> 
                     <el-form-item label="接收安全员"> 
                        <el-select v-model="form.receiver" placeholder="请选择责任安全员">
                           <el-option v-for="item in users" :key="item.id" :label="item.truename" :value="item.id">
                            </el-option>
                        </el-select>
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
                        <el-upload :on-change='changeUpload1' ref="attachment1" :http-request="req1" :before-remove="beforeRemove1" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList1" :show-file-list="true" action="">
                            <el-button size="small" type="primary">点击上传</el-button> 
                        </el-upload>
                        <img ref="img1" :style="'width:100px;display:'+(checkIsImg(form.attachment1!=null ? form.attachment1.substr(form.attachment1.lastIndexOf('\\')+1) : '')?'':'none')" :src="'/api/file/download?filePath='+ encodeURI(encodeURI(form.attachment1))"/>       
                  
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
                        <el-upload :on-change='changeUpload2' ref="attachment2" :http-request="req2" :before-remove="beforeRemove2" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList2" :show-file-list="true" action="">
                            <el-button size="small" type="primary">点击上传</el-button> 
                        </el-upload>
                        <img ref="img2" :style="'height:100px;display:'+(checkIsImg(form.attachment2!=null ? form.attachment2.substr(form.attachment2.lastIndexOf('\\')+1) : '')?'':'none')" :src="'/api/file/download?filePath='+ encodeURI(encodeURI(form.attachment2))"/>       
                    </el-form-item>
                 </el-col>
                <el-col :span="12">
                    <el-form-item label="治理结束时间" prop="end_deal">
                        <el-date-picker v-model="form.end_deal" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
                    </el-form-item>
                </el-col>  
            </el-row>  
            <el-form-item align="center">
                <el-button type="primary" @click="submitForm('form')" v-if="addSaveBtn">添加</el-button>
                <el-button type="primary" @click="updateSave" v-if="editSaveBtn">保存修改</el-button>
                <el-button type="primary" @click="startProcess">报送责任部门安全员</el-button>
                <el-button @click="resetForm" v-if="addSaveBtn">重置</el-button>
            </el-form-item>
    	</el-form> 

        <el-dialog :visible.sync="orgSelectPage" width="20%" title="责任部门选择" append-to-body>
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
        props:['formDetail','fileListA','fileListB','fileListC','usersSelect','addSaveBtn','editSaveBtn'], 
        components:{org4Select,user4Select},
        data(){
            return {
                form: this.formDetail, 
                fileList1: this.fileListA,
                fileList2: this.fileListB,
                fileList3: this.fileListC,
                users: this.usersSelect,
                rules:{
                    year: [
                        {required: true, message: '年份', trigger: 'blur'}
                    ],
                    duty_org_name:[
                        {required: true, message: '隐患责任部门', trigger: 'blur'}
                    ],
                    summary: [
                        {required: true, message: '隐患概要信息', trigger: 'blur'},
                        {max: 100, message: '长度小于100个字符', trigger: 'blur'}
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
                userChoosePage: false
            };
        },
        methods:{
            changeUpload1: function(file, fileList) {  
                if(this.checkIsImg(file.name)){
                    this.$refs.img1.style.display = '';
                    this.$refs.img1.src = file.url;
                }
            },
            changeUpload2: function(file, fileList) {  
                if(this.checkIsImg(file.name)){
                    this.$refs.img2.style.display = '';
                    this.$refs.img2.src = file.url;
                }
            },
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
                this.form.attachment1 = null;
                this.file1 = null; 
                this.fileList1 = []; 

                //delete pic preview
                this.$refs.img1.style.display = 'none';
                this.$refs.img1.src = null;
            },
            beforeRemove2(file, fileList){ 
                this.form.attachment2 = null;
                this.file2 = null;
                this.fileList2 = [];

                //delete pic preview
                this.$refs.img2.style.display = 'none';
                this.$refs.img2.src = null;
            },
            beforeRemove3(file, fileList){ 
                this.file3 = null;
                this.fileList3 = [];
            },
            handleChange(file, fileList) {
                
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let fd = new FormData();
                        fd.append('year',this.form.year);
                        fd.append('org_id',this.form.org_id);
                        fd.append('duty_org_id',this.form.duty_org_id);
                        fd.append('summary',this.form.summary);
                        fd.append('plan',this.form.plan);
                        fd.append('expires',this.form.expires);
                        fd.append('principal',this.form.principal);
                        fd.append('status',this.form.status);
                        fd.append('discovery_time',this.form.discovery_time);
                        fd.append('level',this.form.level);
                        if(this.form.begin_deal!=null)
                            fd.append('begin_deal',this.form.begin_deal);
                        if(this.form.end_deal!=null)
                            fd.append('end_deal',this.form.end_deal);
                        fd.append('attachment1',this.file1);
                        fd.append('attachment2',this.file2); 
                        fd.append('attachment3',this.file3); 
                        this.$request({
                            url: this.api.URL_save,
                            method: 'post',
                            data: fd
                        }).then(res => { 
                            if(res.data.result_code == 0){
                                this.$message({message: '添加成功！',type: 'success'});
                                this.resetForm();
                                this.close();
                            }
                            else{ 
                                this.$message({message: res.data.result_desc,type: 'error'});
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            updateSave(){
                this.$refs['form'].validate((valid) => {
                    if (valid) {
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
                        fd.append('status',this.form.status);
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
                        this.$request({
                            url: this.api.URL_save,
                            method: 'post',
                            data: fd
                        }).then(res => { 
                            if(res.data.result_code == 0){
                                this.$message({message: '修改成功！',type: 'success'});
                                this.resetForm();
                                this.close();
                            }
                            else{ 
                                this.$message({message: res.data.result_desc,type: 'error'});
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            startProcess(){
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        if(this.form.receiver==undefined||this.form.receiver==null||this.form.receiver==''){
                            this.$message({message: '请选择接收隐患报告单的安全员！',type: 'warning'});
                            return;
                        }
                        let fd = new FormData();
                        fd.append('uuid',this.form.uuid);
                        fd.append('year',this.form.year);
                        fd.append('org_id',this.form.org_id);
                        fd.append('org_name',this.form.org_name);
                        fd.append('duty_org_id',this.form.duty_org_id);
                        fd.append('summary',this.form.summary);
                        if(this.form.plan!=undefined)
                            fd.append('plan',this.form.plan); 
                        fd.append('expires',this.form.expires);
                        fd.append('principal',this.form.principal);
                        fd.append('status',2);//启动流程，状态设为处理中
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
                        fd.append('receiver',this.form.receiver);
                        this.$request({
                            url: '/hiddenDanger/startProcess',
                            method: 'post',
                            data: fd
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
                    } else {
                        return false;
                    }
                });
            },
            close(){
                this.$emit('closeAddEdit', '');
            },
            resetForm(){
                let formName = 'form';
                this.$refs[formName].resetFields();
                this.$refs.attachment1.clearFiles();
                this.$refs.img1.src = null;
                this.$refs.attachment2.clearFiles();
                this.$refs.img2.src = null;
                this.$refs.attachment3.clearFiles();
                this.file1 = null;
                this.file2 = null; 
                this.file3 = null; 
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
            }, 
            getObjectURL(file) {
                var url = null ;
                if(file!=undefined&&file!=null){
                    try{
                        // 下面函数执行的效果是一样的，只是需要针对不同的浏览器执行不同的 js 函数而已
                        if (window.createObjectURL!=undefined) { // basic
                            url = window.createObjectURL(file) ;
                        } else if (window.URL!=undefined) { // mozilla(firefox)
                            url = window.URL.createObjectURL(file) ;
                        } else if (window.webkitURL!=undefined) { // webkit or chrome
                            url = window.webkitURL.createObjectURL(file) ;
                        }
                    }
                    catch(e){
                        //console.log(e);
                    }
                }
                return url ;
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
                    this.users = newValue;
                }
            }
        }
    };
</script>

