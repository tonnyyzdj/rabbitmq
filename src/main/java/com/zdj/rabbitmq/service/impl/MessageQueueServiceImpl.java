package com.zdj.rabbitmq.service.impl;

import com.alibaba.fastjson.JSON;
import com.zdj.rabbitmq.constant.MQConstant;
import com.zdj.rabbitmq.message.DLXMessage;
import com.zdj.rabbitmq.service.MessageQueueService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangdanjiang
 * @description
 * @date 2018/11/19
 */
@Service
public class MessageQueueServiceImpl implements MessageQueueService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(String routingKey, String message) {
            rabbitTemplate.convertAndSend(MQConstant.DEFAULT_EXCHANGE,routingKey,message);
    }

    @Override
    public void send(String queueName, String message, long times) {
        DLXMessage dlxMessage = new DLXMessage(queueName,message,times);
        MessagePostProcessor processor = new MessagePostProcessor(){
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(times + "");
                return message;
            }
        };
        dlxMessage.setExchange(MQConstant.DIRECT_EXCHANGE);
        rabbitTemplate.convertAndSend(MQConstant.DIRECT_EXCHANGE,MQConstant.DEAD_LETTER_QUEUE_NAME, JSON.toJSONString(dlxMessage), processor);
        System.out.println("发送延迟消息：message="+message);


    }


}
