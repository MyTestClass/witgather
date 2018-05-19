package com.witgather.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.json.JSONObject;

import com.witgather.baseadapter.BaseAdapter;
import com.witgather.utils.DatabasePool;
import com.witgather.utils.MessageHeader;
import com.witgather.utils.MessageType;
import com.witgather.utils.ObjectPool;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author 陈康勇 用于派发请求到具体的Service进行处理
 *
 */
public class ServerRequestHandler extends ChannelHandlerAdapter {
	// 保存用户列表
	private static ConcurrentHashMap<String, ChannelHandlerContext> user = new ConcurrentHashMap<String, ChannelHandlerContext>();
	private static ExecutorService executor = Executors.newCachedThreadPool();
	private static GenericKeyedObjectPool<String, BaseAdapter> objectPool = ObjectPool.objectpool;
	private static final Logger logger  = Logger.getLogger(ServerRequestHandler.class.getName());
	private String userId = null;
	public ServerRequestHandler() {
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		JSONObject request = (JSONObject) msg;
		userId = request.getString(MessageHeader.USER_ID);
		// 如果用户还没有登陆，那么返回登陆请求的JSON
		if (user.get(userId) == null && request.getString(MessageHeader.TYPE)!=MessageType.LOGIN) {
			// 从对象池中获取处理登陆请求的对象
			BaseAdapter handler = objectPool.borrowObject(MessageType.LOGIN);
			handler.init(ctx, request, user,DatabasePool.getConnection(),objectPool);
			executor.execute(handler);
//			从对象池中获取一个处理该请求的对象进行处理
		} else {
			logger.log(Level.INFO, userId);
			/*
			 * userid:连接用户的ID type:消息的类型 例如login register modify content
			 */
			String type = request.getString(MessageHeader.TYPE);
			// 获取请求的类型
			BaseAdapter handler = objectPool.borrowObject(type);
			handler.init(ctx, request, user,DatabasePool.getConnection(),objectPool);
			executor.execute(handler);
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.log(Level.WARNING, "exception", cause);
		ctx.close();
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		user.remove(userId);
	}
}
