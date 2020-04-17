package com.chengxu.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.SocketAddress;

/**
 * @ClassName CustomHandler
 * @Description 自定义助手类(handler)
 * @Author ruanchanglong
 * @Date 2020/4/1020:57
 * @Version 1.0
 **/
//SimpleChannelInboundHandler  对于请求来说 相当于入站
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel注册");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel移除");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel活跃");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel不活跃");
		super.channelInactive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel读取完毕");
		super.channelReadComplete(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("channel用户事件触发");
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel可写更改");
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("channel异常(应相应的关闭)");
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel助手类添加");
		super.handlerAdded(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel助手类移除");
		super.handlerRemoved(ctx);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) throws Exception {
		//channelHandlerContext  上下文对象
		//获取channel
		Channel channel = channelHandlerContext.channel();
		if (msg instanceof HttpRequest) {

			//显示客户端的远程地址
			SocketAddress address = channel.remoteAddress();
			System.out.println("客户端远程地址:" + address);

			//定义发送消息
			ByteBuf byteBuf = Unpooled.copiedBuffer("hello netty", CharsetUtil.UTF_8);
			//构建响应,增加数据类型和数据长度
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
			//响应到客户端
			channelHandlerContext.writeAndFlush(response);

		}
	}
}
