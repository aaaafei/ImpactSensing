
<template>
	<div>
		<el-form ref="form"  label-width="100px" > 
			<el-form-item label="所选人员：">
				<el-input v-model="selUserName" readonly placeholder="点击下方人员树选择人员"></el-input>
  			</el-form-item>
		</el-form>
		<div style="height:500px;overflow:auto;padding:15px;">
			<el-input placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
			 <el-tree  
			  v-loading="treeLoading"
			  empty-text="无"
			  :data="treeData"
			  node-key="id"
			  :props="defaultProps" 
			  highlight-current
			  :default-expanded-keys="[1]"
			  :filter-node-method="filterNode"
			  :expand-on-click-node="true"
			  ref="tree"
			  @node-click="selUser"
			  style="display: inline-block;/*min-width: 11.8vw*/">
			    <span class="custom-tree-node" slot-scope="{ node, data }">
		            <span>
		                <i :class="node.icon"></i>{{ node.label }}
		            </span>              
	        	</span> 
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
        props:['limitPermision'],
		data(){
			return {
				treeLoading:true,
				treeData: [],
				defaultProps: {
		          children: 'children',
		          label: 'label'
		        },
		        selUserName:null,
		        selUserId:null,
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
	        selUser (data) {
		         if(data.leaf){
		         	this.selUserName = data.label;
		         	this.selUserId = data.id;
		         }
		         else{
		         	this.selUserName = null;
		         	this.selUserId = null;
		         }
		    },
		    choose(){
		    	if(this.selUserName==null){
		    		 this.$message({
                  		message: '请选择人员',
                  		type: 'warning'
                	});
		    		return false;
		    	}
		    	this.$emit('closeUseSel', this.selUserName,this.selUserId);
		    	this.selUserName=null;
		        this.selUserId=null;
		        this.filterText=null;
		    },
		    close(){
		    	this.selUserName=null;
		        this.selUserId=null;
		        this.filterText=null;
       		   	this.$emit('closeUseSel', this.selUserName,this.selUserId);
      		}
		}, 
	    mounted: function () {
	       this.$request({
	        url: '/dept/get/deptsAndUsers/' + this.limitPermision
	      }).then(res => {  
	        this.treeData = res.data.result_data;
	        mergeOrgAndUser(res.data.result_data[0]);  
	        this.treeLoading = false; 
	      });
	    }
 	
	}

	function mergeOrgAndUser(rootOrg){ 
		if(rootOrg.users!=undefined && rootOrg.users.length>0){
			var arrUsers = rootOrg.users;
			var newUsersChildren = new Array();
			for(var i=0;i<arrUsers.length;i++){
				var childUser = {};
				childUser['id'] = arrUsers[i]['id'];
				childUser['label'] = arrUsers[i]['truename'];
				childUser['leaf'] = true;
				childUser['icon'] = 'icon_user4sel';
				newUsersChildren.push(childUser);
			} 
			if(newUsersChildren.length>0 && rootOrg.children!=undefined && rootOrg.children.length>0)
				rootOrg.children = newUsersChildren.concat(rootOrg.children);
			else if(newUsersChildren.length>0 && (rootOrg.children==undefined || rootOrg.children.length==0) )
				rootOrg.children = newUsersChildren;
		}
		if(rootOrg.children!=undefined && rootOrg.children.length>0){
			for(var x=0;x<rootOrg.children.length;x++){
				if(!rootOrg.children[x]['leaf'])
					mergeOrgAndUser(rootOrg.children[x]);
			}
		}
		return rootOrg;
	}
</script>

<style> 
   .icon_user4sel:before{  
	content:  url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABpUlEQVQ4T6WTO0/bYBSG3+MS0rQhtqWGWEJqJjagQ7vQiV+QLZ06lKWwVSV2aAcuK/hDzFUlQAz9AfwAhBAzoheBUDtVINlKFTsJqA1OfJAjAc4FI9GzfMN5z/OdK+E/jXrFp40zTYL3mUDjAP74oC/EqRVb0HmnvgugfayludE4ALDmUfxTEBDj+hQYk35Tmiityr/CkG6A7mwAdGQJZSkszOiVVSIeskzl1R0At9SkvtGSmbTCQnX279P+5r9DW6jJSEDGcE4vpIfjzlLid1jY6gt7x7ZQ5TtLYJLKtinPtJVguB+I8dwSSj46A702CDT2QVgPN5GYpptEL0um/DMSgDw/0LLuAojm2kfGm5apvAGIbwUMFKpPHkv+LjO+e1J/obz86ARvOaal3NcAsgzF7NyFmzG+54TWV9kDY9sScrHzp+DXjF4b8cmvh8u4BgwalXli/4Ut1Nxt263pzmLgs4TaegO7BmR05ysB7yyh7vQCtPxEY4GPmb/ZQn3WDjBcDywrvfb9ChidQbGas5dTW1HHmTYqwxJLcVsM/Ogq4b5XfQlYy6YRwzcaNgAAAABJRU5ErkJggg==');
}
</style>