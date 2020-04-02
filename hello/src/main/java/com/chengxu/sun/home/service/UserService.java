package com.chengxu.sun.home.service;

import cn.hutool.core.util.IdUtil;
import com.chengxu.sun.home.bean.User;
import com.chengxu.sun.home.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
	public UserService(UserDao userDao,RedisTemplate<String, Object> redisTemplate) {
		this.userDao = userDao;
		this.redisTemplate = redisTemplate;
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
}
