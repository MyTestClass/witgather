package com.witgather.service.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.witgather.baseadapter.BaseAdapter;
import com.witgather.utils.MessageHeader;
import com.witgather.utils.MessageType;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author 陈康勇 处理用户的登陆请求
 */
public class LoginHandler extends BaseAdapter {

	@Override
	protected JSONObject handler(ChannelHandlerContext ctx, Connection conn, JSONObject jsonObject,
			ConcurrentHashMap<String, ChannelHandlerContext> user) {
		JSONObject result = new JSONObject();
		result.put(MessageHeader.TYPE, MessageType.LOGIN);
		try {
			PreparedStatement ps = conn.prepareStatement("select password from user where phonenumber=?");
			ps.setString(1, jsonObject.getString(Login.USERID));
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				// 用户存在，且密码正确
				if (rs.getString(1).equals(jsonObject.get(Login.PASSWORD))) {
					result.put(Login.RESULT, Login.LOGIN_SUCCESS);
					// 将用户登陆成功的信息保存起来
					user.put(jsonObject.getString(Login.USERID), ctx);
					// 用户存在，但是密码错误
				} else {
					result.put(Login.RESULT, Login.PASSWORD_ERROR);
				}
				// 用户不存在
			} else {
				result.put(Login.RESULT, Login.USERID_NOEXISTS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
