package com.db.region.biz;

import com.db.region.service.RegionConfig;
import lombok.extern.slf4j.Slf4j;
import com.db.region.service.ZK;
/**
 * @Description: Region Server 主业务逻辑入口
 * @author: csc
 * @date: 2023/5/26
 */
@Slf4j
public class RegionServerManager {

    private RegionServiceManager regionServiceManager;


    private ZK zkServiceManager;

    public RegionServerManager() {
        regionServiceManager = new RegionServiceManager();
        zkServiceManager = new ZK();
    }

    public void run() {
        RegionConfig.init();
        // 线程1：在应用启动的时候自动将本机的Host信息注册到ZooKeeper，然后阻塞，直到应用退出的时候也同时退出
        Thread zkServiceThread = new Thread(zkServiceManager);
        zkServiceThread.start();

        // 主线程：
        regionServiceManager.startService();
    }
}
