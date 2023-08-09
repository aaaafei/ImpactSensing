<template>
    <div>
        <div style="margin:10px;">
            <el-button-group>
                <el-button type="primary" icon="el-icon-plus" @click="add">新增</el-button>
                <el-button type="primary" icon="el-icon-delete" @click="del">删除</el-button>
            </el-button-group>
        </div>
        <el-table :data="tableData" style="width:100%;" height="600" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
            <el-table-column label="型号" prop="model_name" width="200" align="center"></el-table-column>  
            <el-table-column label="排序号" prop="sort_num" align="center"></el-table-column> 
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" type="success" plain @click="handleEdit(scope.$index, scope.row)">编辑</el-button>  
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

       
    </div>
</template>

<script>
/* eslint-disable */
    import addEdit from './materialModelWrite.vue'; 
    export default{
        components: {addEdit},
        props: ["modelSetList","modelSetListTotal","category"],
        data(){
            return {
                category_id: this.category,
                tableData: this.modelSetList,
                selectUUIDs: [],
                page: {
                    pageNum: 1,
                    pageSize: 10,
                    total: this.modelSetListTotal
                },
                addEditTitle: '',
                addEditVisible: false, 
                pageProps: ['id','category_id','model_name', 'sort_num'],
                form: {id: null, category_id: null,model_name:null, sort_num: null}
            };
        },
        methods: {
            handleSelectionChange(rows){
                this.selectUUIDs = [];//clear
                if(rows.length > 0){
                    rows.forEach(row => {
                        if(row){
                            this.selectUUIDs.push(row.id);
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
                var url = "/materialModel/getPageData/" + this.page.pageNum + "/" + this.page.pageSize;
                this.$request({
                    url: url,
                    data: {category_id: this.category_id},
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
                this.form['category_id'] = this.category_id; 
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
                        url: '/materialModel/delete/' + this.selectUUIDs.join(',') +"/",
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
                    url: '/materialModel/delete/' + rowData.uuid
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
            }
        },
        watch:{
            modelSetList(value,oldvalue){ 
                this.tableData = value;
            },
            modelSetListTotal(value,oldvalue){
                this.page.total = value;
            },
            category(value,oldvalue){
                this.category_id = value;
            } 
        }
    };
</script>