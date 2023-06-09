package com.db.common.utils.rpc;

import com.db.common.constant.MasterConstant;
import com.db.common.enums.ErrorCodeEnum;
import com.db.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

/**
 * @Description: 抽象类，Thrift服务端需要继承这个类
 * @author: csc
 * @date: 2023/05/25
 */

@Slf4j
public abstract class DynamicThriftServer {

    protected TProcessor processor = null;

    protected TServer server = null;

    protected Integer port = null;

    public TServer getServer() {
        return server;
    }

    public Integer getPort() {
        return port;
    }

    /**
     *
     * @param processor
     * @param port
     * @throws TTransportException
     */
    public DynamicThriftServer(TProcessor processor, Integer port) throws TTransportException {
        initServer(processor, port);
    }

    /**
     *
     * @param processor
     * @throws TTransportException
     */
    public DynamicThriftServer(TProcessor processor) throws TTransportException {
        initServer(processor, MasterConstant.MASTER_SERVER_PORT);
    }

    /**
     * 在指定的端口上初始化特定类型的服务端
     *
     * @param processor
     * @param port
     */
    private void initServer(TProcessor processor, Integer port) {
        try {
            this.port = port;

            TServerTransport transport = new TServerSocket(port);

            // 生成server对象
            server = new TThreadPoolServer(new TThreadPoolServer.Args(transport)
                    .processor(processor)
                    .maxWorkerThreads(100)
                    .minWorkerThreads(5)
                    .protocolFactory(new TBinaryProtocol.Factory())
                    .transportFactory(new TTransportFactory()));
            log.warn("RPC服务端创建：{}", processor.getClass());
        } catch (TTransportException e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 启动客户端，开始监听端口
     */
    public void startServer() {
        if(server == null) {
            throw new BusinessException(ErrorCodeEnum.FAIL.getCode(), "服务端还未初始化");
        } else {
            server.serve();
            log.warn("RPC服务端创建：{} 完毕，并且开始监听端口 {}", processor.getClass(), port);
        }
    }
}
