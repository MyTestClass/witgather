package com.witgather.coder;

import java.util.List;

import org.json.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class JSONEncoder extends MessageToMessageEncoder<JSONObject>{
	@Override
	protected void encode(ChannelHandlerContext ctx, JSONObject msg, List<Object> out) throws Exception {
		out.add(new String(msg.toString().getBytes()));
	}
	
}

