package com.chengxu.sun.home.controller;

import com.chengxu.common.bean.ResponseBean;
import com.chengxu.sun.home.bean.User;
import com.chengxu.sun.home.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @ClassName WelcomeController
 * @Description 欢迎到来
 * @Author ruanchanglong
 * @Date 2020/3/31 10:26
 * @Version 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/home")
public class WelcomeController {
    private final UserService userService;

    @Autowired
	public WelcomeController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/welcome")
	public ResponseBean welcome(@RequestBody User user){
		userService.newUser(user);
		return new ResponseBean(null);
	}

	@GetMapping("/go")
	public ResponseBean go(){

		return new ResponseBean("gogogog");
	}

	@GetMapping("/mq")
	public ResponseBean mq(){
		userService.sendMq();
		return new ResponseBean<>(null);
	}
	@GetMapping("/executor")
	public ResponseBean executor() throws InterruptedException {
		userService.executor();
		return new ResponseBean<>(null);
	}

	public static void main(String[] args) {

	}

}
