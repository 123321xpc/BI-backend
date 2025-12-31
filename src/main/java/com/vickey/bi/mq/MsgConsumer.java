package com.vickey.bi.mq;

import com.alibaba.excel.util.StringUtils;
import com.rabbitmq.client.Channel;
import com.vickey.bi.common.ErrorCode;
import com.vickey.bi.config.RabbitMqConfig;
import com.vickey.bi.exception.ThrowUtils;
import com.vickey.bi.manager.deepseek.DeepSeekApiService;
import com.vickey.bi.model.entity.Chart;
import com.vickey.bi.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 消息消费者
 */
@Slf4j
@Component
public class MsgConsumer {

    @Resource
    private DeepSeekApiService deepSeekApiService;

    @Resource
    private ChartService chartService;


    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, Message messageObj) {
        log.info("Received message: " + message);

        ThrowUtils.throwIf(StringUtils.isEmpty(message), ErrorCode.OPERATION_ERROR, "mq消息为空!!!");

        long deliveryTag = messageObj.getMessageProperties().getDeliveryTag();

        Chart chart = chartService.getById(Long.parseLong(message));

        String prompt = chart.getExecMsg();

        chart.setStatus("processing");
        chart.setExecMsg("正在生成中...");
        boolean b = chartService.updateById(chart);
        ThrowUtils.throwIf(!b, ErrorCode.OPERATION_ERROR, "更新chart状态失败!!!");

        String aiAns = deepSeekApiService.chatWithDeepSeek(prompt);
        List<String> ansList = deepSeekApiService.extractContent(aiAns);

        //  保存 AI 结果
        chart.setStatus("success");
        chart.setExecMsg("生成成功！");
        chart.setName(ansList.get(0));
        chart.setGenResult(ansList.get(1));
        chart.setGenChart(ansList.get(2));
        chart.setChartType(ansList.get(3));

        boolean res = chartService.updateById(chart);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR, "更新chart状态失败!!!");

        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}