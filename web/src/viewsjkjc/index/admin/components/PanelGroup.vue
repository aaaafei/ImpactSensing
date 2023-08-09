<template>
<div>
  <el-row
    :gutter="20"
    class="panel-group"
  >
    <el-col
      :span="8"
      class="card-panel-col"
      v-for="item in segments"
      :key="item.name"
    >
      <div
        class="card-item"
        style=""
      >
        <div>
          <a
            href="javascript:void(0);"
            style="color: #333;font-size: 26px;font-weight: bold;font-family: 微软雅黑;text-decoration:none;background-color:rgb(237 227 218 / 1);"
            @click="pageTo('monitor2',{segmentuuid:item.uuid})"
          >{{item.name}}段</a>
        </div>

        <div style="margin-left: 0px;">
          <ul style="list-style: none;padding:2px 2px;">
            <li
              class="card-item-left"
              style=""
            >
              <span class="ewb-today-sp1 ewb-toady-spe2 l" style="font-family:SimHei;">
                <br>左线安全度
              </span>
              <span class="ewb-today-sp2 l" :style="renderSafetyLabelStyle(item.left_status)">
                <span
                  id="risk_b_num"
                  @click="pageToStatus({status:item.left_status})"
                >{{item.left_status}}</span>
              </span>
              <span class="ewb-today-sp1  ewb-toady-spe2 l" style="font-family:SimHei;">
                <br>设备完好率
              </span>
              <span
                class="ewb-today-sp3 l"
                style="margin-top: 15px;"
                :style="renderDeviceLabelStyle(item.left_device_intact_rate)"
              >
                <span
                  id="risk_b_rate"
                  @click="pageTo('monitor2',{segmentuuid:item.uuid})"
                >{{item.left_device_intact_rate*100}}%</span>
              </span>

            </li>
            <li
              class="card-item-right"
              style=""
            >
              <span class="ewb-today-sp1  ewb-toady-spe2 l" style="font-family:SimHei;">
                <br>右线安全度
              </span>
              <span class="ewb-today-sp2 l" :style="renderSafetyLabelStyle(item.right_status)">
                <span
                  id="risk_b_num"
                  @click="pageToStatus({status:item.right_status})"
                >{{item.right_status}}</span>
              </span>
              <span class="ewb-today-sp1  ewb-toady-spe2 l" style="font-family:SimHei;">
                <br>设备完好率
              </span>
              <span
                class="ewb-today-sp3 l"
                :style="renderDeviceLabelStyle(item.right_device_intact_rate)"
              >
                <span
                  id="risk_b_rate"
                  @click="pageTo('monitor2',{segmentuuid:item.uuid})"
                >{{item.right_device_intact_rate*100}}%</span>
              </span>

            </li>

          </ul>
        </div>
      </div>
    </el-col>

  </el-row>
  <!-- <span>** 最新数据采集时间：{{collect_time | formatTime('yyyy-MM-dd hh:mm:ss')}}； 距离下次采集还有 {{next_collect_countdown}} 秒 </span><br> -->
  <span>** 数值表示监测区间段结构安全程度(A：健康； B：轻微受损； C：中度受损； D：严重受损； E：极端受损)</span> 
</div>
  
</template>

<script>
/* eslint-disable */
export default {
  data() {
    return {
      segments: [],
      collect_time: '',
      next_collect_countdown: 30,
      color_map: {
          A: "#008000",
            B: "#3888fa",
            C: "#FFA500",
            D: '#FF0000',
            E: '#800000'
      }
    };
  },
  
  filters: {
    formatTime: function (date, fmt) {
      var date = new Date(date);
      if (/(y+)/.test(fmt)) {
          fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
      }
      var o = {
          'M+': date.getMonth() + 1,
          'd+': date.getDate(),
          'h+': date.getHours(),
          'm+': date.getMinutes(),
          's+': date.getSeconds()
      };
      for (var k in o) {
          if (new RegExp('('+k+')').test(fmt)) {
              var str = o[k] + '';
              fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : ('00' + str).substr(str.length));
          }
      }
      return fmt;
    }
  },
  methods: {
    renderSafetyLabelStyle(safety_label) {
      return 'color:'+this.color_map[safety_label];
    },
    renderDeviceLabelStyle(device_intact_rate) {
      //100% color A
      if(device_intact_rate == 1){
        return 'color:'+this.color_map['A'];
      }else if(device_intact_rate >= 0.9){
        return 'color:'+this.color_map['C'];
      }else{
        return 'color:'+this.color_map['D'];
      }
      
    },
    getSegments() {
      this.$request({
        url:'safety/getRealtimeData',
        method:'post',
        params: {}
      }).then(res => {
        let datalist = res.data.result_data;
        let seg={}, can_push=false;
        for (let i=0;i<datalist.length;i++) {
          let item = datalist[i];
          this.collect_time = item.collect_time;
          let seg_name = item.segment_name.substr(0,item.segment_name.length-0);
          seg.name = seg_name;
          if(item.line == 'L') {
            seg.left_status = item.safety_label;
            seg.left_device_intact_rate = item.device_intact_rate;
          }
          if(item.line == 'R') {
            seg.right_status = item.safety_label;
            seg.right_device_intact_rate = item.device_intact_rate;
          }
          
          if(seg.hasOwnProperty("left_status") && seg.hasOwnProperty("right_status")) can_push = true;
          if(can_push) {
            this.segments.push(seg);
            seg = {};
            can_push = false;
          }
        }
      });
      
    },
    pageTo(path, query) {
      this.$router.push({ path: path, query: query });
    },
    pageToStatus(query) {
      console.log(query);
      if(query.status != 'A') {
        this.$router.push({ path: 'warning', query: query });
      }else{
        this.$router.push({ path: 'monitor2', query: query });
      } 
    }
  },
  mounted: function() {
    let _this = this;
    this.collect_time = new Date();
    // this.getSegments();
    setInterval(function(){
      _this.next_collect_countdown--;
      if(_this.next_collect_countdown < 1) {
        _this.next_collect_countdown=30;
        _this.collect_time = new Date();
      }
    },1000);
  }
};
</script>

<style scoped>
.panel-group {
  margin-top: 5px;
}
.card-panel-col {
  margin-bottom: 12px;
}

.card-item {
  height: 150px;
  width: 90%;
  border: 1px solid #dfdfdf;
  border-top: 2px solid #cb5c1d;
  margin-bottom: 10px;
  display: inline-block;
  padding: 10px 10px;
}

.card-item-left {
  margin-top: 0px;
  margin-left: 0px;
  width: 160px;
  height: 90px;
  border-radius: 3px;
  background-color: #ede3da;
  float: left !important;
  display: list-item;
  text-align: -webkit-match-parent;
}

.card-item-right {
  margin-top: 0px;
  margin-left: 5px;
  width: 160px;
  height: 90px;
  border-radius: 3px;
  background-color: #ede3da;
  float: left !important;
  display: list-item;
  text-align: -webkit-match-parent;
}

.ewb-today-sp1 {
  margin-top: 10px;
  width: 25px;
  height: 52px;
  line-height: 52px;
  font-size: 16px;
  text-align: center;
}

.ewb-toady-spe2 {
  margin-top: 8px;
  line-height: 19px;
}

.ewb-today-sp2 {
  margin-left: 5px;
  margin-top: 10px;
  padding-top: 4px;
  width: 80px;
  height: 48px;
  font-size: 28px;
  font-family: Arial;
  font-weight: bold;
  text-align: center;
  color: #f67723;
  border-left: 1px solid #c4b5a5;
  cursor: pointer;
}

.ewb-today-sp3 {
  margin-left: 5px;
  margin-top: 15px;
  padding-top: 4px;
  width: 80px;
  height: 48px;
  font-size: 22px;
  font-family: Arial;
  font-weight: bold;
  text-align: center;
  color: #f67723;
  border-left: 1px solid #c4b5a5;
  cursor: pointer;
}

@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }
}
</style>
