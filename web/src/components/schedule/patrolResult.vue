<template>
	<div> 
		<!-- <el-collapse v-model="activeNames">
			<el-collapse-item title="检查内容" name="1"> -->
				<el-table :data="tableData" style="width:100%;" :row-class-name="tableRowClassName" height="450px"> 
			      <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
			      <el-table-column prop="content" label="检查内容" width="300" align="center">
						</el-table-column>
			      <el-table-column label="检查结果" width="100" align="center">
							<template slot-scope='scope'>
								<el-select  v-model="scope.row.result" v-if="!allNormal && allowUpdate">
									<el-option 
										v-for="item in formatResultOptions(scope.row)" 
										:key="item.value" :label="item.label" :value="item.value">
									</el-option>
								</el-select >
								<span v-else>{{scope.row.options[0]}}</span>
							</template>
			      </el-table-column> 
			      <el-table-column label="备注" align="center">
							<template slot-scope="scope">
								<el-input
									v-if="!allNormal && allowUpdate"
									ref = "remark"
									v-model="scope.row.remark" 
									placeholder=""
									style="width: 100%">
								</el-input>
								<span v-else>{{scope.row.remark}}</span>
							</template>	
						</el-table-column> 
						<el-table-column label="处理部门" align="center" width="150" v-if="!allNormal">
							<template slot-scope='scope'>
								<el-select v-model="dept" clearable placeholder="请选择" v-if="allowUpdate">
									<el-option
										v-for="item in deptOptions"
										:key="item.value"
										:label="item.label"
										:value="item.value">
									</el-option>
								</el-select>
							</template>
						</el-table-column>
						
			      <el-table-column label="异常整改期限(天)" width="150" align="center">
			      	<template slot-scope="scope">
								<el-input-number
									v-if="!allNormal && allowUpdate"
									ref = "expires"
									v-model="scope.row.expires"
									:min=0
									style="width: 90%">
								</el-input-number>
								<span v-else>{{scope.row.expires}}</span>
			      		<!-- {{scope.row.result==1?scope.row.expires:''}} -->
			      	</template>
			      </el-table-column>   
			    </el-table> 
				<div>
					<span style="background-color:#FFE4E1;font-weight:300;font-size:10px;">注：红色列为提报异常项</span>
					<el-button id="submit" @click="submitData" v-if="!allNormal && allowUpdate" type="primary" size="small">提交修改</el-button>
				</div>
				<div v-if="!allNormal && allowUpdate">
				<el-upload
					:action="'/api/patrol/uploadPatrolResultImage'"
					:headers="imageUrlHeader"
					:data="imageData"
					:file-list="fileList"
					list-type="picture-card"
					:limit=3
					accept=".jpg,.jpeg,.png,.JPG,.JPEG,.PNG"
					:on-exceed="picExceed"
					:on-success="picUpload"
					:on-remove="picRemove">
					<i class="el-icon-plus"></i>
				</el-upload>
				<el-dialog :visible.sync="dialogVisible">
					<img width="80%" :src="dialogImageUrl" alt="">
				</el-dialog>
				</div>

				<div id="imgshow" v-else-if="fileList.length>0">
					<ul v-for="item in fileList" class="el-upload-list el-upload-list--picture-card">
						<li class="el-upload-list__item is-success"><img :src="'/api/hiddenDanger/download/'+ encodeURI(encodeURI(item.path))" style="width:80px;height:80px;"></li>
					</ul>
				</div>
			<!-- </el-collapse-item>
		</el-collapse> -->

	</div>
</template>

<script>
/* eslint-disable */
	import store from '@/store';

	export default{
		props:['patrolTask', 'tableData', 'allNormal', 'allowUpdate'],
		data() {
			return {
				activeNames: ['1'],
				dialogImageUrl: '',
				dialogVisible: false,
				patrolTaskInner: {},
				uploadImageUrl: "/api/patrol/updatePatrolResultImage",
				imageUrlHeader:{token:store.getters.getToken},
				fileList: [],
				imageData: {},
				dept:'1',
				deptOptions: [{
          value: '1',
          label: '安全部'
        }, {
          value: '2',
          label: '工程部'
        }],
				// allNormal: true,
				// picsData: this.picsData
			};
		},
		watch: {
			patrolTask: {
				immediate: true,
				handler(newValue, oldValue) {
					this.patrolTaskInner = newValue;
					this.initFileList();
				}
			}
			
    },
		methods: { 
			formatResultOptions(row) {
				let resultOptions = [
					{label:row.options.split(";")[0], value:0},
					{label:row.options.split(";")[1], value:1},
				];
				return resultOptions;
			},
			tableRowClassName({row, rowIndex}) {
					if (row.result == 1) {
						return 'warning-row';
					}
					return '';
			},
			submitData(){
				this.$request({
					url: '/patrol/updatePatrolResult', 
					params: {data:JSON.stringify(this.tableData)},
					method: 'post'
				}).then(res => {
					this.$message({
						message: '修改成功！',
						type: 'success'
					});
					this.$emit("closeDetail","");
				});
			},
			picExceed(files, fileList) {
				this.$message({
					message: '系统最多支持3张图片',
					type: 'warning'
				});
			},
			picRemove(file, fileList) {
				this.$request({
					url:'/patrol/deletePatrolResultImage',
					method:'post',
					params: {patroluuid:this.imageData.patroluuid, path:file.path}
				}).then(res => {
					let data = res.data;
					this.$message({
						message: '删除成功！',
						type: 'success'
					});
				});
			},
			picUpload(response, file, fileList) {
				let responeData = response.result_desc;
				file.path = responeData;
				let pics = [];
				for(let i in fileList) {
					pics.push(fileList[i].path);
				}
			},
			initFileList() {
				this.fileList = [];
				let patrolUUID = this.patrolTaskInner.uuid;
				this.imageData.patroluuid = patrolUUID;
				this.$request({
					url: '/patrol/getPatrolPics/' + patrolUUID, 
					method: 'post'
				}).then(res => {
					let data = res.data;
					let result_data = data.result_data;
					if(result_data.length < 1) return;
					if(!Object.prototype.hasOwnProperty.call(result_data[0], 'pic_path')) return;
					let pic_path = result_data[0].pic_path;
					if(pic_path == '') return;
					let pics = pic_path.split(';');
					for(let i in pics) {
						let obj = {
							name : pics[i].split('\\').reverse()[0],
							url : '/api/hiddenDanger/download/'+ encodeURI(encodeURI(pics[i])),
							path : pics[i]
						};
						this.fileList.push(obj);
					}
				});
			}
		}
		
		
	};
</script>

<style scope>
  .el-table .warning-row {
    background-color: #FFE4E1;
  }
	/* 设置table中hover时不改变颜色，也没有动画效果 */
	.el-table__body tr:hover>td{
      background-color: initial !important;
	}
  /* 改变upload图片框的样式 */
	.el-upload--picture-card, .el-upload-list--picture-card .el-upload-list__item {
		width: 80px;
		height: 80px;
		line-height: 85px;
	}
	input[type=file] {
		display: none;
	}

	#submit {
		float:right;
		margin-right:18px;
	}

	#imgshow {
		height: 80px;
	}
	#imgshow ul li {
		list-style: none;
		display: block;
		float: left;
	}
  
 
</style>