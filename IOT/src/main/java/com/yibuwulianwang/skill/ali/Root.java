package com.yibuwulianwang.skill.ali;

public class Root {
	private String returnCode;

    private String returnErrorSolution;

    private String returnMessage;

    private ReturnValue returnValue;

    public void setReturnCode(String returnCode){
        this.returnCode = returnCode;
    }
    public String getReturnCode(){
        return this.returnCode;
    }
    public void setReturnErrorSolution(String returnErrorSolution){
        this.returnErrorSolution = returnErrorSolution;
    }
    public String getReturnErrorSolution(){
        return this.returnErrorSolution;
    }
    public void setReturnMessage(String returnMessage){
        this.returnMessage = returnMessage;
    }
    public String getReturnMessage(){
        return this.returnMessage;
    }
    public void setReturnValue(ReturnValue returnValue){
        this.returnValue = returnValue;
    }
    public ReturnValue getReturnValue(){
        return this.returnValue;
    }
}
