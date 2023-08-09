<template>
  <div class="container-404">
    <div class="pic-404">
      <img class="pic-404__parent" src="@/assets/404.png" alt="404">
    </div>
    <div class="text-404">
      <h1>{{ message }}</h1>
      <div >请检查您输入的网址是否正确，请点击以下按钮返回主页或者发送错误报告</div>
      <h3><a href="#" @click="toHome">返回首页</a></h3>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Page404',
    computed: {
      message () {
        return '页面不存在......';
      }
    },
    methods: {
      toHome: function () {
        this.$router.push('/home');
      }
    }
  };
</script>

<style>
.container-404 {
  /* transform: translate(-50%,-50%); */
  position: absolute;
  top: 10%;
  left: 0;
}
.pic-404 {
  position: relative;
  float: left;
  /* width: 600px; */
  overflow: hidden;
}
.text-404 {
  position: relative;
  float: left;
  width: 300px;
  padding: 10% 0;
  overflow: hidden;
}
</style>
