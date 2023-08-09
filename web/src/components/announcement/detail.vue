<template>
    <div style="line-height:2">
        <el-row>
            <el-col :span="1" :offset="4" :style="{'margin-top': '10px'}">
                <div>
                    <el-button type="text" @click="$router.go(-1)"><i class="el-icon-arrow-left"></i>返回</el-button>
                </div>
            </el-col>
            <el-col :span="14" center=true :style="{'text-align':'center'}">
                <div class="title">{{obj.title}}</div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="16" :offset="4" :style="{'text-align':'center'}">
                <span class="info">时间：{{obj.publish_date}}</span>
                <span class="info">单位：{{obj.org_name}}</span>
                <span class="info">作者：{{obj.true_name}}</span>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="16" :offset="4">
                <br>
                <div class="content">
                    {{obj.content}}
                </div>
            </el-col>
        </el-row>
        <el-row v-if="obj.attachment_path!=null && obj.attachment_path!='' ">
            <el-col :span="16" :offset="4">
                <br>
                <span class="attachment">附件：</span>
                <br>
                <el-button type="text" @click="fileDownload(obj.attachment_path)"><i class="el-icon-document"></i>
                    {{ obj.attachment_path!=null ? obj.attachment_path.substr(obj.attachment_path.lastIndexOf('\\')+1) : '' }}
                </el-button> 
            </el-col>
        </el-row>
    </div>
</template>

<script>
/* eslint-disable */
   import { fileDownloadUtil } from '@/assets/utils/fileDownloadUtil'; 
  export default {
    data: () => {
        return {
            obj: {}
        };
    },
    methods: {
        fileDownload(attachment_path) {
            fileDownloadUtil(attachment_path);
        }
    },
    mounted:function() { 
        var url = '/announcement/getObjByUUID/' + this.$route.query.uuid; 
            this.$request({
            url: url,
            method: 'get'
        }).then(res => {
            this.obj = res.data.result_data;
        }); 
    }
  };
</script>

<style scoped>
    .title {
        font-size: 28px;
    }
    .info {
        font-size: 12px;
        margin-right: 30px;
    }
    .content {
        font-size: 16px;
        text-indent:33px;
    }

    .el-button {
        color: #363636;
    }
</style>