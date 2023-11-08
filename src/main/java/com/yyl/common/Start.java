package com.yyl.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.core.MessageProducer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang.yonglian
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022/7/28 16:12
 */

@SpringBootApplication
@EnableScheduling
@Slf4j
public class Start {
     public static void main(String[] args) {
         ConfigurableApplicationContext applicationContext = SpringApplication.run(Start.class,args);
    }
}
