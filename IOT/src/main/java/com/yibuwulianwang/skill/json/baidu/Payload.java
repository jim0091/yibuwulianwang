package com.yibuwulianwang.skill.json.baidu;

import java.util.ArrayList;
import java.util.List;
public class Payload
{
    private List<DiscoveredAppliances> discoveredAppliances;

    private List<DiscoveredGroups> discoveredGroups;

    public void setDiscoveredAppliances(List<DiscoveredAppliances> discoveredAppliances){
        this.discoveredAppliances = discoveredAppliances;
    }
    public List<DiscoveredAppliances> getDiscoveredAppliances(){
        return this.discoveredAppliances;
    }
    public void setDiscoveredGroups(List<DiscoveredGroups> discoveredGroups){
        this.discoveredGroups = discoveredGroups;
    }
    public List<DiscoveredGroups> getDiscoveredGroups(){
        return this.discoveredGroups;
    }
}
