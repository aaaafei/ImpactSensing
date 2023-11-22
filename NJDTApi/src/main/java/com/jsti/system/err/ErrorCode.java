package com.jsti.system.err;
import lombok.Data;
@Data
public class ErrorCode {
	private int code;
    private String desc;

    public ErrorCode(){}
    
    public ErrorCode(int code) {
        this.setCode(code);
        this.setDesc(ErrorCodeEnum.getPropByKey(code+""));
    }
    
    public ErrorCode(int code, String desc) {
        this.setCode(code);
        this.setDesc(ErrorCodeEnum.getPropByKey(code+"")+ ":" + desc);
    }

    public void setCode(int code){
        this.code = code;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public int getCode(){
        return this.code;
    }

    public String getDesc(){
        return this.desc;
    }


}
