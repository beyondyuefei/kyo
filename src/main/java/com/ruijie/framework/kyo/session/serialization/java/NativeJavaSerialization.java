package com.ruijie.framework.kyo.session.serialization.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ruijie.framework.kyo.exception.KyoException;
import com.ruijie.framework.kyo.session.serialization.Serialization;

public class NativeJavaSerialization implements Serialization {

	private static final Logger logger = Logger
			.getLogger(NativeJavaSerialization.class);

	@Override
	public byte[] serialize(HttpSession session) {
		ByteArrayOutputStream b = null;
		ObjectOutputStream out = null;
		try {
			b = new ByteArrayOutputStream();
			out = new ObjectOutputStream(b);
			out.writeObject(session);
			return b.toByteArray();
		} catch (IOException e) {
			logger.error("java序列化失败.....", e);
			throw new KyoException(e);
		} finally {
			try {
				b.close();
				out.close();
			} catch (IOException ee) {
				logger.error("关闭流失败.....", ee);
				throw new KyoException(ee);
			}
		}
	}

	@Override
	public HttpSession deserialize(byte[] sessionByte) {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new ByteArrayInputStream(sessionByte));
			return (HttpSession) in.readObject();
		} catch (IOException e1) {
			logger.error("java反序列化失败.....", e1);
			throw new KyoException(e1);
		} catch (ClassNotFoundException e2) {
			logger.error("java反序列化 ClassNotFoundException 失败.....", e2);
			throw new KyoException(e2);
		}

		finally {
			try {
				in.close();
			} catch (IOException e3) {
				logger.error("关闭流失败.....", e3);
				throw new KyoException(e3);
			}
		}
	}

}
