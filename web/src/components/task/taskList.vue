<template>
	<div style="margin-top:5px;">
		<el-tabs type="border-card" v-model="activeName" @tab-click="tabClick">
		  <el-tab-pane label="任务完成统计" name="1">  
    		<div style="margin-bottom:5px;">
    			单位：
    			<el-select placeholder="单位" v-model="org">
      				<el-option
		                v-for="item in feeList"
		                :key="item.id"
		                :label="item.organname"
		                :value="item.id">
	              </el-option> 
    			</el-select>
    			时间范围：
	    		 <el-date-picker type="daterange" v-model="dateRange" value-format="yyyy-MM-dd" align="right" unlink-panels range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
	    		 <el-button type="primary" @click="getTaskStatisticsByDiffOrg">点击查询</el-button>
			</div>
		  	    <el-table style="width:100%;min-height:500px;" :data="taskFinishTableData" :span-method="objectSpanMethod">
					<el-table-column label="人员类型" align="center" prop="userType"></el-table-column>
					<el-table-column label="巡查人员" align="center" prop="userName"></el-table-column>
					<el-table-column label="按月巡查" align="center">
						<el-table-column label="应巡查" align="center" prop="need_month"></el-table-column>
						<el-table-column label="实际巡查" align="center" prop="fact_month"></el-table-column>
						<el-table-column label="完成率" align="center" prop="rate_month"></el-table-column>
					</el-table-column>
					<el-table-column label="按半月巡查" align="center">
						<el-table-column label="应巡查" align="center" prop="need_half_month"></el-table-column>
						<el-table-column label="实际巡查" align="center" prop="fact_half_month"></el-table-column>
						<el-table-column label="完成率" align="center" prop="rate_half_month"></el-table-column>
					</el-table-column>
					<el-table-column label="按周巡查" align="center">
						<el-table-column label="应巡查" align="center" prop="need_week"></el-table-column>
						<el-table-column label="实际巡查" align="center" prop="fact_week"></el-table-column>
						<el-table-column label="完成率" align="center" prop="rate_week"></el-table-column>
					</el-table-column>  
					<el-table-column label="按天巡查" align="center">
						<el-table-column label="应巡查" align="center" prop="need_day">
								<template slot-scope='scope'>
									<span class="patrolnum" @click="showPatrol()">{{scope.row.need_day}}</span>
								</template>
							</el-table-column>
						<el-table-column label="实际巡查" align="center" prop="fact_day"></el-table-column>
						<el-table-column label="完成率" align="center" prop="rate_day"></el-table-column>
					</el-table-column> 
					<el-table-column label="按班次巡查" align="center">
						<el-table-column label="应巡查" align="center" prop="need_shift">
								<template slot-scope='scope'>
									<span class="patrolnum" @click="showPatrol()">{{scope.row.need_shift}}</span>
								</template>
							</el-table-column>
						<el-table-column label="实际巡查" align="center" prop="fact_shift"></el-table-column>
						<el-table-column label="完成率" align="center" prop="rate_shift"></el-table-column>
					</el-table-column>
				</el-table>
        	</el-card>
		  </el-tab-pane>

		  <el-tab-pane label="任务列表" name="2"> 
			<el-row>
				<el-col :span="24"> 
				  <el-input style="width:200px;" v-model="qRiskSourceName" placeholder="请输入风险源名称"></el-input>
				  <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
				</el-col>
			</el-row>   
			<el-table :data="tableData" style="width: 100%" height="450" @selection-change="handleSelectionChange"  :cell-style="{padding:'3px 0'}">
		      <el-table-column type="selection" width="29" header-align="center"></el-table-column>
		      <el-table-column label="序号" type="index" width="50" align="center"></el-table-column>
		      <el-table-column prop="org_name" label="单位" width="110" align="center" :show-overflow-tooltip="true"></el-table-column>
		      <el-table-column prop="username" label="任务执行人" width="100" align="center"></el-table-column>
		      <el-table-column prop="risk_source_name" label="风险源" align="center" show-overflow-tooltip="true"></el-table-column>
		      <el-table-column prop="period" label="任务周期" width="78" align="center"></el-table-column> 
	          <el-table-column prop="start_time" label="任务开始时间" align="center" width="150"></el-table-column>  
		      <el-table-column prop="end_time" label="任务结束时间" align="center" width="150"></el-table-column>  
		      <el-table-column label="期数" width="50" align="center">
		      	<template slot-scope="scope">
		      		{{scope.row.patrol_type!=3?scope.row.serial_num:''}}
		      	</template>
		      </el-table-column>  
			  <el-table-column label="状态" width="80" align="center">
		          <template slot-scope="scope">  
		          	<el-button type="text" v-if="scope.row.has_done==1" @click="viewDetail(scope.row.uuid)">
	             		已完成
	          		</el-button>
		            {{scope.row.has_done==0?'未完成':''}}
		          </template>
	          </el-table-column>  
		    </el-table>
		    <el-pagination
		      @size-change="handleSizeChange"
		      @current-change="handleCurrentChange"
		      :current-page="page.pageNum"
		      :page-sizes="[10, 20, 50]"
		      :page-size="page.pageSize"
		      layout="total, sizes, prev, pager, next, jumper"
		      :total="page.total">
		    </el-pagination> 
		  </el-tab-pane> 
		</el-tabs>
		
		<el-dialog :visible.sync="dialogVisible" width="60%" title="巡检结果">
	       	<patrolResult :tableData="detailTableData" :picsData="detailPicsData"></patrolResult>
	    </el-dialog>

	</div>
</template>

<script>
/* eslint-disable */
import patrolResult from './patrolResult';
	export default {
		components: { patrolResult },
		data(){
			return {
				org: null,
				activeName: '1',
				dateRange: '',
				mergeArr: [],
				feeList: [],
				taskFinishTableData: [],
				selectUUIDs: [],
				tableData: [],
				page: {
		            pageNum: 1,
		            pageSize: 10,
		            total: 0
		        },
				dialogVisible: false,
				detailTableData: [],
				detailPicsData: [],
				qRiskSourceName: null
			};
		},
		methods: {
			viewDetail(patrolUUID){
				this.$request({
		            url: '/patrol/getPatrolResult/' + patrolUUID, 
		            method: 'post'
				}).then(res => {
					let data = res.data;
					this.detailTableData = data.result_data;
					//pictures:
					this.$request({
						url: '/patrol/getPatrolPics/' + patrolUUID,
						method: 'post'
					}).then(res => {
						let data = res.data; 
						if(data.result_data[0].pic_path!=undefined && data.result_data[0].pic_path!=null)
							this.detailPicsData = data.result_data[0].pic_path.split(';');
						else
							this.detailPicsData = [];
						this.dialogVisible = true;
					});
				});

				
			},
			handleSelectionChange(rows){
				this.selectUUIDs=[];
				if(rows.length>0){
					rows.forEach(row => {
					  if (row) {
					      this.selectUUIDs.push(row.uuid);
					  }
					});
				}
			},
			handleSizeChange(val) {
				this.page.pageSize = val;
				this.searchDataList();
			},
			handleCurrentChange(val) {
				this.page.pageNum = val;
				this.searchDataList();
			},
			searchDataList(){
				this.$request({
					url: '/patrol/getPageInfoList/' + this.page.pageNum + '/' + this.page.pageSize,
					data: {risk_source_name:this.qRiskSourceName},
					method: 'post'
				}).then(res => {
					let data = res.data;
					this.tableData = data.result_data.list;
					this.page.total=data.result_data.total;
				});
			},
			search(){
				this.page.pageNum = 1;//点击搜索按钮，数据从第一页开始获取
				this.searchDataList();
			},
			objectSpanMethod({ row, column, rowIndex, columnIndex }) { 
				if (columnIndex === 0) { 
						return {
							rowspan: this.mergeArr[rowIndex],
							colspan: 1
						}; 
				}  		        	
			},
			computeMergeArr(arr,key,merge,headIndex,endIndex){ 
				let length = arr.length; 
				if(endIndex >= length) return merge;
				else{
					if(arr[headIndex][key]==arr[endIndex][key]){
						merge[headIndex] = merge[headIndex] + 1;
						merge[endIndex] = 0; 
						endIndex += 1;
						return this.computeMergeArr(arr,key,merge,headIndex,endIndex);
					}
					else{
						headIndex = endIndex;
						endIndex += 1; 
						return this.computeMergeArr(arr,key,merge,headIndex,endIndex);
					}
				}
			},
			tabClick(tab, event){
				if(tab.index=='1' && this.tableData.length==0)
					this.searchDataList();
			},
			getTaskStatisticsByDiffOrg(){ 
				if(this.dateRange==null){ 
						this.$alert('请选择时间范围','提示');
						return;
				}
				let fd = new FormData(); 
				fd.append('org_id',this.org);
				fd.append('begin_time',this.dateRange[0]);
				fd.append('end_time',this.dateRange[1]);
				this.$request({
						url: '/patrol/getTaskStatisticsByDiffOrg',
						method: 'post',
						data: fd
				}).then(res => { 
						this.taskFinishTableData = res.data.result_data;
						// this.taskFinishTableData = [
						// 	{userType:'安全员',userName:'霍沁湉',need_week:'4',fact_week:'4',rate_week:'100%'},
						// 	{userType:'安全员',userName:'霍沁湉',need_week:'4',fact_week:'4',rate_week:'100%'},
						// 	{userType:'安全员',userName:'霍沁湉',need_week:'4',fact_week:'4',rate_week:'100%'},
						// 	{userType:'现场责任人',userName:'李亮',need_day:'4',fact_day:'4',rate_day:'100%'},
						// 	{userType:'现场责任人',userName:'李亮',need_day:'4',fact_day:'4',rate_day:'100%'},
						// 	{userType:'现场责任人',userName:'李亮',need_day:'4',fact_day:'4',rate_day:'100%'},
						// ];
						let length = this.taskFinishTableData.length;
					let mergeArr = new Array(length);
					for(let i =0;i<length;i++) {mergeArr[i]=1;} 
					this.mergeArr = this.computeMergeArr(this.taskFinishTableData,'userType',mergeArr,0,1);
				});
			},
			showPatrol() {
				this.$message({
					message: '成功！',
					type: 'success'
				});
			}
					
		},
		mounted: function () {
			this.$request({
				url: '/dept/getDeptByParent/0'
			}).then(res => {
				//org
				this.feeList = res.data.result_data[0].children;  
				this.org =  this.feeList[0].id;//set first as default

				//data range:
				let curDate =  new Date();
				let year = curDate.getFullYear();
				let month = (curDate.getMonth() + 1)<10?('0'+(curDate.getMonth() + 1)):(curDate.getMonth() + 1);
				let day = curDate.getDate()<10?('0'+curDate.getDate()):(curDate.getDate());
				this.dateRange = [year+'-01-01',year+'-' + month + '-' + day];
				this.getTaskStatisticsByDiffOrg();

			});
		}
	};

</script>
<style scoped>
	.patrolnum {
		cursor: pointer;
	}
</style>
