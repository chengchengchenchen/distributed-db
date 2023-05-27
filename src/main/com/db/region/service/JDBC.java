package com.db.region.service;

import com.db.common.enums.DataServerStateEnum;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Slf4j
public class JDBC {
    public static String mysqlDriver = "com.mysql.jdbc.Driver";
    public static String mysqlUrl = "jdbc:mysql://localhost:3306/distributed_db";
    public static String username = "root";
    public static String password = "123456";
    public static Connection con = null;
    public static Statement stat = null;
    public static DataServerStateEnum state = DataServerStateEnum.IDLE;
    public static String dualURL = "";

    public static void main(String[] args){

        try {
            //注册驱动
            Class.forName(mysqlDriver);

            //获取数据库的连接对象
            con = DriverManager.getConnection(mysqlUrl, username, password);

            //获取执行sql语句的对象
            stat = con.createStatement();

            //执行sql并接收返回结果
            //int count = stat.executeUpdate(query);

            //处理结果
            //System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }


}
