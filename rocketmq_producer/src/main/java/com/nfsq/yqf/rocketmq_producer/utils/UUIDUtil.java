package com.nfsq.yqf.rocketmq_producer.utils;

import java.util.UUID;

/**
 * Created by qfyu
 * Date:2018/12/27
 * Time:14:41
 **/
public class UUIDUtil {
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
