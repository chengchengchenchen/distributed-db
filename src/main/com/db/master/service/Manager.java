package com.db.master.service;

import com.db.common.enums.ErrorCodeEnum;
import com.db.common.enums.StrategyTypeEnum;
import com.db.common.exception.BusinessException;
import com.db.common.model.DataServer;
import com.db.common.model.DataServerManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 根据ZooKeeper监视器得到的各种事件实行相应的策略
 * @Author: qjc
 * @Date: 2023/5/15 15:50
 */
@Slf4j
public class Manager {

    Executor executor;

    public Manager() {
        executor = new Executor();
    }

    /**
     * 处理服务器节点出现事件
     */
    public void eventServerAppear(String hostName, String hostUrl) {
        log.warn("新增服务器节点：主机名 {}, 地址 {}", hostName, hostUrl);
        DataServer server;
        if (DataServerManager.dataServers.get(hostName) != null) {
            // 该服务器已经存在，即从失效状态中恢复
            server = DataServerManager.dataServers.get(hostName);
            log.warn("对该服务器{}执行恢复策略", hostName);
            executor.execStrategy(server, StrategyTypeEnum.RECOVER);
        } else {
            // 新发现的服务器，新增一份数据
            DataServerManager.addServer(hostName, hostUrl);
            server = DataServerManager.dataServers.get(hostName);
            log.warn("对该服务器{}执行新增策略", hostName);
            executor.execStrategy(server, StrategyTypeEnum.DISCOVER);
        }
    }

    /**
     * 处理服务器节点失效事件
     */
    public void eventServerDisappear(String hostName) {
        log.warn("删除服务器节点：主机名 {}", hostName);
        DataServer server;
        if (DataServerManager.dataServers.get(hostName) == null) {
            throw new BusinessException(ErrorCodeEnum.FAIL.getCode(), "需要删除信息的服务器不存在于服务器列表中");
        } else {
            // 更新并处理下线的服务器
            server = DataServerManager.dataServers.get(hostName);
            log.warn("对该服务器{}执行失效策略", hostName);
            executor.execStrategy(server, StrategyTypeEnum.INVALID);
        }
    }

    /**
     * 处理服务器节点更新事件
     */
    public void eventServerUpdate(String hostName, String hostUrl) {
        log.warn("更新服务器节点：主机名 {}, 地址 {}", hostName, hostUrl);
        DataServer server;
        if (DataServerManager.dataServers.get(hostName) == null) {
            throw new BusinessException(ErrorCodeEnum.FAIL.getCode(), "需要更新信息的服务器不存在于服务器列表中");
        } else {
            // 更新服务器的URL
            server = DataServerManager.dataServers.get(hostName);
            server.hostUrl = hostUrl;
        }
    }
}