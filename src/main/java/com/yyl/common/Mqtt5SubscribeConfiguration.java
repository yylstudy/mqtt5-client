package com.yyl.common;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;

import java.util.UUID;
/**
 * @author yang.yonglian
 * @version 1.0.0
 * @Description mqtt5订阅配置
 * @createTime 2023/11/8 9:51
 */
@Slf4j
@Configuration
public class Mqtt5SubscribeConfiguration {
    @Autowired
    private MqttProperties mqttProperties;
    /**
     * 订阅器
     * @return
     */
    @Bean
    public MessageProducer mqtt5Inbound() {
        return getMessageProducer(mqttConnectionOptions());
    }

    /**
     * 订阅器
     * 貌似没找出更好的多个客户端的写法，或者直接采用mqttv5.client，不依赖spring-integration-mqtt
     * @return
     */
    @Bean
    public MessageProducer mqtt5Inbound2() {
        return getMessageProducer(mqttConnectionOptions());
    }

    /**
     * mqtt连接参数配置
     * @return
     */
    @Bean
    public MqttConnectionOptions mqttConnectionOptions() {
        MqttConnectionOptions connectionOptions = new MqttConnectionOptions();
        connectionOptions.setUserName(mqttProperties.getUsername());
        connectionOptions.setPassword(mqttProperties.getPassword().getBytes());
        connectionOptions.setServerURIs(new String[]{mqttProperties.getUrl()});
        connectionOptions.setKeepAliveInterval(mqttProperties.getKeepAlive());
        // 断线重连，默认没有重新订阅，必须重写 connectComplete 方法
        connectionOptions.setAutomaticReconnect(true);
        connectionOptions.setAutomaticReconnectDelay(1, 5);
        // 是否重新创建 session
        connectionOptions.setCleanStart(false);
        return connectionOptions;
    }

    @Bean
    public MessageChannel mqtt5InboundChannel() {
        return MessageChannels.publishSubscribe().get();
    }
    @Bean
    public MessageChannel errorChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    private MessageProducer getMessageProducer(MqttConnectionOptions mqttConnectionOptions){
        String mqttClientId = mqttClientId();
        Mqttv5PahoMessageDrivenChannelAdapter messageProducer =
                new Mqttv5PahoMessageDrivenChannelAdapter(mqttConnectionOptions, mqttClientId,
                        mqttProperties.getReceiveTopics().stream()
                                .map(MqttProperties.Topic::getName)
                                .toArray(String[]::new)) {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        // 断线重连后重新订阅主题
                        if (reconnect) {
                            mqttProperties.getReceiveTopics().forEach(topic -> {
                                super.removeTopic(topic.getName());
                                super.addTopic(topic.getName(), topic.getQos());
                            });
                        }
                    }
                };
        messageProducer.setPayloadType(String.class);
        messageProducer.setManualAcks(false);
        messageProducer.setOutputChannel(mqtt5InboundChannel());
        return messageProducer;
    }

    private String mqttClientId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String clientIdPrefix = mqttProperties.getClientIdPrefix();
        return clientIdPrefix+"_"+uuid;
    }


}
