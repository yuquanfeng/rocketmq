package com.nfsq.yqf.rocketmq_producer.mq;

import com.nfsq.yqf.rocketmq_producer.bean.YQFResult;
import com.nfsq.yqf.rocketmq_producer.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by qfyu
 * Date:2018/12/27
 * Time:14:37
 **/
@Component
@Slf4j
public class RockerMQSender {
    @Value("${rocketmq.producer.topic}")
    private String topic;

    @Value("${rocketmq.producer.tag}")
    private String tag;

    @Autowired
    RocketMQProcducer rocketMQProcducer;

    public YQFResult sendMessage(String msg){
        YQFResult result = new YQFResult();
        Message message = new Message(topic,tag,msg.getBytes());
        message.setKeys(UUIDUtil.getUUID());
        DefaultMQProducer defaultMQProducer = rocketMQProcducer.getDefaultMQProducer();
        try {
            defaultMQProducer.send(message);
            result.setSuccess(true);
            result.setMessage("消息发送成功");
            log.info("发送消息成功");
        }catch (Exception e){
            log.info("发送消息失败");
            result.setSuccess(false);
            result.setMessage("消息发送失败");
        }
        return result;
    }
}
