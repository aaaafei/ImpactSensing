<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="12" :offset="0">
        <div style="float: left;">
          <span style="font-weight:bold;color: #1684FC;">1小时内动态信息</span>
        </div>
      </el-col>
      <el-col :span="12" :offset="0">
        <div style="float: right;">
          <span style="color: #1684FC;">{{ currentTime }}</span>
        </div>
      </el-col>
    </el-row>
    <div>
      <el-card class="box-card" shadow="never" :body-style="{ padding: '5px' }">
        <div slot="header" class="clearfix">
          <span>设备信息</span>
          <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-refresh" v-on:click="searchData()">刷新</el-button>
        </div>
        <div class="message-list">
          <ul>
            <li v-for="(message, index) in messages" :key="index" :class="{ 'new-message': index === 0 }">
              <el-row :gutter="5">
                <el-col :span="4" :offset="0">
                  <div style="cursor: pointer;" @click="openDeviceInfoDialog(message.clientimei)">
                    <img src="../../../../../static/images/project/device01.png" alt="">
                  </div>
                </el-col>
                <el-col :span="16" :offset="0">
                  <div style="font-size: 14px;font-weight: bold;">{{ message.segment + '-' + message.stakeNumber }}</div>
                    <div style="font-size: 14px;" v-if="message.catalogval == '0' && (message.voltage/500*100) >= 50">{{ '设备运行状态' +
                      (message.catalogval == "0" ? "正常" : "异常") + "，电池余量" + (message.voltage/500*100).toFixed(0) + "%" }}</div>
                    <div style="font-size: 14px;color: red;" v-else>{{ '设备运行状态' + (message.catalogval == "0" ? "正常" : "异常") + "，电池余量"
                      + (message.voltage/500*100).toFixed(0) + "%" }}</div>
                </el-col>
                <el-col :span="4" :offset="0">
                  <div style="float: right;">{{ message.time }}</div>
                </el-col>
              </el-row>
              
            </li>
          </ul>
        </div>
      </el-card>
    </div>

    <device-info :deviceInfoDialog = "deviceInfoDialog" :deviceCode="deviceCode" @changeDeviceInfoDialog="changeDeviceInfoDialog"></device-info>
  </div>
</template>

<script>
/* eslint-disable */
import {formatDate,formatDates} from '@/utils/dateUtils';
import DeviceInfo from './DeviceInfo';
export default {
  components: {
    DeviceInfo,
  },
  props: ['tableData'],
  data() {
    return {
      messages: [],
      currentTime: '',
      deviceInfoDialog: false,
      deviceCode: ''
    };
  },
  methods: {
    addNewMessage() {
      let i = Math.floor(Math.random() * (5 - 0 + 1)) + 0;
      let message = {
        name: ["马群新街01","马群新街02","马群新街03","马群新街04","马群新街05"][i % 5],
        position: ["马群新街01桥墩","马群新街02桥墩","马群新街03桥墩","马群新街04桥墩","马群新街05桥墩"][i % 5],
        status: "" + Math.round(Math.random()),
        battery: Math.floor(Math.random() * (99 - 30 + 1)) + 30,
        time: new Date().getHours() + ":" + new Date().getMinutes()
      };
      this.messages.unshift(message);
      this.messages = this.messages.slice(0, 20);
    },
    getLatestDeviceMessage() {
      this.$request({
        url: '/api/info/getLatestDeviceInfo',
        method: 'post',
        data: {}
      }).then(res => {
        let data = res.data.data.list;
        this.messages.unshift(data[0]);
        this.messages = this.messages.slice(0, 20);
      });
    },
    getDeviceMessage() {
      let param = {};
      // param.clientimei = this.deviceCode;
      this.$request({
        url: '/tmOriginData/getPageListCatalogH0/1/50',
        method: 'post',
        data: param
      }).then(res => {
        let data = res.data.result_data;
        this.messages = data.list;
      });
    },
    changeDeviceInfoDialog(value) {
      this.deviceInfoDialog = value;
    },
    openDeviceInfoDialog (code) {
      this.deviceCode = code;
      this.deviceInfoDialog = true;
    },
  },
  mounted: function () {
    this.getDeviceMessage();
    setInterval(()=>{
      console.log(formatDates(new Date(),'yyyy-MM-dd hh:mm'));
      this.currentTime = formatDates(new Date(),'yyyy-MM-dd hh:mm');
    },1000);
  }
};
</script>

<style scoped>
.el-card>>>.el-card__header {
  padding: 5px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.box-card {
  width: 100%;
  background-color: #f0f2f5;
}

/* message style */
.message-list {
  height: calc(100vh - 420px);
  overflow:auto;
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
}

@keyframes slide-down {
  0% {
    transform: translateY(-100%);
  }
  100% {
    transform: translateY(0);
  }
}
</style>