package com.db.master.service;

import com.db.common.constant.ZKConstant;
import com.db.common.utils.CuratorClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;


/**
 * @Description: ZooKeeper Service
 * @Author: qjc
 * @Date: 2023/5/16 21:02
 */
@Slf4j
public class ZK implements Runnable {
    @Override
    public void run() {
        try (Manager manager = new Manager()){
            // 向ZooKeeper注册临时节点
            CuratorClient client = new CuratorClient(ZKConstant.ZOOKEEPER_HOST);
            log.warn("服务器目录下有子节点：" + client.getChildren(ZKConstant.ZNODE));

            // 开始监听服务器目录，如果有节点的变化，则处理相应事件
            client.monitorChildrenNodes(ZKConstant.ZNODE, new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client_, PathChildrenCacheEvent event) throws Exception {
                    //String eventPath = event.getData() != null ? event.getData().getPath() : null;
                    //String path = eventPath.replaceFirst(ZKConstant.ZNODE + "/", "");

                    // 判断参数是否为空
                    if (client_ == null || event == null) {
                        return;
                    }
                    // 判断事件数据是否为空
                    if (event.getData() == null) {
                        return;
                    }
                    // 判断事件路径是否为空或者是否以 ZKConstant.ZNODE + "/" 开头
                    String eventPath = event.getData().getPath();
                    if (eventPath == null || !eventPath.startsWith(ZKConstant.ZNODE + "/")) {
                        return;
                    }
                    // 获取事件路径中的子节点名称
                    String path = eventPath.replaceFirst(ZKConstant.ZNODE + "/", "");

                    // 接收到事件，对事件类型进行判断并执行相应策略
                    switch (event.getType()) {
                        case CHILD_ADDED -> {
                            log.warn("服务器目录新增节点: " + event.getData().getPath());
                            manager.eventServerAppear(path, client.getData(eventPath));
                        }
                        case CHILD_REMOVED -> {
                            log.warn("服务器目录删除节点: " + event.getData().getPath());
                            manager.eventServerDisappear(path);
                        }
                        case CHILD_UPDATED -> {
                            log.warn("服务器目录更新节点: " + event.getData().getPath());
                            manager.eventServerUpdate(path, client.getData(eventPath));
                        }
                        default -> {
                        }
                    }
                }
            });

            // 阻塞该线程，直到发生异常或者主动退出
            synchronized (this) {
                wait();
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
