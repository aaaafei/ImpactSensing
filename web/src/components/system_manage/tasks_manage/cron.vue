<template>
  <div>
    <el-tabs v-model="selectedTab">
    <el-tab-pane label="秒" name="sec">
       <el-row>
        <el-radio v-model="radio" label="1" @change="everyTime()">每秒 允许的通配符[, - * /]</el-radio>
       </el-row>
       <el-row>
        <el-radio v-model="radio" label="2" @change="cycle">
          <label>周期从</label>
          <el-input-number v-model="secAttr.secStart0" size="mini" :min="1" :max="59" label="描述文字"  @change="cycle"></el-input-number>
          <label>-</label>
          <el-input-number v-model="secAttr.secEnd0" size="mini" :min="1" :max="59" label="描述文字1"  @change="cycle"></el-input-number>
          <label>秒</label>
        </el-radio>
       </el-row>
       <el-row>
        <el-radio v-model="radio" label="3" @change="startOn">
          <label>从</label>
          <el-input-number v-model="secAttr.secStart1" size="mini" :min="0" :max="59" label="描述文字" @change="startOn"></el-input-number>
          <label>秒开始,每</label>
          <el-input-number v-model="secAttr.secEnd1" size="mini" :min="1" :max="59" label="描述文字" @change="startOn"></el-input-number>
          <label>秒执行一次</label>
        </el-radio>
       </el-row>
       <el-row>
        <el-radio v-model="radio" label="4" @change="handleCheckedSecondChange">
          <label>指定</label>        
        </el-radio>
       </el-row>
       <el-row style="padding-left:25px;">
        <el-checkbox-group class="checkbox-specify" v-model="secAttr.checkedSec" @change="handleCheckedSecondChange">
          <el-checkbox v-for="sec in secAttr.secondsArray" :label="sec" :key="sec">{{sec}}</el-checkbox>
        </el-checkbox-group>
       </el-row>  
    </el-tab-pane>
    <el-tab-pane label="分钟" name="min">分钟</el-tab-pane>
    <el-tab-pane label="小时" name="hour">小时</el-tab-pane>
    <el-tab-pane label="日" name="day">日</el-tab-pane>
    <el-tab-pane label="月" name="mon">月</el-tab-pane>
    <el-tab-pane label="周" name="week">周</el-tab-pane>
    <el-tab-pane label="年" name="year">年</el-tab-pane>
  </el-tabs>
  <div>
    <el-row>
      <el-col :span="3" class="time-title"><label>时间</label></el-col>
      <el-col :span="3"><label>秒</label></el-col>
      <el-col :span="3"><label>分钟</label></el-col>
      <el-col :span="3"><label>小时</label></el-col>
      <el-col :span="3"><label>日</label></el-col>
      <el-col :span="3"><label>月</label></el-col>
      <el-col :span="3"><label>星期</label></el-col>
      <el-col :span="3"><label>年</label></el-col>
    </el-row>
    <el-row>
      <el-col :span="3"><label>表达式字段</label></el-col>
      <el-col :span="3"><el-input v-model="secCron"  readonly="readonly"></el-input></el-col>
      <el-col :span="3"><el-input v-model="minCron"  readonly="readonly"></el-input></el-col>
      <el-col :span="3"><el-input v-model="hourCron" readonly="readonly"></el-input></el-col>
      <el-col :span="3"><el-input v-model="dayCron" readonly="readonly"></el-input></el-col>
      <el-col :span="3"><el-input v-model="monthCron"  readonly="readonly"></el-input></el-col>
      <el-col :span="3"><el-input v-model="weekCron" readonly="readonly"></el-input></el-col>
      <el-col :span="3"><el-input placeholder="" readonly="readonly"></el-input></el-col>
    </el-row>
     <el-row>
      <el-col :span="3"><label>Cron表达式</label></el-col>
      <el-col :span="21"><el-input v-model="cronFull" placeholder="*" readonly="readonly"></el-input></el-col>
    </el-row>  
    <el-row style="text-align:center;">
      <el-button type="primary" @click="add2Container">添加</el-button>
    </el-row>
  </div>
  </div>
</template>

<script>
  export default {
    name: 'cron',
    props: [],
    model: {
      prop: 'form.cron',
      event: 'update'
    },
    data: function () {
      return {
        selectedTab: 'sec',
        sec: '1',
        radio: '1',
        secCron: '*',
        minCron: '*',
        hourCron: '*',
        dayCron: '*',
        monthCron: '*',
        weekCron: '?',
        secAttr: {
          secStart0: '1',
          secEnd0: '2',
          secStart1: '0',
          secEnd1: '1',
          secondsArray: [],
          checkedSec: []
        }
        // cronFull: ''
      };
    },
    computed: {
      cronFull () {
        return this.secCron + ' ' + this.minCron + ' ' + this.hourCron + ' ' + this.dayCron + ' ' + this.monthCron + ' ' + this.weekCron;
      }
    },
    methods: {
      change (val) {
        this.cronFull = val;
      },
      everyTime () {
        if (this.selectedTab === 'sec') {
          this.secCron = '*';
        }
        // this.cronChange();
      },
      cycle () {
        if (this.radio === '2') {
          if (this.selectedTab === 'sec') {
            this.secCron = this.secAttr.secStart0 + '-' + this.secAttr.secEnd0;
          }
        }
        // this.cronChange();
      },
      startOn () {
        if (this.radio === '3') {
          if (this.selectedTab === 'sec') {
            this.secCron = this.secAttr.secStart1 + '/' + this.secAttr.secEnd1;
          }
        }
        // this.cronChange();
      },
      handleCheckedSecondChange () {
        if (this.radio === '4') {
          if (this.selectedTab === 'sec') {
            var checkedStr = '';
            if (this.secAttr.checkedSec.length > 0 && this.secAttr.checkedSec.length < 59) {
              for (let item of this.secAttr.checkedSec) {
                checkedStr += parseInt(item) + ',';
              }
              checkedStr = checkedStr.substr(0, checkedStr.length - 1);
            } else if (checkedStr.length === 59) {
              checkedStr = '*';
            } else {
              checkedStr = '?';
            }
            this.secCron = checkedStr;
          }
        }
        // this.cronChange();
      },
      initSecondCheckbox () {
        for (var i = 1; i < 60; i++) {
          if (i < 10) {
            this.secAttr.secondsArray.push('0' + i);
          } else {
            this.secAttr.secondsArray.push(i);
          }
        }
      },
      add2Container () {
        this.$emit('update', this.cronFull);
        this.$emit('closeCron', null);
      }
    },
    mounted: function () {
      // this.cronChange();
      this.initSecondCheckbox();
      this.everyTime();
    }
  };
</script>

<style type="stylus">
  .checkbox-specify .el-checkbox {
      margin-left: 30px !important;
  }
  .time-title {
    text-align: center;
  }
  .el-input-number--mini {
    line-height: 28px;
}
</style>
