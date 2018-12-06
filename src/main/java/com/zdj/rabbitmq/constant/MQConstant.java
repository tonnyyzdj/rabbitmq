package com.zdj.rabbitmq.constant;

/**
 * @author zhangdanjiang
 * @description MQ列名定义
 * @date 2018/11/19
 */
public final class MQConstant {

    //失败重试次数存放key
    public static final String X_REPUBLISH_TIMES_KEY = "retry_times_key";
    //失败重试次数
    public static final Integer X_REPUBLISH_TIMES = 3;
    //失败重试时间间隔,秒数
    public static final Integer X_REPUBLISH_TIME_INTERVAL = 10;

    //exchange name
    public static final String DEFAULT_EXCHANGE = "zdj-exchange";


    //exchange name DirectExchange
    public static final String DIRECT_EXCHANGE = "direct-exchange";
    //DLX QUEUE
    public static final String DEAD_LETTER_QUEUE_NAME = "dlx.queue";
    //DLX 路由
    public static final String DEAD_LETTER_QUEUE_ROUTE = "dlx.queue.route";

    //正常队列
    public static final String QUEUE_NORMAL = "queue.normal";
    //正常队列路由
    public static final String QUEUE_NORMAL_ROUTE = "queue.normal.route";

    public static final String ORDER_ROUTING = "order.route";

    //消息队列名称
    public static final String WORK_QUEUE_NAME = "work.queue";
    //消息队列路由
    public static final String WORK_QUEUE_ROUTE = "work.queue.route";






}
