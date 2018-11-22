package com.zdj.rabbitmq.service;

import com.zdj.rabbitmq.constant.MQConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageQueueServiceTest {

    @Resource
    MessageQueueService messageQueueService;

    @Test
    public void send() {
        messageQueueService.send(MQConstant.ORDER_ROUTING,"order 123");
    }
}