<template>
    <div>
        <div style="margin:10px;">
            
            <el-row>
                <el-col :span="24"> 
                出入库/修正时间范围：
                <el-date-picker type="daterange" v-model="dateRange" value-format="yyyy-MM-dd" align="right" unlink-panels range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
                    <el-cascader v-model="org_id" placeholder="请选择部门" :show-all-levels="false" change-on-select :options="deptOptions" clearable></el-cascader> 
                    <el-select v-model="category_id" placeholder="请选择品类" clearable>
                        <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                    <el-select v-model="op_type" placeholder="请选择操作类型" clearable>
                        <el-option label="入库" value="1"></el-option>
                        <el-option label="出库" value="2"></el-option>
                        <el-option label="公司下发" value="3"></el-option>
                        <el-option label="下发入库" value="4"></el-option>
                        <el-option label="领取入库" value="5"></el-option>
                        <el-option label="领取下发" value="6"></el-option>  
                        <el-option label="修改库存" value="7"></el-option>  
                      </el-select>
                    <el-button type="primary" icon="el-icon-search" @click="searchDataList">搜索</el-button>
                    <el-button type="primary" icon="el-icon-download" @click="exportExcel">导出excel</el-button>
                </el-col>
            </el-row>
        </div>

        <el-table :data="tableData" style="width:100%;" height="450"  :cell-style="{padding:'3px 0'}"> 
            <el-table-column label="序号" type="index" :index="indexMethod" width="50" align="center"></el-table-column>
            <el-table-column label="部门" prop="dept" width="120" align="center" show-overflow-tooltip="true"></el-table-column>  
            <el-table-column label="品类" prop="category_name" align="center" show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="型号" width="100" show-overflow-tooltip="true" prop="model_name" align="center"></el-table-column>
            <el-table-column label="操作类型" prop="op_type" align="center" width="120" show-overflow-tooltip="true">
                <template slot-scope="scope"> 
                    {{ opType(scope.row) }} 
                </template> 
            </el-table-column>
            <el-table-column label="出入库/修正时间" prop="time_in_out" align="center" width="140"></el-table-column>
            <el-table-column label="操作数量" prop="num" align="center" width="80"></el-table-column>
            <el-table-column label="操作前数量" prop="before_num" align="center" width="90"></el-table-column>
            <el-table-column label="操作后数量" prop="after_num" align="center" width="90"></el-table-column>
            <el-table-column label="操作人" prop="op_name" align="center" width="80"></el-table-column>
            <el-table-column label="操作时间" prop="op_time" align="center" width="140"></el-table-column> 
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

    </div>
</template>

<script>
/* eslint-disable */ 
    export default{ 
        data(){
            return {
                tableData: [], 
                page: {
                    pageNum: 1,
                    pageSize: 10,
                    total: 0
                },
                org_id: [],
                category_id: null,
                deptOptions: [],
                categoryList: [],
                op_type: null,
                dateRange: null
            };
        },
        methods: { 
            indexMethod (index) {
                return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);  
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
                let q = {}; 
                if(this.dateRange !=null ){ 
                    q["time_in_out_range_begin"] = this.dateRange[0] + ' 00:00:00';
                    q["time_in_out_range_end"] = this.dateRange[1] + ' 23:59:59';  
                } 
                if (this.category_id != null && this.category_id != "") {
                  q["category_id"] = this.category_id;
                }
                if(this.org_id!=null && this.org_id != '' && this.org_id.length > 0){
                  q['dept_id'] = this.org_id[this.org_id.length-1]; 
                }
                if (this.op_type != null && this.op_type != "") {
                  q["op_type"] = this.op_type;
                }
                var url = "/materialOpDetail/getPageData/" + this.page.pageNum + "/" + this.page.pageSize;
                this.$request({
                    url: url,
                    data: q,
                    method: 'POST'
                }).then( res => {
                    this.tableData = res.data.result_data.list;
                    this.page.total = res.data.result_data.total;
                });
            },
            loadDeptCascader(){
                //判断人员是否可以查看所有进出库信息
                var user = this.$store.getters.getUserInfo;
                var roles = user.roles;
                var isManage = false;
                for (var i in roles) {
                  if (roles[i].name == "Company_Material_Manage") {
                    isManage = true;
                    break;
                  }
                }
                let method;
                if(isManage)
                    method = 'post';//不判断，所有
                else
                    method = 'get';//是is admin判断


                var url = '/dept/get/all';
                this.$request({
                    url: url,
                    method: method
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
            loadCategory(){
                var url = '/materialCategory/getPageData/0/0';
                this.$request({
                    url: url,
                    data: {},
                    method: 'post'
                }).then(res => {
                    this.categoryList = res.data.result_data.list; 
                }); 
            },
            opType(rowData){
                var type = {'1':'入库', '2':'出库', '3':'公司下发', '4': '下发入库', '5': '领取入库', '6': '领取下发', '7': '修改库存'};
                if(rowData.op_type==3){
                    return '公司下发(至' + rowData.receive_dept +')';
                }
                else{
                    if(rowData.op_type==6){
                    return '领取下发(至' + rowData.receive_dept +')';
                    }
                    else
                        return type[rowData.op_type];
                }
            },
            exportExcel(){
                let q = {};
                if(this.dateRange !=null ){ 
                    q["time_in_out_range_begin"] = this.dateRange[0] + ' 00:00:00';
                    q["time_in_out_range_end"] = this.dateRange[1] + ' 23:59:59';  
                } 
                if (this.category_id != null && this.category_id != "") {
                  q["category_id"] = this.category_id;
                }
                if(this.org_id!=null && this.org_id != '' && this.org_id.length > 0){
                  q['dept_id'] = this.org_id[this.org_id.length-1]; 
                }
                if (this.op_type != null && this.op_type != "") {
                  q["op_type"] = this.op_type;
                } 
                this.$request({
                    url: '/materialOpDetail/exportExcel/',
                    data: q,
                    method: 'post',
                    responseType: 'blob'
                }).then(res => { 
                    let disposition = res.headers['content-disposition'];
                    console.log(disposition);
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
        mounted:function(){
            this.loadCategory();
            this.loadDeptCascader();
            this.searchDataList();
        }
    };
</script>