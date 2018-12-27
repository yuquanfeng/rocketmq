package com.nfsq.yqf.rocketmq_producer.controller;

import com.nfsq.yqf.rocketmq_producer.bean.YQFResult;
import com.nfsq.yqf.rocketmq_producer.mq.RockerMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qfyu
 * Date:2018/12/27
 * Time:16:07
 **/
@RestController
public class MQController {
    @Autowired
    RockerMQSender rockerMQSender;

    @RequestMapping("/send")
    public YQFResult send(String message){
        return rockerMQSender.sendMessage(message);
    }
}
