<template>
  <div>
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
        <el-button type="success" size="mini" @click="wander">隧道漫游</el-button>
        <el-button type="warning" size="mini" @click="closeWander">停止漫游</el-button>
        <!-- <el-button type="success" size="mini" @click="print">输出</el-button> -->
      </p>
      </p>
    </div>

    <div id="label" class="label" :style="">
      <div style="margin-top:20px;margin-left:10px;color:white;margin-right:10px;padding:5px 5px;line-height:32px;">
        
        <el-row>
          <el-col :span="8">设备编号</el-col>
          <el-col :span="12">JAAS</el-col>
          <el-col :span="4"><el-button type="" size="mini" @click="closeDialog">关闭</el-button></el-col>
        </el-row>
        <el-row>
          <el-col :span="8">监测项目</el-col>
          <el-col :span="12">管片应力值</el-col>
        </el-row>
        <el-row>
          <el-col :span="8">最新读数</el-col>
          <el-col :span="8">5.5</el-col>
          <el-col :span="8">预警值：8</el-col>
        </el-row>
        <el-row>
          <el-col :span="8">安装位置</el-col>
          <el-col :span="12">拱顶处</el-col>
        </el-row>
        <p>
          <el-button type="primary" size="mini">查看历史数据</el-button>
          <el-button type="primary" size="mini">查看预警记录</el-button>
        </p>
        
      </div>
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
      object:'',
      objects: [],
      biaozhudiv: '',
      img: '',
      biaozhuLabel: '',
      color: {
        red: 0xFF0033,
        orange: 0xedc01c,
        green: 0x03fc52,
        grey: 0xcccccc
      },
      deviceData: [],
      deviceForm: {},
      dialogVisible: false,
      canWander:false

    };
  },
  methods: {
    init() {
      let _this = this;
      this.scene = new THREE.Scene();
      this.scene.add(new THREE.AmbientLight(0xcccccc, 0.4));//环境光 0x999999
      let light = new THREE.DirectionalLight(0xdfebff, 0.45);//从正上方（不是位置）照射过来的平行光，0.45的强度
      light.position.set(30, 200, 100);
      light.position.multiplyScalar(0.3);
      this.scene.add(light);
      //初始化相机
      let perspective = [this.fov, window.innerWidth / window.innerHeight, 1, 1000];
      this.camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 5000);
      this.camera.position.set(-80, 70, 250);
      this.camera.position.set(151, 179, 136);  // 辅助线较好
      this.camera.position.set(162, 23, 48);
      this.camera.lookAt(this.scene.position);
      //初始化控制器
      this.controls = new OrbitControls(this.camera);
      this.controls.target.set(0, 0, 0);
      // this.controls.target.set(5.7, 1.8, 9.4);
      // this.controls.enableZoom = false;
      // this.controls.minDistance = 80;
      this.controls.maxDistance = 400;
      // this.controls.maxPolarAngle = Math.PI / 3;
      this.controls.update();
      //渲染
      this.renderer = new THREE.WebGLRenderer({
        alpha: true,
      });//会在body里面生成一个canvas标签,
      
      const container = document.getElementById('container2');
      this.renderer.setPixelRatio(window.devicePixelRatio);//为了兼容高清屏幕
      // this.renderer.setSize(window.innerWidth, window.innerHeight);
      this.renderer.setSize(container.offsetWidth, container.offsetHeight);
      this.renderer.setClearColor(0xb6d3f7, 0.8);
      

      // 辅助坐标系  参数250表示坐标系大小，可以根据场景大小去设置
      // 红色x轴，绿色y轴，蓝色z轴，
      let axesHelper = new THREE.AxesHelper(50);
      axesHelper.position.x = 5
      this.scene.add(axesHelper);

      let cameraHelper = new THREE.CameraHelper(this.camera);
      this.scene.add(cameraHelper);
      
      let sphere = new THREE.SphereGeometry();
      let object = new THREE.Mesh( sphere, new THREE.MeshBasicMaterial( 0xff0000 ) );
      let box = new THREE.BoxHelper( object, 0xffff00 );
      this.scene.add( box );
      
      let gridHelper = new THREE.GridHelper( 30, 30 );
      this.scene.add( gridHelper );
      // let polarGridHelper = new THREE.PolarGridHelper( 10, 16, 8, 64 );
      // this.scene.add( polarGridHelper );
      let pointLightHelper = new THREE.PointLightHelper( light, 1 );
      this.scene.add( pointLightHelper );
      // let box3 = new THREE.Box3();
      // box3.setFromCenterAndSize( new THREE.Vector3( 1, 1, 1 ), new THREE.Vector3( 2, 1, 3 ) );
      // let helper = new THREE.Box3Helper( box3, 0xffff00 );
      // this.scene.add( helper );
      // let arrowHelper = new THREE.ArrowHelper(new THREE.Vector3( 1, 2, 0 ), new THREE.Vector3( 0, 0, 0 ), 1, 0xffff00);
      // this.scene.add(arrowHelper);
      // let gridHelper = new THREE.GridHelper(this.camera);
      // this.scene.add(gridHelper);

      new MTLLoader(  )
        .setPath( '/static/models/' )
        .load( 'tunnel_1.mtl', function ( materials ) {
          materials.preload();
          new OBJLoader(  )
            .setMaterials( materials )
            .setPath( '/static/models/' )
            .load( 'tunnel_1.obj', function ( object ) {
              object.position.x = 30;
              object.position.y = -20;
              object.position.z = -30;
              // object.translateX(30);
              // object.translateY(-20);
              // object.translateZ(-30);
              object.rotateZ(Math.PI/2);
              object.rotateY(Math.PI/2);
              _this.object = object;
              _this.scene.add( _this.object );
            }, _this.onProgress, _this.onError );

        });
      
      container.appendChild(this.renderer.domElement);
      this.renderer.domElement.style.width = '100%';
      this.renderer.domElement.style.height = '100%';
      //
      const interaction = new Interaction(this.renderer, this.scene, this.camera);
      //
      document.addEventListener('click',this.ray);// 监听窗口鼠标单击事件
      document.addEventListener('mousemove',this.cursor);// 监听窗口鼠标单击事件

    },
    onProgress(xhr) {
        if ( xhr.lengthComputable ) {
          var percentComplete = xhr.loaded / xhr.total * 100;
          console.log( Math.round( percentComplete, 2 ) + '% downloaded' );
        }
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
      // new a interaction, then you can add interaction-event with your free style
      // const interaction = new Interaction(this.renderer, this.scene, this.camera);
      for(let i in this.deviceData) {
        let item = this.deviceData[i];
        let status_color;
        if(item.run_status == '1') status_color = 'green';
        if(item.run_status == '0') status_color = 'red';
        // let geometry = new THREE.SphereGeometry(6, 4, 4); //创建一个几何对象Geometry
        let geometry = new THREE.BoxGeometry(2, 2, 2); //创建一个几何对象Geometry
        let material = new THREE.MeshLambertMaterial({
          color: this.color[status_color]
        }); //材质对象Material
        let mesh = new THREE.Mesh(geometry, material); //网格模型对象Mesh
        mesh.position.set(item.x,item.y,item.z);
        // 添加文字标签
        // this.addSprite(item.x,item.y,item.z,'/static/images/device-d010103-nomal.png',0,'tehfaksjhdfawsst',mesh,function(){});
        // mesh.add(this.biaozhuLabel);
        this.scene.add(mesh); //网格模型添加到场景中
        //
        mesh.info = item;
        this.objects.push(mesh);
        // 
        // var text = document.getElementById('label').cloneNode(true);
        // text.style.visibility = "visible";
        // text.className = "label";
        // // text.childNodes[1].childNodes[1].textContent = item.name;
        // let label = new CSS2DObject(text);
        // label.position.copy(mesh.position);
        // mesh.add(label);

        // let labelRender = new CSS2DRenderer();
        // labelRender.setSize(window.innerWidth, window.innerHeight);
        // labelRender.domElement.style.position = 'absolute';
        // labelRender.domElement.style.top = 0;
        // console.log(labelRender.domElement);
        // document.body.appendChild(labelRender.domElement);

        // // 
        // mesh.cursor = 'pointer';
        // mesh.on('click', function() {
        //   _this.showDevice(item);
        // });
      }
      
    },
    loadDevice() {
      let _this = this;
      const interaction = new Interaction(this.renderer, this.scene, this.camera);
      this.getDeviceData();
      for(let i in this.deviceData) {
        let item = this.deviceData[i];
        new MTLLoader().setPath('/static/models/LOHO/').load('LOHO.mtl', materials => {
          materials.preload();
          new OBJLoader().setMaterials(materials).setPath('/static/models/LOHO/').load('LOHO.obj', obj => {
            obj.scale.set(1, 1, 1);
            obj.position.set(item.x,item.y,item.z);
            obj.cursor = 'pointer';
            obj.on('click', function(obj) {
              _this.showDevice(item);
            });
            // _this.addSprite(-90, -50, -55, '/static/image/sensor.png', -95, '传感器节点', obj, () => {
            //   // this.alarmDetail();
            // });
            _this.scene.add(obj);
            obj.info = item;
            this.objects.push(obj);
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
        {name:'测缝计0021',type:'应变计',serailno:'JC010293',x:10,y:0,z:10,run_status:'0',realtime:'4',time:'2020-03-21 12:00:00'},
        {name:'地震动0010',type:'地震动',serailno:'JC012224',x:50,y:0,z:10,run_status:'1',realtime:'4',time:'2020-03-21 12:00:00'},
      ];
    },
    modelReset() {
      // this.camera.position.set(-80, 70, 250);
      this.object.position.x = 30;
      this.object.position.y = -20;
      this.object.position.z = -30;
      this.camera.position.set(162, 23, 48);
      this.camera.lookAt(this.scene.position);
    },
    wander() {
      let _this = this;
      console.log(this.camera.position);
      console.log(this.scene);
      console.log(this.controls);
      this.camera.position.set(92, -2, 11);
      this.canWander = true;
      let x = 91, y = -2, z = 11;
      setInterval(function(){
        if(_this.canWander) {
          x = x - 1;
          y = y + 0.1;
          _this.camera.position.set(x, y, z);
          console.log(_this.camera.position);
        }
        
      },2000);
    },
    closeWander() {
      this.canWander = false;
    },
    print() {
      console.log(this.camera.position);
    },
    /**
     * 射线拾取函数
     * 选中的网格模型变为半透明效果
     */
    ray(event) {
      var Sx = event.clientX;//鼠标单击位置横坐标
      var Sy = event.clientY;//鼠标单击位置纵坐标
      //屏幕坐标转标准设备坐标
      var x = ( Sx / window.innerWidth ) * 2 - 1;//标准设备横坐标
      var y = -( Sy / window.innerHeight ) * 2 + 1;//标准设备纵坐标
      var standardVector  = new THREE.Vector3(x, y, 0.5);//标准设备坐标
      //标准设备坐标转世界坐标
      var worldVector = standardVector.unproject(this.camera);
      //射线投射方向单位向量(worldVector坐标减相机位置坐标)
      var myray = worldVector.sub(this.camera.position).normalize();
      //创建射线投射器对象
      var raycaster = new THREE.Raycaster(this.camera.position, myray);
      //返回射线选中的对象
      var intersects = raycaster.intersectObjects(this.objects, true);
      // var intersects = raycaster.intersectObjects([boxMesh,sphereMesh,cylinderMesh]);
      // var intersects = raycaster.intersectObjects(objects);
      console.log(intersects.length);
      if (intersects.length > 0) {
          // intersects[0].object.material.transparent = true;
          // intersects[0].object.material.opacity = 0.6;
          this.showDevice(intersects[0].object.info);
      }
    },
    cursor() {
      var Sx = event.clientX;//鼠标单击位置横坐标
      var Sy = event.clientY;//鼠标单击位置纵坐标
      //屏幕坐标转标准设备坐标
      var x = ( Sx / window.innerWidth ) * 2 - 1;//标准设备横坐标
      var y = -( Sy / window.innerHeight ) * 2 + 1;//标准设备纵坐标
      var standardVector  = new THREE.Vector3(x, y, 0.5);//标准设备坐标
      //标准设备坐标转世界坐标
      var worldVector = standardVector.unproject(this.camera);
      //射线投射方向单位向量(worldVector坐标减相机位置坐标)
      var myray = worldVector.sub(this.camera.position).normalize();
      //创建射线投射器对象
      var raycaster = new THREE.Raycaster(this.camera.position, myray);
      //返回射线选中的对象
      var intersects = raycaster.intersectObjects(this.objects, true);
      // var intersects = raycaster.intersectObjects([boxMesh,sphereMesh,cylinderMesh]);
      // var intersects = raycaster.intersectObjects(objects);
      // document.body.style.cursor = 'pointer';
      if (intersects.length > 0) {
        console.log(intersects[0].object.info.name);
        document.body.style.cursor = 'pointer';
      }else{
        document.body.style.cursor = 'default';
      }
    },
    closeDialog() {
      console.log('close dialog');
    }
    

  },
  mounted() {
    this.init();
    this.animate();
    this.addDevice();
    // this.loadDevice();
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
  /* display: none; */
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
  /* display: none; */
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

.label {
  display: none;
  background:url('/static/images/3d_label.png') center no-repeat;;
  background-size: 430px 280px;
  width:400px;
  height:250px;
  top:100px;
  left:230px;
  position:absolute;
  z-index:999;
  /* padding: 5px 5px; */
  border:transparent;
}

#label_top div{
  display: inline-block;
}

#label_middle div{
  display: inline-block;
}

#label_bottom div{
  display: inline-block;
}


</style>