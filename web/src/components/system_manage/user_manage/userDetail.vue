<template>
  <div>
    <el-dialog title="用户详情" @close="close('')" :visible="detailVisible">
      <el-form :model="form"  label-width="130px">
        <el-form-item label="姓名："  prop="realName" >
          {{form.realName}}
        </el-form-item>
        <el-form-item label="帐号：" prop="loginName" >
          {{form.loginName}}
        </el-form-item>
        <el-form-item label="所在组织：" prop="orgName">
          {{form.organname}}
        </el-form-item>
      <el-form-item label="角色：" prop="role">  
            <el-tag v-for="role in form.roles" :key="role.id">{{role.des}}</el-tag>
        </el-form-item>
        <el-form-item label="联系电话：" prop="phone"  ref="userPhone">
          {{form.phone}}
        </el-form-item> 
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */
  export default {
    name: 'userDetailBox',
    props: ['detailVisible', 'detailForm'],
    data: function () {
      return {
        form: this.detailForm 
      };
    },
    methods: {
      close () {
        this.$emit('headCallBackDetail', '');
      }
    },
    mounted: function () { 
    }
  };
</script>

 
