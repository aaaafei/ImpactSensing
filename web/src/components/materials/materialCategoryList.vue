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
            <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
            <el-table-column label="品类" prop="name"  align="center"></el-table-column>  
            <el-table-column label="排序号" prop="sort_num" width="200" align="center"></el-table-column> 
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" type="success" plain @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleModelSet(scope.$index, scope.row)">型号设置</el-button>   
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

       
        <el-dialog :visible.sync="modelSetVisible" width="50%" :title="modelSetTitle" append-to-body>
            <mdoelSet @closeAddEdit="closeAddEdit" :modelSetList="modelSetList" :modelSetListTotal="modelSetListTotal" :category="category_id"></mdoelSet>
        </el-dialog>


    </div>
</template>

<script>
/* eslint-disable */
    import addEdit from './materialCategoryWrite.vue'; 
    import mdoelSet from './materialModelList.vue'; 
    export default{
        components: {addEdit,mdoelSet},
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
                pageProps: ['id', 'name', 'sort_num'],
                form: {id: null, name: null, sort_num: null},
                modelSetVisible: false,
                modelSetTitle: '',
                modelSetList: [],
                modelSetListTotal: 0,
                category_id: null
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
                var url = "/materialCategory/getPageData/" + this.page.pageNum + "/" + this.page.pageSize;
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
                        url: '/materialCategory/delete/' + this.selectUUIDs.join(',') +"/",
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
            handleModelSet(index,rowData){
                var url = "/materialModel/getPageData/1/10";
                this.$request({
                    url: url,
                    data: {category_id: rowData.id},
                    method: 'POST'
                }).then( res => {
                    this.modelSetList = res.data.result_data.list;
                    this.modelSetListTotal = res.data.result_data.total;
                    this.category_id = rowData.id;

                    this.modelSetVisible = true;
                    this.modelSetTitle = rowData.name+'  型号设置';
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
        mounted: function(){
            this.searchDataList();
        }
    };
</script>