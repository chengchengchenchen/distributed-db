package com.db.region.biz;

import com.db.RPC.service.RegionService;
import com.db.region.thrift.RegionServiceImpl;
import com.db.region.thrift.RegionServiceServer;
import lombok.extern.slf4j.Slf4j;

import com.db.region.service.RegionConfig;

@Slf4j
/**
 * @Description: Region RPC Server
 * @author: csc
 * @date: 2023/5/26
 */
public class RegionServiceManager {

    public void startService() {
        try {
            //通过具体Impl作为processor传入将类转化为实例并开始服务
            //修改了port
            RegionServiceServer regionServiceServer = new RegionServiceServer(
                    new RegionService.Processor<>(new RegionServiceImpl()), RegionConfig.port);
            regionServiceServer.startServer();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
