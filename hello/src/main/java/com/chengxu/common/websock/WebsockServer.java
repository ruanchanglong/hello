package com.chengxu.common.websock;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @ClassName WebsockServer
 * @Description TODO
 * @Author ruanchanglong
 * @Date 2020/4/1022:25
 * @Version 1.0
 **/
public class WebsockServer {
	public static void main(String[] args) throws InterruptedException {

		NioEventLoopGroup mainGroup = new NioEventLoopGroup();
		NioEventLoopGroup subGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(mainGroup,subGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(null);
			ChannelFuture future = server.bind(8088).sync();
			future.channel().closeFuture().sync();
		} finally {
			mainGroup.shutdownGracefully();
			subGroup.shutdownGracefully();
		}

	}
}
