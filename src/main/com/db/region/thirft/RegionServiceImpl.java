package com.db.region.thirft;

import com.db.RPC.model.*;
import com.db.RPC.service.RegionService;

public class RegionServiceImpl implements RegionService.Iface {

    /**
     * Client根据表格名查询表格实际数据
     *
     * @param req
     */
    @Override
    public QueryTableDataResponse queryTableData(QueryTableDataRequest req) throws org.apache.thrift.TException {
        return null;
    }

    /**
     * Client修改表格数据
     *
     * @param req
     */
    @Override
    public ChangeTableDataResponse changeTableData(ChangeTableDataRequest req) throws org.apache.thrift.TException {
        return null;
    }

    /**
     * Master告知Region状态变化
     *
     * @param req
     */
    @Override
    public NotifyStateResponse notifyStateChange(NotifyStateRequest req) throws org.apache.thrift.TException {
        return null;
    }

    /**
     * Region对table进行全量复制
     *
     * @param req
     */
    @Override
    public ExecTableCopyResponse execTableCopy(ExecTableCopyRequest req) throws org.apache.thrift.TException {
        return null;
    }
}
