package com.chengxu.common.rabbitmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @ClassName OneListener
 * @Description 监听
 * @Author
 * @Date 2020/4/711:02
 * @Version 1.0
 **/
@Slf4j
@Component
public class OneListener {
	@RabbitHandler
	@RabbitListener(containerFactory = "oneContainerFactory",bindings = {
			@QueueBinding(
					value = @Queue(name = "queue1",arguments={
							@Argument(name = "x-message-ttl", value = "300000", type = "java.lang.Integer"),
							@Argument(name = "x-dead-letter-exchange", value = "exchange.dlx"),
							@Argument(name = "x-dead-letter-routing-key", value = "key.dlx"),
					}),

					exchange = @Exchange(name = "exchange", type = ExchangeTypes.TOPIC),
					key = "key1"
			) }
	)
	public void handlerPurchaseMessage(Message message) {
		log.info("Rabbit purchase message: {}", message);

	}

}


