package com.chengxu.common.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName queueListener
 * @Description TODO
 * @Author EDZ
 * @Date 2020/4/1518:20
 * @Version 1.0
 **/
@Slf4j
@Component
public class queueListener {
	@RabbitListener(containerFactory = "oneContainerFactory",queues = {"queue"})
	public void orderDelayQueue(Object msg, Message message, Channel channel) {
		//order  发送的参数
		log.info("消息是什么东西{}",message.toString());//交换机,路由,队列,设置的延迟时间,等信息
		log.info("Channel的什么东西{}",channel.toString());//mq消息连接路径,端口号等信息
		log.info("###########################################");
		log.info("【orderDelayQueue 监听的消息】 - 【时间】 - [{}]- 内容】 - [{}]",  new Date(), msg.toString());


	}
}
