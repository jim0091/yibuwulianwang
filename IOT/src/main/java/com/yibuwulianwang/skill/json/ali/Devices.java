package com.yibuwulianwang.skill.json.ali;

import java.util.ArrayList;
import java.util.List;
public class Devices
{
    private String deviceId;

    private String deviceName;

    private String deviceType;

    private String zone;

    private String brand;

    private String model;

    private String icon;

    private List<Properties> properties;

    private List<String> actions;

    private Extensions extensions;

    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }
    public String getDeviceId(){
        return this.deviceId;
    }
    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }
    public String getDeviceName(){
        return this.deviceName;
    }
    public void setDeviceType(String deviceType){
        this.deviceType = deviceType;
    }
    public String getDeviceType(){
        return this.deviceType;
    }
    public void setZone(String zone){
        this.zone = zone;
    }
    public String getZone(){
        return this.zone;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setModel(String model){
        this.model = model;
    }
    public String getModel(){
        return this.model;
    }
    public void setIcon(String icon){
        this.icon = icon;
    }
    public String getIcon(){
        return this.icon;
    }
    public void setProperties(List<Properties> properties){
        this.properties = properties;
    }
    public List<Properties> getProperties(){
        return this.properties;
    }
    public void setActions(List<String> actions){
        this.actions = actions;
    }
    public List<String> getActions(){
        return this.actions;
    }
    public void setExtensions(Extensions extensions){
        this.extensions = extensions;
    }
    public Extensions getExtensions(){
        return this.extensions;
    }
}
