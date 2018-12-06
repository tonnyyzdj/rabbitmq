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
     * @param message 消息内容
     * @param times 延迟时间 单位毫秒
     */
    public void send(String message,long times);


    /**
     * 消息延时重试发送
     * @param message 消息内容
     */
    public void sendRetry(String message);

}
