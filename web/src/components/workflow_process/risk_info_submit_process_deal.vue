<template>
    <div>
        <el-form ref="form" :model="form" label-width="130px">
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

            <el-row>
                <el-form-item label="处理意见"> 
                    <el-input type="textarea" v-model="process_comment"></el-input>
                </el-form-item>
            </el-row>
            <el-form-item align="center">
                <el-button type="primary" @click="auditPass" v-if="actStep=='riskInfoAudit'">通过</el-button> 
                <el-button type="primary" @click="auditRefuse" v-if="actStep=='riskInfoAudit'">不通过</el-button> 
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
/* eslint-disable */ 
    export default{
        props: ["detailForm",'procInstId','taskId','actStep'],
        data() {
            return {
                form: this.detailForm,
                process_comment: null
            };
        },
        methods: {  
            resetForm(){ 
                this.$refs['form'].resetFields(); 
                this.process_comment = null;
            },
            auditPass(){
                this.$request({
                    url: '/riskInfoSubmit/auditPass',
                    method: 'post',
                    data: {task_id: this.taskId, procInstId: this.procInstId, comment: this.process_comment, approve: true}
                }).then(res => { 
                    if(res.data.result_code==0)
                        this.$message({message: '处理成功！',type: 'success'});
                    else
                        this.$message({message: '处理出错！',type: 'error'});
                    this.resetForm();
                    this.close();
                }); 
            },
            auditRefuse(){
                this.$request({
                    url: '/riskInfoSubmit/auditRefuse',
                    method: 'post',
                    data: {task_id: this.taskId, procInstId: this.procInstId, comment: this.process_comment, approve: false}
                }).then(res => { 
                    if(res.data.result_code==0)
                        this.$message({message: '处理成功！',type: 'success'});
                    else
                        this.$message({message: '处理出错！',type: 'error'});
                    this.resetForm();
                    this.close();
                }); 
            },
            close(){
                this.$emit("closeProcess","");
            }
        }
    }
</script>