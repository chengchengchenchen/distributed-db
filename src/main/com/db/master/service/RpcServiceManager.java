package com.db.master.service;
import com.db.common.constant.MasterConstant;
import com.db.master.thrift.MasterServiceServer;
import com.db.master.thrift.MasterServiceImpl;
import com.db.RPC.service.MasterService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcServiceManager {
    public void startService() {
        try {
            MasterServiceServer masterServiceServer = new MasterServiceServer(
                    new MasterService.Processor<>(new MasterServiceImpl()), MasterConstant.MASTER_SERVER_PORT);
            masterServiceServer.startServer();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
