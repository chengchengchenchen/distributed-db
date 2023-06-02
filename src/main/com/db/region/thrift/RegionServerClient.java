package com.db.region.thrift;


import com.db.RPC.model.*;
import com.db.RPC.service.RegionService;
import com.db.common.utils.rpc.DynamicThriftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
@Slf4j
public class RegionServerClient extends DynamicThriftClient<RegionService.Client> {

    public RegionServerClient(Class<RegionService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public RegionServerClient(Class<RegionService.Client> ts) {
        super(ts);
    }

    public void execTableCopy(ExecTableCopyRequest execTableCopyRequest) throws TException{
        ExecTableCopyResponse execTableCopyResponse = client.execTableCopy(execTableCopyRequest);
    }

    public void changeTableData(ChangeTableDataRequest changeTableDataRequest) throws TException{
        ChangeTableDataResponse changeTableDataResponse = client.changeTableData(changeTableDataRequest);
        log.warn(changeTableDataResponse.getBaseResp().toString());
    }

    public void execSchemaCopy(ExecSchemaCopyRequest execSchemaCopyRequest) throws TException{
        ExecSchemaCopyResponse execSchemaCopyResponse = client.execSchemaCopy(execSchemaCopyRequest);
        log.warn(execSchemaCopyResponse.getBaseResp().toString());
    }
}
