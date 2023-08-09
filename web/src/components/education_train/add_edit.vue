<template>
    <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
            <el-form-item label="培训主题" prop="train_theme">
                <el-input type="text" v-model="form.train_theme"></el-input> 
            </el-form-item>
            <el-form-item label="培训内容" prop="train_content">
                <el-input type="text" v-model="form.train_content"></el-input> 
            </el-form-item>
            <el-form-item label="授课人/机构" prop="train_instructor">
                <el-input type="text" v-model="form.train_instructor"></el-input> 
            </el-form-item>
            <el-form-item label="受训人员" prop="trainee">
                <el-input type="text" v-model="form.trainee"></el-input> 
            </el-form-item>
            <el-form-item label="培训日期" prop="train_date">
                <el-date-picker v-model="form.train_date" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
            </el-form-item> 
            <el-form-item label="签到表">
                <el-upload ref="sign_attachment_path" :http-request="reqA" :before-remove="beforeRemoveA" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileListA" :show-file-list="true" action="">
                    <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
            </el-form-item>
            <el-form-item label="记录表">
                <el-upload ref="record_attachment_path" :http-request="reqB" :before-remove="beforeRemoveB" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileListB" :show-file-list="true" action="">
                    <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
            </el-form-item>
            <el-form-item label="评价表">
                <el-upload ref="evaluation_attachment_path" :http-request="reqC" :before-remove="beforeRemoveC" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileListC" :show-file-list="true" action="">
                    <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
            </el-form-item>
            <el-form-item align="center">
                <el-button type="primary" @click="add" v-if="addSaveBtn">添加</el-button>
                <el-button @click="resetForm" v-if="addSaveBtn">重置</el-button>
                <el-button type="primary" @click="update" v-if="editSaveBtn">保存修改</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
/* eslint-disable */
    import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil';
    export default{
        props: ["detailForm", "addSaveBtn", "editSaveBtn", "fileListA", "fileListB", "fileListC"],
        data() {
            return {
                form: this.detailForm,
                fileList: this.files,
                rules: {
                    train_theme: [{required: true, message: '培训题目！', trigger: 'blur'}],
                    train_content: [{required: true, message: '培训内容！', trigger: 'blur'}],
                    train_instructor: [{required: true, message: '授课人/机构！', trigger: 'blur'}],
                    trainee: [{required: true, message: '受训人员！', trigger: 'blur'}],
                    train_date: [{required: true, message: '培训日期！', trigger: 'blur'}]
                },
                fileA: null,
                fileB: null,
                fileC: null
            };
        },
        methods: {
            reqA(f){
                this.fileA = f.file;
                this.fileListA.push({name: f.file.name, url: ''});
            },
            beforeRemoveA(file,fileList){
                this.fileA = null;
                this.fileListA = [];
            },
            reqB(f){
                this.fileB = f.file;
                this.fileListB.push({name: f.file.name, url: ''});
            },
            beforeRemoveB(file,fileList){
                this.fileB = null;
                this.fileListB = [];
            },
            reqC(f){
                this.fileC = f.file;
                this.fileListC.push({name: f.file.name, url: ''});
            },
            beforeRemoveC(file,fileList){
                this.fileC = null;
                this.fileListC = [];
            },
            preview(fileInfo){
                if(fileInfo.url!=""){
                    fileDownloadUtil(fileInfo.url);
                }
            },
            exceed(files,fileList){
                if(files.length + fileList.length > 1){
                    this.$message({message:'请先删除原附件再上传！', type:'warning'});
                    return false;
                }
            },
            add(){
                this.$refs['form'].validate((valid) => {
                    if (valid) {  
                        let fd = new FormData();
                        fd.append('train_theme',this.form.train_theme);
                        fd.append('train_content',this.form.train_content);
                        fd.append('train_instructor',this.form.train_instructor); 
                        fd.append('trainee',this.form.trainee);
                        fd.append('train_date',this.form.train_date);
                        fd.append('sign_attachment_path',this.fileA); 
                        fd.append('record_attachment_path',this.fileB);
                        fd.append('evaluation_attachment_path',this.fileC);  
                        this.$request({
                            url: '/educationTrain/save',
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
            resetForm(){ 
                this.$refs['form'].resetFields();
                this.$refs.sign_attachment_path.clearFiles(); 
                this.fileA = null;
                this.$refs.record_attachment_path.clearFiles(); 
                this.fileB = null;
                this.$refs.evaluation_attachment_path.clearFiles(); 
                this.fileC = null;
            },
            update(){
                this.$refs['form'].validate((valid) => {
                    if (valid) {  
                        let fd = new FormData();
                        fd.append('uuid',this.form.uuid);
                        fd.append('train_theme',this.form.train_theme);
                        fd.append('train_content',this.form.train_content);
                        fd.append('train_instructor',this.form.train_instructor); 
                        fd.append('trainee',this.form.train_theme);
                        fd.append('train_date',this.form.train_date);
                        fd.append('sign_attachment_path',this.fileA); 
                        fd.append('fileList1',this.fileListA.length>0?'yes':'');//传给后台做是否删除用 
                        fd.append('record_attachment_path',this.fileB);
                        fd.append('fileList2',this.fileListB.length>0?'yes':'');//传给后台做是否删除用 
                        fd.append('evaluation_attachment_path',this.fileC);  
                        fd.append('fileList3',this.fileListC.length>0?'yes':'');//传给后台做是否删除用 
                        this.$request({
                            url: '/educationTrain/save',
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
            close(){
                this.$emit("closeAddEdit","");
            }
        },
        watch: {
            files: {
                handler(newValue,oldValue){
                    this.fileList = newValue;
                }
            }
        }
    }
</script>