package com.db.common.model;

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
        return name.equals(((TableMeta) another).name);
    }
}
