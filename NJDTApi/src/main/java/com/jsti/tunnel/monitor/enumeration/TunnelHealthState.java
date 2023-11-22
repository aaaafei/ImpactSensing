package com.jsti.tunnel.monitor.enumeration;

public enum TunnelHealthState {

    A("健康",5,"4.2","5.0"),
    B("轻微受损",4,"3.4","4.2"),
    C("中度受损",3,"2.6","3.4"),
    D("严重受损",2,"1.8","2.6"),
    E("极端受损",1,"1.0","1.8");


    private String description;
    private int level;
    private String rangeBegin;
    private String rangeEnd;

    TunnelHealthState(String des,int level,String rangeBegin,String rangeEnd){
        this.description = des;
        this.level = level;
        this.rangeBegin = rangeBegin;
        this.rangeEnd = rangeEnd;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public String getRangeBegin() {
        return rangeBegin;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }


}