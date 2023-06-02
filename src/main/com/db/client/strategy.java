package com.db.client;

import com.db.RPC.model.*;
import com.db.RPC.service.RegionService;
import com.db.common.enums.RpcResultCodeEnum;
import com.db.common.exception.BusinessException;
import com.db.common.enums.SqlQueryEnum;
import com.db.client.rpc.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO:缓存
//添加了3个方法，都在最后

@Slf4j
public class strategy {

    //在client将 bufferedreader 处理成string以及处理quit等情况
    public static void main(String[] args) throws Exception {
        ResultSetData test = new ResultSetData();
        List<String> columnNames = new ArrayList<>();
        columnNames.add("id");
        columnNames.add("name");
        columnNames.add("email");
        test.setColumnNames(columnNames);
        List<List<String>> rows = new ArrayList<>();

        List<String> row = new ArrayList<>();
        row.add("1");
        row.add("zhao");
        row.add("213145");
        rows.add(row);

        test.setRows(rows);
        printResultSet(test);
    }

    public static void interpreter(String query) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < query.length(); i++) {
            char c = query.charAt(i);

            if (Character.isUpperCase(c)) {
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        query = result.toString();
        String[] split = query.split(" ");
        try {
            if (split.length == 1 && split[0].equals(""))
                throw new BusinessException("Default error: No Input.");
            else if (split.length == 1)
                throw new BusinessException("Input error: Invalid query.");

            switch (split[0]) {
                case "create":  //直接传string
                    switch (split[1]) {
                        case "table":
                            changeTable(query, SqlQueryEnum.CREATE_TABLE.ordinal());
                            break;
                        case "index":
                            changeTable(query, SqlQueryEnum.CREATE_INDEX.ordinal());
                            break;
                        default:
                            throw new BusinessException("Create error: Invalid query.");
                    }
                    break;
                case "drop":    //直接传string
                    switch (split[1]) {
                        case "table":
                            changeTable(query, SqlQueryEnum.DROP_TABLE.ordinal());
                            break;
                        case "index":
                            changeTable(query, SqlQueryEnum.DROP_INDEX.ordinal());
                            break;
                        default:
                            throw new BusinessException("Drop error: Invalid query.");
                    }
                    break;
                case "insert":  //传sql文件
                    changeTable(query, SqlQueryEnum.INSERT.ordinal());
                    break;
                case "delete":  //传sql文件
                    changeTable(query, SqlQueryEnum.DELETE.ordinal());
                    break;
                case "select":  //传sql文件
                    select(query);
                    break;
                case "update":  //传sql文件
                    changeTable(query, SqlQueryEnum.UPDATE.ordinal());
                    break;
                default:
                    throw new BusinessException("Input error: Invalid query!");
            }

        } catch (BusinessException e) {
            log.warn(e.getMessage(), e);
        }
    }

    public static void select(String query) throws BusinessException {
        //QueryTableDataRequest req = new QueryTableDataRequest();
        //req.setSql(query);
        //QueryTableDataResponse ret = client.queryTableData(req);
        //输出ret.ResultSetData
        String tableName = extractTableName(query);
        int type = SqlQueryEnum.SELECT.ordinal();
        LocatedServer locatedServer = new LocatedServer();
        locatedServer.getTableLocation(tableName, type);
        if(locatedServer.locatedServerUrl != null){
            QueryTableDataRequest queryTableDataRequest = new QueryTableDataRequest()
                    .setSql(query)
                    .setBase(new Base()
                            .setCaller("Client")
                            .setReceiver(locatedServer.locatedServerName));
            RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class, locatedServer.locatedServerIp, locatedServer.locatedServerPort);
            QueryTableDataResponse queryTableDataResponse;
            try{
                queryTableDataResponse = regionServerClient.queryTableData(queryTableDataRequest);
                if(queryTableDataResponse.getBaseResp().code == 0){
                    ResultSetData resultSetData = queryTableDataResponse.getResultSetData();
                    printResultSet(resultSetData);
                } else {
                    log.warn("查询出现问题：{}",queryTableDataResponse.getBaseResp().desc);
                }
            } catch (Exception e){
                e.printStackTrace();
                log.warn("向region查询失败");
            }
        } else {
            log.warn("查询失败");
        }

    }

    //type:操作类型
    public static void changeTable(String query,int type) throws BusinessException{
        String tableName = extractTableName(query);
        LocatedServer locatedServer = new LocatedServer();
        locatedServer.getTableLocation(tableName, type);
        if(locatedServer.locatedServerUrl != null){
            ChangeTableDataRequest changeTableDataRequest = new ChangeTableDataRequest()
                    .setSql(query)
                    .setName(tableName)
                    .setType(type)
                    .setBase(new Base()
                            .setCaller("Client")
                            .setReceiver(locatedServer.locatedServerName));
            RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class, locatedServer.locatedServerIp, locatedServer.locatedServerPort);
            ChangeTableDataResponse changeTableDataResponse;
            int affectedLines;
            try {
                changeTableDataResponse = regionServerClient.changeTableData(changeTableDataRequest);
                affectedLines = changeTableDataResponse.getResult();
                System.out.println("Changed" + affectedLines + "row.");
                if(changeTableDataResponse.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.ordinal()){
                    log.warn("操作表格成功");
                } else {
                    log.warn("操作表格失败：{}",changeTableDataResponse.getBaseResp().desc);
                }

            }catch(Exception e){
                log.warn("操作表格出现异常");
            }
        } else {
            log.warn("查询表格元数据位置失败");
        }

    }
    //通过正则表达式获取TableName
    public static String extractTableName(String query){
        // 匹配 SELECT、DELETE、UPDATE、INSERT、CREATE TABLE、DROP TABLE 等语句中的表名
        String pattern = "(?i)(?:FROM|JOIN|INTO|UPDATE|INSERT INTO|CREATE TABLE|DROP TABLE)\\s+(\\w+)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(query);

        // 查找匹配的表名
        if (matcher.find()) {
            log.warn("tableName:{}",matcher.group(1));
            return matcher.group(1);
        } else {
            // 如果未找到匹配的表名，可以根据需求返回默认值或抛出异常
            log.warn("sql语句提取正TableName失败");
            throw new IllegalArgumentException("无法从查询中提取表名");
        }
    }

    public static void printResultSet(ResultSetData rs) throws Exception {
        //ResultSetMetaData resultSetMetaData = rs.getMetaData();
        // 获取列数
        int ColumnCount = rs.getColumnNamesSize();
        // 保存当前列最大长度的数组
        int[] columnMaxLengths = new int[ColumnCount];

        for (int i = 0; i < ColumnCount; i ++) {
            columnMaxLengths[i] = Math.max(3, rs.getColumnNames().get(i).length());
        }
        // 缓存结果集,结果集可能有序,所以用ArrayList保存变得打乱顺序.
        //ArrayList<String[]> results = new ArrayList<>();
        // 按行遍历
        int RowCount = rs.getRowsSize();
        for (int i = 0; i < RowCount; i++) {
            // 保存当前行所有列
            String[] columnStr = new String[ColumnCount];
            // 获取属性值.
            for (int j = 0; j < ColumnCount; j++) {
                // 获取一列
                columnStr[j] = rs.getRows().get(i).get(j);
                // 计算当前列的最大长度
                columnMaxLengths[j] = Math.max(columnMaxLengths[j], (columnStr[j] == null) ? 0 : columnStr[j].length());
            }
            // 缓存这一行.
            //results.add(columnStr);
        }
        printSeparator(columnMaxLengths);
        printColumnName(rs.getColumnNames(), columnMaxLengths);
        printSeparator(columnMaxLengths);
        // 遍历集合输出结果
        for (int i = 0; i < RowCount; i++) {
            for (int j = 0; j < ColumnCount; j++) {
                // System.out.printf("|%" + (columnMaxLengths[i] + 1) + "s", columnStr[i]);
                System.out.printf("|%-" + columnMaxLengths[j] + "s", rs.getRows().get(i).get(j));
            }
            System.out.println("|");
        }
        printSeparator(columnMaxLengths);
    }

    private static void printColumnName(List<String> resultSetMetaData, int[] columnMaxLengths) throws Exception {
        int columnCount = resultSetMetaData.size();
        for (int i = 0; i < columnCount; i++) {
            // System.out.printf("|%" + (columnMaxLengths[i] + 1) + "s", resultSetMetaData.getColumnName(i + 1));
            System.out.printf("|%-" + columnMaxLengths[i] + "s", resultSetMetaData.get(i));
        }
        System.out.println("|");
    }

    private static void printSeparator(int[] columnMaxLengths) {
        for (int i = 0; i < columnMaxLengths.length; i++) {
            System.out.print("+");
            // for (int j = 0; j < columnMaxLengths[i] + 1; j++) {
            for (int j = 0; j < columnMaxLengths[i]; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }
}



