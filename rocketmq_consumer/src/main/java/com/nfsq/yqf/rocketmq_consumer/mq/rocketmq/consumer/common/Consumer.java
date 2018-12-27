package com.nfsq.yqf.rocketmq_consumer.mq.rocketmq.consumer.common;

import com.alibaba.fastjson.JSONException;
import com.nfsq.yqf.rocketmq_consumer.redis.RedisServcie;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by qfyu
 * Date:2018/12/27
 * Time:18:26
 **/
@Slf4j
@Component
@TopicSub(topic = "test_topic",tag = "test_tag")
public class Consumer extends DefaultMessageListenerOrderly {
    @Autowired
    RedisServcie redisServcie;

    public ConsumeOrderlyStatus process(String msgId, String msg){
        log.info("UpdateOrgIdNewConsumer 开始处理消息: " + msg);

        if(redisServcie.setNx(msgId, System.currentTimeMillis()+"")){
            try {
                System.out.println("处理业务逻辑");
            } catch(JSONException e){
                //转化异常就说名消息有问题，不再处理
                log.info("处理经销商变更办事处信息出现异常，异常原因:{}", e);
                redisServcie.del(msg);
                return ConsumeOrderlyStatus.SUCCESS;
            } catch (RuntimeException e) {
                log.info("处理经销商变更办事处信息出现异常，异常原因:{}", e);
                redisServcie.del(msgId);
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }else{
            log.info("msgId为{}的消息已处理过，忽略", msgId);
        }

        log.info("UpdateOrgIdNewConsumer 处理消息完成");
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
