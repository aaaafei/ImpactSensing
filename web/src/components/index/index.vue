<template>
  <el-container id="index" style="min-width:800px;">
    <el-container>
      <el-header :style="{ height: headHeight + 'px;' }">
        <index-header></index-header>
      </el-header>

      <el-main :style="{ height: bodyHeight + 'px' }">
        <el-row>
          <el-col :lg="1" :xl="2">&nbsp;</el-col>
          <el-col :lg="22" :xl="20">
            <!-- <div style="margin-top:10px;padding:0px 20px;" v-if="!isHome">
              <breadcrumb />
            </div> -->
            <keep-alive :include="includePages">
              <router-view class="child-view"></router-view>
            </keep-alive>
          </el-col>
          <el-col :lg="1" :xl="2">&nbsp;</el-col>
        </el-row>

      </el-main>

      <el-footer :style="{ height: footerHeight + 'px' }">
        <el-row style="line-height:30px;">
          <el-col :lg="1" :xl="4">&nbsp;</el-col>
          <el-col :lg="22" :xl="16">
            <div style="font-size:8px;color:grey;text-align:center;"> Copyright ©2023 南京地铁运营公司</div>
          </el-col>
          <el-col :lg="1" :xl="4">&nbsp;</el-col>
        </el-row>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
/* eslint-disable */
import indexHeader from '../../components/index/header/header.vue';
import breadcrumb from '../../components/index/breadcrumb.vue';
export default {
  name: 'index',
  data: function () {
    return {
      headHeight: 140,
      footerHeight: 30,
      bodyHeight: 600,
      isHome: window.location.href.split("/").pop() == 'home' ? true:false,
    };
  },
  components: {
    indexHeader: indexHeader,
    breadcrumb
  },
  computed: {
    storedMenu() {
      return this.$store.getters.getMenuActiveMap;
    },
    includePages: {
      get: function () {
        return this.$store.state.includePages;
      },
      set: function (pages) {
        if (pages.length > 0) {
          this.$store.dispatch('setPages', pages.join(','));
        } else {
          this.$store.dispatch('setPages', pages);
        }
      }
    }
  },
  methods: {
    waitHandleAlert() {
      this.$request({
        url: "/workflow/queryProcessTask",
        method: 'get'
      }).then(res => {
        if (res.data.result_data != null && res.data.result_data.length > 0) {
          var audio = new Audio();
          audio.src = "../../../static/audio/song.mp3";
          audio.preload = true;
          audio.loop = false;
          audio.autuplay = false;
          audio.play();

        }
      });
    }
  },
  mounted: function () {
    this.bodyHeight = document.body.clientHeight - this.headHeight - this.footerHeight;
    this.$nextTick(function () {
      this.$store.dispatch('refreshIndexs');
    });
  },
  watch: {

  }
};
</script>

<style type="stylus">
#index {
  width: 100%;
  padding: 0px;
  margin: 0px;
}

.el-header {
  padding: 0px !important;
}

.el-footer {
  background-color: #e9eef3;
}

.child-view {
  position: relative;
  left: 0;
  top: 0;
  margin-top: 10px;
  width: 100%;
}

/**滚动条的宽度*/
::-webkit-scrollbar {
  width: 2px;
  /*竖向*/
  height: 5px;
  /*横向*/
}

/*滚动条的滑块*/
::-webkit-scrollbar-thumb {
  background-color: #409EFF;
  border-radius: 3px;
}

/deep/ .el-table__body-wrapper::-webkit-scrollbar {
  width: 3px;
  /*竖向*/
  height: 5px;
  /*横向*/
}

/deep/ .el-table__body-wrapper::-webkit-scrollbar-thumb {
  background-color: #409EFF;
  border-radius: 3px;
}</style>
