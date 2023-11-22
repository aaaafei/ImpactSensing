<template>
  <div>
    <div>
      <div class="up-map-div-top" style="width:80%;min-height:60px">
        <div style="display: inline;margin-right: 2px;" v-for="(item, index) in carouselList" :key="index">
          <el-button type="primary" size="mini" plain @click="locateCompay(index)" v-if="index == activeIndex">{{
            item.name }}</el-button>
          <el-button type="info" size="mini" plain @click="locateCompay(index)" v-else>{{ item.name }}</el-button>
        </div>
      </div>

      <div class="up-map-div-left">
        <div style="text-align: center;font-size: 20px;font-weight: bold;">{{ currentPosition.title }}</div>
        <div style="text-indent: 32px;">{{ currentPosition.note }}</div>
      </div>

      <div class="up-map-div-bottom">
        <div style="display: inline-block;float: left;padding-left: 5px;padding-right: 2px;">
          <span style="color: #1684FC;"><br>现<br>场<br>图<br>片</span>
        </div>
        <ul class="img-container">
          <li class="img-item" v-for="(item,index) in images" :key="index">
            <img :src=item.filepath alt="" style="height: 145px;" @click="previewImage(item.filepath)">
          </li>
        </ul>
      </div>

      <baidu-map id="bmap" class="map" :scroll-wheel-zoom="true" :center="center" :zoom="zoom" @ready="handler">
      </baidu-map>

      <device-info :deviceInfoDialog="deviceInfoDialog" @changeDeviceInfoDialog="changeDeviceInfoDialog"></device-info>

      <el-dialog title="" :visible.sync="imgDialogVisible" width="50%">
        <div style="height:600px;text-align: center;">
          <img :src="previewImg" alt="" style="height: 100%;">
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
/* eslint-disable */
import DeviceInfo from './DeviceInfo';
export default {
  components: {
    DeviceInfo,
  },
  data: function () {
    return {
      map: null,
      bmap: null,
      deviceInfoDialog: false,
      markerList: [],//风险源点位对象集合
      carouselList: [{
        id: 34,
        jd: 118.90185,
        wd: 32.055767,
        zoom: 20,
        name: '马群站'
      }, {
        id: 61,
        jd: 118.820464,
        wd: 32.117678,
        zoom: 18,
        name: '迈晓区间'
      }],
      center: { lng: 0, lat: 0 },
      zoom: null,
      enableCarousel: true,
      activeIndex: 0,
      currentPosition: { title: "", note: "" },
      currentPics: [],
      imgDialogVisible: false,
      images: [],
      previewImg: '',
    }
  },
  mounted() {
    setTimeout(() => {
      this.carouselCompany();
      this.showDeviceOnMap();
      this.getPosInfo();
      this.getImages();
    }, 1500);

  },
  watch: {
    activeIndex(newv, oldv) {
      this.getPosInfo();
    },
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
      let interval = 1000 * 8;
      let count = 0;
      setInterval((e) => {
        if (!this.enableCarousel) return null;
        if (this.carouselList.length == 0) return null;
        this.activeIndex = count % this.carouselList.length;
        let selItem = this.carouselList[this.activeIndex];
        this.map.panTo(new BMap.Point(selItem.jd, selItem.wd), 1500);
        this.map.setZoom(selItem.zoom);

        count += 1;
        // console.log(this.activeIndex, selItem);
      }, interval);

    },
    locateCompay(index) {
      this.enableCarousel = false;
      this.activeIndex = index;
      let selItem = this.carouselList[this.activeIndex];
      this.map.panTo(new BMap.Point(selItem.jd, selItem.wd), 1500);
      this.map.setZoom(selItem.zoom);
    },
    showDeviceOnMap() {
      this.$request({
        url: '/riskSource/getRiskSourcePageData/1/1000',
        method: 'POST',
        data: {
          pos_id: ""
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
            let iconUrl = "";
            if (stationObj.status == "1") {
              iconUrl = this.$STATIC_PREFIX+'images/demo/dun-green.png';
            } else {
              iconUrl = this.$STATIC_PREFIX+'images/demo/dun-red.png';
            }
            var myIcon = new BMap.Icon(iconUrl, new BMap.Size(50, 50));
            // var myIcon = new BMapGL.Icon(iconUrl, new BMapGL.Size(48, 48));
            //标记点位
            // var marker = new BMap.Marker(new BMap.Point(stationObj.longitude, stationObj.latitude));
            var marker = new BMap.Marker(new BMap.Point(stationObj.longitude, stationObj.latitude), { icon: myIcon });
            var label = new BMap.Label("<font color='#0F0F0F'>" + stationObj.code + ":" + stationObj.battery + "</font>", { offset: new BMap.Size(-10, -20) });
            label.setStyle({ "border-color": "#ffffff" });
            marker.setLabel(label);
            marker.addEventListener('click', (e) => {
              console.log(stationObj);
              this.deviceInfoDialog = true;
            });
            this.map.addOverlay(marker);
          }
        }
      });
    },
    changeDeviceInfoDialog(value) {
      this.deviceInfoDialog = value;
    },
    getPosInfo() {
      let currentPosId = this.carouselList[this.activeIndex].id;
      this.$request({
        url: '/empty',
        method: 'post',
        data: {}
      }).then(res => {
        let data = res.data;
        if (currentPosId == 61) {
          this.currentPosition.title = "迈晓区间";
          this.currentPosition.note = "待补充";
        } else {
          this.currentPosition.title = "马群站";
          this.currentPosition.note = "马群站是南京地铁2号线和南京地铁S6号线的换乘车站，车站位于南京市栖霞区中山门大街与马群新街交叉路口左侧。南京地铁2号线为高架三层侧式车站，南京地铁S6号线为地下二层岛式车站，可与南京麒麟有轨电车站外换乘。";
        }
      });
    },
    getImages() {
      this.images = [
        {"filepath":this.$STATIC_PREFIX+"images/demo/xc001.png"},
        {"filepath":this.$STATIC_PREFIX+"images/demo/xc002.png"},
        {"filepath":this.$STATIC_PREFIX+"images/demo/xc003.png"},
        {"filepath":this.$STATIC_PREFIX+"images/demo/xc004.png"},
        {"filepath":this.$STATIC_PREFIX+"images/demo/xc001.png"},
        {"filepath":this.$STATIC_PREFIX+"images/demo/xc003.png"},
        {"filepath":this.$STATIC_PREFIX+"images/demo/xc002.png"},
        {"filepath":this.$STATIC_PREFIX+"images/demo/xc004.png"},
      ]
    },
    previewImage(value) {
      this.previewImg = value;
      this.imgDialogVisible = true;
    },
  }
}

</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.map {
  margin: 1px;
  height: calc(100vh - 200px);
}


.up-map-div-top {
  top: 10px;
  left: 60px;
  position: absolute;
  z-index: 2000;
  background-color: transparent;
  opacity: 1;
  display: block;
}

.up-map-div-left {
  top: 60px;
  left: 60px;
  height: 200px;
  width: 15%;
  position: absolute;
  z-index: 2000;
  background-color: white;
  border-block-color: white;
  opacity: 0.8;
  display: block;
}

.up-map-div-bottom {
  bottom: 5px;
  left: 15px;
  height: 150px;
  width: 73%;
  position: absolute;
  z-index: 2000;
  background-color: white;
  border-block-color: white;
  opacity: 0.9;
  display: block;

}

.img-container {
  width: 94%;
  height: 145px;
  overflow-x: auto;
  overflow-y: hidden;
  white-space: nowrap;
  display: inline-block;
  padding-left: 5px;
  padding-right: 15px;
  margin-top: 5px;
  margin-bottom: 1px;
}

.img-item {
  display: inline-block;
  cursor: pointer;
  padding: 2px;
}
</style>