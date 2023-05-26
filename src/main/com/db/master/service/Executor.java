package com.db.master.service;

import com.db.common.constant.MasterConstant;
import com.db.common.enums.DataServerStateEnum;
import com.db.common.enums.ErrorCodeEnum;
import com.db.common.enums.StrategyTypeEnum;
import com.db.common.exception.BusinessException;
import com.db.common.model.DataServer;
import com.db.common.model.DataServerManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.io.IOException;
import java.lang.ref.Cleaner;

/**
 * @Description: 根据ZooKeeper监视器得到的各种事件实行相应的策略
 * @Author: qjc
 * @Date: 2023/5/15 15:50
 */
@Slf4j
public class Executor implements AutoCloseable {

    private static final Cleaner cleaner = Cleaner.create();

    private final Cleaner.Cleanable cleanable;

    public Executor() {
        try {
            //log.warn("读取");
            DataServerManager.read();
        } catch (IOException | ClassNotFoundException e) {
            log.warn(e.getMessage(), e);
        }
        // register a cleaning action
        cleanable = cleaner.register(this, () -> {
            try {
                //log.warn("写入");
                DataServerManager.write();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }
        });
    }

    @Override
    public void close() {
        // invoke the cleaning action
        cleanable.clean();
    }

    /**
     * 根据类型执行不同的策略
     */
    public void execStrategy(DataServer server, StrategyTypeEnum type) {
        try {
            switch (type) {
                case RECOVER -> execRecoverStrategy(server);
                case DISCOVER -> execDiscoverStrategy(server);
                case INVALID -> execInvalidStrategy(server);
            }
        } catch (TException e) {
            log.warn(e.getMessage(), e);
        }
    }

    private void execInvalidStrategy(DataServer server) throws TException {
        if (server.state == DataServerStateEnum.PRIMARY || server.state == DataServerStateEnum.COPY) {
            // 该机器为主件机或者副本机
            if (DataServerManager.getIdleServerNum() == 0) {
                // 没有备用机器，非正常状态
                DataServer dualServer = DataServerManager.getDataServerById(server.dualServerId);
                if (dualServer == null) {
                    throw new BusinessException(ErrorCodeEnum.FAIL.getCode(), "主件机或副件机没有配偶");
                } else {
                    dualServer.dualServerId = MasterConstant.NO_DUAL_SERVER;
                    server.serverInvalid();
                    log.warn("服务器数量不足");
                    log.warn("服务器失效后没有备用机器，系统非正常状态");
                }
            } else {
                // 有备用机器，进行一次结对
                // 孤单的服务器
                DataServer singleServer = DataServerManager.getDataServerById(server.dualServerId);
                singleServer.dualServerId = MasterConstant.NO_DUAL_SERVER;
                // 闲置的服务器
                DataServer idleServer = DataServerManager.getIdleServer(null);
                DataServerManager.remakeServerPair(idleServer, singleServer);

                server.serverInvalid();
                log.warn("服务器{}和服务器{}重新完成结对", idleServer.hostName, singleServer.hostName);
            }
        } else if (server.state == DataServerStateEnum.IDLE) {
            if (DataServerManager.getIdleServerNum() > 1) {
                // 当前不只有一台空闲机，直接失效
                server.serverInvalid();
            } else {
                server.serverInvalid();
                throw new BusinessException(ErrorCodeEnum.FAIL.getCode(), "空闲机数量不足，系统非正常状态");
            }
        } else {
            server.serverInvalid();
            throw new BusinessException(ErrorCodeEnum.FAIL.getCode(), "失效的服务器原本的状态不合法");
        }
    }

    private void execRecoverStrategy(DataServer server) throws TException {
        server.serverRecover();
        execDiscoverStrategy(server);
    }

    private void execDiscoverStrategy(DataServer server) throws TException {
        DataServer singleServer = DataServerManager.getSingleServer();
        if (DataServerManager.getValidServerNum() < MasterConstant.MIN_VALID_DATA_SERVER) {
            // 如果目前能够运行的服务器小于3台，提示无法完成服务
            log.warn("目前数据服务器数量少于{}，服务无法完成", MasterConstant.MIN_VALID_DATA_SERVER);

            if (singleServer == null) {
                // 没有机器不成对存在，设置这台机器成为备用机器
                server.serverIdle();
                log.warn("当前没有机器不成对存在，服务器{}成为备用机器", server.hostName);
            } else {
                // 和未结对的机器进行结对
                DataServerManager.remakeServerPair(server, singleServer);
                log.warn("服务器{}和服务器{}重新结对", server.hostName, singleServer.hostName);
            }
            log.warn("目前数据服务器数量不足,服务处于非正常状态");
        } else {
            // 服务器数量足够
            // 如果目前数据服务器不成对存在，与不成对的那一台服务器结对
            log.warn("当前服务器数量充足，有{}台，其中有效的服务器有{}台", DataServerManager.dataServers.size(), DataServerManager.getValidServerNum());

            if (singleServer != null) {
                // 发现有服务器不成对存在，与未结对的机器进行结对
                DataServerManager.remakeServerPair(server, singleServer);
                log.warn("服务器{}和服务器{}重新结对", server.hostName, singleServer.hostName);
            } else {
                // 没有服务器不成对存在，成为备用服务器
                server.state = DataServerStateEnum.IDLE;
                log.warn("服务器{}被设置为备用服务器", server);
                if (DataServerManager.getIdleServerNum() > MasterConstant.REALLOCATE_PAIR_LOW_BOUND) {
                    // 备用机数量过多，可以重新分配一对备用机进行结对
                    DataServerManager.makeServerPairFromIdleServer(server);
                }
            }
        }
    }
}

