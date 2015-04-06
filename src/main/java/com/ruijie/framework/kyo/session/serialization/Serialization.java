package com.ruijie.framework.kyo.session.serialization;

import javax.servlet.http.HttpSession;

import com.ruijie.framework.kyo.common.Constants;
import com.ruijie.framework.kyo.common.extension.Adaptive;
import com.ruijie.framework.kyo.common.extension.SPI;


public interface Serialization {
	
	
    public byte[]  serialize(HttpSession session);
	
    public HttpSession  deserialize(byte[] sessionByte);
}
