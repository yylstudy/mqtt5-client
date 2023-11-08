package com.yyl.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yang.yonglian
 * @version 1.0.0
 * @Description TODO
 * @createTime 2023/11/8 15:47
 */
@RestController
public class MyController {
    @Autowired
    private MqttSendHandler mqttSendHandler;

    /**
     * 发送mqtt消息
     * @param data  负载
     * @param topic 话题
     * @return
     */
    @PostMapping("/topic/send")
    public String sendMsg(String data, String topic) {
        mqttSendHandler.sendToMqtt(topic,data);
        return "success";
    }
}
