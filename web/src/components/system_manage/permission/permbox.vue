<template>
  <div id="permissionBox">
    <el-dialog title="添加权限" :visible="resourceVisible" @open="show" @close="close()" width="30%">
      <div style="height: 56vh;overflow-y: auto;">
        <el-form ref="addEditPostform" :model="form" label-width="100px" >
          <el-form-item label="资源类型"  prop="type" for="therole">
            <el-select id="therole" v-model="form.type" @change="getResource" placeholder="请选择类型" :disabled="isDisabled">
              <el-option v-for="item in typeList" :key="item.id" :label="item.label" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="选择资源"  prop="resourceName" for="therole">
            <!-- <el-select id="therole" v-model="form.resourceid" filterable placeholder="请选择资源" :disabled="isDisabled">
            </el-select> -->
            <el-input id="postName" v-model="form.resourceName" placeholder="请输入资源名称" @focus="menusVisible=true"/>
          </el-form-item>
          <el-form-item label="名称：" prop="name" for="postName">
            <el-input id="postName" v-model="form.name" placeholder="请输入岗位名称"/>
          </el-form-item>
          <el-form-item label="父权限：" prop="pName" for="postName">
            <el-input id="postName" v-model="form.pName" disabled placeholder="请输入父权限"/>
          </el-form-item>
          <el-form-item label="描述：" prop="description" for="postName">
            <el-input id="postName" v-model="form.description" placeholder="请输入描述"/>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: right;">
          <el-button @click="close('')">取 消</el-button>
          <el-button type="primary" :loading="finished" @click="add('addEditPostform')">确 定</el-button>
        </div>
      </div>
    </el-dialog>
    <el-dialog title="选择资源" :visible="menusVisible" @close="close1()" width="30%">
      <div style="height: 100%;overflow-y: auto;border: 1px solid #e5e5e5">
        <el-tree
          class="filter-tree"
          :data="menutreeData"
          node-key="id"
          :props="defaultProps"
          default-expand-all
          highlight-current
          :expand-on-click-node="false"
          @node-click="selectMenu"
          ref="tree"
          style="display: inline-block;min-width: 11.8vw">
        </el-tree>
      </div>
    </el-dialog>
    <div style="max-height: 90vh;height:65vh;padding:15px;background-color:#fff;overflow-y: auto;border: 1px solid #e5e5e5">
      <!-- :check-strictly="true"-->
      <el-tree
        :data="authTreeData"
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
          <span style="float: right;padding-left: 20px;">
              <el-button
                type="text"
                size="mini"
                @click="() => append(data)" v-if="checkButtonPerm('org:add')">
                添加子权限
              </el-button>
                <el-button
                type="text"
                size="mini"
                @click="() => edit(data)" v-if="checkButtonPerm('org:add')">
                修改权限
              </el-button>
              <el-button
                type="text"
                size="mini"
                @click.stop="() => remove(node, data)" v-if="checkButtonPerm('org:delete')">
                删除
              </el-button>
            </span>
        </span>
      </el-tree>
    </div>
    <!-- <div sl1ot="footer" class="dialog-footer" style="text-align: right;margin-top: 10px">
      <el-button @click="close()">取 消</el-button>
      <el-button type="primary" :loading="finished" @click="permissSave()">确 定</el-button>
    </div> -->
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
        // form: this.permisData, // 岗位和角色id的数据
        theoperated: false,
        menusVisible: false,
        resourceVisible: false,
        isDisabled: false,
        finished: false,
        multipleSelection: [], // 选择的数据项
        authTreeData: [],
        menutreeData: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        form: {},
        typeList: [
          {id: 1, label: '菜单'},
          {id: 2, label: '按钮'}
        ]
      };
    },
    watch: {
      // 'form.type': function (val) {
      //   console.log(val);
      //   this.getResource(val);
      // }
    },
    methods: {
      add () {
        var data = this.form;
        this.$request({
          url: '/permission/addPermission',
          method: 'post',
          data: data
        }).then(res => {
          this.$message({
            type: 'success',
            message: res.data.result_desc
          });
          this.permisVisible = false;
          this.getPermission();
        });
      },
      selectMenu (data) {
        this.form.resourceid = data.id;
        this.form.resourceName = data.menuname;
        this.form.name = data.menuname;
        this.menusVisible = false;
      },
      getResource (type) {
        if (type === 1) { // 菜单
          this.getMenus();
        } else { // 按钮
          this.getButtons();
        }
      },
      show () {
        // this.getPermission();
        this.getResource(this.form.type);
      },
      close () {
        this.resourceVisible = false;
        // this.$emit('headCallBackPermin', this.theoperated);
      },
      close1 () {
        this.menusVisible = false;
        // this.$emit('headCallBackPermin', this.theoperated);
      },
      getMenus () {
        this.$request({
          url: '/menu/get/all',
          params: {judge: 1}
        }).then(res => {
          this.menutreeData = res.data.result_data;
        });
      },
      getButtons () {
        this.menutreeData = {};
      },
      edit (data) {
        this.form.pid = data.pid;
        this.form.pName = data.pName;
        this.form.type = data.type;
        this.form.name = data.name;
        this.form.resourceid = data.resourceid;
        this.form.resourceName = data.resourceName;
        this.form.description = data.description;
        this.resourceVisible = true;
      },
      append (data) {
        this.form.pid = data.id;
        this.form.pName = data.name;
        this.form.type = 1;
        this.form.name = '';
        this.form.resourceid = '';
        this.form.resourceName = '';
        this.form.description = '';
        this.resourceVisible = true;
      },
      checkButtonPerm () {
        return true;
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
      remove (node, data) {
        var _this = this;
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$request({
            url: '/permission/deletePermission/' + data.id
          }).then(res => {
            // _this.treePermisLoading = false; // 结束加载
            this.$message({
              type: 'success',
              message: res.data.result_desc
            });
            _this.getPermission();
          });
        }).catch(() => {
        });
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

<style scoped>
  #permissionBox {
    padding-top: 10px;
    height: 90vh;
  }
  .el-dialog__body {
    padding: 10px 20px 30px 20px;
  }
</style>
