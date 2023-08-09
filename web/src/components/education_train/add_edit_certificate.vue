<template>
    <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="姓名" prop="user_id"> 
            <el-input type="text" v-model="form.user_id"></el-input>
        </el-form-item> 
        <el-form-item label="职位" prop="job_position"> 
            <el-input type="text" v-model="form.job_position"></el-input>
        </el-form-item>
        <el-form-item label="资格类型" prop="qualification_type"> 
            <el-input type="text" v-model="form.qualification_type"></el-input>
        </el-form-item> 
        <el-form-item label="证书编号" prop="certificate_no"> 
            <el-input type="text" v-model="form.certificate_no"></el-input>
        </el-form-item> 
        <el-form-item label="证书到期日期" prop="expiration_date"> 
            <el-date-picker v-model="form.expiration_date" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
        </el-form-item> 
        <el-form-item label="初领证时间" prop="got_date"> 
            <el-date-picker v-model="form.got_date" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
        </el-form-item> 
        <el-form-item label="再培训时间" prop="retraining_date">
            <el-date-picker v-model="form.retraining_date" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
        </el-form-item> 
        <el-form-item label="复审时间" prop="recheck_date"> 
            <el-date-picker v-model="form.recheck_date" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
        </el-form-item> 
        <el-form-item label="发证机关" prop="licence_authority"> 
            <el-input type="text" v-model="form.licence_authority"></el-input>
        </el-form-item> 
        <el-form-item>
            <el-button type="primary" @click="onSubmit('form')" v-if="addSaveBtn">立即添加</el-button>
            <el-button type="primary" @click="editSave('form')" v-if="editSaveBtn">保存修改</el-button>
            <el-button @click="resetForm('form')" v-if="addSaveBtn">重置</el-button>
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
                rules: {
                    user_id: [
                        { required: true, message: '姓名', trigger: 'blur' }
                    ],  
                    job_position: [
                        { required: true, message: '职位', trigger: 'blur' }
                    ], 
                    qualification_type: [
                        { required: true, message: '资格类型', trigger: 'blur' }
                    ], 
                    certificate_no: [
                        { required: true, message: '证书编号', trigger: 'blur' }
                    ], 
                    expiration_date: [
                        { required: true, message: '证书到期日期', trigger: 'blur' }
                    ]
                }
            };
        },
        methods: {
            close(){ 
                this.$emit('closeAddEdit', '');
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$request({
                            url: '/staffCertificate/save',
                            method: 'post',
                            data: this.form
                        }).then(res => {
                            this.$message({
                                message: '添加成功！',
                                type: 'success'
                            });
                            this.resetForm('form');
                            this.close();
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
                            url: '/staffCertificate/save',
                            method: 'post',
                            data: this.form
                        }).then(res => { 
                            this.$message({
                                message: '修改成功！',
                                type: 'success'
                            });
                            this.resetForm('form');
                            this.close();
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