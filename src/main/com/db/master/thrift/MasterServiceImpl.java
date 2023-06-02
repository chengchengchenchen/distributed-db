package com.db.master.thrift;

import com.db.RPC.model.*;
import com.db.RPC.service.MasterService;
import com.db.common.enums.SqlQueryEnum;
import com.db.common.model.DataServer;
import com.db.common.model.DataServerManager;
import com.db.common.model.TableMeta;
import com.db.common.utils.RpcResult;
import lombok.extern.slf4j.Slf4j;

import static com.db.common.constant.ZKConstant.HOST_NAME_PREFIX;


@Slf4j
public class MasterServiceImpl implements MasterService.Iface {

    /**
     * Client根据表格名查询表格元数据，若不存在，则根据负载均衡返回一个Region主件机位置
     *
     * @param req
     */
    @Override
    public QueryMetaTableResponse queryTableMeta(QueryMetaTableRequest req) throws org.apache.thrift.TException {
        // 获取请求中的数据
        Base base = req.getBase();
        String name = req.getName();
        int type = req.getType();
        log.warn("接收到对于表格元数据查询请求头{}，目标表格{}，操作类型{}", base, name, type);


        if(type == SqlQueryEnum.SELECT.ordinal()){
            TableMeta tableMeta = DataServerManager.findTable(name);
            if(tableMeta != null){
                log.warn("查询表格地址{}",tableMeta.locatedServerName);
                return new QueryMetaTableResponse()
                        .setLocatedServerName(tableMeta.locatedServerName)
                        .setLocatedServerUrl(tableMeta.locatedServerUrl)
                        .setBaseResp(RpcResult.successResp());
            }else {
                log.warn("查询表格不存在");
                return new QueryMetaTableResponse()
                        .setLocatedServerName("null")
                        .setLocatedServerUrl("null")
                        .setBaseResp(RpcResult.notFoundResp());
            }
        }

        TableMeta tableMeta = DataServerManager.findPrimaryTable(name);
        if (tableMeta != null) {
            log.warn("查询到primary table{}",tableMeta);
            if(type == SqlQueryEnum.CREATE_TABLE.ordinal()){
                log.warn("创建表格已存在");
                return new QueryMetaTableResponse()
                        .setLocatedServerName("null")
                        .setLocatedServerUrl("null")
                        .setBaseResp(RpcResult.hasExistedResp());
            }else{
                return new QueryMetaTableResponse()
                        .setLocatedServerName(tableMeta.locatedServerName)
                        .setLocatedServerUrl(tableMeta.locatedServerUrl)
                        .setBaseResp(RpcResult.successResp());
            }
        } else {
            log.warn("未查询到primary table");
            if(type != SqlQueryEnum.CREATE_TABLE.ordinal()){
                log.warn("需要操作表格不存在");
                return new QueryMetaTableResponse()
                        .setLocatedServerName("null")
                        .setLocatedServerUrl("null")
                        .setBaseResp(RpcResult.notFoundResp());
            } else{
                //DataServer server = DataServerManager.allocateLocatedServer2();
                Integer serverId = DataServerManager.allocateLocatedServer();
                DataServer server = DataServerManager.getDataServerById(serverId);
                log.warn("选择服务器{}作为该表格存储的位置", server.hostName);
                return new QueryMetaTableResponse()
                        .setLocatedServerUrl(server.hostUrl)
                        .setLocatedServerName(server.hostName)
                        .setBaseResp(RpcResult.successResp());
            }
        }
    }

    /**
     * Region向Master通知TableMeta的修改
     *
     * @param req
     */
    @Override
    public NotifyTableMetaChangeResponse notifyTableMetaChange(NotifyTableMetaChangeRequest req) throws org.apache.thrift.TException {
        // 获取请求中的数据
        Base base = req.getBase();
        String name = req.getName();
        int type = req.getType();
        String regionName = req.getRegionName();
        String regionURL = req.getURL();
        log.warn("接收到对于表格元数据变更通知的请求头{}，目标表格{}，操作类型{}", base, name, type);
        TableMeta newTableMeta = new TableMeta(name,HOST_NAME_PREFIX+regionName,regionURL);
        log.warn("newTableMeta: {}",newTableMeta);

        if(type == SqlQueryEnum.CREATE_TABLE.ordinal()){
            //TableMeta tableMeta = DataServerManager.findPrimaryTable(name);
            if(DataServerManager.isTableMetaExisted(newTableMeta)){
                return new NotifyTableMetaChangeResponse().setBaseResp(RpcResult.hasExistedResp());
            }else{
                DataServerManager.addTableMata(newTableMeta);
                return new NotifyTableMetaChangeResponse().setBaseResp(RpcResult.successResp());
            }
        }else if(type == SqlQueryEnum.DROP_TABLE.ordinal()){
            if(!DataServerManager.isTableMetaExisted(newTableMeta)){
                return new NotifyTableMetaChangeResponse().setBaseResp(RpcResult.notFoundResp());
            }else{
                DataServerManager.removeTableMeta(newTableMeta);
                return new NotifyTableMetaChangeResponse().setBaseResp(RpcResult.successResp());
            }
        }else{
            log.warn("错误请求{}", type);
            return new NotifyTableMetaChangeResponse().setBaseResp(RpcResult.failResp());
        }
    }

}
