<template>
<div>
   <el-form :model="form" :rules="rules" ref="form" label-width="120px"> 
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="管理责任人" prop="administrator_name">
                            <el-input placeholder="请选择责任人" v-model="form.administrator_name" class="input-with-select" :readonly= "true"> 
                                <el-button slot="append" icon="el-icon-search" @click="chooseUser"></el-button>
                            </el-input>
                        </el-form-item> 
                    </el-col>
                      <el-col :span="12">
                        <el-form-item label="巡查周期" prop="administrator_period">
                          <el-select v-model="form.administrator_period" placeholder="请选择巡查周期" :clearable="true">
                            <el-option label="季度" value="季度"></el-option>
                            <el-option label="月" value="月"></el-option>
                            <el-option label="半月" value="半月"></el-option>
                            <el-option label="周" value="周"></el-option> 
                            <el-option label="天" value="天"></el-option> 
                          </el-select> 
                        </el-form-item> 
                      </el-col> 
                </el-row> 
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="启动巡检任务">
                            <el-radio-group v-model="form.enabled">
                                <el-radio label="1">是</el-radio>
                                <el-radio label="0">否</el-radio> 
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                          <el-form-item label="任务开始时间"  >
                            <el-date-picker v-model="form.start_time" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
                          </el-form-item> 
                    </el-col>
                </el-row>
                <el-row>  
                    <el-col :span="24">
                        <el-form-item label="备注">
                        <el-input type="textarea" v-model="form.remark" maxlength="100" show-word-limit></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form> 
            <el-row>  
                <div style="text-align:center;padding-top:10px;">
                    <el-button type="primary" @click="save" v-if="op=='add'">立即添加</el-button>
                    <el-button type="primary" @click="save" v-if="op=='edit'">保存修改</el-button>
                    <el-button @click="resetForm">重置</el-button>
                </div>
            </el-row>

        <el-dialog :visible.sync="userChoosePage" width="20%" title="人员选择" append-to-body>
            <user4Select @closeUseSel="closeUseSel" :limitPermision="0"></user4Select>
        </el-dialog>

    </div>
</template>

<script>
/* eslint-disable */ 
    import user4Select from './user4Select.vue';
    export default{
        props: ['detailForm','op'],
        components:{user4Select},
        data(){
            return{
                form: this.detailForm,
                rules: {
                    administrator_name: [
                    { required: true, message: '管理责任人', trigger: 'blur' }
                  ],
                  administrator_period: [
                    { required: true, message: '请选择巡查周期', trigger: 'change' }
                  ] 
                },
                userChoosePage: false
            };
        },
        methods: {
            save(){  
                this.$refs['form'].validate((valid) => {
                  if (valid) {
                      if(this.form.enabled==1){
                        if(this.form.start_time==null){
                             this.$message({
                                type: 'warning',
                                message: '请设置任务开始时间！'
                            });
                             return false;
                        }
                      } 
 
                      this.$request({
                        url: '/riskSourcePersonLiable/save/',
                        method: 'post',
                        data: this.form
                      }).then(res => { 
                        if(res.data.result_code==0){
                            this.$message({
                              message: res.data.result_desc,
                              type: 'success'
                            });
                            this.close();
                        }
                        else{
                            this.$message({
                              message: res.data.result_desc,
                              type: 'error'
                            });
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
            resetForm() {
                this.$refs['form'].resetFields();
            },
            chooseUser(){
                this.userChoosePage = true; 
            }, 
            closeUseSel(name,id){
                this.userChoosePage = false;
                this.form.administrator_id = id;
                this.form.administrator_name = name;
            }
        }
    };
</script>