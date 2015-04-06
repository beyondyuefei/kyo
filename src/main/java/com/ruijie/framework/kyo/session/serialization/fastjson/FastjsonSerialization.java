package com.ruijie.framework.kyo.session.serialization.fastjson;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.framework.kyo.session.serialization.Serialization;

public class FastjsonSerialization implements Serialization {

	@Override
	public byte[] serialize(HttpSession session) {
		return JSON.toJSONBytes(session,SerializerFeature.WriteClassName);
	}

	@Override
	public HttpSession deserialize(byte[] sessionByte)  {
		return JSON.parseObject(sessionByte,HttpSession.class);
	}

}
