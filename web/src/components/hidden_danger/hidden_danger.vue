<template> 
  <div>  
    <div style="margin: 10px;">
      <el-row>
        <el-col :span="4">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="addData">新增</el-button>   
            <el-button type="primary" icon="el-icon-delete" @click="deleteDatas">删除</el-button>
          </el-button-group>
           </el-col>
          <el-col :span="20">
          年度： 
          <el-input-number v-model="year" style="width:120px;" controls-position="right" :step="1"></el-input-number>
          <el-cascader v-model="org_id" placeholder="请选择部门" :show-all-levels="false" change-on-select :options="deptOptions" clearable></el-cascader> 
           <el-select v-model="deal_type" placeholder="请选择治理状态" clearable>
            <el-option label="未处理" value="1"></el-option>
            <el-option label="处理中" value="2"></el-option>
            <el-option label="处理完" value="3"></el-option>
          </el-select>
          <el-input style="width:200px;" v-model="qSummary" placeholder="请输入隐患概要信息"></el-input>
          <el-button type="primary" icon="el-icon-search" @click="searchDataList">搜索</el-button>
        </el-col>
      </el-row>   
    </div> 
    <el-table :data="tableData" style="width: 100%" max-height="600" @selection-change="handleSelectionChange" highlight-current-row  :cell-style="{padding:'3px 0'}">
      <el-table-column type="selection" width="29" header-align="center" :selectable='checkboxInit'></el-table-column>
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" align="center"></el-table-column>
      <el-table-column prop="year" label="年份" width="55" align="center"></el-table-column>
      <el-table-column prop="org_name" label="上报部门"   align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="principal_name" label="上报人" width="65" align="center"></el-table-column>
      <el-table-column prop="duty_org_name" label="责任部门/单位" width="115" align="center" :show-overflow-tooltip="true"></el-table-column> 
      <el-table-column prop="discovery_time" label="发现时间" width="158" align="center"></el-table-column>
      <el-table-column prop="expires" label="治理期限" width="78" align="center">
        <template slot-scope="scope"> 
               {{ scope.row.expires+'天' }}
          </template>
      </el-table-column>
      <el-table-column  label="整改前附件" align="center" width="90" >
        <template slot-scope="scope">
          <el-button type="text" size="small" v-if="!checkIsImg(scope.row.attachment1!=null ? scope.row.attachment1.substr(scope.row.attachment1.lastIndexOf('\\')+1) : '')" @click="downloadFile(scope.row.attachment1)">
             {{ scope.row.attachment1!=null ? scope.row.attachment1.substr(scope.row.attachment1.lastIndexOf('\\')+1) : '' }}
          </el-button>
          <img v-if="checkIsImg(scope.row.attachment1!=null ? scope.row.attachment1.substr(scope.row.attachment1.lastIndexOf('\\')+1) : '')" :src="'/api/file/download?filePath='+ encodeURI(encodeURI(scope.row.attachment1))" style="width:100px;" /> 
        </template>
      </el-table-column> 
      <el-table-column  label="整改后附件" align="center" width="90" > 
        <template slot-scope="scope">
          <el-button type="text" size="small" v-if="!checkIsImg(scope.row.attachment2!=null ? scope.row.attachment2.substr(scope.row.attachment2.lastIndexOf('\\')+1) : '')" @click="downloadFile(scope.row.attachment2)">
             {{ scope.row.attachment2!=null ? scope.row.attachment2.substr(scope.row.attachment2.lastIndexOf('\\')+1) : '' }}
          </el-button>
          <img v-if="checkIsImg(scope.row.attachment2!=null ? scope.row.attachment2.substr(scope.row.attachment2.lastIndexOf('\\')+1) : '')" :src="'/api/file/download?filePath='+ encodeURI(encodeURI(scope.row.attachment2))" style="width:100px;" /> 
        </template>
      </el-table-column> 
      <el-table-column label="治理状态" align="center" width="78">
          <template slot-scope="scope"> 
               {{ statusStr(scope.row.status) }}
          </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="217">
        <template slot-scope="scope">        
          <el-button size="mini" @click="view(scope.row)" >查看</el-button>
          <el-button size="mini" type="success" plain @click="handleEdit(scope.$index, scope.row)"  v-if="scope.row.proc_inst_id==null">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)"  v-if="scope.row.proc_inst_id==null">删除</el-button>
          <el-button size="mini" type="success" @click="processInfo(scope.row.proc_inst_id)" v-if="scope.row.proc_inst_id!=null">流转情况</el-button>  
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

    <!-- add/edit dialog -->
    <el-dialog :visible.sync="addEditVisible" :title="addEditDialogTitle" width="80%" :before-close="beforeCloseAddEdit">
        <addEditComponent ref="addEditComponent" @closeAddEdit="closeAddEdit" :formDetail="formDetail" :fileListA="fileList1" :fileListB="fileList2" :fileListC="fileList3" :usersSelect="usersSelect" :addSaveBtn="addSaveBtn" :editSaveBtn="editSaveBtn"></addEditComponent>
    </el-dialog>

    <!-- view dialog -->
    <el-dialog :visible.sync="viewVisible" title="查看" width="80%">
        <viewComponent ref="viewComponent" @closeView="closeView" :securityDeptRearrange="securityDeptRearrange" :taskId="taskId" :formDetail="formDetail" :fileListA="fileList1" :fileListB="fileList2" :fileListC="fileList3"></viewComponent>
    </el-dialog>

    <el-dialog :visible.sync="processPicVisible" title="流程流转情况" width="80%">
        <el-table :data="tableTaskProcess" style="width:100%" height="300" highlight-current-row> 
                <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
                <el-table-column prop="activityName" label="活动名称" width="250" align="center"></el-table-column>
                <el-table-column prop="assigneeName" label="办理人" width="200" align="center"></el-table-column>
                <el-table-column prop="comment" label="处理意见" align="center"></el-table-column>
                <el-table-column prop="startTime" label="任务发送时间" width="200" align="center"></el-table-column>
                <el-table-column prop="endTime" label="处理结束时间" width="200" align="center"></el-table-column>
        </el-table> 
        <img :src="processPicUrl" class="image">
    </el-dialog>

  </div>
</template>
 

<script>
/* eslint-disable */
import addEditComponent from './hidden_danger_add_edit.vue';
import viewComponent from './hidden_danger_view.vue';
import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
  export default {
    components:{addEditComponent,viewComponent},
    data () {
      return {
        api: {
          URL_list: '/hiddenDanger/getPageData/',
          URL_del: '/hiddenDanger/delete/',
          URL_downloadFile: '/file/download/'
        },
        tableData: [],
        selectUUIDs: [],
        selectRow:null,
        editRow:null,//用于给编辑页初始化数据用
        page:{pageNum:1,pageSize:10,total:0},
        addEditVisible : false,
        addEditDialogTitle: '',
        pageProps: ['uuid', 'year', 'summary', 'plan', 'principal','principal_name', 'discovery_time', 'expires', 'attachment1', 'attachment2','attachment3', 'status', 'begin_deal', 'end_deal','org_name','org_id','level','duty_org_name','duty_org_id','receiver','proc_inst_id'],
        formDetail:{uuid:null,year:null,summary:null,plan:null,principal:null,principal_name:null,discovery_time:null,expires:null,attachment1:null,attachment2:null,attachment3:null,status:null,begin_deal:null,end_deal:null,org_name:null,org_id:null,level:null,duty_org_name:null,duty_org_id:null,receiver:null,proc_inst_id:null},
        fileList1:[],
        fileList2:[],
        fileList3:[],
        addSaveBtn: false,
        editSaveBtn: false,
        qSummary:null,
        processPicUrl: null,
        processPicVisible: false,
        tableTaskProcess: [],
        viewVisible: false,
        usersSelect: [],
        securityDeptRearrange: false,
        taskId: null,
        year: new Date().getFullYear(),
        deptOptions: [],
        deal_type: null,
        org_id: []
      };
    },
    methods: {
      statusStr(s){
        switch(s){
          case 1:
            return '未处理';
          case 2:
            return '处理中';
          case 3:
            return '处理完';
          }
      },
      downloadFile(filePath){ 
         fileDownloadUtil(filePath); 
      },
      closeAddEdit(){
        this.searchDataList();
        this.addEditVisible = false;
      },
      beforeCloseAddEdit(done){
        this.$refs.addEditComponent.resetForm();
        done();
      },
      addData(){
        this.$request({
            url:'/user/getUserInfo',
            method:'get'
        }).then(res => {
            this.addEditVisible = true;
            this.addEditDialogTitle = '添加';
            this.addSaveBtn = true;
            this.editSaveBtn = false;
            for(var i=0;i<this.pageProps.length;i++){
              if(this.pageProps[i]=='year') {this.formDetail['year'] = new Date().getFullYear(); continue;}
              if(this.pageProps[i]=='status') {this.formDetail['status'] = 1; continue;}
              if(this.pageProps[i]=='expires') { this.formDetail[this.pageProps[i]] = undefined; continue;}
              this.formDetail[this.pageProps[i]] = null;
            }  
            this.fileList1 = [];
            this.fileList2 = [];
            this.fileList3 = [];
            this.formDetail['org_id'] = res.data.result_data.organid;
            this.formDetail['org_name'] = res.data.result_data.organname;
            this.formDetail['principal'] = res.data.result_data.id;
            this.formDetail['principal_name'] = res.data.result_data.truename; 
        });;
        
      },
      indexMethod (index) {
        return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);  
      },
      handleSizeChange(val){
          this.page.pageSize = val;
          this.searchDataList ();
      },
      handleCurrentChange(val){
          this.page.pageNum = val;
          this.searchDataList();
      },
      view(val) { 
          this.taskId = null;//先清空值 
          this.editRow = val;  
         for(var i=0;i<this.pageProps.length;i++){ 
            this.formDetail[this.pageProps[i]] = this.editRow[this.pageProps[i]];
          }
          //初始化附件列表：
          if(this.editRow.attachment1!=null && this.editRow.attachment1!=''){
            let path = this.editRow.attachment1;
            let name = path.substr(path.lastIndexOf('\\')+1); 
            this.fileList1 = [{name:name,url:path}]; 
          }
          else{
            this.fileList1 = [];
          }
          if(this.editRow.attachment2!=null && this.editRow.attachment2!=''){
            let path = this.editRow.attachment2;
            let name = path.substr(path.lastIndexOf('\\')+1);
            this.fileList2 = [{name:name,url:this.editRow.attachment2}]; 
          }
          else{
            this.fileList2 = [];
          }
          if(this.editRow.attachment3!=null && this.editRow.attachment3!=''){
            let path = this.editRow.attachment3;
            let name = path.substr(path.lastIndexOf('\\')+1);
            this.fileList3 = [{name:name,url:this.editRow.attachment3}]; 
          }
          else{
            this.fileList3 = [];
          }
 
        let userInfo = this.$store.getters.getUserInfo;
        let roles = userInfo.roles;
        let is_MSS_PRINCIPAL = false;
        for(var i=0;i<roles.length;i++){
            if(roles[i].name == 'ROLE_MSS_PRINCIPAL'){
              is_MSS_PRINCIPAL= true;
              break;
            }
        }

        if(is_MSS_PRINCIPAL && val.proc_inst_id!=null){
          this.$request({
            url:'/workflow/getActiveTask/'+ val.proc_inst_id,
            method:'get'
            }).then(res => {
                if(res.data.result_data != undefined && res.data.result_data.taskDefinitionKey!=undefined && res.data.result_data.taskDefinitionKey!=null && res.data.result_data.taskDefinitionKey=='securityDept'){
                  this.securityDeptRearrange= true;
                  this.taskId = res.data.result_data.taskId;
                }
                else{
                  this.securityDeptRearrange= false;
                }
                this.viewVisible = true;
            });
        }
        else{ 
          this.securityDeptRearrange= false;
          this.viewVisible = true;
        }

      }, 
      handleEdit(index,rowData){  
        this.addEditDialogTitle = '编辑'; 
        this.addSaveBtn = false;
        this.editSaveBtn = true;
        for(var i=0;i<this.pageProps.length;i++){ 
          this.formDetail[this.pageProps[i]] = rowData[this.pageProps[i]];
        }
        //初始化附件列表：
        if(rowData.attachment1!=null && rowData.attachment1!=''){
          let path = rowData.attachment1;
          let name = path.substr(path.lastIndexOf('\\')+1); 
          this.fileList1 = [{name:name,url:path}]; 
        }
        else{
          this.fileList1 = [];
        }
        if(rowData.attachment2!=null && rowData.attachment2!=''){
          let path = rowData.attachment2;
          let name = path.substr(path.lastIndexOf('\\')+1);
          this.fileList2 = [{name:name,url:rowData.attachment2}]; 
        }
        else{
          this.fileList2 = [];
        }
        if(rowData.attachment3!=null && rowData.attachment3!=''){
            let path = rowData.attachment3;
            let name = path.substr(path.lastIndexOf('\\')+1);
            this.fileList3 = [{name:name,url:rowData.attachment3}]; 
        }
        else{
          this.fileList3 = [];
        }

        if(rowData.duty_org_id!=undefined&&rowData.duty_org_id!=null&&rowData.duty_org_id!=''){//初始化接收安全员select options 
          var roleNames = 'ROLE_BASE_OFFICER,ROLE_DEPT_OFFICER';
          this.$request({
              url: '/user/getUserByOrgRole/'+rowData.duty_org_id+'/'+roleNames,
              method: 'get'
          }).then(res => {
              this.usersSelect = res.data.result_data;  
              this.addEditVisible = true; 
          });
        }
        else{
          this.addEditVisible = true; 
        } 
      },
      handleDelete(idnex,rowData){
          this.$confirm('确认删除所选数据吗？','提示',{
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
          }).then(() => {
              this.$request({
                url: this.api.URL_del + rowData.uuid +"/"
              }).then(res => {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
                this.searchDataList(); 
              });
            }).catch(()=>{
              this.$message({type: 'info',message: '已取消删除'});
            }
          );
      },
      deleteDatas () {
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
                url: this.api.URL_del + this.selectUUIDs.join(',') +"/"
              }).then(res => {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
                this.searchDataList();
                this.selectUUIDs=[];
              });
            }).catch(()=>{
              this.$message({type: 'info',message: '已取消删除'});
            }
            );
        }
      },
      handleSelectionChange(rows) {
        this.selectRow = rows; 
        this.selectUUIDs=[];
        if(rows.length>0){
          rows.forEach(row => {
            if (row) {
                this.selectUUIDs.push(row.uuid);
            }
          });
        }
      },
      searchDataList () { 
        let q = {};
        if(this.qSummary!=null && this.qSummary!=''){
          q['summary'] = this.qSummary;
        }
        if(this.$route.query.org_id != null && this.$route.query.org_id != '')
          q['org_id'] = this.$route.query.org_id;
        if(this.org_id!=null && this.org_id != '' && this.org_id.length > 0){

          
          q['org_id'] = this.org_id[this.org_id.length-1]; 
        }
        if(this.year!=null && this.year != '')
          q['year'] = this.year;
        if(this.deal_type != null && this.deal_type != '')
          q['status'] = this.deal_type;
        this.$request({
          url: this.api.URL_list + this.page.pageNum + '/' + this.page.pageSize,
          method: 'post',
          data:q
        }).then(res => {
          let data = res.data;
          this.tableData = data.result_data.list;
          this.page.total=data.result_data.total;
        });
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
      }, 
      checkboxInit(row,index){ 
    　　if (row.proc_inst_id!=null)//这个判断根据你的情况而定
    　　　　return 0;//不可勾选
    　　else
    　　　　return 1;//可勾选
  　　},
      closeView(){
            this.viewVisible = false;
      },
      checkIsImg(fileName){ 
          var isImg = false;
          var suffix = fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase(); 
          var imgFormat = ['bmp','jpg','jpeg','png','gif'];
          imgFormat.forEach(row => { 
            if(suffix==row){ 
              isImg = true; 
            }
          });
          return isImg;
      },
      loadDeptCascader(){
        var url = '/dept/get/all';
        this.$request({
            url: url,
            method: 'get'
        }).then(res => {
            let data = res.data.result_data;
            this.deptOptions = this.formatDeptData(data); 
            this.org_id = this.getFullDeptValue(this.deptOptions,this.$route.query.org_id,[]);
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
      getFullDeptValue(data,value,parentPath){  
        var originLength = parentPath.length;
        if(data!=undefined && data!=null && data.length>0) {
          for(var i=0;i<data.length;i++){ 
            parentPath.push(data[i].value); 
            if(data[i].value==value){ 
              return parentPath;
            }
            if(data[i].children!=undefined && data[i].children!=null && data[i].children.length>0){  
              parentPath = this.getFullDeptValue(data[i].children,value,parentPath); 
            }
            else{
              parentPath.pop(); 
            }
          } 
          if(parentPath.length == originLength && parentPath.length>0){
              parentPath.pop();
          } 
          return parentPath;
        }
        else{ 
          return [];
        }
      }
    },
    mounted:function(){
      if(this.$route.query.year != undefined && this.$route.query.year != null && this.$route.query.year != '')
        this.year = this.$route.query.year; 
      if(this.$route.query.deal_type != undefined && this.$route.query.deal_type != null && this.$route.query.deal_type != '')
        this.deal_type = this.$route.query.deal_type;
      this.loadDeptCascader();
      this.searchDataList();
    } 
  };
</script>

<style> 
  input[type=file]{
      display: none !important;
  } 
</style>