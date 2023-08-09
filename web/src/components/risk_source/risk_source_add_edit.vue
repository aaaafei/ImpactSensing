<template>
  <div>
  <el-form :model="form" :rules="rules" ref="form" label-width="120px">
    <el-row>
      <el-form-item label="风险源名称" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
    </el-row>

    <el-row>
      <el-col :span="12">
      	<el-form-item label="风险源数量" prop="num">
        <el-input-number controls-position="right" v-model="form.num" :step="1" :min="1" ></el-input-number>
			  </el-form-item>
	    </el-col>
      <el-col :span="12"> 
      	<el-form-item label="风险源分类" prop="classify">
	  	 		<el-radio-group v-model="form.classify">
          		<el-radio label="1">业务管辖范围风险源</el-radio>
          		<el-radio label="2">办公区域风险源</el-radio> 
        	</el-radio-group>
			  </el-form-item>
	    </el-col>
    </el-row>

    <el-row>
       <el-col :span="12">
         <el-form-item label="责任单位" prop="org_name">
            <el-input v-model="form.org_name" readonly placeholder="请选择责任单位"> 
                <el-button slot="append" icon="el-icon-search" @click="chooseOrg('org')"></el-button> 
            </el-input>
         </el-form-item>
      </el-col>
	    <el-col :span="12">
        <el-form-item label="业务管理部门" prop="business_org_name">
            <el-input v-model="form.business_org_name" readonly placeholder="请选择业务管理部门"> 
                <el-button slot="append" icon="el-icon-search" @click="chooseOrg('business_org')"></el-button> 
            </el-input>
         </el-form-item>
	    </el-col>
    </el-row>

    <el-row>
       <el-col :span="12">
         <el-form-item label="属地责任部门" prop="apanage_org_name">
            <el-input v-model="form.apanage_org_name" readonly placeholder="请选择属地责任部门"> 
                <el-button slot="append" icon="el-icon-search" @click="chooseOrg('apanage_org')"></el-button> 
            </el-input>
         </el-form-item>
      </el-col>
       <el-col :span="12">
         <el-form-item label="委外单位" prop="outsourcing_org_name">
            <el-input v-model="form.outsourcing_org_name" readonly placeholder="请选择委外单位"> 
                <el-button slot="append" icon="el-icon-search" @click="chooseOrg('outsourcing_org')"></el-button> 
            </el-input>
         </el-form-item>
      </el-col> 
    </el-row>

 
    <el-row>
      <el-col :span="12">
        <el-form-item label="等级" prop="level">
          <el-select v-model="form.level" placeholder="请选择风险源等级">
            <el-option label="A级" value="A"></el-option>
            <el-option label="B级" value="B"></el-option>
            <el-option label="C级" value="C"></el-option>
            <el-option label="D级" value="D"></el-option>
            <el-option label="E级" value="E"></el-option>
          </el-select> 
        </el-form-item> 
      </el-col>
      <el-col :span="12"> 
        <el-form-item label="主要风险类型" prop="type_uuid">
          <el-select v-model="form.type_uuid" multiple placeholder="请选择风险源类型">
            <el-option label="物体打击" value="01"></el-option>
            <el-option label="车辆伤害" value="02"></el-option>
            <el-option label="机械伤害" value="03"></el-option>
            <el-option label="起重伤害" value="04"></el-option>
            <el-option label="触电" value="05"></el-option> 
            <el-option label="淹溺" value="06"></el-option>
            <el-option label="灼烫" value="07"></el-option>
            <el-option label="火灾" value="08"></el-option>
            <el-option label="高处坠落" value="09"></el-option>
            <el-option label="坍塌" value="010"></el-option>
            <el-option label="冒顶片帮" value="011"></el-option>
            <el-option label="透水" value="012"></el-option>
            <el-option label="放炮" value="013"></el-option>
            <el-option label="火药爆炸" value="014"></el-option>
            <el-option label="瓦斯爆炸" value="015"></el-option>
            <el-option label="锅炉爆炸" value="016"></el-option>
            <el-option label="容器爆炸" value="017"></el-option>
            <el-option label="其他爆炸" value="018"></el-option>
            <el-option label="中毒和窒息" value="019"></el-option>
            <el-option label="其他伤害" value="020"></el-option>
          </el-select> 
        </el-form-item> 
      </el-col> 
    </el-row>
    <el-row>
      <el-form-item label="位置" prop="position">
        <el-input type="textarea" v-model="form.position"></el-input>
      </el-form-item>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="经度" prop="longitude">
          <!-- 中国经度范围E73.666666~E135.041666 -->
          <el-input-number controls-position="right" v-model="form.longitude" :precision="6" :step="0.000001" :min="73.666666" :max="135.041666"></el-input-number>
          （范围：E73.666666~E135.041666）
        </el-form-item> 
      </el-col>
      <el-col :span="12">
        <el-form-item label="纬度" prop="latitude"> 
          <!-- 中国纬度范围N3.866666~N53.550000 -->
          <el-input-number controls-position="right" v-model="form.latitude" :precision="6" :step="0.000001" :min="3.866666" :max="53.550000"></el-input-number>
          （范围：N3.866666~N53.550000）
        </el-form-item>
      </el-col> 
    </el-row>
    <el-row>
      <el-form-item label="班组巡查" prop="patrol_onduty">
        <el-radio-group v-model="form.patrol_onduty">
          <el-radio :label="0">否</el-radio>
          <el-radio :label="1">是</el-radio> 
        </el-radio-group>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（注：用于收费站风险源，此项值为“是”的风险源，将在班组排班后生成当班时间的巡查任务）
      </el-form-item>
    </el-row>
    <el-row>
      <el-form-item label="备注" prop="description">
        <el-input type="textarea" v-model="form.description"></el-input>
      </el-form-item>
    </el-row>

   <el-row> 
         <el-form-item label="二维码值" prop="qrcode">
            <el-input v-model="form.qrcode" placeholder="风险源组二维码码值"></el-input> 
         </el-form-item> 
    </el-row>

    <el-row>
      <el-form-item align="center">
        <el-button type="primary" @click="addSave" v-if="op=='add'">添加</el-button>
        <el-button @click="resetForm" v-if="op=='add'">重置</el-button>
        <el-button type="primary" @click="updateSave" v-if="op=='edit'">保存修改</el-button>
      </el-form-item>
    </el-row>
  </el-form> 

 <el-dialog :visible.sync="orgSelectPage" width="20%" title="责任单位选择" append-to-body>
            <org4Select  @closeOrg="closeOrg"></org4Select>
  </el-dialog>

</div>
</template>

<script>
/* eslint-disable */ 
import org4Select from './org4Select.vue';

  export default {
    props: ['detailForm','op'],
    components:{org4Select},
    data() {
      return { 
        orgSelectPage:false, 
        form: this.detailForm,
        rules: {
          org_name:[
            { required: true, message: '责任单位', trigger: 'blur' }
          ],
          name: [
            { required: true, message: '风险源名称', trigger: 'blur' },
            { max: 100, message: '长度小于100个字符', trigger: 'blur' }
          ],
          level: [
            { required: true, message: '请选择风险源等级', trigger: 'change' }
          ],
          position: [
            { required: true, message: '风险源位置', trigger: 'blur' },
            { max: 100, message: '长度小于100个字符', trigger: 'blur' }
          ],
          num: [
            { required: true, message: '风险源数量', trigger: 'blur' }
          ]
        },
        selOrgType: null
      };
    },
    methods: {
      chooseOrg(org){
        this.orgSelectPage = true;
		this.selOrgType = org;
      },
      closeOrg(name,id){
          this.orgSelectPage = false;
          if(name!=null && id != null){
            this.detailForm[this.selOrgType + '_name'] = name;
            this.detailForm[this.selOrgType + '_id'] = id;
          }
		  else{
            this.detailForm[this.selOrgType + '_name'] = '';
            this.detailForm[this.selOrgType + '_id'] = '';
		  }
      },
      close(){
        this.$emit('closeAddEdit', '');
      },
      updateSave() {
        this.$refs['form'].validate((valid) => {
          if (valid) {
              this.form['type_uuid']= this.form['type_uuid'].join(',');
              this.$request({
                url: '/riskSource/update',
                method: 'post',
                data: this.form
              }).then(res => { 
                  if(res.data.result_code==0){
                     this.close();
                      this.$message({
                        message: res.data.result_desc,
                        type: 'success'
                      });
                  }
                  else{
                      this.$message({
                        message: res.data.result_desc,
                        type: 'error'
                      });
                  } 
              });
          } else {
            return false;
          }
        });
      },
      addSave() {
        this.$refs['form'].validate((valid) => {
          if (valid) {
              this.form['type_uuid']= this.form['type_uuid'].join(',');
              this.$request({
                url: '/riskSource/add',
                method: 'post',
                data: this.form
              }).then(res => { 
                  if(res.data.result_code==0){
                     this.close();
                      this.$message({
                        message: res.data.result_desc,
                        type: 'success'
                      });
                  }
                  else{
                      this.$message({
                        message: res.data.result_desc,
                        type: 'error'
                      });
                  } 

              });
          } else {
            return false;
          }
        });
      },
      resetForm() {
        this.$refs['form'].resetFields();
      }

    }
  }
</script>