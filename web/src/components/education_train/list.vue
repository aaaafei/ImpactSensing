<template>
    <div> 

            <el-row style="line-height:50px;">
                <el-col :span="5"> 
                    <el-button-group>
                        <el-button type="primary" icon="el-icon-plus" @click="add">新增</el-button>
                        <el-button type="primary" icon="el-icon-delete" @click="del">删除</el-button>
                    </el-button-group> 
                </el-col>
                <el-col :span="6">&nbsp;</el-col>
                <el-col :span="13">教育/培训信息</el-col>
            </el-row>
            
                <el-table :data="tableData" style="width:100%;" height="450" @selection-change="handleSelectionChange"  :cell-style="{padding:'3px 0'}" >
                    <el-table-column type="selection" width="29"></el-table-column>
                    <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                    <el-table-column label="部门" prop="org_name" width="140" show-overflow-tooltip="true" align="center"></el-table-column>
                    <el-table-column label="培训主题" show-overflow-tooltip="true" prop="train_theme" align="center"></el-table-column>
                    <el-table-column label="授课人/机构" prop="train_instructor" width="120" align="center" show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="受训人员" show-overflow-tooltip="true" prop="trainee" width="120" align="center"></el-table-column>
                    <el-table-column label="培训日期" prop="train_date" width="100" align="center"></el-table-column>
                    <el-table-column label="签到表" header-align="center" width="100" show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="downloadFile(scope.row.sign_attachment_path)">
                                {{ scope.row.sign_attachment_path!=null ? scope.row.sign_attachment_path.substr(scope.row.sign_attachment_path.lastIndexOf('\\')+1) : '' }}
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="记录表" header-align="center" width="100" show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="downloadFile(scope.row.record_attachment_path)">
                                {{ scope.row.record_attachment_path!=null ? scope.row.record_attachment_path.substr(scope.row.record_attachment_path.lastIndexOf('\\')+1) : '' }}
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="评价表" header-align="center" width="100" show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="downloadFile(scope.row.evaluation_attachment_path)">
                                {{ scope.row.evaluation_attachment_path!=null ? scope.row.evaluation_attachment_path.substr(scope.row.evaluation_attachment_path.lastIndexOf('\\')+1) : '' }}
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="150" align="center">
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
                <el-dialog :visible.sync="addEditVisible" width="50%" :title="addEditTitle" append-to-body :before-close="beforeCloseAddEdit">
                    <addEdit ref="addEdit" @closeAddEdit="closeAddEdit" :detailForm="form" :fileListA="fileList1" :fileListB="fileList2" :fileListC="fileList3" :addSaveBtn="addSaveBtn" :editSaveBtn="editSaveBtn"></addEdit>
                </el-dialog>

         
        
      

       <el-row style="line-height:50px;">
        <el-col :span="5">
                    <el-button-group>
                        <el-button type="primary" icon="el-icon-plus" @click="addCertificate">新增</el-button>
                        <el-button type="primary" icon="el-icon-delete" @click="delCertificate">删除</el-button>
                    </el-button-group>
               </el-col>
               <el-col :span="6">&nbsp;</el-col>
               <el-col :span="13">
                人员持证信息
            </el-col>
         </el-row>
                <el-table :data="tableData_certificate" style="width:100%;" height="450" @selection-change="handleSelectionChange_certificate"  :cell-style="{padding:'3px 0'}" >
                    <el-table-column type="selection" width="29"></el-table-column>
                    <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                    <el-table-column label="部门" prop="org_name"  align="center" show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="姓名" prop="user_id" width="70" show-overflow-tooltip="true" align="center"></el-table-column>
                    <el-table-column label="职位" prop="job_position" width="75" show-overflow-tooltip="true" align="center"></el-table-column>
                    <el-table-column label="资格类型" prop="qualification_type" width="100" show-overflow-tooltip="true" align="center"></el-table-column>
                    <el-table-column label="到期时间" prop="expiration_date" width="100" align="center"></el-table-column>
                    <el-table-column label="证书编号" prop="certificate_no" width="100" show-overflow-tooltip="true" align="center"></el-table-column>
                    <el-table-column label="初领证时间" prop="got_date" width="100" align="center"></el-table-column>
                    <el-table-column label="再培训时间" prop="retraining_date" width="100" align="center"></el-table-column>
                    <el-table-column label="复审时间" prop="recheck_date" width="100" align="center"></el-table-column>
                    <el-table-column label="发证机关" prop="licence_authority" width="100" show-overflow-tooltip="true" align="center"></el-table-column>   
                    <el-table-column label="操作" width="150" align="center">
                        <template slot-scope="scope">
                            <el-button size="mini" type="success" plain @click="handleEdit_certificate(scope.$index, scope.row)">编辑</el-button>
                            <el-button size="mini" type="danger" @click="handleDelete_certificate(scope.$index, scope.row)">删除</el-button> 
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                @size-change="handleSizeChange_certificate"
                @current-change="handleCurrentChange_certificate"
                :current-page="page_certificate.pageNum"
                :page-sizes="[10, 20, 50]"
                :page-size="page_certificate.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="page_certificate.total">
                </el-pagination>

                <!-- add/edit page --> 
                <el-dialog :visible.sync="addEditVisible_certificate" width="50%" :title="addEditTitle_certificate" append-to-body :before-close="beforeCloseAddEdit_certificate">
                    <addEditCertificate ref="addEdit_certificate" @closeAddEdit="closeAddEdit_certificate" :detailForm="form_certificate" :addSaveBtn="addSaveBtn_certificate" :editSaveBtn="editSaveBtn_certificate"></addEditCertificate>
                </el-dialog>
          

      

       <el-row style="line-height:50px;">
        <el-col :span="5">
                    <el-button-group>
                        <el-button type="primary" icon="el-icon-plus" @click="addSpecial">新增</el-button>
                        <el-button type="primary" icon="el-icon-delete" @click="delSpecial">删除</el-button>
                    </el-button-group>
              </el-col>
              <el-col :span="6">&nbsp;</el-col>
              <el-col :span="13">
                特种作业人员持证信息
            </el-col>
            </el-row>
                <el-table :data="tableData_special" style="width:100%;" height="450" @selection-change="handleSelectionChange_special"  :cell-style="{padding:'3px 0'}">
                    <el-table-column type="selection" width="29"></el-table-column>
                    <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                    <el-table-column label="部门" prop="org_name" show-overflow-tooltip="true" align="center"></el-table-column>
                    <el-table-column label="姓名" prop="user_id" width="80" align="center"></el-table-column>
                    <el-table-column label="岗位分类" prop="post_category" width="90" align="center"></el-table-column>
                    <el-table-column label="具体工种" prop="work_type" width="90" align="center"></el-table-column>
                    <el-table-column label="资格类型" prop="qualification_type" width="90" align="center"></el-table-column>
                    <el-table-column label="证书编号" prop="certificate_no" width="130" show-overflow-tooltip="true" align="center"></el-table-column>
                    <el-table-column label="证书到期日期" prop="expiration_date" width="110" align="center"></el-table-column>
                    <el-table-column label="复审时间" prop="recheck_date" width="100" align="center"></el-table-column>   
                    <el-table-column label="发证机关" prop="licence_authority" width="150" show-overflow-tooltip="true"  align="center"></el-table-column>
                    <el-table-column label="操作" width="150" align="center">
                        <template slot-scope="scope">
                            <el-button size="mini" type="success" plain @click="handleEdit_special(scope.$index, scope.row)">编辑</el-button>
                            <el-button size="mini" type="danger" @click="handleDelete_special(scope.$index, scope.row)">删除</el-button> 
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                @size-change="handleSizeChange_special"
                @current-change="handleCurrentChange_special"
                :current-page="page_special.pageNum"
                :page-sizes="[10, 20, 50]"
                :page-size="page_special.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="page_special.total">
                </el-pagination>

                <!-- add/edit page --> 
                <el-dialog :visible.sync="addEditVisible_special" width="50%" :title="addEditTitle_special" append-to-body :before-close="beforeCloseAddEdit_special">
                    <addEditSpecial ref="addEdit_special" @closeAddEdit="closeAddEdit_special" :detailForm="form_special" :addSaveBtn="addSaveBtn_special" :editSaveBtn="editSaveBtn_special"></addEditSpecial>
                </el-dialog>
        

    </div>
</template>

<script>
/* eslint-disable */
    import addEdit from './add_edit.vue';
    import addEditCertificate from './add_edit_certificate.vue';
    import addEditSpecial from './add_edit_special.vue';
    import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
    export default{
        components: {addEdit, addEditCertificate, addEditSpecial},
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
                addSaveBtn: false,
                editSaveBtn: false,
                pageProps: ['uuid', 'org_id', 'org_name', 'train_theme', 'train_content', 'train_instructor', 'trainee', 'train_date', 'sign_attachment_path', 'record_attachment_path', 'evaluation_attachment_path'],
                form: {uuid: null, org_id: null, org_name: null, train_theme: null, train_content: null, train_instructor: null, trainee: null, train_date: null, sign_attachment_path: null, record_attachment_path: null, evaluation_attachment_path: null },
                fileList1: [],
                fileList2: [],
                fileList3: [],
                tableData_certificate: [],
                selectUUIDs_certificate: [],
                page_certificate: {
                    pageNum: 0,
                    pageSize: 10,
                    total: 0
                },
                addEditVisible_certificate: false,
                addEditTitle_certificate: '',
                pageProps_certificate: ['uuid', 'user_id', 'org_id', 'job_position', 'qualification_type', 'certificate_no', 'expiration_date', 'got_date', 'retraining_date', 'recheck_date', 'licence_authority'],
                form_certificate: {uuid: null, user_id: null, org_id: null, job_position: null, qualification_type: null, certificate_no: null, expiration_date: null, got_date: null, retraining_date: null, recheck_date: null, licence_authority: null},
                addSaveBtn_certificate: false,
                editSaveBtn_certificate: false,
                tableData_special: [],
                selectUUIDs_special: [],
                page_special: {
                    pageNum: 0,
                    pageSize: 10,
                    total: 0
                },
                addEditVisible_special: false,
                addEditTitle_special: '',
                pageProps_special: ['uuid', 'user_id', 'org_id', 'post_category', 'work_type', 'qualification_type', 'certificate_no', 'expiration_date', 'got_date', 'retraining_date', 'recheck_date', 'licence_authority'],
                form_special: {uuid: null, user_id: null, org_id: null, post_category: null, work_type: null, qualification_type: null, certificate_no: null, expiration_date: null, got_date: null, retraining_date: null, recheck_date: null, licence_authority: null},
                addSaveBtn_special: false,
                editSaveBtn_special: false
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
                var url = "/educationTrain/getPageData/" + this.page.pageNum + "/" + this.page.pageSize;
                this.$request({
                    url: url,
                    data: {},
                    method: 'POST'
                }).then( res => {
                    this.tableData = res.data.result_data.list;
                    this.page.total = res.data.result_data.total;
                });
            },
            downloadFile(filePath){
                fileDownloadUtil(filePath);
            },
            add(){
                this.addEditVisible = true;
                this.addEditTitle = '添加';
                this.addSaveBtn = true;
                this.editSaveBtn = false;
                //initial clear
                for (var i = 0; i < this.pageProps.length; i++) { 
                 var prop = this.pageProps[i];
                 this.form[prop] = null;
                }
                //this.form['yearMonth'] = new Date().getFullYear() + "-" + (new Date().getMonth() + 1);
                this.fileList = [];
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
                        url: '/educationTrain/delete/' + this.selectUUIDs.join(',') +"/",
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
                    url: '/educationTrain/delete/' + rowData.uuid
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
                this.addSaveBtn = false;
                this.editSaveBtn = true; 
                for (var i = 0; i < this.pageProps.length; i++) { 
                 var prop = this.pageProps[i];
                 this.form[prop] = rowData[prop];
                } 
                //初始化附件列表：
                if(rowData.sign_attachment_path!=undefined && rowData.sign_attachment_path!=null && rowData.sign_attachment_path!=''){ 
                  let path = rowData.sign_attachment_path;
                  let name = path.substr(path.lastIndexOf('\\')+1); 
                  this.fileList1 = [{name:name,url:path}]; 
                }
                else{
                  this.fileList1 = [];
                }

                if(rowData.record_attachment_path!=undefined && rowData.record_attachment_path!=null && rowData.record_attachment_path!=''){ 
                  let path = rowData.record_attachment_path;
                  let name = path.substr(path.lastIndexOf('\\')+1); 
                  this.fileList2 = [{name:name,url:path}]; 
                }
                else{
                  this.fileList2 = [];
                }

                if(rowData.evaluation_attachment_path!=undefined && rowData.evaluation_attachment_path!=null && rowData.evaluation_attachment_path!=''){ 
                  let path = rowData.evaluation_attachment_path;
                  let name = path.substr(path.lastIndexOf('\\')+1); 
                  this.fileList3 = [{name:name,url:path}]; 
                }
                else{
                  this.fileList3 = [];
                }
            },
            closeAddEdit() {
                this.addEditVisible = false;
                this.searchDataList();    
            },
            beforeCloseAddEdit(done){
                this.$refs.addEdit.resetForm();
                done();
            },
            beforeCloseAddEdit_certificate(done){
                this.$refs.addEdit_certificate.resetForm('form');
                done();
            },
            closeAddEdit_certificate(){
                this.addEditVisible_certificate = false; 
                this.searchDataList_certificate();
            },
            beforeCloseAddEdit_special(done){
                this.$refs.addEdit_special.resetForm('form');
                done();
            },
            closeAddEdit_special(){
                this.addEditVisible_special = false; 
                this.searchDataList_special();
            },
            addCertificate(){
                this.addEditVisible_certificate = true;
                this.addEditTitle_certificate = '添加';
                this.addSaveBtn_certificate = true;
                this.editSaveBtn_certificate = false;
                //initial clear
                for (var i = 0; i < this.pageProps_certificate.length; i++) { 
                 var prop = this.pageProps_certificate[i];
                 this.form_certificate[prop] = null;
                }  
            },
            delCertificate(){
                if(this.selectUUIDs_certificate.length==0){
                  this.$alert('请选择需要删除的数据！','提示');
                }
                else{
                  this.$confirm('确认删除所选数据吗？','提示',{
                      confirmButtonText: '确定',
                      cancelButtonText: '取消',
                      type: 'warning'
                  }).then(() => {
                      this.$request({
                        url: '/staffCertificate/delete/' + this.selectUUIDs_certificate.join(',') +"/",
                        method:'get'
                      }).then(res => {
                        this.$message({
                          type: 'success',
                          message: '删除成功!'
                        });
                        this.searchDataList_certificate();//reload list
                        this.selectUUIDs_certificate=[];//clear
                      });
                    }).catch(()=>{
                        this.$message({type: 'info',message: '已取消删除'});
                    });
                }
            },
            handleSelectionChange_certificate(rows){
                this.selectUUIDs_certificate = [];//clear
                if(rows.length > 0){
                    rows.forEach(row => {
                        if(row){
                            this.selectUUIDs_certificate.push(row.uuid);
                        }
                    });
                } 
            },
            handleDelete_certificate(index,rowData){
                this.$confirm('确认删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request({
                    url: '/staffCertificate/delete/' + rowData.uuid
                    }).then(res => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.searchDataList_certificate();
                    }); 
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    }); 
                }); 
            },
            handleEdit_certificate(index,rowData){
                this.addEditVisible_certificate = true;
                this.addEditTitle_certificate = '修改';
                this.addSaveBtn_certificate = false;
                this.editSaveBtn_certificate = true; 
                for (var i = 0; i < this.pageProps_certificate.length; i++) { 
                    var prop = this.pageProps_certificate[i];
                    this.form_certificate[prop] = rowData[prop];
                } 
            },
            searchDataList_certificate(){
                var url = "/staffCertificate/getPageData/" + this.page_certificate.pageNum + "/" + this.page_certificate.pageSize;
                this.$request({
                    url: url,
                    data: {},
                    method: 'POST'
                }).then( res => {
                    this.tableData_certificate = res.data.result_data.list;
                    this.page_certificate.total = res.data.result_data.total;
                });
            },
            handleSizeChange_certificate(v){
                this.page_certificate.pageSize = v;
                this.searchDataList_certificate(); 
            },
            handleCurrentChange_certificate(v){
                this.page_certificate.pageNum = v;
                this.searchDataList_certificate();
            },
            searchDataList_certificate(){
                var url = "/staffCertificate/getPageData/" + this.page_certificate.pageNum + "/" + this.page_certificate.pageSize;
                this.$request({
                    url: url,
                    data: {},
                    method: 'POST'
                }).then( res => {
                    this.tableData_certificate = res.data.result_data.list;
                    this.page_certificate.total = res.data.result_data.total;
                });
            },
            addSpecial(){
                this.addEditVisible_special = true;
                this.addEditTitle_special = '添加';
                this.addSaveBtn_special = true;
                this.editSaveBtn_special = false;
                //initial clear
                for (var i = 0; i < this.pageProps_special.length; i++) { 
                 var prop = this.pageProps_special[i];
                 this.form_special[prop] = null;
                }  
            },
            delSpecial(){
                if(this.selectUUIDs_special.length==0){
                  this.$alert('请选择需要删除的数据！','提示');
                }
                else{
                  this.$confirm('确认删除所选数据吗？','提示',{
                      confirmButtonText: '确定',
                      cancelButtonText: '取消',
                      type: 'warning'
                  }).then(() => {
                      this.$request({
                        url: '/specialStaffCertificate/delete/' + this.selectUUIDs_special.join(',') +"/",
                        method:'get'
                      }).then(res => {
                        this.$message({
                          type: 'success',
                          message: '删除成功!'
                        });
                        this.searchDataList_special();//reload list
                        this.selectUUIDs_special=[];//clear
                      });
                    }).catch(()=>{
                        this.$message({type: 'info',message: '已取消删除'});
                    });
                }
            },
            handleSelectionChange_special(rows){
                this.selectUUIDs_special = [];//clear
                if(rows.length > 0){
                    rows.forEach(row => {
                        if(row){
                            this.selectUUIDs_special.push(row.uuid);
                        }
                    });
                } 
            },
            handleDelete_special(index,rowData){
                this.$confirm('确认删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$request({
                    url: '/specialStaffCertificate/delete/' + rowData.uuid
                    }).then(res => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.searchDataList_special();
                    }); 
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    }); 
                }); 
            },
            handleEdit_special(index,rowData){
                this.addEditVisible_special = true;
                this.addEditTitle_special = '修改';
                this.addSaveBtn_special = false;
                this.editSaveBtn_special = true; 
                for (var i = 0; i < this.pageProps_special.length; i++) { 
                    var prop = this.pageProps_special[i];
                    this.form_special[prop] = rowData[prop];
                }
            },
            handleSizeChange_special(v){
                this.page_special.pageSize = v;
                this.searchDataList_special();
            },
            handleCurrentChange_special(v){
                this.page_special.pageNum = v;
                this.searchDataList_special();
            },
            searchDataList_special(){
                var url = "/specialStaffCertificate/getPageData/" + this.page_certificate.pageNum + "/" + this.page_certificate.pageSize;
                this.$request({
                    url: url,
                    data: {},
                    method: 'POST'
                }).then( res => {
                    this.tableData_special = res.data.result_data.list;
                    this.page_special.total = res.data.result_data.total;
                });
            }
        },
        mounted: function(){
            this.searchDataList();
            this.searchDataList_certificate();
            this.searchDataList_special();
        }
    };
</script>

 