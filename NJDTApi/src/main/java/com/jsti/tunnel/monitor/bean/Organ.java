package com.jsti.tunnel.monitor.bean;

import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

@Data
public class Organ {

    private Integer id;

    private String organname; //组织机构名称

    private String remark;//备注

    private Integer parentid;//父节点ID

    private String classtype;//班次类型  （1 两班 2 三班）'

    private String organcode;//机构编码

    private String isshow;//是否显示，0：不显示，1：显示

    private String deptype;//单位类型：10：一般部门，20：收费站，30：委外单位

    private Integer status;//0 正常 -1删除

    private Date creattime;

    private Integer sortno;

    @Transient
    private String label;//用在前台部门树的名字

    @Transient
    private boolean leaf;

    @Transient
    private Integer team_num;

    private List<Organ> children;

    private List<User> users;


}
