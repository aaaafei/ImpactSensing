<template>
<div>
    <div style="margin:5px 0;">
        <el-row>
            <el-col :span="6">
                <el-date-picker v-model="year_month" type="month" value-format="yyyy-MM" placeholder="年月"></el-date-picker>  
                安全生产工作月报表
            </el-col>
            <el-col :span="2">
                <div style="margin-top:8px;">部门/单位负责人：</div>
            </el-col>
            <el-col :span="6">
                <el-input v-model="form.dept_principal_name" readonly= "true" placeholder="请选择部门/单位负责人"> 
                    <el-button slot="append" icon="el-icon-search" @click="chooseUser"></el-button>
                    </el-input>
                </el-input>
            </el-col>
            <el-col :span="10">&nbsp;&nbsp;
                <el-button type="primary" @click="save" v-if="!isView">保存</el-button>
            </el-col>
        </el-row>
    </div>
        <el-tabs type="border-card" v-model="activeName" >

          <el-tab-pane label="规章制度" name="1">   
            <el-form ref="form1" label-width="200px">
                <el-form-item label="新增（修订）安全生产制度"> 
                    <el-row>
                      <el-col :span="6"> 
                        <el-input-number v-model="form.safety_rules" :min="0"></el-input-number> 条 
                      </el-col>
                      <el-col :span="14">
                        <el-input type="textarea" v-model="form.safety_rules_detail"  placeholder="名称及内容"></el-input>
                      </el-col> 
                    </el-row>
                </el-form-item> 
                <el-form-item label="新增（修订）安全操作规程"> 
                    <el-row>
                      <el-col :span="6"> 
                        <el-input-number v-model="form.operation_rules" :min="0"></el-input-number> 条 
                      </el-col>
                      <el-col :span="14">
                        <el-input type="textarea" v-model="form.operation_rules_detail" placeholder="名称及内容"></el-input>
                      </el-col> 
                    </el-row>
                </el-form-item>
                <el-form-item label="新增（修订）安全生产预案"> 
                    <el-row>
                      <el-col :span="6"> 
                        <el-input-number v-model="form.production_plans" :min="0"></el-input-number> 条 
                      </el-col>
                      <el-col :span="14">
                        <el-input type="textarea" v-model="form.production_plans_detail" placeholder="名称及内容"></el-input>
                      </el-col> 
                    </el-row>
                </el-form-item> 
            </el-form> 
          </el-tab-pane>

          <el-tab-pane label="教育和培训" name="2"> 
            <el-form ref="form2" label-width="220px"> 
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>公司级培训</span> 
                    </div>
                    <div>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="公司级培训次数">
                                    <el-input-number v-model="form.company_train_times" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="参培人次">
                                    <el-input-number v-model="form.company_train_person_times" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                        </el-row>   
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="主要培训对象">
                                <el-input v-model="form.company_train_target_population" type="textarea" ></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="有无培训签到表">
                                    <el-input v-model="form.company_train_sign"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>  
                        <el-form-item label="培训主要内容及形式"> 
                            <el-input type="textarea"  v-model="form.company_train_content_form" placeholder="名称及内容"></el-input>                       
                        </el-form-item>                           
                    </div>
                </el-card>

                <el-card class="box-card" style="margin-top:10px;">
                    <div slot="header" class="clearfix">
                        <span>部门、基层单位级教育培训</span> 
                    </div>
                    <div>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="部门、基层单位级教育培训次数">
                                    <el-input-number v-model="form.dept_train_times" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="参培人次">
                                    <el-input-number v-model="form.dept_train_person_times" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                        </el-row>   
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="主要培训对象">
                                <el-input v-model="form.dept_train_target_population" type="textarea"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="有无培训签到表">
                                    <el-input v-model="form.dept_train_sign"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>  
                        <el-form-item label="培训主要内容及形式"> 
                            <el-input v-model="form.dept_train_content_form" type="textarea" placeholder="名称及内容"></el-input>                       
                        </el-form-item>                          
                    </div>
                </el-card> 

                <el-card class="box-card" style="margin-top:10px;">
                    <div slot="header" class="clearfix">
                        <span>班组级教育培训</span> 
                    </div>
                    <div>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="班组级教育培训次数">
                                    <el-input-number v-model="form.team_train_times" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="参培人次">
                                    <el-input-number v-model="form.team_train_person_times"  :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                        </el-row>   
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="主要培训对象">
                                    <el-input v-model="form.team_train_target_population" type="textarea"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="有无培训签到表">
                                    <el-input v-model="form.team_train_sign"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>  
                        <el-form-item label="培训主要内容及形式"> 
                            <el-input v-model="form.team_train_content_form" type="textarea" placeholder="名称及内容"></el-input>                       
                        </el-form-item>                          
                    </div>
                </el-card>   

                <el-card class="box-card" style="margin-top:10px;">
                    <div slot="header" class="clearfix">
                        <span>三级教育</span> 
                    </div>
                    <div>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="本单位员工">
                                    <el-input-number v-model="form.education3_company_persons" :step="1" :min="0"></el-input-number>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="本单位遗漏">
                                    <el-input-number v-model="form.education3_company_omit_persons" :step="1" :min="0"></el-input-number>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="外来人员">
                                    <el-input-number v-model="form.education3_outside_persons" :step="1" :min="0"></el-input-number>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label=" 外来人员遗漏">
                                    <el-input-number v-model="form.education3_outside_omit_persons" :step="1" :min="0"></el-input-number>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="合    计"> 
                                    {{this.detailForm.education3_company_persons + this.detailForm.education3_outside_persons}}
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label=" 遗漏合计"> 
                                    {{this.detailForm.education3_company_omit_persons + this.detailForm.education3_outside_omit_persons}}
                                </el-form-item>
                            </el-col>
                        </el-row> 
                    </div>
                </el-card>   

                <el-card class="box-card" style="margin-top:10px;">
                    <div slot="header" class="clearfix">
                        <span>人员持证</span> 
                    </div>
                    <div>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="应持证人次">
                                    <el-input-number v-model="form.cert_require_person_times" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="实际持证人次">
                                    <el-input-number v-model="form.cert_fact_person_times" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                        </el-row>                  
                        <el-form-item label="证书复审时间">
                            <el-date-picker v-model="form.cert_review_time" value-format="yyyy-mm-dd" placeholder="年月日"></el-date-picker> 
                        </el-form-item>
                        <el-form-item label="未持证原因"> 
                            <el-input v-model="form.cert_lack_reason" type="textarea"></el-input> 
                        </el-form-item> 
                    </div>
                </el-card>             
            </el-form>            
          </el-tab-pane> 

          <el-tab-pane label="风险管控" name="3"> 
            <el-form ref="form3" label-width="200px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="安全生产扫码巡查次数">
                            <el-input-number v-model="form.patrol_times" :step="1" :min="0" ></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"> 
                        <el-form-item label="风险源巡查履职率">
                            <el-input-number v-model="form.patrol_rate" controls-position="right" :min="0" ></el-input-number> %
                        </el-form-item>
                    </el-col>
                </el-row>   
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="安全巡查任务未完成期数">
                            <el-input-number v-model="form.patrol_undone_period" :step="1" :min="0" ></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"> 
                        <el-form-item label="未完成原因">
                            <el-input  v-model="form.patrol_undone_reason" type="textarea"></el-input> 
                        </el-form-item>
                    </el-col>
                </el-row>     
                <el-form-item label="安全巡查任务未完成人员"> 
                    <el-input v-model="form.patrol_undone_persons" type="textarea"></el-input> 
                </el-form-item>   
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="辖区内风险源数量">
                            <el-input-number v-model="form.risk_source_num" :step="1" :min="0" ></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"> 
                        <el-form-item label="本月新增数量">
                            <el-input-number v-model="form.risk_source_add_num" :step="1" :min="0" ></el-input-number>
                        </el-form-item>
                    </el-col>
                </el-row>     
                <el-form-item label="重点风险源标识公示情况"> 
                    <el-input v-model="form.emphasis_risk_source_publicity" type="textarea"></el-input> 
                </el-form-item>                                                              
            </el-form>  
            
          </el-tab-pane> 


          <el-tab-pane label="应急救援演练" name="4"> 
            <el-form ref="form4" label-width="240px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="应急预案培训次数">
                            <el-input-number v-model="form.emergency_plan_train_times" :step="1" :min="0" ></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"> 
                        <el-form-item label="应急预案演练次数">
                            <el-input-number v-model="form.emergency_plan_drill_times" :step="1" :min="0" ></el-input-number>
                        </el-form-item>
                    </el-col>
                </el-row>   
                <el-form-item label="应急预案演练内容"> 
                    <el-input v-model="form.emergency_plan_drill_content" type="textarea"></el-input> 
                </el-form-item>  
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="应急物资、器具是否齐全">
                            <el-input v-model="form.emergency_material_appliance_complete"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"> 
                        <el-form-item label="应急物资、器具是否可以正常使用">
                             <el-input  v-model="form.emergency_material_appliance_normal"></el-input> 
                        </el-form-item>
                    </el-col>
                </el-row>     
                <el-form-item label="异常情况说明"> 
                    <el-input v-model="form.emergency_material_appliance_abnormal" type="textarea"></el-input> 
                </el-form-item>                                              
            </el-form>     
          </el-tab-pane> 


          <el-tab-pane label="违章作业" name="5"> 
            <el-form ref="form5" label-width="240px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="发现违章作业数量">
                            <el-input-number v-model="form.against_rule_num" :step="1" :min="0" ></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"> 
                        <el-form-item label="违章作业类型">
                            <el-input v-model="form.against_rule_type"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>   
                <el-form-item label="重复出现的违章作业"> 
                    <el-input v-model="form.against_rule_repeat" type="textarea"></el-input> 
                </el-form-item> 
                <el-form-item label="涉及部门/单位、人员"> 
                    <el-input v-model="form.against_rule_dept_person" type="textarea"></el-input> 
                </el-form-item>                           
            </el-form>   
          </el-tab-pane> 



          <el-tab-pane label="安全生产整改" name="6"> 
            <el-form ref="form6" label-width="240px"> 
                <el-card class="box-card" style="margin-top:10px;">
                    <div slot="header" class="clearfix">
                        <span>上期隐患</span> 
                    </div>
                    <div>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="发现隐患数量">
                                    <el-input-number v-model="form.pre_hidden_danger_num" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="已完成整改数量">
                                    <el-input-number v-model="form.pre_hidden_danger_rectify_num" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                        </el-row>   
                        <el-form-item label="未完成整改项目"> 
                            <el-input v-model="form.pre_hidden_danger_unfit_items" type="textarea"></el-input> 
                        </el-form-item> 
                        <el-form-item label="未完成整改原因"> 
                            <el-input v-model="form.pre_hidden_danger_unfit_reason" type="textarea"></el-input> 
                        </el-form-item> 
                    </div>
                </el-card>   
                <el-card class="box-card" style="margin-top:10px;margin-bottom:40px;">
                    <div slot="header" class="clearfix">
                        <span>本期隐患</span> 
                    </div>
                    <div>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="发现隐患数量">
                                    <el-input-number v-model="form.hidden_danger_num" :step="1" :min="0" ></el-input-number>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12"> 
                                <el-form-item label="隐患类型">
                                    <el-input v-model="form.hidden_danger_type"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>   
                        <el-form-item label="重复出现的隐患"> 
                            <el-input v-model="form.hidden_danger_repeat" type="textarea"></el-input> 
                        </el-form-item> 
                        <el-form-item label="涉及部门/单位、人员"> 
                            <el-input v-model="form.hidden_danger_dept_person" type="textarea"></el-input> 
                        </el-form-item> 
                    </div>
                </el-card>   
                <el-form-item label="综合分析"> 
                    <el-input v-model="form.hidden_danger_analysis" type="textarea"></el-input> 
                </el-form-item>    
            </el-form>    
          </el-tab-pane> 

         
        <el-tab-pane label="消防管理" name="7"> 
            <el-form ref="form7" label-width="240px">
                <el-form-item label="消防安全责任制落实"> 
                    <el-input v-model="form.fire_fighting_reponsibility" type="textarea"></el-input> 
                </el-form-item>    
                <el-form-item label="消防设施巡检次数"> 
                    <el-input type="textarea" v-model="form.fire_fighting_patrol_num"></el-input> 
                </el-form-item>                              
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="消防设备故障数量">
                            <el-input-number v-model="form.fire_device_fault_num" :step="1" :min="0" ></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"> 
                        <el-form-item label="24小时修复率">
                            <el-input-number v-model="form.fire_device_repair_rate" controls-position="right" :min="0" ></el-input-number>%
                        </el-form-item>
                    </el-col>
                </el-row>   
                <el-form-item label="主要故障类型"> 
                    <el-input v-model="form.fire_device_breakdown_type" type="textarea"></el-input> 
                </el-form-item> 
                <el-form-item label="人员密集场所防火检查次数"> 
                    <el-input v-model="form.crowded_area_patrol_num" type="textarea"></el-input> 
                </el-form-item>  
                <el-form-item label="是否达到法律法规要求（人员密集场所营业期间检查应达到2小时1次，未达到阐述原因并给出对策）"> 
                    <el-input v-model="form.meet_requirement" type="textarea" :rows="6"></el-input> 
                </el-form-item>                           
            </el-form>  
          </el-tab-pane> 

        </el-tabs>
         
        <el-dialog :visible.sync="userChoosePage" width="20%" title="人员选择" append-to-body>
            <user4Select @closeUseSel="closeUseSel" limitPermision="1"></user4Select>
        </el-dialog>

    </div>
</template>

<script>
/* eslint-disable */ 
    import user4Select from './user4Select.vue';
    export default{
        props: ["detailForm","isView"],
        components: {user4Select},
        data() {
            return {
                activeName: '1',
                form: this.detailForm,  
                year_month: this.detailForm['yearMonth'],
                userChoosePage: false
            };
        },
        methods: { 
            chooseUser(){
                this.userChoosePage = true; 
            },
            closeUseSel(name,id){
                this.userChoosePage = false;
                this.form.dept_principal_id = id;
                this.form.dept_principal_name = name;
            },
            save(){
                if(this.form.month_report_id){
                    //edit 
                    if(this.year_month){
                        this.form.report_year = this.year_month.split('-')[0];
                        this.form.report_month = this.year_month.split('-')[1]; 
                        this.$request({
                            url: '/tbMonthReport/update',
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
                    }
                    else{ 
                        this.$message({message:'请选择年月！', type:'warning'});
                    }
                }  
                else{
                    //add
                    if(this.year_month){
                        this.form.report_year = this.year_month.split('-')[0];
                        this.form.report_month = this.year_month.split('-')[1]; 
                        this.form.report_type = 1; 
                        this.$request({
                            url: '/tbMonthReport/add',
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
                    }
                    else{ 
                        this.$message({message:'请选择年月！', type:'warning'});
                    }
                } 
            },
            resetForm(){ 
                this.$refs['form1'].resetFields(); 
                this.$refs['form2'].resetFields(); 
                this.$refs['form3'].resetFields(); 
                this.$refs['form4'].resetFields(); 
                this.$refs['form5'].resetFields(); 
                this.$refs['form6'].resetFields(); 
                this.$refs['form7'].resetFields(); 
                this.activeName = '1';
            },
            close(){
                this.$emit("closeAddEdit","");
            }
        }, 
        watch: {
            detailForm: {
                deep: true,
                handler(newValue, oldValue) { 
                    this.year_month = newValue['yearMonth']; 
                }
            }
            
        }
    }
</script>