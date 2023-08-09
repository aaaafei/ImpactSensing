<template>

  <div id="USER">    
    <el-row>
      
      <el-col :span="24" class="right-content">

        <el-col :span="24" >
            <div>
                报告类型：<el-select v-model="formInline.bglx" placeholder="请选择" @change="queryData" :clearable="true">
                    <el-option
                        v-for="item in fxpgList"
                        :key="item.code"
                        :label="item.name"
                        :value="item.code">
                    </el-option>
                </el-select>
                <el-button type="danger"  size="small" style="float: right;margin-right:10px" @click="fxbgDelete()" v-if="adminUser">删除</el-button>
                <el-button type="primary"  size="small" style="float: right;margin-right:10px" @click="fxbgAdd()" v-if="adminUser">新增</el-button>
            </div>
        </el-col> 
        <el-col :span="24" style="height:64vh;margin-top:10px;">
            <el-table ref="multipleTable" :data="tableData3" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="40"></el-table-column>
              <el-table-column type="index" label="序号" :index="indexMethod" width="50"></el-table-column>
              <el-table-column prop="name" label="报告名称" >
                <template slot-scope="scope" v-if="scope.row.name">
                    <el-button type="text" size="small" title="点击下载" @click="previewFile(scope.row.full_path)">
                        {{scope.row.name}}
                    </el-button>
                  </template>
              </el-table-column>
              <el-table-column prop="bglx" label="类型" width="150">
                  <template slot-scope="scope" v-if="scope.row.bglx">
                      {{scope.row.bglx=='1'?'健康监测分析报告':(scope.row.bglx=='2'?'隧道异常情况分析报告':(scope.row.bglx=='3'?'重点关注区域报告':''))}}
                  </template>
              </el-table-column>
              <el-table-column prop="report_date" label="上报时间"  width="150"></el-table-column> 
              <el-table-column prop="creator" label="上报人员"  width="150"></el-table-column> 
              <el-table-column label="操作" fixed="right" width="250">
                <template slot-scope="scope" class="theoprations">
                    <el-button size="mini" type="success" @click="fxbgEdit(scope.row)" v-if="adminUser">修改</el-button>
                    <el-button size="mini" type="warning" @click="downloadFile(scope.row.full_path)">下载</el-button>
                </template>
              </el-table-column> 
            </el-table>
            <div class="pageDiv">
              <el-pagination
                class="page"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="page.pageNum"
                :page-sizes="[5, 10, 15, 20]"
                :page-size="page.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="page.total">
              </el-pagination>
            </div>
        </el-col> 
      </el-col>
    </el-row>
    <el-dialog
      title="报告文件管理"
      :visible.sync="fxbgDialog.visible"
      width="30%"
      :before-close="fxbgDialogClose">
        <el-form :inline="true" :model="fxbgForm" :rules="rules4fxbg" ref="fxbgForm" label-width="100px" size="mini">
            <el-form-item label="报告类型" prop="bglx">
                <el-select v-model="fxbgForm.bglx" placeholder="请选择" style="width:200px">
                    <el-option
                        v-for="item in fxpgList"
                        :key="item.code"
                        :label="item.name"
                        :value="item.code">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="上报日期" prop="report_date">
                <el-date-picker
                    v-model="fxbgForm.report_date"
                    type="date"
                    value-format="yyyy-MM-dd"
                    placeholder="选择日期"  style="width:200px">
                </el-date-picker>
            </el-form-item>

            <el-form-item label="文件上传" prop="full_path" v-if="!fxbgForm.uuid">
                <el-upload ref="attachment_path" :http-request="req" :before-remove="beforeRemove" :on-preview="handlePreview" :on-exceed="handleExceed" :limit="1" :file-list="fileList" :show-file-list="true" action="" v-show="!fxbgForm.name">                
                    <el-button size="mini" type="primary" plain>点击上传</el-button>
                </el-upload>
                <label v-show="fxbgForm.name">{{fxbgForm.name}}</label>
            </el-form-item>
        </el-form>
        <div style="text-align:center">
            <el-button type="primary" @click="updateSave">保存</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
  /* eslint-disable */
  import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
  export default {
    name: 'user',
    data: function () {
      return {
        formInline: { // 条件查询的数据
            bglx: '',//报告类型
            name: '',//文件名称
            full_path: '',//保存路径
            create_date: '',//创建/上传时间
            creator: '',//操作/上传人员
            report_date:''//报告时间
        },
        fxbgForm: {
            bglx: '',//报告类型
            name: '',//文件名称
            full_path: '',//保存路径
            create_date: '',//创建/上传时间
            creator: '',//操作/上传人员
            report_date:''//报告时间
        },
        rules4fxbg:{
            bglx:[{required:true,message:'请选择类型',trigger:'change'}],
            report_date:[{required:true,message:'请选择上报日期',trigger:'change'}]
        },
        fxbgDialog:{
            visible:false
        },
        fxpgList:[{code:'1',name:'健康监测分析报告'},{code:'2',name:'隧道异常情况分析报告'},{code:'3',name:'重点关注区域报告'}],//报告类型
        tableData3: [],
        selectUUIDs:[],
        page: { // 分页的数据信息
            pageNum: 1,
            pageSize: 10,
            total: 0
        },
        fileList: [],
        file:null,
        adminUser:this.$checkRoleAdmin(),
      };
    },
    mounted:function(){
        this.queryData();
    },
    methods: {

        indexMethod (index) {
          return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);
        },
        handleSizeChange (val) { // 设置每页总共几条 
          // alert(val)
            this.page.pageSize = val;
            this.page.pageNum = 1;
            this.queryUser('queryForm', true);
        },
        handleCurrentChange (val) { // 跳到设置的当前页
            this.page.pageNum = val;
            this.queryUser('queryForm', false);
        },
        queryData(){
            var url = '/fxbg/getPageData/' + this.page.pageNum + '/' + this.page.pageSize;
                this.$request({
                url: url,
                method: 'post',
                data:{
                    bglx:this.formInline.bglx
                }
            }).then(res => {
                console.log("查询到的数据",res);
                this.tableData3 = res.data.result_data.list;
                this.page.total = res.data.result_data.total;
            });
        },
        fxbgAdd(){
            this.fxbgForm={};
            this.resetForm();
            this.file = null;
            this.fileList=[];
            this.fxbgDialog.visible=true;
        },
        fxbgEdit(row){
            this.fxbgForm=row;
            this.fxbgDialog.visible=true;
        },
        handleSelectionChange(rows){
            this.selectUUIDs=[];//clear
            if(rows.length>0){
              rows.forEach(row => {
              if (row) {
                  this.selectUUIDs.push(row.uuid);
              }
              });
            }
        },
        fxbgDelete(row){
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
                        url: '/fxbg/delete/' + this.selectUUIDs.join(',') +"/",
                        method:'get'
                    }).then(res => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.queryData();//reload list
                        this.selectUUIDs=[];//clear
                    });
                }).catch(()=>{
                    this.$message({type: 'info',message: '已取消删除'});
                });
            }
        },
        resetForm(){
            let formName = 'fxbgForm';
            if(this.$refs[formName]){
                this.$refs[formName].resetFields();
            }

        },
        fxbgDialogClose(){
            this.fxbgDialog.visible=false;
            this.queryData();
        },
        previewFilepreviewFile(filePath){//页面查看附件内容
            window.open('./preview?filepath='+filePath);
        },
        downloadFile(filePath){//下载附件
            fileDownloadUtil(filePath);
        },
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        handlePreview(file) {
            console.log(file);
        },
        handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },
        beforeRemove(file, fileList) {
            return this.$confirm(`确定移除 ${ file.name }？`);
        },
        handleSuccess(response,file,fileList){
            console.log(file,fileList);
        },
        req(f){
          this.file = f.file;  
          this.fileList.push({name:f.file.name,url:''});
        },
        updateSave(){//表单提交
            this.$refs['fxbgForm'].validate((valid) => {
                let fd = new FormData();
                fd.append('bglx',this.fxbgForm.bglx);
                fd.append('full_path',this.file); 
                fd.append('report_date',this.fxbgForm.report_date); 
                if (valid) {
                    var url = "/fxbg/add/";
                    if(this.fxbgForm.uuid){
                        url = "/fxbg/update/";
                        fd.append('uuid',this.fxbgForm.uuid);
                        console.log("要修改的数据",fd.get("uuid"));
                    }
                    this.$request({
                        url: url,
                        method: 'post',
                        data: fd
                    }).then(res => { 
                        if(res.data.result_code == 0){
                            this.$message({message: '保存成功！',type: 'success'});
                            this.resetForm();
                            this.fxbgDialog.visible=false;
                            this.queryData();
                        }else{ 
                            this.$message({message: res.data.result_desc,type: 'error'});
                        }
                    });
                }else{
                    return false;
                }
            });
        }
    }
  };
</script>

<style >
  #USER .userPostBox {
    background-color:rgba(0, 0, 0, 0.5);
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    z-index: 2000;
    padding: 8vh 15vw;
  }
  #USER .posthide {
    display: none;
  }
  #USER .postshow {
    display: block;
  }
  #USER label {
    font-size: 12px;
  }
  #USER .el-tree-node__label {
    font-size: 12px;
  }
  #USER th {
    white-space: nowrap;
    font-weight: bold !important;
    color: #b2b2b2;
  }
  #USER input {
  height: 30px;
}
#USER .el-table td{
  padding: 7px 0;
  color: #666666;
}
#USER .el-table th,td {
  text-align: center;
  font-size: 12px;
}
#USER .el-row {
  margin: 15px;
  margin-top: 10px;
}
#USER .left-content {
  padding-right: 15px;
  /*min-height: 400px;*/
}
#USER .el-button {
  /*width: 84px;
  height: 32px;*/
  padding: 9px 29px;
}
#USER .right-content > .el-col:not(.middle-operate) {
  background-color: #fff;
  border-radius: 5px;
  /* box-shadow: 2px 2px 5px #999; */
}
#USER .right-content > .el-col:first-child { 
   padding: 12px 0px 5px 15px;
 }
 
#USER .el-form--inline .el-form-item {
  margin-right: 30px;
}
#USER .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
  margin-bottom: 15px;
}
#USER .el-button--text{
  padding: 9px 0px;
}
#USER .thefilterInput {
  width: 90%;
  margin-left: 5%;
  border-bottom: 1px solid #e5e5e5;
  padding-bottom: 10px
}
  #uesr_tree .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #4baff2;
    color: #fff;
  }
#USER .the_left_tree {
  padding-top: 10px;
  background-color: #fff;
  border-radius: 5px;
  /* box-shadow: 2px 2px 5px #999; */
  background-color: #fff;
 /* overflow-y: auto;
  overflow-x: hidden;*/
}
#USER .middle-operate {
  text-align: right;
  padding: 8px 0;
}
#USER .clickName{
  color: #606266;
  font-size: 12px;
  width: auto !important;
  height: auto !important;
   text-decoration: underline;
 }
  #USER .clickName:hover{
   color: #409EFF;
 }
/* #USER .el-button--primary {
  background-color: #4baff2;
  border-color: #4baff2;
}
#USER .el-button--danger {
  background-color: #f36f6f;
  border-color: #f36f6f;
}
#USER .el-button--info {
  background-color: #b1bac0;
  border-color: #b1bac0;
} */
/*分页样式*/
#USER .pageDiv {
  padding: 10px;
  height: 35px;
  background-color: rgb(255, 255, 255);
}

#USER .pageDiv .page {
  float: right;
}
#USER .el-pager {
  margin-left: 12px;
}
#USER .el-pager li.active {
  color: rgb(255, 255, 255);
  background-color: #a7bbf5;
  border: 1px solid;
  border-radius: 6px;
}

#USER .el-pager li.number {
  border: 1px solid #d4d4d4;
  border-radius: 6px;
}

#USER .el-pager li {
   padding: 0 10px;
  min-width: 0px;
  font-family: 宋体;
   margin: 0 6px;
}

#USER .el-pagination button {
  border: 1px solid #d4d4d4;
  border-radius: 6px;
  width: 27px;
  min-width: 0px;
}

#USER .el-pagination .btn-next {
  margin-left: 10px;
  padding-left: 6px;
}

/**************/
#USER .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background-color: #4baff2;
  color: #fff;
}
</style>
