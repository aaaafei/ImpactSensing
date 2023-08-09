<template>
	<div style="margin-top:5px;">
	 
        <div style="margin-bottom:5px;"> 
            时间范围：
                <el-date-picker type="daterange" v-model="dateRange" value-format="yyyy-MM-dd" align="right" unlink-panels range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
  				<el-button type="primary" @click="refresh">刷新统计数据</el-button>
                <el-button type="primary" @click="superviseSave">仅保存</el-button>
                <el-button type="primary" @click="superviseSaveAndAlert">保存并提醒</el-button>
        </div>
		
        <el-card class="box-card" v-if="superviseRole=='ROLE_STATION_AGENT'||superviseRole=='ROLE_DEPT_OFFICER'||superviseRole=='ROLE_ORG_HEAD'">
            <div slot="header" class="clearfix">
                <span> 业务管辖范围内的风险源检查情况统计：</span> 
            </div>
            <el-table style="width:100%;" :data="taskFinishTableData" :span-method="objectSpanMethod">
                <el-table-column label="人员类型" align="center" prop="userType" width="80" fixed></el-table-column>
                <el-table-column label="巡查人员" align="center" prop="userName" width="80" fixed></el-table-column>
                <el-table-column label="按月巡查" align="center">
                    <el-table-column label="应查" align="center" width="49" prop="need_month">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'need','月')" v-if="scope.row.need_month!='-'">{{scope.row.need_month}}</span>
							<span v-if="scope.row.need_month=='-'">{{scope.row.need_month}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="实查" align="center"  width="49" prop="fact_month">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'fact','月')" v-if="scope.row.fact_month!='-'">{{scope.row.fact_month}}</span>
							<span v-if="scope.row.fact_month=='-'">{{scope.row.fact_month}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="完成率" align="center"  width="70" prop="rate_month"></el-table-column>
                </el-table-column>
                <el-table-column label="按半月巡查" align="center">
                    <el-table-column label="应查" align="center"  width="49" prop="need_half_month">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'need','半月')" v-if="scope.row.need_half_month!='-'">{{scope.row.need_half_month}}</span>
							<span v-if="scope.row.need_half_month=='-'">{{scope.row.need_half_month}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="实查" align="center"  width="49" prop="fact_half_month">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'fact','半月')" v-if="scope.row.fact_half_month!='-'">{{scope.row.fact_half_month}}</span>
							<span v-if="scope.row.fact_half_month=='-'">{{scope.row.fact_half_month}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="完成率" align="center"  width="70" prop="rate_half_month"></el-table-column>
                </el-table-column>
                <el-table-column label="按周巡查" align="center">
                    <el-table-column label="应查" align="center"  width="49" prop="need_week">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'need','周')" v-if="scope.row.need_week!='-'">{{scope.row.need_week}}</span>
							<span v-if="scope.row.need_week=='-'">{{scope.row.need_week}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="实查" align="center"  width="49" prop="fact_week">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'fact','周')" v-if="scope.row.fact_week!='-'">{{scope.row.fact_week}}</span>
							<span v-if="scope.row.fact_week=='-'">{{scope.row.fact_week}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="完成率" align="center"  width="70" prop="rate_week"></el-table-column>
                </el-table-column>  
                <el-table-column label="按天巡查" align="center">
                    <el-table-column label="应查" align="center"  width="49" prop="need_day">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'need','天')" v-if="scope.row.need_day!='-'">{{scope.row.need_day}}</span>
							<span v-if="scope.row.need_day=='-'">{{scope.row.need_day}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="实查" align="center"  width="49" prop="fact_day">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'fact','天')" v-if="scope.row.fact_day!='-'">{{scope.row.fact_day}}</span>
							<span v-if="scope.row.fact_day=='-'">{{scope.row.fact_day}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="完成率" align="center"  width="70" prop="rate_day"></el-table-column>
                </el-table-column> 
                <el-table-column label="按班次巡查" align="center">
                    <el-table-column label="应查" align="center"  width="49" prop="need_shift">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'need','班')" v-if="scope.row.need_shift!='-'">{{scope.row.need_shift}}</span>
							<span v-if="scope.row.need_shift=='-'">{{scope.row.need_shift}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="实查" align="center"  width="49" prop="fact_shift">
						<template slot-scope='scope'>
							<span class="patrolnum" @click="showPatrol(scope.row.userid,'fact','班')" v-if="scope.row.fact_shift!='-'">{{scope.row.fact_shift}}</span>
							<span v-if="scope.row.fact_shift=='-'">{{scope.row.fact_shift}}</span>
						</template>
					</el-table-column>
                    <el-table-column label="完成率" align="center"  width="70" prop="rate_shift"></el-table-column>
                </el-table-column>
				<el-table-column label="督查" min-width="180" align="center" fixed="right">
					<template slot-scope='scope'>
						<el-radio v-model="patrolSuperviseData[scope.row.userid]" label="1" size="mini">已完成</el-radio>
  						<el-radio v-model="patrolSuperviseData[scope.row.userid]" label="2" size="mini">提醒</el-radio> 
					 	<el-input v-model="patrolSuperviseRemark[scope.row.userid]" placeholder="备注" type="textarea" autosize></el-input> 
					</template>
				</el-table-column>
            </el-table> 
        </el-card>

        <br/>

        <el-card class="box-card"  v-if="superviseRole=='ROLE_ORG_HEAD' || superviseRole=='ROLE_MSS_PRINCIPAL' ||  superviseRole=='ROLE_MSS_OFFICER' || superviseRole=='ROLE_BUSINESS_LEADER'">
            <div slot="header" class="clearfix">
                <span> 抽查统计：</span> 
				<i class="el-icon-question" style="cursor:pointer" @click="randomPatrolRequireVisible=true"></i>	
			</div> 
            <el-table :data="randomPatrolData">
				<el-table-column label="人员" prop="username" align="center"  width="100"></el-table-column> 
				<el-table-column label="A级风险源" align="center">
						<el-table-column label="应查" align="center"  width="70"> 
							<template slot-scope='scope'>
								<span class="patrolnum" @click="showRandomPatrol(scope.row.userid,'A','need')" v-if="scope.row.a_need!='-'">{{scope.row.a_need}}</span>
								<span v-if="scope.row.a_need=='-'">{{scope.row.a_need}}</span>
							</template>
						</el-table-column>
						<el-table-column label="实查" align="center"  width="70">
							<template slot-scope='scope'>
								<span class="patrolnum" @click="showRandomPatrol(scope.row.userid,'A','fact')" v-if="scope.row.a_fact!='-'">{{scope.row.a_fact}}</span>
								<span v-if="scope.row.a_fact=='-'">{{scope.row.a_fact}}</span>
							</template>
						</el-table-column>
						<el-table-column label="完成率" prop="a_rate" align="center"  width="70"></el-table-column>
				</el-table-column>
				<el-table-column label="B级风险源" align="center">
						<el-table-column label="应查" align="center"  width="70">
							<template slot-scope='scope'>
								<span class="patrolnum" @click="showRandomPatrol(scope.row.userid,'B','need')" v-if="scope.row.b_need!='-'">{{scope.row.b_need}}</span>
								<span v-if="scope.row.b_need=='-'">{{scope.row.b_need}}</span>
							</template>
						</el-table-column>
						<el-table-column label="实查" align="center"  width="70">
							<template slot-scope='scope'>
								<span class="patrolnum" @click="showRandomPatrol(scope.row.userid,'B','fact')" v-if="scope.row.b_fact!='-'">{{scope.row.b_fact}}</span>
								<span v-if="scope.row.b_fact=='-'">{{scope.row.b_fact}}</span>
							</template> 
						</el-table-column>
						<el-table-column label="完成率" prop="b_rate" align="center"  width="70"></el-table-column>
				</el-table-column>
				<el-table-column label="C级风险源" align="center">
						<el-table-column label="应查" align="center"  width="70">
							<template slot-scope='scope'>
								<span class="patrolnum" @click="showRandomPatrol(scope.row.userid,'C','need')" v-if="scope.row.c_need!='-'">{{scope.row.c_need}}</span>
								<span v-if="scope.row.c_need=='-'">{{scope.row.c_need}}</span>
							</template>
						</el-table-column>
						<el-table-column label="实查" align="center"  width="70">
							<template slot-scope='scope'>
								<span class="patrolnum" @click="showRandomPatrol(scope.row.userid,'C','fact')" v-if="scope.row.c_fact!='-'">{{scope.row.c_fact}}</span>
								<span v-if="scope.row.c_fact=='-'">{{scope.row.c_fact}}</span>
							</template>
						</el-table-column>
						<el-table-column label="完成率" prop="c_rate" align="center"  width="70"></el-table-column>
				</el-table-column>
				<el-table-column label="D级风险源" align="center">
						<el-table-column label="应查" align="center"  width="70">
							<template slot-scope='scope'>
								<span class="patrolnum" @click="showRandomPatrol(scope.row.userid,'D','need')" v-if="scope.row.d_need!='-'">{{scope.row.d_need}}</span>
								<span v-if="scope.row.d_need=='-'">{{scope.row.d_need}}</span>
							</template>
						</el-table-column>
						<el-table-column label="实查" align="center"  width="70">
							<template slot-scope='scope'>
								<span class="patrolnum" @click="showRandomPatrol(scope.row.userid,'D','fact')" v-if="scope.row.d_fact!='-'">{{scope.row.d_fact}}</span>
								<span v-if="scope.row.d_fact=='-'">{{scope.row.d_fact}}</span>
							</template>
						</el-table-column>
						<el-table-column label="完成率" prop="d_rate" align="center"  width="70"></el-table-column>
				</el-table-column> 
				<el-table-column label="督查"  align="center" fixed="right">
					<template slot-scope='scope'>
						<el-radio v-model="randomPatrolSuperviseData[scope.row.userid]" label="1" size="mini">已完成</el-radio>
  						<el-radio v-model="randomPatrolSuperviseData[scope.row.userid]" label="2" size="mini">提醒</el-radio> 
					 	<el-input v-model="randomPatrolSuperviseRemark[scope.row.userid]" placeholder="备注" type="textarea" autosize></el-input> 
					</template>
				</el-table-column>
			</el-table>
        </el-card>

        <br/>

        <el-card class="box-card"  v-if="superviseRole=='ROLE_ORG_HEAD' || superviseRole=='ROLE_MSS_PRINCIPAL' ||  superviseRole=='ROLE_MSS_OFFICER' || superviseRole=='ROLE_BUSINESS_LEADER'">
            <div slot="header" class="clearfix">
                <span> 督查统计：</span> 
				<i class="el-icon-question" style="cursor:pointer" @click="superviseRequireVisible=true"></i>	
            </div> 
			<el-table :data="superviseData">
				<el-table-column label="人员" prop="userName" align="center"></el-table-column>
				<el-table-column label="执行督查次数" align="center">
					<template slot-scope="scope"> 
						<span class="patrolnum" @click="showSupervise(scope.row.userId)" v-if="scope.row.num!='0'">{{scope.row.num}}</span>
						<span v-if="scope.row.num=='0'">{{scope.row.num}}</span> 
					</template>
				</el-table-column>
				<el-table-column label="督查结果" align="center" width="200px">
					<template slot-scope='scope'>
						<el-radio v-model="supervisedData[scope.row.userId]" label="1" size="mini">已完成</el-radio>
  						<el-radio v-model="supervisedData[scope.row.userId]" label="2" size="mini">提醒</el-radio> 
					 	<el-input v-model="supervisedRemark[scope.row.userId]" placeholder="备注" type="textarea" autosize></el-input> 
					</template>
				</el-table-column>
			</el-table>
        </el-card>

		<br/>

		<el-dialog title="风险源检查情况" :visible.sync="userPatrolDialogVisible"  width="1200px">
			<el-table :data="userPatrolData" >  
		      <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
		      <el-table-column prop="org_name" label="单位" width="150" align="center"></el-table-column>
		      <el-table-column prop="username" label="任务执行人" width="90" align="center"></el-table-column>
		      <el-table-column prop="risk_source_name" label="风险源" align="center"></el-table-column>
		      <el-table-column prop="period" label="任务周期" width="100" align="center"></el-table-column> 
	          <el-table-column prop="start_time" label="任务开始时间" align="center" width="200"></el-table-column>  
		      <el-table-column prop="end_time" label="任务结束时间" align="center" width="200"></el-table-column>  
		      <el-table-column label="期数" width="100" align="center">
		      	<template slot-scope="scope">
		      		{{scope.row.patrol_type!=3?scope.row.serial_num:''}}
		      	</template>
		      </el-table-column>  
			  <el-table-column label="状态" width="100" align="center">
		          <template slot-scope="scope">  
		          	<el-button type="text" v-if="scope.row.has_done==1" @click="viewDetail(scope.row.uuid)">
	             		已完成
	          		</el-button>
		            {{scope.row.has_done==0?'未完成':''}}
		          </template>
	          </el-table-column>  
		    </el-table>
		    <el-pagination
		      @size-change="userPatrolHandleSizeChange"
		      @current-change="userPatrolHandleCurrentChange"
		      :current-page="userPatrolPage.pageNum"
		      :page-sizes="[10, 20, 50]"
		      :page-size="userPatrolPage.pageSize"
		      layout="total, sizes, prev, pager, next, jumper"
		      :total="userPatrolPage.total">
		    </el-pagination> 
		</el-dialog>

		<el-dialog title="抽查要求" :visible.sync="randomPatrolRequireVisible">
			<el-table :data="randomPatrolRequireData">
				<el-table-column prop="userType" label="人员类型" align="center"></el-table-column>
				<el-table-column label="业务管辖范围内的风险源" align="center">
					<el-table-column prop="AB" label="A、B级风险源" align="center"></el-table-column>
					<el-table-column prop="CD" label="C、D级风险源" align="center"></el-table-column>
				</el-table-column>
			</el-table>
		</el-dialog>

		<el-dialog title="抽查" :visible.sync="randomPatrolVisible" width="1200px">
			<el-table :data="randomPatrolTableData"> 
		      <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
		      <el-table-column prop="org_name" label="单位" width="150" align="center"></el-table-column>
		      <el-table-column prop="user_name" label="抽查执行人" width="90" align="center"></el-table-column>
		      <el-table-column prop="risk_source_name" label="风险源" align="center"></el-table-column> 
	          <el-table-column prop="start_time" label="任务开始时间" align="center" width="200"></el-table-column>  
		      <el-table-column prop="end_time" label="任务结束时间" align="center" width="200"></el-table-column>   
			  <el-table-column label="状态" width="100" align="center">
		          <template slot-scope="scope">  
		          	<el-button type="text" v-if="scope.row.has_done==1" @click="viewDetail(scope.row.uuid)">
	             		已完成
	          		</el-button>
		            {{scope.row.has_done==0?'未完成':''}}
		          </template>
	          </el-table-column>  
		    </el-table>
		    <el-pagination
		      @size-change="randomPatrolHandleSizeChange"
		      @current-change="randomPatrolHandleCurrentChange"
		      :current-page="randomPatrolPage.pageNum"
		      :page-sizes="[10, 20, 50]"
		      :page-size="randomPatrolPage.pageSize"
		      layout="total, sizes, prev, pager, next, jumper"
		      :total="randomPatrolPage.total">
		    </el-pagination> 
		</el-dialog>

		<el-dialog title="督查要求" :visible.sync="superviseRequireVisible">
			<el-table :data="superviseRequireData">
				<el-table-column prop="userType" label="人员类型" align="center"></el-table-column> 
				<el-table-column prop="content" label="督查内容" align="center"></el-table-column>
				<el-table-column prop="frequency" label="频次要求" align="center"></el-table-column> 
			</el-table>
		</el-dialog>
		
		<el-dialog :visible.sync="patrolResultDialogVisible" width="60%" title="巡检结果">
	       	<patrolResult :tableData="detailTableData" :picsData="detailPicsData"></patrolResult>
	    </el-dialog>
 
		<el-dialog title="督查" :visible.sync="userSuperviseVisible">
			<el-table :data="userSuperviseData">
				<el-table-column label="督查日期起" align="center">
					<template slot-scope='scope'>
						{{scope.row.begin_date!=null?scope.row.begin_date.substring(0,10):''}}
					</template>
				</el-table-column>
				<el-table-column label="督查日期止" align="center">
					<template slot-scope='scope'>
						{{scope.row.end_date!=null?scope.row.end_date.substring(0,10):''}}
					</template>
				</el-table-column>
				<el-table-column prop="supervise_time" label="督查时间" align="center"></el-table-column>
				<el-table-column label="查看详情" align="center">
					<template slot-scope='scope'> 
						<el-button type="text" size="mini" @click="showSuperviseDetail(scope.row.uuid)">详情</el-button>
					</template>
				</el-table-column>
			</el-table>
		    <el-pagination
		      @size-change="userSuperviseHandleSizeChange"
		      @current-change="userSuperviseHandleCurrentChange"
		      :current-page="userSupervisePage.pageNum"
		      :page-sizes="[10, 20, 50]"
		      :page-size="userSupervisePage.pageSize"
		      layout="total, sizes, prev, pager, next, jumper"
		      :total="userSupervisePage.total">
		    </el-pagination> 
		</el-dialog>

		<el-dialog title="督查详情" :visible.sync="superviseDetailVisible">
			<el-card class="box-card">
           		<div slot="header" class="clearfix">
           		    <span> 风险源检查：</span> 
           		</div>
				<el-table :data="superviseDetailData_Patrol">
					<el-table-column label="人员" prop="supervise_username" align="center"></el-table-column>
					<el-table-column label="是否合格" align="center">
						<template slot-scope='scope'>
							{{scope.row.evaluate==1?'合格':'不合格'}}
						</template>
					</el-table-column>
					<el-table-column label="备注" prop="remark" header-align="center"></el-table-column>
				</el-table>
			</el-card>
			<el-card class="box-card">
           		<div slot="header" class="clearfix">
           		    <span> 抽查：</span> 
					<el-table :data="superviseDetailData_RandomPatrol">
						<el-table-column label="人员" prop="supervise_username" align="center"></el-table-column>
						<el-table-column label="是否合格" prop="evaluate" align="center"></el-table-column>
						<el-table-column label="备注" prop="remark" header-align="center"></el-table-column>
					</el-table>
           		</div>
			</el-card>
			<el-card class="box-card">
           		<div slot="header" class="clearfix">
           		    <span> 督查：</span> 
           		</div> 
				<el-table :data="superviseDetailData_Supervise">
					<el-table-column label="人员" prop="supervise_username" align="center"></el-table-column>
					<el-table-column label="是否合格" prop="evaluate" align="center"></el-table-column>
					<el-table-column label="备注" prop="remark" header-align="center"></el-table-column>
				</el-table>
			</el-card>
		</el-dialog>

	</div>
</template>

<script>
/* eslint-disable */  
	import patrolResult from './../task/patrolResult';
	export default { 
		components: { patrolResult },  
		data(){
			return { 
				activeName: '1',
				dateRange: '',
				mergeArr: [], 
				taskFinishTableData: [],
				selectUUIDs: [],
				tableData: [],
				page: {pageNum: 1, pageSize: 10, total: 0 },
				dialogVisible: false, 
				qRiskSourceName: null,
                superviseRole: null,
				randomPatrolRequireVisible: false,
				randomPatrolVisible: false,
				randomPatrolTableData: [],
				randomPatrolPage: { pageNum: 0, pageSize: 10, total: 0 },
				randomPatrolCondition: {},
				randomPatrolRequireData: [{"userType":'公司分管领导','AB':'≥1次/6个月','CD':'≥1次/年'},
				{"userType":'职能部门负责人','AB':'≥1次/3个月','CD':'≥1次/6个月'},
				{"userType":'职能部门安全员','AB':'≥1次/月','CD':'≥1次/3个月'}],
				superviseRequireVisible: false,
				superviseRequireData: [{'userType':'业务分管领导','content':'职能部门负责人履职情况','frequency':'≥1次/6个月'},
				{'userType':'安全管理部负责人','content':'职能部门负责人、公司专职安全员履职情况','frequency':'≥1次/6个月'},
				{'userType':'职能部门负责人','content':'本部门业务管辖范围风险源检查情况、部门安全员履职情况','frequency':'≥1次/6个月'},
				{'userType':'公司专职安全员','content':'其他部门安全员履职情况','frequency':'≥1次/3个月'},
				{'userType':'职能部门安全员','content':'本部门业务管辖范围风险源检查情况','frequency':'≥1次/3个月'}],
				randomPatrolData: [],
				randomPatrolSuperviseData: {},
				randomPatrolSuperviseRemark: {},
				patrolSuperviseData: {},
				patrolSuperviseRemark: {},
				userPatrolDialogVisible: false,
				userPatrolData: [],
				userPatrolPage:{ pageNum: 0, pageSize: 10, total: 0 },
				userPatrolCondition:{},
				detailTableData: [],
				detailPicsData: [],
				patrolResultDialogVisible: false,
				superviseData: [],
				supervisedData: {},
				supervisedRemark: {},
				userSuperviseVisible: false,
				userSuperviseData: [],
				userSupervisePage: { pageNum: 0, pageSize: 10, total: 0 },
				getUserSuperviseData_userId: null,
				superviseDetailVisible: false,
				superviseDetailData_Patrol: [],
				superviseDetailData_RandomPatrol: [],
				superviseDetailData_Supervise: []
			};
		},
		methods: { 
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
			getPatrolData(){ 
				if(this.dateRange==null){ 
					this.$alert('请选择时间范围','提示');
					return;
				}
				
				let fd = new FormData();  
				fd.append('begin_time',this.dateRange[0]);
				fd.append('end_time',this.dateRange[1]);
				this.$request({
						url: '/patrol/getSupervisePatrol',
						method: 'post',
						data: fd
				}).then(res => { 
						this.taskFinishTableData = res.data.result_data; 
						let length = this.taskFinishTableData.length;
						let mergeArr = new Array(length);
						for(let i =0;i<length;i++) {mergeArr[i]=1;} 
						this.mergeArr = this.computeMergeArr(this.taskFinishTableData,'userType',mergeArr,0,1);

						this.getSupervisePatrolDetail();//获得风险源检查的督查结果
				});
			},
			showPatrol(userid,needOrFact,period) {
				this.userPatrolCondition.userid = userid;
				this.userPatrolCondition.needOrFact = needOrFact;
				this.userPatrolCondition.period = period;
				this.userPatrolDataQuery();
				this.userPatrolDialogVisible = true;
			},
			getRandomPatrolData(){
				let fd = new FormData();  
				fd.append('begin_time',this.dateRange[0]);
				fd.append('end_time',this.dateRange[1]);
				this.$request({
					url: '/randomPatrol/getRandomPatrolAnalysisData',
					method: 'post',
					data: fd
				}).then(res=>{
					this.randomPatrolData = res.data.result_data;
					this.getSuperviseRandomPatrolDetail();
				});
			},
			refresh(){
				this.getPatrolData();   
				this.getRandomPatrolData();  
				this.getSuperviseData();
			},
			userPatrolHandleSizeChange(v){
				this.userPatrolPage.pageSize = v;
				this.userPatrolDataQuery();
			},
			userPatrolHandleCurrentChange(v){
				this.userPatrolPage.pageNum = v;
				this.userPatrolDataQuery();
			},
			userPatrolDataQuery(){
				var params = {};
				params.userid = this.userPatrolCondition.userid;
				if(this.userPatrolCondition.needOrFact=='fact')
					params.has_done = 1;
				else
					params.has_done = null;
				params.period =  this.userPatrolCondition.period;
				params.start_time = this.dateRange[0] + " 00:00:00";
				params.end_time = this.dateRange[1] + " 23:59:59";
				this.$request({
					url: '/patrol/getUserData4Supervise/' + this.userPatrolPage.pageNum + '/' + this.userPatrolPage.pageSize,
					data: params,
					method: 'post'
				}).then(res => {
					let data = res.data;
					this.userPatrolData = data.result_data.list;
					this.userPatrolPage.total=data.result_data.total;
				});
			},
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
						this.patrolResultDialogVisible = true;
					});
				});
			},
			superviseSave(){
				//提交督查结果
				var superviseData = {begin_date:this.dateRange[0], end_date:this.dateRange[1]};
				var superviseDetail = new Array();
				for(var key in this.patrolSuperviseData){ 
					var detail = {}; 
					detail.supervise_type = 1;//风险源检查
					detail.supervise_userid = key;
					detail.evaluate = this.patrolSuperviseData[key];
					detail.remark = this.patrolSuperviseRemark[key];
					superviseDetail.push(detail);
				}
				for(var key in this.randomPatrolSuperviseData){
					var detail = {};
					detail.supervise_type=2;//抽查
					detail.supervise_userid = key;
					detail.evaluate = this.randomPatrolSuperviseData[key];
					detail.remark = this.randomPatrolSuperviseRemark[key];
					superviseDetail.push(detail);
				}
				for(var key in this.supervisedData){
					var detail = {};
					detail.supervise_type=3;//督查
					detail.supervise_userid = key;
					detail.evaluate = this.supervisedData[key];
					detail.remark = this.supervisedRemark[key];
					superviseDetail.push(detail);
				}
				let fd = new FormData();
				fd.append('supervise',JSON.stringify(superviseData));
				fd.append('superviseDetail',JSON.stringify(superviseDetail));
				this.$request({
					url: '/supervise/saveSuperviseData',
					method: 'post', 
					data: fd
				}).then(res=>{
                     this.$alert("保存成功！", "提示");
					//console.log(res);
				}); 
			},
			superviseSaveAndAlert(){
				//提交督查结果
				var superviseData = {begin_date:this.dateRange[0], end_date:this.dateRange[1]};
				var superviseDetail = new Array();
				for(var key in this.patrolSuperviseData){ 
					var detail = {}; 
					detail.supervise_type = 1;//风险源检查
					detail.supervise_userid = key;
					detail.evaluate = this.patrolSuperviseData[key];
					detail.remark = this.patrolSuperviseRemark[key];
					superviseDetail.push(detail);
				}
				for(var key in this.randomPatrolSuperviseData){
					var detail = {};
					detail.supervise_type=2;//抽查
					detail.supervise_userid = key;
					detail.evaluate = this.randomPatrolSuperviseData[key];
					detail.remark = this.randomPatrolSuperviseRemark[key];
					superviseDetail.push(detail);
				}
				for(var key in this.supervisedData){
					var detail = {};
					detail.supervise_type=3;//督查
					detail.supervise_userid = key;
					detail.evaluate = this.supervisedData[key];
					detail.remark = this.supervisedRemark[key];
					superviseDetail.push(detail);
				}
				let fd = new FormData();
				fd.append('supervise',JSON.stringify(superviseData));
				fd.append('superviseDetail',JSON.stringify(superviseDetail));
				this.$request({
					url: '/supervise/saveSuperviseDataAndAlert',
					method: 'post', 
					data: fd
				}).then(res=>{
                     this.$alert("保存、推送提醒成功！", "提示");
					//console.log(res);
				}); 
			},
			getSupervisePatrolDetail(){
				//获得风险源检查的督查结果
				this.patrolSuperviseData = {};
				this.patrolSuperviseRemark = {};
				let fd = new FormData();
				fd.append('begin_date',this.dateRange[0]);
				fd.append('end_date',this.dateRange[1]);
				fd.append("type",1);
				this.$request({
					url: '/supervise/getSupervisePatrolDetail',
					method: 'post', 
					data: fd
				}).then(res=>{
					var detailArr = res.data.result_data; 
					for(var d in detailArr){ 
						this.$set(this.patrolSuperviseData, detailArr[d].supervise_userid.toString(), detailArr[d].evaluate.toString()); 
						this.$set(this.patrolSuperviseRemark,detailArr[d].supervise_userid.toString(),detailArr[d].remark);
					}  
				});
			},
			getSuperviseRandomPatrolDetail(){
				//获得抽查的督查结果
				this.randomPatrolSuperviseData = {};
				this.randomPatrolSuperviseRemark = {};
				let fd = new FormData();
				fd.append('begin_date',this.dateRange[0]);
				fd.append('end_date',this.dateRange[1]);
				fd.append("type",2);
				this.$request({
					url: '/supervise/getSupervisePatrolDetail',
					method: 'post', 
					data: fd
				}).then(res=>{
					var detailArr = res.data.result_data; 
					for(var d in detailArr){ 
						this.$set(this.randomPatrolSuperviseData, detailArr[d].supervise_userid.toString(), detailArr[d].evaluate.toString()); 
						this.$set(this.randomPatrolSuperviseRemark,detailArr[d].supervise_userid.toString(),detailArr[d].remark);
					}  
				});
			},
			getSuperviseDetail(){
				//获得督查的督查结果
				this.supervisedData = {};
				this.supervisedRemark = {};
				let fd = new FormData();
				fd.append('begin_date',this.dateRange[0]);
				fd.append('end_date',this.dateRange[1]);
				fd.append("type",3);
				this.$request({
					url: '/supervise/getSupervisePatrolDetail',
					method: 'post', 
					data: fd
				}).then(res=>{
					var detailArr = res.data.result_data; 
					for(var d in detailArr){ 
						this.$set(this.supervisedData, detailArr[d].supervise_userid.toString(), detailArr[d].evaluate.toString()); 
						this.$set(this.supervisedRemark,detailArr[d].supervise_userid.toString(),detailArr[d].remark);
					}  
				});
			},
			getSuperviseData(){
				let fd = new FormData();
				fd.append('begin_date',this.dateRange[0]);
				fd.append('end_date',this.dateRange[1]); 
				this.$request({
					url: '/supervise/getSupervisedData',
					method: 'post', 
					data: fd
				}).then(res=>{
					this.superviseData = res.data.result_data;  
					this.getSuperviseDetail();
				});
			},
			showSupervise(userId){
				this.userSuperviseVisible = true;
				this.getUserSuperviseData_userId=userId;
				this.getUserSuperviseData(this.getUserSuperviseData_userId);
			},
			userSuperviseHandleSizeChange(v){
				this.userSupervisePage.pageSize = v;
				this.getUserSuperviseData(this.getUserSuperviseData_userId);
			}, 
			userSuperviseHandleCurrentChange(v){
				this.userSupervisePage.pageNum = v;
				this.getUserSuperviseData(this.getUserSuperviseData_userId); 
			},
			getUserSuperviseData(userId){
				let fd = new FormData();
				fd.append('begin_date',this.dateRange[0]);
				fd.append('end_date',this.dateRange[1]);  
				fd.append('userId',userId);
				this.$request({
					url: '/supervise/getUserSuperviseData/'+ this.userSupervisePage.pageNum + '/' + this.userSupervisePage.pageSize,
					method: 'post',
					data: fd
				}).then(res=>{
					this.userSuperviseData = res.data.result_data.list;  
					this.userSupervisePage.total = res.data.result_data.total;
				});
			},
			showSuperviseDetail(id){
				this.superviseDetailVisible = true;
				this.getUserSuperviseDetail(id);
			},
			getUserSuperviseDetail(id){ 
				var _this = this;
				_this.superviseDetailData_Patrol = [];
				_this.superviseDetailData_RandomPatrol = [];
				_this.superviseDetailData_Supervise = [];
				this.$request({
					url: '/supervise/getUserSuperviseDetail/'+id,
					method: 'get' 
				}).then(
					function(res){ 
						res.data.result_data.forEach(v => {
							if(v.supervise_type==1){
								_this.superviseDetailData_Patrol.push(v);
							}
							if(v.supervise_type==2){
								_this.superviseDetailData_RandomPatrol.push(v);
							}
							if(v.supervise_type==3){
								_this.superviseDetailData_Supervise.push(v);
							}
						});
					}
				);
			}, 
			randomPatrolHandleSizeChange(v){ 
				this.randomPatrolPage.pageSize = v;
				this.getUserRandomPatrolData(this.randomPatrolCondition.userid,this.randomPatrolCondition.level,this.randomPatrolCondition.needOrFact);
			}, 
			randomPatrolHandleCurrentChange(v){
				this.randomPatrolPage.pageNum = v;
				this.getUserRandomPatrolData(this.randomPatrolCondition.userid,this.randomPatrolCondition.level,this.randomPatrolCondition.needOrFact);
			},
			showRandomPatrol(userid,level,needOrFact){
				this.randomPatrolCondition.userid = userid;
				this.randomPatrolCondition.level = level;
				this.randomPatrolCondition.needOrFact = needOrFact;
				this.getUserRandomPatrolData(this.randomPatrolCondition.userid,this.randomPatrolCondition.level,this.randomPatrolCondition.needOrFact);
				this.randomPatrolVisible = true;
			},
			getUserRandomPatrolData(userid,level,needOrFact){
				var q = {};
				q['start_time'] = this.dateRange[0];
				q['end_time'] = this.dateRange[1];  
				q['userid'] = userid;
				if(needOrFact== 'fact'){
					q['has_done'] = 1;
				}
				this.$request({
					url: '/randomPatrol/getRandomPatrolData/' + level + '/' + this.page.pageNum + "/" + this.page.pageSize,
					data: q,
					method: "post"
				}).then(res => {
					let data = res.data;
					this.randomPatrolTableData = data.result_data.list;
					this.randomPatrolPage.total = data.result_data.total;
				});
			}
		},
		mounted: function () { 
			//data range:
			let curDate = new Date();
			let year = curDate.getFullYear();
			let month = (curDate.getMonth() + 1)<10?('0'+(curDate.getMonth() + 1)):(curDate.getMonth() + 1);
			let day = curDate.getDate()<10?('0'+curDate.getDate()):(curDate.getDate());
			this.dateRange = [year+'-01-01',year+'-' + month + '-' + day];

			//角色判断
            var user = this.$store.getters.getUserInfo;
            var roles = user.roles; 
            // 以下6个角色从实际上看是互斥的，即同一人不会同时有以下角色中的多个
			// ROLE_STATION_AGENT 		//各基层单位负责人(收费站站长)
            // ROLE_DEPT_OFFICER        //职能部门安全员
            // ROLE_ORG_HEAD            //职能部门负责人
            // ROLE_MSS_PRINCIPAL       //安全管理部负责人
            // ROLE_MSS_OFFICER         //公司专职安全员
            // ROLE_BUSINESS_LEADER     //业务分管领导
            for (var i in roles) { 
                if(roles[i].name == 'ROLE_STATION_AGENT' ||roles[i].name == 'ROLE_DEPT_OFFICER' || roles[i].name == 'ROLE_ORG_HEAD' || roles[i].name == 'ROLE_MSS_PRINCIPAL' || roles[i].name == 'ROLE_MSS_OFFICER' || roles[i].name == 'ROLE_BUSINESS_LEADER'){
                    this.superviseRole = roles[i].name;
                    break;
                } 
            }  

			//加载：业务管辖范围内的风险源检查情况统计数据
		 	this.getPatrolData();   

			this.getRandomPatrolData();      
			
			this.getSuperviseData();
 
		} 
	};

</script>
<style scoped>
	.patrolnum {
		cursor: pointer;
	}
</style>
