package com.nfsq.yqf.rocketmq_producer.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by qfyu
 * Date:2018/12/27
 * Time:14:25
 **/
@Component
@Slf4j
public class RocketMQProcducer {
    @Value("${rocketmq.producer.groupName}")
    private String producerGroup;

    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;

    private DefaultMQProducer defaultMQProducer;

    @PostConstruct
    public void init(){
        log.info("rocketmq initlize");
        defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setProducerGroup(producerGroup);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setInstanceName(System.currentTimeMillis()+"");
        try{
            defaultMQProducer.start();
            log.info("rocketmq start OK");
        }catch (Exception e){
            e.printStackTrace();
            log.info("rocketmq start FAIL");
        }
    }

    @PreDestroy
    public void close(){
        if(defaultMQProducer != null){
            defaultMQProducer.shutdown();
        }
    }

    public DefaultMQProducer getDefaultMQProducer(){
        return this.defaultMQProducer;
    }

}
