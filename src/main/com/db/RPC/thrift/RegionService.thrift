include "Operation.thrift"

service com.db.RPC.service.RegionService {

    /**
     * Client根据表格名查询表格实际数据
     */
    Operation.QueryTableDataResponse queryTableData(1: Operation.QueryTableDataRequest req),

    /**
     * Client修改表格数据
     */
    Operation.ChangeTableDataResponse changeTableData(1: Operation.ChangeTableDataRequest req),

    /**
     * Master告知Region状态变化
     */
    Operation.NotifyStateResponse notifyStateChange(1: Operation.NotifyStateRequest req),

    /**
     * Region对table进行全量复制
     */
    Operation.ExecTableCopyResponse execTableCopy(1: Operation.ExecTableCopyRequest req),

    /**
     * Master通知Region进行数据复制
     */
    Operation.StartSchemaCopyResponse startSchemaCopy(1: Operation.StartSchemaCopyRequest req)

    /**
     * Region对schema进行全量复制
     */
    Operation.ExecSchemaCopyResponse execSchemaCopy(1: Operation.ExecSchemaCopyRequest req)
}