package com.db.region.service;

import com.db.common.enums.DataServerStateEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import com.mysql.jdbc.Driver;
import static com.db.common.constant.ServerConstant.getHostAddress;

@Slf4j
public class RegionConfig {
    //MySQL属性
    public static String mysqlDriver = "com.mysql.cj.jdbc.Driver";
    public static String mysqlUrl = "jdbc:mysql://localhost:3306/distributed_db";
    public static String username = "root";
    public static String password = "123456";
    public static Connection con = null;
    public static Statement stat = null;

    //RPC属性
    public static String serverName = "";
    public static String serverURL = "";
    public static Integer port = 2335;
    public static DataServerStateEnum state = DataServerStateEnum.IDLE;
    public static String dualName = "";
    public static String dualURL = "";

    public static void init(){
        try{
            //配置文件
            Properties props = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/config.properties");
            props.load(in);
            in.close();

            mysqlDriver = props.getProperty("mysqlDriver");
            mysqlUrl = props.getProperty("mysqlUrl");
            username = props.getProperty("username");
            password = props.getProperty("password");


            serverName = props.getProperty("serverName");
            serverURL = getHostAddress();
            port = Integer.valueOf(props.getProperty("port"));

            log.warn("{},{},{},{},{},{},{}",mysqlDriver,mysqlUrl,username,password,serverName,serverURL,port);

            //注册驱动
            Class.forName(mysqlDriver);
            //获取数据库的连接对象
            con = DriverManager.getConnection(mysqlUrl, username, password);
            //获取执行sql语句的对象
            stat = con.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
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
