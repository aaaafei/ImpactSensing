<template>
  <div class="app-container">
    <div style="padding-bottom: 10px;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>基础信息</el-breadcrumb-item>
        <el-breadcrumb-item>高架结构设施设备信息统计表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-row type="flex" align="middle" style="height: 80px;background-color: #CECECE;">
      <el-col :span="17">
        <div style="padding-left: 30px;;">
          <el-select v-model="param.lineCode" placeholder="请选择线路">
              <el-option
                v-for="item in lineOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
        </div>
      </el-col>
      <el-col :span="5">
        <el-input placeholder="区间名称" prefix-icon="el-icon-search" v-model="param.sectionName">
        </el-input>
      </el-col>
      <el-col :span="3" style="text-align: center;">
        <el-button type="primary" size="small" @click="searchData">查询</el-button>
        <el-button type="warning" size="small" @click="handleAdd">新增</el-button>
      </el-col>

    </el-row>
    <div style="height:10px;"></div>

    <el-table :data="deviceList" :height="600" border stripe 
      :header-cell-style="{ background: '#A1B6D8', color: '#fff' }">
      <el-table-column fixed type="index" label="序号" width="50" align="center"></el-table-column>
      <el-table-column fixed prop="lineName" label="线路名称" align="center"></el-table-column>
      <el-table-column fixed prop="sectionName" width="150" label="区间名称" align="center"></el-table-column>
      <el-table-column label="基础信息" align="center">
        <el-table-column prop="basicType" width="100" label="类型" align="center"></el-table-column>
        <el-table-column prop="basicMileage" label="起止里程" align="center"></el-table-column>
        <el-table-column prop="basicLength" width="100" label="长度（m）" align="center"></el-table-column>
      </el-table-column>
      <el-table-column label="桥墩" align="center">
        <el-table-column prop="_pierTotalCount" label="合计" align="center">
          <template slot-scope='scope'>
            <el-popover
              placement="top-start"
              :title="'桥墩合计（个）：'+ scope.row.pierTotalCount"
              width="200"
              trigger="hover">
              <div>
                <p><el-tag>普通：{{ scope.row.pierNormalCount }}</el-tag></p>
                <p><el-tag type="success">跨路口：{{ scope.row.pierTransroadCount }}</el-tag></p>
                <p><el-tag type="warning">涉水：{{ scope.row.pierTranswaterCount }}</el-tag></p>
              </div>
              <el-button type="text" slot="reference">{{ scope.row.pierTotalCount }}</el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="支座" align="center">
        <el-table-column prop="_padTotalCount" label="合计" align="center">
          <template slot-scope='scope'>
            <el-popover
              placement="top-start"
              :title="'支座合计（个）：'+ scope.row.padTotalCount"
              width="200"
              trigger="hover">
              <div>
                <p><el-tag>盆式橡胶支座：{{ scope.row.padBasinTypeCount }}</el-tag></p>
                <p><el-tag type="success">板式橡胶支座：{{ scope.row.padPlateTypeCount }}</el-tag></p>
                <p><el-tag type="warning">球形钢支座：{{ scope.row.padBallTypeCount }}</el-tag></p>
              </div>
              <el-button type="text" slot="reference">{{ scope.row.padTotalCount }}</el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="附属设施" align="center">
        <el-table-column prop="accessoryExpansionJointCount" label="伸缩缝" align="center"></el-table-column>
      </el-table-column>
      <el-table-column label="声屏障" align="center">
        <el-table-column prop="soundBarrierMileage" label="起止里程" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.soundBarrierMileage == undefined || scope.row.soundBarrierMileage == '' "> - </span>
            <el-popover
              v-else
              placement="top-start"
              :title="'起止里程：'"
              width="200"
              trigger="hover">
              <div>
                <div v-for="item in (scope.row.soundBarrierMileage+'').split(';')">{{ item }}</div>
              </div>
              <el-button type="text" slot="reference">{{ (scope.row.soundBarrierMileage+"").split(";").length }}</el-button>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="_soundBarrierPosition" label="位置" align="center">
          <template slot-scope='scope'>
            <span>{{ scope.row.soundBarrierPosition }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="soundBarrierType" label="形式" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.soundBarrierType == undefined || scope.row.soundBarrierType == '' "> - </span>
            <el-popover
              v-else
              placement="top-start"
              :title="'形式：'"
              width="200"
              trigger="hover">
              <div>
                {{ (scope.row.soundBarrierType+"") }}
              </div>
              <el-button type="text" slot="reference">{{ (scope.row.soundBarrierType+"").substring(0,4)+"..." }}</el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="护栏板" align="center">
        <el-table-column prop="_" label="现浇板" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.shieldedBoardSlabMileage == undefined || scope.row.shieldedBoardSlabMileage == '' "> - </span>
            <el-popover
              v-else
              placement="top-start"
              :title="'现浇板：'"
              width="250"
              trigger="hover">
              <div>
                <el-row>
                  <el-col :span="18">
                    <p>起止里程</p>
                    <div v-for="item in (scope.row.shieldedBoardSlabMileage+'').split(';')">{{ item }}</div>
                  </el-col>
                  <el-col :span="6">
                    <p>长度(m)</p>
                    <div> {{ scope.row.shieldedBoardSlabType }}</div>
                  </el-col>
                </el-row>
              </div>
              <el-button type="text" slot="reference">{{ (scope.row.shieldedBoardSlabMileage+"").split(";").length }}</el-button>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="_" label="ALC板" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.shieldedBoardAlcMileage == undefined || scope.row.shieldedBoardAlcMileage == '' "> - </span>
            <el-popover
              v-else
              placement="top-start"
              :title="'ALC板：'"
              width="250"
              trigger="hover">
              <div>
                <el-row>
                  <el-col :span="18">
                    <p>起止里程</p>
                    <div v-for="item in (scope.row.shieldedBoardAlcMileage+'').split(';')">{{ item }}</div>
                  </el-col>
                  <el-col :span="6">
                    <p>长度(m)</p>
                    <div> {{ scope.row.shieldedBoardAlcType }}</div>
                  </el-col>
                </el-row>
              </div>
              <el-button type="text" slot="reference">{{ (scope.row.shieldedBoardAlcMileage+"").split(";").length }}</el-button>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="_" label="金属板" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.shieldedBoardMetalMileage == undefined || scope.row.shieldedBoardMetalMileage == '' "> - </span>
            <el-popover
              v-else
              placement="top-start"
              :title="'金属板：'"
              width="250"
              trigger="hover">
              <div>
                <el-row>
                  <el-col :span="18">
                    <p>起止里程</p>
                    <div v-for="item in (scope.row.shieldedBoardMetalMileage+'').split(';')">{{ item }}</div>
                  </el-col>
                  <el-col :span="6">
                    <p>长度(m)</p>
                    <div> {{ scope.row.shieldedBoardMetalType }}</div>
                  </el-col>
                </el-row>
              </div>
              <el-button type="text" slot="reference">{{ (scope.row.shieldedBoardMetalMileage+"").split(";").length }}</el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="防撞设施(个)" align="center">
        <el-table-column prop="antiCollisionHeightLimitation" label="限高架" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.antiCollisionHeightLimitation == 0">-</span>
            <span v-if="scope.row.antiCollisionHeightLimitation != 0">{{ scope.row.antiCollisionHeightLimitation }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="antiCollisionSlab" label="防撞钢板" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.antiCollisionSlab == 0">-</span>
            <span v-if="scope.row.antiCollisionSlab != 0">{{ scope.row.antiCollisionSlab }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="antiCollisionRack" label="防撞架" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.antiCollisionRack == 0">-</span>
            <span v-if="scope.row.antiCollisionRack != 0">{{ scope.row.antiCollisionRack }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="antiCollisionLabel" label="防撞标贴" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.antiCollisionLabel == 0">-</span>
            <span v-if="scope.row.antiCollisionLabel != 0">{{ scope.row.antiCollisionLabel }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="antiWall" label="隔离墙" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.antiWall == 0">-</span>
            <span v-if="scope.row.antiWall != 0">{{ scope.row.antiWall }}</span>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="桥梁支座检修平台" align="center">
        <el-table-column prop="maintenancePlatformCount" label="数量(座)" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.maintenancePlatformCount == 0">-</span>
            <span v-if="scope.row.maintenancePlatformCount != 0">{{ scope.row.maintenancePlatformCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="maintenancePlatformPier" label="对应墩号" align="center">
          <template slot-scope='scope'>
            <span v-if="scope.row.maintenancePlatformPier == 0">-</span>
            <span v-if="scope.row.maintenancePlatformPier != 0">{{ scope.row.maintenancePlatformPier }}</span>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column fixed="right" width="150" prop="_" label="操作" align="center">
        <template slot-scope='scope'>
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row.uuid)">删除</el-button>
          
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.pageNum" :page-sizes="[10, 20, 50]" :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="float: left;"></el-pagination>

    <device-info :deviceInfoDialog = "deviceInfoDialog" :deviceCode="deviceCode" @changeDeviceInfoDialog="changeDeviceInfoDialog"></device-info>

    <el-dialog title="高架结构设施设备信息表" :visible.sync="imgDialogVisible" width="50%" center>
      <div class="dialog-main">
      <el-form ref="form" :model="form" label-width="150px"  size="small" height="500">
        <el-input type="hidden" v-model="form.uuid"></el-input>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>基础信息</span>
          </div>
          <el-form-item label="线路">
            <el-select v-model="form.lineCode" placeholder="请选择线路" @change="handleLineChange">
              <el-option
                v-for="item in lineOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="区间名称">
            <el-input v-model="form.sectionName"></el-input>
          </el-form-item>
          <el-form-item label="类型">
            <el-input v-model="form.basicType"></el-input>
          </el-form-item>
          <el-form-item label="起止里程">
            <el-input v-model="form.basicMileage"></el-input>
          </el-form-item>
          <el-form-item label="长度（米）">
            <el-input v-model="form.basicLength"></el-input>
          </el-form-item>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>桥墩</span>
          </div>
          <el-form-item label="合计">
            <el-input-number v-model="form.pierTotalCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="普通">
            <el-input-number v-model="form.pierNormalCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="跨路口">
            <el-input-number v-model="form.pierTransroadCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="涉水">
            <el-input-number v-model="form.pierTranswaterCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>支座</span>
          </div>
          <el-form-item label="合计">
            <el-input-number v-model="form.padTotalCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="盆式橡胶支座">
            <el-input-number v-model="form.padBasinTypeCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="板式橡胶支座">
            <el-input-number v-model="form.padPlateTypeCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="球形钢支座">
            <el-input-number v-model="form.padBallTypeCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>附属设施设备信息</span>
          </div>
          <el-form-item label="伸缩缝">
            <el-input-number v-model="form.accessoryExpansionJointCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>声屏障</span>
          </div>
          <el-form-item label="起止里程">
            <el-input v-model="form.soundBarrierMileage"></el-input>
          </el-form-item>
          <el-form-item label="位置">
            <el-input v-model="form.soundBarrierPosition"></el-input>
          </el-form-item>
          <el-form-item label="形式">
            <el-input v-model="form.soundBarrierType"></el-input>
          </el-form-item>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>护栏板</span>
          </div>
          <el-form-item label="现浇板起止里程">
            <el-input v-model="form.shieldedBoardSlabMileage"></el-input>
          </el-form-item>
          <el-form-item label="现浇板长度">
            <el-input v-model="form.shieldedBoardSlabType"></el-input>
          </el-form-item>
          <el-form-item label="ALC板起止里程">
            <el-input v-model="form.shieldedBoardAlcMileage"></el-input>
          </el-form-item>
          <el-form-item label="ALC板长度">
            <el-input v-model="form.shieldedBoardAlcType"></el-input>
          </el-form-item>
          <el-form-item label="金属板（彩钢板、铝合金）起止里程">
            <el-input v-model="form.shieldedBoardMetalMileage"></el-input>
          </el-form-item>
          <el-form-item label="金属板（彩钢板、铝合金）长度">
            <el-input v-model="form.shieldedBoardMetalType"></el-input>
          </el-form-item>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>防撞设施</span>
          </div>
          <el-form-item label="限高架（副）">
            <el-input-number v-model="form.antiCollisionHeightLimitation" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="防撞钢板（个）">
            <el-input-number v-model="form.antiCollisionSlab" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="防撞架（个）">
            <el-input-number v-model="form.antiCollisionRack" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="防撞标贴（个）">
            <el-input-number v-model="form.antiCollisionLabel" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="防护隔离墙（个）">
            <el-input-number v-model="form.antiWall" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>桥梁支座检修平台</span>
          </div>
          <el-form-item label="数量(座)">
            <el-input-number v-model="form.maintenancePlatformCount" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="对应墩号">
            <el-input v-model="form.maintenancePlatformPier"></el-input>
          </el-form-item>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>其他</span>
          </div>
          <el-form-item label="排序号">
            <el-input-number v-model="form.sortno" :min="0" :max="10000" label="描述文字"></el-input-number>
          </el-form-item>
        </el-card>
        
      </el-form>
      
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="save">保存</el-button>
    </span>
    </el-dialog>

  </div>
</template>

<script>
/* eslint-disable */
import DeviceInfo from '../index/admin/components/DeviceInfo.vue'
export default {
  name: 'bridgeBaseinfoList',
  components: {
    DeviceInfo,
  },
  data() {
    return {
      page: {
        pageNum: 1,
        pageSize: 50,
        total: 15
      },
      deviceList: [],
      param: {},
      search: '',
      deviceInfoDialog: false,
      imgDialogVisible: false,
      previewImg: '',
      deviceCode:'',
      form: {},
      lineOptions: [
        {value:'1', label:'一号线'},
        {value:'2', label:'二号线'},
        {value:'3', label:'三号线'},
        {value:'4', label:'四号线'},
        {value:'7', label:'七号线'},
        {value:'10', label:'十号线'},
        {value:'S1', label:'机场线S1号线'},
        {value:'S3', label:'宁和城际S3号线'},
        {value:'S6', label:'宁句城际S6号线'},
        {value:'S7', label:'宁溧城际S7号线'},
        {value:'S8', label:'宁天城际S8号线'},
        {value:'S9', label:'宁高城际S9号线'},
      ],
    };
  },
  methods: {
    changeDeviceInfoDialog(value) {
      this.deviceInfoDialog = value;
    },
    openDeviceInfoDialog (code) {
      this.deviceInfoDialog = true;
      this.deviceCode = code;
    },
    previewImage(value) {
      this.previewImg = value;
      this.imgDialogVisible = true;
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.searchData();
    },
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.searchData();
    },
    searchData() {
      this.$request({
        url: '/tmbridgebaseinfo/getPageData/' + this.page.pageNum + '/' + this.page.pageSize,
        method: 'post',
        data: this.param
      }).then(res => {
        let data = res.data.result_data;
        this.page.total = data.total;
        this.page.pageNum = data.pageNum;
        this.page.pageSize = data.pageSize;
        this.deviceList = data.list;
      });

    },
    handleLineChange(nv) {
      const filteredItem= this.lineOptions.filter(item => item.value === nv);
      this.form.lineName = filteredItem[0].label;
    },
    handleEdit(form){
      this.imgDialogVisible = true;
      this.form = form;
    },
    save() {
      this.$request({
        url: '/tmbridgebaseinfo/saveTmBridgeBaseinfo',
        method: 'post',
        data: this.form
      }).then(res => {
        this.$message({
          message: '保存成功',
          type: 'success'
        });
        this.searchData();
        this.imgDialogVisible = false;
      });
    },
    handleAdd() {
      this.imgDialogVisible = true;
      this.form = {};
    },
    handleDelete(uuid) {
      this.$confirm('是否删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request({
          url: '/tmbridgebaseinfo/deleteTmBridgeBaseinfo/'+ uuid,
          method: 'post',
        }).then(res => {
          this.$message({
            message: '删除成功',
            type: 'success'
          });
          this.searchData();
        });
      }).catch(() => {
                  
      });
      
    },
  },
  mounted: function () {
    this.searchData();
  }
};
</script>

<style scoped>
  .dialog-main {
    height: 60vh;
    overflow:auto;
  }
  .box-card {
    margin-bottom: 10px;
  }
  
  /deep/ .el-card__header {
    padding: 5px 20px;
  }
  /deep/ .el-card__body {
    padding: 5px 20px;
  }
  /deep/ .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }

</style>
