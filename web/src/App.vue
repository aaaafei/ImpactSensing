<template>
  <div>
    <transition :name="transitionName">
      <router-view class="child-view"></router-view>
    </transition>

  </div>
</template>

<script>
/* eslint-disable */

export default {
  name: 'app',
  data: function () {
    return {
      transitionName: ''
    };
  },
  watch: {
    '$route' (to, from) {
      const toDepth = to.path.split('/').length;
      const fromDepth = from.path.split('/').length;
      this.transitionName = toDepth < fromDepth ? 'slide-right' : 'slide-left';
    }
  },
  components: {
  },
  methods: {
  },
  mounted: function () {
    this.$nextTick(function () {
     // console.log($);
    });
  }
};
</script>

<style type="stylus">
  .child-view {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 90%;
    transition: all .25s cubic-bezier(.55,0,.1,1);
  }
  .slide-left-enter, .slide-right-leave-active {
    opacity: 0;
    -webkit-transform: translate(0, -50px);
    transform: translate(0, -50px);
  }
  .slide-left-leave-active, .slide-right-enter {
    opacity: 0;
    -webkit-transform: translate(-50px, 0);
    transform: translate(-50px, 0);
  }


</style>
