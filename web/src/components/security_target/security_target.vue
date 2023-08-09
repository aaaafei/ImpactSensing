<template>
    <div>
        <div style="margin:10px;">
            <el-button-group>
                <el-button type="primary" icon="el-icon-plus" @click="add">新增</el-button>
                <el-button type="primary" icon="el-icon-delete" @click="del">删除</el-button>
            </el-button-group>
        </div>
        <el-table :data="tableData" style="width:100%;" height="450" @selection-change="handleSelectionChange"  :cell-style="{padding:'3px 0'}">
            <el-table-column type="selection" width="29"></el-table-column>
            <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
            <el-table-column label="年度" prop="year" width="120" align="center"></el-table-column> 
            <el-table-column label="部门" prop="org_name" width="350" align="center"></el-table-column>
            <el-table-column label="目标／责任状" header-align="center">
                <template slot-scope="scope">
                    <el-button type="text" size="small" @click="downloadFile(scope.row.file_path)">
                        {{ scope.row.file_path!=null ? scope.row.file_path.substr(scope.row.file_path.lastIndexOf('\\')+1) : '' }}
                    </el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200" align="center">
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
        <el-dialog :visible.sync="addEditVisible" width="50%" :title="addEditTitle" append-to-body>
            <addEdit @closeAddEdit="closeAddEdit" :detailForm="form" :files="fileList" :addSaveBtn="addSaveBtn" :editSaveBtn="editSaveBtn"></addEdit>
        </el-dialog>
    </div>
</template>

<script>
/* eslint-disable */
    import addEdit from './add_edit.vue';
    import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
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
                addSaveBtn: false,
                editSaveBtn: false,
                pageProps: ['uuid', 'year', 'file_path'],
                form: {uuid: null, year: undefined, file_path: null },
                fileList: []
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
                console.log(this.selectUUIDs);
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
                var url = "/securityTarget/getPageData/" + this.page.pageNum + "/" + this.page.pageSize;
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
                this.form['year'] =  new Date().getFullYear();
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
                        url: '/securityTarget/delete/' + this.selectUUIDs.join(',') +"/",
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
                    url: '/securityTarget/delete/' + rowData.uuid
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
                if(rowData.file_path!=undefined && rowData.file_path!=null && rowData.file_path!=''){
                  let path = rowData.file_path;
                  let name = path.substr(path.lastIndexOf('\\')+1); 
                  this.fileList = [{name:name,url:path}]; 
                }
                else{
                  this.fileList = [];
                }
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