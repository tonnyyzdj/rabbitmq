package com.zdj.rabbitmq.constant;

/**
 * @author zhangdanjiang
 * @description MQ列名定义
 * @date 2018/11/19
 */
public final class MQConstant {

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

    //Hello 测试消息队列名称
    public static final String ORDER_QUEUE_NAME = "zdj.order";


}
