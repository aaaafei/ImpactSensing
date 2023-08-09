<template>
    <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="巡查项内容" prop="content">
            <el-input type="textarea" v-model="form.content"></el-input>
        </el-form-item>
        <el-form-item label="检查结果待选项" prop="options">
            <el-input type="text" v-model="form.options" placeholder="如：是;否、有;无、完好;不完好，“;”分隔"></el-input>
        </el-form-item>
        <el-form-item label="排序号">
          <el-input-number controls-position="right" v-model="form.sort_num" :precision="0" :step="1" :min="0"></el-input-number>（注：值越大排列越靠前）
        </el-form-item> 
        <el-form-item>
            <el-button type="primary" @click="onSubmit('form')" v-if="addSaveBtn">立即添加</el-button>
            <el-button type="primary" @click="editSave('form')" v-if="editSaveBtn">保存修改</el-button>
            <el-button @click="resetForm('form')">重置</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
/* eslint-disable */ 
    export default{
        props: ['detailForm','addSaveBtn','editSaveBtn'],
        data(){
            return{
                form: this.detailForm,
                api:{
                    URL_add:'/riskSourcePatrolContent/add', 
                    URL_update:'/riskSourcePatrolContent/update'
                },
                rules: {
                    content: [
                        { required: true, message: '巡查项内容', trigger: 'blur' },
                        { max: 200, message: '长度小于200个字符', trigger: 'blur' }
                    ], 
                    options: [
                        { required: true, message: '检查结果待选项', trigger: 'blur' },
                        { max: 200, message: '长度小于50个字符', trigger: 'blur' }
                    ]
                }
            };
        },
        methods: {
            close(){ 
                this.$emit('closeContentAddEdit', '');
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$request({
                            url: this.api.URL_add,
                            method: 'post',
                            data: this.form
                        }).then(res => {
                            this.close();
                            this.$message({
                            message: '添加成功！',
                            type: 'success'
                            });
                        });
                    } else {
                        return false;
                    }
                });
            },
            editSave(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$request({
                            url: this.api.URL_update,
                            method: 'post',
                            data: this.form
                        }).then(res => {
                            this.close();
                            this.$message({
                            message: '修改成功！',
                            type: 'success'
                            });
                        });
                    } else {
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    };
</script>