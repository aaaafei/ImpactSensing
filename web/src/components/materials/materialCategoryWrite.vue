<template>
    <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
            <el-form-item label="品类" prop="name">
                <el-input type="text" v-model="form.name"></el-input> 
            </el-form-item>
            <el-form-item label="序号" prop="sort_num">
                <el-input type="number" v-model="form.sort_num"></el-input>
            </el-form-item> 

            <el-form-item align="center">
                <el-button type="primary" @click="save" >保存</el-button> 
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
/* eslint-disable */ 
    export default{
        props: ["detailForm"],
        data() {
            return {
                form: this.detailForm,
                rules: {
                    name: [{required: true, message: '品类！', trigger: 'blur'}, {max: 100, message: '最多输入100个字符', trigger: 'blur' }]
                }
            };
        },
        methods: { 
            save(){
                this.$refs['form'].validate((valid) => {
                    if (valid) { 
                        let op_url;
                        if(this.form.id!=undefined && this.form.id!=null && this.form.id!='')
                            op_url = '/materialCategory/update';
                        else
                            op_url = '/materialCategory/add';
                        this.$request({
                            url: op_url,
                            method: 'post',
                            data: this.form
                        }).then(res => { 
                            if(res.data.result_code == 0){
                                this.$message({message: '保存成功！',type: 'success'});
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
            },
            close(){
                this.$emit("closeAddEdit","");
            }
        }
    }
</script>