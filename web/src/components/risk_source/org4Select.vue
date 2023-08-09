
<template>
	<div>
		<el-form ref="form"  label-width="100px" > 
			<el-form-item label="所选部门：">
				<el-input v-model="selName" readonly placeholder="点击下方人员树选择部门"></el-input>
  			</el-form-item>
		</el-form>
		<div style="height:500px;overflow:auto;padding:15px;">
			<el-input
  placeholder="输入关键字进行过滤"
  v-model="filterText">
</el-input>
		 <el-tree  
		  v-loading="treeLoading"
		  :data="treeData"
		  node-key="id"
		  :props="defaultProps" 
		  highlight-current
		  :default-expanded-keys="[1]"
		  :filter-node-method="filterNode"
		  :expand-on-click-node="true"
		  ref="tree"
		  @node-click="sel"
		  style="display: inline-block;/*min-width: 11.8vw*/"> 
		</el-tree>
		</div>
		<el-row>
			 <div style="text-align:center;">
			<el-button type="primary" plain @click.native="choose">确定</el-button>
			<el-button plain @click.native="close">取消</el-button>
		</div>
		</el-row>
	</div>
</template>

<script>
  /* eslint-disable */
	export default{
		data(){
			return {
				treeLoading:false,
				treeData: [],
				defaultProps: {
		          children: 'children',
		          label: 'label'
		        },
		        selName:null,
		        selId:null,
		        filterText: null
			};
		},
		 watch: {
	      filterText(val) {
	        this.$refs.tree.filter(val);
	      }
	    },
		methods:{
			filterNode (value, data) {  //  树结构的过滤函数
		        if (!value) return true;
		        return data.label.indexOf(value) !== -1;
	        },
	        sel (data) { 
		        this.selName = data.label;
		        this.selId = data.id;
		    },
		    choose(){
		    	if(this.selName==null){
		    		 this.$message({
                  		message: '请选择单位',
                  		type: 'warning'
                	});
		    		return false;
		    	}
		    	this.$emit('closeOrg', this.selName,this.selId);
		    	this.selName=null;
		        this.selId=null;
		        this.filterText=null;
		    },
		    close(){
		    	this.selName=null;
		        this.selId=null;
		        this.filterText=null;
       		   	this.$emit('closeOrg', this.selName,this.selId);
      		}
		}, 
	    mounted: function () {
	       this.$request({
	        url: '/dept/get/all'
	      }).then(res => { 
	        this.treeLoading = true; 
	        this.treeData = res.data.result_data; 
	        this.treeLoading = false; 
	      });
	    }
 	
	}

 
</script>

 