package com.jsti.tunnel.monitor.bean;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.sql.Date;

@Data
public class Fxbg {
    private String uuid;
    private String name;
    @NotBlank(message = "报告类型不能为空")
    private String bglx;
    private String full_path;
    @NotEmpty(message = "上报时间不能为空")
    private String report_date;
    private String creator;

}
