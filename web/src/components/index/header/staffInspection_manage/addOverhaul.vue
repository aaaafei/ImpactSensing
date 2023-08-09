<template>
  <div id="addOverhauldiven1">
    <el-dialog :title="title" :visible="addgdVisible" @open="open()" :before-close="handleClose" width="60%" append-to-body>
      <el-form  ref="overForm" :model="form" :rules="rules" label-width="100px" id="addoverForm1" v-loading="gdloading">
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
          <el-input
            id="workFrom"
            v-model="form.workFromName"
            disabled="disabled"
            />
        </el-form-item>
        <el-form-item label="检修部门：" for="depart">
          <el-input
            id="depart"
            v-model="form.orgName"
            readonly="readonly"
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
        <el-form-item label="完成时间：" for="time">
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
            :rows="4"
            v-model="form.remark"
            placeholder="请输入备注"/>
        </el-form-item>
        <el-form-item label="设备：" id="equip" prop="equip" for="equip">
          <el-table
            :data="equipList"
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
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="close('overForm', 'Y')">取 消</el-button>
        <el-button type="primary" @click="save('overForm')">保 存</el-button>
        <el-button type="primary" @click="add('overForm')">提 交</el-button>
      </div>
      <orgTree :orgVisible= "orgVisible" v-on:headCallBack="callBack">
      </orgTree>
    </el-dialog>
  </div>
</template>

<script>
import orgTree from '../../../maintenance_manage/routeInspection_manage/orgTree.vue';
export default {
  name: 'addOverhaul1',
  props: ['addgdVisible', 'title', 'workfromId', 'deviceId', 'compt'],
  data: function () {
    return {
      gdloading: false,
      orgVisible: false, // 组织树弹出
      form: {
        id: '', // 工单id
        code: '', // 工单编码
        name: '', // 工单名称
        startTime: '', // 开始时间
        endTime: '', // 完成期限
        workFrom: this.workfromId, // 工单来源
        workFromName: '', // 工单来源名称
        compt: '', // 分区id
        org: '', // 检修部门id
        orgName: '', // 检修部门
        deviceType: '', // 设备类型
        device: '', // 设备id
        deviceName: '', // 设备名称
        status: '', // 工单状态
        statusName: '', // 状态名称
        remark: ''// 备注
      },
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
      sourceList: [] // 工单来源
    };
  },
  components: {
    orgTree: orgTree
  },
  methods: {
    open () {
      // 根据工单来源id,获得工单来源名
      this.gdloading = true;
      this.queryworkfromList();
      // 根据id查设备
      this.queryEquipById();
    },
    queryworkfromList () { // 查询工单来源list
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        }
      };
      this.$http.get(this.$api_prefix + '/dict/codeMap/work_from_type', options).then((res) => {
        if (res.body.code === 401) {
          this.$router.push('/login');
          return false;
        }
        let success = res.body.success;
        if (success === true) {
          let result = res.body.data.work_from_type;
          this.form.workFromName = result[this.form.workFrom];
        } else {
          this.$message.error(res.body.message);
        }
      }, (res) => {
        this.$message.error('服务器暂时无法连接，请稍后再试！');
      });
    },
    handleClose () { // 叉
      this.$emit('headCallBack', false); // 给父组件传值，隐藏弹出框
      this.equipList = [];
    },
    close (formName) { // 点击取消按钮
      // this.$refs[formName].resetFields(); // 清空验证信息
      this.$emit('headCallBack', false); // 给父组件传值，隐藏弹出框
      this.equipList = [];
    },
    add (formName) { // 提交
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
          device: this.form.device,
          code: this.form.code,
          status: this.form.status,
          remark: this.form.remark
        }
      };
      var Self = this;
      this.$refs[formName].validate(function (valid) { // form 表单验证
        if (valid) {
          Self.gdloading = true;
          Self.$http.get(this.$api_prefix + '/overhaul/submit', options).then((res) => { // 提交工单
            if (res.body.code === 401) {
              Self.$router.push('/login');
              return false;
            }
            let success = res.body.success;
            if (success === true) {
              Self.$message({
                type: 'success',
                message: '提交成功'
              });
              Self.gdloading = false;
              Self.$emit('headCallBack', false); // 给父组件传值，隐藏弹出框
              Self.$refs['overForm'].resetFields(); // 清空验证信息
            } else {
              Self.gdloading = false;
              Self.$message.error(res.body.message);
            }
          }, (res) => {
            Self.$message.error('服务器暂时无法连接，请稍后再试！');
            Self.gdloading = false;
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    save (formName) {  // 保存
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
          device: this.form.device,
          code: this.form.code,
          status: this.form.status,
          remark: this.form.remark
        }
      };
      var Self = this;
      let url = this.$api_prefix + '/overhaul/add';
      if (Self.form.id === '' || Self.form.id === undefined) {
        url = this.$api_prefix + '/overhaul/add';
      } else {
        url = this.$api_prefix + '/overhaul/update';
      }
      this.$refs[formName].validate(function (valid) { // form 表单验证
        if (valid) {
          Self.gdloading = true;
          Self.$http.get(url, options).then((res) => { // 调用添加巡检接口
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
              Self.gdloading = false;
              Self.$emit('headCallBack', false); // 给父组件传值，隐藏弹出框
              Self.$refs['overForm'].resetFields(); // 清空验证信息
            } else {
              Self.gdloading = false;
              Self.$message.error(res.body.message);
            }
          }, (res) => {
            Self.$message.error('服务器暂时无法连接，请稍后再试！');
            Self.gdloading = false;
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    querydeptList () { // 查询巡检部门
      this.orgVisible = true;
    },
    queryEquipById () { // 根据设备id查询设备信息
      this.form.device = this.deviceId;
      let equipId = this.form.device; // 设备id
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        }
      };
      if (equipId !== '') {
        this.$http.get(this.$api_prefix + '/device/' + equipId, options).then((res) => {
          if (res.body.code === 401) {
            this.$router.push('/login');
            return false;
          }
          let success = res.body.success;
          if (success === true) {
            let result = res.body.data;
            this.form.compt = result.compt;
            this.equipList.push(result);
            this.gdloading = false;
          } else {
            this.$message.error(res.body.message);
            this.gdloading = false;
          }
        }, (res) => {});
      }
    },
    callBack (msg, depart, multipleSelection) {
      this.orgVisible = msg;
      this.equipVisible = msg;
      if (depart !== null && depart !== undefined) {
        this.form.org = depart['id'];
        this.form.orgName = depart['label'];
      }
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
#addoverForm1{
  height:320px;
  overflow-y: auto;
  overflow-x: hidden;
  padding-left:10px;
  padding-bottom:10px;
}
#addoverForm1 .el-input{
  width: 75%;
}
#addoverForm1 .el-textarea{
  width: 75%;
}
#addoverForm1 #depart {
  cursor:pointer;
}
</style>
