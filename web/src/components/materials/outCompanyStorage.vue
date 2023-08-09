<template>
    <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px"> 
            <el-row>
              <el-col :span="12">
                    <el-form-item label="型号" prop="model_id">
                        <el-select v-model="form.model_id" placeholder="请选择型号" @change="onChangeModel">
                            <el-option v-for="item in modelList" :key="item.id" :label="item.model_name" :value="item.id">
                            </el-option>
                        </el-select> 
                    </el-form-item> 
                </el-col>
                <el-col :span="12"> 
                    库存：{{invetory_num}}
                </el-col>
            </el-row> 

            <el-row>
                <el-col :span="12">
                    <el-form-item label="出库数量" prop="num"> 
                         <el-input-number v-model="form.num" :min="0" ></el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="12"> 
                      <el-form-item label="出库时间" prop="time_in_out">
                        <el-date-picker v-model="form.time_in_out" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-row> 

 

            <el-row>
                <el-form-item label="备注" prop="remark">
                    <el-input type="textarea" v-model="form.remark"></el-input>
                </el-form-item>
            </el-row>
 

            <el-form-item align="center">
                <el-button type="primary" @click="save">保存</el-button> 
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
/* eslint-disable */ 
    export default{
        props: ["modelList"],
        data() {
            return {
                form: {model_id:null},
                rules: { 
                    model_id: [{required: true, message: '请选择型号', trigger: 'blur'}],
                    num: [{required: true, message: '请输入数量', trigger: 'blur'}],
                    time_in_out: [
                        {required: true, message: '出库时间', trigger: 'blur'}
                    ]
                },
                invetory_num: 0
            };
        },
        methods: { 
            save(){
                this.$refs['form'].validate((valid) => {
                    if (valid) { 
                        var r = /^\+?[1-9][0-9]*$/;　　//正整数 
                        if(!r.test(this.form.num)){
                            this.$message({message: '出库数量至少为1件且为整数！',type: 'warning'});
                            return false;
                        }

                        if(this.invetory_num>0 && this.invetory_num >= this.form.num){
                            this.$request({
                                url: '/materialInventory/outCompanyStorage',
                                method: 'post',
                                data: this.form
                            }).then(res => { 
                                if(res.data.result_code == 0){
                                    this.$message({message: '出库成功！',type: 'success'});
                                    this.resetForm();
                                    this.close();
                                }
                                else{ 
                                    this.$message({message: res.data.result_desc,type: 'error'});
                                }
                            });
                        }
                        else{ 
                            this.$message({message: '无库存或库存不够！',type: 'warning'});
                        }
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
            },
            onChangeModel(value){
                //获取公司当前型号物品库存 
                this.$request({
                    url: '/materialInventory/getCompanyModelInventory/'+value,
                    method: 'get'
                }).then(res => { 
                    this.form.num = undefined;
                    if(res.data!=undefined && res.data.result_data!=undefined && res.data.result_data.num!=undefined)
                        this.invetory_num = res.data.result_data.num;
                    else
                        this.invetory_num = 0;
                });
            }
        },
        watch:{
            modelList(value,oldvalue){ 
                this.form.model_id = null;
                this.form.remark = null;
                this.invetory_num = 0;
            } 
        }
    };
</script>