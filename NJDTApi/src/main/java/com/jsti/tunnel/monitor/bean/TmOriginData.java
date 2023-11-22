package com.jsti.tunnel.monitor.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * tm_origin_data
 * @author 
 */
@Data
public class TmOriginData implements Serializable {
    private Long ic;

    private String clientip;

    private String clientport;

    private String clientimei;

    private String catalog;

    private String catalogval;

    private String voltage;

    private String signalval;

    private String contents;

    private Date timstamp;

    //

    private String mtype;

    private String line;

    private String segment;

    private String stakeNumber;

}