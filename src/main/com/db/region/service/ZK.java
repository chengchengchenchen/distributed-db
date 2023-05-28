package com.db.region.service;

import com.db.common.constant.MasterConstant;
import com.db.common.constant.ZKConstant;
import com.db.common.utils.CuratorClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;

/**
 * @Description: ZooKeeper Service
 * @Author: qjc
 * @Date: 2023/5/15 11:02
 */
@Slf4j
public class ZK implements Runnable {
    @Override
    public void run() {
        try {
            // 向ZooKeeper注册临时节点
            CuratorClient curatorClientHolder = new CuratorClient(MasterConstant.MASTER_SERVER_IP+":2181");
            curatorClientHolder.createNode(ZKConstant.ZNODE + "/" + ZKConstant.HOST_NAME_PREFIX + RegionConfig.hostName,
                    RegionConfig.hostURL,
                    CreateMode.EPHEMERAL);

            // 阻塞该线程，直到发生异常或者主动退出
            synchronized (this) {
                wait();
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
