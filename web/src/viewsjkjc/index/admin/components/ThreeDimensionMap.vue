<template>
  <div v-on:mouseover="enableZoom()" v-on:mouseout="disableZoom()">
    <div id="container2">
    <div id="up-map-div2">
      <p><span>监测类型：</span>{{deviceForm.type}}</p>
      <p><span>最新读数：</span>{{deviceForm.realtime}}</p>
      <p><span>采集时间：</span>{{deviceForm.time}}</p>
      <p>
        <el-button type="primary" size="mini">查看历史数据</el-button>
        <el-button type="primary" size="mini">查看预警记录</el-button>
      </p>
    </div>
    <div id="up-map-div">
      <p>共连接传感器：120只</p>
      
      <div>
        <div style="width:30px;height:15px;background-color:green;display:inline-block;margin-right:5px;"></div><span>运行正常：</span> <span>118支</span> 
      </div>
      <div>
        <div style="width:30px;height:15px;background-color:red;display:inline-block;margin-right:5px;"></div><span>运行故障：</span> <span>2支</span> 
      </div>
      <p><span>旋转：鼠标左键；</span></p>
      <p><span>平移：鼠标右键或键盘←↑↓→；</span></p>
      <p><span>缩放：鼠标滚轮；</span></p>
      <p>
        <el-button type="success" size="mini" @click="modelReset">复位</el-button>
        <el-button type="success" size="mini" @click="modelReset">隧道漫游</el-button>
      </p>
      </p>
    </div>

    <el-dialog
      title="传感器信息"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <el-form :model="deviceForm">
        <el-form-item label="设备名称" label-width="120px">
          <el-input v-model="deviceForm.name" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="设备编号" label-width="120px">
          <el-input v-model="deviceForm.serialno" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="仪器类型" label-width="120px">
          <el-input v-model="deviceForm.type" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="监测项目" label-width="120px">
          <el-input v-model="deviceForm.type" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="安装位置" label-width="120px">
          <el-input v-model="deviceForm.x" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="运行状态" label-width="120px">
          <el-input v-model="deviceForm.status" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="最新读数" label-width="120px">
          <el-input v-model="deviceForm.realtime" :disabled="true"></el-input>
        </el-form-item>
      </el-form>
      <div style="margin-left:40px;">
        <el-button type="primary">查看历史数据</el-button>
        <el-button type="primary">查看预警记录</el-button>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关 闭</el-button>
        <!-- <el-button type="primary" @click="dialogVisible = false">关 闭</el-button> -->
      </span>
    </el-dialog>

    </div>
  </div>
</template>

<script>
/* eslint-disable */
import * as THREE from "three";
import {OBJLoader, MTLLoader} from 'three-obj-mtl-loader';
import {CSS2DRenderer, CSS2DObject} from 'three-css2drender';

const OrbitControls = require('three-orbit-controls')(THREE);
import { Interaction } from 'three.interaction';
export default {
  components: {},
  data() {
    return {
      scene: '',
      camera: '',
      controls: '',
      renderer: '',
      biaozhudiv: '',
      img: '',
      biaozhuLabel: '',
      color: {
        red: '0xFF0033',
        orange: '0xedc01c',
        grren: '0x03fc52',
        grey: '0xcccccc'
      },
      deviceData: [],
      deviceForm: {},
      dialogVisible: false

    };
  },
  methods: {
    init() {
      let _this = this;
      this.scene = new THREE.Scene();
      this.scene.add(new THREE.AmbientLight(0xcccccc, 0.4));//环境光 0x999999
      this.light = new THREE.DirectionalLight(0xdfebff, 0.45);//从正上方（不是位置）照射过来的平行光，0.45的强度
      this.light.position.set(30, 200, 100);
      this.light.position.multiplyScalar(0.3);
      this.scene.add(this.light);
      //初始化相机
      let perspective = [this.fov, window.innerWidth / window.innerHeight, 1, 1000];
      this.camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 5000);
      this.camera.position.set(-80, 70, 250);
      // this.camera.position.set(16, 40, 100);
      this.camera.lookAt(this.scene.position);
      //初始化控制器
      this.controls = new OrbitControls(this.camera);
      this.controls.target.set(0, 0, 0);
      this.controls.enableZoom = false;
      // this.controls.minDistance = 80;
      this.controls.maxDistance = 400;
      // this.controls.maxPolarAngle = Math.PI / 3;
      this.controls.update();
      //渲染
      this.renderer = new THREE.WebGLRenderer({
        alpha: true,
      });//会在body里面生成一个canvas标签,
      // this.renderer = new THREE.CanvasRenderer();//会在body里面生成一个canvas标签,
      
      this.renderer.setPixelRatio(window.devicePixelRatio);//为了兼容高清屏幕
      this.renderer.setSize(window.innerWidth, window.innerHeight);
      this.renderer.setClearColor(0xb6d3f7, 0.8);
      const container = document.getElementById('container2');
      // container.appendChild(this.renderer.domElement);

      // 辅助坐标系  参数250表示坐标系大小，可以根据场景大小去设置
      // 红色x轴，绿色y轴，蓝色z轴，
      let axisHelper = new THREE.AxesHelper(250);
      // this.scene.add(axisHelper);

      new MTLLoader(  )
        .setPath( '/static/models/' )
        .load( 'tunnel_1.mtl', function ( materials ) {
          materials.preload();
          new OBJLoader(  )
            .setMaterials( materials )
            .setPath( '/static/models/' )
            .load( 'tunnel_1.obj', function ( object ) {
              object.position.x = 500;
              object.position.y = -30;
              object.position.z = -40;
              _this.scene.add( object );
            }, _this.onProgress, _this.onError );

        });
      
      console.log(container.innerHTML);  
      container.appendChild(this.renderer.domElement);
      this.renderer.domElement.style.width = '100%';
      this.renderer.domElement.style.height = '100%';
      console.log(this.renderer.domElement);

    },
    onProgress() {

    },
    onError() {

    },
    animate () {
      this.controls.update();
      this.renderer.render( this.scene, this.camera );
      requestAnimationFrame( this.animate );
    },
    addDevice() {
      let _this = this;
      this.getDeviceData();
      for(let i in this.deviceData) {
        let item = this.deviceData[i];
        let color;
        if(item.status == '1') color = 'green';
        if(item.status == '2') color = 'red';
        let geometry = new THREE.BoxGeometry(2, 2, 2); //创建一个立方体几何对象Geometry
        let material = new THREE.MeshLambertMaterial({
          color: '0xFF0033'
        }); //材质对象Material
        let mesh = new THREE.Mesh(geometry, material); //网格模型对象Mesh
        mesh.position.set(item.x,item.y,item.z);
        // 添加文字标签
        this.addSprite(item.x,item.y,item.z,'/static/images/device-d010103-nomal.png',0,'tehfaksjhdfawsst',mesh,function(){});
        mesh.add(this.biaozhuLabel);
        this.scene.add(mesh); //网格模型添加到场景中
        // 
        // new a interaction, then you can add interaction-event with your free style
        const interaction = new Interaction(this.renderer, this.scene, this.camera);
        mesh.cursor = 'pointer';
        mesh.on('click', function() {
          _this.showDevice(item);
        });
      }
      
    },
    loadDevice() {
      let _this = this;
      const interaction = new Interaction(this.renderer, this.scene, this.camera);
      this.getDeviceData();
      for(let i in this.deviceData) {
        let item = this.deviceData[i];
        new MTLLoader().setPath('/static/models/square/').load('zhengfangxing.mtl', materials => {
          materials.preload();
          new OBJLoader().setMaterials(materials).setPath('/static/models/square/').load('zhengfangxing.obj', obj => {
            obj.scale.set(0.8, 0.8, 0.8);
            obj.position.set(item.x,item.y,item.z);
            obj.cursor = 'pointer';
            obj.on('click', function(obj) {
              // console.log(_this.camera.position);
              _this.showDevice(item);
            });
            this.addSprite(-90, -50, -55, '/static/image/sensor.png', -95, '传感器节点', obj, () => {
              // this.alarmDetail();
            });
            this.scene.add(obj)
          })
        });
      }
      
    },
    addSprite(x, y, z, image, loc, text, Mesh, callback) {
      //添加div标签
      this.biaozhudiv = document.createElement('div');
      //添加图标标签
      this.img = document.createElement('img');
      this.img.src = image;
      this.img.style.marginLeft = loc + 'px';
      this.biaozhudiv.className = 'lable';
      //两者的执行顺序
      this.biaozhudiv.textContent = text;
      this.biaozhudiv.appendChild(this.img);
      //标注的样式
      this.biaozhudiv.id = 'biaozhu';
      this.biaozhudiv.style.color = 'rgb(' + 0 + ',' + 0 + ',' + 0 + ')';
      this.biaozhudiv.style.fontSize = 40 + 'px';
      this.biaozhudiv.style.fontFamily = 'Georgia,serif';
      this.biaozhudiv.style.cursor = 'pointer';
      this.biaozhudiv.onclick = function () {
        callback(Mesh);
      };
      this.biaozhuLabel = new CSS2DObject(this.biaozhudiv);
      this.biaozhuLabel.position.set(x, y, z);
      // Mesh.add(this.biaozhuLabel);
      
    },
    showDevice(item){
      // obj.mydata = item;
      this.deviceForm = item;
      // this.dialogVisible = true;
    },
    onWindowResize () {
      this.controls.update();
      this.renderer.render( this.scene, this.camera );
      requestAnimationFrame( this.animate );
    },
    render () {
      this.camera.lookAt( this.scene.position );
      this.renderer.render( this.scene, this.camera );
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    getDeviceData() {
      this.deviceData = [
        {name:'测缝计0021',type:'应变计',serailno:'JC010293',x:15,y:15,z:50,run_status:'0',realtime:'4',time:'2020-03-21 12:00:00'},
        {name:'地震动0010',type:'地震动',serailno:'JC012224',x:15,y:15,z:30,run_status:'0',realtime:'4',time:'2020-03-21 12:00:00'},
      ];
    },
    modelReset() {
      this.camera.position.set(-80, 70, 250);
      // this.camera.position.set(16, 40, 100);
      this.camera.lookAt(this.scene.position);
    },
    enableZoom() {
      console.log('in ');
      this.controls.enableZoom = true;
    },
    disableZoom() {
      console.log('out');
      this.controls.enableZoom = false;
    }

  },
  mounted() {
    this.init();
    this.animate();
    // this.addDevice();
    this.loadDevice();
  }
};
</script>
<style scoped>
p{
  line-height: 10px;;
}
#container2{
  display:block;
  height:800px;
  width:100%;
}
#up-map-div{
  width:300px;
  height:200px;
  top:30px;
  right:30px;
  position:absolute;
  z-index:999;
  padding: 5px 5px;
  border:1px solid white;
  background-color:white;
}

#up-map-div2{
  width:300px;
  height:150px;
  top:30px;
  right:400px;
  position:absolute;
  z-index:999;
  padding: 5px 5px;
  border:1px solid white;
  background-color:white;
}


</style>