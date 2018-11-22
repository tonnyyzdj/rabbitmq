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
}
