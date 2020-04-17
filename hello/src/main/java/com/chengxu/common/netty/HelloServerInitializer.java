package com.chengxu.common.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @ClassName HelloServerInitializer
 * @Description 初始化器  channel注册后,会执行相应的初始化方法.
 * @Author ruanchanglong
 * @Date 2020/4/1020:46
 * @Version 1.0
 **/
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		//通过socketChannel 去获取对应的管道
		ChannelPipeline pipeline = socketChannel.pipeline();
		//多个handler组成(可理解为拦截器)
		//通过管道,添加handler
		//HttpServerCodec是由netty自己提供的助手类,可以理解拦截器
		//当请求到服务器,我们需要做解码,响应到客户端做编码
		pipeline.addLast("HttpServerCodec",new HttpServerCodec());
		//添加自定义助手类,返回hello netty
		pipeline.addLast("customHandler",new CustomHandler());
	}
}
