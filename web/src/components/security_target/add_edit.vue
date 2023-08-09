<template>
    <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="150px">
            <el-form-item label="年度" prop="year">
                <el-input-number v-model="form.year" controls-position="right"></el-input-number>
            </el-form-item>
            <el-form-item label="目标／责任状">
                <el-upload ref="file_path" :http-request="req" :before-remove="beforeRemove" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList" :show-file-list="true" action="">
                    <el-button size="small" type="primary">点击上传</el-button>
                    (建议pdf格式)
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
        props: ["detailForm", "addSaveBtn", "editSaveBtn", "files"],
        data() {
            return {
                form: this.detailForm,
                fileList: this.files,
                rules: {
                    year: [{required: true, message: '年度必填！', trigger: 'blur'}]
                },
                file: null
            };
        },
        methods: {
            req(f){
                this.file = f.file;
                this.fileList.push({name: f.file.name, url: ''});
            },
            beforeRemove(file,fileList){
                this.file = null;
                this.fileList = [];
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
                        if(this.file==null){
                            this.$message({ type: 'warning', message: '请上传安全目标文档!' });
                            return false;
                        }

                        let fd = new FormData();
                        fd.append('year',this.form.year); 
                        fd.append('file_path',this.file); 
                        this.$request({
                            url: '/securityTarget/save',
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
                this.$refs.file_path.clearFiles(); 
                this.file = null;
            },
            update(){
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        if(this.fileList.length ==0 && this.file==null){
                            this.$message({ type: 'warning', message: '请上传月报!' });
                            return false;
                        }

                        let fd = new FormData();
                        fd.append('uuid',this.form.uuid);
                        fd.append('org_id',this.form.org_id);
                        fd.append('year',this.form.year);
                        fd.append('file_path',this.file);
                        fd.append('fileList',this.fileList.length>0?'yes':'');//传给后台做是否删除用 
                        this.$request({
                            url: '/securityTarget/save',
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