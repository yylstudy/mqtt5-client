package com.yyl.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;

import java.nio.charset.StandardCharsets;

/**
 * @author yang.yonglian
 * @version 1.0.0
 * @Description TODO
 * @createTime 2023/11/8 15:14
 */
@Slf4j
@Configuration
public class MyMessageHandlerConfig {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 消息处理器，这里发送到rabbitmq
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqtt5InboundChannel")
    public MessageHandler handlerMqtt5Message() {
        return message -> {
            MessageHeaders headers = message.getHeaders();
            //获取消息Topic
            String receivedTopic = (String) headers.get(MqttHeaders.RECEIVED_TOPIC);
            log.info("获取到v5的消息的topic :{} ", receivedTopic);
            //获取消息体
            String payload = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
            log.info("获取到v5的消息的payload :{} ", payload);
            rabbitTemplate.convertAndSend(receivedTopic,"*",payload);
        };
    }
}
