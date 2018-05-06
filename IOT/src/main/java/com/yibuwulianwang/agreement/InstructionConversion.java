package com.yibuwulianwang.agreement;

public class InstructionConversion {
	
	public static String getBaiduAnalysisMe(String str) {
		for (int a = 0; a < operationInstruction.length; a++) {
			if(str.equals(operationInstruction[a][1])) {
				return operationInstruction[a][0];
			}
        }
		return "Baidu Null";
	}
	
	public static String getAliAnalysisMe(String str) {
		for (int a = 0; a < operationInstruction.length; a++) {
			if(str.equals(operationInstruction[a][2])) {
				return operationInstruction[a][0];
			}
        }
		return "Ali Null";
	}
	
    //操作指令
    private static String operationInstruction[][] = {
    		//me    baidu       ali
            {"TurnOn", "TurnOnRequest","TurnOn"},
            {"TurnOff", "TurnOffRequest","TurnOff"},
            {"Discovery", "DuerOS.ConnectedHome.Discovery","AliGenie.Iot.Device.Discovery"},
            {"Control", "DuerOS.ConnectedHome.Control","AliGenie.Iot.Device.Control"},
            {"Query", "DuerOS.ConnectedHome.Query","AliGenie.Iot.Device.Query"},
            {"TurnOff", "TurnOffRequest","TurnOff"},
    };
}
