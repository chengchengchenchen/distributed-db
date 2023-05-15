package com.db.common.utils;


import com.db.common.constant.ZKConstant;
import com.db.common.enums.ErrorCodeEnum;
import com.db.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 持有一个连接到ZooKeeper的Curator连接客户端
 * @Author: qjc
 * @Date: 2023/5/13
 */
@Slf4j
public class CuratorClient {

    /**
     * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
     */
    public ExecutorService pool = Executors.newFixedThreadPool(10);
    public CuratorFramework client = null;
    public String hostUrl = null;

    public CuratorClient() {
        this.setUpConnection(ZKConstant.ZOOKEEPER_HOST);
    }

    public CuratorClient(String hostUrl) {
        this.setUpConnection(hostUrl);
    }

    /**
     * 在特定Host中连接ZooKeeper
     */
    public void setUpConnection(String hostUrl) {
        this.hostUrl = hostUrl;

        if (client == null) {
            synchronized (this) {
                client = CuratorFrameworkFactory.builder()
                        .connectString(hostUrl)
                        .connectionTimeoutMs(ZKConstant.ZK_CONNECTION_TIMEOUT)
                        .sessionTimeoutMs(ZKConstant.ZK_SESSION_TIMEOUT)
                        .retryPolicy(new ExponentialBackoffRetry(ZKConstant.ZK_BASE_SLEEP_TIME, ZKConstant.ZK_MAX_RETRIES))
                        .build();
                client.start();
            }
        }
    }

    /**
     * 创建节点。默认为持久节点
     */
    public String createNode(String registerPath, String value) throws Exception {
        checkClientConnected();
        return client.create().creatingParentsIfNeeded().forPath(registerPath, value.getBytes());
    }

    /**
     * 创建节点
     */
    public String createNode(String path, String value, CreateMode nodeType) throws Exception {
        checkClientConnected();
        if (nodeType == null) {
            throw new BusinessException(ErrorCodeEnum.BASIC_VALIDATION_FAILED.getCode(), "创建节点类型不合法");
        } else {
            return client.create().creatingParentsIfNeeded().withMode(nodeType).forPath(path, value.getBytes());
        }
    }

    /**
     * 获取单个节点
     */
    public String getData(String path) throws Exception {
        checkClientConnected();
        return new String(client.getData().forPath(path));
    }

    /**
     * 获取子节点列表
     */
    public List<String> getChildren(String path) throws Exception {
        checkClientConnected();
        return client.getChildren().forPath(path);
    }

    /**
     * 检测节点是否存在
     */
    public boolean checkNodeExist(String path) throws Exception {
        checkClientConnected();
        Stat s = client.checkExists().forPath(path);
        return s != null;

    }

    /**
     * 监听数据节点的数据变化
     */
    public void monitorNode(String path, NodeCacheListener listener) throws Exception {
        final NodeCache nodeCache = new NodeCache(client, path, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(listener, pool);
    }

    /**
     * 监听子节点数据变化
     */
    public void monitorChildrenNodes(String path, PathChildrenCacheListener listener) throws Exception {
        final PathChildrenCache childrenCache = new PathChildrenCache(client, path, true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(listener, pool);
    }

    /**
     * 监听节点及子节点变化
     */
    public void listenNode(String path, CuratorCacheListener listener) throws Exception{
        CuratorCache curatorCache = CuratorCache.build(client, path);
        curatorCache.start();
        curatorCache.listenable().addListener(listener, pool);
    }

    private void checkClientConnected() {
        if (client == null) {
            this.setUpConnection(ZKConstant.ZOOKEEPER_HOST);
        }
    }
}

