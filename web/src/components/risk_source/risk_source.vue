<template>
  <div>
    <div style="margin: 10px;">
      <el-row>
        <el-col :span="24">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            <el-button type="primary" icon="el-icon-delete" @click="deleteDatas">删除</el-button>
          </el-button-group>

          <el-button plain icon="el-icon-location" circle @click="mapDialogVisible = true">风险源地图</el-button>
 <el-cascader v-model="org_id" placeholder="请选择部门" :show-all-levels="false" change-on-select :options="deptOptions" clearable></el-cascader> 
          <el-input style="width:200px;" v-model="qName" placeholder="请输入风险源名称"></el-input>

          <el-button type="primary" icon="el-icon-search" @click="searchDataList">搜索</el-button>
        </el-col>
      </el-row>
    </div>
    <el-table
      :data="tableData"
      style="width: 100%"
      height="450"  :cell-style="{padding:'3px 0'}" 
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="29" header-align="center"></el-table-column>
      <el-table-column label="序号" type="index" :index="indexMethod" width="50" align="center"></el-table-column>
      <el-table-column prop="org_name" label="责任单位" width="120" align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="name" label="风险源名称" width="160" header-align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="num" label="风险源数量" width="90" align="center"></el-table-column>
      <el-table-column prop="level" label="等级" width="48" align="center"></el-table-column>
      <el-table-column label="主要风险类型" width="135" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">{{getRiskType(scope.row.type_uuid)}}</template>
      </el-table-column>
      <el-table-column prop="position" label="位置" width="120" header-align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="班组巡查" width="78" align="center">
        <template slot-scope="scope">{{patrolOnduty[scope.row.patrol_onduty]}}</template>
      </el-table-column>
      <el-table-column prop="description" label="备注" header-align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="操作" width="245" header-align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            plain
            @click="handleEdit(scope.$index, scope.row)"
          >编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          <el-button size="mini" type="info" @click="setPatrolContent(scope.$index, scope.row)">巡查设置</el-button>
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
      :total="page.total"
    ></el-pagination>

    <!-- add or edit page -->
    <el-dialog :visible.sync="addEditDialogVisible" width="60%" :title="addEditDialogTitle">
      <addEditComponent @closeAddEdit="closeAddEdit" :detailForm="formValue" :op="op" ></addEditComponent>
    </el-dialog>

    <!-- 地图 -->
    <el-dialog :visible.sync="mapDialogVisible" width="99%" title="风险源地图">
      <mapComponent :tableData="tableData"></mapComponent>
    </el-dialog>

    <!-- 设置巡查内容 -->
    <el-dialog :visible.sync="patrolContentVisible" width="60%" :title="riskSourceName">
      <patrolContent
        :riskSourceUUID="riskSourceUUID"
        :patrolContentData="patrolContentData"
        :patrolSetData="patrolSetData" 
      ></patrolContent>
    </el-dialog>
  </div>
</template>

  <script>
/* eslint-disable */
import mapComponent from "./risk_map.vue";
import addEditComponent from "./risk_source_add_edit.vue";
import patrolContent from "./risk_source_content.vue";
export default {
  components: { mapComponent, addEditComponent , patrolContent },
  data() {
    return {
      addEditDialogVisible: false,
      mapDialogVisible: false,
      addEditDialogTitle: "",
      op: null,
      patrolContentVisible: false,
      api: {
        URL_list: "/riskSource/getRiskSourcePageData/",
        URL_del: "/riskSource/delete/"
      },
      page: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      tableData: [],
      selectUUIDs: [],
      pageProps: [
        "uuid",
        "name",
        "num",
        "level",
        "type_uuid",
        "position",
        "longitude",
        "latitude",
        "description",
        "org_id",
        "org_name",
        "business_org_id",
        "business_org_name",
	    	"apanage_org_id",
	    	"apanage_org_name",
	    	"outsourcing_org_id",
	    	"outsourcing_org_name",
        "qrcode",
        "patrol_onduty",
        "classify"
      ],
      formValue: {
        uuid: null,
        name: null,
        num: 1,
        level: null,
        type_uuid: null,
        position: null,
        longitude: null,
        latitude: null,
        description: null,
        org_id: null,
        org_name: null,
        business_org_id: null,
        business_org_name: null,
		    apanage_org_id: null,
		    apanage_org_name: null,
		    outsourcing_org_id: null,
		    outsourcing_org_name: null,
        qrcode: null,
        patrol_onduty: null,
        classify: null
      }, 

      
      riskSourceName: "",
      riskSourceUUID: "",
      patrolContentData: [],
      qName: null,
      q: {},
      riskType: {
        "01": "物体打击",
        "02": "车辆伤害",
        "03": "机械伤害",
        "04": "起重伤害",
        "05": "触电",
        "06": "淹溺",
        "07": "灼烫",
        "08": "火灾",
        "09": "高处坠落",
        "010": "坍塌",
        "011": "冒顶片帮",
        "012": "透水",
        "013": "放炮",
        "014": "火药爆炸",
        "015": "瓦斯爆炸",
        "016": "锅炉爆炸",
        "017": "容器爆炸",
        "018": "其他爆炸",
        "019": "中毒和窒息",
        "020": "其他伤害"
      },
      patrolOnduty: ["否", "是"],
      patrolSetData: [],
      deptOptions: [], 
      org_id: []
    };
  },
  methods: {  
    indexMethod (index) {
      return (this.page.pageNum - 1) * this.page.pageSize + (index + 1);  
    },
    coseAddEdit() {
      this.searchDataList();
      this.addEditDialogVisible = false;
    },
    handleSelectionChange(rows) {
      this.selectUUIDs = [];
      if (rows.length > 0) {
        rows.forEach(row => {
          if (row) {
            this.selectUUIDs.push(row.uuid);
          }
        });
      }
    },
    deleteDatas() {
      if (this.selectUUIDs.length == 0) {
        this.$alert("请选择要删除的列数据！", "提示");
      } else {
        this.$confirm("确认删除吗?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.$request({
              url: this.api.URL_del + this.selectUUIDs.join(",") + "/"
            }).then(res => {
              this.$message({ type: "success", message: "删除成功!" });
              this.searchDataList();
              this.selectUUIDs = [];
            });
          })
          .catch(() => {
            this.$message({ type: "info", message: "已取消删除" });
          });
      }
    },
    handleAdd() {
        this.addEditDialogTitle = "新增";
        this.addEditDialogVisible = true;
        this.op="add";
      for (var i = 0; i < this.pageProps.length; i++) {
        var prop = this.pageProps[i];
        if (prop == "longitude") {
          this.formValue[prop] = undefined;
          continue;
        }
        if (prop == "latitude") {
          this.formValue[prop] = undefined;
          continue;
        }
        if (prop == "patrol_onduty") {
          this.formValue[prop] = 0;
          continue;
        }
        if(prop == 'classify'){
            this.formValue[prop] = '1';
            continue;
        } 
        if(prop == 'type_uuid'){
            this.formValue[prop] = [];
            continue;
        } 
        this.formValue[prop] = null;
      }
    },
    handleEdit(index, rowData) {
        this.addEditDialogTitle = "修改";
        this.addEditDialogVisible = true;
        this.op="edit";
      for (var i = 0; i < this.pageProps.length; i++) {
        var prop = this.pageProps[i];
        this.formValue[prop] = rowData[prop]; 
        if(prop == 'type_uuid'){
          this.formValue[prop] = rowData[prop].split(',');
        }
      }
    },
    handleDelete(index, rowData) {
      this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$request({
            url: this.api.URL_del + rowData.uuid
          }).then(res => {
            this.$message({
              type: "success",
              message: "删除成功!"
            });
            this.searchDataList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    setPatrolContent(index, rowData) {
      this.patrolContentVisible = true;
      this.riskSourceName = rowData.name + "-风险源巡查设置";
      this.riskSourceUUID = rowData.uuid;
      var url =
        "/riskSourcePatrolContent/getPatrolContentByRiskSourceUUID/" +
        this.riskSourceUUID;
      this.$request({
        url: url,
        method: "post"
      }).then(res => {
        let data = res.data;
        this.patrolContentData = data.result_data;
      });

      //巡查人设置
      let url2 = '/riskSourcePersonLiable/getByRiskSourceId/'; 
      this.$request({
        url: url2 + this.riskSourceUUID ,
        method: "post"
      }).then(res => {
        if (res.data.result_data != undefined) {
          this.patrolSetData = res.data.result_data; 
        }
      });
 
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.searchDataList();
    },
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.searchDataList();
    },
    searchDataList() {
      this.q = {};
      if (this.qName != null && this.qName != "") {
        this.q["name"] = this.qName;
      }
      if(this.org_id!=null && this.org_id != '' && this.org_id.length > 0){
        this.q['org_id'] = this.org_id[this.org_id.length-1]; 
      }
      this.$request({
        url: this.api.URL_list + this.page.pageNum + "/" + this.page.pageSize,
        data: this.q,
        method: "post"
      }).then(res => {
        let data = res.data;
        this.tableData = data.result_data.list;
        this.page.total = data.result_data.total;
      });
    },
    closeAddEdit(){
      this.addEditDialogVisible = false;
      this.searchDataList();
    },
    getRiskType(value){
      var str = '';
      var arr = value.split(',');
      arr.forEach((item,index)=>{ 
        str +=  this.riskType[item] +',';
      });
      return str.substring(0, str.lastIndexOf(','));;
    },
    loadDeptCascader(){
        var url = '/dept/get/all';
        this.$request({
            url: url,
            method: 'get'
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
      }
  },
  mounted: function() {
    this.loadDeptCascader();
    this.searchDataList();
  }
};
</script>