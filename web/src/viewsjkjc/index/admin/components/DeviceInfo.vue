<template>
  <div>
    <el-dialog title="" :visible.sync="deviceInfoDialog" width="50%" :before-close="handleClose">
      <el-row :gutter="20" style="height: 550px;">
        <el-col :span="12" :offset="0" class="left">
          <el-row type="flex" :gutter="20" style="margin-top: 25px;">
            <el-col :span="18" :offset="0">
              <div style="font-size: 32px;color:#1684FC;">{{ deviceMap.segment  + '(' + deviceMap.stakeNumber + ')'}}</div>
              <!-- <el-progress :percentage="deviceMap.electricPercentage"></el-progress> -->
            </el-col>
            <el-col :span="6" :offset="0" style="text-align: center;">
              <el-button type="success" :circle=true icon="el-icon-check"></el-button>
              <div style="color:#1684FC">运行正常</div>
            </el-col>
          </el-row>
          <div>

          </div>
          <el-card class="box-card">
            <div v-for="(o,index) in deviceInfo" :key="index" class="text item">
              {{ o.label + ': ' + o.value }}
            </div>
          </el-card>
        </el-col>
        <el-col :span="12" :offset="0">
          <div>
            <div style="display:inline-block; font-size: 28px;color:#7F83F7;">最新消息</div>
            <div style="display:inline-block; float: right;padding-top: 15px;;">
              <!-- <span type="primary">查看更多>></span> -->
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
                    <div style="font-size: 14px;font-weight: bold;">{{ message.segment + '-' + message.stakeNumber + '&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp;' +message.timstamp}} </div>

                    <div style="font-size: 14px;" v-if="message.catalogval == '0' && (message.voltage/450*100) >= 50">{{ '设备运行状态' +
                      (message.catalogval == "0" ? "正常" : "正常") + "，电池余量" + (message.voltage/450*100).toFixed(0) + "%" }}</div>
                    <div style="font-size: 14px;color: black;" v-else>{{ '设备运行状态' + (message.catalogval == "0" ? "正常" : "正常") + "，电池余量"
                      + (message.voltage/500*100).toFixed(0) + "%" }}</div>
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
  props: ["deviceInfoDialog","deviceCode"],
  data() {
    return {
      dialogVisible: this.deviceInfoDialog,
      deviceInfo: [],
      deviceMap: {},
      messages: [],
    };
  },
  methods: {
    handleClose(done) {
      this.$emit("changeDeviceInfoDialog", false);
    },
    getInfo() {
      this.$request({
        url: '/tmDevice/getDeviceDataByCode/' + this.deviceCode,
        method: 'post',
        data: {}
      }).then(res => {
        let data = res.data.result_data;
        data['electricVolume'] = 400;
        data['electricPercentage'] = (data.signalval/35*100).toFixed(0);
        this.deviceMap = data;
        this.deviceInfo = [
        { label: "设备IMEI", value: data.code },
        { label: "监测项目", value: data.type },
        // { label: "电池余量", value: data.electricPercentage + '%' },
        // { label: "电池容量", value: "450mAh" },
        { label: "线路", value: data.line },
        { label: "区间", value: data.segment },
        { label: "桩号/桥墩号", value: data.stakeNumber },
        { label: "安装位置", value: data.installPosition },
        // { label: "安装日期", value: "2023-11-28" },
      ]
      });
      
    },
    getDeviceMessage() {
      let param = {};
      param.clientimei = this.deviceCode;
      this.$request({
        url: '/tmOriginData/getPageList/1/15',
        method: 'post',
        data: param
      }).then(res => {
        let data = res.data.result_data;
        this.messages = data.list;
      });
    }
  },
  watch: {
    deviceCode: {
      handler(nv,ov) {
        this.getInfo();
        this.getDeviceMessage();
      }
    }
  },
  mounted: function () {
    
  }
};
</script>

<style scoped>
.left{
  height: 550px;
  background:url('../../../../../static/images/project/dialog-background.png');
}
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