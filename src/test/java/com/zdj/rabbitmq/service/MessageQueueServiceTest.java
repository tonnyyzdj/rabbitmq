package com.zdj.rabbitmq.service;

import com.zdj.rabbitmq.constant.MQConstant;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageQueueServiceTest {

    @Resource
    MessageQueueService messageQueueService;

    @Test
    public void send() {
        messageQueueService.send(MQConstant.ORDER_ROUTING,"order 123");
    }


    @Test
    public void delayQueueSend() {
        //延迟队列测试
        String message = "test";
        messageQueueService.send(message,10000l);
        System.out.println(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+"发送延迟消息：message="+message);
    }

    @Test
    public void retryQueueSend() {
        //延迟队列测试
        String message = "{test}";
        messageQueueService.sendRetry(message);
        System.out.println(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+"发送重试消息：message="+message);
    }
}