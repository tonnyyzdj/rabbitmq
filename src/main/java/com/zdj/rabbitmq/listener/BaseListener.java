package com.zdj.rabbitmq.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.zdj.rabbitmq.constant.MQConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public abstract class BaseListener  {

    protected static final Logger logger = LogManager.getLogger();

    static String RETRY_QUEUE_SUFFIX = ".retry";

    static String ERROR_QUEUE_SUFFIX = ".error";
    static final String ERROR_MESSAGE_KEY = "errorMsg";


    //失败重试处理
    public void handelException(Message message, Channel channel,Exception exception) throws Exception {
       try {
           Map<String, Object> headers = message.getMessageProperties().getHeaders();
           Integer republishTimes = (Integer) headers.get(MQConstant.X_REPUBLISH_TIMES_KEY);
           if(republishTimes == null)
               republishTimes = 0;
           //如果超过了重试次数，放入错误队列
           if (republishTimes >= MQConstant.X_REPUBLISH_TIMES) {
               addError(message, channel, exception);
               return;
           } else {
               //没有超过重试次数，加入重试队列
               addRetry(message,channel,republishTimes);
           }
       }catch (Exception e){
           logger.error("处理重试消息失败：error={}",e.getMessage());
       }
    }

    /**
     * 超过失败重试次数，将消息放入error队列
     *
     * @param message  消息
     * @param channel  通道
     * @throws Exception
     */
    private void addError(Message message, Channel channel, Exception e) throws Exception {
        byte[] errorMsgBody;
        try {
            JSONObject jsonObj = JSON.parseObject(new String(message.getBody()));
            jsonObj.put(ERROR_MESSAGE_KEY, e.getMessage());
            errorMsgBody = jsonObj.toJSONString().getBytes();
        } catch (JSONException e1) {
            logger.error("Original Message Body: " + new String(message.getBody()));
            errorMsgBody = message.getBody();
        }
        String queueName = message.getMessageProperties().getConsumerQueue();
        String errorQueueName = queueName + ERROR_QUEUE_SUFFIX;

        channel.queueDeclare(errorQueueName, true, false, false, null);
        channel.queueBind(errorQueueName, MQConstant.DEFAULT_EXCHANGE, errorQueueName);
        channel.basicPublish(MQConstant.DEFAULT_EXCHANGE, errorQueueName,
                MessageProperties.PERSISTENT_TEXT_PLAIN, errorMsgBody);
    }

    //如果失败，并次数小于重试次数，则放入死信队列，并设置过期时间，并将死信队列配置转发到正常队列路由（时间间隔过后，两次进入正常消费队列）
    public void addRetry(Message message, Channel channel,int republishTimes) throws Exception {
        republishTimes++;
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        //获取队列名，重试队列名在后面加.retry，重试路由名跟队列名取成一样
        String queueName = message.getMessageProperties().getConsumerQueue();
        String retryQueueName = queueName + RETRY_QUEUE_SUFFIX;
        //获取队列exchange及routeKey
        String exchange = message.getMessageProperties().getReceivedExchange();
        String routeKey = message.getMessageProperties().getReceivedRoutingKey();
        //将dlx 绑定到重试队列上面
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", exchange);
        arguments.put("x-dead-letter-routing-key", routeKey);
        arguments.put("x-message-ttl", MQConstant.X_REPUBLISH_TIME_INTERVAL * 1000);
        channel.queueDeclare(retryQueueName,true,false,false,arguments);

        headers.put(MQConstant.X_REPUBLISH_TIMES_KEY, republishTimes);
        channel.queueBind(retryQueueName, MQConstant.DEFAULT_EXCHANGE, retryQueueName);
        channel.basicPublish(MQConstant.DEFAULT_EXCHANGE, retryQueueName,
                new AMQP.BasicProperties.Builder().headers(headers).build(), msg.getBytes());
       logger.info(String.format("messageStr,第 [%s]次重试 ",republishTimes));


    }



}
