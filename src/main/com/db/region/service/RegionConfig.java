package com.db.region.service;

import com.db.common.enums.DataServerStateEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;


@Slf4j
public class RegionConfig {
    //MySQL属性
    public static String mysqlDriver = "com.mysql.cj.jdbc.Driver";
    public static String mysqlUrl = "jdbc:mysql://localhost:3306/distributed_db";
    public static String dbName = null;
    public static String username = "root";
    public static String password = "123456";
    public static Connection con = null;
    public static Statement stat = null;

    //RPC属性
    public static String hostName = null;
    public static String hostURL = null;
    public static Integer port = 2335;
    public static DataServerStateEnum state = DataServerStateEnum.IDLE;
    public static String dualName = null;
    public static String dualURL = null;

    public static void init() {
        try {
            //配置文件
            Properties props = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/config.properties");
            props.load(in);
            in.close();

            mysqlDriver = props.getProperty("mysqlDriver");
            mysqlUrl = props.getProperty("mysqlUrl");
            dbName = props.getProperty("dbName");
            username = props.getProperty("username");
            password = props.getProperty("password");

            hostName = getHostname();
            port = Integer.valueOf(props.getProperty("port"));
            hostURL = getHostAddress() + ":" + port;
            //TODO:hosturl已设定为本机
            //hostURL = "127.0.0.1" + ":" +port;

            log.warn("{},{},{},{},{},{}:{}", mysqlDriver, mysqlUrl, username, password, hostName, hostURL, port);

            //注册驱动
            Class.forName(mysqlDriver);
            //获取数据库的连接对象
            con = DriverManager.getConnection(mysqlUrl, username, password);
            //获取执行sql语句的对象
            stat = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

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
            /*if (con != null) {
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
            }*/
        }


    }

    /**
     * 获得本机IP
     */
    public static String getHostAddress() {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("获取本机IP失败");
        }
        return ip;
    }

    /**
     * 随机获得本机名称
     */
    public static String getHostname() {
        if (hostName != null) {
            return hostName;
        }
        String basicCharSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(basicCharSet.length());
            sb.append(basicCharSet.charAt(number));
        }
        return sb.toString();
    }
}
