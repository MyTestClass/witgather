package com.witgather.baseadapter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.json.JSONObject;

import com.witgather.utils.MessageHeader;

import io.netty.channel.ChannelHandlerContext;

public abstract class BaseAdapter implements Runnable{
	private Connection conn;
	private JSONObject jsonObject;
	private ChannelHandlerContext ctx;
	private ConcurrentHashMap<String, ChannelHandlerContext> user;
	private GenericKeyedObjectPool<String,BaseAdapter> objectpool;
	public BaseAdapter() {}
	/**
	 * @param ctx 通道上下文
	 * @param jsonObject 客户端的请求
	 * @param user 保存所有在线用户的通道上下文
	 * @param conn 从数据库连接池中获取的连接
	 * @param objectpool 对象池，用于使用完成后，将对象归还到对象池中
	 */
	public void init(ChannelHandlerContext ctx,
			JSONObject jsonObject,
			ConcurrentHashMap<String, ChannelHandlerContext> user,
			Connection conn,
			GenericKeyedObjectPool<String,BaseAdapter> objectpool
			) {
		this.ctx =ctx;
		this.jsonObject=jsonObject;
		this.user = user;
		this.conn = conn;
		this.objectpool = objectpool;
	}
	
	@Override
	public void run() {
		JSONObject result=null;
		try {
			if(conn==null)
				return;
			result = handler(ctx,conn,jsonObject,user);
			if(result!=null)
				ctx.writeAndFlush(result);
		}finally {
//			将对象归还到对象池中
			objectpool.returnObject(jsonObject.getString(MessageHeader.TYPE), this);
			try {
//				将数据库连接归还到池中
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			jsonObject = null;
			result = null;
			ctx.close();
		}
	}

	/**
	 * @param conn 数据库连接
	 * @param jsonObject 客户端发送的请求参数
	 * @param user 用户容器
	 * @return 返回数据给客户端
	 */
	protected abstract JSONObject handler(ChannelHandlerContext ctx,Connection conn,
			JSONObject jsonObject,
			ConcurrentHashMap<String, ChannelHandlerContext> user);
}
