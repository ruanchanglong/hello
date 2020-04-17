package com.chengxu.sun.home.service;

import cn.hutool.core.util.IdUtil;
import com.chengxu.common.rabbitmq.producer.DelayRabbitSend;
import com.chengxu.common.rabbitmq.producer.RabbitMqSend;
import com.chengxu.common.util.SpringUtils;
import com.chengxu.sun.home.bean.User;
import com.chengxu.sun.home.dao.UserDao;
import com.chengxu.sun.user.service.ThredTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author EDZ
 * @Date 2020/4/112:59
 * @Version 1.0
 **/
@Service
@Slf4j
public class UserService {

	private final UserDao userDao;
	private final RedisTemplate<String,Object> redisTemplate;
	private final RabbitMqSend rabbitMqSend;
	private final DelayRabbitSend delayRabbitSend;
	private final ThredTest thredTest;
    @Autowired
	public UserService(UserDao userDao, RedisTemplate<String, Object> redisTemplate, RabbitMqSend rabbitMqSend, DelayRabbitSend delayRabbitSend, ThredTest thredTest) {
		this.userDao = userDao;
		this.redisTemplate = redisTemplate;
		this.rabbitMqSend = rabbitMqSend;
		this.delayRabbitSend = delayRabbitSend;
		this.thredTest = thredTest;
	}

	/**
	 * 注册用户
	 * @param user
	 */
	public void newUser(User user) {
		redisTemplate.opsForValue().set("a","a");
		/*User user = new User();
		user.setId(IdUtil.fastUUID());
		user.setName("lail");
		userDao.newUser(user);*/
	}

	public static void main(String[] args) {
		String uuid = IdUtil.fastUUID();
		System.out.println(uuid
		);
	}

	public void sendMq() {
		//rabbitMqSend.sendMq("来了","exchange","key1");
		//延时队列
		delayRabbitSend.sendDelayMq("来咯","DELAY_EXCHANGE","DELAY_ROUTING_KEY");

	}

	/**
	 * 多线程测试
	 */
	public void executor() throws InterruptedException {
		//UserService bean = SpringUtils.getBean(UserService.class);
		mainThread();
		thredTest.test();
	}
    @Async
	public void childThread() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println("子线程"+Thread.currentThread().getId());
	}

	private void mainThread() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println("主线程"+Thread.currentThread().getName());
	}
}
