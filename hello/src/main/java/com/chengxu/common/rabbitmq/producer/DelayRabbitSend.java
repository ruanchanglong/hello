package com.chengxu.common.rabbitmq.producer;

import com.chengxu.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @ClassName DelayRabbitSend
 * @Description 延时队列发送
 * @Author ruanchanglong
 * @Date 2020/4/169:03
 * @Version 1.0
 **/
@Slf4j
@Component
public class DelayRabbitSend {

	private final RabbitTemplate rabbitTemplate;

	public DelayRabbitSend(@Qualifier("oneRabbitTemplate")RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendDelayMq(Object msg,String exchange,String routingKey){
		log.info("exchange: {}, routing key: {}", exchange, routingKey);
		log.info("Send message : {}", JsonUtils.toJson(msg));
		rabbitTemplate.convertAndSend(exchange, routingKey, msg, message -> {
			// 如果配置了 params.put("x-message-ttl", 5 * 1000); 那么这一句也可以省略,具体根据业务需要是声明 Queue 的时候就指定好延迟时间还是在发送自己控制时间
			//  todo 根据属性配置延迟时间 灵活 配置  order.getExpirationTime()
			message.getMessageProperties().setExpiration("10000");
			return message;
		});
	}
}
