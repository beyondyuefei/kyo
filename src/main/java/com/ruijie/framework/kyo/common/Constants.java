package com.ruijie.framework.kyo.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ruijie.framework.kyo.exception.KyoException;

public class Constants {

	private static Properties KYO_PROP;
	private static final Logger logger = Logger.getLogger(Constants.class);

	public static final String DEFAULT_SESSION_SERIALIZATION = "java";

	public static final String DEFAULT_SESSION_CONFIG = "zookeeper";

	public static final String DEFAULT_SESSION_STORE = "redis";
	// 默认为30分钟
	public static final String DEFAULT_SESSION_TIMEOUT = "30";

	public static final String SESSION_SERIALIZATION_KEY = "serialization";

	public static final String SESSION_CONFIG_KEY = "config";

	public static final String SESSION_STORE_KEY = "store";

	public static final String SESSION_TIMEOUT_KEY = "timeout";

	public static final int DEFAULT_SESSION_ID_LENGTH = 31;

	public static final String SESSION_ID_IDENTITY = "KSESSIONID";

	public static String get(String key) {
		return KYO_PROP.getProperty(key);
	}

	public static void init() {
		if (KYO_PROP != null) {
			return;
		}
		InputStream in = Constants.class.getClassLoader().getResourceAsStream(
				"kyo.properties");
		try {
			if (in == null) {
				throw new KyoException(
						"加载kyo的配置文件出错，请确认在classpath根目录下是否存在: kyo.properties");
			}
			KYO_PROP = new Properties();
			KYO_PROP.load(in);
		} catch (IOException e) {
			logger.error("加载 kyo.properties配置文件信息出错", e);
			throw new KyoException(e.getCause());
		} finally {
			try {
				in.close();
			} catch (IOException ee) {
				throw new KyoException("关闭InputStream出错", ee.getCause());
			}
		}
	}

}
