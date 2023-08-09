<template>
  <div id="addOverhaul1">
    <el-dialog :title="title" :visible="addVisible" @open="open()" :before-close="handleClose" id="addOverhaulDailogId1">
      <el-form  ref="overForm" :model="form" :rules="rules" label-width="100px" v-loading="addLoading">
        <el-form-item label="工单编号：" for="code">
          <el-input
          id="code"
          v-model="form.code"
          placeholder="请输入工单编号"/>
        </el-form-item>
        <el-form-item label="工单名称：" for="orderName">
        <el-input
          id="orderName"
          v-model="form.name"
          placeholder="请输入工单名称"/>
        </el-form-item>
        <el-form-item label="工单来源：" for="workFrom">
          <el-select
          id="workFrom"
          v-model="form.workFrom"
          placeholder="请选择工单来源">
            <el-option
            v-for="item in sourceList2"
            :key="item.value"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="检修部门：" for="depart">
          <el-input
          id="depart"
          v-model="form.orgName"
          @click.native="querydeptList()"
          placeholder="请选择检修部门">
          </el-input>
        </el-form-item>
        <el-form-item label="开始时间：" for="time">
          <el-date-picker
          id="time"
          type="datetime"
          v-model="form.startTime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyyMMddHHmmss"
          placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="完成期限：" for="time">
          <el-date-picker
          id="time"
          type="datetime"
          v-model="form.endTime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyyMMddHHmmss"
          placeholder="请选择完成期限">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="备注：" for="comments">
        <el-input
          id="comments"
          type="textarea"
          :rows="3"
          v-model="form.remark"
          placeholder="请输入备注"/>
        </el-form-item>
        <el-form-item label="设备列表：" id="equip" prop="equip" for="equip">
          <el-button type="primary" size="mini" @click="addEquip('')">添加</el-button>
          <el-table
            :data="equipList"
            height="200px"
            stripe
            style="width: 100%">
            <el-table-column
              align="center"
              prop="name"
              show-overflow-tooltip
              label="设备名称">
            </el-table-column>
            <el-table-column
              align="center"
              prop="code"
              label="设备编号">
            </el-table-column>
            <el-table-column
              align="center"
              prop="deviceTypeName"
              label="设备类型">
            </el-table-column>
            <el-table-column
              align="center"
              prop="comptName"
              label="所在分区">
            </el-table-column>
            <el-table-column
              align="center"
              label="操作">
              <template slot-scope="scope">
                <el-button
                  @click="deleteEquip(scope.$index, scope.row)" type="text">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose('overForm', 'Y')">取 消</el-button>
        <el-button type="primary" @click="save('overForm')">保 存</el-button>
        <el-button type="primary" @click="add('overForm')">提 交</el-button>
      </div>
      <orgTree :orgVisible= "orgVisible" v-on:headCallBack="callBack">
      </orgTree>
      <equip :equipVisible= "equipVisible" v-on:headCallBack="callBack">
      </equip>
    </el-dialog>
  </div>
</template>

<script>
import $ from 'jquery';
import orgTree from '../../../maintenance_manage/routeInspection_manage/orgTree.vue';
import equip from './equipList.vue';
export default {
  name: 'addOverhaul1',
  props: ['addVisible', 'propForm', 'title', 'sectionList', 'sourceList'],
  data: function () {
    return {
      addLoading: false,
      orgVisible: false, // 组织树弹出
      equipVisible: false,
      addVisible2: this.addVisible, // 页面可见性
      form: this.propForm,
      rules: { // 表单检验规则
        /* deptName: [
          { required: true, message: '请选择巡视部门', trigger: 'change' }
          // blur失去焦点时触发验证
        ],
        place: [
          { required: true, message: '请选择巡视区域', trigger: 'change' }
        ],
        time: [
          { required: true, message: '请选择截止时间', trigger: 'change' }
          // change 值发生改变时触发验证
        ],
        content: [
          { required: true, message: '请输入巡视内容', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择巡视类型', trigger: 'change' }
        ] */
      },
      equipList: [], // 设备list
      sourceList2: this.sourceList // 工单来源
    };
  },
  components: {
    orgTree: orgTree,
    equip: equip
  },
  methods: {
    open () {
      this.queryEquipById();
    },
    handleClose () {
      this.$refs['overForm'].resetFields(); // 清空验证信息
      this.addVisible2 = false;
      this.$emit('headCallBack', this.addVisible2); // 给父组件传值，隐藏弹出框
      this.equipList = [];
    },
    add (formName) {
      let equipIds = '';
      if (this.equipList.length >= 1) {
        for (var i = this.equipList.length - 1; i >= 0; i--) {
          equipIds += this.equipList[i].id + ',';
        }
      };
      this.form.startTime = this.form.startTime.replace(/\s/g, '');
      this.form.startTime = this.form.startTime.replace(/:/g, '');
      this.form.startTime = this.form.startTime.replace(/-/g, '');
      this.form.endTime = this.form.endTime.replace(/\s/g, '');
      this.form.endTime = this.form.endTime.replace(/:/g, '');
      this.form.endTime = this.form.endTime.replace(/-/g, '');
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        },
        params: {
          id: this.form.id,
          name: this.form.name,
          startTime: this.form.startTime,
          endTime: this.form.endTime,
          workFrom: this.form.workFrom,
          compt: this.form.compt,
          org: this.form.org,
          device: equipIds.substring(0, equipIds.length - 1),
          code: this.form.code,
          status: this.form.status,
          remark: this.form.remark
        }
      };
      var Self = this;
      this.$refs[formName].validate(function (valid) { // form 表单验证
        if (valid) {
          Self.addLoading = true;
          Self.$http.get(this.$api_prefix + '/overhaul/submit', options).then((res) => { // 调用添加检修工单接口
            if (res.body.code === 401) {
              Self.$router.push('/login');
              return false;
            }
            let success = res.body.success;
            if (success === true) {
              Self.$message({
                type: 'success',
                message: '保存成功'
              });
              Self.addLoading = false;
              Self.$refs['overForm'].resetFields(); // 清空验证信息
              Self.addVisible2 = false;
              Self.$emit('headCallBackSubmit', Self.addVisible2); // 给父组件传值，隐藏弹出框
              Self.equipList = [];
            } else {
              Self.addLoading = false;
              Self.$message.error(res.body.message);
            }
          }, (res) => {
            Self.addLoading = false;
            Self.$message.error('服务器暂时无法连接，请稍后再试！');
          });
          Self.equipList = [];
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    save (formName) {
      let equipIds = '';
      if (this.equipList.length >= 1) {
        for (var i = this.equipList.length - 1; i >= 0; i--) {
          equipIds += this.equipList[i].id + ',';
        }
      };
      this.form.startTime = this.form.startTime.replace(/\s/g, '');
      this.form.startTime = this.form.startTime.replace(/:/g, '');
      this.form.startTime = this.form.startTime.replace(/-/g, '');
      this.form.endTime = this.form.endTime.replace(/\s/g, '');
      this.form.endTime = this.form.endTime.replace(/:/g, '');
      this.form.endTime = this.form.endTime.replace(/-/g, '');
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        },
        params: {
          id: this.form.id,
          name: this.form.name,
          startTime: this.form.startTime,
          endTime: this.form.endTime,
          workFrom: this.form.workFrom,
          compt: this.form.compt,
          org: this.form.org,
          device: equipIds.substring(0, equipIds.length - 1),
          code: this.form.code,
          status: this.form.status,
          remark: this.form.remark
        }
      };
      let url = '';
      if (options.params.id !== '' && options.params.id !== undefined) {
        url = this.$api_prefix + '/overhaul/update';
      } else {
        url = this.$api_prefix + '/overhaul/add';
      }
      var Self = this;
      this.$refs[formName].validate(function (valid) { // form 表单验证
        if (valid) {
          Self.addLoading = true;
          Self.$http.get(url, options).then((res) => { // 调用修改检修工单接口
            if (res.body.code === 401) {
              Self.$router.push('/login');
              return false;
            }
            let success = res.body.success;
            if (success === true) {
              Self.$message({
                type: 'success',
                message: res.body.message
              });
              Self.addLoading = false;
              Self.$refs['overForm'].resetFields(); // 清空验证信息
              Self.addVisible2 = false;
              Self.$emit('headCallBackSubmit', Self.addVisible2); // 给父组件传值，隐藏弹出框
              Self.equipList = [];
            } else {
              Self.addLoading = false;
              Self.$message.error(res.body.message);
            }
          }, (res) => {
            Self.addLoading = false;
            Self.$message.error('服务器暂时无法连接，请稍后再试！');
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    querydeptList () { // 查询巡检部门
      this.orgVisible = true;
      setTimeout(function () {
        $('.v-modal').css('z-index', '0 !important');
      }, 100);
    },
    queryEquipById () { // 根据设备id查询设备信息
      let equipIds = this.form.device.split(',');
      for (var i = 0; i < equipIds.length; i++) {
        let options = {
          headers: {
            Authorization: this.$store.getters.getToken
          }
        };
        if (equipIds[i] !== '') {
          this.$http.get(this.$api_prefix + '/device/' + equipIds[i], options).then((res) => { // 调用查询巡检计划列表接口
            if (res.body.code === 401) {
              this.$router.push('/login');
              return false;
            }
            let success = res.body.success;
            if (success === true) {
              let result = res.body.data;
              this.equipList.push(result);
            } else {
              this.$message.error(res.body.message);
            }
          }, (res) => {
            this.$message.error('服务器暂时无法连接，请稍后再试！');
          });
        };
      }
    },
    addEquip () {
      if (this.equipList.length >= 1) {
        this.$message({
          type: 'info',
          message: '只能选择一个设备，无法继续添加！'
        });
      } else {
        this.equipVisible = true;
      }
    },
    deleteEquip (index, rows) { // 删除
      this.$confirm('确认删除此设备?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
        this.equipList.splice(index, 1);
        setTimeout(function () {
          $('.v-modal').css('z-index', '0 !important');
        }, 100);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
        setTimeout(function () {
          $('.v-modal').css('z-index', '0 !important');
        }, 100);
      });
    },
    callBack (msg, depart, multipleSelection) {
      this.orgVisible = msg;
      this.equipVisible = msg;
      if (depart !== null && depart !== undefined) {
        this.form.org = depart['id'];
        this.form.orgName = depart['label'];
      };
      if (multipleSelection != null) {
        this.equipList.push(multipleSelection);
      }
    }
  },
  mounted: function () {
    this.$nextTick(function () {
    });
  }
};
</script>

<style>
  #addOverhaulDailogId1 .el-dialog__body{
    height: 300px;
    overflow: auto;
  }
  #addOverhaul1 .el-dialog {
     width: 60%;
   }
  #addOverhaul1 .el-dialog__wrapper{
    z-index: 99999;
    background-color:rgba(0, 0, 0, 0.5);
  }
  #addOverhaul1 .el-dialog .el-form {
    width: 100%;
    margin: 0 auto;
  }
</style>
