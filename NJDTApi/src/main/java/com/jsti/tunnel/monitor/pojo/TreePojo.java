package com.jsti.tunnel.monitor.pojo;

import lombok.Data;

import java.util.List;

@Data
public class TreePojo {

    String code;
    String label;
    List<TreePojo> children;
}
