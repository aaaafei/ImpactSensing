<template>
    <div style="margin-top:50px;">
    	<el-row>
    		<el-col :span="2">&nbsp;</el-col>
    		  <el-col :span="20">
    		
      <el-form ref="form" :model="form" label-width="100px">
  <el-form-item label="相交叉作业">
    <el-input v-model="form.name"></el-input>
  </el-form-item> 
  <el-form-item label="作业时间">
    <el-col :span="11">
      <el-date-picker type="date" placeholder="选择日期" v-model="form.date1" style="width: 100%;"></el-date-picker>
    </el-col>
    <el-col class="line" :span="2">-</el-col>
    <el-col :span="11">
      <el-time-picker placeholder="选择时间" v-model="form.date2" style="width: 100%;"></el-time-picker>
    </el-col>
  </el-form-item>
 
<el-tag
  :key="tag"
  v-for="tag in dynamicTags"
  closable
  :disable-transitions="false"
  @close="handleClose(tag)">
  {{tag}}
</el-tag>
<el-input
  class="input-new-tag"
  v-if="inputVisible"
  v-model="inputValue"
  ref="saveTagInput"
  size="small"
  @keyup.enter.native="handleInputConfirm"
  @blur="handleInputConfirm"
>
</el-input>
<el-button v-else class="button-new-tag" size="small" @click="showInput">+ </el-button>
 
  <el-form-item label="项目概况">
    <el-input type="textarea" v-model="form.desc"></el-input>
  </el-form-item>

<el-upload
  class="upload-demo"
  action="https://jsonplaceholder.typicode.com/posts/"
  :on-preview="handlePreview"
  :on-remove="handleRemove"
  :before-remove="beforeRemove"
  multiple
  :limit="3"
  :on-exceed="handleExceed"
  :file-list="fileList">
  <el-button size="small" type="primary">点击上传</el-button>  
</el-upload>


  <el-form-item>
    <el-button type="primary" @click="onSubmit">保存</el-button>
    <el-button>取消</el-button>
  </el-form-item>
</el-form>

</el-col>
	<el-col :span="2"></el-col>
</el-row>

        <div style="margin:10px;">
            <el-button-group>
                <el-button type="primary" icon="el-icon-plus" @click="add">新增录入</el-button> 
            </el-button-group>
        </div>
        <el-table :data="tableData" style="width:100%;" height="450" @selection-change="handleSelectionChange"  :cell-style="{padding:'3px 0'}">
            <el-table-column type="selection" width="29"></el-table-column>
            <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
            <el-table-column label="显示内容" prop="year" width="200" align="center"></el-table-column>
            <el-table-column label="时间" prop="month" width="120" align="center"></el-table-column> 
            <el-table-column label="情报板编号" prop="attachment_path" width="120" align="center"></el-table-column> 
           <!-- <el-table-column label="审批文件" prop="ff" width="120" align="center">
            	 <template slot-scope="scope">
                    <el-button type="text" size="small" @click="downloadFile(scope.row.attachment_path)">
                        {{ scope.row.attachment_path!=null ? scope.row.attachment_path.substr(scope.row.attachment_path.lastIndexOf('\\')+1) : '' }}
                    </el-button>
                </template>
            </el-table-column> -->
            <el-table-column label="操作" width="300" align="center">
                <template slot-scope="scope"> 
                    <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">抽查情况</el-button> 
                </template>
            </el-table-column>
        </el-table>

 
    </div>
</template>

<script>
/* eslint-disable */ 
    import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
    export default{ 
        data(){
            return {
                tableData: [{year:'前方道路施工请减速慢行',month:'2019-12-02',attachment_path:'Q0023'},{year:'前方道路右侧施工，请注意右侧变窄',month:'2020-04-22',attachment_path:'Q0005'},{year:'道路施工，限速20km/h',month:'2020-06-13',attachment_path:'Q0089'}],
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
                pageProps: ['uuid', 'yearMonth', 'attachment_path'],
                form: {uuid: null, yearMonth: undefined, attachment_path: null },
                fileList: [],
                form: {
          name: '',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: ''
        },
        fileList: [{name: '工程项目有关文件', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}, {name: '施工组织设计', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'},
        {name: '安全交底', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'},
        {name: '现场管控', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}

        ]

        ,
           dynamicTags: ['相关方一', '相关方二', '相关方三'],
        inputVisible: false,
        inputValue: ''

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
                var url = "/monthReport/getPageData/" + this.page.pageNum + "/" + this.page.pageSize;
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
                        url: '/monthReport/delete/' + this.selectUUIDs.join(',') +"/",
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
                    url: '/monthReport/delete/' + rowData.uuid
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
                this.form['yearMonth'] = rowData.year + "-" + rowData.month;
                //初始化附件列表：
                if(rowData.attachment_path!=undefined && rowData.attachment_path!=null && rowData.attachment_path!=''){ 
                  let path = rowData.attachment_path;
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
            } ,

              handleClose(tag) {
        this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      },

      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },

      handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue) {
          this.dynamicTags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      }
        }
    };
</script>
<style>
  .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
</style>