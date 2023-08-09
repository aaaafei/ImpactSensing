<template>
    <baidu-map id="bmap" class="mapDemo"
               :scroll-wheel-zoom="true"
               :center="center"
               :zoom="zoom"
               @ready="handler">
    </baidu-map>
</template>

<script>
/* eslint-disable */ 
    export default {
      name: 'bmap',
      props:['tableData'],
      data () {
        return {
          center: {lng: 0, lat: 0},
          zoom: null,
          map:null,
          bmap:null,
          riskLevelImg:{'B':'risklevel_b.png','C':'risklevel_c.png','D':'risklevel_d.png','E':'risklevel_e.png'}
        };
      },
      methods: {
        handler ({BMap, map}) {  
          this.map = map;
          this.bmap = BMap;

          this.zoom = 12;
          map.addControl(new BMap.NavigationControl());
          //右上角：地图\卫星 切换
          map.addControl(new BMap.MapTypeControl({mapTypes: [ BMAP_NORMAL_MAP, BMAP_SATELLITE_MAP ]}));
  
          for(var i= 0;i<this.tableData.length;i++){
            if(this.tableData[i].longitude!=undefined && this.tableData[i].latitude!=undefined){
              var point = new BMap.Point(this.tableData[i].longitude,this.tableData[i].latitude);
              var iconPath = this.$STATIC_PREFIX + '/images/' + this.riskLevelImg[this.tableData[i].level];
              var marker = new BMap.Marker(point, {icon: new BMap.Icon(iconPath, new BMap.Size(110, 92))});
              this.map.addOverlay(marker);
              this.map.centerAndZoom(point, 15);
            }
          }
        } 
      },
      watch:{
        tableData:{
          handler(newvalue,oldvalue){ 

            //init clear
            this.map.clearOverlays();

            for(var i= 0;i<this.tableData.length;i++){
              if(this.tableData[i].longitude!=undefined && this.tableData[i].latitude!=undefined){ 
                var point = new BMap.Point(this.tableData[i].longitude,this.tableData[i].latitude);
                var iconPath = this.$STATIC_PREFIX + '/images/' + this.riskLevelImg[this.tableData[i].level];
                var marker = new this.bmap.Marker(point, {icon: new this.bmap.Icon(iconPath, new this.bmap.Size(110, 92))});
                this.map.addOverlay(marker);
              }

            }

          }
        }
      }
    };
</script>

<style>
  .mapDemo {
    width: 100%;
    height: 680px;
  }
  .anchorBL{
    display:none;
  }
</style>