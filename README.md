1.延迟队列Demo  
    *  通过DLX和TTL实现延迟队列的功能
    * 消息变成死信一般是由于以下几种情况:
    * 消息被拒绝 (Basic.Reject/Basic .Na ck) ，井且设置 requeue 参数为 false;
    * 消息过期;
    * 令队列达到最大长度。
    * 生产者首先发送一条携带路由键为 " rk " 的消息，然后经过交换器exchange .normal 顺利地存储到队列 queue.normal 中 。由于队列 queue.normal 设置了过期时间为10s ， 在这 10s 内没有消费者消费这条消息，那么判定这条消息为过期。由于设置了 DLX ， 过期
    * 之时 ，消息被丢给交换器 exchange.dlx 中，这时找到与 exchange.dlx 匹配的队列 queue .dlx ， 最后消息被存储在 queue.dlx 这个死信队列中。
    * 消费者订阅的并非是 queue.normal 这个队列，而是 queue.dlx 这个队列 。当消息从 queue.normal 这个队列中过期之后被存入 queue.dlx 这个队列中，消费者就恰巧消费到了延迟 10 秒的这条消息 。
    
2.延时重试队列Demo
  <img src="https://camo.githubusercontent.com/970eeb6ddbb663d6a180a3d0fc4b5a3c4e85f874/68747470733a2f2f6f61797273736a70612e716e73736c2e636f6d2f7878782e6a7067"/>
  生产者发布消息到主Exchange
  主Exchange根据Routing Key将消息分发到对应的消息队列
  多个消费者的worker进程同时对队列中的消息进行消费，因此它们之间采用“竞争”的方式来争取消息的消费
  消息消费后，不管成功失败，都要返回ACK消费确认消息给队列，避免消息消费确认机制导致重复投递，同时，如果消息处理成功，则结束流程，否则进入重试阶段
  如果重试次数小于设定的最大重试次数（3次），则将消息重新投递到Retry Exchange的重试队列
  重试队列不需要消费者直接订阅，它会等待消息的有效时间过期之后，重新将消息投递给Dead Letter Exchange，我们在这里将其设置为主Exchange，实现延时后重新投递消息，这样消费者就可以重新消费消息
  如果三次以上都是消费失败，则认为消息无法被处理，直接将消息投递给Failed Exchange的Failed Queue，这时候应用可以触发报警机制，以通知相关责任人处理
  等待人工介入处理（解决bug）之后，重新将消息投递到主Exchange，这样就可以重新消费了
      