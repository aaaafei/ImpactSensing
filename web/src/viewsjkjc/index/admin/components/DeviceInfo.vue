<template>
  <div>
    <el-dialog title="" :visible.sync="deviceInfoDialog" width="50%" :before-close="handleClose">
      <el-row :gutter="20" style="height: 550px;">
        <el-col :span="12" :offset="0"
          style="height: 550px;background:url('../../../../../static/images/project/dialog-background.png');">
          <el-row type="flex" :gutter="20" style="margin-top: 25px;">
            <el-col :span="18" :offset="0">
              <div style="font-size: 32px;color:#1684FC;">马群新街001</div>
              <el-progress :percentage="50"></el-progress>
            </el-col>
            <el-col :span="6" :offset="0" style="text-align: center;">
              <el-button type="success" circle="true" icon="el-icon-check"></el-button>
              <div style="color:#1684FC">运行正常</div>
            </el-col>
          </el-row>
          <div>

          </div>
          <el-card class="box-card">
            <div v-for="o in deviceInfo" :key="o" class="text item">
              {{ o.label + ': ' + o.value }}
            </div>
          </el-card>
        </el-col>
        <el-col :span="12" :offset="0">
          <div>
            <div style="display:inline-block; font-size: 28px;color:#7F83F7;">最新消息</div>
            <div style="display:inline-block; float: right;padding-top: 15px;;"><el-link type="primary">查看更多>></el-link>
            </div>
          </div>

          <div class="message-list">
            <ul>
              <li v-for="(message, index) in messages" :key="index" :class="{ 'new-message': index === 0 }">
                <el-row :gutter="5">
                  <el-col :span="4" :offset="0">
                    <div>
                      <img src="../../../../../static/images/project/device01.png" alt="">
                    </div>
                  </el-col>
                  <el-col :span="16" :offset="0">
                    <div style="font-size: 14px;font-weight: bold;">{{ message.name }}</div>
                    <div style="font-size: 14px;" v-if="message.status == '1' && message.battery >= 50">{{ '设备运行状态' +
                      (message.status == "1" ? "正常" : "异常") + "，电池余量" + message.battery + "%" }}</div>
                    <div style="font-size: 14px;color: red;" v-else>{{ '设备运行状态' + (message.status == "1" ? "正常" : "异常") + "，电池余量"
                      + message.battery + "%" }}</div>
                  </el-col>
                  <el-col :span="4" :offset="0">
                    <div style="float: right;">{{ message.time }}</div>
                  </el-col>
                </el-row>

              </li>
            </ul>
          </div>
        </el-col>
      </el-row>

    </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */
export default {
  props: ["deviceInfoDialog"],
  data() {
    return {
      dialogVisible: this.deviceInfoDialog,
      deviceInfo: {},
      messages: [],
    };
  },
  methods: {
    handleClose(done) {
      this.$emit("changeDeviceInfoDialog", false);
    },
    getInfo() {
      this.deviceInfo = [
        { label: "设备编码", value: "020040010050001" },
        { label: "设备名称", value: "马群新街002号" },
        { label: "电池余量", value: "55%" },
        { label: "电池容量", value: "2000mAh" },
        { label: "线路", value: "2号线" },
        { label: "方向", value: "下行路" },
        { label: "桩号", value: "LK4+300" },
        { label: "桥墩号", value: "QDH02004001" },
        { label: "安装日期", value: "2023-04-01" },
      ]
    },
    getDeviceMessage() {
      this.$request({
        url: '/api/info/getDeviceInfo',
        method: 'post',
        data: {}
      }).then(res => {
        this.messages = res.data.data.list;
      });
    }
  },
  mounted: function () {
    this.getInfo();
    this.getDeviceMessage();
  }
};
</script>

<style scoped>
.text {
  font-size: 18px;
  color: #1684FC;
}

.item {
  padding: 8px 0;
}

.box-card {
  margin-top: 30px;
  height: 420px;
  opacity: 0.8;
}

/* message style */
.message-list {
  height: 515px;
  overflow-y: scroll;
  overflow-x: hidden;
}

ul {
  list-style-type: none;
  padding: 0;
}

ul li {
  background-color: #f0f2f5;
  padding: 2px;
  margin-bottom: 5px;
  transition: transform 0.3s ease;
}

.new-message {
  background-color: #d3eaff;
  animation: slide-down 0.3s;
}</style>