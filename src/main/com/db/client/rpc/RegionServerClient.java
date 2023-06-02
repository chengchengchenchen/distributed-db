package com.db.client.rpc;


import com.db.RPC.model.*;
import com.db.RPC.service.RegionService;
import com.db.common.utils.rpc.DynamicThriftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
//向region查询和操作表格的消息客户端
@Slf4j
public class RegionServerClient extends DynamicThriftClient<RegionService.Client> {

    public RegionServerClient(Class<RegionService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public RegionServerClient(Class<RegionService.Client> ts) {
        super(ts);
    }

    public QueryTableDataResponse queryTableData(QueryTableDataRequest queryTableDataRequest) throws TException{
        QueryTableDataResponse queryTableDataResponse = client.queryTableData(queryTableDataRequest);
        return queryTableDataResponse;
    }

    public ChangeTableDataResponse changeTableData(ChangeTableDataRequest changeTableDataRequest) throws TException{
        ChangeTableDataResponse changeTableDataResponse = client.changeTableData(changeTableDataRequest);
        return changeTableDataResponse;
    }

}
