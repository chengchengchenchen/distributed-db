package com.db.common.model;

import com.db.common.constant.MasterConstant;
import com.db.common.enums.DataServerStateEnum;
import com.db.common.enums.ErrorCodeEnum;
import com.db.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class DataServerManager {

    public static Map<String, DataServer> dataServers = new HashMap<>();

    public static List<TableMeta> tableMetaList = new LinkedList<>();

    public static void addServer(String hostName, String hostUrl) {
        DataServer dataServer = new DataServer();
        dataServer.hostName = hostName;
        dataServer.hostUrl = hostUrl;
        dataServer.id = dataServers.size();
        dataServer.state = DataServerStateEnum.IDLE;
        dataServer.dualServerId = MasterConstant.NO_DUAL_SERVER;
        dataServer.parseHostUrl();

        dataServers.put(hostName, dataServer);
    }

    public static void read() throws IOException, ClassNotFoundException {
        File file = new File(MasterConstant.META_INFO_STORAGE_FILE);
        if (!file.exists()) {
            dataServers = new HashMap<>();
            log.warn("服务器数据存储文件不存在，已重新为存储对象进行内存分配");
            return;
        }
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        dataServers = (Map<String, DataServer>) in.readObject();
        tableMetaList = (List<TableMeta>) in.readObject();
        log.warn("从文件{}中读取对象{}和{}", MasterConstant.META_INFO_STORAGE_FILE, dataServers, tableMetaList);
        in.close();
    }

    public static void write() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(MasterConstant.META_INFO_STORAGE_FILE));
        out.writeObject(dataServers);
        out.writeObject(tableMetaList);
        log.warn("将对象{}和{}写入到文件{}中", dataServers, tableMetaList, MasterConstant.META_INFO_STORAGE_FILE);
        out.close();
    }

    public static DataServer getDataServerById(Integer id) {
        for (DataServer server : dataServers.values()) {
            if (server.id.equals(id)) {
                return server;
            }
        }
        return null;
    }

    public static Integer getServerNumByState(DataServerStateEnum state) {
        Integer num = 0;
        for (DataServer server : dataServers.values()) {
            if (server.state == state) {
                num++;
            }
        }
        return num;
    }

    public static Integer getValidServerNum() {
        return getServerNumByState(DataServerStateEnum.PRIMARY) +
                getServerNumByState(DataServerStateEnum.COPY) +
                getServerNumByState(DataServerStateEnum.IDLE);
    }

    public static Integer getIdleServerNum() {
        return getServerNumByState(DataServerStateEnum.IDLE);
    }

    /**
     * 从闲置服务器中选择一台，并与参数服务器不同
     */
    public static DataServer getIdleServer(DataServer server) {
        for (DataServer dataServer : dataServers.values()) {
            if (dataServer.state == DataServerStateEnum.IDLE && dataServer != server) {
                return dataServer;
            }
        }
        return null;
    }

    /**
     * 选择一台没有配对的服务器
     */
    public static DataServer getSingleServer() {
        for (DataServer server : dataServers.values()) {
            if (server.state == DataServerStateEnum.PRIMARY || server.state == DataServerStateEnum.COPY) {
                if (server.dualServerId.equals(MasterConstant.NO_DUAL_SERVER)) {
                    log.warn("查询到目前数据服务器{}处于未结对状态", server);
                    return server;
                }
            }
        }
        return null;
    }

    /**
     * 让一台服务器和一台已经结对过但配偶失效的服务器重新结对
     */
    public static void remakeServerPair(DataServer server, DataServer singleServer) {
        // 状态改变
        server.remakePair(singleServer);

        //TODO:RPC
    }

    /**
     * 从闲置服务器中选择两台结对
     */
    public static void makeServerPairFromIdleServer(DataServer server) {
        DataServer server2 = getIdleServer(server);
        if (server2 == null){
            log.warn("不存在其他闲置服务器");
        }else{
            server.makePair(server2);
            //TODO:RPC
        }
    }

    /**
     * 根据name找到table；若未找到，返回null
     */
    public static TableMeta findTable(String name) {
        for (TableMeta item : tableMetaList) {
            if (item.name.equals(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 增加TableMeta
     */
    public static void addTableMata(TableMeta tableMeta) {
        if (findTable(tableMeta.name) != null || tableMetaList.contains(tableMeta)) {
            throw new BusinessException(ErrorCodeEnum.FAIL.getCode(), "该表格已经存在");
        } else {
            tableMetaList.add(tableMeta);
        }
    }

    /**
     * 删除TableMeta
     */
    public static void removeTableMeta(String tableName) {
        tableMetaList.removeIf(tableMeta -> tableMeta.name.equals(tableName));
    }

    /**
     * 根据负载均衡返回合适的dataServer的ID
     */
    public static Integer allocateLocatedServer() {
        // 得到主件机组成的列表
        List<DataServer> primaryServers = dataServers.values()
                .stream()
                .filter(server -> server.state == DataServerStateEnum.PRIMARY).toList();

        // 使用TreeMap来存储每个主件机的表格数量，键为数量，值为ID
        TreeMap<Integer, Integer> tableCounts = new TreeMap<>();
        for (DataServer server : primaryServers) {
            int tableCount = 0;
            for (TableMeta tableMeta : tableMetaList) {
                if (tableMeta.locatedServerName.equals(server.hostName)) {
                    tableCount++;
                }
            }
            tableCounts.put(tableCount, server.id);
        }

        // 返回表格数量最小的主件机的ID
        return tableCounts.firstEntry().getValue();
}
