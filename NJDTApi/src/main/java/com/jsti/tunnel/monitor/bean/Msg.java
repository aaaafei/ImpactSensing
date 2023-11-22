package com.jsti.tunnel.monitor.bean;

import lombok.Data;

/**
 * 文件名：Msg
 * 版本信息：日期：2017/3/31 .
 */
@Data
public class Msg {
    private String title;
    private String content;
    private String etraInfo;
    public Msg(String title, String content, String etraInfo) {
        super();
        this.title = title;
        this.content = content;
        this.etraInfo = etraInfo;
    }
}
