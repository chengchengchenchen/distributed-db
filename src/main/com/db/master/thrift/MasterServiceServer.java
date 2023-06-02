package com.db.master.thrift;

import com.db.common.utils.rpc.DynamicThriftServer;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransportException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MasterServiceServer extends DynamicThriftServer {
    public MasterServiceServer(TProcessor processor, Integer port) throws TTransportException {
        super(processor, port);
    }

    public MasterServiceServer(TProcessor processor) throws TTransportException {
        super(processor);
    }

}
