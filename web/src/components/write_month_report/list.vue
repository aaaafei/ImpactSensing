<template>
    <div>
        <div style="margin:10px;">
            <el-button-group>
                <el-button type="primary" icon="el-icon-plus" @click="add">新增</el-button>
                <el-button type="primary" icon="el-icon-delete" @click="del">删除</el-button>
            </el-button-group>
        </div>
        <el-table :data="tableData" style="width:100%;" height="450" @selection-change="handleSelectionChange" :cell-style="{padding:'3px 0'}">
            <el-table-column type="selection" width="29"></el-table-column>
            <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
            <el-table-column label="年度" prop="report_year" width="200" align="center"></el-table-column>
            <el-table-column label="月份" prop="report_month" width="200" align="center"></el-table-column>
            <el-table-column label="部门/单位" prop="dept_name" align="center"></el-table-column> 
            <el-table-column label="部门/单位负责人" prop="dept_principal_name" align="center"></el-table-column> 
            <el-table-column label="填报人" prop="reporter_user_name" align="center"></el-table-column> 
            <el-table-column label="操作" width="250" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" type="success" plain @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button> 
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.pageNum"
        :page-sizes="[10, 20, 50]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total">
        </el-pagination>

        <!-- add/edit page --> 
        <el-dialog :visible.sync="addEditVisible" width="90%" :title="addEditTitle" append-to-body>
            <addEdit @closeAddEdit="closeAddEdit" :detailForm="form"></addEdit>
        </el-dialog>
    </div>
</template>

<script>
/* eslint-disable */
    import addEdit from './add_edit.vue'; 
    export default{
        components: {addEdit},
        data(){
            return {
                tableData: [],
                selectUUIDs: [],
                page: {
                    pageNum: 0,
                    pageSize: 10,
                    total: 0
                },
                addEditTitle: '',
                addEditVisible: false, 
                pageProps: ['month_report_id','dept_id','report_year','report_month','write_date','safety_rules','safety_rules_detail','operation_rules','operation_rules_detail','production_plans','production_plans_detail','company_train_times','company_train_person_times','company_train_target_population','company_train_sign','company_train_content_form','dept_train_times','dept_train_person_times','dept_train_target_population','dept_train_sign','dept_train_content_form','team_train_times','team_train_person_times','team_train_target_population','team_train_sign','team_train_content_form','education3_company_persons','education3_outside_persons','education3_company_omit_persons','education3_outside_omit_persons','cert_require_person_times','cert_fact_person_times','cert_review_time','cert_lack_reason','patrol_times','patrol_rate','patrol_undone_period','patrol_undone_reason','patrol_undone_persons','risk_source_num','risk_source_add_num','emphasis_risk_source_publicity','emergency_plan_train_times','emergency_plan_drill_times','emergency_plan_drill_content','emergency_material_appliance_complete','emergency_material_appliance_normal','emergency_material_appliance_abnormal','against_rule_num','against_rule_type','against_rule_repeat','against_rule_dept_person','pre_hidden_danger_num','pre_hidden_danger_rectify_num','pre_hidden_danger_unfit_items','pre_hidden_danger_unfit_reason','hidden_danger_num','hidden_danger_type','hidden_danger_repeat','hidden_danger_dept_person','hidden_danger_analysis','fire_fighting_reponsibility','fire_fighting_patrol_num','fire_device_fault_num','fire_device_repair_rate','fire_device_breakdown_type','crowded_area_patrol_num','meet_requirement','reporter_user_id','dept_principal_id','dept_principal_name'],
                form: {month_report_id: null, dept_id: null, report_year: null,report_month: null, write_date: null, safety_rules: null, safety_rules_detail: null, operation_rules: null, operation_rules_detail: null, production_plans: null, production_plans_detail: null, company_train_times: null, company_train_person_times: null, company_train_target_population: null, company_train_sign: null, company_train_content_form: null, dept_train_times: null, dept_train_person_times: null, dept_train_target_population: null, dept_train_sign: null, dept_train_content_form: null, team_train_times: null, team_train_person_times: null, team_train_target_population: null, team_train_sign: null, team_train_content_form: null, education3_company_persons: null, education3_outside_persons: null, education3_company_omit_persons: null, education3_outside_omit_persons: null, cert_require_person_times: null, cert_fact_person_times: null, cert_review_time: null, cert_lack_reason: null, patrol_times: null, patrol_rate: null, patrol_undone_period: null, patrol_undone_reason: null, patrol_undone_persons: null, risk_source_num: null, risk_source_add_num: null, emphasis_risk_source_publicity: null, emergency_plan_train_times: null, emergency_plan_drill_times: null, emergency_plan_drill_content: null, emergency_material_appliance_complete: null, emergency_material_appliance_normal: null, emergency_material_appliance_abnormal: null, against_rule_num: null, against_rule_type: null, against_rule_repeat: null, against_rule_dept_person: null, pre_hidden_danger_num: null, pre_hidden_danger_rectify_num: null, pre_hidden_danger_unfit_items: null, pre_hidden_danger_unfit_reason: null, hidden_danger_num: null, hidden_danger_type: null, hidden_danger_repeat: null, hidden_danger_dept_person: null, hidden_danger_analysis: null, fire_fighting_reponsibility: null, fire_fighting_patrol_num: null, fire_device_fault_num: null, fire_device_repair_rate: null, fire_device_breakdown_type: null, crowded_area_patrol_num: null, meet_requirement: null, reporter_user_id: null, dept_principal_id: null, dept_principal_name: null} 
            };
        },
        methods: {
            handleSelectionChange(rows){
                this.selectUUIDs = [];//clear
                if(rows.length > 0){
                    rows.forEach(row => {
                        if(row){
                            this.selectUUIDs.push(row.month_report_id);
                        }
                    });
                }
            },
            handleSizeChange(v){
                this.page.pageSize = v;
                this.searchDataList();
            },
            handleCurrentChange(v){
                this.page.pageNum = v;
                this.searchDataList();
            },
            searchDataList(){
                var url = "/tbMonthReport/getPageData/" + this.page.pageNum + "/" + this.page.pageSize;
                this.$request({
                    url: url,
                    data: {'report_type': 1},
                    method: 'POST'
                }).then( res => {
                    this.tableData =  res.data.result_data.list;
                    this.page.total = res.data.result_data.total;
                });
            }, 
            add(){
                this.addEditVisible = true;
                this.addEditTitle = '添加';
                //initial clear
                for (var i = 0; i < this.pageProps.length; i++) { 
                 var prop = this.pageProps[i];
                 this.form[prop] = null;
                }
                this.form['yearMonth'] = new Date().getFullYear() + "-" + (new Date().getMonth() + 1);
            },
            del(){  
                if(this.selectUUIDs.length==0){
                  this.$alert('请选择需要删除的数据！','提示');
                }
                else{
                  this.$confirm('确认删除所选数据吗？','提示',{
                      confirmButtonText: '确定',
                      cancelButtonText: '取消',
                      type: 'warning'
                  }).then(() => {
                      this.$request({
                        url: '/tbMonthReport/delete/' + this.selectUUIDs.join(',') +"/",
                        method:'get'
                      }).then(res => {
                        this.$message({
                          type: 'success',
                          message: '删除成功!'
                        });
                        this.searchDataList();//reload list
                        this.selectUUIDs=[];//clear
                      });
                    }).catch(()=>{
                        this.$message({type: 'info',message: '已取消删除'});
                    });
                }
            },
            handleDelete(index,rowData){
                this.$confirm('确认删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request({
                    url: '/tbMonthReport/delete/' + rowData.month_report_id
                    }).then(res => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.searchDataList();
                    }); 
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    }); 
                }); 
            },
            handleEdit(index,rowData){ 
                this.addEditVisible = true;
                this.addEditTitle = '修改';
                for (var i = 0; i < this.pageProps.length; i++) { 
                 var prop = this.pageProps[i];
                 this.form[prop] = rowData[prop];
                }
                this.form['yearMonth'] = this.form['report_year']  + "-" + this.form['report_month'] ;   
            },
            closeAddEdit() {
                this.addEditVisible = false;
                this.searchDataList();    
            } 
        },
        mounted: function(){
            this.searchDataList();
        }
    };
</script>