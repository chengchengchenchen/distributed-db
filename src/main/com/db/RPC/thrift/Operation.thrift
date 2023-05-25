include "Base.thrift"
include "FileData.thrift"
include "ResultSetData.thrift"

/**
 * 请求体：向Master请求获取tableMeta
 */
struct com.db.RPC.model.QueryMetaTableRequest {
    1: required string name,        // table的名字
    2: required i32 type            // 操作类型
    255: required Base.Base base
}

/**
 * 响应体：Master返回tableMeta所在region
 */
struct com.db.RPC.model.QueryMetaTableResponse {
    1: required string locatedServerName,
    2: required string locatedServerUrl,
    255: required Base.BaseResp baseResp
}

/**
 * 请求体：Client向Region请求获取tableData, select语句
 */
struct com.db.RPC.model.QueryTableDataRequest {
    1: required string sql,
    255: required Base.Base base
}

/**
 * 响应体：Region返回ResultSetData
 */
struct com.db.RPC.model.QueryTableDataResponse {
    1: required ResultSetData.ResultSetData com.db.RPC.model.ResultSetData,
    255: required Base.BaseResp baseResp
}

/**
 * 请求体：向Region请求修改tableData, insert, update, delete, create, drop语句
 */
struct com.db.RPC.model.ChangeTableDataRequest {
    1: required string sql,
    2: required string name,        //table name
    3: required i32 type,           //操作类型
    255: required Base.Base base
}

/**
 * 响应体：Region返回修改结果
 */
struct com.db.RPC.model.ChangeTableDataResponse {
    1: required i32 result,         //影响行数
    255: required Base.BaseResp baseResp
}

/**
 * Region告知Master TableMeta变化请求
 */
struct com.db.RPC.model.NotifyTableMetaChangeRequest {
    1: optional string name,        //table name
    2: optional i32 type,           //create || drop
    255: required Base.Base base
}

/**
 * Region告知Master TableMeta变化响应
 */
struct com.db.RPC.model.NotifyTableMetaChangeResponse {
    255: required Base.BaseResp baseResp
}

/**
 * Master告知Region当前状态请求
 */
struct com.db.RPC.model.NotifyStateRequest {
    1: optional i32 stateCode,
    2: optional string dualServerName,
    3: optional string dualServerUrl,
    255: required Base.Base base
}

/**
 * Master告知Region当前状态响应
 */
struct com.db.RPC.model.NotifyStateResponse {
    255: required Base.BaseResp baseResp
}

/**
 * Region主件机向副本机发送table复制
 */
struct com.db.RPC.model.ExecTableCopyRequest {
    1: required string name         //table name
    2: required FileData.FileData fileData,     //文件数据
    255: required Base.Base base
}

/**
 * Region副本机table复制响应
 */
struct com.db.RPC.model.ExecTableCopyResponse {
    255: required Base.BaseResp baseResp
}