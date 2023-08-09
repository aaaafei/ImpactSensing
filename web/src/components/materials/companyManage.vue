<template>
    <div>
        <el-tabs type="border-card" v-model="activeName" >
           <el-tab-pane label="公司汇总" name="1">    
                <div style="margin:10px;">
                    <el-button-group>
                        <el-button type="primary" icon="el-icon-plus" @click="categorySetVisible=true">设置物资种类</el-button> 
                    </el-button-group>
                </div>
                <el-table :data="tableData" style="width:100%;" height="450"  :cell-style="{padding:'3px 0'}" > 
                    <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
                    <el-table-column label="品类" prop="category_name"  align="center"></el-table-column>
                    <el-table-column label="基层单位库存量" prop="depts_num" width="150" align="center"></el-table-column> 
                    <el-table-column label="公司库存量" prop="company_num" width="150" align="center"></el-table-column> 
                    <el-table-column label="总库存" prop="total_num" width="150" align="center"></el-table-column> 
                    <el-table-column label="操作" width="300" align="center">
                        <template slot-scope="scope">
                            <el-button size="mini" type="success" plain @click="inStorage(scope.$index, scope.row)">入库</el-button>
                            <el-button size="mini" type="danger" @click="outStorage(scope.$index, scope.row)">出库</el-button>  
                            <el-button size="mini" type="success" @click="grant2Dept(scope.$index, scope.row)">下发基层单位</el-button> 
                        </template>
                    </el-table-column>
                </el-table> 
           </el-tab-pane>

           <el-tab-pane label="各部门明细" name="2">   


                <el-row>
                    <el-col :span="24">
                        <el-cascader v-model="org_id" placeholder="请选择部门" :show-all-levels="false" change-on-select :options="deptOptions" clearable @change="onCascaderChange"></el-cascader> 
                    </el-col>
                </el-row>

                <el-table :data="deptTableData" style="width:100%;" height="450"  :cell-style="{padding:'3px 0'}" > 
                    <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
                    <el-table-column label="品类" prop="category_name"  align="center"></el-table-column>
                    <el-table-column label="库存量" prop="depts_num" width="300" align="center"></el-table-column>   
                    <el-table-column label="操作" width="300" align="center">
                        <template slot-scope="scope"> 
                            <el-button size="mini" type="danger" @click="changeStorage(scope.$index, scope.row)">修正库存</el-button>    
                        </template>
                    </el-table-column>
                </el-table> 

           </el-tab-pane>

        </el-tabs>

 


        <el-dialog :visible.sync="categorySetVisible" title="设置物资种类" width="80%"> 
             <materialCategoryList></materialCategoryList>
        </el-dialog>
   
    

        <el-dialog :visible.sync="inStorageVisible" :title="inStorageTitle" width="60%">
            <inCompanyStorage  @closeAddEdit="closeInStorage" :modelList="modelList"></inCompanyStorage>
        </el-dialog>


        <el-dialog :visible.sync="outStorageVisible" :title="outStorageTitle" width="60%">
             <outCompanyStorage  @closeAddEdit="closeOutStorage" :modelList="modelList"></outCompanyStorage>
        </el-dialog>

        <el-dialog :visible.sync="grant2DeptVisible" :title="grant2DeptTitle" width="50%">
             <grant2Dept  @closeAddEdit="closeGrant2Dept" :modelList="modelList"></grant2Dept>
        </el-dialog>

        <el-dialog :visible.sync="changeStorageVisible" :title="changeStorageTitle" width="60%">
            <changeStorage  @closeAddEdit="closeChangeStorage" :deptid="org_id" :modelList="modelList"></changeStorage>
        </el-dialog>

    </div>
</template>

<script>
/* eslint-disable */ 

    import materialCategoryList from './materialCategoryList.vue';
    import inCompanyStorage from './inCompanyStorage.vue';
    import outCompanyStorage from './outCompanyStorage.vue';
    import grant2Dept from './grant2Dept.vue';
    import changeStorage from './changeStorage';
    export default{ 
        components: {materialCategoryList, inCompanyStorage, outCompanyStorage, grant2Dept, changeStorage},
        data(){
            return {
                activeName: '1',
                tableData: [],     
                categorySetVisible: false,
                inStorageVisible: false,
                outStorageVisible: false,
                grant2DeptVisible: false,
                modelList: [],
                inStorageTitle: null,
                outStorageTitle: null,
                grant2DeptTitle: null,
                deptTableData: [],
                deptOptions: [],
                org_id: null,
                changeStorageVisible: false,
                changeStorageTitle: null,
                deptTableData: []
            };
        },
        methods: {  
            searchDataList(){
                var url = "/materialInventory/getCompanyInventory";
                this.$request({
                    url: url,
                    data: {},
                    method: 'POST'
                }).then( res => {
                     res.data.result_data.list.forEach(data => {
                        if(data.company_num!=undefined && data.company_num!=null && data.company_num!=''&&
                            data.depts_num!=undefined && data.depts_num!=null && data.depts_num!='')
                            data.total_num = data.company_num +  data.depts_num;
                            
                    });

                    this.tableData = res.data.result_data.list; 
                });
            },  
            searchDeptTableDataList(deptid){
                var url = "/materialInventory/getInventoryByDeptId";
                url += "/" + deptid; 
                this.$request({
                    url: url, 
                    method: 'get'
                }).then( res => {  
                    this.deptTableData = res.data.result_data.list; 
                });
            },  
            inStorage(index,rowData){
                this.inStorageVisible = true; 
                this.inStorageTitle = rowData.category_name + "  入库"; 
                var url = "/materialModel/getPageData/0/0";
                this.$request({
                    url: url,
                    data: {category_id: rowData.category_id},
                    method: 'POST'
                }).then( res => {
                    this.modelList = res.data.result_data.list; 
                });
            },  
            outStorage(index,rowData){
                this.outStorageVisible = true; 
                this.outStorageTitle = rowData.category_name + "  出库"; 
                var url = "/materialModel/getPageData/0/0";
                this.$request({
                    url: url,
                    data: {category_id: rowData.category_id},
                    method: 'POST'
                }).then( res => {
                    this.modelList = res.data.result_data.list; 
                });
            },  
            grant2Dept(index,rowData){
                this.grant2DeptVisible = true; 
                this.grant2DeptTitle = rowData.category_name + "  下发基层单位"; 
                var url = "/materialModel/getPageData/0/0";
                this.$request({
                    url: url,
                    data: {category_id: rowData.category_id},
                    method: 'POST'
                }).then( res => {
                    this.modelList = res.data.result_data.list; 
                });
            },
            closeInStorage() {
                this.inStorageVisible = false;
                this.searchDataList();    
            },
            closeOutStorage(){
                this.outStorageVisible = false;
                this.searchDataList();    
            },
            closeGrant2Dept(){
                this.grant2DeptVisible = false;
                this.searchDataList();  
            },
            loadDeptCascader(){
                var url = '/dept/get/all';
                this.$request({
                    url: url,
                    method: 'post'
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
            onCascaderChange(val){
                if(val.length>0)
                    this.searchDeptTableDataList(val[val.length-1]);
                else
                    this.deptTableData = [];
            },
            changeStorage(index,rowData){  
                if(rowData.depts_num != undefined){
                    this.changeStorageVisible = true;      
                    this.changeStorageTitle = rowData.category_name + "  修改库存";  
                    var url = "/materialModel/getPageData/0/0";
                    this.$request({
                        url: url,
                        data: {category_id: rowData.category_id},
                        method: 'POST'
                    }).then( res => {
                        this.modelList = res.data.result_data.list; 
                    });   
                }
                else{
                    this.$message({message: '无原始库存，请通过出入库方式操作！',type: 'warning'});
                }
            },
            closeChangeStorage(){
                this.changeStorageVisible = false; 
                this.searchDeptTableDataList(this.org_id[this.org_id.length-1]);
            }
        },
        mounted: function(){
            this.searchDataList();
            this.loadDeptCascader();
        }
    };
</script>