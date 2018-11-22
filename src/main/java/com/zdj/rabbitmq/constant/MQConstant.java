package com.zdj.rabbitmq.constant;

/**
 * @author zhangdanjiang
 * @description MQ列名定义
 * @date 2018/11/19
 */
public final class MQConstant {

    //exchange name
    public static final String DEFAULT_EXCHANGE = "zdj-exchange";

    //DLX QUEUE
    public static final String DEFAULT_DEAD_LETTER_QUEUE_NAME = "zdj.test.dead.letter.queue";

    //DLX repeat QUEUE 死信转发队列
    public static final String DEFAULT_REPEAT_TRADE_QUEUE_NAME = "zdj.test.repeat.trade.queue";



    public static final String ORDER_ROUTING = "order.route";

    //Hello 测试消息队列名称
    public static final String ORDER_QUEUE_NAME = "zdj.order";


}
