<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="标题" prop="title">
            <el-input type="text" v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
            <el-input type="textarea" v-model="form.content" :rows="5"></el-input>
        </el-form-item>

         <el-form-item label="文件">
          <el-upload ref="attachment_path" :http-request="req" :before-remove="beforeRemove" :on-preview="preview" :on-exceed="exceed" :limit="1" :file-list="fileList" :show-file-list="true" action="">
              <el-button size="small" type="primary">点击上传</el-button> 
            </el-upload>
        </el-form-item>
        
 		<el-form-item align="center">
            <el-button type="primary" @click="submitForm('form')" v-if="addSaveBtn">添加</el-button>
            <el-button @click="resetForm" v-if="addSaveBtn">重置</el-button>
            <el-button type="primary" @click="updateSave" v-if="editSaveBtn">保存修改</el-button>
        </el-form-item>

    </el-form>	
  </div>
</template>

<script>
/* eslint-disable */
  export default{
  	props: ['detailForm','addSaveBtn','editSaveBtn','files'],
    data() {
      return{
        form: this.detailForm,
        fileList: this.files,
        rules: {
          title: [{required: true, message: '标题必填！', trigger: 'blur'},{max: 200, message: '最多可以输入200个字！', trigger: 'blur'}],
          content: [{required: true, message: '请输入内容！', trigger: 'blur'},{max: 500, message: '最多可以输入500个字！', trigger: 'blur'}]
        },
        file: null
      };
    },
    methods: {
      req(f){
        this.file = f.file;  
        this.fileList.push({name:f.file.name,url:''});
      },
      beforeRemove(file, fileList){ 
        this.file = null; 
        this.fileList = []; 
      },
      preview(fileInfo){ 
        if(fileInfo.url!=""){
            let url = '/file/download/' + encodeURI(encodeURI(fileInfo.url)); 
            this.$request({
                url: url,
                responseType: 'blob'
            }).then(res => { 
                let url = window.URL.createObjectURL(new Blob([res.data]))
                let link = document.createElement('a')
                link.style.display = 'none'
                link.href = url
                link.setAttribute('download', fileInfo.name); 
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            });
        } 
      },
      exceed(files, fileList){
            if(files.length+fileList.length>1){
                    this.$message({message:'请先删除原附件再上传！',type:'warning'});
                    return false;
                }    
      },
      submitForm(formName){
        this.$refs[formName].validate((valid) => {
            if (valid) {
                let fd = new FormData();
                fd.append('title',this.form.title);
                fd.append('content',this.form.content);
                fd.append('attachment_path',this.file); 
                this.$request({
                    url: '/announcement/add',
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
                    fd.append('title',this.form.title);
                    fd.append('content',this.form.content);
                    fd.append('attachment_path',this.file); 
                    fd.append('org_id',this.form.org_id);
                    fd.append('fileList',this.fileList.length>0?'yes':'');//传给后台做是否删除用 
                    this.$request({
                        url: '/announcement/update',
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
      resetForm(){
        let formName = 'form';
        this.$refs[formName].resetFields();
        this.$refs.attachment_path.clearFiles(); 
        this.form.title = null;
        this.form.content = null;
        this.file = null;
      },
      close(){
        this.$emit('closeAddEdit', '');
      }
    },
    watch:{
    	files:{
    		handler(newvalue,oldvalue){  
        	this.fileList = newvalue; 
        }
    	}
    } 
  };
</script>