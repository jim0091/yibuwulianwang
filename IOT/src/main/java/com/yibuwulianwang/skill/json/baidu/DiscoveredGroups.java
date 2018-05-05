package com.yibuwulianwang.skill.json.baidu;

import java.util.ArrayList;
import java.util.List;
public class DiscoveredGroups
{
    private String groupName;

    private List<String> applianceIds;

    private String groupNotes;

    private AdditionalGroupDetails additionalGroupDetails;

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    public String getGroupName(){
        return this.groupName;
    }
    public void setApplianceIds(List<String> applianceIds){
        this.applianceIds = applianceIds;
    }
    public List<String> getApplianceIds(){
        return this.applianceIds;
    }
    public void setGroupNotes(String groupNotes){
        this.groupNotes = groupNotes;
    }
    public String getGroupNotes(){
        return this.groupNotes;
    }
    public void setAdditionalGroupDetails(AdditionalGroupDetails additionalGroupDetails){
        this.additionalGroupDetails = additionalGroupDetails;
    }
    public AdditionalGroupDetails getAdditionalGroupDetails(){
        return this.additionalGroupDetails;
    }
}

