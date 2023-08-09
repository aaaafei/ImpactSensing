<template>
    <div class="menu-tree"> 
        <template  v-for="item in data" >
            <el-submenu :key="item.id" :index="item.id" v-if="item.children">
                <template slot="title">  
                      <span style="margin-left:5px;font-weight:bold;font-size:16px;">{{item.menuname}}</span> 
                </template>
                
                    <menutree :data="item.children"></menutree>
                
            </el-submenu>
            <el-menu-item v-else :key="item.id" :index="item.url"> 
                <span slot="title" style="margin-left:5px;font-weight:bold;font-size:16px;">{{item.menuname}}</span> 
            </el-menu-item>
        </template >
    </div>
</template>

<script>
/* eslint-disable */
/* 此封装暂时不用，与NavMenu类似，启用的话样式需要调 */
import menutree from "@/components/index/menutree";

export default {
    name: "menutree",
    data() {
        return {
        menu_data: {}
        };
    },
    components: {
        menutree: menutree
    },
    props: ["data"]
};

</script>