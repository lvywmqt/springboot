package com.cgltech.cat_ip_tcp.util;

import java.util.UUID;

/**
 * 生成uuid工具类
 *
 * @author
 * @create 2018-11-22 16:09
 **/
public class UuidUtil {
    public static String get32UUID(){
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-","");
        return uuid;
    }
}