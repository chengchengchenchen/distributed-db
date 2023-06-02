package com.db.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@NoArgsConstructor
public class TableMeta {

    public String name;

    public String locatedServerName;

    public String locatedServerUrl;

    //public List<Attribute> attributes;
    public TableMeta(String name, String locatedServerName, String locatedServerUrl){
        this.name=name;
        this.locatedServerName=locatedServerName;
        this.locatedServerUrl=locatedServerUrl;
    }
    @Override
    public boolean equals(Object another){
        if (! (another instanceof TableMeta))
            return false;
        if(another == this)
            return true;
        return name.equals(((TableMeta) another).name) && locatedServerName.equals(((TableMeta) another).locatedServerName) &&  locatedServerUrl.equals(((TableMeta) another).locatedServerUrl);
    }
}
