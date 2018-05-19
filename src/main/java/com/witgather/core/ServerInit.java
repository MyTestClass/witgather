package com.witgather.core;

import com.witgather.coder.JSONDecoder;
import com.witgather.coder.JSONEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 
 */
public class ServerInit {
	/**
	 * @param port
	 * 需要监听的端口号
	 */
	public void bindPort(int port) {
		// 创建两个线程组，一个用于接收请求，一个用于处理请求（解码和编码等）
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		// 启动类
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				//最大的连接数
				.option(ChannelOption.SO_BACKLOG, 2048)
				//配置请求经过的Handler
				.childHandler(new ChildChannelHandler());
		try {
			// 等待绑定端口成功
			ChannelFuture f = b.bind(port).sync();
			// 等待关闭端口成功
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	/**
	 * 
	 */
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel e) throws Exception {
			e.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 2, 0, 2))
					.addLast(new LengthFieldPrepender(2)).
					addLast(new StringDecoder()).
					addLast(new StringEncoder())
					.addLast(new JSONDecoder()).
					addLast(new JSONEncoder()).
					addLast(new ServerRequestHandler());
			
		}

	}
}
