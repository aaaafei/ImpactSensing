<template>
  <div id="userAllMenusBox">
    <el-dialog title="权限查看" :visible="menusVisible" @close="close()" width="30%">
      <div style="height: 56vh;overflow-y: auto;border: 1px solid #e5e5e5">
        <el-tree
          :data="menusData"
          :expand-on-click-node="false"
          v-loading="treePermisLoading"
          :props="defaultProps"
          ref="tree"
          default-expand-all
        style="width: 26.6vw">
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span style="font-size: 12px;color: #a09f9f;" v-text="judgeType(node.data.nodeType)"> ()</span>
          </span>
        </el-tree>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'userAllMenus',
    props: ['menusVisible', 'menusData', 'treePermisLoading'],
    data: function () {
      return {
        theVisible: this.menusVisible,
        data: this.menusData,
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      };
    },
    methods: {
      close () {
        this.$emit('headCallBack2');
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
        if (data === null) {
          return '(按钮)';
        }
      }
    },
    mounted: function () {
      this.$nextTick(function () {
      });
    }
  };
</script>

<style type="stylus">
  .el-dialog__body {
    padding: 10px 20px 30px 20px;
  }
</style>
