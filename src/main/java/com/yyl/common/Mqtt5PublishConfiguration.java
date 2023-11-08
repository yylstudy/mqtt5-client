package com.yyl.common;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.Mqttv5PahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaderMapper;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;

import java.util.UUID;

/**
 * @author yang.yonglian
 * @version 1.0.0
 * @Description mqtt5配置类
 * @createTime 2023/11/8 9:51
 */
@Slf4j
@Configuration
public class Mqtt5PublishConfiguration {
    @Autowired
    private MqttProperties mqttProperties;
    @Bean
    public MessageChannel mqtt5OutboundChannel() {
        return MessageChannels.direct().get();
    }
    @Bean
    @ServiceActivator(inputChannel = "mqtt5OutboundChannel")
    public MessageHandler mqtt5Outbound(@Qualifier("mqttConnectionOptions") MqttConnectionOptions mqttConnectionOptions) {
        String mqttClientId = mqttClientId();
        Mqttv5PahoMessageHandler messageHandler = new Mqttv5PahoMessageHandler(mqttConnectionOptions, mqttClientId);
        MqttHeaderMapper mqttHeaderMapper = new MqttHeaderMapper();
        mqttHeaderMapper.setOutboundHeaderNames(MqttHeaders.RESPONSE_TOPIC, MqttHeaders.CORRELATION_DATA, MessageHeaders.CONTENT_TYPE);
        messageHandler.setHeaderMapper(mqttHeaderMapper);
        //messageHandler.setDefaultTopic(mqttProperties.getDefaultSendTopic().getName());
        //messageHandler.setDefaultQos(mqttProperties.getDefaultSendTopic().getQos());
        messageHandler.setAsync(true);
        messageHandler.setAsyncEvents(true);
        return messageHandler;
    }
    private String mqttClientId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String clientIdPrefix = mqttProperties.getClientIdPrefix();
        return clientIdPrefix+"_"+uuid;
    }
}
