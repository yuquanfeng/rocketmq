package com.nfsq.yqf.rocketmq_consumer.mq.rocketmq.consumer.common;

import java.lang.annotation.*;

/**
 * @author fWang
 * @Description:topic和tags
 * @date 2017/11/20 17:16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TopicSub {

    /**
     * topic,必填
     * @return
     */
    String topic();

    /**
     * tag
     * @return
     */
    String[] tag() default "*";
}
