<template>
    <div  style="margin-top:5px;">
        <el-tabs type="border-card" v-model="activeName">
          <el-tab-pane label="部门/单位填报" name="1">  
            <div style="margin-bottom:5px;">
                <el-cascader v-model="dept_id" placeholder="请选择部门" :show-all-levels="false" change-on-select :options="deptOptions" clearable></el-cascader> 
                <el-date-picker v-model="year_month" type="month" value-format="yyyy-MM" placeholder="年月"></el-date-picker>  
                 <el-button type="primary" @click="searchDataList">查询</el-button>
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
                        <el-button size="mini" type="success" plain @click="handleView(scope.$index, scope.row)">查看</el-button> 
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
            </el-card>
          </el-tab-pane>

          <el-tab-pane label="公司汇总" name="2">  
            <div style="margin:10px;">
                <el-date-picker v-model="year_month" type="month" value-format="yyyy-MM" placeholder="年月"></el-date-picker>  
                <el-button-group>
                    <el-button type="primary" icon="el-icon-plus" @click="summarize">汇总</el-button>

                    <el-button type="primary" icon="el-icon-delete" @click="del">删除</el-button>
                </el-button-group>
                <span style="color:grey;font-size:12px;">(注：汇总将对各部门/单位上报数量进行累加求和，对文字内容进行文字叠加式汇总)</span>
            </div>
            <el-table :data="tableDataCompany" style="width:100%;" height="450" @selection-change="handleSelectionChange" :cell-style="{padding:'3px 0'}">
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
                        <el-button size="mini" type="success" @click="exportDoc(scope.$index, scope.row)">导出报表</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
            @size-change="handleSizeChangeCompany"
            @current-change="handleCurrentChangeCompany"
            :current-page="pageCompany.pageNum"
            :page-sizes="[10, 20, 50]"
            :page-size="pageCompany.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pageCompany.total">
            </el-pagination> 
          </el-tab-pane> 
        </el-tabs>







        <!-- add/edit page --> 
        <el-dialog :visible.sync="addEditVisible" width="90%" :title="addEditTitle" append-to-body>
            <addEdit @closeAddEdit="closeAddEdit" :detailForm="form" :isView="isViewPage"></addEdit>
        </el-dialog>

        <!-- summarize page --> 
        <el-dialog :visible.sync="summarizeVisible" width="90%" :title="addEditTitle" append-to-body>
            <summarizePage @closeAddEdit="closeSummarizePage" :detailForm="form"></summarizePage>
        </el-dialog>



    </div>
</template>

<script>
/* eslint-disable */
    import addEdit from './add_edit.vue'; 
    import summarizePage from './summarize.vue';
    import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
    export default{
        components: {addEdit, summarizePage},
        data(){
            return {
                activeName: '1',
                year_month: null,
                dept_id: null,
                deptOptions: [],
                tableData: [],
                tableDataCompany: [],
                selectUUIDs: [],
                page: {
                    pageNum: 0,
                    pageSize: 10,
                    total: 0
                },
                pageCompany: {
                    pageNum: 0,
                    pageSize: 10,
                    total: 0
                },
                addEditTitle: '',
                isViewPage: false,
                addEditVisible: false, 
                summarizeVisible: false,
                pageProps: ['month_report_id','dept_id','report_year','report_month','write_date','safety_rules','safety_rules_detail','operation_rules','operation_rules_detail','production_plans','production_plans_detail','company_train_times','company_train_person_times','company_train_target_population','company_train_sign','company_train_content_form','dept_train_times','dept_train_person_times','dept_train_target_population','dept_train_sign','dept_train_content_form','team_train_times','team_train_person_times','team_train_target_population','team_train_sign','team_train_content_form','education3_company_persons','education3_outside_persons','education3_company_omit_persons','education3_outside_omit_persons','cert_require_person_times','cert_fact_person_times','cert_review_time','cert_lack_reason','patrol_times','patrol_rate','patrol_undone_period','patrol_undone_reason','patrol_undone_persons','risk_source_num','risk_source_add_num','emphasis_risk_source_publicity','emergency_plan_train_times','emergency_plan_drill_times','emergency_plan_drill_content','emergency_material_appliance_complete','emergency_material_appliance_normal','emergency_material_appliance_abnormal','against_rule_num','against_rule_type','against_rule_repeat','against_rule_dept_person','pre_hidden_danger_num','pre_hidden_danger_rectify_num','pre_hidden_danger_unfit_items','pre_hidden_danger_unfit_reason','hidden_danger_num','hidden_danger_type','hidden_danger_repeat','hidden_danger_dept_person','hidden_danger_analysis','fire_fighting_reponsibility','fire_fighting_patrol_num','fire_device_fault_num','fire_device_repair_rate','fire_device_breakdown_type','crowded_area_patrol_num','meet_requirement','reporter_user_id','dept_principal_id','dept_principal_name','report_type'],
                form: {month_report_id: null, dept_id: null, report_year: null,report_month: null, write_date: null, safety_rules: null, safety_rules_detail: null, operation_rules: null, operation_rules_detail: null, production_plans: null, production_plans_detail: null, company_train_times: null, company_train_person_times: null, company_train_target_population: null, company_train_sign: null, company_train_content_form: null, dept_train_times: null, dept_train_person_times: null, dept_train_target_population: null, dept_train_sign: null, dept_train_content_form: null, team_train_times: null, team_train_person_times: null, team_train_target_population: null, team_train_sign: null, team_train_content_form: null, education3_company_persons: null, education3_outside_persons: null, education3_company_omit_persons: null, education3_outside_omit_persons: null, cert_require_person_times: null, cert_fact_person_times: null, cert_review_time: null, cert_lack_reason: null, patrol_times: null, patrol_rate: null, patrol_undone_period: null, patrol_undone_reason: null, patrol_undone_persons: null, risk_source_num: null, risk_source_add_num: null, emphasis_risk_source_publicity: null, emergency_plan_train_times: null, emergency_plan_drill_times: null, emergency_plan_drill_content: null, emergency_material_appliance_complete: null, emergency_material_appliance_normal: null, emergency_material_appliance_abnormal: null, against_rule_num: null, against_rule_type: null, against_rule_repeat: null, against_rule_dept_person: null, pre_hidden_danger_num: null, pre_hidden_danger_rectify_num: null, pre_hidden_danger_unfit_items: null, pre_hidden_danger_unfit_reason: null, hidden_danger_num: null, hidden_danger_type: null, hidden_danger_repeat: null, hidden_danger_dept_person: null, hidden_danger_analysis: null, fire_fighting_reponsibility: null, fire_fighting_patrol_num: null, fire_device_fault_num: null, fire_device_repair_rate: null, fire_device_breakdown_type: null, crowded_area_patrol_num: null, meet_requirement: null, reporter_user_id: null, dept_principal_id: null, dept_principal_name: null, report_type: null},
                calNumProps: ['safety_rules','operation_rules','production_plans','company_train_times','company_train_person_times','dept_train_times','dept_train_person_times','team_train_times','team_train_person_times','education3_company_persons','education3_outside_persons','education3_company_omit_persons','education3_outside_omit_persons','cert_require_person_times','cert_fact_person_times','patrol_times','patrol_undone_period','risk_source_num','risk_source_add_num','emergency_plan_train_times','emergency_plan_drill_times','against_rule_num','pre_hidden_danger_num','pre_hidden_danger_rectify_num','hidden_danger_num','fire_device_fault_num'],
                colStrProps: ['safety_rules_detail','operation_rules_detail','production_plans_detail','company_train_target_population','company_train_sign','company_train_content_form','dept_train_target_population','dept_train_sign','dept_train_content_form','team_train_target_population','team_train_sign','team_train_content_form','cert_review_time','cert_lack_reason','patrol_undone_reason','patrol_undone_persons','emphasis_risk_source_publicity','emergency_plan_drill_content','emergency_material_appliance_complete','emergency_material_appliance_normal','emergency_material_appliance_abnormal','against_rule_type','against_rule_repeat','against_rule_dept_person','pre_hidden_danger_unfit_items','pre_hidden_danger_unfit_reason','hidden_danger_type','hidden_danger_repeat','hidden_danger_dept_person','hidden_danger_analysis','fire_fighting_reponsibility','fire_fighting_patrol_num','fire_device_breakdown_type','crowded_area_patrol_num','meet_requirement'],
                colRateProps: ['patrol_rate','fire_device_repair_rate']
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
                var q = {}; 
                if(this.dept_id!=null && this.dept_id != '' && this.dept_id.length > 0){
                    q['dept_id'] = this.dept_id[this.dept_id.length-1]; 
                }
                if(this.year_month){ 
                    q['report_year'] = this.year_month.split('-')[0];
                    q['report_month'] = this.year_month.split('-')[1];
                }
                q['report_type'] = 1;

                var url = "/tbMonthReport/getPageData/" + this.page.pageNum + "/" + this.page.pageSize;
                this.$request({
                    url: url,
                    data: q,
                    method: 'POST'
                }).then( res => {
                    this.tableData =  res.data.result_data.list;
                    this.page.total = res.data.result_data.total;
                });
            }, 
            handleSelectionChangeCompany(rows){
                this.selectUUIDs = [];//clear
                if(rows.length > 0){
                    rows.forEach(row => {
                        if(row){
                            this.selectUUIDs.push(row.month_report_id);
                        }
                    });
                }
            },
            handleSizeChangeCompany(v){
                this.pageCompany.pageSize = v;
                this.searchDataListCompany();
            },
            handleCurrentChangeCompany(v){
                this.pageCompany.pageNum = v;
                this.searchDataListCompany();
            },
            searchDataListCompany(){
                var q = {};  
                q['report_type'] = 2;

                var url = "/tbMonthReport/getPageData/" + this.pageCompany.pageNum + "/" + this.pageCompany.pageSize;
                this.$request({
                    url: url,
                    data: q,
                    method: 'POST'
                }).then( res => {
                    this.tableDataCompany =  res.data.result_data.list;
                    this.pageCompany.total = res.data.result_data.total;
                });
            }, 
            summarize(){
                if(this.year_month){ 
                    this.summarizeVisible = true;
                    this.addEditTitle = '汇总';
                    this.form['yearMonth'] = this.year_month;

                    var calculateNumberProps = this.calNumProps;
                    var collectStringProps = this.colStrProps;
                    var collectRateProps  = this.colRateProps;

                    //initial clear
                    for (var i = 0; i < this.pageProps.length; i++) { 
                        var prop = this.pageProps[i];
                        this.form[prop] = null;
                    }

                    this.$request({
                        url: '/tbMonthReport/summarize/' + this.year_month.split('-')[0] +"/" + this.year_month.split('-')[1] +"/1",
                        method:'get'
                    }).then(res => {
                        var list = res.data.result_data.list;
                        for(var i=0;i<list.length;i++){
                            //数值累加
                            for(var a=0;a<calculateNumberProps.length;a++){
                                var prop = calculateNumberProps[a];
                                if(this.form[prop] == null){
                                    if(list[i][prop])
                                        this.form[prop] = list[i][prop];
                                    else
                                        this.form[prop] = 0;
                                }
                                else{
                                    if(list[i][prop])
                                        this.form[prop] += list[i][prop]; 
                                }
                            }
                            //字符串叠加
                            for(var b=0;b<collectStringProps.length;b++){
                                var prop = collectStringProps[b];
                                if(this.form[prop] == null){
                                    if(list[i][prop]){
                                        this.form[prop] = list[i][prop] + '[' + list[i]['dept_name'] +']';
                                    } 
                                }
                                else{
                                    if(list[i][prop]){
                                        this.form[prop] += "  " +  list[i][prop] + '[' + list[i]['dept_name'] +']';
                                    }
                                }
                            }
                            //百分比加%符号
                            for(var c=0;c<collectRateProps.length;c++){
                                var prop = collectRateProps[c];
                                if(this.form[prop] == null){
                                    if(list[i][prop]){
                                        this.form[prop] = list[i][prop] + '%[' + list[i]['dept_name'] +']';
                                    } 
                                }
                                else{
                                    if(list[i][prop]){
                                        this.form[prop] += "  " +  list[i][prop] + '%[' + list[i]['dept_name'] +']';
                                    }
                                }
                            }
                        }
                    }); 
                }
                else{ 
                    this.$message({message:'请选择年月！', type:'warning'});
                }


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
                        this.searchDataListCompany();//reload list
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
                        this.searchDataListCompany();
                    }); 
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    }); 
                }); 
            },
            handleEdit(index,rowData){ 
                this.summarizeVisible = true;
                this.addEditTitle = '修改';
                this.isViewPage = false;
                for (var i = 0; i < this.pageProps.length; i++) { 
                 var prop = this.pageProps[i];
                 this.form[prop] = rowData[prop];
                }
                this.form['yearMonth'] = this.form['report_year']  + "-" + this.form['report_month'] ;   
            },
            handleView(index,rowData){ 
                this.addEditVisible = true;
                this.addEditTitle = '查看';
                this.isViewPage = true;
                for (var i = 0; i < this.pageProps.length; i++) { 
                 var prop = this.pageProps[i];
                 this.form[prop] = rowData[prop];
                }
                this.form['yearMonth'] = this.form['report_year']  + "-" + this.form['report_month'] ;   
            },
            closeAddEdit() {
                this.addEditVisible = false;
                this.searchDataListCompany();
            },
            closeSummarizePage(){
                this.summarizeVisible = false;
                this.searchDataListCompany(); 
            },
            loadDeptCascader(){
                var url = '/dept/get/all';
                this.$request({
                    url: url,
                    method: 'get'
                }).then(res => {
                    let data = res.data.result_data;
                    this.deptOptions = this.formatDeptData(data);  
                });  
            },
            formatDeptData(data){ 
                var r = [];
                if(data!=undefined && data!=null && data.length>0){
                  data.forEach(row => { 
                    var temp = {value: row.id, label: row.organname}; 
                    if(row.children!=undefined && row.children!=null && row.children.length>0){ 
                      temp.children = this.formatDeptData(row.children);
                    }
                    r.push(temp);
                  });
                }
            return r;    
            },
            exportDoc(index,rowData){  
                this.$request({
                    url: '/tbMonthReport/exportCompanyMonthReport/' + rowData.month_report_id,
                    responseType: 'blob'
                }).then(res => { 
                    let disposition = res.headers['content-disposition']; 
                    var filename = ""; 
                    if (disposition && disposition.indexOf('attachment') !== -1) {
                        var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                        var matches = filenameRegex.exec(disposition);
                        if (matches != null && matches[1]) { 
                          filename = matches[1].replace(/['"]/g, '');
                        }
                    } 
                    let url = window.URL.createObjectURL(new Blob([res.data]))
                    let link = document.createElement('a')
                    link.style.display = 'none'
                    link.href = url
                    link.setAttribute('download',decodeURI(escape(filename))); 
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                });
            }
        },
        mounted: function(){
            this.loadDeptCascader();
            this.searchDataList();
            this.searchDataListCompany();
        }
    };
</script>