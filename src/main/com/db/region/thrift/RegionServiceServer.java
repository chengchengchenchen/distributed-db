package com.db.region.thrift;

//调用抽象类构造函数将抽象类转化为特定类
import com.db.common.utils.rpc.DynamicThriftServer;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransportException;

public class RegionServiceServer extends DynamicThriftServer {
    public RegionServiceServer(TProcessor processor) throws TTransportException {
        super(processor);
    }

    public RegionServiceServer(TProcessor processor, Integer port) throws TTransportException {
        super(processor, port);
    }
}
