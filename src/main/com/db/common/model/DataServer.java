package com.db.common.model;

import com.db.common.enums.DataServerStateEnum;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

/**
 * @Description: DataServer基本数据结构
 * @Author: qjc
 * @Date: 2023/5/11
 */

@Slf4j
public class DataServer implements Serializable {
    public Integer id;
    public String ip;
    public Integer port;
    public String hostName;
    public String hostUrl;
    public DataServerStateEnum state;
    public Integer dualServerId;

    private static final String HOST_URL_REGEX = "(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)";
    @Test
    public void parseHostUrl() {
       System.out.println("ok");
    }
}
