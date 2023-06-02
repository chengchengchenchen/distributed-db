package com.db.region;

import com.db.region.service.RegionConfig;
import com.db.region.service.ZK;
import lombok.extern.slf4j.Slf4j;
import com.db.region.biz.RegionServiceManager;
import com.db.region.biz.RegionServerManager;
/**
 * @Description: Master服务启动入口
 * @Author: qjc
 * @Date: 2023/5/15
 */
@Slf4j
public class RegionRunner {
//    private static final ZK zkThread = new ZK();
//    /**
//     * Region Server
//     * 1. 启动时，与ZooKeeper建立连接，维持Session，以临时节点的形式把自己注册到Zookeeper的某节点下，并设置本机的IP和PORT
//     * 2. 当发生异常或主动断开连接时，与Session断开连接，该临时节点被删除。
//     * 3. BufferManager，收到RPC调用请求（这可能是来自于Client的数据请求，也有可能是Master执行策略时需要的请求）时进行相应调用并返回结果
//     */
//    public static void main(String[] args){
//        //初始化Region配置
//        RegionConfig.init();
//
//        // 线程1：在应用启动的时候自动将本机的Host信息注册到ZooKeeper，然后阻塞，直到应用退出的时候也同时退出
//        Thread zkServiceThread = new Thread(zkThread);
//        //log.warn("{},{}",ServerConstant.HOST_NAME,ServerConstant.HOST_URL);
//        zkServiceThread.start();
//
//        // 主线程：
//        //TODO: RPC---逻辑在RegionServerManager
//    }
        public static void main(String args[]) {
            RegionServerManager regionServerManager = new RegionServerManager();
            regionServerManager.run();
        }
}
