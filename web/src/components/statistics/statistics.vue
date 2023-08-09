<template>
 
  <div style="height:100%;">


 
 
  <el-row style="background: rgb(255, 255, 255); padding: 16px 16px 0px;margin-bottom: 20px;margin-top:10px; "> 
      <div class="show-section top">
        <span>隐患：</span>
      </div>
      <div style="border-bottom: 1px solid #dfe9ee;"></div>
      <div class="show-section"> 
        <el-row>
          <el-col :span="12">
            <div id="hiddenDangerChart"  style="width:100%;height:400px;"></div>
          </el-col>

          <el-col  v-if="isLeader()" :span="12">
            各部门处理情况：
            <el-table  :header-cell-style="{background:'#f5f7fa'}" :data="diffOrgData" height="300" :cell-style="{padding:'3px 0'}"  style="width:100%;" >
                <el-table-column prop="organname" label="部门" align="center"></el-table-column>
                <el-table-column prop="done" label="处理完" width="100" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="doneProcess(scope.row)">
                            {{scope.row.done}}
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column label="处理中" width="100" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="doingProcess(scope.row)">
                            {{scope.row.doing}}
                        </el-button>
                    </template>
                </el-table-column>  
                <el-table-column label="未处理" width="100" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="untreatedProcess(scope.row)">
                            {{scope.row.untreated}}
                        </el-button>
                    </template>  
                </el-table-column> 
                <el-table-column label="合计" width="100" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="totalProcess(scope.row)">
                            {{scope.row.total}}
                        </el-button>
                    </template>
                </el-table-column> 
            </el-table>
          </el-col>

          <el-col  v-if="!isLeader()" :span="12">
                  <el-table :show-header="false" :data="orgHiddenDangerData" height="300"  :cell-style="{padding:'3px 0'}"  style="width:100%;">
                        <el-table-column prop="type" align="center"></el-table-column>
                        <el-table-column prop="num"   align="center">
                            <template slot-scope="scope">
                                <el-button type="text" size="small" @click="orgHiddenDangerProcess(scope.row)">
                                    {{scope.row.num}}
                                </el-button>
                            </template>
                        </el-table-column>
                  </el-table>
          </el-col>
        </el-row>
      </div> 
    </el-row> 

    <el-row v-if="isLeader()" style="background: rgb(255, 255, 255); padding: 16px 16px 0px;margin-bottom: 20px;"> 
      <div class="show-section top">
        <span>各单位任务完成进度：</span>
      </div>
      <div style="border-bottom: 1px solid #dfe9ee;"></div>
      <div class="show-section"> 
        <el-row>
          <el-col :span="12">
            <div id="progressChart" :style="{width: '100%', height: '400px'}"></div>
          </el-col>
          <el-col :span="12">
            各部门详情：
            <el-table  :header-cell-style="{background:'#f5f7fa'}" :data="progressTableData" height="300"  :cell-style="{padding:'3px 0'}" style="width:100%;">
                <el-table-column prop="organname" label="部门" align="center"></el-table-column>
                <el-table-column label="已完成任务" width="100" align="center">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="hasDoneTask(scope.row)">
                                {{scope.row.has_done}}
                            </el-button>
                        </template>
                </el-table-column>
                <el-table-column label="未完成任务" width="100" align="center">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="waitDoTask(scope.row)">
                                {{scope.row.wait_do}}
                            </el-button>
                        </template>
                </el-table-column>   
                <el-table-column label="合计" width="100" align="center">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="totalTask(scope.row)">
                                {{scope.row.total}}
                            </el-button>
                        </template>
                </el-table-column> 
            </el-table>
          </el-col> 
        </el-row>
      </div> 
    </el-row> 


    <el-row  v-if="!isLeader()" style="background: rgb(255, 255, 255); padding: 16px 16px 0px;margin-bottom: 20px;"> 
      <div class="show-section top">
        <span>任务完成进度：</span>
      </div>
      <div style="border-bottom: 1px solid #dfe9ee;"></div>
      <div class="show-section"> 
        <el-row>
          <el-col :span="12">
            <div id="deptProgressChart" :style="{width: '100%', height: '400px'}"></div>
          </el-col>
          <el-col :span="12"> 
            <el-table :show-header="false" :data="deptProgressTableData" height="300"  :cell-style="{padding:'3px 0'}" style="width:100%;">
             <el-table-column prop="type" align="center"></el-table-column>
                <el-table-column prop="num"   align="center">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="showTask(scope.row)">
                            {{scope.row.num}}
                        </el-button>
                    </template>
                </el-table-column>  
            </el-table>
          </el-col> 
        </el-row>
      </div> 
    </el-row> 

    <el-row style="background: rgb(255, 255, 255); padding: 16px 16px 0px; margin-bottom: 20px;">
      <div class="show-section top">
        <span>风险源异常简析：</span>
      </div>
      <div style="border-bottom: 1px solid #dfe9ee;"></div>
      <div class="show-section">
        <el-row>
          <el-col :span="12" v-if="isLeader()">
            各单位异常风险源数量：
            <el-table :header-cell-style="{background:'#f5f7fa'}" :data="abnormalRiskSourceTableData"  height="300"  :cell-style="{padding:'3px 0'}" style="width:97%;">
              <el-table-column prop="org_name" label="部门" :show-overflow-tooltip="true" align="center"></el-table-column>
              <el-table-column prop="total" label="风险源总数" align="center" width="140"></el-table-column>
              <el-table-column prop="abnormal_num" label="异常风险源数量" align="center" width="180"></el-table-column>
              <el-table-column label="占比" align="center" width="100">
                 <template slot-scope="scope">
                  {{(scope.row.abnormal_num/scope.row.total*100).toFixed(2)+"%"}}
                </template>
              </el-table-column>
            </el-table>
          </el-col> 
          <el-col :span="isLeader()?12:24">  
            各风险源累计上报异常数量：
            <el-table  :header-cell-style="{background:'#f5f7fa'}" :data="abnormalItemTableData" height="300"  :cell-style="{padding:'3px 0'}" style="width:100%;">
               <el-table-column prop="risk_source_name" label="风险源" align="center"></el-table-column>
               <el-table-column prop="org_name" label="部门" align="center"></el-table-column>
               <el-table-column prop="num" label="累计上报异常数量" align="center"></el-table-column>
            </el-table>
          </el-col> 
        </el-row>
      </div> 
    </el-row> 

        <el-row v-if="isLeader()" style="background: rgb(255, 255, 255); padding: 16px 16px 0px;margin-top:10px; margin-bottom:20px;"> 
      <div class="show-section top">
        <span>站点和委外单位：</span>
      </div>
      <div style="border-bottom: 1px solid #dfe9ee;"></div>
      <div class="show-section"> 
        <el-table  :header-cell-style="{background:'#f5f7fa'}" :data="feeStationTable" height="300"  :cell-style="{padding:'3px 0'}" style="width:100%;">
          <el-table-column prop="organname" label="站点" align="center"></el-table-column>
          <el-table-column prop="team_num" label="班组数" width="500" align="center"></el-table-column> 
        </el-table>
      </div> 

      <div class="show-section">
        
        <el-tag type="info">委外单位：</el-tag>
        <el-tag type="danger" v-for="item in OutsourceDepts" :key="item.id">{{item.organname}}</el-tag>
      </div> 

    </el-row>

    <el-dialog :visible.sync="hiddenDangerProcessVisible" title="隐患处理情况" width="80%">
        <hiddenDangerProcess :dutyOrg="duty_org_id" :status="processStatus" :hiddenDangerTableData="hiddenDangerTableData" :hiddenDangerTotal="hiddenDangerTotal"></hiddenDangerProcess>
    </el-dialog>

    <el-dialog :visible.sync="taskVisible" title="任务" width="80%">
        <task :dutyOrg="duty_org_id" :status="taskStatus" :taskTableData="taskTableData" :taskTotal="taskTotal"></task>
    </el-dialog>
  </div>
  
</template>

<script>
/* eslint-disable */
import hiddenDangerProcess from './hidden_danger_process.vue';
import task from './task.vue';
  export default {
    components: {hiddenDangerProcess, task},
    data() {
      return {
        feeStationTable: [],
        OutsourceDepts:[],
        diffOrgData:[],
        progressTableData: [],
        abnormalRiskSourceTableData: [],
        abnormalItemTableData: [],
        hiddenDangerProcessVisible: false,
        duty_org_id: null,
        processStatus: null,
        taskStatus: null,
        taskVisible: false,
        taskTableData: [],
        taskTotal: 0,
        hiddenDangerTableData: [],
        hiddenDangerTotal: 0,
        orgHiddenDangerData: [],
        deptProgressTableData: []
      }
    },
    mounted () {
        this.getFeeStationTeamNum();
        this.getOutsourceDepts();
        this.hiddenDanger();
        this.taskProgress();
        this.riskSourceAnalysis();
    },
    methods: {
        isAdmin () {
            var user = this.$store.getters.getUserInfo;
            var roles = user.roles;
            var isAdmin = false;
            for (var i in roles) {
                if (roles[i].name == "ROLE_ADMIN" || roles[i].name == "ROLE_COMPANY_ADMIN") {
                    isAdmin = true;
                    break;
                }
            }
            return isAdmin;
        }, 
        isLeader () {
            var user = this.$store.getters.getUserInfo;
            var roles = user.roles;
            var isLeader = false;
            for (var i in roles) {
                if (roles[i].name == "ROLE_ADMIN" || roles[i].name == "ROLE_COMPANY_ADMIN"||
                roles[i].name == "ROLE_MSS_PRINCIPAL" || roles[i].name == "ROLE_LEADER" ||
                roles[i].name == "ROLE_MSS_OFFICER") {
                    isLeader = true;
                    break;
                }
            }
            return isLeader;
        },         
      taskProgress () {
        this.$request({
          url: '/patrol/getDiffOrgPatrolProgress',
          method: 'post'
        }).then(res => {
            this.progressTableData = res.data.result_data;  
            if(res.data.result_data.length>0){
                this.deptProgressTableData.push({type:'已完成任务',num:res.data.result_data[0].has_done,org_id:res.data.result_data[0].org_id});
                this.deptProgressTableData.push({type:'未完成任务',num:res.data.result_data[0].wait_do,org_id:res.data.result_data[0].org_id});
                this.deptProgressTableData.push({type:'合计',num:res.data.result_data[0].total,org_id:res.data.result_data[0].org_id});
            }
            if(this.isLeader()){
                let xData = new Array();
                let yData = new Array();
                for(let item of this.progressTableData){
                    yData.push(item.organname);
                    xData.push((item.has_done/item.total*100).toFixed(2));
                }
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('progressChart'));
                // 绘制图表
                myChart.setOption({ 
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        },
                        formatter: '{b0} 任务完成进度：{c0}%'
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value',
                        interval:10,
                        max:100
                    },
                    yAxis: {
                        type: 'category',
                        inverse:true,
                        data: yData
                    },
                    series: [
                        {
                        name: '巡查进度',
                        type: 'bar',
                        itemStyle : {
                            normal : {
                                color : '#13ce66',
                                label : {
                                    show : true,
                                    barWidth : 5,
                                    position : 'right',//数据显示
                                    formatter : '{c}%'//百分比显示
                                }
                            }
                        },
                        data: xData
                        }
                    ]
                });
            }
            else{
                this.$request({
                    url: '/patrol/getDeptDiffPeoplePatrolProgress',
                    method: 'get'
                }).then(res => {
                    let data = res.data.result_data; 
                    let xData = new Array();
                    let yData = new Array();
                    for(let item of data){
                        yData.push(item.truename);
                        xData.push((item.has_done/item.total*100).toFixed(2));
                    }
                    // 基于准备好的dom，初始化echarts实例
                    let myChart = this.$echarts.init(document.getElementById('deptProgressChart'));
                    // 绘制图表
                    myChart.setOption({ 
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow'
                            },
                            formatter: '{b0} 任务完成进度：{c0}%'
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value',
                            interval:10,
                            max:100
                        },
                        yAxis: {
                            type: 'category',
                            inverse:true,
                            data: yData
                        },
                        series: [
                            {
                            name: '巡查进度',
                            type: 'bar',
                            itemStyle : {
                                normal : {
                                    color : '#13ce66',
                                    label : {
                                        show : true,
                                        barWidth : 5,
                                        position : 'right',//数据显示
                                        formatter : '{c}%'//百分比显示
                                    }
                                }
                            },
                            data: xData
                            }
                        ]
                    });
                });
            }
            
        }); 
      },
      getFeeStationTeamNum(){
        this.$request({
          url: '/dept/getFeeStationTeamNum',
          method: 'post'
        }).then(res => {
          let data = res.data.result_data; 
          this.feeStationTable = data;
         });
      },
      getOutsourceDepts(){
        this.$request({
          url: '/dept/getDeptInfo',
          data:{deptype:'30'},
          method: 'post'
        }).then(res => {
          let data = res.data.result_data; 
          this.OutsourceDepts = data;
         });
      },
      hiddenDanger(){
        this.$request({
          url: '/hiddenDanger/getDiffProcessNum',
          method: 'get'
        }).then(res => {
            let data = res.data.result_data;  
            let processNum = {};
            let total = 0;
 
            data.forEach((item, index) => {
              if(item.status==1){
                  processNum['untreated'] = item.num;
                }
                if(item.status==2){
                  processNum['doing'] = item.num;
                }
                if(item.status==3){
                  processNum['done'] = item.num;
                }
                total += item.num;
            });
     
                var option = {
                  title : {
                      text: '隐患数量合计:'+total, 
                      x:'center'
                  },
                  tooltip : {
                      trigger: 'item',
                      formatter: "{b} :{c}({d}%)"
                  },
                  legend: {
                      orient : 'vertical',
                      x : 'left',
                      data:['处理完','处理中','未处理']
                  }, 
                  calculable : true,
                  series : [
                      { 
                          type:'pie',
                          radius : ['40%', '60%'],
                          itemStyle : {
                              normal : {
                                  label : {
                                      show : true
                                  },
                                  labelLine : {
                                      show : true
                                  }
                              },
                              emphasis : {
                                  label : {
                                      show : true,
                                      position : 'center',
                                      textStyle : {
                                          fontSize : '30',
                                          fontWeight : 'bold'
                                      }
                                  }
                              }
                          },
                          data:[
                              {value:processNum.done, name:'处理完', itemStyle:{color:'#13ce66'}},
                              {value:processNum.doing, name:'处理中', itemStyle:{color:'#209eff'}},
                              {value:processNum.untreated, name:'未处理', itemStyle:{color:'#ff4848'}}
                          ]
                      }
                  ]
                };

              let myChart = this.$echarts.init(document.getElementById('hiddenDangerChart'));
              // 绘制图表
              myChart.setOption(option);

            });

    
            this.$request({
              url: '/hiddenDanger/getDiffProcessNumGroupByDiffOrg', 
              method: 'get'
            }).then(res => {
                let data = res.data.result_data; 
                this.diffOrgData = data; 
                if(data.length>0){
                    this.orgHiddenDangerData.push({type:'处理完',num:data[0].done,org_id:data[0].org_id});
                    this.orgHiddenDangerData.push({type:'处理中',num:data[0].doing,org_id:data[0].org_id});
                    this.orgHiddenDangerData.push({type:'未处理',num:data[0].untreated,org_id:data[0].org_id});
                    this.orgHiddenDangerData.push({type:'合计',num:data[0].total,org_id:data[0].org_id});
                } 
            });
 
        },
        riskSourceAnalysis(){
            this.$request({
            url: '/riskSource/getDiffOrgAbnormalRiskSourceNum', 
                method: 'get'
            }).then(res => {
            let data = res.data.result_data; 
                this.abnormalRiskSourceTableData = data; 
            });
    
            this.$request({
               url: '/riskSource/getRiskSourceAbnormalNum', 
               method: 'get'
            }).then(res => {
               let data = res.data.result_data; 
               this.abnormalItemTableData = data;
            });

        },
        orgHiddenDangerProcess(rowData){
            if(rowData.type=='处理完') this.doneProcess(rowData);
            if(rowData.type=='处理中') this.doingProcess(rowData);
            if(rowData.type=='未处理') this.untreatedProcess(rowData);
            if(rowData.type=='合计') this.totalProcess(rowData);
        },
        doneProcess(rowData){
            this.duty_org_id = rowData.org_id;
            this.processStatus = 3;
            let q = {org_id:this.duty_org_id,status:this.processStatus}; 
            this.$request({
                url: '/hiddenDanger/getPageData/0/10',
                method: 'post',
                data:q
            }).then(res => {
                let data = res.data;
                this.hiddenDangerTableData = data.result_data.list;
                this.hiddenDangerTotal=data.result_data.total;
                this.hiddenDangerProcessVisible = true;
            }); 
        },
        doingProcess(rowData){
            this.duty_org_id = rowData.org_id;
            this.processStatus = 2;
            let q = {org_id:this.duty_org_id,status:this.processStatus}; 
            this.$request({
                url: '/hiddenDanger/getPageData/0/10',
                method: 'post',
                data:q
            }).then(res => {
                let data = res.data;
                this.hiddenDangerTableData = data.result_data.list;
                this.hiddenDangerTotal=data.result_data.total;
                this.hiddenDangerProcessVisible = true;
            }); 
        },
        untreatedProcess(rowData){
            this.duty_org_id = rowData.org_id;
            this.processStatus = 1;
            let q = {org_id:this.duty_org_id,status:this.processStatus}; 
            this.$request({
                url: '/hiddenDanger/getPageData/0/10',
                method: 'post',
                data:q
            }).then(res => {
                let data = res.data;
                this.hiddenDangerTableData = data.result_data.list;
                this.hiddenDangerTotal=data.result_data.total;
                this.hiddenDangerProcessVisible = true;
            }); 
        },
        totalProcess(rowData){
            this.duty_org_id = rowData.org_id;
            this.processStatus = null;
            let q = {org_id:this.duty_org_id,status:this.processStatus}; 
            this.$request({
                url: '/hiddenDanger/getPageData/0/10',
                method: 'post',
                data:q
            }).then(res => {
                let data = res.data;
                this.hiddenDangerTableData = data.result_data.list;
                this.hiddenDangerTotal=data.result_data.total;
                this.hiddenDangerProcessVisible = true;
            }); 
        },
        showTask(rowData){
            if(rowData.type=='已完成任务') this.hasDoneTask(rowData);
            if(rowData.type=='未完成任务') this.waitDoTask(rowData);
            if(rowData.type=='合计') this.totalTask(rowData); 
        },
        hasDoneTask(rowData){ 
            this.duty_org_id = rowData.org_id;
            this.taskStatus = 1; 
            let q = {org_id:this.duty_org_id,has_done:this.taskStatus};
            this.$request({
                url: '/patrol/getPageInfoList/0/10',
                data: q,
                method: 'post'
            }).then(res => {
                let data = res.data;
                this.taskTableData = data.result_data.list;
                this.taskTotal=data.result_data.total;
                this.taskVisible = true;
            });
        },
        waitDoTask(rowData){
            this.duty_org_id = rowData.org_id;
            this.taskStatus = 0;
            let q = {org_id:this.duty_org_id,has_done:this.taskStatus};
            this.$request({
                url: '/patrol/getPageInfoList/0/10',
                data: q,
                method: 'post'
            }).then(res => {
                let data = res.data;
                this.taskTableData = data.result_data.list;
                this.taskTotal=data.result_data.total;
                this.taskVisible = true;
            });
        },
        totalTask(rowData){ 
            this.duty_org_id = rowData.org_id;
            this.taskStatus = null;
            let q = {org_id:this.duty_org_id,has_done:this.taskStatus};
            this.$request({
                url: '/patrol/getPageInfoList/0/10',
                data: q,
                method: 'post'
            }).then(res => {
                let data = res.data;
                this.taskTableData = data.result_data.list;
                this.taskTotal=data.result_data.total;
                this.taskVisible = true;
            });
        }
    }
  };
</script>
<style> 
  .show-section {
    margin: 10px;
    border-radius: 4px;
  }
  .show-section.top {
    background-color: white;
    padding: 10px;
    margin-top: 0;
  }
  .show-section span {
    font-weight: bold;
    font-size: 18px;
    color: #333333;
  }
  
</style>