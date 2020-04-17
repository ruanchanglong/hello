package com.chengxu.common.executor.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName TaskExecutorConfig
 * @Description 线程池配置
 * @Author ruanchanglong
 * @Date 2020/4/1710:03
 * @Version 1.0
 **/
@Slf4j
@Configuration
public class TaskExecutorConfig implements AsyncConfigurer {
	/**
	 * 使用注解 @Async  使用线程池
	 * ThredPoolTaskExcutor的处理流程 当池子大小小于corePoolSize，就新建线程，并处理请求
	 * 当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去workQueue中取任务并处理
	 * 当workQueue放不下任务时，就新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize，
	 * 就用RejectedExecutionHandler来做拒绝处理
	 * 当池子的线程数大于corePoolSize时，多余的线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁
	 */
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

		// 最小线程数(核心线程数)
		taskExecutor.setCorePoolSize(16);
		// 最大线程数
		taskExecutor.setMaxPoolSize(16);
		//空闲时间 秒
		taskExecutor.setKeepAliveSeconds(60);
		// 等待队列(队列最大长度)
		taskExecutor.setQueueCapacity(600);
		taskExecutor.initialize();
		//拒绝策略 由调用者执行
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return taskExecutor;
	}

	/**
	 * 异步异常处理
	 *
	 * @return
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SpringAsyncExceptionHandler();
	}

	static class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
		@Override
		public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
			log.info("Exception occurs in async method(异步方法中发生异常){}", throwable.getMessage());
		}
	}

}
