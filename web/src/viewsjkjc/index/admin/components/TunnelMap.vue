<template>
  <div>
    <div class="box-card2-div">
      <div class="up-map-div-top" style="width:80%;min-height:60px">
        <div style="display: inline;margin-right: 2px;" v-for="(item, index) in carouselList" :key="index">
          <el-button type="primary" size="mini" plain @click="locateCompay(index)" v-if="index == activeIndex">{{ item.name }}</el-button>
          <el-button type="info" size="mini" plain  @click="locateCompay(index)" v-else>{{ item.name }}</el-button>
        </div>
      </div>
      
      <baidu-map id="bmap" class="box-card2-div" :scroll-wheel-zoom="true" :center="center" :zoom="zoom" @ready="handler">
      </baidu-map>
    </div>
  </div>
</template>

<script>
/* eslint-disable */
export default {
  data: function () {
    return {
      map: null,
      bmap: null,
      markerList: [],//风险源点位对象集合
      markerLevelMap: { level_b: [], level_c: [], level_d: [] },
      yhMarkerList: [],//隐患点位对象集合
      fxyDialogVisible: false,//风险源点位详情弹窗显示开关        
      fxyStatObj: {},//当前选中的风险源点位信息对象
      yhDialogVisible: false,//隐患点位详情弹窗显示开关
      yhStatObj: {},//当前选中的隐患点位信息对象
      levelDict: [],
      pdMap: {},//按人员统计巡查进度数据集
      riskSourceCheckBox: { level_b: true, level_c: true, level_d: true },
      principalSelectData: [],
      userInfo: {},
      carouselList: [{
        orgID: 34,
        jd: 118.870607,
        wd: 32.147963,
        zoom:17,
        name: '南京新港粮油食品市场'
      }, {
        orgID: 61,
        jd: 118.953854,
        wd: 32.171023,
        zoom:17,
        name: '南京石埠桥粮食储备库有限公司'
      }, {
        orgID: 74,
        jd: 118.88173,
        wd: 32.344272,
        zoom:17,
        name: '南京灵山粮食储备库有限公司'
      }, {
        orgID: 76,
        jd: 119.088441,
        wd: 32.218902,
        zoom:17,
        name: '南京现代粮食物流有限公司'
      }],
      center: { lng: 0, lat: 0 },
      zoom: null,
      hasGroupUser: '1',//是否集团用户 1:是 2:否
      iconList: [
        this.$STATIC_PREFIX + '/images/icon/t1.png',
        this.$STATIC_PREFIX + '/images/icon/fa-life-ring.png',
        this.$STATIC_PREFIX + '/images/icon/fa-check-square.png',
        this.$STATIC_PREFIX + '/images/icon/fa-tasks.png',//'/images/icon/fa-medium.png',
        this.$STATIC_PREFIX + '/images/icon/fa-book.png',
        this.$STATIC_PREFIX + '/images/icon/fa-users.png',
        this.$STATIC_PREFIX + '/images/icon/fa-file-alt.png',
        this.$STATIC_PREFIX + '/images/icon/fa-ambulance.png',
        this.$STATIC_PREFIX + '/images/icon/fa-bullhorn.png',
        this.$STATIC_PREFIX + '/images/icon/fa-tasks.png',
        this.$STATIC_PREFIX + '/images/icon/fa-chart-pie.png',
        this.$STATIC_PREFIX + '/images/icon/fa-chart-area.png',//'/images/icon/fa-comments.png',
        this.$STATIC_PREFIX + '/images/icon/fa-chart-area.png',
        this.$STATIC_PREFIX + '/images/icon/risk_b.png',
        this.$STATIC_PREFIX + '/images/icon/risk_c.png',
        this.$STATIC_PREFIX + '/images/icon/risk_d.png',
        this.$STATIC_PREFIX + '/images/icon/yh_red.png',
        this.$STATIC_PREFIX + '/images/icon/yh_yellow.png',
        this.$STATIC_PREFIX + '/images/icon/an_quan_guan_li.png',
        this.$STATIC_PREFIX + '/images/icon/dai_ban_shi_xiang.png',
        this.$STATIC_PREFIX + '/images/icon/tong_zhi_gong_gao.png',
        this.$STATIC_PREFIX + '/images/icon/gong_zuo_jian_bao.png',
        this.$STATIC_PREFIX + '/images/icon/feng_xian_yuan_guan_kong.png',
        this.$STATIC_PREFIX + '/images/icon/yin_huan_pai_cha.png',
      ],
      enableCarousel:true,
      activeIndex:0,
    }
  },
  mounted() {
    this.carouselCompany();
  },
  methods: {
    handler({ BMap, map }) {
      this.map = map;
      this.bmap = BMap;
      map.addControl(new BMap.NavigationControl());
      let stCtrl = new BMap.PanoramaControl(); //构造全景控件
      stCtrl.setOffset(new BMap.Size(40, 40));
      this.map.addControl(stCtrl);//添加全景控件
      //右上角：地图\卫星 切换
      this.map.addControl(new BMap.MapTypeControl({ mapTypes: [BMAP_NORMAL_MAP, BMAP_SATELLITE_MAP] }));
      let selItem = this.carouselList[this.activeIndex];
      this.map.centerAndZoom(new BMap.Point(selItem.jd, selItem.wd), selItem.zoom);
      this.map.setMapStyle({ style: 'googlelite' });
      this.map.setMapType(BMAP_SATELLITE_MAP);
      //   this.initStatPoint();
    },
    carouselCompany() {
      let interval = 1000*8;
      let count = 0;
      setInterval((e)=>{
        if(!this.enableCarousel) return null;
        if(this.carouselList.length == 0) return null;
        this.activeIndex = count % this.carouselList.length;
        let selItem = this.carouselList[this.activeIndex];
        // this.showRiskSourceOnMap(selItem.orgID);
        this.map.panTo(new BMap.Point(selItem.jd, selItem.wd), 1500);
        this.map.setZoom(selItem.zoom);
        
        count += 1;
        // console.log(this.activeIndex, selItem);
      },interval);

    },
    locateCompay(index) {
      this.enableCarousel = false;
      this.activeIndex = index;
      let selItem = this.carouselList[this.activeIndex];
      this.map.panTo(new BMap.Point(selItem.jd, selItem.wd), 1500);
      this.map.setZoom(selItem.zoom);
      // this.showRiskSourceOnMap(selItem.orgID);
    },
    showRiskSourceOnMap(org_id){
      this.$request({
        url: '/riskSource/getRiskSourcePageData/1/1000',
        method: 'POST',
        data: {
          org_id: org_id
        }
      }).then(res => {
        // console.log("返回的点位数据",res);
        if (res && res.data && res.data.result_data && res.data.result_data.list) {
          //先清除已有的点
          for (var i = 0; i < this.markerList.length; i++) {
            this.map.removeOverlay(this.markerList[i]);
          }
          this.markerList = [];
          for (var i = 0; i < res.data.result_data.list.length; i++) {
            var stationObj = res.data.result_data.list[i];
            //console.log("点位信息",stationObj);
            var iconUrl = '';
            if (stationObj.level == 'D' || stationObj.level == 'd') {
              iconUrl = this.iconList[15];
            } else if (stationObj.level == 'C' || stationObj.level == 'c') {
              iconUrl = this.iconList[14];
            } else if (stationObj.level == 'B' || stationObj.level == 'b') {
              iconUrl = this.iconList[13];
            }
            var myIcon = new BMap.Icon(iconUrl, new BMap.Size(21, 32));
            //myIcon.setAnchor(new BMap.Size(10,32));
            //标记点位
            var marker = new BMap.Marker(new BMap.Point(stationObj.longitude, stationObj.latitude), { icon: myIcon });

            var label = new BMap.Label("<font color='#0F0F0F'>" + stationObj.name + "</font>", { offset: new BMap.Size(20, -10) });
            label.setStyle({ "border-color": "#ffffff" });
            // marker.setLabel(label);
            this.map.addOverlay(marker);
            // this.markerList.push(marker);
            //
            if (stationObj.level == 'D' || stationObj.level == 'd') {
              this.markerLevelMap.level_d.push(marker);
            } else if (stationObj.level == 'C' || stationObj.level == 'c') {
              this.markerLevelMap.level_c.push(marker);
            } else if (stationObj.level == 'B' || stationObj.level == 'b') {
              this.markerLevelMap.level_b.push(marker);
            }

            // this.addEvent(marker, stationObj);
          }
        }
      });
    },
    
    
   
  }
}

</script>

<style scoped>
.box-card2 {
  margin: 1px;
  height: calc((100vh - 140px) * 2 / 3 + 13px);
}

.box-card2-div {
  margin: 1px;
  height: calc(100vh - 200px);
}


.up-map-div-top {
  top:10px;
  left: 60px;
  position: absolute;
  z-index: 2000;
  background-color: transparent;
  opacity: 1;
  display: block;
}
</style>