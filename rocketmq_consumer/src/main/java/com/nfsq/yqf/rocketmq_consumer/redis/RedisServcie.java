package com.nfsq.yqf.rocketmq_consumer.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by qfyu
 * Date:2018/12/27
 * Time:16:23
 **/
@Service
@Slf4j
public class RedisServcie {
    private static Jedis jedis;
    static {
        jedis = new Jedis("127.0.0.1",6379);
    }

    public boolean setNx(String key,String value){
        if(key == null){
            log.error("key is null");
            return false;
        }
        try{
            return jedis.setnx(key,value)==1;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    public boolean del(String key){
        if(key == null){
            log.error("key is null");
            return false;
        }
        try {
            return jedis.del(key)>0;
        }catch ( Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }
}
