package com.db.common.enums;

/**
 * @Description: DataServer状态
 * @Author: qjc
 * @Date: 2023/5/11
 */
public enum DataServerStateEnum {
    PRIMARY(1, "主件机"),
    COPY(2, "副本机"),
    IDLE(0, "空闲中"),
    INVAILID(-1, "失效中"),
    ;

    /**
     * 数据类型编码
     */
    private final Integer code;

    /**
     * 数据类型名称
     */
    private final String type;

    DataServerStateEnum(int code, String type){
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
