package com.db.common.model;

public class TableMeta {

    public String name;

    public String primaryKey;

    public String locatedServerName;

    public String locatedServerUrl;

    //public List<Attribute> attributes;

    @Override
    public boolean equals(Object another){
        if (! (another instanceof TableMeta))
            return false;
        if(another == this)
            return true;
        return name.equals(((TableMeta) another).name) && primaryKey.equals(((TableMeta) another).primaryKey);
    }
}
