<template>

  <div>
    <el-row style="margin-top: 10px;">
      <el-col :span="24">
        <div class="grid-content bg-purple">
          <el-input
            placeholder="输入组织名进行过滤"
            v-model="filterText">
          </el-input>
          <div style="margin-top: 8px;min-height: 350px;overflow-y: auto;">
            <el-tree
              class="filter-tree"
              :data="treeData"
              v-loading="treeLoading"
              :props="defaultProps"
              default-expand-all
              :filter-node-method="filterNode"
              :expand-on-click-node="false"
              ref="tree"
              @node-click="handleNodeClick"
              node-key="id">
            </el-tree>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
/* eslint-disable */
  import {mapGetters} from 'vuex';
  export default {
    name: 'user',
    data () {
      return {
        filterText: '',
        treeData: [],
        treeLoading: false, // 组织树是否加载
        multipleSelection: [], // 选择的数据项
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        treeNode: {
          isClose: true,
          id: '',
          label: ''
        }
      };
    },
    computed: mapGetters({
      token: 'getToken'
    }),
    components: {
    },
    mounted: function () {
      this.$nextTick(function () {
        let token = this.$store.getters.getToken;
        if (token === '') {
          this.$store.dispatch('refresh');
        }
        this.seachOrg();
      });
    },
    watch: {
      filterText (val) {
        this.$refs.tree.filter(val);
      }
    },
    methods: {
      seachOrg () { // 查询组织
        this.treeLoading = true; // 开始加载
        // let options = {
        //   headers: {
        //     Authorization: this.$store.getters.getToken
        //   }
        // };
        // this.$http.get(this.$api_prefix + '/org/tree', options).then((res) => {
        //   if (res.body.code === 401) {
        //     this.$router.push('/login');
        //     return false;
        //   }
        //   let success = res.body.success;
        //   if (success === true) {
        //     console.log(res.body);
        //     this.treeData = res.body.data;
        //     this.treeLoading = false;
        //   } else {
        //     this.$message.error('组织获取失败！');
        //     this.treeLoading = false;
        //   }
        // }, (res) => {
        //   this.$message.error('请求失败！');
        //   this.treeLoading = false;
        // });
        this.$request({
          url: '/dept/get/all'
        }).then(res => {
          this.treeData = res.data.result_data;
          // let Self = this;
          // setTimeout(function () {
          //   Self.$refs.tree2.setCurrentKey(Self.treeNode.id);
          // }, 500);
          this.treeLoading = false;
          //this.postsData.postTreeLoading = false;
        });
        this.operated = false; // 每次查询结束后把操作成功项归原
        this.loading = false; // 查询结束后，停止加载
      },
      filterNode (value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      handleNodeClick (data) { 
        this.treeNode.id = data.id;
        this.treeNode.label = data.label;
        this.$emit('headCallBack1', this.treeNode);
      }
    }
  };
</script>

<style>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
