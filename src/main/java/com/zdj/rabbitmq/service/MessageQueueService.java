package com.zdj.rabbitmq.service;

/**
 * 消息队列服务接口
 */
public interface MessageQueueService {

    /**
     *
     * @param routingKey 路由名称
     * @param message 消息内容
     */
    public void send(String routingKey,String message);


    /**
     * 延迟发送消息到队列
     * @param queueName 队列名称
     * @param message 消息内容
     * @param times 延迟时间 单位毫秒
     */
    public void send(String queueName,String message,long times);

}
