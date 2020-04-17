package com.chengxu.common.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @ClassName HelloNetty
 * @Description 高性能处理
 * @Author ruanchanglong
 * @Date 2020/4/1020:20
 * @Version 1.0
 **/
public class HelloNetty {
	public static void main(String[] args) throws InterruptedException {
		//Nio 线程组
		//主线程组 接收客户端连接,但不做任何事
		NioEventLoopGroup boosExecutors = new NioEventLoopGroup();
		//从线程组 主线程组把任务交给我,从线程组做任务
		NioEventLoopGroup workExecutors = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			//Nio  netty服务器的创建  serverBootstrap是一个启动类
			//设置主从线程组
			serverBootstrap.group(boosExecutors,workExecutors)
					//设置Nio双向通道
					.channel(NioServerSocketChannel.class)
					//子处理器 用于处理workExecutors
					//一个channel由多个handler共同组成管道  (pipeline)
					.childHandler(new HelloServerInitializer());
			//启动server 设置启动端口号,同时启动方式为同步
			ChannelFuture sync = serverBootstrap.bind(8081).sync();
			//监听关闭 channel,设置同步方式
			sync.channel().closeFuture().sync();
		} finally {
			boosExecutors.shutdownGracefully();
			workExecutors.shutdownGracefully();
		}

	}
}
