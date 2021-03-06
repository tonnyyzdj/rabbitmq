package com.zdj.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.zdj.rabbitmq.constant.MQConstant;
import org.apache.http.client.utils.DateUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author zhangdanjiang
 * @description  延迟队列消息
 * @date 2018/11/19
 */
@Component
public class DLXQueueListener  extends BaseListener  {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MQConstant.DEAD_LETTER_QUEUE_NAME, durable = "true"),
                    exchange = @Exchange(value = MQConstant.DEFAULT_EXCHANGE, type = ExchangeTypes.TOPIC, durable = "true"),
                    key = MQConstant.DEAD_LETTER_QUEUE_ROUTE),
            admin = "rabbitAdmin")
    public void onMessage(Message message, Channel channel) throws Exception{
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
