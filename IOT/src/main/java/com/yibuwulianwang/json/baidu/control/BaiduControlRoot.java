package com.yibuwulianwang.json.baidu.control;

public class BaiduControlRoot {
	private Header header;

    private Payload payload;

    public void setHeader(Header header){
        this.header = header;
    }
    public Header getHeader(){
        return this.header;
    }
    public void setPayload(Payload payload){
        this.payload = payload;
    }
    public Payload getPayload(){
        return this.payload;
    }
}
