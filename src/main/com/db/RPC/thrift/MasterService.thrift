include "Operation.thrift"

service MasterService {

    /**
     * Client根据表格名查询表格元数据，若不存在，则根据负载均衡返回一个Region主件机位置
     */
    Operation.QueryMetaTableResponse queryTableMeta(1: Operation.QueryMetaTableRequest req),

    /**
     * Region向Master通知TableMeta的修改
     */
    Operation.NotifyTableMetaChangeResponse notifyTableMetaChange(1: Operation.NotifyTableMetaChangeRequest req),

}