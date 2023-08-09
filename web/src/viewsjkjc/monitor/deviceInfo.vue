<template>
  <div class="app-container">  
    <el-form :model="deviceInfoForm" ref="form" label-width="120px">

      <el-row>
        <el-col :span="12">
          <el-form-item label="设备编号" prop="code">
            <el-input  v-model="deviceInfoForm.code"></el-input> 
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仪器型号" prop="model">
            <el-input v-model="deviceInfoForm.model" ></el-input>
          </el-form-item>
        </el-col>  
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="传感器类型">  
              <el-input v-model="deviceInfoForm.deviceType" ></el-input>
          </el-form-item>
        </el-col>  
        <el-col :span="12">
          <el-form-item label="监测类型">  
            <el-input v-model="deviceInfoForm.type" ></el-input>
          </el-form-item>
        </el-col>  
      </el-row>

      <el-row>
          <el-col :span="12">
            <el-form-item label="监测区段"> 
                <el-input v-model="deviceInfoForm.segment" ></el-input>
            </el-form-item> 
          </el-col> 
          <el-col :span="12">
            <el-form-item label="桩号">   
                <el-input v-model="deviceInfoForm.stakeNumber" ></el-input>
            </el-form-item> 
          </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="安装位置" prop="warning_data">
              <el-input  v-model="deviceInfoForm.installPosition"></el-input>
          </el-form-item>
        </el-col> 
        <el-col :span="12">
          <el-form-item label="量程" prop="measureRange">
              <el-input v-model="deviceInfoForm.measureRange"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
 
      <el-row>
        
        <el-col :span="12"> 
          <el-form-item label="供电" prop="electricitySupply">
              <el-input v-model="deviceInfoForm.electricitySupply"></el-input>
          </el-form-item>
        </el-col> 
      </el-row> 

    </el-form> 
  </div>
</template>

<script>
/* eslint-disable */
  export default {
    props:['deviceInfoForm'],
    data() {
      return {
        
      }
    },
    methods: {
      save() { 
            
      }
    },
    mounted: function () {
        console.log(this.deviceInfoForm);
    }
  }
</script>