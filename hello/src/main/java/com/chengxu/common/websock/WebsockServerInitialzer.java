package com.chengxu.common.websock;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @ClassName WebsockServerInitialzer
 * @Description 初始化器  channel注册后,会执行相应的初始化方法.
 * @Author ruanchanglong
 * @Date 2020/4/1022:32
 * @Version 1.0
 **/
public class WebsockServerInitialzer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//websocke基于http  编解码器
		pipeline.addLast(new HttpServerCodec());
		//写大数据流支持
		pipeline.addLast(new ChunkedWriteHandler());
		//创建消息最大长度
		pipeline.addLast(new HttpObjectAggregator(1024*64));
	}
}
