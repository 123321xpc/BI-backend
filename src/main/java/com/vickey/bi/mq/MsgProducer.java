package com.vickey.bi.mq;

import com.vickey.bi.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息生产者
 */
@Component
public class MsgProducer {

    // 注入 RabbitTemplate，用于发送消息
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到简单队列
     * @param message 要发送的消息内容
     */
    public void sendMessage(String message) {
        // 参数1：队列名称；参数2：消息内容
        rabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE_NAME, message);
        System.out.println("生产者发送消息成功：" + message);
    }
}