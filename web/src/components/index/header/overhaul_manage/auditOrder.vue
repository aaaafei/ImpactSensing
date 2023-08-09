<template>
  <div id="auditOrder1">
    <el-dialog title="工单审核" :visible="auditVisible" @open="open()" :before-close="handleClose">
      <el-form  ref="overForm" :model="form"  label-width="110px" >
        <el-form-item label="检修结果：" prop="result" for="result">
          <el-input
          id="result"
          v-model="form.result"
          readonly
          placeholder="无检修结果">
          </el-input>
        </el-form-item>
        <el-form-item label="结果状态：" prop="statusName" for="statusName">
          <el-input
          id="statusName"
          v-model="form.statusName"
          readonly
          placeholder="无结果类型"/>
        </el-form-item>
        <el-form-item label="实际完成时间：" for="endTime">
          <el-date-picker
          id="endTime"
          type="dateTime"
          v-model="form.endTime"
          format="yyyy-MM-dd HH:mm"
          readonly
          value-format="yyyy-MM-dd"
          placeholder="无实际完成时间">
          </el-date-picker>
        </el-form-item>
        <el-row>
          <el-form-item  v-for="n in form.workOrderMaterialVos" :key="n.id" label="领用备品备件：" prop="spare" for="spare">
            <el-col :span="10">
              <el-select v-model="n.materialId" disabled placeholder="无备品备件">
                <el-option
                  v-for="item in spareList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>

              <!--<el-input
              id="name"
              readonly
              v-model="n.materialId"
              placeholder="无备品备件"/>-->
              <!--v-model="n.materialName"-->
            </el-col>
            <el-col :span="14" class="number">
            数量：<el-input-number v-model="n.aAmount" disabled :step="1" :min="0"></el-input-number>
            </el-col>
          </el-form-item>
        </el-row>
        <el-form-item label="检修图片：" prop="img" for="img">
          <el-upload
            disabled
            ref="upload"
            :file-list="fileList"
            action=""
            :headers="headers"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
        <el-form-item label="审核意见：" for="remark">
        <el-input
          id="remark"
          type="textarea"
          :rows="3"
          v-model="remark"
          placeholder="请输入审核意见"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="close('overForm')">取 消</el-button>
        <el-button type="primary" @click="reback('overForm')">打回</el-button>
        <el-button type="success" @click="audit('overForm')">通过</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'auditOrder1',
  props: ['auditVisible', 'auditId'],
  data: function () {
    return {
      auditVisible2: this.auditVisible, // 页面可见性
      form: {},
      fileList: [],
      remark: '',
      dialogImageUrl: '',
      dialogVisible: false,
      spareList: []
    };
  },
  methods: {
    open () {
      this.querySpaceParts();
      this.queryAduitDetail();
    },
    handleClose (done) {
      this.fileList = [];
      this.auditVisible2 = false;
      this.$emit('headCallBack', this.auditVisible2); // 给父组件传值，隐藏弹出框
    },
    close (formName, flag) {
      this.auditVisible2 = false;
      this.$refs[formName].resetFields();
      this.$emit('headCallBack', this.auditVisible2); // 给父组件传值，隐藏弹出框
      this.remark = '';
      this.fileList = [];
    },
    audit (formName) {
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        },
        params: {
          workId: this.auditId,
          remark: this.remark,
          ok: true
        }
      };
      var Self = this;
      this.$refs[formName].validate(function (valid) { // form 表单验证
        if (valid) {
          Self.$http.get(this.$api_prefix + '/overhaul/audit', options).then((res) => {
            if (res.body.code === 401) {
              Self.$router.push('/login');
              return false;
            }
            let success = res.body.success;
            if (success === true) {
              Self.$message({
                type: 'success',
                message: '工单通过成功'
              });
              Self.auditVisible2 = false;
              Self.$refs[formName].resetFields();
              Self.$emit('headCallBackAuditOrderSubmit', Self.auditVisible2); // 给父组件传值，隐藏弹出框
              Self.remark = '';
              Self.fileList = [];
            } else {
              this.$message.error(res.body.message);
            }
          }, (res) => {
            this.$message.error('服务器暂时无法连接，请稍后再试！');
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    reback (formName) {
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        },
        params: {
          workId: this.auditId,
          remark: this.remark,
          ok: false
        }
      };
      var Self = this;
      this.$refs[formName].validate(function (valid) { // form 表单验证
        if (valid) {
          Self.$http.get(this.$api_prefix + '/overhaul/audit', options).then((res) => {
            if (res.body.code === 401) {
              Self.$router.push('/login');
              return false;
            }
            let success = res.body.success;
            if (success === true) {
              Self.$message({
                type: 'success',
                message: '工单打回成功'
              });
              Self.auditVisible2 = false;
              Self.$refs[formName].resetFields();
              Self.$emit('headCallBackAuditOrderSubmit', Self.auditVisible2); // 给父组件传值，隐藏弹出框
              Self.remark = '';
            } else {
              this.$message.error(res.body.message);
            }
          }, (res) => {
            this.$message.error('服务器暂时无法连接，请稍后再试！');
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    queryAduitDetail () {
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        }
      };
      this.$http.get(this.$api_prefix + '/overhaul/findExe/' + this.auditId, options).then((res) => {
        if (res.body.code === 401) {
          this.$router.push('/login');
          return false;
        }
        let success = res.body.success;
        if (success === true) {
          this.form = res.body.data;
          let result = res.body.data.attach;
          this.fileList = [];
          for (let i in result) {
            let fileObject = {
              name: '',
              url: ''
            };
            fileObject.name = result[i].id;
            fileObject.url = this.$api_prefix + '/attach/download/' + result[i].id + '?Authorization=' + this.$store.getters.getToken;
            this.fileList[i] = fileObject;
          }
        } else {
          this.$message.error(res.body.message);
        }
      }, (res) => {});
    },
    handlePictureCardPreview (file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    querySpaceParts () { // 查询所有备品备件
      let options = {
        headers: {
          Authorization: this.$store.getters.getToken
        }
      };
      this.$http.get(this.$api_prefix + '/material/list', options).then((res) => {
        if (res.body.code === 401) {
          this.$router.push('/login');
          return false;
        }
        let success = res.body.success;
        if (success === true) {
          let result = res.body.data;
          for (let i in result) {
            let spare = {
              value: result[i].id,
              label: result[i].name
            };
            this.spareList.push(spare);
          };
        } else {
          this.$message.error(res.body.message);
        }
      }, (res) => {
        this.$message.error('服务器暂时无法连接，请稍后再试！');
      });
    }
  },
  computed: {
    headers () {
      return {
        Authorization: this.$store.getters.getToken
      };
    }
  },
  mounted: function () {
    this.$nextTick(function () {
      let token = this.$store.getters.getToken;
      if (token === '') {
        console.log('token没有，重新赋值');
        this.$store.dispatch('refresh');
      };
    });
  }
};
</script>

<style type="stylus">
.number {
  padding-left: 10px;
}
#auditOrder1 .el-dialog {
  width: 50%;
}
#auditOrder1 .el-dialog__wrapper{
  z-index: 99999;
  background-color:rgba(0, 0, 0, 0.5);
}
#auditOrder1 .el-dialog .el-form {
  width: 100%;
  margin: 0px auto;
}
#auditOrder1 .el-upload--picture-card {
  display: none;
}
</style>
