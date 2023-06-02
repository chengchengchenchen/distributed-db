package com.db.client;
import com.db.client.ReadIn;
import com.db.region.biz.RegionServerManager;

public class ClientRunner {
    //TODO：应该是把解释器跑起来？RPC纯请求没有持续开启需要（client不提供服务）
    public static void main(String args[]) {
        ReadIn.initial();
    }
}
