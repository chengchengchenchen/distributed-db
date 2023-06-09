package com.db.master;

import com.db.master.service.Executor;
import com.db.master.service.RpcServiceManager;
import com.db.master.service.ZK;

import java.util.Scanner;

import static java.lang.Thread.sleep;

public class MasterRunner {
    /**
     * Master Server
     * 需要承担的工作有：
     * 1. 启动时，和ZooKeeper集群进行通信，加载对应节点数据，获取了目前所有能提供服务的数据服务器列表，并且加载到内存中
     * 2. 在1完成后，与每个RegionServer进行通信，获取到每个服务器上保存的表格元数据，并储存到本地
     * 3. 一直监听节点，当节点内容发生变化，即某Server上线或者某Server下线时，获得通知，重新加载节点数据，刷新内存中数据服务器列表
     * 4. 接收客户端的请求，返回需要的元数据
     * 5. (Optional)在3中获得消息后，与相应的服务器进行联系，执行容错容灾、副本复制等策略
     * 解决方案设计：
     * 设计两个线程，第一个线程在启动时向ZooKeeper发送请求，获得ZNODE目录下的信息并且持续监控，如果发生了目录的变化则执行回调函数，处理相应策略。
     * 策略主要包含两个方面，分别是3、5。第二个线程负责监听RPC端口，接收客户端的请求，返回需要的元数据。
     */
    private static final ZK zkThread = new ZK();

    public static void main(String[] args){
        // 第一个线程在启动时向ZooKeeper发送请求，获得ZNODE目录下的信息并且持续监控，如果发生了目录的变化则执行回调函数，处理相应策略。
        try{
            Thread zkServiceThread = new Thread(zkThread);
            zkServiceThread.start();
            // 主线程：
            //TODO: RPC
            RpcServiceManager rpcServiceManage = new RpcServiceManager();
            rpcServiceManage.startService();

            String a="";
            while(!a.equals("quit")){
                Scanner scanner = new Scanner(System.in);
                a =scanner.next ();// 输入
                if(a.equals("quit")){
                    zkServiceThread.interrupt();
                    sleep(1000);
                }
            }
            System.exit(0);
        }catch (Exception e){
            e.getStackTrace();
        }





    }
}
