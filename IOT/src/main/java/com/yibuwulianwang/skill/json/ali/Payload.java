package com.yibuwulianwang.skill.json.ali;
import java.util.ArrayList;
import java.util.List;
public class Payload {
	private List<Devices> devices;

    public void setDevices(List<Devices> devices){
        this.devices = devices;
    }
    public List<Devices> getDevices(){
        return this.devices;
    }
    
    private String deviceId;

	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
    
}
