package com.db.client;

import com.db.RPC.model.ChangeTableDataRequest;
import com.db.common.exception.BusinessException;
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
        //在JDBC建立的前提下执行
        //int ret = executeUpdate(query);
        //如果是主机则在副本机上同样执行上述语句
        //返回ret
        //ChangeTableDataRequest req = new ChangeTableDataRequest();
        //req.setSql(query);
        //req.setType(1);
    }

    public static void createIndex(String query) throws BusinessException {
        //在JDBC建立的前提下执行
        //int ret = executeUpdate(query);
        //如果是主机则在副本机上同样执行上述语句
        //返回ret
    }

    public static void dropTable(String query) throws BusinessException {
        //在JDBC建立的前提下执行
        //int ret = executeUpdate(query);
        //如果是主机则在副本机上同样执行上述语句
        //返回ret
    }

    public static void dropIndex(String query) throws BusinessException {
        //在JDBC建立的前提下执行
        //int ret = executeUpdate(query);
        //如果是主机则在副本机上同样执行上述语句
        //返回ret
    }

    public static void insert(String query) throws BusinessException {
        //在JDBC建立的前提下执行
        //int ret = executeUpdate(query);
        //将表格导出为sql文件传给副本机
        //返回ret
    }

    public static void delete(String query) throws BusinessException {
        //在JDBC建立的前提下执行
        //int ret = executeUpdate(query);
        //将表格导出为sql文件传给副本机
        //返回ret
    }

    public static void select(String query) throws BusinessException {
        //在JDBC建立的前提下执行
        //ResultSet ret = executeQuery(query);
        //返回ret
    }

    public static void update(String query) throws BusinessException {
        //在JDBC建立的前提下执行
        //int ret = executeUpdate(query);
        //将表格导出为sql文件传给副本机
        //返回ret
    }
}
