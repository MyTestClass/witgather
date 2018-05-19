package com.witgather.utils;

/**
 * @author 陈康勇 userid:连接用户的ID type:消息的类型 例如user/login user/register user/modify
 *         content:请求的内容，里面也是一个JSON对象
 */
public class MessageHeader {
	public static final String USER_ID = "userId";
	public static final String TYPE = "type";
	public final static String CONTENT = "content";
}
