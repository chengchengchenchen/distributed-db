package com.db.client;

import com.db.RPC.model.Base;
import com.db.RPC.model.ChangeTableDataRequest;
import com.db.RPC.model.QueryMetaTableRequest;
import com.db.RPC.model.QueryMetaTableResponse;
import com.db.RPC.service.MasterService;
import com.db.RPC.service.RegionService;
import com.db.client.rpc.MasterServerClient;
import com.db.common.constant.MasterConstant;
import com.db.common.constant.ServerConstant;
import com.db.common.enums.DataServerStateEnum;
import com.db.common.enums.RpcResultCodeEnum;
import com.db.common.enums.SqlQueryEnum;
import com.db.common.exception.BusinessException;
import com.db.common.model.DataServer;
import com.db.client.rpc.*;
import com.db.common.utils.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
@Slf4j
//保存对应表格region位置信息，目前是每次执行select或改变表格时都查询（未缓存）
public class LocatedServer {
    public String locatedServerName;
    public String locatedServerUrl;
    public String locatedServerIp;
    public int locatedServerPort;
    public void getTableLocation(String tableName, int type) throws BusinessException {
        QueryMetaTableRequest queryMetaTableRequest = new QueryMetaTableRequest()
                .setName(tableName)
                .setType(type)
                .setBase(new Base()
                        .setCaller("Client")
                        .setReceiver(MasterConstant.MASTER_HOST_NAME));
        MasterServerClient masterServerClient = new MasterServerClient(MasterService.Client.class,MasterConstant.MASTER_SERVER_IP,MasterConstant.MASTER_SERVER_PORT);
        try {
            QueryMetaTableResponse queryMetaTableResponse = masterServerClient.queryTableMeta(queryMetaTableRequest);
            if(queryMetaTableResponse.getBaseResp().getCode() == 1){
                log.warn("不存在该表格");
            }
            else if(queryMetaTableResponse.getBaseResp().getCode() == -1){
                log.warn("查询表格所在位置失败");
            }
            else if(queryMetaTableResponse.getBaseResp().getCode() == 2){
                log.warn("该表格已存在");
            }
            else{
                this.locatedServerName = queryMetaTableResponse.getLocatedServerName();
                this.locatedServerUrl = queryMetaTableResponse.getLocatedServerUrl();
                DataServer dataServer = DataServer.builder().hostUrl(this.locatedServerUrl).build();
                dataServer.parseHostUrl();
                this.locatedServerIp=dataServer.getIp();
                this.locatedServerPort= dataServer.getPort();
                if(type == SqlQueryEnum.CREATE_TABLE.ordinal()){
                    log.warn("创建table的目标Region：{},URL:{}",this.locatedServerName,this.locatedServerUrl);
                } else {
                    log.warn("查询到table存在于Region：{},URL:{}",this.locatedServerName,this.locatedServerUrl);
                }
            }
        } catch (TException e) {
            log.warn("向master查询tableMeta失败");
        }
    }


}
