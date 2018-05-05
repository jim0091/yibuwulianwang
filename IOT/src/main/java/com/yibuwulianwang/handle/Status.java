package com.yibuwulianwang.handle;

public class Status {
	String url;
	String msg;
	int code;
	int status;
	
	public Status(String url,String msg,int code) {
		setUrl(url);
		setMsg(msg);
		setCode(code);
	}
	
	public Status(String url,int code) {
		setUrl(url);
		setCode(code);
	}
	
	public Status(int status,String msg) {
		setStatus(status);
		setMsg(msg);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
