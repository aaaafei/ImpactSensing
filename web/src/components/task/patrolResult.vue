<template>
	<div> 
		<el-collapse v-model="activeNames">
			<el-collapse-item title="检查内容" name="1">
				<el-table :data="tables" style="width:100%;" :row-class-name="tableRowClassName" height="300px"> 
			      <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
			      <el-table-column prop="content" label="检查内容" width="300" align="center"></el-table-column>
			      <el-table-column label="检查结果" width="90" align="center">
			      	<template slot-scope="scope">
			      		{{scope.row.options.split(';')[scope.row.result]}}
			      	</template>
			      </el-table-column> 
			      <el-table-column prop="remark" label="备注" align="center"></el-table-column> 
			      <el-table-column label="异常整改期限(天)" width="150" align="center">
			      	<template slot-scope="scope">
			      		{{scope.row.result==1?scope.row.expires:''}}
			      	</template>
			      </el-table-column>   
			    </el-table> 
				<div>
					<span style="background-color:#FFE4E1;font-weight:300;font-size:10px;">注：红色列为提报异常项</span>
				</div>
			</el-collapse-item>
			<el-collapse-item title="检查现场照片" name="2">
			    <el-carousel :interval="4000" type="card" height="200px">
    				<el-carousel-item v-for="item in pics" :key="item">
      					<img :src="'/api/file/download?filePath='+ encodeURI(encodeURI(item))" style="width:400px;">
    				</el-carousel-item>
  				</el-carousel>
			</el-collapse-item>
		</el-collapse>

	</div>
</template>

<script>
/* eslint-disable */
	export default{
		props:['tableData','picsData'],
		data() {
			return {
				tables: this.tableData,
				activeNames: ['1'],
				pics: this.picsData
			};
		},
		methods: { 
			tableRowClassName({row, rowIndex}) {
		        if (row.result == 1) {
		    	   return 'warning-row';
		       	}
		       return '';
		    }
		},
		watch:{
			tableData: {
				handler(newValue,oldValue){
					this.tables =  newValue;
				}
			},
			picsData: {
				handler(newValue,oldValue){
					this.pics = newValue; 
				}
			}
		}
	};
</script>

<style>
  .el-table .warning-row {
    background: #FFE4E1;
  }

  
 
</style>