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
 * @description
 * @date 2018/11/19
 */
@Component
public class OrderListener {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MQConstant.ORDER_QUEUE_NAME, durable = "true"),
                    exchange = @Exchange(value = MQConstant.DEFAULT_EXCHANGE, type = ExchangeTypes.TOPIC, durable = "true"),
                    key = MQConstant.ORDER_ROUTING),
            admin = "rabbitAdmin")
    public void onMessage(Message message, Channel channel) {
        String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);

        System.out.println(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+"接受消息："+ messageStr);
    }
}
