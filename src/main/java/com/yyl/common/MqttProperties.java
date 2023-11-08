package com.yyl.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @author yang.yonglian
 * @version 1.0.0
 * @Description mqtt5配置类
 * @createTime 2023/11/8 9:51
 */
@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

    private String url;

    private String username;

    private String password;
    /**
     * 客户端前缀
     */
    private String clientIdPrefix;
    /**
     * 订阅的topic
     */
    private List<Topic> receiveTopics;

    private Integer keepAlive = 15;

    private Integer completionTimeout = 3000;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Topic {
        private String name;
        private Integer qos;
    }

}
