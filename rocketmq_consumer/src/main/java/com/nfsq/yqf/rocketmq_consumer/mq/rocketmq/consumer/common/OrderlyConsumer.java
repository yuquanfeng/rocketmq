package com.nfsq.yqf.rocketmq_consumer.mq.rocketmq.consumer.common;


import com.google.common.collect.Maps;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

/**
 * 有序的消费者
 */
@Component
public class OrderlyConsumer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrv;
    @Value("${rocketmq.consumer.groupName}")
    private String consumerGroupName;
    @Value("${rocketmq.consumer.instanceName}")
    private String instanceName;
    public static Map<String, String> subscriptionTable = Maps.newConcurrentMap();
    private DefaultMQPushConsumer consumer;

    public OrderlyConsumer() {
        super();
    }

    public OrderlyConsumer(String namesrv, String consumerGroupName, String instanceName, Map<String, String> subscriptionTable) {
        this.namesrv = namesrv;
        this.consumerGroupName = consumerGroupName;
        this.instanceName = instanceName;
        this.subscriptionTable = subscriptionTable;
    }

    /**
     * 初始化默认的消费者
     * @throws MQClientException
     */
    @PostConstruct
    public void init(){
        logger.info("start OrderlyConsumer initialize! consumerGroupName:{}, instanceName:{}, namesrv:{}", consumerGroupName, instanceName, namesrv);

        if (null == namesrv || null == consumerGroupName || null == instanceName || null == subscriptionTable) {
            throw new RuntimeException("properties not set");
        }

        consumer = new DefaultMQPushConsumer(consumerGroupName);
        consumer.setNamesrvAddr(namesrv);
        consumer.setInstanceName(instanceName);
        consumer.setSubscription(subscriptionTable);
        consumer.setMessageListener(new DefaultMessageListenerOrderly());

        logger.info("the OrderlyConsumer initialize success!");
    }

    /**
     * 开启消费者
     */
    public void start() throws MQClientException {
        logger.info("start OrderlyConsumer!");
        consumer.start();
        logger.info("OrderlyConsumer start success!");
    }

    /**
     * 关闭消费者
     */
    @PreDestroy
    public void destroy() {
        logger.info("start OrderlyConsumer shutdown!");
        consumer.shutdown();
        logger.info("OrderlyConsumer shutdown success!");
    }

    public static void setSubscriptionTable(Map<String, String> subscriptionTable) {
        OrderlyConsumer.subscriptionTable.putAll(subscriptionTable);
    }
}
