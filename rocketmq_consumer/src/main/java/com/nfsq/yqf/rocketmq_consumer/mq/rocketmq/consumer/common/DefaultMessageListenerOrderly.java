package com.nfsq.yqf.rocketmq_consumer.mq.rocketmq.consumer.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.List;

/**
 * @author fWang
 * @Description:默认的有序消息监听类
 * @date 2017/11/21 14:27
 */
public class DefaultMessageListenerOrderly implements MessageListenerOrderly {
    public static final String KEY_SEPARATOR = "#";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static Hashtable<String, DefaultMessageListenerOrderly> topicMap;

    /**
     * 消费的业务方法，子类需重写该方法以实现自己的业务逻辑
     * @param msg
     * @return
     */
    public ConsumeOrderlyStatus process(String msgId, String msg){
        logger.info("------consume Message, msgId:{}, msg:{}", msgId, msg);
        return ConsumeOrderlyStatus.SUCCESS;
    }

    /**
     * 消息消费的入口
     * @param msgs
     * @param context
     * @return
     */
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        logger.info("------start consumeMessage：");
        ConsumeOrderlyStatus status = ConsumeOrderlyStatus.SUCCESS;
        Boolean findRealListener = Boolean.FALSE;

        MessageExt msg = msgs.get(0);

        if(topicMap != null){
            DefaultMessageListenerOrderly realListenerOrderly = topicMap.get(getTopicAndTagKey(msgs.get(0).getTopic(), msgs.get(0).getTags()));
            if(realListenerOrderly != null){
                findRealListener = Boolean.TRUE;
                status = realListenerOrderly.process(msg.getMsgId(), new String(msgs.get(0).getBody()));
            }
        }

        if(!findRealListener){
            status = process(msg.getMsgId(), new String(msgs.get(0).getBody()));
        }

        logger.info("------end consumeMessage：");

        return status;
    }

    /**
     * 设置topic和处理Listener的对应关系
     * @param topicMap
     */
    public static void setTopicMap(Hashtable<String, DefaultMessageListenerOrderly> topicMap) {
        DefaultMessageListenerOrderly.topicMap = topicMap;
    }

    /**
     * 获取唯一的Key
     * @param topic
     * @param tags
     * @return
     */
    public static String getTopicAndTagKey(String topic, String[] tags){
        if(null == topic){
            return null;
        }

        String tagStr = "*";
        if(null != tags){
            tagStr = StringUtils.join(tags, " || ");
        }
        return getTopicAndTagKey(topic, tagStr);
    }

    /**
     * 获取唯一的key
     * @param topic
     * @param tags
     * @return
     */
    public static String getTopicAndTagKey(String topic, String tags){
        return topic + DefaultMessageListenerOrderly.KEY_SEPARATOR + tags;
    }
}
