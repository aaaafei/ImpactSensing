
<template>
	<div>
		<el-form ref="form"   label-width="100px" :model="form" :rules="rules"  > 
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
                         <el-input-number v-model="form.num" :min="0"></el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="12"> 
                    <el-form-item label="所选部门：" prop="receive_dept"> 
						<el-input v-model="form.receive_dept" readonly placeholder="点击下方人员树选择部门"></el-input>
		  			</el-form-item>
                </el-col>
            </el-row> 

            <el-row>
                <el-col :span="12">
                    <el-form-item label="下发时间" prop="time_in_out">
                        <el-date-picker v-model="form.time_in_out" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="12"> 
                  
                </el-col>
            </el-row> 

            <el-row>
                <el-form-item label="备注" prop="remark">
                    <el-input type="textarea" v-model="form.remark"></el-input>
                </el-form-item>
            </el-row>
 
		
  			<el-row>
			 <div style="text-align:center;">
			<el-button type="primary" plain @click.native="save">确定</el-button> 
		</div>
		</el-row>
		</el-form>
		<div style="height:500px;overflow:auto;padding:15px;">
			<el-input
  placeholder="输入关键字进行过滤"
  v-model="filterText">
</el-input>
		 <el-tree  
		  v-loading="treeLoading"
		  :data="treeData"
		  node-key="id"
		  :props="defaultProps" 
		  highlight-current
		  :default-expanded-keys="[1]"
		  :filter-node-method="filterNode"
		  :expand-on-click-node="true"
		  ref="tree"
		  @node-click="sel"
		  style="display: inline-block;/*min-width: 11.8vw*/"> 
		</el-tree>
		</div>
		
	</div>
</template>

<script>
  /* eslint-disable */
	export default{
		props: ["modelList"],
		data(){
			return {
				form: {model_id:null,num:null,receive_dept:null,receive_dept_id:null,remark:null,time_in_out:null},
				invetory_num: 0,
				treeLoading:false,
				treeData: [],
				defaultProps: {
		          children: 'children',
		          label: 'label'
		        }, 
		        filterText: null,
                rules: { 
                    model_id: [{required: true, message: '请选择型号', trigger: 'blur'}],
                    num: [{required: true, message: '请输入数量', trigger: 'blur'}],
                    receive_dept: [{required: true, message: '请选部门', trigger: 'blur'}],
                    time_in_out: [
                        {required: true, message: '下发时间', trigger: 'blur'}
                    ]
                }
			};
		},
		 watch: {
	      	filterText(val) {
	        	this.$refs.tree.filter(val);
	      	},
	        modelList(value,oldvalue){ 
                this.form.model_id = null;
                this.invetory_num = 0;
                this.form.receive_dept = null;
                this.form.remark = null;
            } 
	    },
		methods:{
			filterNode (value, data) {  //  树结构的过滤函数
		        if (!value) return true;
		        return data.label.indexOf(value) !== -1;
	        },
	        sel (data) { 
		        this.form.receive_dept = data.label;
		        this.form.receive_dept_id = data.id;
		    },
		    save(){
                this.$refs['form'].validate((valid) => {
                    if (valid) { 
                        var r = /^\+?[1-9][0-9]*$/;　　//正整数 
                        if(!r.test(this.form.num)){
                            this.$message({message: '下发数量至少为1件且为整数！',type: 'warning'});
                            return false;
                        }

                        if(this.invetory_num>0 && this.invetory_num >= this.form.num){
                            this.$request({
                                url: '/materialInventory/outCompanyStorage',
                                method: 'post',
                                data: this.form
                            }).then(res => { 
                                if(res.data.result_code == 0){
                                    this.$message({message: '下发成功！',type: 'success'});
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
		    close(){ 
		    	this.$emit('closeAddEdit'); 
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
            },
            resetForm(){ 
                this.$refs['form'].resetFields(); 
            }
		}, 
	    mounted: function () {
	       this.$request({
	        url: '/dept/get/all',
            method: 'post'
	      }).then(res => { 
	        this.treeLoading = true; 
	        this.treeData = res.data.result_data; 
	        this.treeLoading = false; 
	      });
	    }
 	
	}

 
</script>

 