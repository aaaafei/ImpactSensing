<template>
    <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
            <el-form-item label="主题" prop="title">
                <el-input type="text" v-model="form.title"></el-input> 
            </el-form-item>
            <el-form-item label="内容" prop="content">
                <el-input type="textarea" v-model="form.content" rows="6"></el-input>
            </el-form-item>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="报送时间" prop="submit_time">
                        <el-date-picker v-model="form.submit_time" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="同步到集团" prop="syn_status"> 
                        <el-radio-group v-model="form.syn_status">
                            <el-radio :label="0">无需同步</el-radio>
                            <el-radio :label="1">未同步</el-radio>
                            <el-radio :label="2">已同步</el-radio>
                        </el-radio-group>
                        <p>(注：同步将在审批通过后由后台自动执行)</p>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-form-item align="center">
                <el-button type="primary" @click="save" v-if="form.check_status==null||form.check_status==0">保存</el-button>
                <el-button @click="resetForm" v-if="form.check_status==null">重置</el-button>
                <el-button type="primary" @click="sendAudit" v-if="form.check_status==null||form.check_status==0">送审批</el-button> 
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
                    title: [{required: true, message: '主题！', trigger: 'blur'}, {max: 100, message: '最多输入100个字符', trigger: 'blur' }],
                    content: [{required: true, message: '内容！', trigger: 'blur'}],
                    submit_time: [{required: true, message: '报送时间！', trigger: 'blur'}]
                }
            };
        },
        methods: { 
            save(){
                this.$refs['form'].validate((valid) => {
                    if (valid) { 
                        this.$request({
                            url: '/riskInfoSubmit/save',
                            method: 'post',
                            data: this.form
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
            },
            sendAudit(){
                this.$refs['form'].validate((valid) => {
                    if (valid) { 
                        this.$request({
                            url: '/riskInfoSubmit/sendAudit',
                            method: 'post',
                            data: this.form
                        }).then(res => { 
                            this.$message({message: '提交成功！',type: 'success'});
                            this.resetForm();
                            this.close();
                        });
                    } else {
                        return false;
                    }
                });
            },
            close(){
                this.$emit("closeAddEdit","");
            }
        }
    }
</script>