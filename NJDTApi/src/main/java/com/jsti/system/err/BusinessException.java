package com.jsti.system.err;
import lombok.Data;
@Data
public class BusinessException extends RuntimeException {
	
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1L;
	private ErrorCode errCode;
	
	public BusinessException() {
		super();
		this.setErrCode(new ErrorCode());
	}

	public BusinessException(int errorCode) {
		super();
		this.setErrCode(new ErrorCode(errorCode));
	}
	
	public BusinessException(int errorCode, String msg) {
		super();
		this.setErrCode(new ErrorCode(errorCode, msg));
	}

	public BusinessException(int errorCode, Throwable cause) {
		super(cause);
		this.setErrCode(new ErrorCode(errorCode));
	}

	public void setErrCode(ErrorCode errCode){
		this.errCode = errCode;
	}

	public ErrorCode getErrCode(){
		return this.errCode;
	}

}
