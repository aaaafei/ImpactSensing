<template>
  <div class="app-container">  
    
    <img :src="pic2Path" alt="剖面图" style="width:100%;"/>

  </div>
</template>

<script>
/* eslint-disable */
  export default {
    props:["deviceInfoForm"],
    data() {
      return {
      }
    },
    computed: {
    //   pic1Path: function () {
    //     if (this.deviceInfoForm.hasOwnProperty("pic1")) {
    //       return require('@/assets/img/' + this.deviceInfoForm.pic1);
    //     }else {
    //       return require('@/assets/img/1.png');
    //     }
    //   },
      pic2Path: function() {
        console.log(this.deviceInfoForm);
        if (this.deviceInfoForm.hasOwnProperty("pic2")) {
          return require('@/assets/position_img/' + this.deviceInfoForm.pic2);
        }else {
          return require('@/assets/img/2.png');
          // console.log(this.$STATIC_PREFIX+'images/tunnel/'+this.deviceInfoForm.pic2);
          // return require(this.$STATIC_PREFIX+'images/tunnel/'+this.deviceInfoForm.pic2);
          
        }
      }
    },
    watch:{
        // deviceInfoForm:{
        //     handler(newValue,oldValue){
        //       console.log(newValue);
        //       if (newValue.hasOwnProperty("pic2")) {
        //         let picpath =  this.$STATIC_PREFIX+'tunnel/'+newValue.pic2;
        //         this.pic2Path = picpath;
        //         // return require('@/assets/img/' + this.deviceInfoForm.pic2);
        //       }else {
        //         // return require('@/assets/img/2.png');
        //         let picpath =  this.$STATIC_PREFIX+'tunnel/'+newValue.pic2;
        //         this.pic2Path = picpath;
        //       }
        //     }
        // },
    },
    methods: {
      save() { 
        
      }
    },
    mounted:function() {

    }
  }
</script>