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
                    当前库存：{{invetory_num}}
                </el-col>
            </el-row> 

            <el-row>
                <el-col :span="12">
                    <el-form-item label="修改成库存" prop="num"> 
                        <el-input-number v-model="form.num" :min="0"></el-input-number>
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
        props: ["modelList","deptid"],
        data() {
            return {
                form: {model_id:null},
                rules: { 
                    model_id: [{required: true, message: '请选择型号', trigger: 'blur'}],
                    num: [{required: true, message: '请输入数量', trigger: 'blur'}]
                },
                invetory_num: 0,
                org_id: this.deptid
            };
        },
        methods: { 
            save(){
                this.$refs['form'].validate((valid) => {
                    if (valid) {  
                        var r = /^\d+$/;　　//非负整数 
                        if(!r.test(this.form.num)){
                            this.$message({message: '库存数量应为非负整数 ！',type: 'warning'});
                            return false;
                        }
                        this.form['dept_id'] = this.org_id[this.org_id.length-1];
                        
                        this.$request({
                            url: '/materialInventory/changeDeptModelInventory',
                            method: 'post',
                            data: this.form
                        }).then(res => { 
                            if(res.data.result_code == 0){
                                this.$message({message: '库存修改成功！',type: 'success'});
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
            },
            onChangeModel(value){
                var url = '/materialInventory/getDeptModelInventory/'+value;
                url += '/' + this.org_id[this.org_id.length-1];
                //获取公司当前型号物品库存 
                this.$request({
                    url: url,
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
            },
            deptid(value,oldvalue){
                this.org_id = value;
            }
        }
    };
</script>