<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="border-card">
      <el-tab-pane label="左线" name="L">

        <el-row >
          <!-- 左边 -->
          <el-col :span="6">  
            <el-tree ref="treeL" :props="props" :load="loadLeftNode" node-key="code" :highlight-current="true" lazy :expand-on-click-node="false" @node-click="leftHandleNodeClick"></el-tree>
          </el-col>

          <!-- 右边 -->
          <el-col :span="18"> 
              <el-row>
                <el-col :span="24" >
                  <div>   
                      <span style="font-weight:bold;">{{lTableTitle}}</span> 
                      <el-button type="primary" icon="el-icon-plus" @click="addLeftNode()" size="small" style="float:right;margin-right:10px">新增节点</el-button>
                  </div>
                  <div style="height:20px;"></div>
                </el-col>
              </el-row>

              <el-table :data="lModelList" :height="500" :header-cell-style="{background:'#409eff',color:'#fff'}">  
                <el-table-column label="序号" type="index"  width="50" align="center"></el-table-column>
                <el-table-column prop="type" label="节点类型" width="120" align="center"></el-table-column>
                <el-table-column prop="no" label="编号" width="120" align="center"></el-table-column>
                <el-table-column prop="description" label="节点" width="120" align="center"></el-table-column>
                <el-table-column prop="weightiness" label="权重" width="120" align="center"></el-table-column>    
                <el-table-column prop="health_value" label="安全度值" width="120" align="center">
                  <template slot-scope="scope">
                     <div :style="'width:60px;height:23px;background-color:'+getStatusColor(scope.row.health_value)+';display:inline-block;margin-right:5px;color:white;'">
                       {{scope.row.health_value}}
                     </div>
                  </template>  
                </el-table-column>    
                <el-table-column label="操作"  align="center">
                  <template slot-scope="scope">
                      <el-button size="mini" plain @click="viewTreeNodeHealth(scope.row.id)">健康度</el-button>
                      <el-button size="mini" plain @click="handleLeftEdit(scope.$index, scope.row)">编辑</el-button>
                      <el-button size="mini" plain @click="handleDelete(scope.$index, scope.row)">删除</el-button> 
                  </template>
                </el-table-column>
              </el-table> 
          </el-col>
        </el-row>


        <el-dialog :title="dialogLeftTitle" :visible.sync="dialogLeftVisible">
          <el-form ref="dataLeftForm" :rules="leftRules" :model="leftModelForm" label-width="120px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="上层父节点">
                  {{lSelectNodeTxt==null ? "无":lSelectNodeTxt}} 
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="节点类型" prop="type"> 
                  <el-select size="small" v-model="leftModelForm.type" placeholder="节点类型" class="filter-item" style="width: 130px">
                    <el-option v-for="item in nodeType" :key="item.key" :label="item.key+'-'+item.display_name" :value="item.key" />
                  </el-select>
                  <br/>
                  (备注：M为最底层叶子节点，需设置关联的传感器)
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="编号" prop="no">
                  <el-input v-model="leftModelForm.no" ></el-input>
                </el-form-item>
              </el-col>  
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="节点" prop="description">  
                    <el-input v-model="leftModelForm.description" ></el-input>
                </el-form-item>
              </el-col>  
              <el-col :span="12">
                <el-form-item label="权重" prop="weightiness">         
                  <el-input-number controls-position="right" v-model="leftModelForm.weightiness" :precision="4" :step="0.0001" :min="0" :max="1"></el-input-number> 
                </el-form-item>
              </el-col>  
            </el-row>   

            <el-row v-if="leftModelForm.type=='M'">
              <el-col :span="12">
                <el-form-item label="设备类型">  
                  <el-radio-group v-model="leftModelForm.device_type">
                    <el-radio :label="1">一般设备</el-radio>
                    <el-radio :label="2">静力水准仪</el-radio> 
                  </el-radio-group>
                </el-form-item>        
              </el-col>
              <el-col :span="12">
                <el-form-item label="传感器1">  
                    <el-select
                      v-model="leftModelForm.device1_id"  
                      filterable 
                      placeholder="传感器编号"
                      clearable  >
                      <el-option
                        v-for="item in leftDevice1Options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                </el-form-item> 
              </el-col>
            </el-row>
            <el-row v-if="leftModelForm.type=='M'">
              <el-col :span="12">
                <el-form-item label="传感器2">  
                    <el-select
                      v-model="leftModelForm.device2_id"   
                          filterable 
                      placeholder="传感器编号"
                      clearable>
                      <el-option
                        v-for="item in leftDevice2Options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>                     
                </el-form-item> 
              </el-col>
            </el-row>


          </el-form>

          <div slot="footer" class="dialog-footer">
            <el-button @click="closeLeftNode()">
              关闭
            </el-button>
            <el-button type="primary" @click="saveLeftNode()">
              保存
            </el-button>
          </div>
        </el-dialog>
  


 

      </el-tab-pane>












      <el-tab-pane label="右线" name="R">
        <el-row>
          <!-- 左边 -->
          <el-col :span="6">
            <el-tree ref="treeR" :props="props" :load="loadRightNode" node-key="code" :highlight-current="true" lazy :expand-on-click-node="false" @node-click="rightHandleNodeClick"></el-tree>
          </el-col>

          <!-- 右边 -->
          <el-col :span="18">
        
       
              <el-row>
                    <el-col :span="24" >
                      <div>   
                          <span style="font-weight:bold;">{{rTableTitle}}</span> 
                          <el-button type="primary" icon="el-icon-plus" @click="addRightNode()" size="small" style="float:right;margin-right:10px">新增节点</el-button>
                      </div>
                      <div style="height:20px;"></div>
                    </el-col>
                  </el-row>

                  <el-table :data="rModelList" :height="500" :header-cell-style="{background:'#409eff',color:'#fff'}">  
                    <el-table-column label="序号" type="index"  width="50" align="center"></el-table-column>
                    <el-table-column prop="type" label="节点类型" width="120" align="center"></el-table-column>
                    <el-table-column prop="no" label="编号" width="120" align="center"></el-table-column>
                    <el-table-column prop="description" label="节点" width="120"  align="center"></el-table-column>
                    <el-table-column prop="weightiness" label="权重" width="120" align="center"></el-table-column>     
                    <el-table-column prop="health_value" label="安全度值" width="120" align="center">
                      <template slot-scope="scope">
                        <div :style="'width:60px;height:23px;background-color:'+getStatusColor(scope.row.health_value)+';display:inline-block;margin-right:5px;color:white;'">
                          {{scope.row.health_value}}
                        </div>
                      </template>  
                    </el-table-column>  
                    <el-table-column label="操作" align="center">
                      <template slot-scope="scope">
                          <el-button size="mini" plain @click="viewTreeNodeHealth(scope.row.id)">健康度</el-button>
                          <el-button size="mini" plain @click="handleRightEdit(scope.$index, scope.row)">编辑</el-button>
                          <el-button size="mini" plain @click="handleDelete(scope.$index, scope.row)">删除</el-button> 
                      </template>
                    </el-table-column>
                  </el-table> 
           
          </el-col>
        </el-row>
      </el-tab-pane> 


        <el-dialog :title="dialogRightTitle" :visible.sync="dialogRightVisible">
          <el-form ref="dataRightForm" :rules="rightRules" :model="rightModelForm" label-width="120px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="上层父节点">
                  {{rSelectNodeTxt==null ? "无":rSelectNodeTxt}} 
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="节点类型" prop="type"> 
                  <el-select size="small" v-model="rightModelForm.type" placeholder="节点类型" class="filter-item" style="width: 130px">
                    <el-option v-for="item in nodeType" :key="item.key" :label="item.key+'-'+item.display_name" :value="item.key" />
                  </el-select>
                  <br/>
                  (备注：M为最底层叶子节点，需设置关联的传感器)
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="编号" prop="no">
                  <el-input v-model="rightModelForm.no" ></el-input>
                </el-form-item>
              </el-col>  
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="节点" prop="description">  
                    <el-input v-model="rightModelForm.description" ></el-input>
                </el-form-item>
              </el-col>  
              <el-col :span="12">
                <el-form-item label="权重" prop="weightiness">         
                  <el-input-number controls-position="right" v-model="rightModelForm.weightiness" :precision="4" :step="0.0001" :min="0" :max="1"></el-input-number> 
                </el-form-item>
              </el-col>  
            </el-row>  

            <el-row v-if="rightModelForm.type=='M'">
              <el-col :span="12">
                <el-form-item label="设备类型">  
                  <el-radio-group v-model="rightModelForm.device_type">
                    <el-radio :label="1">一般设备</el-radio>
                    <el-radio :label="2">静力水准仪</el-radio> 
                  </el-radio-group>
                </el-form-item>        
              </el-col>
              <el-col :span="12">
                <el-form-item label="传感器1">  
                    <el-select
                      v-model="rightModelForm.device1_id" 
                      filterable 
                      placeholder="传感器编号"
                      clearable>
                      <el-option
                        v-for="item in rightDevice1Options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                </el-form-item> 
              </el-col>
            </el-row>
            <el-row v-if="rightModelForm.type=='M'">
              <el-col :span="12">
                <el-form-item label="传感器2">  
                    <el-select
                      v-model="rightModelForm.device2_id" 
                      filterable 
                      placeholder="传感器编号" 
                      clearable>
                      <el-option
                        v-for="item in rightDevice2Options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>                     
                </el-form-item> 
              </el-col>
            </el-row> 

          </el-form>

          <div slot="footer" class="dialog-footer">
            <el-button @click="closeRightNode()">
              关闭
            </el-button>
            <el-button type="primary" @click="saveRightNode()">
              保存
            </el-button>
          </div>
        </el-dialog>

    </el-tabs>


       <el-dialog :visible.sync="dialogTreeViewVisible" width="96%">
          <tree-view :treeData="treeData"></tree-view>
        </el-dialog>



 
  </div>
</template>

<script>
 
import TreeView from './treeView.vue';
/* eslint-disable */

export default {
  components: { TreeView },
  name: 'monitor', 
  data () {
    return { 
     props: {
        label: 'label',
        children: 'children',
        isLeaf: 'leaf'
      },
      activeName:'L',  
      leftDevice1Loading: false,
      leftDevice1Options: [],
      leftDevice2Loading: false,
      leftDevice2Options: [],     
      rightDevice1Loading: false,
      rightDevice1Options: [],
      rightDevice2Loading: false,
      rightDevice2Options: [],   
      leftTreeData: [], 
      rightTreeData: [],
      lModelList: [],
      rModelList: [],
      lTableTitle: null,
      rTableTitle: null,
      lSelectNodeTxt: null,
      lSelectNode: null,
      rSelectNodeTxt: null,
      rSelectNode: null,      
      dialogLeftTitle: null,
      dialogLeftVisible: false,
      dialogTreeViewVisible:false,
      leftModelForm: {id:null,line:null,type:null,no:null,description:null,weightiness:null,parent_category_id:null,is_leaf:null,device1_id:null,device2_id:null,device_type:null},
      dialogRightTitle: null,
      dialogRightVisible: null,
      rightModelForm:{id:null,line:null,type:null,no:null,description:null,weightiness:null,parent_category_id:null,is_leaf:null,device1_id:null,device2_id:null,device_type:null},
      leftRules: {
       type: [{ required: true, message: '请选择节点类型', trigger: 'change' }],
       no: [{ required: true, message: '请输入节点编号', trigger: 'blur' },
            { max: 100, message: '长度小于100个字符', trigger: 'blur' }],
       description: [{ required: true, message: '请输入节点名', trigger: 'blur' },
                     { max: 100, message: '长度小于100个字符', trigger: 'blur' }],
       weightiness: [{ required: true, message: '请输入节点的权重值', trigger: 'blur' }]
      },
      rightRules: {
       type: [{ required: true, message: '请选择节点类型', trigger: 'change' }],
       no: [{ required: true, message: '请输入节点编号', trigger: 'blur' },
            { max: 100, message: '长度小于100个字符', trigger: 'blur' }],
       description: [{ required: true, message: '请输入节点名', trigger: 'blur' },
                     { max: 100, message: '长度小于100个字符', trigger: 'blur' }],
       weightiness: [{ required: true, message: '请输入节点的权重值', trigger: 'blur' }]
      },
      nodeType: [
        { key: 'G', display_name: '区段' },
        { key: 'C', display_name: '断面' },
        { key: 'P', display_name: '管片' },
        { key: 'N', display_name: '断面' },
        { key: 'S', display_name: '检测项' },
        { key: 'M', display_name: '设备' }
      ],
      lRootNode: null,
      lRootNodeResolve: null,
      rRootNode: null,
      rRootNodeResolve: null,
      leftDeviceMap: {},
      rightDeviceMap: {},
      treeData:{}
    };
  },
  computed: { 
  },
  methods: {
      refreshLeftNode(id){
         let node = this.$refs.treeL.getNode(id); // 通过节点id找到对应树节点对象
         node.loaded = false;
         node.expand(); // 主动调用展开节点方法，重新查询该节点下的所有子节点
      },
      refreshRightNode(id){
         let node = this.$refs.treeR.getNode(id); // 通过节点id找到对应树节点对象
         node.loaded = false;
         node.expand(); // 主动调用展开节点方法，重新查询该节点下的所有子节点
      },
      addLeftNode(){
        this.leftModelForm = {id:null,line:null,type:null,no:null,description:null,weightiness:null,parent_category_id:null,is_leaf:null,device1_id:null,device2_id:null,device_type:null};
        this.leftModelForm["line"] = "L";
        //this.leftModelForm["device_type"] = 1;
        this.dialogLeftTitle = "新增节点";
        this.dialogLeftVisible = true;
      },
      addRightNode(){
        this.rightModelForm ={id:null,line:null,type:null,no:null,description:null,weightiness:null,parent_category_id:null,is_leaf:null,device1_id:null,device2_id:null,device_type:null};
        this.rightModelForm["line"] = "R";
        //this.rightModelForm["device_type"] = 1;
        this.dialogRightTitle = "新增节点";
        this.dialogRightVisible = true;
      },
      saveLeftNode(){


        this.$refs['dataLeftForm'].validate((valid) => {
          if (valid) {
            //判断节点类型是否为M
            if(this.leftModelForm['type']=="M"){
              if(this.leftModelForm['device_type']==undefined || this.leftModelForm['device_type']==null){
                    this.$message({
                      message: '请选择设备类型',
                      type: 'error'
                    });
                    return;
              }
              if(this.leftModelForm['device1_id']==undefined || this.leftModelForm['device1_id']==null){
                    this.$message({
                      message: '请选择关联设备1',
                      type: 'error'
                    });
                    return; 
              }
              if(this.leftModelForm['device_type']=='2'){ 
                if(this.leftModelForm['device2_id']==undefined || this.leftModelForm['device2_id']==null){
                      this.$message({
                        message: '请选择关联设备2',
                        type: 'error'
                      });
                      return; 
                }
              }
            }


            if(this.lSelectNode!=null)
              this.leftModelForm["parent_category_id"] = this.lSelectNode.code; 

            this.$request({
              url:'/calculateModel/saveNode',
              method:'post',
              data: this.leftModelForm
            }).then(res => { 
                this.$message({
                  message: '节点保存成功',
                  type: 'success'
                });
                this.dialogLeftVisible = false;

                this.refreshLeftNodeAndTable();
            }); 
   
          

        } else {
            return false;
          }
        });



      },
      closeLeftNode(){
        this.dialogLeftVisible = false;
          this.leftModelForm = {id:null,line:null,type:null,no:null,description:null,weightiness:null,parent_category_id:null,is_leaf:null,device1_id:null,device2_id:null,device_type:null};
      },
      saveRightNode(){

        this.$refs['dataRightForm'].validate((valid) => {
          if (valid) {
            //判断节点类型是否为M
            if(this.rightModelForm['type']=="M"){
              if(this.rightModelForm['device_type']==undefined || this.rightModelForm['device_type']==null){
                    this.$message({
                      message: '请选择设备类型',
                      type: 'error'
                    });
                    return;
              }
              if(this.rightModelForm['device1_id']==undefined || this.rightModelForm['device1_id']==null){
                    this.$message({
                      message: '请选择关联设备1',
                      type: 'error'
                    });
                    return; 
              }
              if(this.rightModelForm['device_type']=='2'){ 
                if(this.rightModelForm['device2_id']==undefined || this.rightModelForm['device2_id']==null){
                      this.$message({
                        message: '请选择关联设备2',
                        type: 'error'
                      });
                      return; 
                }
              }
            }



            if(this.rSelectNode!=null)
              this.rightModelForm["parent_category_id"] = this.rSelectNode.code; 
            
            this.$request({
              url:'/calculateModel/saveNode',
              method:'post',
              data: this.rightModelForm
            }).then(res => { 
                this.$message({
                  message: '节点保存成功',
                  type: 'success'
                });
                this.dialogRightVisible = false;

                this.refreshRightNodeAndTable();
            }); 
 
          

        } else {
            return false;
          }
        });

      },
      closeRightNode(){
         this.dialogRightVisible = false;
           this.rightModelForm = {id:null,line:null,type:null,no:null,description:null,weightiness:null,parent_category_id:null,is_leaf:null,device1_id:null,device2_id:null,device_type:null};
      },
      rightHandleNodeClick(obj,obj2,obj3){
        let parentId = obj.code;
        this.$request({
          url:'/calculateModel/getSonNodeListByParentId',
          method:'post',
          data: {'line': this.activeName, 'parent_category_id': parentId}
        }).then(res => { 
          this.rModelList = res.data.result_data.tableList;
           
        
            this.rTableTitle = "隧道右线节点【"+ obj.label +"】下层节点：";  
            this.rSelectNodeTxt = "隧道右线节点【"+ obj.label +"】"; 
           
          this.rSelectNode=obj;
        }); 

      },
      leftHandleNodeClick(obj,obj2,obj3) { 
        let parentId = obj.code;
        this.$request({
          url:'/calculateModel/getSonNodeListByParentId',
          method:'post',
          data: {'line': this.activeName, 'parent_category_id': parentId}
        }).then(res => { 
          this.lModelList = res.data.result_data.tableList;
          
            this.lTableTitle = "隧道左线节点【"+ obj.label +"】下层节点："; 
            this.lSelectNodeTxt = "隧道左线节点【"+ obj.label +"】";  
          
          this.lSelectNode=obj;
        }); 
      },
      loadLeftNode(node, resolve) {   
        setTimeout(() => {
          if (node.level === 0) {
            this.lRootNode = node;
            this.lRootNodeResolve = resolve;
            resolve(this.leftTreeData);  
          } 
        }, 500); 
        if (node.level > 0) {
          let parentId = node.data.code;
          this.$request({
            url:'/calculateModel/getSonNodeListByParentId',
            method:'post',
            data: {'line': 'L', 'parent_category_id': parentId}
          }).then(res => {
            let data = res.data.result_data.treePojos; 
            resolve(data);
          });   
        } 
      }, 
      loadRightNode(node, resolve) {  
        setTimeout(() => {
          if (node.level === 0) {
            this.rRootNode = node;
            this.rRootNodeResolve = resolve;
            resolve(this.rightTreeData);  
          } 
        }, 500); 
        if (node.level > 0) {
          let parentId = node.data.code;
          this.$request({
            url:'/calculateModel/getSonNodeListByParentId',
            method:'post',
            data: {'line': 'R', 'parent_category_id': parentId}
          }).then(res => {
            let data = res.data.result_data.treePojos; 
            resolve(data);
          });   
        }        
      },
      handleDelete(index,rowData){
        //检测是否包含下层节点
          this.$request({
            url:'/calculateModel/getSonNodeListByParentId',
            method:'post',
            data: {'line': this.activeName, 'parent_category_id': rowData.id}
          }).then(res => {
            let data = res.data.result_data.treePojos; 
            if(data!=undefined && data!=null && data.length>0){
                this.$message({
                  message: '请先删除下层节点！',
                  type: 'warning'
                });
            }
            else{ 


      this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {


     this.$request({
                url:'/calculateModel/delete/'+rowData.id,
                method:'get'
              }).then(res => {
 
                this.$message({
                  message: '节点删除成功',
                  type: 'success'
                }); 

                if(this.activeName=='L')
                  this.refreshLeftNodeAndTable();
                else
                  this.refreshRightNodeAndTable();
              }); 

 
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });


         
            }


          });
      },
      refreshLeftNodeAndTable(){
          //刷新树和数据表
          if(this.lSelectNode!=null){
            this.refreshLeftNode(this.lSelectNode.code); 
            this.leftHandleNodeClick(this.lSelectNode);
          }
          else{
            //this.$router.go(0); 
            this.loadLeftTreeRootNode(true);
          }
      },
      refreshRightNodeAndTable(){
          //刷新树和数据表
          if(this.rSelectNode!=null){
            this.refreshRightNode(this.rSelectNode.code);
            this.rightHandleNodeClick(this.rSelectNode);
          }
          else{
            //this.$router.go(0); 
            this.loadRightTreeRootNode(true);
          }
      },      
      handleLeftEdit(index,rowData){   
          this.dialogLeftTitle = "编辑节点"; 
          this.leftModelForm.id = rowData.id;
          this.leftModelForm.line = rowData.line;
          this.leftModelForm.type = rowData.type;
          this.leftModelForm.no = rowData.no;
          this.leftModelForm.description = rowData.description;
          this.leftModelForm.weightiness = rowData.weightiness;
          this.leftModelForm.parent_category_id = rowData.parent_category_id;
          this.leftModelForm.is_leaf = rowData.is_leaf;
          if(rowData.type=='M'){
            this.$request({
              url:'/calculateModel/getMConfigById/'+rowData.id,
              method:'get'
            }).then(res => {
              let valueConfig = res.data.result_data; 
              this.leftModelForm.device_type = valueConfig.type;
              if(valueConfig.device1_id!=null)
                this.leftModelForm.device1_id = valueConfig.device1_id+'';
              if(valueConfig.device2_id!=null)
                this.leftModelForm.device2_id = valueConfig.device2_id+'';

 
              this.dialogLeftVisible = true; 
            }); 
          }
          else{ 
            this.dialogLeftVisible = true;
          }
      }, 
      handleRightEdit(index,rowData){ 
          this.dialogRightTitle = "编辑节点"; 
          this.rightModelForm.id = rowData.id;
          this.rightModelForm.line = rowData.line;
          this.rightModelForm.type = rowData.type;
          this.rightModelForm.no = rowData.no;
          this.rightModelForm.description = rowData.description;
          this.rightModelForm.weightiness = rowData.weightiness;
          this.rightModelForm.parent_category_id = rowData.parent_category_id;
          this.rightModelForm.is_leaf = rowData.is_leaf;
          if(rowData.type=='M'){
            this.$request({
              url:'/calculateModel/getMConfigById/'+rowData.id,
              method:'get'
            }).then(res => {
              let valueConfig = res.data.result_data; 
              this.rightModelForm.device_type = valueConfig.type;
              if(valueConfig.device1_id!=null)
                this.rightModelForm.device1_id = valueConfig.device1_id+'';
              if(valueConfig.device2_id!=null)
                this.rightModelForm.device2_id = valueConfig.device2_id+'';

 
              this.dialogRightVisible = true; 
            }); 
          }
          else{ 
            this.dialogRightVisible = true;
          }

      }, 
      loadLeftTreeRootNode(refresh){
        this.$request({
          url:'/calculateModel/getRootNodeList/L',
          method:'get'
        }).then(res => {
          this.leftTreeData = res.data.result_data.treePojos;
          if(refresh)  { 
            this.lRootNode.childNodes = []; 
            this.lRootNodeResolve(this.leftTreeData);  
          }
          this.lModelList = res.data.result_data.tableList;
         
            this.lTableTitle = "隧道左线第一层节点："; 
           
           
        });
      },
      loadRightTreeRootNode(refresh){
        this.$request({
          url:'/calculateModel/getRootNodeList/R',
          method:'get'
        }).then(res => {
          this.rightTreeData = res.data.result_data.treePojos;
          if(refresh)  { 
            this.rRootNode.childNodes = []; 
            this.rRootNodeResolve(this.rightTreeData);  
          }
          this.rModelList = res.data.result_data.tableList;
           
            this.rTableTitle = "隧道右线第一层节点：";
        
        });
      },
      leftDevice1RemoteMethod(query) { 
        if (query !== '') {
          this.leftDevice1Loading = true;
          setTimeout(() => {
            this.leftDevice1Loading = false;


            let list = this.leftDeviceMap;


            this.leftDevice1Options = list.filter(item => {
              return item.label.toLowerCase()
                .indexOf(query.toLowerCase()) > -1;
            });
          }, 200);
        } else {
          this.leftDevice1Options = [];
        }
      },
      leftDevice2RemoteMethod(query) { 
        if (query !== '') {
          this.leftDevice2Loading = true;
          setTimeout(() => {
            this.leftDevice2Loading = false;


            let list = this.leftDeviceMap;


            this.leftDevice2Options = list.filter(item => {
              return item.label.toLowerCase()
                .indexOf(query.toLowerCase()) > -1;
            });
          }, 200);
        } else {
          this.leftDevice2Options = [];
        }
      },    
      rightDevice1RemoteMethod(query) { 
        if (query !== '') {
          this.rightDevice1Loading = true;
          setTimeout(() => {
            this.rightDevice1Loading = false;


            let list = this.rightDeviceMap;


            this.rightDevice1Options = list.filter(item => {
              return item.label.toLowerCase()
                .indexOf(query.toLowerCase()) > -1;
            });
          }, 200);
        } else {
          this.rightDevice1Options = [];
        }
      },
      rightDevice2RemoteMethod(query) { 
        if (query !== '') {
          this.rightDevice2Loading = true;
          setTimeout(() => {
            this.rightDevice2Loading = false;


            let list = this.rightDeviceMap;


            this.leftDevice2Options = list.filter(item => {
              return item.label.toLowerCase()
                .indexOf(query.toLowerCase()) > -1;
            });
          }, 200);
        } else {
          this.leftDevice2Options = [];
        }
      },    
      loadLeftDevice(){
        this.$request({
          url:'/tmDevice/getDeviceNoIdMap',
          method:'post',
          data: {line: '左线'}
        }).then(res => {
          this.leftDeviceMap = res.data.result_data; 
          this.leftDevice1Options = this.leftDeviceMap;
          this.leftDevice2Options = this.leftDeviceMap; 
        });          
      },    
      loadRightDevice(){
        this.$request({
          url:'/tmDevice/getDeviceNoIdMap',
          method:'post',
          data: {line: '右线'}
        }).then(res => {
          this.rightDeviceMap = res.data.result_data; 
          this.rightDevice1Options = this.rightDeviceMap;
          this.rightDevice2Options = this.rightDeviceMap;
        });          
      },
      getStatusColor(value){ 
        var colorStatus = {
          A: "#008000",
          B: "#3888fa",
          C: "#FFA500",
          D: '#FF0000',
          E: '#800000'
        }; 
        if(value>4.2) {
          return colorStatus['A'];
        }
        else if(value>3.4){
          return colorStatus['B'];
        }
        else if(value>2.6){
          return colorStatus['C'];
        }
        else if(value>1.8){
          return colorStatus['D'];
        }
        else {
          return colorStatus['E'];
        }
      },
      viewTreeNodeHealth(id){ 
        this.$request({
          url:'/calculateModel/getHeathTreeValues/'+id,
          method:'get'
        }).then(res => {
          this.treeData = res.data.result_data.node;  
          this.dialogTreeViewVisible = true;
        });  

      }
  },
  mounted: function() {
    this.loadLeftTreeRootNode();
    this.loadLeftDevice();


    this.loadRightTreeRootNode();
    this.loadRightDevice();
  }
};
</script>