package com.db.master.thrift;

import com.db.RPC.model.NotifyStateRequest;
import com.db.RPC.model.NotifyStateResponse;
import com.db.RPC.service.RegionService;
import com.db.RPC.model.ExecSchemaCopyRequest;
import com.db.common.model.DataServer;
import com.db.common.utils.rpc.DynamicThriftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import com.db.RPC.model.StartSchemaCopyRequest;
import com.db.RPC.model.StartSchemaCopyResponse;

import java.util.List;
@Slf4j
public class RegionServerClient extends DynamicThriftClient<RegionService.Client> {
    public RegionServerClient(Class<RegionService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public RegionServerClient(Class<RegionService.Client> ts) {
        super(ts);
    }

    public void notifyStateChange(NotifyStateRequest notifyStateRequest) throws TException {
        NotifyStateResponse notifyStateResponse = client.notifyStateChange(notifyStateRequest);
    }

    public void execSchemaCopy(ExecSchemaCopyRequest execSchemaCopyRequest) throws TException {
        client.execSchemaCopy(execSchemaCopyRequest);
    }

    public void startSchemaCopy(StartSchemaCopyRequest startSchemaCopyRequest) throws TException{
        client.startSchemaCopy(startSchemaCopyRequest);
    }

}
