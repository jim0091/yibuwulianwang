package com.yibuwulianwang.json.baidu.control;

public class TargetTemperature {
	private int value;

    private String scale;

    public void setValue(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
    public void setScale(String scale){
        this.scale = scale;
    }
    public String getScale(){
        return this.scale;
    }
}
