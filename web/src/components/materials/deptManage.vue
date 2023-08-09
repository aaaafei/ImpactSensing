<template>
    <div>
        <div style="margin:10px;"></div>
        <el-table :data="tableData" style="width:100%;" height="450"  :cell-style="{padding:'3px 0'}" > 
            <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
            <el-table-column label="品类" prop="category_name"  align="center"></el-table-column>
            <el-table-column label="库存量" prop="depts_num" width="300" align="center"></el-table-column>   
            <el-table-column label="操作" width="300" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" type="success" plain @click="inStorage(scope.$index, scope.row)">入库</el-button>
                    <el-button size="mini" type="danger" @click="outStorage(scope.$index, scope.row)">出库</el-button>   
                    <el-button size="mini" type="success" @click="getFromCompany(scope.$index, scope.row)">从公司领取</el-button>  
                </template>
            </el-table-column>
        </el-table> 
 

        <el-dialog :visible.sync="inStorageVisible" :title="inStorageTitle" width="60%">
            <inDeptStorage  @closeAddEdit="closeInStorage" :modelList="modelList"></inDeptStorage>
        </el-dialog>

        <el-dialog :visible.sync="outStorageVisible" :title="outStorageTitle" width="60%">
             <outDeptStorage  @closeAddEdit="closeOutStorage" :modelList="modelList"></outDeptStorage>
        </el-dialog>

        <el-dialog :visible.sync="getFromCompanyVisible" :title="getFromCompanyTitle" width="60%">
             <getFromCompany  @closeAddEdit="closeGetFromCompany" :modelList="modelList"></getFromCompany>
        </el-dialog>
 
    </div>
</template>

<script>
/* eslint-disable */ 
 
    import inDeptStorage from './inDeptStorage.vue';
    import outDeptStorage from './outDeptStorage.vue'; 
    import getFromCompany from './getFromCompany.vue';
    export default{ 
        components: {inDeptStorage, outDeptStorage, getFromCompany},
        data(){
            return {
                tableData: [],      
                inStorageVisible: false,
                outStorageVisible: false, 
                getFromCompanyVisible: false,
                modelList: [],
                inStorageTitle: null,
                outStorageTitle: null,
                getFromCompanyTitle: null
            };
        },
        methods: {  
            searchDataList(){
                var url = "/materialInventory/getDeptInventory";
                this.$request({
                    url: url, 
                    method: 'get'
                }).then( res => {  
                    this.tableData = res.data.result_data.list; 
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
            getFromCompany(index,rowData){
                this.getFromCompanyVisible = true; 
                this.getFromCompanyTitle = rowData.category_name + "  从公司领取"; 
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
            closeGetFromCompany(){
                this.getFromCompanyVisible = false;
                this.searchDataList();    
            }
        },
        mounted: function(){
            this.searchDataList();
        }
    };
</script>