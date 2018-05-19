package com.witgather.service.user;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.witgather.baseadapter.BaseAdapter;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author 陈康勇
 *	修改用户的个人信息
 */
public class ModifyInformation extends BaseAdapter{

	@Override
	protected JSONObject handler(ChannelHandlerContext ctx, Connection conn, JSONObject jsonObject,
			ConcurrentHashMap<String, ChannelHandlerContext> user) {
		return null;
	}

}
