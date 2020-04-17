package com.chengxu.sun.user.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName ThredTest
 * @Description TODO
 * @Author EDZ
 * @Date 2020/4/1711:29
 * @Version 1.0
 **/
@Service
public class ThredTest {


	@Async
	public void test(){
		System.out.println("执行 了"+Thread.currentThread().getName());
	}
}
