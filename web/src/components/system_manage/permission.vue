<template>
  <div id="permissionBox">
    <el-dialog title="授予角色菜单权限" :visible="permisVisible" @open="show" @close="close()" width="30%">
      <div style="max-height: 50vh;overflow-y: auto;border: 1px solid #e5e5e5">
       <!-- :check-strictly="true"-->
        <el-tree
          :data="authTreeData"
          show-checkbox
          node-key="id"
          v-loading="treePermisLoading"
          :default-checked-keys="checkedData"
          :expand-on-click-node="false"
          :props="defaultProps"
          ref="tree"
          :check-strictly = "true"
          default-expand-all
          @check-change="handleSelectionChange"
          style="min-height: 30vh"
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span style="font-size: 12px;color: #a09f9f;" v-text="judgeType(node.data.nodeType)"> ()</span>
          </span>
        </el-tree>
      </div>
      <div sl1ot="footer" class="dialog-footer" style="text-align: right;margin-top: 10px">
        <el-button @click="close()">取 消</el-button>
        <el-button type="primary" :loading="finished" @click="permissSave()" v-if="permisData.useButton">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  // import Vue from 'vue';
  export default {
    name: 'permissionBox',
    props: ['permisData', 'checkedData', 'permisVisible', 'treePermisLoading'],
    data: function () {
      return {
        theVisible: this.permisVisible, // 页面可见性
        form: this.permisData, // 岗位和角色id的数据
        theoperated: false,
        finished: false,
        multipleSelection: [], // 选择的数据项
        authTreeData: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      };
    },
    methods: {
      show () {
        this.getPermission();
      },
      close () {
        this.theVisible = false;
        this.$emit('headCallBackPermin', this.theoperated);
      },
      getPermission () {
        this.$request({
          url: '/permission/get/all'
        }).then(res => {
          // _this.treePermisLoading = false; // 结束加载
          this.authTreeData = res.data.result_data;
        });
      },
      judgeType (data) {
        // console.log('判断dhkahow')
        // console.log(data)
        if (data === 'menu-1') {
          return '(模块)';
        }
        if (data === 'menu-2') {
          return '(页面)';
        }
        if (data === 'button') {
          return '(按钮)';
        }
      },
      permissSave () {  // 功能授权的保存
        this.finished = true;
        let menus = '';
        let buttons = '';
        let checkedNodes = this.$refs.tree.getCheckedNodes(); // 判断选中的节点
        if (checkedNodes.length !== 0) {
          for (let x = 0; x < checkedNodes.length; x++) {
            if (checkedNodes[x].nodeType === 'button') {
              buttons = buttons + checkedNodes[x].id + ',';
            } else {
              menus = menus + checkedNodes[x].id + ',';
            }
          }
        }
        let halfCheckedNodes = this.$refs.tree.getHalfCheckedNodes(); // 判断半选中的节点
        if (halfCheckedNodes.length !== 0) {
          for (let y = 0; y < halfCheckedNodes.length; y++) {
            if (halfCheckedNodes[y].nodeType === 'button') {
              buttons = buttons + halfCheckedNodes[y].id + ',';
            } else {
              menus = menus + halfCheckedNodes[y].id + ',';
            }
          }
        }
        menus = menus.substring(0, menus.length - 1);
        buttons = buttons.substring(0, buttons.length - 1);
        // Vue.http.options.emulateJSON = true;
        // Vue.http.headers.common['Authorization'] = this.$store.state.token;
        let options = {
          menus: menus,
          buttons: buttons
        };
        // options.params.menus = menus;
        // options.params.buttons = buttons;
        let url = '';
        // console.log(this.permisData.name);
        if (this.permisData.name === 'role') {
          options.roleId = this.form.id;
          url = '/role/roleBindPermission/' + options.roleId + '/' + menus;
        } else if (this.permisData.name === 'post') {
          options.positionId = this.form.id;
          url = this.$api_prefix + '/position/saveAuth';
        }
        this.$request({
          url: url
        }).then(res => {
          this.$message({
            type: 'success',
            message: res.data.result_desc
          });
          this.finished = false;
          this.close();
        });
        // this.$http.post(url, options).then((res) => {
        //   if (res.body.code === 401) {
        //     this.$router.push('/login');
        //     return false;
        //   }
        //   let success = res.body.success;
        //   if (success === true) {
        //     this.$message({
        //       type: 'success',
        //       message: res.body.message
        //     });
        //     this.finished = false;
        //     this.close();
        //   } else {
        //     this.$message.error('操作失败！');
        //     this.finished = false;
        //   }
        // }, (res) => {
        //   this.$message.error('请求错误，请重试！');
        //   this.finished = false;
        // });
      },
      handleSelectionChange (data, data1) {
        this.multipleSelection = data1;
        console.log(this.multipleSelection);
      }
    },
    mounted: function () {
      this.$nextTick(function () {
        // this.serchPermission();
        this.getPermission();
      });
    }
  };
</script>

<style type="stylus">
  .el-dialog__body {
    padding: 10px 20px 30px 20px;
  }
</style>
