<template>
    <div>
        <div style="margin:10px;">
            <el-button-group>
                <el-button type="primary" icon="el-icon-plus" @click="add">新增</el-button>
                <el-button type="primary" icon="el-icon-delete" @click="del">删除</el-button>
            </el-button-group>
        </div>
        <el-table :data="tableData" style="width:100%;" height="450" @selection-change="handleSelectionChange"  :cell-style="{padding:'3px 0'}">
            <el-table-column type="selection" width="29" :selectable='checkboxInit'></el-table-column>
            <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
            <el-table-column label="报送单位" prop="org_name" width="200" align="center"></el-table-column>
            <el-table-column label="报送人" prop="user_name" width="150" align="center"></el-table-column> 
            <el-table-column label="报送主题" prop="title" width="200" align="center"></el-table-column> 
            <el-table-column label="报送时间" prop="submit_time" align="center"></el-table-column>
            <el-table-column label="审核状态" width="120" align="center">
                <template slot-scope="scope">
                    {{checkStatus['c'+scope.row.check_status]}}
                </template>
            </el-table-column> 
            <el-table-column label="同步状态" width="120" align="center">
                <template slot-scope="scope">
                    {{synStatus['s'+scope.row.syn_status]}}
                </template>
            </el-table-column> 
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.check_status==0" size="mini" type="success" plain @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button v-if="scope.row.check_status==0" size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>  
                    <el-button v-if="scope.row.proc_inst_id!=null" size="mini" type="success" @click="processInfo(scope.row.proc_inst_id)">流转情况</el-button> 
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
        <el-dialog :visible.sync="addEditVisible" width="50%" :title="addEditTitle" append-to-body>
            <addEdit @closeAddEdit="closeAddEdit" :detailForm="form"></addEdit>
        </el-dialog>

        <el-dialog :visible.sync="processPicVisible" title="流程流转情况" width="80%">
            <el-table :data="tableTaskProcess" style="width:100%"  :cell-style="{padding:'3px 0'}" height="300" highlight-current-row> 
                    <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                    <el-table-column prop="activityName" label="活动名称" width="200" align="center"></el-table-column>
                    <el-table-column prop="assigneeName" label="办理人" width="120" align="center"></el-table-column>
                    <el-table-column prop="comment" label="处理意见" header-align="center" show-overflow-tooltip="true"></el-table-column>
                    <el-table-column prop="startTime" label="活动开始时间" width="200" align="center"></el-table-column>
                    <el-table-column prop="endTime" label="活动结束时间" width="200" align="center"></el-table-column>
            </el-table> 
            <img :src="processPicUrl" class="image">
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
                pageProps: ['uuid', 'user_id', 'org_id', 'title', 'content', 'submit_time', 'syn_status', 'check_status'],
                form: {uuid: null, user_id: null, org_id: null, title:null,  content: null, submit_time: null,syn_status: null, check_status: null},
                checkStatus: {c0:'未提交',c1:'审批中',c2:'审批通过',c3:'审批不通过'},
                synStatus: {s0: '无需同步',s1:'未同步',s2:'已同步'},
                processPicUrl: null,
                processPicVisible: false,
                tableTaskProcess: []
            };
        },
        methods: {
            handleSelectionChange(rows){
                this.selectUUIDs = [];//clear
                if(rows.length > 0){
                    rows.forEach(row => {
                        if(row){
                            this.selectUUIDs.push(row.uuid);
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
                var url = "/riskInfoSubmit/getPageData/" + this.page.pageNum + "/" + this.page.pageSize;
                this.$request({
                    url: url,
                    data: {},
                    method: 'POST'
                }).then( res => {
                    this.tableData = res.data.result_data.list;
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
                this.form.syn_status = 0;//默认不需要同步
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
                        url: '/riskInfoSubmit/delete/' + this.selectUUIDs.join(',') +"/",
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
                    url: '/riskInfoSubmit/delete/' + rowData.uuid
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
            },
            closeAddEdit() {
                this.addEditVisible = false;
                this.searchDataList();    
            },
            checkboxInit(row,index){ 
            　　if (row.check_status!=0)//这个判断根据你的情况而定
    　　　　        return 0;//不可勾选
    　　　　    else
    　　　　        return 1;//可勾选
  　　      },
            processInfo(procInstId){
                var url = "/api/file/traceProcess/" + procInstId;
                this.processPicUrl =  url;
                this.processPicVisible = true;
                this.$request({
                    url: "/workflow/taskProcessInfo/" + procInstId,
                    method: 'get'
                }).then(res => {
                    let data = res.data;
                    this.tableTaskProcess = data.result_data; 
                });          
            }
        },
        mounted: function(){
            this.searchDataList();
        }
    };
</script>