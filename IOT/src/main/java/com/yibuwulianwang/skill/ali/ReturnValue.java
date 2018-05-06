package com.yibuwulianwang.skill.ali;

import java.util.List;

public class ReturnValue {
	private String reply;

    private String resultType;

    private List<Actions> actions;

    private Properties properties;

    private String executeCode;

    private String msgInfo;

    public void setReply(String reply){
        this.reply = reply;
    }
    public String getReply(){
        return this.reply;
    }
    public void setResultType(String resultType){
        this.resultType = resultType;
    }
    public String getResultType(){
        return this.resultType;
    }
    public void setActions(List<Actions> actions){
        this.actions = actions;
    }
    public List<Actions> getActions(){
        return this.actions;
    }
    public void setProperties(Properties properties){
        this.properties = properties;
    }
    public Properties getProperties(){
        return this.properties;
    }
    public void setExecuteCode(String executeCode){
        this.executeCode = executeCode;
    }
    public String getExecuteCode(){
        return this.executeCode;
    }
    public void setMsgInfo(String msgInfo){
        this.msgInfo = msgInfo;
    }
    public String getMsgInfo(){
        return this.msgInfo;
    }
}
