package com.nfsq.yqf.rocketmq_consumer.mq.rocketmq.consumer.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author fWang
 * @Description:初始化所有rocketmq消费者
 * @date 2017/11/21 11:01
 */
@Component
public class InitRocketMqConsumerDataListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderlyConsumer orderlyConsumer;

    /**
     * 加载所有继承了DefaultMessageListenerOrderly并有TopicSub注解的消费者
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null) {
            logger.info("初始化rocketMq相关topic和tags");
            Map<String, Object> beans = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(TopicSub.class);
            Hashtable<String, DefaultMessageListenerOrderly> mqMap = new Hashtable<>();
            Map<String, String> topicSubMap = Maps.newConcurrentMap();
            if (beans != null && !beans.isEmpty()) {
                for (Object bean : beans.values()) {
                    if (bean != null && bean instanceof DefaultMessageListenerOrderly) {
                        TopicSub topic = bean.getClass().getDeclaredAnnotation(TopicSub.class);
                        if (StringUtils.isNotBlank(topic.topic())) {

                            if(topic.tag() != null){
                                for(String tag: topic.tag()){
                                    mqMap.put(DefaultMessageListenerOrderly.getTopicAndTagKey(topic.topic(), tag), (DefaultMessageListenerOrderly) bean);
                                }
                            }

                            String topicSubValue = StringUtils.join(topic.tag(), " || ");
                            if(topicSubMap.containsKey(topic.topic())){
                                topicSubValue = topicSubMap.get(topic.topic()) + " || " + topicSubValue;
                            }
                            topicSubMap.put(topic.topic(), topicSubValue);
                        }

                    }
                }
            }
            DefaultMessageListenerOrderly.setTopicMap(mqMap);
            orderlyConsumer.setSubscriptionTable(topicSubMap);

            logger.info("consumer subscription:{}", JSON.toJSON(topicSubMap));

            try {
                orderlyConsumer.start();
            } catch (MQClientException e) {
                logger.info("启动rocketMq有序消息的消费者失败，失败原因:", e);
            }
        }
    }
}
