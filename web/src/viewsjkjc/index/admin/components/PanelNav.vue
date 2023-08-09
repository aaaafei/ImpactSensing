<template>
  <el-row
    :gutter="0"
    class="panel-group"
  >
    <el-col
      :span="8"
      class="card-panel-col"
      v-for="item in segments"
      :key="item.name"
    >
      <div
        class="card-item"
        style=""
      >
        <div style="margin-left: 0px;">
          <ul style="list-style: none;padding:2px 2px;">
            <li
              class="card-item-left"
              style="cursor:pointer;"
              @click="pageTo(item.url,{})"
            >
              <div style="display:inline-block;width:100px">
                <span>{{item.name}}</span>
              </div>
              <div style="display:inline-block;width:40px">
                <img src="http://47.96.100.4:9205/img/绩效考核.c54d409e.png" alt="" style="width:100%;vertical-align:middle;">
              </div>
            </li>

          </ul>
        </div>
      </div>
    </el-col>

  </el-row>
</template>

<script>
/* eslint-disable */
export default {
  data() {
    return {
      segments: []
    };
  },
  methods: {
    getSegments() {
      this.segments = [
        {
          uuid: "1",
          name: "专家库",
          url: "",
          icon: "A"
        },
        {
          uuid: "1",
          name: "参数配置",
          url: "role",
          icon: "A"
        }
      ];
    },
    pageTo(path, query) {
      if (path == "monitor") {
        this.$router.push({ path: path, query: query });
      }else {
        this.$router.push({ path: path });
      }
    }
  },
  mounted: function() {
    this.getSegments();
  }
};
</script>

<style scoped>
.panel-group {
  background: #fff;
  height:100%;
}
.card-panel-col {
  margin-bottom: 12px;
  
}

.card-item {
  /* height: 150px; */
  width: 90%;
  /* border: 1px solid #dfdfdf; */
  /* border-top: 2px solid #cb5c1d; */
  /* margin-bottom: 10px; */
  display: inline-block;
  padding: 5px 5px;
}

.card-item-left {
  margin-top: 0px;
  margin-left: 0px;
  width: 150px;
  height: 45px;
  border-radius: 5px;
  background-color: #ede3da;
  float: left !important;
  display: list-item;
  text-align: -webkit-match-parent;
  padding: 10px 0px 20px 10px;
}


@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }
}
</style>
