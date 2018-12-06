package com.zdj.rabbitmq.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.zdj.rabbitmq.constant.MQConstant;
import com.zdj.rabbitmq.service.MessageQueueService;
import org.apache.http.client.utils.DateUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangdanjiang
 * @description  延迟队列消息
 * @date 2018/11/19
 */
@Component
public class WorkQueueListener extends BaseListener  {



    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MQConstant.WORK_QUEUE_NAME, durable = "true"),
                    exchange = @Exchange(value = MQConstant.DEFAULT_EXCHANGE, type = ExchangeTypes.TOPIC, durable = "true"),
                    key = MQConstant.WORK_QUEUE_ROUTE),
            admin = "rabbitAdmin")
    public void onMessage(Message message, Channel channel) throws Exception {
        String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            System.out.println(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+"[WorkQueueListener]接受消息："+ messageStr);

          throw new Exception("test");

        } catch (Exception e) {
            logger.error("处理消息失败：error={}",e.getMessage());
            handelException(message, channel,e);
        }

    }




}
