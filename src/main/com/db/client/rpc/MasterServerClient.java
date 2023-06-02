package com.db.client.rpc;

//向master请求表格所在region的消息客户端
import com.db.RPC.model.*;
import com.db.RPC.service.MasterService;
import com.db.RPC.service.RegionService;
import com.db.common.utils.rpc.DynamicThriftClient;
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

    public QueryMetaTableResponse queryTableMeta(QueryMetaTableRequest queryMetaTableRequest) throws TException{
        QueryMetaTableResponse queryMetaTableResponse = client.queryTableMeta(queryMetaTableRequest);
        log.warn(queryMetaTableResponse.getBaseResp().toString());
        return queryMetaTableResponse;
    }

}
