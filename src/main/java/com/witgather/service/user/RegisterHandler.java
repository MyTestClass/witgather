package com.witgather.service.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.witgather.baseadapter.BaseAdapter;
import com.witgather.utils.MessageHeader;
import com.witgather.utils.MessageType;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author 陈康勇
 *	处理用户的注册请求
 *	返回的JSON数据：注册成功、注册失败、已经注册
 */
public class RegisterHandler extends BaseAdapter{

	@Override
	protected JSONObject handler(ChannelHandlerContext ctx, Connection conn, JSONObject jsonObject,
			ConcurrentHashMap<String, ChannelHandlerContext> user) {
		JSONObject result = new JSONObject();
		result.put(MessageHeader.TYPE, MessageType.REGISTER);
		try {
			PreparedStatement ps = conn.prepareStatement("insert into user(schoolid,userid,phonenumber,"
					+ "password,name,majorid) values(?,?,?,?,?,?)");
			ps.setString(1, jsonObject.getString(Register.SCHOOLID));
			ps.setString(2, jsonObject.getString(Register.USERID));
			ps.setString(3, jsonObject.getString(Register.PHONENUMBER));
			ps.setString(4, jsonObject.getString(Register.PASSWORD));
			ps.setString(5, jsonObject.getString(Register.NAME));
			ps.setString(6, jsonObject.getString(Register.MAJOR));
			ps.execute();
		} catch (SQLException e) {
			result.put(Register.STATE, Register.REGISTERFAIL);
			e.printStackTrace();
		}
		return result;
	}

}
