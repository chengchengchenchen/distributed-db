package com.db.common.constant;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

//弃用，改为使用RegionConfig统一管理
@Slf4j
public class ServerConstant {
    /**
     * 本机主机地址
     */
    public static String HOST_URL = getHostAddress() + ":" + RegionConstant.port;

    /**
     * 主机IP
     */
    public static String HOST_IP = getHostAddress();

    /**
     * 随机的本机主机名
     */
    public static String HOST_NAME = getHostname();

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
        if (HOST_NAME != null) {
            return HOST_NAME;
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
