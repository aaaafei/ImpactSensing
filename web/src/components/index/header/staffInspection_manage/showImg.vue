<template>
  <div id="showImg1">
    <el-dialog title="图片详情" :visible="showImgVisible" @close="close()" @open="open" width="30%">
      <el-upload
        ref="upload"
        :file-list="fileList"
        action="https://jsonplaceholder.typicode.com/posts/"
        list-type="picture-card"
        disabled
        :on-preview="handlePictureCardPreview"
        auto-upload>
      </el-upload>
      <el-dialog :visible.sync="dialogVisible" append-to-body>
        <img width="100%" :src="dialogImageUrl" alt="">
      </el-dialog>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'showImg1',
  props: ['showImgVisible', 'pictureAttach'],
  data: function () {
    return {
      showImgVisible2: this.showImgVisible,
      dialogImageUrl: '',
      dialogVisible: false,
      fileList: []
    };
  },
  methods: {
    open () {
      this.fileList = [];
      let result = this.pictureAttach;
      for (let i in result) {
        let fileObject = {
          name: '',
          url: ''
        };
        fileObject.name = result[i].id;
        fileObject.url = this.$api_prefix + '/attach/download/' + result[i].id + '?Authorization=' + this.$store.getters.getToken;
        this.fileList.push(fileObject);
      }
    },
    close () {
      this.showImgVisible2 = false;
      this.$emit('headCallBack', this.showImgVisible2); // 给父组件传值，隐藏弹出框
    },
    handleRemove (file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview (file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    }
  }
};
</script>

<style>
  #showImg1 .el-upload--picture-card {
    display: none;
  }
</style>
