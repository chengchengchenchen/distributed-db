package com.db.region.thrift;

import com.db.RPC.service.MasterService;
import com.db.common.utils.rpc.DynamicThriftClient;
import com.db.RPC.model.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

@Slf4j
public class MasterServerClient extends DynamicThriftClient<MasterService.Client> {
    public MasterServerClient(Class<MasterService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public MasterServerClient(Class<MasterService.Client> ts) {
        super(ts);
    }

    public NotifyTableMetaChangeResponse notifyTableMetaChange(NotifyTableMetaChangeRequest notifyTableMetaChangeRequest) throws TException {
        return client.notifyTableMetaChange(notifyTableMetaChangeRequest);
    }
}
