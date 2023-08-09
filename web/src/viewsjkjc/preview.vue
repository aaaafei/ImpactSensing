<template>
    <img :src="src" alt="" height="80%" style="width:auto;" v-if="suffix=='png'||suffix=='jpg'||suffix=='bng'">
    <iframe :src="src" height="100%" width="100%" frameborder="0" scrolling="auto" v-else></iframe>
    <!-- <embed :src="src" height="100%" width="100%" type="application/pdf" scrolling="auto" v-else> -->
    <!-- <object :data="src" type="application/pdf" width="100%" height="100%" v-else>
        <iframe :src="src" height="100%" width="100%" frameborder="0" scrolling="auto" type="application/pdf"></iframe>
    </object> -->
</template>

<script>
/* eslint-disable */
  export default {
    data () {
      return {
        //   src:this.$route.query.src
        src:'',
        suffix:''
      };
    },
    mounted: function() {
        let filepath = this.$route.query.filepath;
        this.src = '/api/file/download?filePath='+ encodeURI(encodeURI(filepath));
        this.suffix = filepath.split('.').pop().toLowerCase();
        // this.src= 'http://storage.xuetangx.com/public_assets/xuetangx/PDF/PlayerAPI_v1.0.6.pdf';
    }
  };
</script>

<style scoped>
.child-view {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    transition: all .25s cubic-bezier(.55,0,.1,1);
  }
</style>