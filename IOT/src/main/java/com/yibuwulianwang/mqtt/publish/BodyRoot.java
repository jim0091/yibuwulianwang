package com.yibuwulianwang.mqtt.publish;
public class BodyRoot
{
	private String topic;

    private String payload;

    private int qos;

    private boolean retain;

    private String client_id;

    public void setTopic(String topic){
        this.topic = topic;
    }
    public String getTopic(){
        return this.topic;
    }
    public void setPayload(String payload){
        this.payload = payload;
    }
    public String getPayload(){
        return this.payload;
    }
    public void setQos(int qos){
        this.qos = qos;
    }
    public int getQos(){
        return this.qos;
    }
    public void setRetain(boolean retain){
        this.retain = retain;
    }
    public boolean getRetain(){
        return this.retain;
    }
    public void setClient_id(String client_id){
        this.client_id = client_id;
    }
    public String getClient_id(){
        return this.client_id;
    }
}