package com.db.common.model;

import com.db.common.constant.MasterConstant;
import com.db.common.enums.DataServerStateEnum;
import com.db.common.enums.ErrorCodeEnum;
import com.db.common.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;


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

    /**
     * 解析url:ip，如127.0.0.1:2345
     */
    public void parseHostUrl() {
        if (hostUrl == null || hostUrl.equals("")) {
            throw new BusinessException(ErrorCodeEnum.FAIL.getCode(), "URL不存在或不合法");
        } else if (!hostUrl.matches(HOST_URL_REGEX)) {
            throw new BusinessException(ErrorCodeEnum.FAIL.getCode(), "URL不存在或不合法");
        } else {
            ip = hostUrl.split(":")[0];
            port = Integer.parseInt(hostUrl.split(":")[1]);
        }
    }

    /**
     * 设置机器从失效状态中恢复
     */
    public void serverRecover() {
        state = DataServerStateEnum.IDLE;
        dualServerId = MasterConstant.NO_DUAL_SERVER;
        log.warn("服务器{}恢复", hostName);
    }

    /**
     * 设置机器为失效
     */
    public void serverInvalid() {
        state = DataServerStateEnum.INVAILID;
        dualServerId = MasterConstant.NO_DUAL_SERVER;
        log.warn("服务器{}失效", hostName);
    }

    /**
     * 设置机器为空闲
     */
    public void serverIdle() {
        state = DataServerStateEnum.IDLE;
        dualServerId = MasterConstant.NO_DUAL_SERVER;
        log.warn("服务器{}空闲", hostName);
    }

    /**
     * 和一台未结对的机器进行结对
     */
    public void makePair(DataServer server) {
        if (server.state != DataServerStateEnum.IDLE || this.state != DataServerStateEnum.IDLE) {
            throw new BusinessException(ErrorCodeEnum.BUSINESS_VALIDATION_FAILED.getCode(), "不能和不为空闲状态的机器结对");
        } else {
            this.state = DataServerStateEnum.PRIMARY;
            server.state = DataServerStateEnum.COPY;
            this.dualServerId = server.id;
            server.dualServerId = this.id;
            log.warn("{}的配偶是{}", this.hostName, server.hostName);
        }
    }

    /**
     * 和一台结对的机器进行结对
     */
    public void remakePair(DataServer server) {
        if (server.state == DataServerStateEnum.IDLE || server.state == DataServerStateEnum.INVAILID) {
            throw new BusinessException(ErrorCodeEnum.BUSINESS_VALIDATION_FAILED.getCode(), "不能和失效或者空闲状态的机器结对");
        } else if (server.dualServerId != MasterConstant.NO_DUAL_SERVER) {
            throw new BusinessException(ErrorCodeEnum.BUSINESS_VALIDATION_FAILED.getCode(), "不能和还处于结对状态的机器结对");
        } else {
            if (server.state == DataServerStateEnum.PRIMARY) {
                // 如果没有结对的机器是主件机，则让这台机器成为副本机
                this.state = DataServerStateEnum.COPY;
            } else if (server.state == DataServerStateEnum.COPY) {
                // 如果没有结对的机器是副本机，则让这台机器成为主件机
                this.state = DataServerStateEnum.PRIMARY;
            }
            // 设置dualServerId
            this.dualServerId = server.id;
            server.dualServerId = this.id;
            log.warn("{}的配偶是{}", this.hostName, server.hostName);
        }
    }

}
