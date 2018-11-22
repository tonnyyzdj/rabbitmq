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
    public static final String DEAD_LETTER_QUEUE_NAME = "zdj.test.dead.letter.queue";

    //DLX repeat QUEUE 死信转发队列 routing-key
    public static final String REPEAT_TRADE_ROUTING_KEY = "zdj.test.repeat.trade";



    public static final String ORDER_ROUTING = "order.route";

    //Hello 测试消息队列名称
    public static final String ORDER_QUEUE_NAME = "zdj.order";


}
