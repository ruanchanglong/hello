package com.chengxu.common.rabbitmq.producer;

import com.chengxu.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @ClassName RabbitMqSend
 * @Description 发送
 * @Author ruanchanglong
 * @Date 2020/4/7 10:47
 * @Version 1.0
 **/
@Slf4j
@Component
public class RabbitMqSend {

	private final RabbitTemplate rabbitTemplate;

	public RabbitMqSend(@Qualifier("oneRabbitTemplate") RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendMq(Object msg,String exchange,String routingKey){
		log.info("exchange: {}, routing key: {}", exchange, routingKey);
		log.info("Send message : {}", JsonUtils.toJson(msg));
		rabbitTemplate.convertAndSend(exchange, routingKey, msg);
	}
}
