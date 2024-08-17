<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="12" :offset="0">
        <div style="float: left;">
          <span style="font-weight:bold;color: #1684FC;">现在时间</span>
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
          <span>最新动态（每分钟刷新）</span>
          <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-refresh" @click="getDeviceMessage()">刷新</el-button>
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
                  <div>
                    <span style="font-size: 14px;font-weight: bold;">{{ message.segment + '-' + message.stakeNumber }}</span>
                    <span style="font-size: 12px;">{{ message.timstamp }}</span>
                    
                  </div>
                    <!-- 状态异常 -->
                    <div style="font-size: 14px;color: red;" v-if="message.catalogval != '1'">{{ '设备运行状态' +
                      (message.catalogval == "1" ? "正常" : "异常") + "，电池余量：" + (message.voltage/450*100).toFixed(0) + "%" }}</div>
                    <!-- 低电量 -->
                    <div style="font-size: 14px;color: red;" v-else-if="(message.voltage/450*100) < 50">{{ '设备运行状态' +
                      (message.catalogval == "1" ? "正常" : "异常") + "，电池余量低：" + (message.voltage/450*100).toFixed(0) + "%" }}</div>
                    <!-- 正常状态 -->
                    <div style="font-size: 14px;" v-else>{{ '设备运行状态' + (message.catalogval == "1" ? "正常" : "异常") + "，电池余量："
                      + (message.voltage/450*100).toFixed(0) + "%" }}</div>
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
    getDeviceMessage() {
      let param = {};
      // param.clientimei = this.deviceCode;
      this.$request({
        url: '/tmOriginData/getPageListCatalogS6/1/50',
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
      this.currentTime = formatDates(new Date(),'yyyy-MM-dd hh:mm:ss');
      if(this.currentTime.split(":")[2] == '00') {
        this.getDeviceMessage()
      }
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