package com.yibuwulianwang.skill.json.ali;

public class Volue {
	static String powerstate="off";
	static int temperature=21,windspeed=3;
	public static String getPowerstate() {
		return Volue.powerstate;
	}
	public static void setPowerstate(String powerstate) {
		Volue.powerstate = powerstate;
	}
	public static int getTemperature() {
		return temperature;
	}
	public static void setTemperature(int temperature) {
		Volue.temperature = temperature;
	}
	public static int getWindspeed() {
		return windspeed;
	}
	public static void setWindspeed(int windspeed) {
		Volue.windspeed = windspeed;
	}
	
//	public static String getPowerstateAdd() {
//		Volue.powerstate = "on";
//		return Volue.powerstate;
//	}

	public static int getTemperatureAdd() {
		Volue.temperature = Volue.temperature+1;
		return Volue.temperature;
	}

	public static int getWindspeedAdd() {
		Volue.windspeed = Volue.windspeed+1;
		return Volue.windspeed;
	}
}
