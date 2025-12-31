package com.vickey.bi.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 队列配置类
 */
@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "BI_Queue";

    @Bean
    public Queue simpleQueue() {
        return new Queue(QUEUE_NAME, true, false, false);
    }
}