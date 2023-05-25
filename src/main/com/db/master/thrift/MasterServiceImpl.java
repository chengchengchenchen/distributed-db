package com.db.master.thrift;

import com.db.RPC.model.NotifyTableMetaChangeRequest;
import com.db.RPC.model.NotifyTableMetaChangeResponse;
import com.db.RPC.model.QueryMetaTableRequest;
import com.db.RPC.model.QueryMetaTableResponse;
import com.db.RPC.service.MasterService;

public class MasterServiceImpl implements MasterService.Iface {

    /**
     * Client根据表格名查询表格元数据，若不存在，则根据负载均衡返回一个Region主件机位置
     *
     * @param req
     */
    @Override
    public QueryMetaTableResponse queryTableMeta(QueryMetaTableRequest req) throws org.apache.thrift.TException {
        return null;
    }

    /**
     * Region向Master通知TableMeta的修改
     *
     * @param req
     */
    @Override
    public NotifyTableMetaChangeResponse notifyTableMetaChange(NotifyTableMetaChangeRequest req) throws org.apache.thrift.TException {
        return null;
    }

}
