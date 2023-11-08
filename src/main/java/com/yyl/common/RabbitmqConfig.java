package com.yyl.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author yang.yonglian
 * @version 1.0.0
 * @Description TODO
 * @createTime 2023/11/8 15:09
 */
@Configuration
@Slf4j
public class RabbitmqConfig {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init(){
        //发送端确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("发送端消息确认,correlationData:{},ack:{},cause:{}",correlationData, ack, cause);
        });
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }
    @Bean
    public Queue tQueue(){
        return new Queue("mqtt_queue");
    }
    /**
     * 交换器
     * @return
     */
    @Bean
    public Exchange exchange(){
        Exchange exchange = ExchangeBuilder.directExchange("mqtt_exchange").build();
        return exchange;
    }

    @Bean
    public Binding binding(){
        Binding binding = new Binding("mqtt_queue", Binding.DestinationType.QUEUE,
                "mqtt_exchange", "*", null);
        return binding;
    }
}
