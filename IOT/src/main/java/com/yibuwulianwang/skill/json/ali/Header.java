package com.yibuwulianwang.skill.json.ali;

public class Header {
	 private String namespace;

	    private String name;

	    private String messageId;

	    private int payLoadVersion;

	    public void setNamespace(String namespace){
	        this.namespace = namespace;
	    }
	    public String getNamespace(){
	        return this.namespace;
	    }
	    public void setName(String name){
	        this.name = name;
	    }
	    public String getName(){
	        return this.name;
	    }
	    public void setMessageId(String messageId){
	        this.messageId = messageId;
	    }
	    public String getMessageId(){
	        return this.messageId;
	    }
	    public void setPayLoadVersion(int payLoadVersion){
	        this.payLoadVersion = payLoadVersion;
	    }
	    public int getPayLoadVersion(){
	        return this.payLoadVersion;
	    }
}
