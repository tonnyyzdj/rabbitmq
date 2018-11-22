package com.zdj.rabbitmq.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdj.rabbitmq.constant.MQConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.StatelessRetryOperationsInterceptorFactoryBean;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangdanjiang
 * @description
 * @date 2018/11/19
 */
@Configuration
public class RabbitMQConfiguration {
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    @Bean
    public StatelessRetryOperationsInterceptorFactoryBean retryOperationsInterceptorFactoryBean() {
        StatelessRetryOperationsInterceptorFactoryBean bean = new StatelessRetryOperationsInterceptorFactoryBean();
        bean.setMessageRecoverer(messageRecoverer());
        return bean;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory containerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean(name = "concurrentContainerFactory")
    public SimpleRabbitListenerContainerFactory concurrentContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(retryOperationsInterceptorFactoryBean().getObject());
        factory.setConcurrentConsumers(30);
        return factory;
    }


    @Bean
    public MessageRecoverer messageRecoverer() {
        return new TmsMessageRecoverer();
    }


    public class TmsMessageRecoverer implements MessageRecoverer {

        private Logger logger = LoggerFactory.getLogger(TmsMessageRecoverer.class);

        private static final String ERROR_MESSAGE_KEY = "errorMsg";
        private static final String ERROR_QUEUE_SUFFIX = ".error";

        @Override
        public void recover(Message message, Throwable throwable) {
            try{
                String msg = new String(message.getBody(), "UTF-8");
                logger.error("RabbitMQ接收消息，重试三次后仍然失败。队列名：{}, 消息内容：{}", message.getMessageProperties().getConsumerQueue(), msg);
                logger.error(throwable.getCause().getMessage(), throwable.getCause());

                String errorMsgBody;
                JSONObject jsonObj = JSON.parseObject(msg);
                if (null == jsonObj) {
                    jsonObj = new JSONObject();
                }
                jsonObj.put(ERROR_MESSAGE_KEY, throwable.getCause().getMessage());
                errorMsgBody = jsonObj.toJSONString();

                String queueName = message.getMessageProperties().getConsumerQueue();
                String errorQueueName = queueName + ERROR_QUEUE_SUFFIX;


//                MqUtil.publishMessage(MQConstant.DEFAULT_EXCHANGE, errorQueueName, errorMsgBody);
            } catch (Exception e) {
                logger.error("异常消息发送到错误队列失败，原始消息内容: " + new String(message.getBody()));
                logger.error(e.getMessage(), e);
            }

        }
    }


    //信道配置
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MQConstant.DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", MQConstant.DEFAULT_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", MQConstant.ORDER_ROUTING);
        Queue queue = new Queue(MQConstant.DEAD_LETTER_QUEUE_NAME,true,false,false,arguments);
        System.out.println(" deadLetterQueue arguments :" + queue.getArguments());
        return queue;
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(directExchange()).with(MQConstant.DEAD_LETTER_QUEUE_NAME);
    }



}
