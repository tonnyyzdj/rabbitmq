package com.zdj.rabbitmq.service.impl;

import com.zdj.rabbitmq.constant.MQConstant;
import com.zdj.rabbitmq.service.MessageQueueService;
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
}
