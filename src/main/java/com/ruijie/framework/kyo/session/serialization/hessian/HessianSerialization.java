package com.ruijie.framework.kyo.session.serialization.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.ruijie.framework.kyo.exception.KyoException;
import com.ruijie.framework.kyo.session.serialization.Serialization;

public class HessianSerialization implements Serialization {

	private static final SerializerFactory factory = new SerializerFactory();
	private static final Logger logger = Logger
			.getLogger(HessianSerialization.class);

	@Override
	public byte[] serialize(HttpSession session) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Hessian2Output write = new Hessian2Output(out);
		write.setSerializerFactory(factory);
		try {
			write.writeObject(session);
			write.flushBuffer();
			return out.toByteArray();
		} catch (IOException e) {
			logger.error("hessian序列化失败", e);
			throw new KyoException(e);
		} finally {
			try {
				out.close();
				write.close();
			} catch (IOException ee) {
				logger.error("关闭流失败.....", ee);
				throw new KyoException(ee);
			}
		}
	}

	@Override
	public HttpSession deserialize(byte[] sessionByte) {
		Hessian2Input read = null;
		ByteArrayInputStream in = new ByteArrayInputStream(sessionByte);
		try {
			read = new Hessian2Input(in);
			read.setSerializerFactory(factory);
			return (HttpSession) read.readObject();
		} catch (IOException e) {
			logger.error("hessian反序列化失败", e);
			throw new KyoException(e);
		} finally {
			try {
				in.close();
				read.close();
			} catch (IOException ee) {
				logger.error("关闭流失败.....", ee);
				throw new KyoException(ee);
			}
		}
	}
}
