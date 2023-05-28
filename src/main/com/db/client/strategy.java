package com.db.client;

import com.db.RPC.model.ChangeTableDataRequest;
import com.db.common.exception.BusinessException;
import com.db.common.enums.SqlQueryEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class strategy {
    //在client将 bufferedreader 处理成string以及处理quit等情况
    public static void main(String[] args){
        String test = "";
        interpreter(test);
    }

    public static void interpreter(String query) {
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
                            createTable(query);
                            break;
                        case "index":
                            createIndex(query);
                            break;
                        default:
                            throw new BusinessException("Create error: Invalid query.");
                    }
                    break;
                case "drop":    //直接传string
                    switch (split[1]) {
                        case "table":
                            dropTable(query);
                            break;
                        case "index":
                            dropIndex(query);
                            break;
                        default:
                            throw new BusinessException("Drop error: Invalid query.");
                    }
                    break;
                case "insert":  //传sql文件
                    insert(query);
                    break;
                case "delete":  //传sql文件
                    delete(query);
                    break;
                case "select":  //传sql文件
                    select(query);
                    break;
                case "update":  //传sql文件
                    update(query);
                    break;
                default:
                    throw new BusinessException("Input error: Invalid query!");
            }

        } catch (BusinessException e) {
            log.warn(e.getMessage(), e);
        }
    }

    public static void createTable(String query) throws BusinessException {
        //TODO:打开client向相应table所在region的socket
        //ChangeTableDataRequest req = new ChangeTableDataRequest();
        //req.setSql(query);
        //req.setType(SqlQueryEnum.CREATE_TABLE.ordinal());
        //req.setName(); 这里问一下gpt怎么正则表达式获得sql指令中table的名字
        //ChangeTableDataResponse ret = client.changeTableData(req);
        //System.out.println("Changed" + ret.getResult() + "row.");
    }

    public static void createIndex(String query) throws BusinessException {
        //TODO:打开client向相应table所在region的socket
        //ChangeTableDataRequest req = new ChangeTableDataRequest();
        //req.setSql(query);
        //req.setType(SqlQueryEnum.CREATE_INDEX.ordinal());
        //req.setName(); 这里问一下gpt怎么正则表达式获得sql指令中table的名字
        //ChangeTableDataResponse ret = client.changeTableData(req);
        //System.out.println("Changed" + ret.getResult() + "row.");
    }

    public static void dropTable(String query) throws BusinessException {
        //TODO:打开client向相应table所在region的socket
        //ChangeTableDataRequest req = new ChangeTableDataRequest();
        //req.setSql(query);
        //req.setType(SqlQueryEnum.DROP_TABLE.ordinal());
        //req.setName(); 这里问一下gpt怎么正则表达式获得sql指令中table的名字
        //ChangeTableDataResponse ret = client.changeTableData(req);
        //System.out.println("Changed" + ret.getResult() + "row.");
    }

    public static void dropIndex(String query) throws BusinessException {
        //TODO:打开client向相应table所在region的socket
        //ChangeTableDataRequest req = new ChangeTableDataRequest();
        //req.setSql(query);
        //req.setType(SqlQueryEnum.DROP_INDEX.ordinal());
        //req.setName(); 这里问一下gpt怎么正则表达式获得sql指令中table的名字
        //ChangeTableDataResponse ret = client.changeTableData(req);
        //System.out.println("Changed" + ret.getResult() + "row.");
    }

    public static void insert(String query) throws BusinessException {
        //TODO:打开client向相应table所在region的socket
        //ChangeTableDataRequest req = new ChangeTableDataRequest();
        //req.setSql(query);
        //req.setType(SqlQueryEnum.INSERT.ordinal());
        //req.setName(); 这里问一下gpt怎么正则表达式获得sql指令中table的名字
        //ChangeTableDataResponse ret = client.changeTableData(req);
        //System.out.println("Changed" + ret.getResult() + "row.");
    }

    public static void delete(String query) throws BusinessException {
        //TODO:打开client向相应table所在region的socket
        //ChangeTableDataRequest req = new ChangeTableDataRequest();
        //req.setSql(query);
        //req.setType(SqlQueryEnum.DELETE.ordinal());
        //req.setName(); 这里问一下gpt怎么正则表达式获得sql指令中table的名字
        //ChangeTableDataResponse ret = client.changeTableData(req);
        //System.out.println("Changed" + ret.getResult() + "row.");
    }

    public static void select(String query) throws BusinessException {
        //TODO:打开client向相应table所在region的socket
        //QueryTableDataRequest req = new QueryTableDataRequest();
        //req.setSql(query);
        //QueryTableDataResponse ret = client.queryTableData(req);
        //输出ret.ResultSetData
    }

    public static void update(String query) throws BusinessException {
        //TODO:打开client向相应table所在region的socket
        //ChangeTableDataRequest req = new ChangeTableDataRequest();
        //req.setSql(query);
        //req.setType(SqlQueryEnum.UPDATE.ordinal());
        //req.setName(); 这里问一下gpt怎么正则表达式获得sql指令中table的名字
        //ChangeTableDataResponse ret = client.changeTableData(req);
        //System.out.println("Changed" + ret.getResult() + "row.");
    }
}
